package Logic;

import Domain.*;
import Domain.Scenario.Combat;
import Services.Locator;

public class CombatHandler {
    Player hero;
    Combat combat;
    CombatLogic logic;

    public CombatHandler() {
        this.logic = new CombatLogic();
    }

    public void setCombat(Player hero, Combat combat) {
        this.hero = hero;
        this.combat = combat;
        logic.init(hero, combat);
    }

    public boolean combatVictory() throws InterruptedException {
        combat.setCombatOn(true);
        int turn = 1;

        while (combat.isCombatOn()) {
            Locator.comIO().turnStart(turn);
            logic.initiativeOrder();
            for (int i = 0; i < logic.getUnitList().size(); i++) {
                Unit unit = logic.getUnitList().get(i);
                if (isLoss()) {
                    return false;
                }
                if (isVictory()) {
                    return true;
                }
                if (!unit.isAlive()) {
                    logic.getVillainList().remove(unit);
                    logic.getUnitList().remove(i);
                    i--;
                    continue;
                } else {
                    Locator.comIO().turnDeclaration(unit);
                }
                logic.processStatusDamage(unit);
                if (isLoss()) {
                    return false;
                }
                if (isVictory()) {
                    return true;
                }
                if (!unit.isAlive()) {
                    logic.getUnitList().remove(i);
                    logic.getVillainList().remove(unit);
                    i--;
                    continue;
                }
                logic.processCooldowns(unit);
                Unit target;
                if (unit instanceof Player) {
                    heroTurn();
                } else {
                    target = hero;
                    Invocation inv = logic.chosenInvocation(unit);
                    if (inv != null) {
                        logic.invoke(unit, inv);
                    }
                    if (isLoss()) {
                    return false;
                    }
                    if (isVictory()) {
                        return true;
                    }
                    Attack atk = logic.chosenAttack(unit);
                    if (atk instanceof Special) {
                        logic.specialSingle(unit, target, (Special)atk);
                    } else {
                        logic.attackSingle(unit, target, atk);
                    }
                }
            }
            if (isLoss()) {
                return false;
            }
            if (isVictory()) {
                return true;
            }
            for (int i = 0; i < logic.getUnitList().size(); i++) {
                if (!logic.getUnitList().get(i).isAlive()) {
                    Unit removable = logic.getUnitList().get(i);
                    logic.getUnitList().remove(removable);
                    logic.getVillainList().remove(removable);
                    i--;
                }
            }
            if (logic.getVillainList().isEmpty()) {
                Locator.comIO().victory();
                combat.setCombatOn(false);
                combat.setVictory(true);
                return true;
            }
            turn++;
        }
        return combat.isVictory();
    }

    private void heroTurn() throws InterruptedException {
        boolean hasInvoked = false;
        while (true) {
            if (isLoss() || isVictory()) {
                break;
            }
            Locator.comIO().heroTurnStart(logic.getVillainList());
            int choice = Locator.comIO().actionChoice(hero, logic.getVillainList(), "Attack", "Special", "Invoke", "Inspect");
            if (choice == 1) {
                Attack atk = hero.getBasicAtk();
                Enemy target = Locator.comIO().selectEnemy(hero, atk, logic.getVillainList());
                if (target != null) {
                    logic.attackSingle(hero, target, atk);
                    break;
                }
            } else if (choice == 2) {
                if (!hero.readySpecials().isEmpty()) {
                    Special sp = Locator.comIO().specialChoice(hero, hero.readySpecials());
                    if (sp != null) {
                        if (logic.handleSpecialHero(hero, sp)) {
                            break;
                        }
                    }
                } else {
                    Locator.comIO().noAvailableSpecials();
                }
            } else if (choice == 3){
                if (!hero.readyInvocations().isEmpty() && !hasInvoked) {
                    Invocation inv = Locator.comIO().invocationChoice(hero, hero.readyInvocations());
                    if (inv != null) {
                        if (logic.handleInvocationHero(hero, inv)) {
                            hasInvoked = true;
                            if (!inv.isTargetsSelf()) {
                                for (int i = 0; i < logic.getVillainList().size(); i++) {
                                    if (!logic.getVillainList().get(i).isAlive()) {
                                        logic.getVillainList().remove(i);
                                        i--;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (hasInvoked) {
                        Locator.comIO().cannotInvokeAgain();
                    } else {
                        Locator.comIO().noAvailableInvocations();
                    }
                }
            } else if (choice == 4) {
                while (true) {
                    Unit u = Locator.comIO().selectUnit(logic.getUnitList());
                    if (u != null) {
                        Locator.io().inspectUnit(u);
                        while (true) {
                            int inspectChoice = Locator.io().printInspectOptions();
                            if (inspectChoice == 0) {
                                break;
                            } else if (inspectChoice == 1) {
                                Locator.io().inspectUnit(u);
                            } else if (inspectChoice == 2) {
                                Locator.io().inspectUnitBasicAttack(u);
                            } else if (inspectChoice == 3) {
                                Locator.io().inspectUnitSpecials(u);
                            } else if (inspectChoice == 4) {
                                Locator.io().inspectUnitInvs(u);
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private boolean isLoss() throws InterruptedException {
        if (!hero.isAlive() && combat.isCombatOn()) {
            combat.setCombatOn(false);
            combat.setVictory(false);
            Locator.comIO().gameOver();
            return true;
        }
        return false;
    }

    private boolean isVictory() throws InterruptedException {
        boolean someAlive = false;
        for (Enemy e: logic.getVillainList()) {
            if (e.isAlive()) {
                someAlive = true;
                break;
            }
        }
        if (!someAlive) {
            Locator.comIO().victory();
            return true;
        }
        return false;
    }
}
