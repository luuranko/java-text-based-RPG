package Logic;

import IO.Statics;
import Domain.*;

public class CombatCalculation {
	    public static double calcAtkHr(Unit unit, Unit target, Attack atk) {
        double hitChance = unit.hrMod();
        hitChance += atk.getHr();
        hitChance -= target.evasion();
        hitChance = Statics.roundDouble(hitChance);
        if (hitChance > 1.0) {
            hitChance = 1.0;
        } else if (hitChance < 0.0) {
            hitChance = 0.0;
        }
        return hitChance;
    }

    public static double calcAtkCr(Unit unit, Unit target, Attack atk) {
        double critChance = unit.crMod();
        critChance += atk.getCr();
        critChance -= target.crResist();
        critChance = Statics.roundDouble(critChance);
        if (critChance > 1.0) {
            critChance = 1.0;
        } else if (critChance < 0.0) {
            critChance = 0.0;
        }
        return critChance;
    }

    public static double calcEffectChance(Unit target, Effect e, double chance) {
        if (e == null) {
            return 0;
        }
        if (!e.isBuff()) {
            chance -= target.dbResist();
        }
        double res = target.resistanceTo(e);
        chance -= res;
        if (chance > 1.0) {
            chance = 1.0;
        } else if (chance < 0) {
            chance = 0;
        }
        return chance;
    }

    public static double calcStatusChance(Unit target, Status s, double chance) {
        if (s == null) {
            return 0;
        }
        if (!s.isBuff()) {
            chance -= target.dbResist();
        }
        double res = target.resistanceTo(s);
        chance -= res;
        if (chance > 1.0) {
            chance = 1.0;
        } else if (chance < 0) {
            chance = 0;
        }
        return chance;
    }

    public static int damageForecast(Unit unit, Unit target, Attack atk) {
        int damage = unit.getStr() + atk.getPwr();
        damage -= target.getDef();
        if (atk instanceof Special) {
            Special sp = (Special) atk;
            if (sp.getTargetEffect()!= null) {
                damage -= sp.getTargetEffect().getHpChange();
            }
        }
        if (damage < 0) {
            damage = 0;
        }
        return damage;
    }
    public static int critDamageForecast(Unit unit, Unit target, Attack atk) {
        int damage = (unit.getStr() + atk.getPwr())*2;
        damage -= target.getDef();
        if (atk instanceof Special) {
            Special sp = (Special) atk;
            if (sp.getTargetEffect()!= null) {
                damage -= sp.getTargetEffect().getHpChange();
            }
        }
        if (damage < 0) {
            damage = 0;
        }
        return damage;
    }

    public static double attackScore(Unit unit, Unit target, Attack atk) {
        double hr = calcAtkHr(unit, target, atk);
        double damage = damageForecast(unit, target, atk)/(double)target.getMaxHp()*1.0;
        if (damage > target.getMaxHp()) {
            damage = 1.0;
        } else if (damage < 0) {
            damage = 0;
        }
        double normalScore = hr * damage;
        double bonus = 0;
        if (atk instanceof Special) {
            bonus = calcSpecialRating((Special) atk);
        } else {
            bonus = calcAttackRating(atk);
        }
        normalScore += bonus;
        double cr = calcAtkCr(unit, target, atk);
        double critDamage = critDamageForecast(unit, target, atk)/(double)target.getMaxHp();
        if (critDamage > target.getMaxHp()) {
            critDamage = 1.0;
        } else if (critDamage < 0) {
            critDamage = 0;
        }
        double critScore = hr * cr * critDamage;
        critScore += bonus;
        if (normalScore > critScore) {
            return normalScore;
        } else {
            return critScore;
        }
    }

    public static double calcUnitTotalRating(Unit unit) {
        // System.out.println();
        // System.out.println(unit);
        double defensive = calcUnitDefensiveRating(unit);
        // System.out.println(defensive);
        double offensive = calcUnitOffensiveRating(unit);
        // System.out.println(offensive);
        if (unit instanceof Player) {
            offensive *= 0.65;
        }
        double rating = (defensive + offensive)/2;
        
        
        return Statics.roundDouble(rating);
    }

    public static double calcUnitOffensiveRating(Unit unit) {
        double rating = ((double) unit.getStr())*1.0;
        rating += Statics.crMod * unit.getStr();
        rating += Statics.hrMod * unit.getSpd();
        rating += calcAttackRating(unit.getBasicAtk());
        for (Special sp: unit.getSpecials()) {
            double spRating = calcSpecialRating(sp);
            if (sp.targetsAll() && unit instanceof Player) {
                spRating *= 3.5;
            }
            rating += spRating;
        }
        for (Invocation inv: unit.getInvocations()) {
            if (!inv.isUsed()) {
                if (!inv.isTargetsSelf() && unit instanceof Player) {
                    rating += calcInvocationRating(inv)*3.5;
                } else if (!inv.isTargetsSelf()) {
                    rating += calcInvocationRating(inv);
                }
            }
        }
        return rating;
    }

