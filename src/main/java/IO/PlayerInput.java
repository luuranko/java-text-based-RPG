package IO;

import Domain.*;
import Domain.Scenario.*;

import java.util.ArrayList;

public interface PlayerInput {
	boolean inputEnter();
	int inputInteger(int max);
	int selectedInspection(int max);
	int outOfCombatChoice(int max, Player hero);
	int chooseToUpgrade(int max);
	Special upgradableSpecial(Player hero);
	Invocation upgradableInvocation(Player hero);

	int selectedAction(Unit hero, ArrayList<Enemy> enemies, String... actions);
	Special selectedSpecial(Unit hero, ArrayList<Special> specials);
	Invocation selectedInvocation(Unit hero, ArrayList<Invocation> invocations);
	Unit selectedUnit(ArrayList<Unit> units);
	Enemy selectedEnemy(Unit hero, Attack atk, ArrayList<Enemy> enemies);
	boolean confirm();
	Choice selectedChoice(Player hero, ArrayList<Choice> choices);
}