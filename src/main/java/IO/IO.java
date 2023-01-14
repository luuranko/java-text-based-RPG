package IO;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import Domain.*;
import Domain.Scenario.*;
import Services.Locator;

public class IO implements AnyIO {
    protected String pad; // Padding for printed Strings
    protected PlayerInput input;

    public IO() {
        this.pad = "    ";
    }

    public void init() {
        this.input = Locator.input();
    }

    // WAITING

    public void secWait() throws InterruptedException {
        // TimeUnit.MILLISECONDS.sleep(700);
        TimeUnit.MILLISECONDS.sleep(500);
    }

    public void halfWait() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(350);
        // TimeUnit.MILLISECONDS.sleep(100);
    }

    public void smallWait() throws InterruptedException {
        // TimeUnit.MILLISECONDS.sleep(100);
        TimeUnit.MILLISECONDS.sleep(50);
    }

    // PRINTING, FORMATTING

    public void print(String s) {
        System.out.print(s);
    }

    public void println(String s) throws InterruptedException {
        System.out.println(s);
        halfWait();
    }

    public void printFast(String s) throws InterruptedException {
        System.out.println(s);
        smallWait();
    }

    public void printArrow(String s) throws InterruptedException {
        System.out.println("> " + s);
        halfWait();
    }

    public void printInspect(String s) throws InterruptedException {
        System.out.println(s);
        smallWait();
    }

    public boolean printAndWaitInput(String s) throws InterruptedException {
        System.out.print(s);
        if (input instanceof AutoInput) {
            System.out.println();
        }
        return input.inputEnter();
    }

    public String formatDouble(double d) {
        return Statics.roundDouble(d*100) + "%";
    }

    // TEXT STYLE

    public String bold(String s) {
        return Statics.STYLE_BOLD + s + Statics.RESET;
    }

    public String italic(String s) {
        return Statics.STYLE_ITALIC + s + Statics.RESET;
    }

    public String underline(String s) {
        return Statics.STYLE_UNDERLINE + s + Statics.RESET;
    }

    public String faint(String s) {
        return Statics.STYLE_FAINT + s + Statics.RESET;
    }

    // TEXT COLORS

    public String red(String s) {
        return Statics.RED + s + Statics.RESET;
    }

    public String green(String s) {
        return Statics.GREEN + s + Statics.RESET;
    }

    public String yellow(String s) {
        return Statics.YELLOW + s + Statics.RESET;
    }

    public String blue(String s) {
        return Statics.BLUE + s + Statics.RESET;
    }

    public String purple(String s) {
        return Statics.PURPLE + s + Statics.RESET;
    }

    public String cyan(String s) {
        return Statics.CYAN + s + Statics.RESET;
    }

    public String grey(String s) {
        return Statics.GREY + s + Statics.RESET;
    }

    // Formatting 

    public String ifNegThenRedDouble(double num) {
        String txt = formatDouble(num);
        if (num < 0) {
            return red(txt);
        }
        return txt;
    }

    public String ifNegThenRedInt(int num) {
        if (num < 0) {
            return red(num + "");
        }
        return num + "";
    }

    public String unitHpChange(Unit unit) {
        String hp = unitHp(unit);
        return " -> " + bold(hp);
    }

    public String hitChance(double hit) {
        String hr = formatDouble(hit);
        if (hit > 0.7) {
            return green(hr);
        } else if (hit <= 0.4) {
            return red(hr);
        } else {
            return yellow(hr);
        }
    }

    public String critChance(double crit) {
        String cr = formatDouble(crit);
        if (crit >= 0.4) {
            return green(cr);
        } else if (crit < 0.1) {
            return red(cr);
        } else {
            return yellow(cr);
        }
    }

    // OTHER SCENES THAN COMBAT

    public void printSceneDescrip(Scenario scene) throws InterruptedException {
        for (int i = 0; i < scene.getDescrip().getLines().size()-1; i++) {
            String line = scene.getDescrip().getLines().get(i);
            if (line.isEmpty()) {
                printAndWaitInput(line);
            } else {
                println(line);
            }
        }
        if (scene instanceof ChoiceScenario) {
            println(scene.getDescrip().getLines().get(scene.getDescrip().getLines().size()-1));
        } else {
            printAndWaitInput(scene.getDescrip().getLines().get(scene.getDescrip().getLines().size()-1));
        }
     }

    public Choice makeChoice(Player hero, ArrayList<Choice> choices) throws InterruptedException {
        int i = 1;
        for (Choice c: choices) {
            println(bold(grey("[" + i + "] " + c.getDescrip())));
            i++;
        }
        return input.selectedChoice(hero, choices);
    }

    public void announceReward(Player hero, Reward r) throws InterruptedException {
        if (r.getAtk() != null) {
            printAndWaitInput(blue("[ " + hero + " gains a new basic Attack: " + r.getAtk() + "! ]"));
        }
        if (r.getSp() != null) {
            printAndWaitInput(purple("[ " + hero + " gains a new Special Attack: " + r.getSp() + "! ]"));
        }
        if (r.getInv() != null) {
            printAndWaitInput(green("[ " + hero + " gains a new Invocation: " + r.getInv() + "! ]"));
        }
        if (r.getStats() != null) {
            String txt = "[ " + hero + " gains ";
            for (int i = 0; i < r.getStats().length; i++) {
                int stat = r.getStats()[i];
                if (stat != 0) {
                    txt += Statics.statNames[i] + " ";
                    if (stat > 0) {
                        txt += "+" + stat + " ";
                    } else if (stat < 0) {
                        txt += stat + " ";
                    }
                }
                
            }
            txt += "]";
            printAndWaitInput(bold(txt));
        }
    }

    public void announceUnitState(Player hero) throws InterruptedException {
        println(hero + "'s updated overview:");
        inspectUnit(hero);
    }

    public void longRest(Player hero) throws InterruptedException {
        printAndWaitInput(hero + " rests and is fully refreshed.");
    }

    public void shortRest(Player hero) throws InterruptedException {
        printAndWaitInput(hero + " takes a small breather. " + unitHpChange(hero));
    }

    public void invocationRegained(Player hero, Invocation inv) throws InterruptedException {
        printAndWaitInput(green(hero + " can invoke " + inv + " again."));
    }

    public int outOfCombatMenu(Player hero) throws InterruptedException {
        printFast(hero + " will...");
        String txt = pad + bold("[0] Advance");
        txt += pad + grey("[1] Inspect");
        txt += pad + blue("[2] Spend Skill Points (" + hero.getSkillPoints() + ")");
        printFast(txt);
        return input.outOfCombatChoice(2, hero);
    }

    public void notEnoughSkillPoints() throws InterruptedException {
        printFast(pad + red("Not enough Skill Points to upgrade."));
    }

    public int upgradeMenu() throws InterruptedException {
        printFast("Upgrade what?");
        printFast(pad + grey("[0] Cancel") + pad + purple("[1] Specials") + pad + green("[2] Invocations"));
        return input.chooseToUpgrade(2);
    }

    public Invocation chooseUpgradeInvocation(Player hero) throws InterruptedException {
        printFast(pad + bold(green("Invocations")) + pad + "Skill Points: " + hero.getSkillPoints());
        printFast(pad + grey("[0] Cancel"));
        int rowLimit;
        if (hero.getInvocations().size() >= 5) {
            rowLimit = 3;
        } else {
            rowLimit = 2;
        }
        int i = 1;
        String print = "";
        int printSize = 0;
        for (Invocation inv: hero.getInvocations()) {
            int cost = Statics.invocationUpgradeCost(inv.getLevel());
            String txt = pad + "[" + i + "] " + inv
                + " (Cost: " + cost + ")";
            if (cost > hero.getSkillPoints()) {
                txt = red(txt);
            } else {
                txt = green(txt);
            }
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
        return input.upgradableInvocation(hero);
    }

    public Special chooseUpgradeSpecial(Player hero) throws InterruptedException {
        printFast(pad + bold(purple("Specials")) + pad + "Skill Points: " + hero.getSkillPoints());
        printFast(pad + grey("[0] Cancel"));
        int rowLimit;
        if (hero.getSpecials().size() >= 5) {
            rowLimit = 3;
        } else {
            rowLimit = 2;
        }
        int i = 1;
        String print = "";
        int printSize = 0;
        for (Special sp: hero.getSpecials()) {
            int cost = Statics.specialUpgradeCost(sp.getLevel());
            String txt = pad + "[" + i + "] " + sp
                + " (Cost: " + cost + ")";
            if (cost > hero.getSkillPoints()) {
                txt = red(txt);
            } else {
                txt = green(txt);
            }
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
        return input.upgradableSpecial(hero);
    }

    public void upgradedInvocation(Invocation inv) throws InterruptedException {
        printFast(green(inv + " has been upgraded to level " + (inv.getLevel()+1) + "!"));
    }

    public void upgradedSpecial(Special sp) throws InterruptedException {
        printFast(purple(sp + " has been upgraded to level " + (sp.getLevel()+1) + "!"));
    }

    public void skillPointsChange(Player hero, int amount) throws InterruptedException {
        if (amount < 0) {
            println(hero + " spends " + -amount + " Skill Points.");
        } else if (amount > 0) {
            if (amount == 1) {
                println(hero + " gains 1 Skill Point.");
            } else {
               println(hero + " gains " + amount + " Skill Points."); 
            }
            
        }
    }

    // INSPECTIONS

    public String unitHp(Unit u) {
        int hp = u.getHp();
        int max = u.getMaxHp();
        if (hp > max*0.5) {
            return green("(" + hp + "/" + max + " HP)");
        } else if (hp <= max*0.33) {
            return red("(" + hp + "/" + max + " HP)");
        } else {
            return yellow("(" + hp + "/" + max + " HP)");
        }
    }

    public void inspectUnit(Unit u) throws InterruptedException {
        printInspect("\n<< " + bold(u.getName().toUpperCase()) + " " + unitHp(u) + " >>");
        printInspect(pad + "STR " + ifNegThenRedInt(u.getStr()) + pad + "DEF " + ifNegThenRedInt(u.getDef()) + pad + "SPD " + ifNegThenRedInt(u.getSpd()));
        printInspect(pad + "HR Mod " + ifNegThenRedDouble(u.hrMod()) + pad + "CR Mod " + ifNegThenRedDouble(u.crMod()));
        printInspect(pad + "Evasion " + ifNegThenRedDouble(u.evasion()) + pad + "Crit Res " + ifNegThenRedDouble(u.crResist()) + pad + "Debuff Res " + ifNegThenRedDouble(u.dbResist()) + " ]");
        for (Resistance r: u.getResistances()) {
            inspectResistance(r);
        }
        println("");
        if (hasStatus(u)) {
            printInspect("Current Status Effects");
            for (Status s: u.getStati()) {
                if (s.getId() != 0) {
                    inspectStatus(s);
                }
            }
            println("");
        }
    }

    public void inspectUnitBasicAttack(Unit u) throws InterruptedException {
        printInspect(blue("[Basic Attack]"));
        inspectAttack(u.getBasicAtk());
    }

    public void inspectUnitSpecials(Unit u) throws InterruptedException {
        if (!u.getSpecials().isEmpty()) {
            printInspect(purple("[Special Attacks]"));
            for (Special sp: u.getSpecials()) {
                inspectSpecial(sp);
            }
        } else {
            printInspect(u + " has no Special Attacks.");
        }
    }

    public void inspectUnitInvs(Unit u) throws InterruptedException {
        if (!u.getInvocations().isEmpty()) {
            printInspect(green("[Invocations]"));
            for (Invocation inv: u.getInvocations()) {
                inspectInvocation(inv);
            }
        } else {
            printInspect(u + " has no Invocations.");
        }
    }

    public int printInspectOptions() throws InterruptedException {
        String print = pad + grey("[0] Return");
        print += pad + bold("[1] Current State");
        print += pad + blue("[2] Basic Attack");
        print += pad + purple("[3] Special Attacks");
        print += pad + green("[4] Invocations");
        printFast(print);
        int choice = input.selectedInspection(4);
        return choice;
    }

    public void inspectResistance(Resistance r) throws InterruptedException {
        String chance = "";
        if (r.getChance() < 0) {
            chance = formatDouble(r.getChance());
        } else {
            chance = "+" + formatDouble(r.getChance());
        }
        if (r.getEffect() != null && r.getStatus() != null) {
            printInspect(cyan(pad + r + ": " + chance + " chance to resist " + r.getEffect() + " and becoming " + r.getStatus() + "."));
        } else if (r.getEffect() != null) {
            printInspect(cyan(pad + r + ": " + chance + " chance to resist " + r.getEffect() + "."));
        } else if (r.getStatus() != null) {
            printInspect(cyan(pad + r + ": " + chance + " chance to resist becoming " + r.getStatus() + "."));
        }
    }

    public void inspectAttack(Attack a) throws InterruptedException {
        printInspect(blue(" << " + a + " >>"));
        printInspect(pad + "PWR " + a.getPwr() + pad + "HR " + formatDouble(a.getHr()) + pad + "CR " + formatDouble(a.getCr()) + " ]");
        println("");
    }

    public void inspectSpecial(Special sp) throws InterruptedException {
        printInspect(purple(" << " + sp + " >>"));
        printInspect(pad + "Level " + (sp.getLevel()+1));
        String cd = pad + "Cooldown " + sp.getCooldown() + pad;
        if (sp.isReady()) {
            cd += green("Ready now");
        } else {
            cd += "Ready in " + sp.getTurnsLeft() + " turns";
        }
        printInspect(cd);
        if (sp.targetsAll()) {
            printInspect(pad + "Targets all enemies.");
        } else {
            printInspect(pad + "One target.");
        }
        printInspect(pad + "PWR " + sp.getPwr() + pad + "HR " + formatDouble(sp.getHr()) + pad + "CR " + formatDouble(sp.getCr()) + " ]");
        if (sp.getTargetEffect() != null) {
            printInspect(cyan("[Effect inflicted on target on hit]"));
            printInspect("Base Chance: " + hitChance(sp.getChance()));
            inspectEffect(sp.getTargetEffect());
        }
        if (sp.getTargetStatus() != null) {
            printInspect(cyan("[Status inflicted on target on success]"));
            printInspect("Base Chance: " + hitChance(sp.getChance()));
            inspectStatus(sp.getTargetStatus());
        }
        if (sp.getSelfEffect() != null) {
            printInspect(cyan("[Effect granted to self on success]"));
            printInspect("Base Chance: " + hitChance(sp.getChance()));
            inspectEffect(sp.getSelfEffect());
        }
        if (sp.getSelfStatus() != null) {
            printInspect(cyan("[Status granted to self on success]"));
            printInspect("Base Chance: " + hitChance(sp.getChance()));
            inspectStatus(sp.getSelfStatus());
        }
        
        println("");
    }

    public void inspectInvocation(Invocation inv) throws InterruptedException {
        printInspect(green(" << " + inv + " >>"));
        if (!inv.isUsed()) {
            printInspect(pad + green("Ready now"));
        } else {
            printInspect(pad + red("Already used"));
        }
        if (inv.isTargetsSelf()) {
            if (inv.getEffect() != null) {
                printInspect(cyan("[Effect on self]"));
                printInspect("Base Chance: " + hitChance(inv.getChance()));
                inspectEffect(inv.getEffect());
            }
            if (inv.getStatus() != null) {
                printInspect(cyan("[Status granted to self]"));
                printInspect("Base Chance: " + hitChance(inv.getChance()));
                inspectStatus(inv.getStatus());
            }
        } else {
            if (inv.getEffect() != null) {
                printInspect(cyan("[Effect on enemies]"));
                printInspect("Chance: " + hitChance(inv.getChance()));
                inspectEffect(inv.getEffect());
            }
            if (inv.getStatus() != null) {
                printInspect(cyan("[Status inflicted on enemies]"));
                printInspect("Chance: " + hitChance(inv.getChance()));
                inspectStatus(inv.getStatus());
            }
        }
        
        println("");
    }

    public void inspectEffect(Effect e) throws InterruptedException {
        printInspect(cyan("<< " + e + " >>"));
        if (e.getHpChange() > 0) {
            printInspect(pad + "Heals " + green(e.getHpChange() + "") + " HP.");
        } else if (e.getHpChange() < 0) {
            printInspect(pad + "Deals " + red(-e.getHpChange() + "") + " damage.");
        }
        if (e.getCooldownChange() > 0) {
            printInspect(pad + "Puts all Specials on a cooldown for " + red(e.getCooldownChange() + "") + " turns.");
        } else if (e.getCooldownChange() < 0) {
            printInspect(pad + "Quickens the cooldown for all Specials on cooldown by " + green(-e.getCooldownChange() + "") + " turns.");
        }
        if (e.isClearBuffs() && e.isClearDebuffs()) {
            printInspect(pad + "Clears all status effects.");
        } else if (e.isClearBuffs()) {
            printInspect(pad + red("Removes all buffs."));
        } else if (e.isClearDebuffs()) {
            printInspect(pad + green("Removes all debuffs."));
        }
        // println("");
    }

    public void inspectStatus(Status s) throws InterruptedException {
        printInspect(cyan(" << " + s + " >>"));
        printInspect(pad + "Turns:  " + s.getDuration());
        String txt = pad;
        for (int i = 0; i < s.getStats().length; i++) {
            int stat = s.getStats()[i];
            if (stat != 0) {
                txt += Statics.statNames[i+1] + " ";
                if (stat > 0) {
                    txt += green("+" + stat) + pad;
                } else if (stat < 0) {
                    txt += red(stat + "")+ pad;
                }
            }
        }
        if (!txt.equals(pad)) {
            printInspect(txt);
        }
        txt = pad;
        int txtHasRates = 0;
        for (int i = 0; i < s.getRates().length; i++) {
            double rate = s.getRates()[i];
            if (rate != 0) {
                txt += Statics.rateNames[i] + " ";
                if (rate > 0) {
                    txt += green("+" + formatDouble(rate)) + pad;
                } else if (rate < 0) {
                    txt += red(formatDouble(rate)) + pad;
                }
                txtHasRates++;
            }
            if (txtHasRates == 2) {
                printInspect(txt);
                txt = pad;
                txtHasRates = 0;
            }
        }
        if (!txt.equals(pad)) {
            printInspect(txt);
        }
        if (s.getHpPerTurn() != 0) {
            if (s.getHpPerTurn() > 0) {
                printInspect(pad + "Heals " + green(s.getHpPerTurn() + "") + " HP per turn.");
            } else {
                printInspect(pad + "Deals " + red(-s.getHpPerTurn() + "") + " damage per turn.");
            }
        }
    }

    public boolean hasStatus(Unit u) {
        return !u.getStati().isEmpty();
    }

    public void gameEnd() throws InterruptedException {
        halfWait();
        println("\n[ THE END ]\n");
    }
}
