package Domain.Scenario;

import Domain.Attack;
import Domain.Special;
import Domain.Invocation;

public class Reward {
    private Attack atk;
    private Special sp;
    private Invocation inv;
    private int[] stats;

    public Reward(Attack atk, Special sp, Invocation inv, int[] stats) {
        this.atk = atk;
        this.sp = sp;
        this.inv = inv;
        this.stats = stats;
    }

    // COPY CONSTRUCTOR

    public Reward(Reward base) {
        if (base.getAtk() != null) {
            this.atk = new Attack(base.getAtk());
        }
        if (base.getSp() != null) {
            this.sp = new Special(base.getSp());
        }
        if (base.getInv() != null) {
            this.inv = new Invocation(base.getInv());
        }
        if (base.getStats() != null) {
            this.stats = base.getStats();
        }
    }

    public Reward(int[] stats) {
        this.stats = stats;
    }

    public Reward(Attack atk) {
        this.atk = atk;
    }

    public Reward(Invocation inv) {
        this.inv = inv;
    }

    public Reward(Special sp) {
        this.sp = sp;
    }

    public Attack getAtk() {
        return atk;
    }

    public void setAtk(Attack atk) {
        this.atk = atk;
    }

    public Special getSp() {
        return sp;
    }

    public void setSp(Special sp) {
        this.sp = sp;
    }

    public Invocation getInv() {
        return this.inv;
    }

    public void setInv(Invocation inv) {
        this.inv = inv;
    }

    public int[] getStats() {
        return stats;
    }

    public void setStats(int[] stats) {
        this.stats = stats;
    }
}
