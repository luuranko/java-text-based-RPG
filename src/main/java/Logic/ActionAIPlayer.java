package Logic;

import java.util.ArrayList;
import Services.Locator;

import Domain.*;

public class ActionAIPlayer extends ActionAI {
    
    private boolean hasInvoked;
    private boolean hasActed;
    private Invocation invoke;
    private Attack action;
    private Enemy currentTarget;

    public ActionAIPlayer() {
        super();
        this.hasInvoked = false;
    }

    public boolean hasInvoked() {
        return this.hasInvoked;
    }

    public void setHasInvoked(boolean invoked) {
        this.hasInvoked = invoked;
    }

    public boolean hasActed() {
        return this.hasActed;
    }

    public void setHasActed(boolean acted) {
        this.hasActed = acted;
    }

    public Invocation currentInvocation() {
        return this.invoke;
    }

    public Attack currentAction() {
        return this.action;
    }

    public Enemy currentTarget() {
        return this.currentTarget;
    }

    public void planTurn(Unit hero, ArrayList<Enemy> enemies, ArrayList<Invocation> invs, ArrayList<Special> specials) {
    }

    public int selectAction(Unit hero, ArrayList<Enemy> enemies) {
        if (hasActed) {
            hasInvoked = false;
            hasActed = false;
            invoke = null;
            action = null;
            currentTarget = null;
        }
        if (!hasInvoked) {
            invoke = decideInvocation(hero, enemies, hero.readyInvocations());
            if (invoke != null) {
                hasInvoked = true;
                return 3;
            }
        }
        action = decideSpecial(hero, enemies, hero.readySpecials());
        if (action instanceof Special) {
            if (!((Special) action).targetsAll()) {
                if (currentTarget == null) {
                    currentTarget = chooseTarget(hero, enemies, action);
                }
            }
            hasActed = true;
            return 2;
        } else {
            if (currentTarget == null) {
                currentTarget = chooseTarget(hero, enemies, action);
            }
            hasActed = true;
            return 1;
        }

    }

    // Which Special the hero picks in AI
    public Attack decideSpecial(Unit hero, ArrayList<Enemy> enemies, ArrayList<Special> ready) {
        return decideSpecialSmart(hero, enemies, ready);
        // return decideSpecialDumb(hero, enemies, ready);
    }

    // TOISTAISEKSI TYHMÄ KOSKA EFFECTEJÄ EI OLE OTETTU HUOMIOON
    public Invocation decideInvocation(Unit hero, ArrayList<Enemy> enemies, ArrayList<Invocation> invs) {
        return decideInvocationSmart(hero, enemies, invs);
        // return decideInvocationDumb(hero, enemies, invs);
    }

    // Smart AI for deciding whether to use an Invocation or not and which one to use.
    // First checks need for healing, then checks if can debuff hero, then checks if can buff self
    public Invocation decideInvocationSmart(Unit hero, ArrayList<Enemy> enemies, ArrayList<Invocation> invs) {
        if (invs.isEmpty()) {
            return null;
        }
        // if the last enemy could be killed during this turn, don't waste inv
        if (killStrike(hero, enemies, hero.readySpecials())!= null && enemies.size() == 1) {
            return null;
        }
        Invocation inv = shouldHealInv(hero, invs);
        if (inv != null || invs.isEmpty()) {
            return inv;
        }
        inv = enableHealingFromSpecial(hero, invs);
        if (inv != null) {
            return inv;
        }
        removesInvocationsThatWouldHaveNoEffect(hero, invs);
        inv = removeDebuffs(hero, invs);
        if (inv != null) {
            return inv;
        }
        inv = killsTargets(hero, enemies, invs);
        if (inv != null) {
            return inv;
        }
        inv = removeBuffs(hero, enemies, invs);
        if (inv != null) {
            return inv;
        }
        inv = getsBestBuffInv(hero, invs);
        if (inv != null) {
            return inv;
        }
        inv = playerInflictsDebuffInv(hero, enemies, invs);
        if (inv != null) {
            return inv;
        }
        inv = mostDamage(hero, invs);
        if (inv != null) {
            return inv;
        }
        return null;
    }

