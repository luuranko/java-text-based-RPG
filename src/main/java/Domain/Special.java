package Domain;

public class Special extends Attack {
    private int cooldown;
    private int turnsLeft;
    private Effect targetEffect;
    private Effect selfEffect;
    private Status targetStatus;
    private Status selfStatus;
    private double chance;
    private boolean targetAll;
    private int level;

    public Special(int id, String name, int pwr, double hr, double cr,
        boolean targetAll, int cooldown, Effect selfEffect, Effect targetEffect,
        Status selfStatus, Status targetStatus, double chance) {
        super(id, name, pwr, hr, cr);
        this.cooldown = cooldown;
        this.turnsLeft = 0;
        this.targetAll = targetAll;
        this.selfEffect = selfEffect;
        this.targetEffect = targetEffect;
        this.selfStatus = selfStatus;
        this.targetStatus = targetStatus;
        this.chance = chance;
        this.level = 0;
    }

    public Special(int id, String name, int pwr, double hr, double cr, boolean targetAll, int cooldown) {
        this(id, name, pwr, hr, cr, targetAll, cooldown, null, null, null, null, 0);
    }

    // COPY CONSTRUCTOR
    public Special(Special sp) {
        this(sp.getId(), sp.getName(), sp.getPwr(), sp.getHr(), sp.getCr(), sp.targetsAll(), sp.getCooldown());
        if (sp.getSelfEffect() != null) {
            setSelfEffect(new Effect(sp.getSelfEffect()));
        }
        if (sp.getTargetEffect() != null) {
            setTargetEffect(new Effect(sp.getTargetEffect()));
        }
        if (sp.getSelfStatus() != null) {
            setSelfStatus(new Status(sp.getSelfStatus()));
        }
        if (sp.getTargetStatus() != null) {
            setTargetStatus(new Status(sp.getTargetStatus()));
        }
        this.chance = sp.getChance();
        this.level = sp.getLevel();
    }

    @Override
    public String toString() {
        return getName();
    }

    public void startCooldown() {
        turnsLeft = cooldown;
    }

    public void advanceCooldown() {
        turnsLeft--;
        if (turnsLeft < 0) {
            turnsLeft = 0;
        }
    }

    public boolean isReady() {
        return getTurnsLeft() == 0;
    }

    public int getTurnsLeft() {
        return turnsLeft;
    }

    public void setTurnsLeft(int turnsLeft) {
        this.turnsLeft = turnsLeft;
        if (this.turnsLeft < 0) {
            this.turnsLeft = 0;
        }
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
        if (this.cooldown < 2) {
            this.cooldown = 2;
        }
    }

    public Effect getSelfEffect() {
        return this.selfEffect;
    }

    public void setSelfEffect(Effect selfEffect) {
        this.selfEffect = selfEffect;
    }

    public Effect getTargetEffect() {
        return this.targetEffect;
    }

    public void setTargetEffect(Effect targetEffect) {
        this.targetEffect = targetEffect;
    }

    public Status getSelfStatus() {
        return selfStatus;
    }

    public void setSelfStatus(Status selfStatus) {
        this.selfStatus = selfStatus;
    }

    public Status getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(Status targetStatus) {
        this.targetStatus = targetStatus;
    }

    public double getChance() {
        return this.chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public boolean targetsAll() {
        return targetAll;
    }

    public void setTargetAll(boolean targetAll) {
        this.targetAll = targetAll;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int lvl) {
        this.level = lvl;
    }
}
