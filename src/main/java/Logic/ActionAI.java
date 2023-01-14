package Logic;

import java.util.ArrayList;

import Domain.*;
import Services.Locator;

public class ActionAI {

    public ActionAI() {
        
    }

    // The Special that has the best Rating.
    protected Special bestRatingSpecial(Unit unit, ArrayList<Special> ready) {
        Special best = null;
        double bestScore = 0;
        for (Special sp: ready) {
            double score = CombatCalculation.calcSpecialRating(sp);
            if (score > bestScore) {
                bestScore = score;
                best = sp;
            }
        }
        return best;
    }

    protected Special mostValuableSpecial(Unit unit, Unit target, ArrayList<Special> ready) {
        Special mostValuable = null;
        double bestScore = 0;
        for (Special sp: ready) {
            double score = CombatCalculation.attackScore(unit, target, sp);
            double cd = (double) sp.getCooldown()/5;
            score += cd;
            if (score > bestScore) {
                bestScore = score;
                mostValuable = sp;
            }
        }
        return mostValuable;
    }

    protected Special inflictsDebuff(Unit unit, Unit target, ArrayList<Special> ready) {
        if (ready.isEmpty()) {
            return null;
        }
        Special bestDebuff = null;
        double targetRating = CombatCalculation.calcUnitRating(target);
        double lowestRating = targetRating;
        for (int i = 0; i < ready.size(); i++) {
            Special sp = ready.get(i);
            if (sp.getTargetStatus() != null) {
                if (!target.hasStatus(sp.getTargetStatus())) {
                    double statusRating = CombatCalculation.calcStatusRating(sp.getTargetStatus());
                    if (targetRating - statusRating < lowestRating) {
                        lowestRating = targetRating - statusRating;
                        bestDebuff = sp;
                    }
                } else {
                    ready.remove(i);
                    i--;
                }
            }
        }
        return bestDebuff;
    }

    protected Special getsBestBuff(Unit unit, ArrayList<Special> ready) {
        Special bestBuff = null;
        double bestRating = 0;
        double unitRating = CombatCalculation.calcUnitRating(unit);
        for (int i = 0; i < ready.size(); i++) {
            Special sp = ready.get(i);
            if (sp.getSelfStatus() != null) {
                if (!unit.hasStatus(sp.getSelfStatus())) {
                    double statusRating = CombatCalculation.calcStatusRating(sp.getSelfStatus());
                    if (unitRating + statusRating > bestRating) {
                        bestRating = unitRating + statusRating;
                        bestBuff = sp;
                    }
                } else {
                    ready.remove(i);
                    i--;
                }
            }
        }
        return bestBuff;
    }
    
    protected Special longestCooldown(Unit unit, ArrayList<Special> ready) {
        Special mostValuable = null;
        int longest = 0;
        for (Special sp: ready) {
            if (sp.getCooldown() > longest) {
                longest = sp.getCooldown();
                mostValuable = sp;
            }
        }
        return mostValuable;
    }


    // If the unit has less than 80% of its max HP
    // And can gain the full effect of a Special heal
    // (If overheal might happen from per-turn-heal, doesn't discount)
    // The unit will use the Special. Else, the Special will be saved for later.
    protected Special shouldHeal(Unit unit, ArrayList<Special> ready) {
        Special bestHealSp = null;
        int bestHeal = 0;
        for (int i = 0; i < ready.size(); i++) {
            Special sp = ready.get(i);
            int heal = 0;
            boolean saveForLater = false;
            if (sp.getSelfEffect() != null) {
                if (sp.getSelfEffect().getHpChange() > 0) {
                    heal = sp.getSelfEffect().getHpChange();
                    if (unit.getHp()  >= (unit.getMaxHp()*0.8)) {
                        saveForLater = true;
                    } else if (heal > (unit.getMaxHp()-unit.getHp())) {
                        saveForLater = true;
                    }
                }
            }
            if (!saveForLater) {
                if (sp.getSelfStatus() != null) {
                    if (sp.getSelfStatus().getHpPerTurn() > 0) {
                        if (!unit.hasStatus(sp.getSelfStatus())) {
                            heal += sp.getSelfStatus().getHpPerTurn()*sp.getSelfStatus().getDuration();
                        } else if (heal < sp.getSelfStatus().getHpPerTurn()*sp.getSelfStatus().getDuration()){
                            saveForLater = true;
                        }
                    }
                }
            }
            if (saveForLater) {
                ready.remove(i);
                i--;
            } else if (heal > bestHeal) {
                bestHealSp = sp;
                bestHeal = heal;
            }
        }
        return bestHealSp;
    }

    protected void removesInvocationsThatWouldHaveNoEffect(Unit unit, ArrayList<Invocation> invs) {
        for (int i = 0; i < invs.size(); i++) {
            Invocation inv = invs.get(i);
            boolean remove = false;
            if (inv.isTargetsSelf() && inv.getEffect() != null) {
                if (inv.getEffect().getCooldownChange() < 0 && unit.getSpecials().size() == unit.readySpecials().size()) {
                    remove = true;
                }
            }
            if (inv.isTargetsSelf() && inv.getStatus() != null) {
                if (unit.hasStatus(inv.getStatus())) {
                    remove = true;
                }
            }
            if (remove) {
                invs.remove(i);
                i--;
            }
        }
    }

