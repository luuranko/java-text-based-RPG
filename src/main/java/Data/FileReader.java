package Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import Domain.*;
import Domain.Scenario.*;
import Services.Locator;

public class FileReader {

    private Elements elems;
    private Scenes scenes;

    public FileReader() {
    }

    public void initElems() {
        this.elems = Locator.elems();
    }

    public void initScenes() {
        this.scenes = Locator.scenes();
    }

    public boolean loadElems(String effectFile, String statusFile, String attackFile, String specialFile, String enemyFile, String invocationFile, String resistanceFile) {
        if (loadEffects(effectFile) == false) {
            return false;
        }
        // System.out.println();
        if (loadStati(statusFile) == false) {
            return false;
        }
        // System.out.println();
        if (loadResistances(resistanceFile) == false) {
            return false;
        }
        // System.out.println();
        if (loadAttacks(attackFile) == false) {
            return false;
        }
        // System.out.println();
        if (loadSpecials(specialFile) == false) {
            return false;
        }
        // System.out.println();
        if (loadInvocations(invocationFile) == false) {
            return false;
        }
        // System.out.println();
        if (loadEnemies(enemyFile) == false) {
            return false;
        }
        // System.out.println();
        return true;
    }

    public boolean loadScenes(String descripFile, String choiceSetFile, String scenesFile) {
        if (loadDescrips(descripFile) == false) {
            return false;
        }
        if (loadChoiceSets(choiceSetFile) == false) {
            return false;
        }
        if (loadScenarios(scenesFile) == false) {
            return false;
        }
        return true;
    }

