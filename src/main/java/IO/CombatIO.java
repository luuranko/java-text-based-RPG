package IO;

import java.util.ArrayList;

import Domain.*;
import Logic.CombatCalculation;
import Services.Locator;

public class CombatIO extends IO implements AnyCombatIO {

    private PlayerInput input;

    public CombatIO() {
    }

    public void init() {
        this.input = Locator.input();
    }

    // PRINTING TURN START

    public void turnStart(int turn) throws InterruptedException {
        secWait();
        println(bold("\n// TURN " + turn + " //"));
    }

    public void turnDeclaration(Unit unit) throws InterruptedException {
        println("");
        if(unit.getName().toUpperCase().endsWith("s")) {
            printArrow(bold(unit.getName().toUpperCase() + "' TURN"));
        } else {
            printArrow(bold(unit.getName().toUpperCase() + "'S TURN"));
        }
        printUnitState(unit);
    }

    public void printUnitState(Unit unit) throws InterruptedException {
        printArrow(unitState(unit));
    }

    public String unitState(Unit unit) throws InterruptedException {
        String txt = unit + " " + unitHp(unit);
        String s = "";
        if (hasStatus(unit)) {
            s += " (";
            for (int i = 0; i < unit.getStati().size()-1; i++) {
                s += unit.getStati().get(i) + ", ";
            }
            s += unit.getStati().get(unit.getStati().size()-1) + ")";
        }
        txt += italic(s);
        return txt;
    }

    public void heroTurnStart(ArrayList<Enemy> enemies) throws InterruptedException {
        printFast("> Enemies: ");
        for (Enemy e: enemies) {
            print(pad + unitState(e));
        }
        printFast("");
    }

    public void healing(Unit u, int hp) throws InterruptedException {
        printArrow(u + " heals by " + green(hp + "") + " HP." + unitHpChange(u));
    }

    public void healingFromStatus(Unit u, int hp, Status s) throws InterruptedException {
        printArrow(u + " heals by " + green(hp + "") + " HP from being "+ s + "." + unitHpChange(u));
    }

    public void damage(Unit u, int hp) throws InterruptedException {
        printArrow(u + " takes " + red(hp + "") + " damage!" + unitHpChange(u));
    }

    public void damageFromStatus(Unit u, int hp, Status s) throws InterruptedException {
        printArrow(u + " takes " + red(hp + "") + " damage from being " + s + "!" + unitHpChange(u));
    }

    public void savedFromDeath(Unit u, Status s) throws InterruptedException {
        printArrow(u + " is saved from death by being " + s + "!" + unitHpChange(u));
    }

    public void deathByStatus(Unit u, Status s) throws InterruptedException {
        printArrow(red(u + " dies from being " + s + "!"));
    }

    public void announceStatusWoreOut(Unit u, Status status) throws InterruptedException {
        printArrow(cyan(u + " is no longer " + status + "."));
    }

    public void announceSpecialReady(Special sp) throws InterruptedException {
        printArrow(purple(sp + " has recharged."));
    }

    public void noAvailableSpecials() throws InterruptedException {
        printFast(red("No ready Special attacks."));
    }

    public void noAvailableInvocations() throws InterruptedException {
        printFast(red("You have nothing left to Invoke."));
    }

    public void cannotInvokeAgain() throws InterruptedException {
        printFast(red("You have already Invoked on this turn."));
    }

    // PLAYER MAKING CHOICES ON TURN

    public int actionChoice(Unit hero, ArrayList<Enemy> enemies, String... actions) throws InterruptedException {
        printFast("> Select action");
        printCommands(actions);
        int choice = input.selectedAction(hero, enemies, actions);
        return choice;
    }

    public Special specialChoice(Unit unit, ArrayList<Special> specials) throws InterruptedException {
        printFast(purple("> Select Special"));
        printCommandsSpecials(specials);
        return input.selectedSpecial(unit, specials);
    }

    public Invocation invocationChoice(Unit unit, ArrayList<Invocation> invocations) throws InterruptedException {
        printFast(green("> Select Invocation"));
        printCommandsInvocations(invocations);
        return input.selectedInvocation(unit, invocations);
    }

    public Unit selectUnit(ArrayList<Unit> units) throws InterruptedException {
        printFast(pad + "Select target");
        printCommandsUnits(units);
        return input.selectedUnit(units);
    }

