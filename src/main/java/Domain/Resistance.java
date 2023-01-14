package Domain;

public class Resistance {
	private int id;
	private String name;
	private Effect eff;
	private Status status;
	private double chance;
	private boolean buff;

	public Resistance(int id, String name, Effect eff, Status status, double chance, boolean buff) {
		this.id = id;
		this.name = name;
		this.eff = eff;
		this.status = status;
		this.chance = chance;
		this.buff = buff;
	}

	// COPY CONSTRUCTOR
	public Resistance(Resistance base) {
		this(base.getId(), base.getName(), base.getEffect(), base.getStatus(), base.getChance(), base.isBuff());
	}

	@Override
	public String toString() {
		return this.name;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Effect getEffect() {
		return this.eff;
	}

	public void setEffect(Effect eff) {
		this.eff = eff;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status s) {
		this.status = s;
	}

	public double getChance() {
		return this.chance;
	}

	public void setChance(double c) {
		this.chance = c;
	}

	public boolean isBuff() {
		return this.buff;
	}

	public void setBuff(boolean buff) {
		this.buff = buff;
	}
}