    // Dumb AI. If there are available Invocations,
    // has a 50% chance to use a random one.
    public Invocation decideInvocationDumb(Unit hero, ArrayList<Enemy> enemies, ArrayList<Invocation> invs) {
        if (invs.isEmpty()) {
            return null;
        } else {
            if (Locator.rng().nextDouble() < 0.5) {
                return null;
            }
            return invs.get(Locator.rng().nextInt(invs.size()));
        }
    }

    // Which Enemy the hero targets in AI
    public Enemy chooseTarget(Unit hero, ArrayList<Enemy> enemies, Attack atk) {
        return chooseTargetSmart(hero, enemies, atk);
        // return chooseTargetDumb(hero, enemies, atk);
    }

    // Smart AI for deciding the Special (or basic Attack).
    // First checks need for heals, then removes options with lowest HR,
    // then checks if hitting multiple enemies is possible,
    // then checks debuff chances, then buff chances,
    // then picks the best Special.
    public Attack decideSpecialSmart(Unit hero, ArrayList<Enemy> enemies, ArrayList<Special> ready) {
        playerRemovesSpecialsWithTooLowHr(hero, enemies, ready);
        Attack sp = shouldHeal(hero, ready);
        if (sp != null) {
            return sp;
        }
        sp = hitMultiple(hero, enemies, ready);
        if (sp != null) {
            return sp;
        }
        sp = killStrike(hero, enemies, ready);
        if (sp != null) {
            return sp;
        }
        sp = playerInflictsDebuff(hero, enemies, ready);
        if (sp != null) {
            return sp;
        }
        sp = getsBestBuff(hero, ready);
        if (sp != null) {
            return sp;
        }
        sp = bestRatingSpecial(hero, ready);
        if (sp != null) {
            return sp;
        }
        return hero.getBasicAtk();
    }

    // Dumb AI for picking a Special or basic Attack.
    // If Specials available, picks one randomly!
    // ...But has a 20% chance to pick the basic Attack anyway.
    public Attack decideSpecialDumb(Unit hero, ArrayList<Enemy> enemies, ArrayList<Special> ready) {
        if (!ready.isEmpty()) {
            if (Locator.rng().nextDouble() < 0.2) {
                return hero.getBasicAtk();
            }
            return ready.get(Locator.rng().nextInt(ready.size()));
        }
        return hero.getBasicAtk();
    }

    // Removing those Specials that will have less than 45% chance of hitting
    // when checking all enemies
    // If it removes all Specials, the best one has a 30% chance of being added back in.
    protected void playerRemovesSpecialsWithTooLowHr(Unit hero, ArrayList<Enemy> enemies, ArrayList<Special> ready) {
        if (ready.isEmpty()) {
            return;
        }
        Special bestHrSp = ready.get(0);
        double bestHr = CombatCalculation.calcAtkHr(hero, enemies.get(0), ready.get(0));
        for (int i = 0; i < ready.size(); i++) {
            boolean tooLow = true;
            for (Enemy e: enemies) {
                double hr = CombatCalculation.calcAtkHr(hero, e, ready.get(i));
                if (hr > bestHr) {
                    bestHr = hr;
                    bestHrSp = ready.get(i);
                }
                if (hr > 0.45) {
                    tooLow = false;
                } 
            }
            if (tooLow) {
                ready.remove(i);
                i--;
            }
        }
        if (ready.isEmpty()) {
            if (Locator.rng().nextDouble() < 0.3) {
                ready.add(bestHrSp);
            }
        }
    }

    protected Attack killStrike(Unit hero, ArrayList<Enemy> enemies, ArrayList<Special> ready) {
        Enemy target= null;
        Attack bestAction = null;
        double highestHr = 0;
        for (Enemy e: enemies) {
            if (e.getHp() <= CombatCalculation.damageForecast(hero, e, hero.getBasicAtk())) {
                double hr = CombatCalculation.calcAtkHr(hero, e, hero.getBasicAtk());
                if (hr > highestHr) {
                    highestHr = hr;
                    bestAction = hero.getBasicAtk();
                    target = e;
                }
            }
        }
        if (highestHr > 0.95) {
            this.action = bestAction;
            this.currentTarget = target;
            return bestAction;
        }
        for (Special sp: ready) {
            for (Enemy e: enemies) {
               if (e.getHp() <= CombatCalculation.damageForecast(hero, e, sp)) {
                    double hr = CombatCalculation.calcAtkHr(hero, e, sp);
                    if (hr > highestHr) {
                        highestHr = hr;
                        bestAction = sp;
                        target = e;
                    }
                } 
            }
        }
        this.currentTarget = target;
        return bestAction;
    }

