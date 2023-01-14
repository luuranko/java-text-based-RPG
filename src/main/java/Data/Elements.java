package Data;

import java.util.ArrayList;
import java.util.HashMap;

import Domain.*;
import IO.Statics;
import Logic.CombatCalculation;
import Services.Locator;

public class Elements {
    private ArrayList<Enemy> villainList;
    private ArrayList<Attack> attackList;
    private ArrayList<Special> specialList;
    private ArrayList<Invocation> invocationList;
    private ArrayList<Status> statusList;
    private ArrayList<Resistance> resistanceList;
    private HashMap<String, Enemy> villains;
    private HashMap<String, Attack> attacks;
    private HashMap<String, Special> specials;
    private HashMap<String, Invocation> invocations;
    private HashMap<String, Status> stati;
    private HashMap<String, Resistance> resistances;
    private HashMap<String, Effect> effects;

    private HashMap<String, Double> enemyRatings;
    private double lowestRating;

    public Elements() {
        statusList = new ArrayList<>();
        resistanceList = new ArrayList<>();
        stati = new HashMap<>();
        effects = new HashMap<>();
        resistances = new HashMap<>();
        specialList = new ArrayList<>();
        invocationList = new ArrayList<>();
        specials = new HashMap<>();
        invocations = new HashMap<>();
        attackList = new ArrayList<>();
        attacks = new HashMap<>();
        villainList = new ArrayList<>();
        villains = new HashMap<>();

        enemyRatings = new HashMap<>();
        lowestRating = 100.0;
    }

    public void addEnemy(Enemy e) {
        this.villainList.add(e);
        this.villains.put(e.getName(), e);
        this.enemyRatings.put(e.getName(), CombatCalculation.calcUnitTotalRating(e));
        if (enemyRatings.get(e.getName()) < lowestRating) {
            lowestRating = enemyRatings.get(e.getName());
        }
        // System.out.println(e.getName() + ": " + enemyRatings.get(e.getName()));
    }

    // RANDOMIZED ENCOUNTER THING

    public Enemy enemyByRating(double min, double max) {
        // Toistaiseksi kokeilee 50 kertaa randomia vihua ennen kuin luovuttaa
        for (int i = 0; i < 50; i++) {
            Enemy e = randomEnemy();
            if (e.getName().equals("Shula") || e.getName().equals("Esther") || e.getName().equals("Nuray")) {
                continue;
            }
            double rating = enemyRatings.get(e.getName());
            if (rating >= min && rating <= max) {
                return e;
            }
        }
        return null;
    }

    public Enemy randomEnemy() {
        return new Enemy(villainList.get(Locator.rng().nextInt(villainList.size())));
    }

    public Special randomSpecial() {
        return new Special(specialList.get(Locator.rng().nextInt(specialList.size())));
    }

    public void upgradeEnemy(Enemy e, int level) {
        System.out.println("UPGRADING ENEMY");
        for (int i = 0; i < level; i++) {
            for (Special sp: e.getSpecials()) {
                upgradeSpecial(sp);
            }
            for (Invocation inv: e.getInvocations()) {
                upgradeInvocation(inv);
            }
        }
    }

    public void upgradeSpecial(Special sp) {
        int pwr = 0;
        if (sp.targetsAll()) {
            if (Locator.rng().nextDouble() < (Statics.chanceToAddPwr * 0.5)) {
                pwr = 1;
            }
        } else {
            if (Locator.rng().nextDouble() < Statics.chanceToAddPwr) {
                pwr = 1;
            }
        }
        sp.setPwr(sp.getPwr() + pwr);
        sp.setHr(sp.getHr() + Statics.addHr);
        sp.setCr(sp.getCr() + Statics.addCr);

        int cooldownReduction = 0;
        if (sp.targetsAll()) {
            if (Locator.rng().nextDouble() < (Statics.chanceToReduceCooldown * 0.5)) {
                cooldownReduction = 1;
            }
        } else {
            if (Locator.rng().nextDouble() < Statics.chanceToReduceCooldown) {
                cooldownReduction = 1;
            }
        }
        sp.setCooldown(sp.getCooldown() - cooldownReduction);

        double chance;
        if (sp.getSelfEffect() == null && sp.getSelfStatus() == null && sp.getTargetEffect() == null && sp.getTargetStatus() == null) {
            chance = 0;
        } else {
            chance = Statics.addChance;
            if (sp.targetsAll()) {
                chance *= 0.5;
            }
        }
        sp.setChance(sp.getChance() + chance);

        if (sp.getSelfEffect() != null) {
            upgradeEffect(sp.getSelfEffect());
        }
        if (sp.getSelfStatus() != null) {
            upgradeStatus(sp.getSelfStatus());
        }
        if (sp.getTargetEffect() != null) {
            upgradeEffect(sp.getTargetEffect());
        }
        if (sp.getTargetStatus() != null) {
            upgradeStatus(sp.getTargetStatus());
        }

        sp.setLevel(sp.getLevel() + 1);
    }

