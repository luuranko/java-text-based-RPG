package Domain;

public class Status {
    private int id;
    private String name;
    private int duration; // How many turns left for the status
    private int[] stats; // str, def, spd. 0 if no effect
    private double[] rates; // hrMod, evasion, crMod, crResist, dbResist. 0 if no effect

    // Either damage or healing
    // that the afflicted receives at the start of its turn
    private int hpPerTurn;
    private boolean buff;

    public Status(int id, String name, int duration, int[] stats, double[] rates, int hpPerTurn, boolean buff) {
        this.setId(id);
        this.name = name;
        this.duration = duration;
        this.stats = stats;
        this.rates = rates;
        this.setHpPerTurn(hpPerTurn);
        this.buff = buff;
    }

    // COPY CONSTRUCTOR
    public Status(Status base) {
        this(base.getId(), base.getName(), base.getDuration(),
            base.getStats(), base.getRates(), base.getHpPerTurn(), base.isBuff());
    }

    // COPY
    public void copy(Status status) {
        this.setId(status.getId());
        this.name = status.getName();
        this.duration = status.getDuration();
        this.stats = status.getStats();
        this.rates = status.getRates();
        this.hpPerTurn = status.getHpPerTurn();
        this.buff = status.isBuff();
    }

    // "Destroying" the status by turning it into a null status
    public void nullify() {
        this.id = 0;
        this.name = "None";
        this.duration = 0;
        this.stats = new int[]{0,0,0};
        this.rates = new double[]{0,0,0,0,0};
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Reduces the duration by one (at the start of a turn)
    // and returns true if the status is nullified
    public boolean countdown() {
        setDuration(duration-1);
        if (duration == 0) {
            nullify();
            return true;
        }
        return false;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
        if (duration < 0) {
            duration = 0;
        }
    }

    public int[] getStats() {
        return stats;
    }

    public void setStats(int[] stats) {
        this.stats = stats;
    }

    public double[] getRates() {
        return rates;
    }

    public void setRates(double[] rates) {
        this.rates = rates;
    }

    public int getStr() {
        return stats[0];
    }

    public int getDef() {
        return stats[1];
    }

    public int getSpd() {
        return stats[2];
    }

    public double hrMod() {
        return rates[0];
    }

    public double evasion() {
        return rates[1];
    }

    public double crMod() {
        return rates[2];
    }

    public double crResist() {
        return rates[3];
    }

    public double dbResist() {
        return rates[4];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHpPerTurn() {
        return hpPerTurn;
    }

    public void setHpPerTurn(int hpPerTurn) {
        this.hpPerTurn = hpPerTurn;
    }

    public boolean isBuff() {
        return this.buff;
    }

    public void setBuff(boolean buff) {
        this.buff = buff;
    }
}
