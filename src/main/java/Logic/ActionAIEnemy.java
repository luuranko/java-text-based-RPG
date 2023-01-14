package Logic;

import java.util.ArrayList;

import Domain.*;
import Services.Locator;

public class ActionAIEnemy extends ActionAI {

    public ActionAIEnemy() {
        super();
    }
    public Attack decideSpecial(Unit unit, Unit hero, ArrayList<Special> ready) {
        return decideSpecialSmart(unit, hero, ready);
        // return decideSpecialDumb(unit, hero, ready);
    }

    // TOISTAISEKSI TYHMÄ KOSKA EFFECTEJÄ EI OLE OTETTU HUOMIOON
    public Invocation decideInvocation(Unit unit, Unit hero, ArrayList<Invocation> invs) {
        return decideInvocationSmart(unit, hero, invs);
        // return decideInvocationDumb(unit, hero, invs);
    }

    // Smart AI for deciding a Special to use.
    public Attack decideSpecialSmart(Unit unit, Unit hero, ArrayList<Special> ready) {
        if (ready.isEmpty()) {
            return unit.getBasicAtk();
        }
        Attack sp = shouldHeal(unit, ready);
        if (sp != null) {
            return sp;
        }
        // Removing bad HR Specials only after deciding if healing is needed
        removeSpecialsWithTooLowHr(unit, hero, ready);
        if (ready.isEmpty()) {
            return unit.getBasicAtk();
        }
        sp = inflictsDebuff(unit, hero, ready);
        if (sp != null) {
            return sp;
        }
        sp = getsBestBuff(unit, ready);
        if (sp != null) {
            return sp;
        }
        // If no ready Specials, will return basic Attack
        sp = hitEasiest(unit, hero, ready);
        return sp;
    }

    // Dumb AI.
    // If there are ready Specials, will randomize the Special,
    // but still has a 20% chance of using basic Attack anyway.
    public Attack decideSpecialDumb(Unit unit, Unit hero, ArrayList<Special> ready) {
        if (ready.isEmpty()) {
            return unit.getBasicAtk();
        } else {
            if (Locator.rng().nextDouble() < 0.2) {
                return unit.getBasicAtk();
            }
            return ready.get(Locator.rng().nextInt(ready.size()));
        }
    }

    // Smart AI for deciding whether to use an Invocation or not and which one to use.
    // First checks need for healing, then checks if can debuff hero, then checks if can buff self
    public Invocation decideInvocationSmart(Unit unit, Unit hero, ArrayList<Invocation> invs) {
        if (invs.isEmpty()) {
            return null;
        }
        Invocation inv = shouldHealInv(unit, invs);
        if (inv != null) {
            return inv;
        }
        inv = enableHealingFromSpecial(unit, invs);
        if (inv != null) {
            return inv;
        }
        removesInvocationsThatWouldHaveNoEffect(unit, invs);
        inv = removeDebuffs(unit, invs);
        if (inv != null) {
            return inv;
        }
        inv = removeBuffs(hero, invs);
        if (inv != null) {
            return inv;
        }
        inv = inflictsDebuffInv(unit, hero, invs);
        if (inv != null) {
            return inv;
        }
        inv = getsBestBuffInv(unit, invs);
        if (inv != null) {
            return inv;
        }
        inv = mostDamage(unit, invs);
        if (inv != null) {
            return inv;
        }
        return null;
    }

    // Dumb AI. If there are available Invocations,
    // has a 60% chance to use a random one.
    public Invocation decideInvocationDumb(Unit unit, Unit hero, ArrayList<Invocation> invs) {
        if (invs.isEmpty()) {
            return null;
        } else {
            if (Locator.rng().nextDouble() < 0.4) {
                return null;
            }
            return invs.get(Locator.rng().nextInt(invs.size()));
        }
    }

    // Used if there are at least 2 debuffs and debuffs are higher rating than buffs
    private Invocation removeBuffs(Unit target, ArrayList<Invocation> invs) {
        double debuffTotalRating = 0;
        double buffTotalRating = 0;
        int buffCount = 0;
        for (Status s: target.getStati()) {
            if (s.isBuff()) {
                buffTotalRating += CombatCalculation.calcStatusRating(s);
                buffCount++;
            } else {
                debuffTotalRating += CombatCalculation.calcStatusRating(s);
            }
        }
        if (buffCount < 2) {
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

    protected Invocation inflictsDebuffInv(Unit unit, Unit target, ArrayList<Invocation> invs) {
        Invocation bestDebuff = null;
        double targetRating = CombatCalculation.calcUnitRating(target);
        double lowestRating = targetRating;
        for (int i = 0; i < invs.size(); i++) {
            Invocation inv = invs.get(i);
            if (!target.hasStatus(inv.getStatus())) {
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
        return bestDebuff;
    }

    // If there are Specials that have less than 45% chance of hitting
    // those Specials are removed from the list.
    // If all Specials are removed,
    // the one with the best chances has a 10% chance of getting added back
    // unless the hr is less than 10%.
    protected void removeSpecialsWithTooLowHr(Unit unit, Unit target, ArrayList<Special> ready) {
        Special bestHrSp = null;
        double bestHr = 0;
        for (int i = 0; i < ready.size(); i++) {
            double hr = CombatCalculation.calcAtkHr(unit, target, ready.get(i));
            if (hr > bestHr) {
                bestHr = hr;
                bestHrSp = ready.get(i);
            }
            if (hr < 0.45) {
                ready.remove(i);
                i--;
            }
        }
        if (ready.isEmpty()) {
            if (Locator.rng().nextDouble() < 0.1) {
                if (bestHrSp != null && bestHr > 0.1) {
                    ready.add(bestHrSp);
                }
            }
        }
    }

    // The enemy wants to hit the hero with a Special that
    // it most likely can hit and that would deal most damage
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
}
