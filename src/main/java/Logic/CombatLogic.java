package Logic;

import java.util.ArrayList;
import java.util.Collections;

import Domain.*;
import Domain.Scenario.Combat;
import IO.*;
import Services.Locator;

public class CombatLogic {
    private ActionAIEnemy ai;
    private Player hero;
    private ArrayList<Enemy> villainList;
    private ArrayList<Unit> unitList;

    public CombatLogic() {
        this.ai = new ActionAIEnemy();
    }

    public ArrayList<Unit> getUnitList() {
        return unitList;
    }

    public void setUnitList(ArrayList<Unit> unitList) {
        this.unitList = unitList;
    }

    public void init(Player hero, Combat combat) {
        this.setVillains(combat.villainList());
        this.setHero(hero);
        this.setUnitList(combat.unitList(hero));
    }

    public void setVillains(ArrayList<Enemy> villainList) {
        this.villainList = villainList;
    }

    public void setHero(Player hero) {
        this.hero = hero;
    }

    public ArrayList<Unit> initiativeOrder() {
        Collections.sort(unitList);
        return unitList;
    }

    public Attack chosenAttack(Unit enemy) {
        return ai.decideSpecial(enemy, hero, enemy.readySpecials());
    }

    public Invocation chosenInvocation(Unit enemy) {
        return ai.decideInvocation(enemy, hero, enemy.readyInvocations());
    }

    public boolean handleSpecialHero(Unit hero, Special sp) throws InterruptedException {
        if (sp.targetsAll()) {
            boolean ok = Locator.comIO().confirmTargetAll(hero, sp, villainList);
            if (ok) {
                specialAll(hero, sp);
                return true;
            }
        } else {
            Enemy e = Locator.comIO().selectEnemy(hero, sp, villainList);
            if (e != null) {
                specialSingle(hero, e, sp);
                return true;
            }
        }
        return false;
    }

    public boolean handleInvocationHero(Unit hero, Invocation inv) throws InterruptedException {
        if (inv.isTargetsSelf() && hero.hasStatus(inv.getStatus())) {
            Locator.comIO().alreadyHaveInvokeStatus();
            return false;
        }
        boolean ok = Locator.comIO().confirmInvocationUse(hero, inv, villainList);
        if (ok) {
            invoke(hero, inv);
        }
        return ok;
    }

    public void useDeathDefianceIfAvailable(Unit u) throws InterruptedException {
        Player hero = (Player) u;
        if (hero.isAlive() == false && hero.getDeathDefiances() > 0) {
            hero.setHp((int)Math.round(hero.getMaxHp()*0.5));
            hero.setAlive(true);
            hero.setDeathDefiances(hero.getDeathDefiances() - 1);
            Locator.comIO().usedDeathDefiance(hero);
        }
    }

    public void processStatusDamage(Unit unit) throws InterruptedException {
        ArrayList<Status> wornOutStatuses = new ArrayList<>();
        ArrayList<Status> damagingStati = new ArrayList<>();
        ArrayList<Status> healingStati = new ArrayList<>();
        for (int i = 0; i < unit.getStati().size(); i++) {
            Status copy = new Status(unit.getStati().get(i));
            int hpPerTurn = copy.getHpPerTurn();
            if (hpPerTurn < 0) {
                damagingStati.add(copy);
            } else if (hpPerTurn > 0) {
                healingStati.add(copy);
            }
            boolean statusWoreOut = unit.getStati().get(i).countdown();
            if (statusWoreOut) {
                wornOutStatuses.add(copy);
                unit.removeStatus(0);
                i--;
            }
        }
        for (Status s: damagingStati) {
            if (unit.isAlive()) {
                int prevHp = unit.getHp();
                int damage = s.getHpPerTurn();
                int currentHp = prevHp + damage;
                unit.setHp(currentHp);
                Locator.comIO().damageFromStatus(unit, -damage, s);
                if (!unit.isAlive()) {
                    int differenceFromLife = 1 + currentHp;
                    for (int i = 0; i < healingStati.size(); i++) {
                        if (!unit.isAlive() && healingStati.get(i).getHpPerTurn() >= differenceFromLife) {
                            unit.setHp(currentHp + healingStati.get(i).getHpPerTurn());
                            unit.setAlive(true);
                            Locator.comIO().savedFromDeath(unit, healingStati.get(i));
                            healingStati.remove(i);
                            break;
                        }
                    }
                    if (unit instanceof Player) {
                        useDeathDefianceIfAvailable(unit);
                    }
                }
                if (!unit.isAlive()) {
                    Locator.comIO().deathByStatus(unit, s);
                }
            }
        }
        if (unit.isAlive()) {
            for (int i = 0; i < healingStati.size(); i++) {
                unit.setHp(unit.getHp() + healingStati.get(i).getHpPerTurn());
                Locator.comIO().healingFromStatus(unit, healingStati.get(i).getHpPerTurn(), healingStati.get(i));
            }
            for (int i = 0; i < wornOutStatuses.size(); i++) {
                Locator.comIO().announceStatusWoreOut(unit, wornOutStatuses.get(i));
            }
            if (!wornOutStatuses.isEmpty() || !damagingStati.isEmpty() || !healingStati.isEmpty()) {
                Locator.comIO().printUnitState(unit);
            }
        }
        
    }

