package Domain;

public class Effect {
	private int id;
	private String name;
	// Heals or deals damage.
	private int hpChange;
	// If negative: shortens all current cooldowns.
	// If positive: puts all Specials on cooldown equal to the amount,
	// current cooldowns are lengthened as well
	private int cooldownChange;
	// Applying the effect clears all buffs on the target.
	private boolean clearBuffs;
	// Applying the effect clears all debuffs on the target.
	private boolean clearDebuffs;
	private boolean buff;

	public Effect(int id, String name, int hpChange, int cooldownChange,
			boolean clearBuffs, boolean clearDebuffs, boolean buff) {
		this.id = id;
		this.name = name;
		this.hpChange = hpChange;
		this.cooldownChange = cooldownChange;
		this.clearBuffs = clearBuffs;
		this.clearDebuffs = clearDebuffs;
		this.buff = buff;
	}

	// COPY CONSTRUCTOR

	public Effect(Effect e) {
		this(e.getId(), e.getName(), e.getHpChange(), e.getCooldownChange(),
			e.isClearBuffs(), e.isClearDebuffs(), e.isBuff());
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

	public int getHpChange() {
		return this.hpChange;
	}

	public void setHpChange(int hpChange) {
		this.hpChange = hpChange;
	}

	public int getCooldownChange() {
		return this.cooldownChange;
	}

	public void setCooldownChange(int cooldownChange) {
		this.cooldownChange = cooldownChange;
	}

	public boolean isClearBuffs() {
		return this.clearBuffs;
	}

	public boolean isClearDebuffs() {
		return this.clearDebuffs;
	}

	public boolean isBuff() {
		return this.buff;
	}
}