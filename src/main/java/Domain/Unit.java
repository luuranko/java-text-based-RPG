package Domain;

import java.util.ArrayList;

import IO.Statics;

public class Unit implements Comparable<Unit> {
    private int id;
    private String name;
    private int baseHp;
    private int[] stats; // hp (not max), str, def, spd
    private double[] rates; // hrMod, evasion, crMod, crResist, dbResist
    protected boolean alive;
    private Attack basicAtk;
    private ArrayList<Special> specials;
    private ArrayList<Invocation> invocations;
    private ArrayList<Status> stati;
    private ArrayList<Resistance> resistances;

    public Unit(int id, String name, int hp, int str, int def, int spd, Attack atk) {
        this.id = id;
        this.stati = new ArrayList<>();
        this.specials = new ArrayList<>();
        this.invocations = new ArrayList<>();
        this.resistances = new ArrayList<>();
        this.name = name;
        this.stats = new int[4];
        setStr(str);
        setDef(def);
        setSpd(spd);
        setBaseHp(hp);
        setHp(getMaxHp());
        this.alive = true;
        this.rates = new double[5];
        basicAtk = atk;
        update();
    }

    public Unit(int id, String name, int[] stats, Attack atk) {
        this(id, name, stats[0], stats[1], stats[2], stats[3], atk);
    }

    // COPY CONSTRUCTOR
    public Unit(Unit u) {
        this(u.getId(), u.getName(), u.getStats(), u.getBasicAtk());
        this.copySpecials(u.getSpecials());
        this.copyInvocations(u.getInvocations());
        this.copyResistances(u.getResistances());
    }

    public void update() {
        calcHrMod();
        calcEvasion();
        calcCrMod();
        calcCrResist();
    }

    public String toString() {
        return name;
    }

    public int getMaxHp() {
        int max = getBaseHp();
        if (max <= 0) {
            max = 1;
        }
        return max;
    }

    public void calcHrMod() {
        double hr = (Statics.hrMod * getSpd());
        for (Status status: stati) {
            hr += status.hrMod();
        }
        rates[0] = hr;
    }

    public double hrMod() {
        calcHrMod();
        return rates[0];
    }

    public void calcEvasion() {
        double e = (Statics.evMod * getSpd());
        for (Status status: stati) {
            e += status.evasion();
        }
        rates[1] = e; 
    }

    public double evasion() {
        calcEvasion();
        return rates[1];
    }

    public void calcCrMod() {
        double cr = (Statics.crMod * getStr());
        for (Status status: stati) {
            cr += status.crMod();
        }
        rates[2] = cr;
    }

    public double crMod() {
        calcCrMod();
        return rates[2];
    }

    public void calcCrResist() {
        double crre = (Statics.crreMod * getDef());
        for (Status status: stati) {
            crre += status.crResist();
        }
        rates[3] = crre;
    }

    public double crResist() {
        calcCrResist();
        return rates[3];
    }

    public void calcDbResist() {
        double dbre = (Statics.dbreMod * getDef());
        for (Status status: stati) {
            dbre += status.dbResist();
        }
        rates[4] = dbre;
    }

    public double dbResist() {
        calcDbResist();
        return rates[4];
    }

    public void setBaseHp(int baseHp) {
        this.baseHp = baseHp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return stats[0];
    }

    public void setHp(int hp) {
        stats[0] = hp;
        if (stats[0] < 0) {
            stats[0] = 0;
        }
        int maxHp = getMaxHp();
        if (stats[0] > maxHp) {
            stats[0] = maxHp;
        }
        if (stats[0] == 0) {
            this.setAlive(false);
        }
    }

    public int getStr() {
        int str = stats[1];
        for (Status status: stati) {
            str += status.getStr();
        }
        return str;
    }

    public void setStr(int str) {
        stats[1] = str;
    }

    public int getDef() {
        int def = stats[2];
        for (Status status: stati) {
            def += status.getDef();
        }
        return def;
    }