    public void processCooldowns(Unit unit) throws InterruptedException {
        for (Special sp: unit.getSpecials()) {
            if (!sp.isReady()) {
                sp.advanceCooldown();
                if (sp.isReady()) {
                    Locator.comIO().announceSpecialReady(sp);
                }
            }
        }
    }

    public void invoke(Unit unit, Invocation inv) throws InterruptedException {
        inv.setUsed(true);
        double chance = inv.getChance();
        Locator.comIO().invoke(unit, inv);
        if (inv.isTargetsSelf()) {
            boolean success = true;
            Locator.comIO().announceEffectStatusChances(inv.getEffect(), CombatCalculation.calcEffectChance(unit, inv.getEffect(), inv.getChance()), inv.getStatus(), CombatCalculation.calcStatusChance(unit, inv.getStatus(), inv.getChance()));
            if (inv.getEffect() != null) {
                success = applyEffect(unit, inv.getEffect(), chance);
            } 
            if (inv.getStatus() != null && success) {
                applyStatus(unit, inv.getStatus(), chance);
            }
        } else {
            if (unit instanceof Player) {
                for (Enemy e: villainList) {
                    boolean success = true;
                    if (inv.getEffect() != null) {
                        success = applyEffect(e, inv.getEffect(), chance);
                    } 
                    if (e.isAlive() && inv.getStatus() != null && success) {
                        applyStatus(e, inv.getStatus(), chance);
                    }
                }
            } else {
                Locator.comIO().announceEffectStatusChances(inv.getEffect(), CombatCalculation.calcEffectChance(hero, inv.getEffect(), inv.getChance()), inv.getStatus(), CombatCalculation.calcStatusChance(hero, inv.getStatus(), inv.getChance()));
                boolean success = true;
                if (inv.getEffect() != null) {
                    success = applyEffect(hero, inv.getEffect(), chance);
                } 
                if (hero.isAlive() && inv.getStatus() != null && success) {
                    applyStatus(hero, inv.getStatus(), chance);
                }
            }
        }
    }

    public boolean applyEffect(Unit target, Effect e, double chance) throws InterruptedException {
        // TODO TARKISTUS SIITÄ ONKO HEALMOD, HEALBLOCK YMS KÄYTÖSSÄ, BUFF/DEBUFFBLOCK
        // TODO TARKISTUS ONKO RES TÄLLE EFEKTILLE
        chance = CombatCalculation.calcEffectChance(target, e, chance);
        if (Locator.rng().nextDouble() > chance) {
            Locator.comIO().resistsEffect(target, e);
            return false;
        }
        Locator.comIO().announceEffect(target, e);
        if (e.getHpChange() > 0) {
            int hpChange = e.getHpChange();
            // int hpChange = target.getHp();
            target.setHp(target.getHp() + e.getHpChange());
            // hpChange -= target.getHp();
            if (hpChange < 0) {
                hpChange = -hpChange;
            }
            Locator.comIO().healing(target, hpChange);
        } else if (e.getHpChange() < 0) {
            int hpChange = e.getHpChange();
            // int hpChange = target.getHp();
            target.setHp(target.getHp() + e.getHpChange());
            // hpChange -= target.getHp();
            if (hpChange < 0) {
                hpChange = -hpChange;
            }
            Locator.comIO().damage(target, hpChange);
        }
        if (!target.isAlive() && target instanceof Player) {
            useDeathDefianceIfAvailable(target);
        }
        if (!target.isAlive()) {
            Locator.comIO().announceDeath(target);
        }
        if (e.getCooldownChange() != 0) {
            for (Special sp: target.getSpecials()) {
                sp.setTurnsLeft(sp.getTurnsLeft() + e.getCooldownChange());
            }
            Locator.comIO().announceCooldownChange(target, e);
        }
        if (e.isClearBuffs()) {
            for (int i = 0; i < target.getStati().size(); i++) {
                if (target.getStati().get(i).isBuff()) {
                    Locator.comIO().announceStatusWoreOut(target, target.getStati().get(i));
                    target.removeStatus(target.getStati().get(i));
                    i--;
                }
            }
        }
        if (e.isClearDebuffs()) {
            for (int i = 0; i < target.getStati().size(); i++) {
                if (!target.getStati().get(i).isBuff()) {
                    Locator.comIO().announceStatusWoreOut(target, target.getStati().get(i));
                    target.removeStatus(target.getStati().get(i));
                    i--;
                }
            }
        }
        return true;
    }