    public void upgradeInvocation(Invocation inv) {
        if (inv.getEffect() != null) {
            upgradeEffect(inv.getEffect());
        }
        if (inv.getStatus() != null) {
            upgradeStatus(inv.getStatus());
        }
        inv.setChance(inv.getChance() + Statics.addChance);

        inv.setLevel(inv.getLevel() + 1);
    }

    public void upgradeEffect(Effect eff) {
        int hpChange = (int) Math.round(eff.getHpChange() * Statics.addHpChange);
        if (hpChange < 1) {
            hpChange = 1;
        }
        if (eff.getHpChange() > 0) {
            eff.setHpChange(eff.getHpChange() + hpChange);
        } else if (eff.getHpChange() < 0) {
            eff.setHpChange(eff.getHpChange() - hpChange);
        }

        int cooldownChange = 0;
        if (Locator.rng().nextDouble() < Statics.chanceToChangeCooldown) {
            if (eff.getCooldownChange() < 0) {
                cooldownChange = -1;
            } else if (eff.getCooldownChange() > 0) {
                cooldownChange = 1;
            }
        }
        eff.setCooldownChange(eff.getCooldownChange() + cooldownChange);
    }

    public void upgradeStatus(Status s) {
        int duration = 0;
        if (Locator.rng().nextDouble() < Statics.chanceToIncreaseDuration) {
            duration = 1;
        }
        s.setDuration(s.getDuration() + duration);

        int[] stats = new int[s.getStats().length];
        for (int i = 0; i < stats.length; i++) {
            if (s.getStats()[i] > 0) {
                if (Locator.rng().nextDouble() < Statics.chanceToChangeStat) {
                    stats[i] = s.getStats()[i] + 1;
                } else {
                    stats[i] = s.getStats()[i];
                }
            } else if (s.getStats()[i] < 0) {
                if (Locator.rng().nextDouble() < Statics.chanceToChangeStat) {
                    stats[i] = s.getStats()[i] - 1;
                } else {
                    stats[i] = s.getStats()[i];
                }
            }
        }
        s.setStats(stats);

        double[] rates = new double[s.getRates().length];
        for (int i = 0; i < s.getRates().length; i++) {
            if (s.getRates()[i] > 0) {
                rates[i] = s.getRates()[i] + Statics.rateChange;
            } else if (s.getRates()[i] < 0) {
                rates[i] = s.getRates()[i] - Statics.rateChange;
            }
        }
        s.setRates(rates);

        if (s.getHpPerTurn() > 0) {
            if (Locator.rng().nextDouble() < Statics.chanceToChangeHpPerTurn) {
                s.setHpPerTurn(s.getHpPerTurn() + 1);
            }
        } else if (s.getHpPerTurn() < 0) {
            if (Locator.rng().nextDouble() < Statics.chanceToChangeHpPerTurn) {
                s.setHpPerTurn(s.getHpPerTurn() - 1);
            }
        }
    }

    public Enemy shadowHero(Player hero, int difficulty) {
        Enemy e = new Enemy(hero);
        e.removeResistance("Holy");
        if (difficulty == 1) {
            e.setInvocations(new ArrayList<>());
        } else if (difficulty == 2) {
            for (int i = 0; i < e.getInvocations().size(); i++) {
                Invocation inv = e.getInvocations().get(i);
                if (Locator.rng().nextDouble() < 0.8) {
                    e.removeInvocation(inv);
                }
            }
        } else if (difficulty == 3) {
            for (int i = 0; i < e.getInvocations().size(); i++) {
                Invocation inv = e.getInvocations().get(i);
                if (Locator.rng().nextDouble() < 0.5) {
                    e.removeInvocation(inv);
                }
            }
            // upgradeEnemy(e, 1);
        }
        e.setName("Shadow " + e.getName());
        return e;
    }

