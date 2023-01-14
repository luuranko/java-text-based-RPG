package Domain;

import IO.Statics;

public class Player extends Unit {
    private int skillPoints;
    private int deathDefiances;
    
    public Player(int id, String name, int hp, int str, int def, int spd, Attack atk) {
        super(id, name, hp, str, def, spd, atk);
        this.skillPoints = 0;
        this.deathDefiances = Statics.defaultDeathDefiances;
    }

    public Player(int id, String name, int[] stats, Attack atk) {
        super(id, name, stats, atk);
        this.skillPoints = 0;
        this.deathDefiances = Statics.defaultDeathDefiances;
    }

    // COPY CONSTRUCTORS
    public Player(Player p) {
        super(p);
        this.skillPoints = p.getSkillPoints();
        this.deathDefiances = p.getDeathDefiances();
    }

    public Player(Enemy e) {
        super(e);
        this.deathDefiances = Statics.defaultDeathDefiances;
    }

    public Player(Unit u) {
        super(u);
        if (u instanceof Player) {
            this.deathDefiances = ((Player) u).getDeathDefiances();
        } else {
            this.deathDefiances = Statics.defaultDeathDefiances;
        }
    }

    public int getSkillPoints() {
        return this.skillPoints;
    }

    public void setSkillPoints(int points) {
        this.skillPoints = points;
        if (skillPoints < 0) {
            skillPoints = 0;
        }
    }

    public int getDeathDefiances() {
        return this.deathDefiances;
    }

    public void setDeathDefiances(int num) {
        this.deathDefiances = num;
    }
}