    private boolean applyStatus(Unit unit, Status s, double chance) throws InterruptedException {
        chance = CombatCalculation.calcStatusChance(unit, s, chance);
        // Checks the chance to set the Status, returning if fails
        if (Locator.rng().nextDouble() > chance) {
            Locator.comIO().resistsStatus(unit, s);
            return false;
        }
        // If chance succeeded, proceeds to set the Status if possible
        boolean addedStatus = unit.addStatus(new Status(s));
        Locator.comIO().announceStatus(addedStatus, unit, s);
        return addedStatus;
    }

    public void attackSingle(Unit unit, Unit target, Attack atk) throws InterruptedException {

        Locator.comIO().singleTarget(unit, target, atk);

        double hitChance = CombatCalculation.calcAtkHr(unit, target, atk);
        double critChance = CombatCalculation.calcAtkCr(unit, target, atk);

        Locator.comIO().announceChances(hitChance, critChance);

        attack(hitChance, critChance, unit, target, atk);
    }

    public void specialSingle(Unit unit, Unit target, Special sp) throws InterruptedException {
        sp.startCooldown();
        Locator.comIO().singleTarget(unit, target, sp);

        double hitChance = CombatCalculation.calcAtkHr(unit, target, sp);
        double critChance = CombatCalculation.calcAtkCr(unit, target, sp);

        Locator.comIO().announceChances(hitChance, critChance);
        boolean effectOrStatusOnTarget = (sp.getTargetEffect() != null || sp.getTargetStatus() != null);
        boolean hit = specialAttack(hitChance, critChance, unit, target, sp);
        if (hit & target.isAlive() && effectOrStatusOnTarget) {
            Locator.comIO().announceEffectStatusChances(sp.getTargetEffect(), CombatCalculation.calcEffectChance(target, sp.getTargetEffect(), sp.getChance()), sp.getTargetStatus(), CombatCalculation.calcStatusChance(target, sp.getTargetStatus(), sp.getChance()));
        }
        if (hit && effectOrStatusOnTarget && target.isAlive()) {
            handleSpecialEffectStatus(unit, target, sp);
        } else if (hit && !effectOrStatusOnTarget) {
            handleSpecialEffectStatusOnSelf(unit, sp);
        } else if (hit && sp.getTargetStatus() == null && sp.getSelfStatus() != null) {
            handleSpecialEffectStatusOnSelf(unit, sp);
        }
    }

    public void specialAll(Unit hero, Special sp) throws InterruptedException {
        sp.startCooldown();
        Locator.comIO().allTarget(hero, sp);
        boolean hitAtLeastOne = false;
        boolean effectOrStatusOnTarget = (sp.getTargetEffect() != null || sp.getTargetStatus() != null);
        boolean setOnSelf = false;
        for (Enemy e: getVillainList()) {
            if (specialAttack(CombatCalculation.calcAtkHr(hero, e, sp), CombatCalculation.calcAtkCr(hero, e, sp), hero, e, sp)) {
                hitAtLeastOne = true;
                if (effectOrStatusOnTarget && e.isAlive()) {
                    handleSpecialEffectStatus(hero, e, sp);
                } else if (effectOrStatusOnTarget) {
                    handleSpecialEffectStatusOnSelf(hero, sp);
                    setOnSelf = true;
                }
            }
        }
        if (!effectOrStatusOnTarget && hitAtLeastOne) {
            handleSpecialEffectStatusOnSelf(hero, sp);
        } else if (!setOnSelf && hitAtLeastOne && sp.getTargetStatus() == null && sp.getSelfStatus() != null) {
            handleSpecialEffectStatusOnSelf(hero, sp);
        }
    }

