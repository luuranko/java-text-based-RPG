package Data;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

import Domain.Scenario.*;
import Logic.CombatCalculation;
import Domain.*;
import Services.Locator;

public class Scenes {
    private ArrayList<SceneSet> sceneList;
    private HashMap<String, SceneSet> sceneSetsByName;
    private HashMap<String, Scenario> allScenes;
    private HashMap<Integer, Scenario> allScenesID;
    private HashMap<String, ChoiceScenario> choiceScenes;
    private HashMap<Integer, ChoiceScenario> choiceScenesID;
    private HashMap<String, CombatScenario> combatScenes;
    private HashMap<Integer, CombatScenario> combatScenesID;
    private HashMap<String, RewardScenario> rewardScenes;
    private HashMap<Integer, RewardScenario> rewardScenesID;
    private HashMap<Integer, Description> descrips;
    private HashMap<Integer, ChoiceSet> choiceSets;

    public Scenes() {
        this.sceneList = new ArrayList<>();
        sceneSetsByName = new HashMap<>();
        allScenes = new HashMap<>();
        setAllScenesID(new HashMap<>());
        choiceScenes = new HashMap<>();
        choiceScenesID = new HashMap<>();
        combatScenes = new HashMap<>();
        combatScenesID = new HashMap<>();
        rewardScenes = new HashMap<>();
        rewardScenesID = new HashMap<>();
        setDescrips(new HashMap<>());
        choiceSets = new HashMap<>();
    }

    public SceneSet findSceneSetByScene(Scenario scene) {
        for (SceneSet ss: sceneList) {
            if (ss.containsScene(scene)) {
                return ss;
            }
        }
        return null;
    }

    public Scenario sceneSetFirst(String name) {
        if (sceneSetsByName.containsKey(name)) {
            return sceneSetsByName.get(name).first();
        }
        return null;
    }

    public void addSceneSet(SceneSet set) {
        this.sceneList.add(set);
        this.sceneSetsByName.put(set.getName(), set);
    }

    public void finishSceneList() {
        Collections.reverse(sceneList);
    }

    public CombatScenario randomEncounterEasy(Player hero) {
        ArrayList<Enemy> enemies = Locator.elems().randomEncounterEnemies(hero, 1, 0);
        Combat combat = new Combat(enemies);
        CombatScenario scene = new CombatScenario(-2, "Easy Random Encounter",
            descrip(-2), combat);
        return scene;
    }

    public CombatScenario randomEncounterMedium(Player hero) {
        ArrayList<Enemy> enemies = Locator.elems().randomEncounterEnemies(hero, 2, 0);
        Combat combat = new Combat(enemies);
        CombatScenario scene = new CombatScenario(-3, "Medium Random Encounter",
            descrip(-3), combat);
        return scene;
    }

    public CombatScenario randomEncounterHard(Player hero) {
        ArrayList<Enemy> enemies = Locator.elems().randomEncounterEnemies(hero, 3, 0);
        Combat combat = new Combat(enemies);
        CombatScenario scene = new CombatScenario(-4, "Hard Random Encounter",
            descrip(-4), combat);
        return scene;
    }

    public RewardScenario randomEncounterReward(Player hero, CombatScenario random) {
        double heroRating = CombatCalculation.calcUnitTotalRating(hero);
        double combatRating = 0;
        for (Enemy e: random.getCombat().villainList()) {
            String name = e.getName();
            if (name.substring(name.length()-2, name.length()-1).equals(" ")) {
                name = name.substring(0, name.length()-2);
            } else if (name.equals("Shadow Shula")) {
                name = "Shula";
            } else if (name.equals("Shadow Nuray")) {
                name = "Nuray";
            } else if (name.equals("Shadow Esther")) {
                name = "Esther";
            }
            combatRating += Locator.elems().getEnemyRatings().get(name);
        }
        int[] stats = {1,0,0,0};
        double diff = combatRating - heroRating;
        if (diff > 0 && Locator.rng().nextDouble() < 0.1) {
            stats[0]++;
        }
        if (diff > 0 && random.getCombat().villainList().size() > 3) {
            if (Locator.rng().nextDouble() < 0.05) {
                stats[Locator.rng().nextInt(3)+1]++;
            }
        }
        // Might award a new Special in rare cases!
        // Adjust chance later
        Special sp = null;
        if (diff > 0 && Locator.rng().nextDouble() < 0.1) {
            sp = Locator.elems().randomSpecial();
            if (hero.hasSpecial(sp)) {
                sp = null;
            }
        }
        Reward reward = new Reward(null, sp, null, stats);
        RewardScenario scene = new RewardScenario(-9, "Randomized reward for random encounter.", descrip(-9), reward);
        return scene;
    }

