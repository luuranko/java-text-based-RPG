package Domain;

import IO.Statics;

public class Attack {
    private int id;
    private String name;
    private int pwr;
    private double hr;
    private double cr;

    public Attack(int id, String name, int pwr, double hr, double cr) {
        this.id = id;
        this.name = name;
        this.pwr = pwr;
        this.hr = hr;
        this.cr = cr;
    }

    public Attack() {
        this(0, "Attack", 0, Statics.hr, Statics.cr);
    }

    // COPY CONSTRUCTOR
    public Attack(Attack a) {
        this(a.getId(), a.getName(), a.getPwr(), a.getHr(), a.getCr());
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPwr() {
        return pwr;
    }

    public void setPwr(int pwr) {
        this.pwr = pwr;
    }

    public double getHr() {
        return hr;
    }

    public void setHr(double hr) {
        this.hr = hr;
    }

    public double getCr() {
        return cr;
    }

    public void setCr(double cr) {
        this.cr = cr;
    }
}