    public boolean loadEffects(String fileName) {
        try {
            File file = new File(fileName);
            Scanner sc = new Scanner(file);
            boolean buff = true;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.equals(";")) {
                    buff = false;
                } else if (line.startsWith("//") || line.isEmpty()) {
                    continue;
                } else {
                    createEffect(line, buff);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " was not found.");
            return false;
        }
        return true;
    }

    public boolean loadStati(String fileName) {
        try {
            File file = new File(fileName);
            Scanner sc = new Scanner(file);
            boolean buff = true;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.equals(";")) {
                    buff = false;
                } else if (line.startsWith("//") || line.isEmpty()) {
                    continue;
                } else {
                    createStatus(line, buff);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " was not found.");
            return false;
        }
        return true;
    }

    public boolean loadResistances(String fileName) {
        try {
            File file = new File(fileName);
            Scanner sc = new Scanner(file);
            boolean buff = true;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.equals(";")) {
                    buff = false;
                } else if (line.startsWith("//") || line.isEmpty()) {
                    continue;
                } else {
                    createResistance(line, buff);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " was not found.");
            return false;
        }
        return true;
    }

    public boolean loadAttacks(String fileName) {
        try {
            File file = new File(fileName);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("//") || line.isEmpty()) {
                    continue;
                } else {
                    createAttack(line);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " was not found.");
            return false;
        }
        return true;
    }

    public boolean loadSpecials(String fileName) {
        try {
            File file = new File(fileName);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("//") || line.isEmpty()) {
                    continue;
                } else {
                    createSpecial(line);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " was not found.");
            return false;
        }
        return true;
    }

    public boolean loadInvocations(String fileName) {
        try {
            File file = new File(fileName);
            Scanner sc = new Scanner(file);
            boolean targetsSelf = true;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.equals(";")) {
                    targetsSelf = false;
                } else if (line.startsWith("//") || line.isEmpty()) {
                    continue;
                } else {
                    createInvocation(line, targetsSelf);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " was not found.");
            return false;
        }
        return true;
    }

    public boolean loadEnemies(String fileName) {
        try {
            File file = new File(fileName);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("//") || line.isEmpty()) {
                    continue;
                } else {
                    createEnemy(line);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " was not found.");
            return false;
        }
        return true;
    }

    public boolean loadDescrips(String fileName) {
        try {
            File file = new File(fileName);
            Scanner sc = new Scanner(file);
            int id = -99;
            ArrayList<String> lines = new ArrayList<>();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("//")) {
                    continue;
                } else if (line.startsWith(">")){
                    id = Integer.parseInt(line.substring(1));
                } else if (line.equals(";")) {
                    if (id != -99 && !lines.isEmpty()) {
                        ArrayList<String> finished = new ArrayList<>();
                        finished.addAll(lines);
                        Description d = new Description(id, finished);
                        id = -99;
                        lines.clear();
                        scenes.getDescrips().put(d.getId(), d);
                    }
                } else {
                    lines.add(line);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " was not found.");
            return false;
        }
        return true;
    }

    public boolean loadChoiceSets(String fileName) {
        try {
            File file = new File(fileName);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("//") || line.isEmpty()) {
                    continue;
                } else {
                    createChoiceSet(line);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " was not found.");
            return false;
        }
        return true;
    }

    public boolean loadScenarios(String fileName) {
        try {
            File file = new File(fileName);
            Scanner sc = new Scanner(file);
            SceneSet set = null;
            HeroScenario hs = null;
            int scenesInHs = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("//") || line.isEmpty()) {
                    continue;
                } else if (line.startsWith("{")) {
                    set = new SceneSet(line.substring(1));
                } else if (line.startsWith("}")) {
                    set.finish();
                    scenes.addSceneSet(set);
                    set = null;
                } else if (line.startsWith("[")) {
                    hs = new HeroScenario(scenes.getAllScenes().keySet().size()+1, line.substring(1));
                    scenes.getAllScenes().put(hs.getName(), hs);
                    scenes.getAllScenesID().put(hs.getId(), hs);
                    if (set != null) {
                        set.addScene(hs);
                    }
                } else if (line.startsWith("]")) {
                    hs = null;
                    scenesInHs = 0;
                } else {
                    Scenario scene = createScenario(line);
                    if (set != null) {
                        set.addScene(scene);

                    }
                    if (hs != null) {
                        if (scenesInHs == 0) {
                            hs.setFirstHeroScene(scene);
                            scenesInHs++;
                        } else if (scenesInHs == 1) {
                            hs.setSecondHeroScene(scene);
                            scenesInHs++;
                        } else {
                            hs.setThirdHeroScene(scene);
                            scenesInHs++;
                        }
                    }
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " was not found.");
            return false;
        }
        scenes.finishSceneList();
        return true;
    }

    // CREATING OBJECTS

    private void createEffect(String data, boolean buff) {
        int id = elems.getEffects().keySet().size();
        String[] parts = data.split(";");
        String name = parts[0];
        int hpChange = Integer.parseInt(parts[1]);
        int cooldownChange = Integer.parseInt(parts[2]);
        boolean clearBuffs = Boolean.parseBoolean(parts[3]);
        boolean clearDebuffs = Boolean.parseBoolean(parts[4]);
        Effect e = new Effect(id, name, hpChange, cooldownChange, clearBuffs, clearDebuffs, buff);
        elems.getEffects().put(e.getName(), e);

        // System.out.println(name + " " + CombatCalculation.calcEffectRating(e));
    }

    private void createStatus(String data, boolean buff) {
        int id = elems.getStati().keySet().size();
        String[] parts = data.split(";");
        String name = parts[0];
        int duration = Integer.parseInt(parts[1]);
        int[] stats = new int[3];
        String[] help;
        if (!parts[2].equals("-")) {
            help = parts[2].split(",");
            stats[0] = Integer.parseInt(help[0]);
            stats[1] = Integer.parseInt(help[1]);
            stats[2] = Integer.parseInt(help[2]);
        }
        double[] rates = new double[5];
        if (!parts[3].equals("-")) {
            help = parts[3].split(",");
            rates[0] = Double.parseDouble(help[0]);
            rates[1] = Double.parseDouble(help[1]);
            rates[2] = Double.parseDouble(help[2]);
            rates[3] = Double.parseDouble(help[3]);
            rates[4] = Double.parseDouble(help[4]);
        }
        int hpPerTurn = Integer.parseInt(parts[4]);
        Status s = new Status(id, name, duration, stats, rates, hpPerTurn, false);
        if (buff) {
            s.setBuff(true);
        }
        elems.getStati().put(s.getName(), s);

        // System.out.println(name + " " + CombatCalculation.calcStatusRating(s));
    }

    private void createResistance(String data, boolean buff) {
        int id = elems.getResistances().keySet().size();
        String[] parts = data.split(";");
        String name = parts[0];
        Effect eff = elems.effect(parts[1]);
        Status status = elems.status(parts[2]);
        double chance = Double.parseDouble(parts[3]);
        Resistance r = new Resistance(id, name, eff, status, chance, buff);
        elems.getResistanceList().add(r);
        elems.getResistances().put(r.getName(), r);

        // System.out.println(name + " " + CombatCalculation.calcResistanceRating(r));
    }

    private void createAttack(String data) {
        int id = elems.getAttackList().size();
        String[] parts = data.split(";");
        String name = parts[0];
        int pwr = Integer.parseInt(parts[1]);
        double hr = Double.parseDouble(parts[2]);
        double cr = Double.parseDouble(parts[3]);
        Attack a = new Attack(id, name, pwr, hr, cr);
        // elems.getAttackList().add(a);
        elems.getAttacks().put(a.getName(), a);

        // System.out.println(name + " " + CombatCalculation.calcAttackRating(a));
    }

    private void createSpecial(String data) {
        int id = elems.getSpecialList().size();
        String[] parts = data.split(";");
        String name = parts[0];
        int pwr = Integer.parseInt(parts[1]);
        double hr = Double.parseDouble(parts[2]);
        double cr = Double.parseDouble(parts[3]);
        boolean targetAll = Boolean.parseBoolean(parts[4]);
        int cooldown = Integer.parseInt(parts[5]);
        Special sp = new Special(id, name, pwr, hr, cr, targetAll, cooldown);
        Effect selfEffect = elems.effect(parts[6]);
        sp.setSelfEffect(selfEffect);
        Status selfStatus = elems.status(parts[7]);
        sp.setSelfStatus(selfStatus);
        Effect targetEffect = elems.effect(parts[8]);
        sp.setTargetEffect(targetEffect);
        Status targetStatus = elems.status(parts[9]);
        sp.setTargetStatus(targetStatus);
        double chance = Double.parseDouble(parts[10]);
        sp.setChance(chance);
        elems.getSpecialList().add(sp);
        elems.getSpecials().put(sp.getName(), sp);

        // System.out.println(name + " " + CombatCalculation.calcSpecialRating(sp));
    }

    private void createInvocation(String data, boolean targetsSelf) {
        int id = elems.getInvocationList().size();
        String[] parts = data.split(";");
        String name = parts[0];
        Effect effect = elems.effect(parts[1]);
        Status status = elems.status(parts[2]);
        double chance = Double.parseDouble(parts[3]);
        Invocation inv = new Invocation(id, name, effect, status, targetsSelf, chance);
        elems.getInvocationList().add(inv);
        elems.getInvocations().put(inv.getName(), inv);

        // System.out.println(name + " " + CombatCalculation.calcInvocationRating(inv));
    }

    private void createEnemy(String data) {
        int id = elems.getVillainList().size();
        String[] parts = data.split(";");
        String name = parts[0];
        int[] stats = new int[4];
        String[] help = parts[1].split(",");
        stats[0] = Integer.parseInt(help[0]);
        stats[1] = Integer.parseInt(help[1]);
        stats[2] = Integer.parseInt(help[2]);
        stats[3] = Integer.parseInt(help[3]);
        Attack atk = elems.attack(parts[2]);
        Enemy e = new Enemy(id, name, stats, atk);
        if (!parts[3].equals("SP")) {
            help = parts[3].split(",");
            for (int i = 0; i < help.length; i++) {
                Special sp = elems.special(help[i]);
                e.addSpecial(sp);
            }
        }
        if (!parts[4].equals("INV")) {
            help = parts[4].split(",");
            for (int i = 0; i < help.length; i++) {
                Invocation inv = elems.invocation(help[i]);
                e.addInvocation(inv);
            }
        }
        if (!parts[5].equals("RES")) {
            help = parts[5].split(",");
            for (int i = 0; i < help.length; i++) {
                Resistance r = elems.resistance(help[i]);
                if (r != null) {
                    e.addResistance(r);
                }
            }
        }
        // if (name.equals("Shula") || name.equals("Nuray") || name.equals("Esther")) {
        //     System.out.println(name + ": " + CombatCalculation.calcUnitTotalRating(new Player((Unit)e)));    
        // }
        elems.addEnemy(e);
    }

    private void createChoiceSet(String data) {
        String[] parts = data.split(";");
        int id = Integer.parseInt(parts[0]);
        ArrayList<Choice> choices = new ArrayList<>();
        String result = "";
        String descrip;
        for (int i = 1; i < parts.length; i++) {
            if (i % 2 == 1) {
                result = parts[i];
            } else {
                descrip = parts[i];
                Choice c = new Choice(descrip, result);
                choices.add(c);
                result = "";
                descrip = "";
            }
        }
        ChoiceSet cs = new ChoiceSet(id, choices);
        scenes.getChoiceSets().put(cs.getId(), cs);
    }

    public Scenario createScenario(String data) {
        String[] parts = data.split(";");
        int id;
        if (!parts[0].equals("-")) {
            id = Integer.parseInt(parts[0]);
        } else {
            id = scenes.getAllScenes().keySet().size()+1;
        }
        String name = parts[1];
        Description descrip = scenes.descrip(Integer.parseInt(parts[2]));
        Scenario nextScene;
        if (parts[3].startsWith("SET:")) {
            nextScene = scenes.sceneSetFirst(parts[3].substring(4));
        } else {
            nextScene = scenes.scene(parts[3]);
        }
        if (parts[4].equals("Choice")) {
            ChoiceSet choices = scenes.choiceSet(Integer.parseInt(parts[5]));
            ChoiceScenario scene = new ChoiceScenario(id, name, descrip, choices);
            scene.setNextScene(nextScene);
            scenes.getChoiceScenes().put(scene.getName(), scene);
            scenes.getChoiceScenesID().put(scene.getId(), scene);
            scenes.getAllScenes().put(scene.getName(), scene);
            scenes.getAllScenesID().put(scene.getId(), scene);
            return scene;
        }
        if (parts[4].equals("Combat")) {
            ArrayList<Enemy> villainList = new ArrayList<>();
            String[] help = parts[5].split(",");
            for (int i = 0; i < help.length; i++) {
                Enemy e = elems.villain(help[i]);
                villainList.add(e);
            }
            Combat combat = new Combat(villainList);
            CombatScenario scene = new CombatScenario(id, name, descrip, combat);
            scene.setNextScene(nextScene);
            scenes.getCombatScenes().put(scene.getName(), scene);
            scenes.getCombatScenesID().put(scene.getId(), scene);
            scenes.getAllScenes().put(scene.getName(), scene);
            scenes.getAllScenesID().put(scene.getId(), scene);
            return scene;
        }
        if (parts[4].equals("Reward")) {
            Attack atk = elems.attack(parts[5]);
            Special sp = elems.special(parts[6]);
            Invocation inv = elems.invocation(parts[7]);
            int[] stats = null;
            if (!parts[8].equals("STAT")) {
                stats = new int[4];
                String[] help = parts[8].split(",");
                stats[0] = Integer.parseInt(help[0]);
                stats[1] = Integer.parseInt(help[1]);
                stats[2] = Integer.parseInt(help[2]);
                stats[3] = Integer.parseInt(help[3]);
            }
            Reward reward = new Reward(atk, sp, inv, stats);
            RewardScenario scene = new RewardScenario(id, name, descrip, reward);
            scene.setNextScene(nextScene);
            scenes.getRewardScenes().put(scene.getName(), scene);
            scenes.getRewardScenesID().put(scene.getId(), scene);
            scenes.getAllScenes().put(scene.getName(), scene);
            scenes.getAllScenesID().put(scene.getId(), scene);
            return scene;
        }
        if (parts[4].equals("Basic")) {
            Scenario scene = new Scenario(id, name, descrip);
            scene.setNextScene(nextScene);
            scenes.getAllScenes().put(scene.getName(), scene);
            scenes.getAllScenesID().put(scene.getId(), scene);
            return scene;
        }
        System.out.println(data);
        System.out.println("WRONGLY FORMATTED SCENARIO FILE");
        return null;
    }
}
