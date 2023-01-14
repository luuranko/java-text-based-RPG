package IO;

import Domain.*;
import Domain.Scenario.*;
import Logic.ActionAI;
import Logic.ActionAIPlayer;
import Services.Locator;
import IO.Statics;

import java.util.ArrayList;

public class AutoInput implements PlayerInput {

	private ActionAIPlayer ai;
	private Special upgradableSpecial;
	private Invocation upgradableInvocation;

	public AutoInput() {
		this.ai = new ActionAIPlayer();
	}

	public boolean inputEnter() {
		return true;
	}

	public int inputInteger(int max) {
		return Locator.rng().nextInt(max);
	}

	public int selectedInspection(int max) {
		return 0;
	}

	public int outOfCombatChoice(int max, Player hero) {
		upgradableInvocation = null;
		upgradableSpecial = null;
		Invocation inv;
		Special sp;
		int i = 0;
		while(true) {
			if (i == hero.getInvocations().size()) {
				break;
			}
			inv = hero.getInvocations().get(i);
			if (Statics.invocationUpgradeCost(inv.getLevel()) <= hero.getSkillPoints()) {
				upgradableInvocation = inv;
				break;
			}
			i++;
		}
		if (upgradableInvocation != null) {
			return 2;
		}
		i = 0;
		while (true) {
			if (i == hero.getSpecials().size()) {
				break;
			}
			sp = hero.getSpecials().get(i);
			if (Statics.specialUpgradeCost(sp.getLevel()) <= hero.getSkillPoints()) {
				upgradableSpecial = sp;
				break;
			}
			i++;
		}
		if (upgradableSpecial != null) {
			return 2;
		}
		return 0;
		
	}

	public int chooseToUpgrade(int max) {
		if (upgradableInvocation != null) {
			return 2;
		} else if (upgradableSpecial != null) {
			return 1;
		}
		return 0;
	}

	public Special upgradableSpecial(Player hero) {
		if (upgradableSpecial != null) {
			Special sp = upgradableSpecial;
			upgradableSpecial = null;
			return sp;
		} else {
			return null;
		}
		
	}

	public Invocation upgradableInvocation(Player hero) {
		if (upgradableInvocation != null) {
			Invocation inv = upgradableInvocation;
			upgradableInvocation = null;
			return inv;
		} else {
			return null;
		}
	}


	public int selectedAction( Unit hero, ArrayList<Enemy> enemies, String... actions) {
		return ai.selectAction(hero, enemies);
	}

	public Special selectedSpecial(Unit hero, ArrayList<Special> specials) {
		Attack current = ai.currentAction();
		if (current instanceof Special) {
			return ((Special) current);
		} else {
			return null;
		}
	}

	public Invocation selectedInvocation(Unit hero, ArrayList<Invocation> invocations) {
		return ai.currentInvocation();
	}

	public Unit selectedUnit(ArrayList<Unit> units) {
		return units.get(Locator.rng().nextInt(units.size()));
	}

	public Enemy selectedEnemy(Unit hero, Attack atk, ArrayList<Enemy> enemies) {
		return ai.currentTarget();
	}
	
	public boolean confirm() {
		return true;
	}

	public Choice selectedChoice(Player hero, ArrayList<Choice> choices) {
		int choice = choices.size()-1;
		if (choices.get(0).getDescrip().equals("Rest")) {
			if (hero.getHp() < hero.getMaxHp()) {
				choice--;
				if (hero.getHp() <= hero.getMaxHp()*0.85) {
					choice--;
					if (hero.getHp() <= hero.getMaxHp()*0.65) {
						choice--;
					}
				}
			}
			if (hero.readyInvocations().size() < hero.getInvocations().size()) {
				choice--;
				if (hero.readyInvocations().size() <= hero.getInvocations().size()*0.75) {
					choice--;
					// System.out.println("Charged invocations: " + hero.readyInvocations().size() + "choiceNum: " + choice);
					if (hero.readyInvocations().size() <= hero.getInvocations().size()*0.5) {
						choice--;
						// System.out.println("Charged invocations: " + hero.readyInvocations().size() + "choiceNum: " + choice);
					}
				}
			}
			// LAITA TÄMÄ TESTAUKSEN VUOKSI KOHTA
			if (Locator.rng().nextDouble() < 0.33) {
				choice -= 3;
			}
			if (choice < 0) {
				choice = 0;
			}
		// } else if (choices.get(0).getDescrip().equals("Shula")) {
		// 	choice = 2;
		} else {
			choice = Locator.rng().nextInt(choices.size());
		}
		// System.out.println("Choice: " + (choice+1));
		return choices.get(choice);
	}
}