    protected Invocation shouldHealInv(Unit unit, ArrayList<Invocation> invs) {
        Invocation bestHealInv = null;
        int bestHeal = 0;
        for (int i = 0; i < invs.size(); i++) {
            Invocation inv = invs.get(i);
            int heal = 0;
            boolean saveForLater = false;
            if (inv.isTargetsSelf()) {
                if (inv.getEffect() != null) {
                    if (inv.getEffect().getHpChange() > 0) {
                        heal = inv.getEffect().getHpChange();
                        if (unit.getHp()  >= (unit.getMaxHp()*0.65)) {
                            saveForLater = true;
                        } else if (heal > (unit.getMaxHp()-unit.getHp())) {
                            saveForLater = true;
                        }
                    }
                }
                if (!saveForLater) {
                    if (inv.getStatus() != null) {
                        if (inv.getStatus().getHpPerTurn() > 0) {
                            if (!unit.hasStatus(inv.getStatus())) {
                                heal += inv.getStatus().getHpPerTurn()*inv.getStatus().getDuration();
                            } else if (heal < inv.getStatus().getHpPerTurn()*inv.getStatus().getDuration()){
                                saveForLater = true;
                            }
                        }
                    }
                }
            }
            if (saveForLater) {
                invs.remove(i);
                i--;
            } else if (heal > bestHeal) {
                bestHealInv = inv;
                bestHeal = heal;
            }
        }
        return bestHealInv;
    }

    // Only used if HP is < 50%
    protected Invocation enableHealingFromSpecial(Unit unit, ArrayList<Invocation> invs) {
        if (unit.getHp()  > (unit.getMaxHp()*0.5)) {
            return null;
        }
        for (int i = 0; i < invs.size(); i++) {
            Invocation inv = invs.get(i);
            if (inv.getEffect() != null) {
                if (inv.getEffect().getCooldownChange() < 0) {
                    ArrayList<Special> couldUseIfThisInvUsed = new ArrayList<>();
                    for (Special sp: unit.getSpecials()) {
                        if (sp.getTurnsLeft() > 0 && sp.getTurnsLeft() - inv.getEffect().getCooldownChange() <= 0 && sp.getSelfEffect() != null) {
                            if (sp.getSelfEffect().getHpChange() > 0) {
                                couldUseIfThisInvUsed.add(sp);
                            }
                        }
                    }
                    Special sp = shouldHeal(unit, couldUseIfThisInvUsed);
                    if (sp != null) {
                        return inv;
                    }
                }
            }
        }
        return null;
    }


    // Used if there are at least 2 debuffs and debuffs are higher rating than buffs
    protected Invocation removeDebuffs(Unit unit, ArrayList<Invocation> invs) {
        double debuffTotalRating = 0;
        double buffTotalRating = 0;
        int debuffCount = 0;
        for (Status s: unit.getStati()) {
            if (s.isBuff()) {
                buffTotalRating += CombatCalculation.calcStatusRating(s);
            } else {
                debuffTotalRating += CombatCalculation.calcStatusRating(s);
                debuffCount++;
            }
        }
        if (debuffCount < 2) {
            return null;
        }
        for (int i = 0; i < invs.size(); i++) {
            Invocation inv = invs.get(i);
            if (inv.isTargetsSelf() && inv.getEffect() != null) {
                if (!inv.getEffect().isBuff()) {
                    continue;
                }
                if (inv.getEffect().isClearDebuffs()) {
                    if (!inv.getEffect().isClearBuffs() || buffTotalRating < debuffTotalRating) {
                        return inv;
                    }
                }
            }
        }
        return null;
    }

    protected Invocation mostDamage(Unit unit, ArrayList<Invocation> invs) {
        Invocation mostDamaging = null;
        int mostDamage = 0;
        for (int i = 0; i < invs.size(); i++) {
            Invocation inv = invs.get(i);
            if (!inv.isTargetsSelf() && inv.getEffect() != null) {
                if (-inv.getEffect().getHpChange() > mostDamage) {
                    mostDamage = -inv.getEffect().getHpChange();
                    mostDamaging = inv;
                }
            }
        }
        return mostDamaging;
    }

    // TODO
    // protected Invocation weakensToHitWithSpecial(Unit unit, ArrayList<Invocation> invs) {

    // }

    protected Invocation getsBestBuffInv(Unit unit, ArrayList<Invocation> invs) {
        Invocation bestBuff = null;
        double bestRating = 0;
        double unitRating = CombatCalculation.calcUnitRating(unit);
        for (int i = 0; i < invs.size(); i++) {
            Invocation inv = invs.get(i);
            if (!unit.hasStatus(inv.getStatus())) {
                double statusRating = CombatCalculation.calcStatusRating(inv.getStatus());
                if (unitRating + statusRating > bestRating) {
                    bestRating = unitRating + statusRating;
                    bestBuff = inv;
                }
            } else {
                invs.remove(i);
                i--;
            }
        }
        return bestBuff;
    }
}
