package Domain.Scenario;

import java.util.ArrayList;

import Domain.Unit;
import Domain.Enemy;
import Domain.Player;

public class Combat {
    private ArrayList<Enemy> villainList;
    private boolean combatOn;
    private boolean victory;

    // CONSTRUCTORS

    public Combat(ArrayList<Enemy> villainList) {
        this.victory = false;
        this.combatOn = false;
        this.villainList = villainList;
        differentiateEnemies();
    }

    // COPY CONSTRUCTOR

    public Combat(Combat base) {
        this.victory = false;
        this.combatOn = false;
        ArrayList<Enemy> list = new ArrayList<>();
        for (Enemy e: base.villainList()) {
            list.add(new Enemy(e));
        }
        this.villainList = list;
        differentiateEnemies();
    }

    // CHECK IF SAME NAMES BTWN MONSTERS.
    // THEN CHANGE THOSE TO "Monster 1", "Monster 2"
    public void differentiateEnemies() {
    for (Enemy e: villainList) {
            int num = 1;
            String name = e.getName();
            for (Enemy en: villainList) {
                if (en.getName().equals(name) && !en.equals(e)) {
                    if (num == 1) {
                        e.setName(name + " 1");
                        num++;
                    }
                    en.setName(name + " " + num);
                    num++;
                }
            }

        }
    }

    // BASIC STUFF

    public boolean isVictory() {
        return victory;
    }

    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    public boolean isCombatOn() {
        return combatOn;
    }

    public void setCombatOn(boolean combatOn) {
        this.combatOn = combatOn;
    }

    public void setVillainList(ArrayList<Enemy> villainList) {
        this.villainList = villainList;
    }

    public ArrayList<Enemy> villainList() {
        return this.villainList;
    }

    public ArrayList<Unit> unitList(Player hero) {
        ArrayList<Unit> list = new ArrayList<>();
        list.add(hero);
        list.addAll(villainList);
        return list;
    }
}
