package IO;

import Domain.*;
import Domain.Scenario.*;

import java.util.Scanner;
import java.util.ArrayList;

public class CommandLineInput implements PlayerInput {

	private Scanner sc;

	public CommandLineInput() {
		this.sc = new Scanner(System.in);
	}

	public boolean inputEnter() {
		String line = sc.nextLine();
		if (line.isEmpty()) {
			return true;
		} else {
			return inputEnter();
		}
	}

	public int inputInteger(int max) {
		System.out.print(Statics.STYLE_BOLD + "    > " + Statics.STYLE_RESET);
        String s = sc.nextLine();
        int input;
        try {
            input = Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return inputInteger(max);
        }
        if (input < 0 || input > max) {
            System.out.println(Statics.RED + "    False command!" + Statics.RESET);
            return inputInteger(max);
        } else if (input == 0) {
            return 0;
        } else {
            return input;
        }
	}

	public int outOfCombatChoice(int max, Player hero) {
		return inputInteger(max);
	}

	public int selectedInspection(int max) {
		return inputInteger(max);
	}

	public int chooseToUpgrade(int max) {
		return inputInteger(max);
	}

	public Special upgradableSpecial(Player hero) {
		int choice = inputInteger(hero.getSpecials().size());
		if (choice == 0) {
			return null;
		} else {
			return hero.getSpecials().get(choice-1);
		}
	}

	public Invocation upgradableInvocation(Player hero) {
		int choice = inputInteger(hero.getInvocations().size());
		if (choice == 0) {
			return null;
		} else {
			return hero.getInvocations().get(choice-1);
		}
	}

	public Choice selectedChoice(Player hero, ArrayList<Choice> choices) {
		int choice = inputInteger(choices.size());
		if (choice == 0) {
			return selectedChoice(hero, choices);
		}
		return choices.get(choice-1);
	}

	public int selectedAction(Unit hero, ArrayList<Enemy> enemies, String... actions) {
		return inputInteger(actions.length);
	}

	public Special selectedSpecial(Unit hero, ArrayList<Special> specials) {
		int choice = inputInteger(specials.size());
		if (choice == 0) {
			return null;
		}
		return specials.get(choice-1);
	}

	public Invocation selectedInvocation(Unit hero, ArrayList<Invocation> invocations) {
		int choice = inputInteger(invocations.size());
        if (choice == 0) {
            return null;
        }
        return invocations.get(choice-1);
	}

	public Unit selectedUnit(ArrayList<Unit> units) {
		int choice = inputInteger(units.size());
        if (choice == 0) {
            return null;
        }
        return units.get(choice-1);
	}

	public Enemy selectedEnemy(Unit hero, Attack atk, ArrayList<Enemy> enemies) {
		int choice = inputInteger(enemies.size());
        if (choice == 0) {
            return null;
        }
        return enemies.get(choice-1);
	}

	public boolean confirm() {
		int choice = inputInteger(1);
        if (choice == 0) {
            return false;
        } else {
            return true;
        }
	}
}