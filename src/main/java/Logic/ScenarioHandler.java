package Logic;

import java.util.ArrayList;

import Domain.Player;
import Domain.Special;
import Domain.Invocation;
import Domain.Scenario.*;
import Services.Locator;
import IO.Statics;

public class ScenarioHandler {

    private Scenario current;
    private SceneSet currentSet;
    private CombatHandler com;
    private Player hero;
    private int nextPlotSceneId;

    public ScenarioHandler() {
        com = new CombatHandler();
        setCurrent(Locator.scenes().scene(-1));
        currentSet = null;
    }

    public void outOfCombatMenu() throws InterruptedException {
        while (true) {
            int choice = Locator.io().outOfCombatMenu(hero);
            if (choice == 1) {
                inspectHero();
            } else if (choice == 2) {
                int upgradeChoice = Locator.io().upgradeMenu();
                if (upgradeChoice == 1) {
                    upgradeSpecials();
                } else if (upgradeChoice == 2) {
                    upgradeInvocations();
                }
            } else if (choice == 0) {
                break;
            }
        }
    }

    public void upgradeInvocations() throws InterruptedException {
        while (true) {
            Invocation inv = Locator.io().chooseUpgradeInvocation(hero);
            if (inv != null) {
                if (Statics.invocationUpgradeCost(inv.getLevel()) <= hero.getSkillPoints()) {
                    int cost = Statics.invocationUpgradeCost(inv.getLevel());
                    Locator.elems().upgradeInvocation(inv);
                    hero.setSkillPoints(hero.getSkillPoints() - cost);
                    Locator.io().skillPointsChange(hero, -cost);
                    Locator.io().upgradedInvocation(inv);
                    Locator.io().inspectInvocation(inv);
                } else {
                    Locator.io().notEnoughSkillPoints();
                }
            } else {
                break;
            }
        }
    }

    public void upgradeSpecials() throws InterruptedException {
        while (true) {
            Special sp = Locator.io().chooseUpgradeSpecial(hero);
            if (sp != null) {
                if (Statics.specialUpgradeCost(sp.getLevel()) <= hero.getSkillPoints()) {
                    int cost = Statics.specialUpgradeCost(sp.getLevel());
                    Locator.elems().upgradeSpecial(sp);
                    hero.setSkillPoints(hero.getSkillPoints() - cost);
                    Locator.io().skillPointsChange(hero, -cost);
                    Locator.io().upgradedSpecial(sp);
                    Locator.io().inspectSpecial(sp);
                } else {
                    Locator.io().notEnoughSkillPoints();
                }
            } else {
                break;
            }
        }
    }

    public void inspectHero() throws InterruptedException {
        Locator.io().inspectUnit(hero);
        while (true) {
            int inspectChoice = Locator.io().printInspectOptions();
            if (inspectChoice == 0) {
                break;
            } else if (inspectChoice == 1) {
                Locator.io().inspectUnit(hero);
            } else if (inspectChoice == 2) {
                Locator.io().inspectUnitBasicAttack(hero);
            } else if (inspectChoice == 3) {
                Locator.io().inspectUnitSpecials(hero);
            } else if (inspectChoice == 4) {
                Locator.io().inspectUnitInvs(hero);
            }
        }
    }

    public void shortRest(Player hero) throws InterruptedException {
        hero.setStati(new ArrayList<>());
        hero.update();
        double hpRegen = (double) (hero.getMaxHp()*0.5);
        int regained = (int) Math.round(hpRegen);
        hero.setHp(hero.getHp() + regained);
        for (Special sp: hero.getSpecials()) {
            sp.setTurnsLeft(0);
        }
        Locator.io().shortRest(hero);
        double chanceToRegainInv = 0.65;
        for (Invocation inv: hero.getInvocations()) {
            if (inv.isUsed()) {
               if (Locator.rng().nextDouble() < chanceToRegainInv) {
                    inv.setUsed(false);
                    chanceToRegainInv *= 0.85;
                    Locator.io().invocationRegained(hero, inv);
                } 
            }
        }
        hero.setSkillPoints(hero.getSkillPoints() + 1);
        Locator.io().skillPointsChange(hero, 1);
    }

    public void longRest(Player hero) throws InterruptedException {
        hero.setStati(new ArrayList<>());
        hero.update();
        hero.setHp(hero.getMaxHp());
        for (Special sp: hero.getSpecials()) {
            sp.setTurnsLeft(0);
        }
        for (Invocation inv: hero.getInvocations()) {
            inv.setUsed(false);
        }
        Locator.io().longRest(hero);
    }

    public void applyReward(Player hero, Reward r) {
        if (r.getStats() != null) {
            hero.setBaseHp(hero.getBaseHp() + r.getStats()[0]);
            hero.setHp(hero.getHp() + r.getStats()[0]);
            hero.setStr(hero.getStr() + r.getStats()[1]);
            hero.setDef(hero.getDef() + r.getStats()[2]);
            hero.setSpd(hero.getSpd() + r.getStats()[3]);
        }
        if (r.getAtk() != null) {
            hero.setBasicAtk(r.getAtk());
        }
        if (r.getSp() != null) {
            hero.addSpecial(r.getSp());
        }
        if (r.getInv() != null) {
            hero.addInvocation(r.getInv());
        }
    }

