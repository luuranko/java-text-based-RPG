package Domain;

import java.util.ArrayList;

public class FeatureStatus extends Status {
	// All the Resistances this Status provides
	private ArrayList<Resistance> resistances;
	// The healing that the Status receiver gains is multiplied by this amount.
	// 2.0 for double heals, 0.5 for halved heals. -1 for damage whenever heals!
	private double healMultiplier;
	// All the damage dealt BY the Statushaver is multiplied by this amount.
	// 2 for double damage, 0.5 for halved damage, 0 for no damage! Etc.
	private double dmgMultiplier;
	// boolean for if the Status receiver's resistances are out of commission
	// while under the Status
	private boolean resistanceBlock;
	// if the Status blocks using specials.
	// important: special cooldowns are still counted when block is on!
	private boolean specialBlock;
	// if the Status blocks using invocations.
	private boolean invocationBlock;

	public FeatureStatus(int id, String name, int duration, int[] stats,
				double[] rates, int hpPerTurn, boolean buff, ArrayList<Resistance> resistances,
				double healMultiplier, double dmgMultiplier, boolean resistanceBlock,
				boolean specialBlock, boolean invocationBlock) {
		super(id, name, duration, stats, rates, hpPerTurn, buff);
		this.resistances = resistances;
		this.healMultiplier = healMultiplier;
		this.dmgMultiplier = dmgMultiplier;
		this.resistanceBlock = resistanceBlock;
		this.specialBlock = specialBlock;
		this.invocationBlock = invocationBlock;
	}

	public FeatureStatus(Status s, ArrayList<Resistance> resistances,
				double healMultiplier, double dmgMultiplier, boolean resistanceBlock,
				boolean specialBlock, boolean invocationBlock) {
		super(s);
		this.resistances = resistances;
		this.healMultiplier = healMultiplier;
		this.dmgMultiplier = dmgMultiplier;
		this.resistanceBlock = resistanceBlock;
		this.specialBlock = specialBlock;
		this.invocationBlock = invocationBlock;
	}

	// COPY CONSTRUCTOR
	public FeatureStatus(FeatureStatus fs) {
		this(fs.getId(), fs.getName(), fs.getDuration(), fs.getStats(),
			fs.getRates(), fs.getHpPerTurn(), fs.isBuff(), fs.getResistances(),
			fs.getHealMultiplier(), fs.getDmgMultiplier(), fs.isResistanceBlock(),
			fs.isSpecialBlock(), fs.isInvocationBlock());
	}

	public Resistance getResistance(Resistance res) {
		for (Resistance r: resistances) {
			if (r.getId() == res.getId()) {
				return r;
			}
		}
		return null;
	}

	public ArrayList<Resistance> getResistances() {
		return this.resistances;
	}

	public void setResistances(ArrayList<Resistance> resistances) {
		this.resistances = resistances;
	}

	public double getHealMultiplier() {
		return this.healMultiplier;
	}

	public void setHealMultiplier(double multiplier) {
		this.healMultiplier = multiplier;
	}

	public double getDmgMultiplier() {
		return this.dmgMultiplier;
	}

	public void setDmgMultiplier(double multiplier) {
		this.dmgMultiplier = multiplier;
	}

	public boolean isResistanceBlock() {
		return this.resistanceBlock;
	}

	public void setResistanceBlock(boolean block) {
		this.resistanceBlock = block;
	}

	public boolean isSpecialBlock() {
		return this.specialBlock;
	}

	public void setSpecialBlock(boolean block) {
		this.specialBlock = block;
	}

	public boolean isInvocationBlock() {
		return this.invocationBlock;
	}

	public void setInvocationBlock(boolean block) {
		this.invocationBlock = block;
	}
}