    public Enemy selectEnemy(Unit hero, Attack atk, ArrayList<Enemy> enemies) throws InterruptedException {
        printFast(pad + "Select target");
        printCommandsEnemies(hero, atk, enemies);
        return input.selectedEnemy(hero, atk, enemies);
    }

    public boolean confirmTargetAll(Unit hero, Special sp, ArrayList<Enemy> enemies) throws InterruptedException {
        printCommandsEnemiesTargetAll(hero, sp, enemies);
        printFast(pad + sp + " will target all enemies.");
        print(pad + grey("[0] Cancel"));
        print(pad + "[1] Confirm");
        printFast("");
        return input.confirm();
    }

    public boolean confirmInvocationUse(Unit hero, Invocation inv, ArrayList<Enemy> enemies) throws InterruptedException {
        if (inv.isTargetsSelf()) {
            String txt = pad + "Attempt granting ";
            if (inv.getEffect() != null) {
                txt += inv.getEffect() + "";
            }
            if (inv.getEffect() != null && inv.getStatus() != null) {
                txt += " and ";
            }
            if (inv.getStatus() != null) {
                txt += inv.getStatus() + "";
            }
            txt += " to yourself?";
            printFast(txt);
        } else {
            printEnemyTargetWInvocation(inv, enemies);
            String txt = pad + "Attempt inflicting ";
            if (inv.getEffect() != null) {
                txt += inv.getEffect() + "";
            }
            if (inv.getEffect() != null && inv.getStatus() != null) {
                txt += " and ";
            }
            if (inv.getStatus() != null) {
                txt += inv.getStatus() + "";
            }
            txt += " on all enemies?";
            printFast(txt);
        }
        print(pad + grey("[0] Cancel"));
        print(pad + "[1] Confirm");
        printFast("");
        return input.confirm();
    }

    public void alreadyHaveInvokeStatus() throws InterruptedException {
        printFast(pad + red("You already have the Status provided by this Invocation."));
    }

    // CHOICE PRINTING

    public void printCommandsUnits(ArrayList<Unit> units) throws InterruptedException {
        printFast(pad + grey("[0] Cancel"));
        int i = 1;
        for (Unit u: units) {
            print(pad + "[" + i + "] " + u);
            i++;
        }
        printFast("");
    }

    public void printCommandsEnemies(Unit hero, Attack atk, ArrayList<Enemy> units) throws InterruptedException {
        printFast(pad + grey("[0] Cancel"));
        int i = 1;
        String print = "";
        int printSize = 0;
        for (Enemy e: units) {
            String s = pad + "[" + i + "] " + e.getName()
            + " (HR " + hitChance(CombatCalculation.calcAtkHr(hero, e, atk))
            + " CR " + critChance(CombatCalculation.calcAtkCr(hero, e, atk))
            + " DMG " + CombatCalculation.damageForecast(hero, e, atk);
            if (atk instanceof Special) {
                if (((Special) atk).getTargetEffect() != null) {
                    s += " " + ((Special) atk).getTargetEffect().getName() + " " + hitChance(CombatCalculation.calcEffectChance(e, ((Special) atk).getTargetEffect(), ((Special) atk).getChance()));
                }
                if (((Special) atk).getTargetStatus() != null) {
                    s += " " + ((Special) atk).getTargetStatus().getName() + " " + hitChance(CombatCalculation.calcStatusChance(e, ((Special) atk).getTargetStatus(), ((Special) atk).getChance()));
                }
            }
            s += ")";
            i++;
            print += s;
            printSize++;
            if (printSize == 2) {
                printFast(print);
                print = "";
                printSize = 0;
            }
        }
        if (printSize > 0) {
            printFast(print);
        }
    }

    public void printEnemyTargetWInvocation(Invocation inv, ArrayList<Enemy> units) throws InterruptedException {
        String print = "";
        int printSize = 0;
        for (Enemy e: units) {
            String s = pad + e.getName() + " (";
            if (!inv.isTargetsSelf()) {
                if (inv.getEffect() != null) {
                    s += inv.getEffect().getName() + " " + hitChance(CombatCalculation.calcEffectChance(e, inv.getEffect(), inv.getChance()));
                }
                if (inv.getEffect() != null && inv.getStatus() != null) {
                    s += " ";
                }
                if (inv.getStatus() != null) {
                    s += inv.getStatus().getName() + " " + hitChance(CombatCalculation.calcStatusChance(e, inv.getStatus(), inv.getChance()));
                }
            }
            s += ")";
            print += s;
            printSize++;
            if (printSize == 2) {
                printFast(print);
                print = "";
                printSize = 0;
            }
        }
        if (printSize > 0) {
            printFast(print);
        }
    }