     protected Attack hitEasiest(Unit unit, Unit hero, ArrayList<Special> ready) {
        Attack easiest = unit.getBasicAtk();
        double bestScore = CombatCalculation.attackScore(unit, hero, easiest);
        for (Special sp: ready) {
            double score = CombatCalculation.attackScore(unit, hero, sp);
            if (score > bestScore) {
                bestScore = score;
                easiest = sp;
            }
        }
        return easiest;
    }

    // If there are 2 or more enemies, hitting multiple is good.
    protected Special hitMultiple(Unit hero, ArrayList<Enemy> enemies, ArrayList<Special> ready) {
        if (enemies.size() >= 2) {
            for (Special sp: ready) {
                if (sp.targetsAll()) {
                    int canPossiblyHit = 0;
                    for (Enemy e: enemies) {
                        if (CombatCalculation.calcAtkHr(hero, e, sp) > 0.5) {
                            canPossiblyHit++;
                        }
                    }
                    double hitPerc = ((double)canPossiblyHit/enemies.size()*1.0);
                    if (hitPerc >= 0.75) {
                        return sp;
                    }
                }
            }
        }
        return null;
    }

    protected Special playerInflictsDebuff(Unit unit, ArrayList<Enemy> enemies, ArrayList<Special> ready) {
        Special choice = null;
        Enemy target = null;
        double bestScore = 0;
        for (Enemy e: enemies) {
            Special sp = inflictsDebuffOnTarget(unit, e, ready);
            if (sp != null) {
                double score = CombatCalculation.calcSpecialRating(sp);
                if (score > bestScore) {
                    bestScore = score;
                    choice = sp;
                    target = e;
                } 
            }
        }
        currentTarget = target;
        return choice;
    }

    protected Special inflictsDebuffOnTarget(Unit unit, Unit target, ArrayList<Special> ready) {
        Special bestDebuff = null;
        double unitRating = CombatCalculation.calcUnitRating(unit);
        double lowestRating = unitRating;
        for (int i = 0; i < ready.size(); i++) {
            Special sp = ready.get(i);
            if (sp.getTargetStatus() != null) {
                if (!target.hasStatus(sp.getTargetStatus())) {
                    double inflictingChance = CombatCalculation.calcStatusChance(target, sp.getTargetStatus(), sp.getChance());
                    if (inflictingChance < 0.5) {
                        continue;
                    }
                    double statusRating = CombatCalculation.calcStatusRating(sp.getTargetStatus());
                    if (unitRating + statusRating < lowestRating) {
                        lowestRating = unitRating + statusRating;
                        bestDebuff = sp;
                    }
                }
            }
        }
        return bestDebuff;
    }

    // Smart AI for choosing the target.
    // First Player checks if it can kill anyone with the next hit
    // Then checks if it can debuff any enemy
    // Otherwise picks the one that is easiest to hit
    public Enemy chooseTargetSmart(Unit hero, ArrayList<Enemy> enemies, Attack atk) {
        Enemy target = killStrike(hero, enemies, atk);
        if (target != null) {
            return target;
        }
        if (atk instanceof Special) {
            target = debuffable(hero, enemies, (Special) atk);
            if (target != null) {
                return target;
            }
        }
        target = easiestHit(hero, enemies, atk);
        if (target != null) {
            return target;
        }
        return target;
    }

    // Dumb AI for choosing a target. Completely random!
    public Enemy chooseTargetDumb(Unit hero, ArrayList<Enemy> enemies, Attack atk) {
        Enemy target = enemies.get(Locator.rng().nextInt(enemies.size()));
        return target;
    }
    
    // The hero wants to hit an enemy that it can kill with the next strike
    // AND which it is most likely to hit
    private Enemy killStrike(Unit hero, ArrayList<Enemy> enemies, Attack atk) {
        Enemy target = null;
        ArrayList<Enemy> killableTargets = new ArrayList<>();
        for (Enemy e: enemies) {
            if (e.getHp() <= CombatCalculation.damageForecast(hero, e, atk)) {
                killableTargets.add(e);
            }
        }
        double bestHr = 0;
        for (Enemy e: killableTargets) {
            double hr = CombatCalculation.attackScore(hero, e, atk);
            if (hr > bestHr) {
                bestHr = hr;
                target = e;
            }
        }
        return target;
    }