    private boolean attack(double hitChance, double critChance, Unit unit, Unit target, Attack atk) throws InterruptedException {
        boolean hit = false;
        if (Locator.rng().nextDouble() < hitChance) {
            hit = true;
        }
        if (hit) {
            boolean crit = false;
            if (Locator.rng().nextDouble() < critChance) {
                crit = true;
            }
            int damage = unit.getStr() + atk.getPwr();
            if (crit) {
               damage *= 2;
            }
            damage -= target.getDef();
            if (damage < 0) {
                damage = 0;
            }
            target.setHp(target.getHp() - damage);
            Locator.comIO().announceDamage(unit, target, atk, damage, crit);
            if (!target.isAlive() && target instanceof Player) {
                useDeathDefianceIfAvailable(target);
            }
            if (!target.isAlive()) {
                Locator.comIO().announceDeath(target);
            }
        } else {
            Locator.comIO().missedAttack(unit, target);
        }
        return hit;
    }

    public boolean specialAttack(double hitChance, double critChance, Unit unit, Unit target, Special sp) throws InterruptedException {
        boolean hit = false;
        if (Locator.rng().nextDouble() < hitChance) {
            hit = true;
        }
        if (hit) {
            boolean crit = false;
            if (Locator.rng().nextDouble() < critChance) {
                crit = true;
            }
            int damage = unit.getStr() + sp.getPwr();
            if (crit) {
               damage *= 2;
            }
            damage -= target.getDef();
            if (damage < 0) {
                damage = 0;
            }
            target.setHp(target.getHp() - damage);
            Locator.comIO().announceDamage(unit, target, sp, damage, crit);
            if (!target.isAlive() && target instanceof Player) {
                useDeathDefianceIfAvailable(target);
            }
            if (!target.isAlive()) {
                Locator.comIO().announceDeath(target);
            }
        } else {
            Locator.comIO().missedAttack(unit, target);
        }
        return hit;
    }

    private void handleSpecialEffectStatusOnSelf(Unit unit, Special sp) throws InterruptedException {
        if (sp.getSelfEffect() != null) {
            boolean effectOnSelf = applyEffect(unit, sp.getSelfEffect(), sp.getChance());
            if (effectOnSelf && sp.getSelfStatus() != null && unit.isAlive()) {
                applyStatus(unit, sp.getSelfStatus(), sp.getChance());
            }
        } else if (sp.getSelfStatus() != null) {
            applyStatus(unit, sp.getSelfStatus(), sp.getChance());
        }
    }

    private void handleSpecialEffectStatus(Unit unit, Unit target, Special sp)  throws InterruptedException{
        if (sp.getTargetEffect() != null) {
            boolean effectOnTarget = applyEffect(target, sp.getTargetEffect(), sp.getChance());
            if (effectOnTarget && sp.getSelfEffect() != null && !sp.targetsAll()) {
                applyEffect(unit, sp.getSelfEffect(), sp.getChance());
            }
            if (effectOnTarget && sp.getTargetStatus() != null && target.isAlive()) {
                boolean statusOnTarget = applyStatus(target, sp.getTargetStatus(), sp.getChance());
                if (statusOnTarget && sp.getSelfStatus() != null && !sp.targetsAll()) {
                    applyStatus(unit, sp.getSelfStatus(), sp.getChance());
                }
            } else if (effectOnTarget && sp.getSelfStatus() != null && !sp.targetsAll()) {
                applyStatus(unit, sp.getSelfStatus(), sp.getChance());
            }
        } else if (sp.getTargetStatus() != null) {
            if (sp.getSelfEffect() != null) {
                applyEffect(unit, sp.getSelfEffect(), sp.getChance());
            }
            boolean statusOnTarget = applyStatus(target, sp.getTargetStatus(), sp.getChance());
            if (statusOnTarget && sp.getSelfStatus() != null) {
                applyStatus(unit, sp.getSelfStatus(), sp.getChance());
            }
        }
    }

    public ActionAIEnemy getAi() {
        return ai;
    }

    public void setAi(ActionAIEnemy ai) {
        this.ai = ai;
    }

    public Player getHero() {
        return hero;
    }

    public ArrayList<Enemy> getVillainList() {
        return villainList;
    }

    public void setVillainList(ArrayList<Enemy> villainList) {
        this.villainList = villainList;
    }
}
