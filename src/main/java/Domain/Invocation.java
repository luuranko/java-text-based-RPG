package Domain;

public class Invocation {
    // Voidaan käyttää kerran per tappelu, EI käytä actionia!
    private int id;
    private String name;
    private Effect effect;
    private Status status; // Status inflicted by the Invocation
    private boolean targetsSelf; // The Invocation either targets the user or all enemies!
    private double chance;
    private int level;
    private boolean used; // The Invocation can only be used once per battle.

    public Invocation(int id, String name, Effect effect, Status status, boolean targetsSelf, double chance) {
        this.id = id;
        this.name = name;
        this.effect = effect;
        this.status = status;
	    this.targetsSelf = targetsSelf;
        this.chance = chance;
        this.used = false;
        this.level = 0;
    }

    // COPY CONSTRUCTOR
    public Invocation(Invocation inv) {
        this(inv.getId(), inv.getName(), inv.getEffect(), inv.getStatus(), inv.isTargetsSelf(), inv.getChance());
        this.setLevel(inv.getLevel());
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int lvl) {
        this.level = lvl;
    }

    public boolean isTargetsSelf() {
	return targetsSelf;
    }

    public void setTargetsSelf(boolean targetsSelf) {
	   this.targetsSelf = targetsSelf;
    }

    public Effect getEffect() {
        return this.effect;
    }

    public void setEffect(Effect e) {
        this.effect = e;
    }

    public Status getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getChance() {
        return this.chance;
    }

    public void setChance(double c) {
        this.chance = c;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

}