    public HashMap<Integer, RewardScenario> getRewardScenesID() {
        return rewardScenesID;
    }

    public void setRewardScenesID(HashMap<Integer, RewardScenario> rewardScenesID) {
        this.rewardScenesID = rewardScenesID;
    }

    public HashMap<Integer, CombatScenario> getCombatScenesID() {
        return combatScenesID;
    }

    public void setCombatScenesID(HashMap<Integer, CombatScenario> combatScenesID) {
        this.combatScenesID = combatScenesID;
    }

    public HashMap<Integer, ChoiceScenario> getChoiceScenesID() {
        return choiceScenesID;
    }

    public void setChoiceScenesID(HashMap<Integer, ChoiceScenario> choiceScenesID) {
        this.choiceScenesID = choiceScenesID;
    }

    public HashMap<Integer, ChoiceSet> getChoiceSets() {
        return choiceSets;
    }

    public void setChoiceSets(HashMap<Integer, ChoiceSet> choiceSets) {
        this.choiceSets = choiceSets;
    }

    public HashMap<Integer, Scenario> getAllScenesID() {
        return allScenesID;
    }

    public void setAllScenesID(HashMap<Integer, Scenario> allScenesID) {
        this.allScenesID = allScenesID;
    }

    public HashMap<Integer, Description> getDescrips() {
        return descrips;
    }

    public void setDescrips(HashMap<Integer, Description> descrips) {
        this.descrips = descrips;
    }

    public Scenario scene(Player hero, String name) {
        if (name.startsWith("SET:")) {
            name = name.substring(4);
        }
        if (name.equals("Random")) {
            System.out.println("\n\n\nSomething went wrong with scene generation\n\n\n");
        }
        if (allScenes.containsKey(name)) {
            if (choiceScenes.containsKey(name)) {
                return choiceScenes.get(name);
            } else if (combatScenes.containsKey(name)) {
                return combatScenes.get(name);
            } else if (rewardScenes.containsKey(name)) {
                return rewardScenes.get(name);
            }
            return allScenes.get(name);
        }
        return null;
    }

    public Scenario scene(String name) {
        if (name.startsWith("SET:")) {
            name = name.substring(4);
        }
        if (allScenes.containsKey(name)) {
            if (choiceScenes.containsKey(name)) {
                return choiceScenes.get(name);
            } else if (combatScenes.containsKey(name)) {
                return combatScenes.get(name);
            } else if (rewardScenes.containsKey(name)) {
                return rewardScenes.get(name);
            }
            return allScenes.get(name);
        }
        return null;
    }

    public Scenario scene(int id) {
        if (allScenesID.containsKey(id)) {
            if (choiceScenesID.containsKey(id)) {
                return choiceScenesID.get(id);
            } else if (combatScenesID.containsKey(id)) {
                return combatScenesID.get(id);
            } else if (rewardScenesID.containsKey(id)) {
                return rewardScenesID.get(id);
            }
            return allScenesID.get(id);
        }
        return null;
    }

    public Description descrip(int id) {
        if (descrips.containsKey(id)) {
            return descrips.get(id);
        }
        return null;
    }

    public ChoiceSet choiceSet(int id) {
        if (choiceSets.containsKey(id)) {
            return choiceSets.get(id);
        }
        return null;
    }

    public HashMap<String, Scenario> getAllScenes() {
        return allScenes;
    }

    public void setAllScenes(HashMap<String, Scenario> allScenes) {
        this.allScenes = allScenes;
    }

    public HashMap<String, ChoiceScenario> getChoiceScenes() {
        return choiceScenes;
    }

    public void setChoiceScenes(HashMap<String, ChoiceScenario> choiceScenes) {
        this.choiceScenes = choiceScenes;
    }

    public HashMap<String, CombatScenario> getCombatScenes() {
        return combatScenes;
    }

    public void setCombatScenes(HashMap<String, CombatScenario> combatScenes) {
        this.combatScenes = combatScenes;
    }

    public HashMap<String, RewardScenario> getRewardScenes() {
        return rewardScenes;
    }

    public void setRewardScenes(HashMap<String, RewardScenario> rewardScenes) {
        this.rewardScenes = rewardScenes;
    }
    

}