    // The hero wants to inflict a debuff on an enemy
    // AND choose one that it can most likely hit
    private Enemy debuffable(Unit hero, ArrayList<Enemy> enemies, Special sp) {
        Enemy target = null;
        ArrayList<Enemy> debuffableTargets = new ArrayList<>();
        for (Enemy e: enemies) {
            if (sp.getTargetStatus() != null) {
                if (!e.hasStatus(sp.getTargetStatus())) {
                    debuffableTargets.add(e);
                }
            }
        }
        double bestHr = 0;
        for (Enemy e: debuffableTargets) {
            double hr = CombatCalculation.attackScore(hero, e, sp);
            if (hr > bestHr) {
                bestHr = hr;
                target = e;
            }
        }
        return target;
    }

    // The hero wants to hit an enemy that it is most likely to hit
    private Enemy easiestHit(Unit hero, ArrayList<Enemy> enemies, Attack atk) {
        Enemy target = null;
        double bestHr = 0;
        for (Enemy e: enemies) {
            double hr = CombatCalculation.attackScore(hero, e, atk);
            if (hr > bestHr) {
                bestHr = hr;
                target = e;
            }
        }
        return target;
    }

    private Invocation killsTargets(Unit unit, ArrayList<Enemy> enemies, ArrayList<Invocation> invs) {
        Invocation deadliest = null;
        int mostKills = 0;
        for (int i = 0; i < invs.size(); i++) {
            Invocation inv = invs.get(i);
            if (!inv.isTargetsSelf() && inv.getEffect() != null) {
                if (inv.getEffect().getHpChange() < 0) {
                   int killCount = 0;
                    for (Enemy e: enemies) {
                        if (e.getHp()+inv.getEffect().getHpChange() <= 0) {
                            killCount++;
                        }
                    }
                    if (killCount > mostKills) {
                        mostKills = killCount;
                        deadliest = inv;
                    }
                }
            }
        }
        return deadliest;
    }

    private Invocation removeBuffs(Unit hero, ArrayList<Enemy> enemies, ArrayList<Invocation> invs) {
        double debuffTotalRating = 0;
        double buffTotalRating = 0;
        int buffCount = 0;
        for (Enemy e: enemies) {
            for (Status s: e.getStati()) {
                if (s.isBuff()) {
                    buffTotalRating += CombatCalculation.calcStatusRating(s);
                    buffCount++;
                } else {
                    debuffTotalRating += CombatCalculation.calcStatusRating(s);
                }
            }
        }
        if (buffCount < enemies.size()) {
            return null;
        }
        for (int i = 0; i < invs.size(); i++) {
            Invocation inv = invs.get(i);
            if (!inv.isTargetsSelf() && inv.getEffect() != null) {
                if (inv.getEffect().isBuff()) {
                    continue;
                }
                if (inv.getEffect().isClearBuffs()) {
                    if (!inv.getEffect().isClearDebuffs() || buffTotalRating > debuffTotalRating) {
                        return inv;
                    }
                }
            }
        }
        return null;
    }

    protected Invocation playerInflictsDebuffInv(Unit hero, ArrayList<Enemy> enemies, ArrayList<Invocation> invs) {
        Invocation bestDebuff = null;
        double targetRating = CombatCalculation.calcUnitRating(enemies.get(0));
        double lowestRating = targetRating;
        for (Enemy target: enemies) {
            for (int i = 0; i < invs.size(); i++) {
                Invocation inv = invs.get(i);
                if (!inv.isTargetsSelf()) {
                    if (!target.hasStatus(inv.getStatus()) && inv.getStatus() != null) {
                        double chanceToGet = CombatCalculation.calcStatusChance(target, inv.getStatus(), inv.getChance());
                        if (chanceToGet < 0.5) {
                            continue;
                        }
                        double statusRating = CombatCalculation.calcStatusRating(inv.getStatus());
                        if (targetRating + statusRating < lowestRating) {
                            lowestRating = targetRating + statusRating;
                            bestDebuff = inv;
                        }
                    } else {
                        invs.remove(i);
                        i--;
                    }
                }
            }
        }
        return bestDebuff;
    }
}