    public static double calcUnitDefensiveRating(Unit unit) {
        double rating = ((double) unit.getDef())*1.0;
        rating += Statics.dbreMod * unit.getDef();
        rating += Statics.crreMod * unit.getDef();
        rating += unit.getHp();
        rating += Statics.evMod * unit.getSpd();
        for (Resistance r: unit.getResistances()) {
            // Resistances come into play so rarely
            rating += calcResistanceRating(r) * 0.2;
        }
        for (Invocation inv: unit.getInvocations()) {
            if (inv.isTargetsSelf()) {
                rating += calcInvocationRating(inv);
            }
        }
        for (Special sp: unit.getSpecials()) {
            if (sp.getSelfEffect() != null) {
                rating += calcEffectRating(sp.getSelfEffect())*0.5;
            }
            if (sp.getSelfStatus() != null) {
                rating += calcStatusRating(sp.getSelfStatus())*0.5;
            }
        }
        return rating;
    }

    public static double calcUnitRating(Unit unit) {
        double stats = ((double)unit.getHp());
        stats += unit.getStr();
        stats += unit.getDef();
        stats += unit.getSpd();

        double rates = unit.hrMod();
        rates += unit.evasion();
        rates += unit.crMod();
        rates += unit.crResist();
        rates += unit.dbResist();
        // HOW MUCH THE RATES WEIGH IN A RATING... not much when at *2
        // ADJUST LATER?
        rates *= 1;

        double rating = stats + rates;
        return Statics.roundDouble(rating);
    }

    public static double calcAttackRating(Attack atk) {
        int pwr = atk.getPwr();
        if (pwr < 1) {
            pwr = 1;
        }
        double rating = pwr * atk.getHr();
        rating += (pwr * atk.getCr())* atk.getHr();
        return Statics.roundDouble(rating);
    }

    public static double calcSpecialRating(Special sp) {
        double rating = sp.getPwr() * sp.getHr();
        if (sp.getPwr() < 0) {
            rating *= 0;
        }
        rating += sp.getCr();
        if (sp.getSelfEffect() != null) {
            rating += calcEffectRating(sp.getSelfEffect())*sp.getChance();
        }
        if (sp.getTargetEffect() != null) {
            rating += calcEffectRating(sp.getTargetEffect())*sp.getChance();
        }
        if (sp.getSelfStatus() != null) {
            rating += calcStatusRating(sp.getSelfStatus())*sp.getChance();
        }
        if (sp.getTargetStatus() != null) {
            rating += calcStatusRating(sp.getTargetStatus())*sp.getChance();
        }
        rating /= (sp.getCooldown()/2);
        return Statics.roundDouble(rating);
    }

    public static double calcEffectRating(Effect e) {
        double rating = 0;
        // TODO think how much hpchange should affect rating
        if (e.isBuff()) {
            rating += ((double) e.getHpChange())*0.5;
            rating -= e.getCooldownChange();
            if (e.isClearBuffs()) {
                rating -= 2;
            }
            if (e.isClearDebuffs()) {
                rating += 5;
            }
        } else {
            rating -= ((double) e.getHpChange())*0.5;
            rating += e.getCooldownChange();
            if (e.isClearBuffs()) {
                rating += 5;
            }
            if (e.isClearDebuffs()) {
                rating -= 2;
            }
        }
        return Statics.roundDouble(rating);
    }

    public static double calcStatusRating(Status s) {
        if (s == null) {
            return 0;
        }
        double stats = ((double)s.getStr())*1.0;
        stats += s.getDef();
        stats += s.getSpd();
        // stats *= 0.5;

        double rates = s.hrMod();
        rates += s.evasion();
        rates += s.crMod();
        rates += s.crResist();
        rates += s.dbResist();
        // HOW MUCH THE RATES WEIGH IN A RATING...
        // ADJUST LATER?
        rates *= 2;

        double rating = stats + rates;
        rating += ((double)s.getHpPerTurn())*0.5;
        rating *= s.getDuration();

        if (s.isBuff()) {
            return Statics.roundDouble(rating);
        } else {
            return Statics.roundDouble(-rating);
        }
    }

    public static double calcInvocationRating(Invocation inv) {
        double rating = 0;
        if (inv.getEffect() != null) {
            rating += calcEffectRating(inv.getEffect());
        }
        if (inv.getStatus() != null) {
            rating += calcStatusRating(inv.getStatus());
        }
        rating *= inv.getChance();
        return Statics.roundDouble(rating);
    }

    public static double calcResistanceRating(Resistance r) {
        double rating = r.getChance();
        if (r.isBuff()) {
            return Statics.roundDouble(rating);
        } else {
            if (r.getEffect() != null && r.getStatus() != null) {
                if (r.getEffect().isBuff() || r.getStatus().isBuff()) {
                    return Statics.roundDouble(rating*2);
                } else {
                    return Statics.roundDouble(-rating*2);
                }
            } else if (r.getEffect() != null) {
                if (r.getEffect().isBuff()) {
                    return Statics.roundDouble(rating);
                } else {
                    return Statics.roundDouble(-rating);
                }
            } else if (r.getStatus() != null) {
                if (r.getStatus().isBuff()) {
                    return Statics.roundDouble(rating);
                } else {
                    return Statics.roundDouble(-rating);
                }
            } else {
                return Statics.roundDouble(rating);
            }
        }
        
    }
}