    public void printCommandsEnemiesTargetAll(Unit hero, Attack atk, ArrayList<Enemy> units) throws InterruptedException {
        String print = "";
        int printSize = 0;
        for (Enemy e: units) {
            String s = pad + e.getName()
            + " (HR " + hitChance(CombatCalculation.calcAtkHr(hero, e, atk))
            + " CR " + critChance(CombatCalculation.calcAtkCr(hero, e, atk))
            + " DMG " + CombatCalculation.damageForecast(hero, e, atk);
            if (atk instanceof Special) {
                if (((Special) atk).getTargetEffect() != null) {
                    s += " " + ((Special) atk).getTargetEffect().getName() + " " + hitChance(CombatCalculation.calcEffectChance(e, ((Special) atk).getTargetEffect(), ((Special) atk).getChance()));
                }
                if (((Special) atk).getTargetStatus() != null) {
                    s += " " + ((Special) atk).getTargetStatus().getName() + " " + hitChance(CombatCalculation.calcStatusChance(e, ((Special) atk).getTargetStatus(), ((Special) atk).getChance()));
                }
            }
            s += ")";
            print += s;
            printSize++;
            if (printSize == 2) {
                printFast(print);
                print = "";
                printSize = 0;
            }
        }
        if (printSize > 0) {
            printFast(print);
        }
    }

    public void printCommandsSpecials(ArrayList<Special> specials) throws InterruptedException {
        printFast(pad + grey("[0] Cancel"));
        int rowLimit;
        if (specials.size() >= 5) {
            rowLimit = 3;
        } else {
            rowLimit = 2;
        }
        int i = 1;
        String print = "";
        int printSize = 0;
        for (Special sp: specials) {
            String txt = pad + purple("[" + i + "] " + sp);
            i++;
            print += txt;
            printSize++;
            if (printSize == rowLimit) {
                printFast(print);
                print = "";
                printSize = 0;
            }
        }
        if (printSize > 0) {
            printFast(print);
        }
    }

    public void printCommandsInvocations(ArrayList<Invocation> invocations) throws InterruptedException {
        printFast(pad + grey("[0] Cancel"));
        int rowLimit;
        if (invocations.size() >= 5) {
            rowLimit = 3;
        } else {
            rowLimit = 2;
        }
        int i = 1;
        String print = "";
        int printSize = 0;
        for (Invocation inv: invocations) {
            String txt = pad + green("[" + i + "] " + inv.getName());
            i++;
            print += txt;
            printSize++;
            if (printSize == rowLimit) {
                printFast(print);
                print = "";
                printSize = 0;
            }
        }
        if (printSize > 0) {
            printFast(print);
        }
    }

    public void printCommands(String... commands) throws InterruptedException {
        int i = 1;
        for (String c: commands) {
            if (c.equals("Attack")) {
                print(pad + blue("[" + i + "] " + c));
            } else if (c.equals("Special")) {
                print(pad + purple("[" + i + "] " + c));
            } else if (c.equals("Invoke")) {
                print(pad + green("[" + i + "] " + c));
            } else {
                print(pad + grey("[" + i + "] " + c));
            }
            i++;
        }
        printFast("");
    }



    // COMBAT

    public void usedDeathDefiance(Player hero) throws InterruptedException {
        println(red(hero + " defied death! -> " + unitHp(hero)));
        println("Death Defiances left: " + hero.getDeathDefiances());
    }

    public void invoke(Unit unit, Invocation inv)  throws InterruptedException {
        println(green(unit + " invokes " + inv + "!"));
    }

    public void allTarget(Unit unit, Attack atk) throws InterruptedException {
        println(unit + " targets all enemies with " + atk + ".");
        halfWait();
    }

    public void singleTarget(Unit unit, Unit target, Attack atk) throws InterruptedException {
        println(unit + " targets " + target + " with " + atk + ".");
        halfWait();
    }

    public void announceChances(double hit, double crit) throws InterruptedException {
        println("Hit Chance: " + hitChance(hit) + ". Critical Hit Chance: " + critChance(crit) + ".");
        halfWait();
    }