    public void setDef(int def) {
        stats[2] = def;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getSpd() {
        int spd = stats[3];
        for (Status status: stati) {
            spd += status.getSpd();
        }
        return spd;
    }

    public void setSpd(int spd) {
        stats[3] = spd;
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

    public Attack getBasicAtk() {
        return basicAtk;
    }

    public void setBasicAtk(Attack basicAtk) {
        this.basicAtk = basicAtk;
    }

    public ArrayList<Status> getStati() {
        return stati;
    }

    public void setStati(ArrayList<Status> stati) {
        this.stati = stati;
    }

    public boolean addStatus(Status status) {
        if (status == null || hasStatus(status)) {
            return false;
        } else {
            this.stati.add(status);
            return true;
        }
    }

    public void removeStatus(Status status) {
        if (hasStatus(status)) {
            this.stati.remove(getStatus(status.getId()));
        }
    }

    public void removeStatus(int id) {
        if (hasStatus(id)) {
            this.stati.remove(getStatus(id));
        }
    }

    public boolean hasStatus(Status status) {
        if (status == null) {
            return false;
        }
        for (Status s: this.stati) {
            if (s.getId() == status.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasStatus(int id) {
        for (Status s: this.stati) {
            if (s.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public Status getStatus(int id) {
        for (Status s: this.stati) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public Status getStatus(String name) {
        for (Status s: this.stati) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    public int getBaseHp() {
        return baseHp;
    }

    public void copySpecials(ArrayList<Special> specials) {
        ArrayList<Special> newList = new ArrayList<>();
        for (Special sp: specials) {
            newList.add(new Special(sp));
        }
        setSpecials(newList);
    }

    public ArrayList<Special> getSpecials() {
        return specials;
    }

    public ArrayList<Special> readySpecials() {
        ArrayList<Special> readyList = new ArrayList<>();
        for (Special sp: getSpecials()) {
            if (sp.isReady()) {
                readyList.add(sp);
            }
        }
        return readyList;
    }

    public void setSpecials(ArrayList<Special> specials) {
        this.specials = specials;
    }

    public void addSpecial(Special sp) {
        if (!hasSpecial(sp) && sp != null) {
            this.specials.add(sp);
        }
    }

    public void removeSpecial(Special sp) {
        if (hasSpecial(sp)) {
            this.specials.remove(getSpecial(sp));
        }
    }

    public Special getSpecial(Special sp) {
        if (sp == null) {
            return null;
        }
        for (Special special: this.specials) {
            if (special.getId() == sp.getId()) {
                return special;
            }
        }
        return null;
    }

    public boolean hasSpecial(Special sp) {
        if (sp == null) {
            return false;
        }
        for (Special special: this.specials) {
            if (special.getId() == sp.getId()) {
                return true;
            }
        }
        return false;
    }

    public void copyInvocations(ArrayList<Invocation> invocations) {
        ArrayList<Invocation> list = new ArrayList<>();
        for (Invocation i: invocations) {
            list.add(new Invocation(i));
        }
        setInvocations(list);
    }

    public void addInvocation(Invocation inv) {
        if (!hasInvocation(inv) && inv != null) {
            this.invocations.add(inv);
        }
    }

    public void removeInvocation(Invocation inv) {
        if (hasInvocation(inv)) {
            this.invocations.remove(inv);
        }
    }

    public boolean hasInvocation(Invocation inv) {
        if (inv == null) {
            return false;
        }
        for (Invocation i: invocations) {
            if (i.getId() == inv.getId()) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Invocation> readyInvocations() {
        ArrayList<Invocation> readyList = new ArrayList<>();
        for (Invocation inv: getInvocations()) {
            if (!inv.isUsed()) {
                readyList.add(inv);
            }
        }
        return readyList;
    }

    public ArrayList<Invocation> getInvocations() {
        return invocations;
    }

    public void setInvocations(ArrayList<Invocation> invocations) {
        this.invocations = invocations;
    }

    public void copyResistances(ArrayList<Resistance> resistances) {
        ArrayList<Resistance> list = new ArrayList<>();
        for (Resistance r: resistances) {
            list.add(r);
        }
        this.resistances = list;
    }

    public boolean addResistance(Resistance res) {
        if (!hasResistance(res) && res != null) {
            this.resistances.add(res);
            return true;
        }
        return false;
    }

    public void removeResistance(Resistance res) {
        if (hasResistance(res)) {
            this.resistances.remove(res);
        }
    }

    public void removeResistance(String res) {
        for (int i = 0; i < resistances.size(); i++) {
            Resistance r = resistances.get(i);
            if (r.getName().equals(res)) {
                this.resistances.remove(r);
            }
        }
    }

    public boolean hasResistance(Resistance res) {
        for (Resistance r: this.resistances) {
            if (r.getId() == res.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasResistance(Status s) {
        for (Resistance r: this.resistances) {
            if (r.getStatus().getId() == s.getId()) {
                return true;
            }
        }
        return false;
    }

    public double resistanceTo(Status s) {
        double res = 0;
        for (Resistance r: this.resistances) {
            if (r.getStatus() != null) {
                if (r.getStatus().getId() == s.getId()) {
                    res += r.getChance();
                }
            }
        }
        return res;
    }

    public double resistanceTo(Effect eff) {
        double res = 0;
        for (Resistance r: this.resistances) {
            if (r.getEffect() != null) {
                if (r.getEffect().getId() == eff.getId()) {
                    res += r.getChance();
                }
            }
        }
        return res;
    }

    public ArrayList<Resistance> getResistances() {
        return this.resistances;
    }

    public void setResistances(ArrayList<Resistance> resistances) {
        this.resistances = resistances;
    }

    // This is for initiative order
    @Override
    public int compareTo(Unit other) {
        if (getSpd() > other.getSpd()) {
            return -1;
        } else if (getSpd() < other.getSpd()) {
            return 1;
        }
        return 0;
    }

    public int getId() {
        return this.id;
    }
}