    public void runPlayerChoose() throws InterruptedException {
        setCurrent(Locator.scenes().scene(-1));
        Locator.io().printSceneDescrip(current);
        ChoiceScenario scene = (ChoiceScenario) current;
        Choice choice = Locator.io().makeChoice(hero, scene.getChoices().getChoices());
        if (choice.getDescrip().equals("Esther")) {
            setHero(new Player(Locator.elems().villain("Esther")));
        } else if (choice.getDescrip().equals("Nuray")) {
            setHero(new Player(Locator.elems().villain("Nuray")));
        } else {
            setHero(new Player(Locator.elems().villain("Shula")));
        }
        hero.update();
        inspectHero();
        setCurrent(scene.getNextScene());
    }

    public void randomEncounterChoice() throws InterruptedException {
        ChoiceScenario scene = (ChoiceScenario) current;
        Choice choice = Locator.io().makeChoice(hero, scene.getChoices().getChoices());
        if (choice.getResult().equals("Easy")) {
            setCurrent(Locator.scenes().randomEncounterEasy(hero));
        } else if (choice.getResult().equals("Medium")) {
            setCurrent(Locator.scenes().randomEncounterMedium(hero));
        } else if (choice.getResult().equals("Hard")) {
            setCurrent(Locator.scenes().randomEncounterHard(hero));
        } else {
            longRest(hero);
            setCurrent(Locator.scenes().scene(nextPlotSceneId));
        }
    }

    public boolean runScene(Scenario scene) throws InterruptedException {
        if (scene.getId() == 1) {
            Locator.io().printSceneDescrip(scene);
            return false;
        }
        if (scene instanceof HeroScenario) {
            HeroScenario hs = new HeroScenario((HeroScenario) scene);
            scene = hs.getHeroScene(hero);
        }
        Locator.io().printSceneDescrip(scene);
        if (scene instanceof ChoiceScenario) {
            ChoiceScenario chsc = new ChoiceScenario((ChoiceScenario) scene);
            if (chsc.getId() == -8) {
                randomEncounterChoice();
            } else {
                Choice choice = Locator.io().makeChoice(hero, chsc.getChoices().getChoices());
                if (choice.getResult().startsWith("SET:")) {
                    choice.setResult(Locator.scenes().sceneSetFirst(choice.getResult().substring(4)).getName());
                }
                chsc.setNextScene(Locator.scenes().scene(choice.getResult()));
                setCurrent(chsc.getNextScene());
                if (getCurrent() == null) {
                    return false;
                }
            }
        }
        if (scene instanceof CombatScenario) {
            CombatScenario cosc = new CombatScenario((CombatScenario) scene);
            if (cosc.getId() == -2 || cosc.getId() == -3 || cosc.getId() == -4) {
                cosc.setNextScene(Locator.scenes().randomEncounterReward(hero, cosc));
            }
            com.setCombat(hero, cosc.getCombat());
            boolean victory = com.combatVictory();
            if (!victory) {
                return false;
            } else {
                hero.setStati(new ArrayList<>());
                hero.update();
            }
            setCurrent(cosc.getNextScene());
            if (getCurrent() == null) {
                return false;
            }
        }
        if (scene instanceof RewardScenario) {
            RewardScenario rsc = new RewardScenario((RewardScenario) scene);
            if (rsc.getId() == -9) {
               rsc.setNextScene(Locator.scenes().scene(-8));
            }
            applyReward(hero, rsc.getReward());
            hero.update();
            Locator.io().announceReward(hero, rsc.getReward());
            shortRest(hero);
            outOfCombatMenu();
            setCurrent(rsc.getNextScene());
            if (getCurrent() == null) {
                return false;
            }
        }
        if (getCurrent() == null) {
            return false;
        }
        SceneSet set = Locator.scenes().findSceneSetByScene(current);
        if (set != null && currentSet != null) {
            if (!set.getName().equals(currentSet.getName())) {
                currentSet = set;
                nextPlotSceneId = current.getId();
                setCurrent(Locator.scenes().scene(-8));
            }
        } else if (currentSet == null && set != null) {
            currentSet = set;
        } else if (set == null && currentSet != null) {
            currentSet = set;
            if (scene.getId() != -8) {
                nextPlotSceneId = current.getId();
                setCurrent(Locator.scenes().scene(-8));
            }
        }
        
        return true;
    }

    public Scenario getCurrent() {
        return current;
    }

    public void setCurrent(Scenario current) {
        this.current = current;
    }

    public Player getHero() {
        return hero;
    }

    public void setHero(Player hero) {
        this.hero = hero;
    }
}