    public void announceDamage(Unit unit, Unit target, Attack atk, int damage, boolean crit) throws InterruptedException {
        if (damage > 0) {
            if (crit) {
                println(red("A critical hit!"));
                halfWait();
            }
            println(unit + " hits and deals " + red(damage + "") + " damage to " + target + "!" + unitHpChange(target));
        } else if (!(atk instanceof Special) ) {
            println(unit + " hits, but " + target + " endures the hit!");
        } else {
            Special sp = (Special) atk;
            if (sp.getTargetStatus() == null) {
                println(unit + " hits, but " + target + " endures the hit!");
            } else {
                println(unit + " hits " + target + "!");
            }
        }
        halfWait();
    }

    public void announceDeath(Unit target) throws InterruptedException {
        println(red(target + " has been defeated!"));
        halfWait();
    }

    public void announceEffectStatusChances(Effect e, double effChance, Status s, double statChance) throws InterruptedException {
        if (e != null && s != null) {
            println(e + " Chance: " + hitChance(effChance) + ". " + s + " Chance: " + hitChance(statChance) + ".");
        } else if (e != null) {
            println(e + " Chance: " + hitChance(effChance) + ".");
        } else if (s != null) {
            println(s + " Chance: " + hitChance(statChance) + ".");
        }
        halfWait();
    }

    public void resistsEffect(Unit unit, Effect e) throws InterruptedException {
        println(yellow(unit + " resisted " + e + "."));
        halfWait();
    }

    public void resistsStatus(Unit unit, Status s) throws InterruptedException {
        println(yellow(unit + " resisted becoming " + s + "."));
        halfWait();
    }

    public void announceEffect(Unit unit, Effect e) throws InterruptedException {
        if (e.isBuff()) {
            println(unit + " is granted " + e + "!");
        } else {
            println(unit + " is afflicted by " + e + "!");
        }
        halfWait();
    }

    public void announceCooldownChange(Unit unit, Effect e) throws InterruptedException {
        if (unit.getSpecials().isEmpty()) {
            return;
        }
        if (e.getCooldownChange() > 0) {
            println(purple("All of " + unit + "'s Special cooldowns are extended by " + red(e.getCooldownChange() + "") + " turns!"));
        } else if (e.getCooldownChange() < 0) {
            println(purple("All of " + unit + "'s Special cooldowns are shortened by " + green(-e.getCooldownChange() + "") + " turns!"));
        }
        halfWait();
    }

    public void announceStatus(boolean addedStatus, Unit unit, Status s) throws InterruptedException {
        if (addedStatus) {
            String txt = unit + " is now " + bold(s.getName()) + "! (";
            for (int i = 0; i < s.getStats().length; i++) {
                int stat = s.getStats()[i];
                if (stat != 0) {
                    txt += Statics.statNames[i+1] + " ";
                    if (stat > 0) {
                        txt += green("+" + stat) + " ";
                    } else if (stat < 0) {
                        txt += red(stat + "")+ " ";
                    }
                }
            }
            for (int i = 0; i < s.getRates().length; i++) {
                double rate = s.getRates()[i];
                if (rate != 0) {
                    txt += Statics.rateNames[i] + " ";
                    if (rate > 0) {
                        txt += green("+" + formatDouble(rate)) + " ";
                    } else if (rate < 0) {
                        txt += red(formatDouble(rate)) + " ";
                    }
                }
            }
            if (s.getHpPerTurn() != 0) {
                if (s.getHpPerTurn() > 0) {
                    txt += "Healed by " + green(s.getHpPerTurn() + "") + "/turn";
                } else {
                    txt += "Damaged by " + red(-s.getHpPerTurn() + "") + "/turn";
                }
            }
            txt += ")";
            println(cyan(txt));
        } else {
            println(yellow(unit + " is already " + bold(s.getName()) + "."));
        }
        halfWait();
    }

    public void missedAttack(Unit unit, Unit target) throws InterruptedException {
        int spdDiff = unit.getSpd() - target.getSpd();
        if (spdDiff > 1 && target.getDef() > 0) {
            println(yellow(target + " blocks the attack!"));
        } else if (spdDiff > 1) {
            println(yellow(target + " evades the attack with pure dumb luck!"));
        } else {
            println(yellow(target + " dodges the attack!"));
        }
    }

    // COMBAT END

    public void victory() throws InterruptedException{
        halfWait();
        println(green("\n [ VICTORY ]\n"));
    }

    public void gameOver() throws InterruptedException{
        halfWait();
        println(red("\n[ GAME OVER ]\n"));
    }

    
}
