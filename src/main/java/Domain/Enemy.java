package Domain;

public class Enemy extends Unit {
    
    public Enemy(int id, String name, int hp, int str, int def, int spd, Attack atk) {
        super(id, name, hp, str, def, spd, atk);
    }

    public Enemy(int id, String name, int[] stats, Attack atk) {
        super(id, name, stats, atk);
    }

    // COPY CONSTRUCTOR
    public Enemy(Enemy e) {
        super(e);
    }

    public Enemy(Unit u) {
        super(u);
    }

    public Enemy(Player p) {
        super(p);
    }
}