    public ArrayList<Enemy> randomEncounterEnemies(Player hero, int difficulty, int attempt) {
        attempt++;
        double heroRating = CombatCalculation.calcUnitTotalRating(hero);
        int enemyNum = Locator.rng().nextInt(3)+2;
        double multiplier = 0.2;
        double min = 0.5 + multiplier*(difficulty - 2);
        double max = 0.5 + multiplier*(difficulty + 2);
        // if (enemyNum > 3) {
        //     min += multiplier * 0.5;
        //     max += multiplier * 0.5;
        // }
        double minRating = heroRating*min;
        double maxRating = heroRating*max;
        // System.out.println("Range [" + minRating + ", " + maxRating + "]");
        
        int add = Locator.rng().nextInt(3);
        double mplier = 0.3 + (add*0.1);
        // System.out.println(mplier);
        // System.out.println((1-mplier));
        double firstMin = minRating*mplier;
        double firstMax = maxRating*mplier;
        double singleMin = (minRating*(1-mplier))/(enemyNum-1);
        double singleMax = (maxRating*(1-mplier))/(enemyNum-1);
    
        double rating = 0;
        ArrayList<Enemy> enemies = new ArrayList<>();
        int enemiesInList = 0;
        int tries = 0;
        while (enemiesInList < enemyNum) {
            if (singleMax < lowestRating) {
                break;
            }
            Enemy e;
            if (enemiesInList == 0) {
                // System.out.println("FIRST! Searching range [" + firstMin + ", " + firstMax + "]");
                e = enemyByRating(firstMin, firstMax);
            } else {
                // System.out.println("Searching range [" + singleMin + ", " + singleMax + "]");
                e = enemyByRating(singleMin, singleMax);
            }
            if (e != null) {
                enemies.add(e);
                double added = enemyRatings.get(e.getName());
                rating += added;
                enemiesInList++;
                minRating -= added;
                maxRating -= added;
                if (minRating < 0) {
                    minRating = 0;
                }
                if (maxRating < 0) {
                    maxRating = 0;
                }
            }
            tries++;
            if (tries > 50) {
                break;
            }
        }
        if (enemiesInList < 2) {
            if (attempt < 50) {
                return randomEncounterEnemies(hero, difficulty, attempt);
            } else {
                System.out.println("Random encounter generation failed.");
                enemies.add(shadowHero(hero, difficulty));
            }
        }
        // System.out.println(enemyNum);
        // System.out.println("DIFFICULTY: " + difficulty);
        // System.out.println("HERO RATING: " + heroRating);
        // System.out.println("COMBAT RATING: " + rating);
        return enemies;
    }

    // FINDING AN ELEMENT

    public Enemy villain(String name) {
        if (villains.containsKey(name)) {
            return new Enemy(villains.get(name));
        } else {
            return null;
        }
    }

    public Invocation invocation(String name) {
        if (invocations.containsKey(name)) {
            return new Invocation(invocations.get(name));
        } else {
            return null;
        }
    }

    public Attack attack(String name) {
        if (attacks.containsKey(name)) {
            return new Attack(attacks.get(name));
        } else {
            return null;
        }
    }

    public Special special(String name) {
        if (specials.containsKey(name)) {
            return new Special(specials.get(name));
        } else {
            return null;
        }
    }

    public Status status(String name) {
        if (stati.containsKey(name)) {
            return new Status(stati.get(name));
        } else {
            return null;
        }
    }

    public Resistance resistance(String name) {
        if (resistances.containsKey(name)) {
            return new Resistance(resistances.get(name));
        } else {
            return null;
        }
    }

    public Effect effect(String name) {
        if (effects.containsKey(name)) {
            return new Effect(effects.get(name));
        } else {
            return null;
        }
    }

    public HashMap<String, Effect> getEffects() {
        return this.effects;
    }

    public ArrayList<Enemy> getVillainList() {
        return villainList;
    }

    public void setVillainList(ArrayList<Enemy> villainList) {
        this.villainList = villainList;
    }

    public ArrayList<Attack> getAttackList() {
        return attackList;
    }

    public void setAttackList(ArrayList<Attack> attackList) {
        this.attackList = attackList;
    }

    public ArrayList<Special> getSpecialList() {
        return specialList;
    }

    public void setSpecialList(ArrayList<Special> specialList) {
        this.specialList = specialList;
    }

    public ArrayList<Status> getStatusList() {
        return statusList;
    }

    public ArrayList<Resistance> getResistanceList() {
        return resistanceList;
    }

    public void setStatusList(ArrayList<Status> statusList) {
        this.statusList = statusList;
    }

    public HashMap<String, Enemy> getVillains() {
        return villains;
    }

    public void setVillains(HashMap<String, Enemy> villains) {
        this.villains = villains;
    }

    public HashMap<String, Attack> getAttacks() {
        return attacks;
    }

    public void setAttacks(HashMap<String, Attack> attacks) {
        this.attacks = attacks;
    }

    public HashMap<String, Special> getSpecials() {
        return specials;
    }

    public void setSpecials(HashMap<String, Special> specials) {
        this.specials = specials;
    }

    public HashMap<String, Status> getStati() {
        return stati;
    }

    public HashMap<String, Resistance> getResistances() {
        return resistances;
    }

    public void setStati(HashMap<String, Status> stati) {
        this.stati = stati;
    }

    public ArrayList<Invocation> getInvocationList() {
        return this.invocationList;
    }

    public void setInvocationList(ArrayList<Invocation> list) {
        this.invocationList = list;
    }

    public HashMap<String, Invocation> getInvocations() {
        return this.invocations;
    }

    public void setInvocations(HashMap<String, Invocation> map) {
        this.invocations = map;
    }

    public HashMap<String, Double> getEnemyRatings() {
        return this.enemyRatings;
    }

}
