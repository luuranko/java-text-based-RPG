package Logic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Random;

import Services.Locator;
import Domain.*;
import Domain.Scenario.Combat;
import IO.*;
import Logic.*;

public class CombatLogicTest {
	CombatLogic logic;
	Player hero;
	Combat combat;

	@Before
	public void setUp() {
		Locator.provideRng(new Random());
		Locator.provideComIO(new CombatIO());
		hero = new Player(0, "Hero", new int[]{3,1,1,1}, new Attack());
		ArrayList<Enemy> villainList = new ArrayList<>();
		Enemy a = new Enemy(1, "EnemyA", new int[]{2,1,1,1}, new Attack());
		villainList.add(a);
		combat = new Combat(villainList);
		logic = new CombatLogic();
		logic.init(hero, combat);
	}

	@Test
	public void applyEffect1() throws InterruptedException {
		Effect eff = new Effect(0, "RemovesBuffs", 0, 0, true, false, false);
		Unit e = new Enemy(1, "EnemyA", new int[]{2,1,1,1}, new Attack());
		Status s = new Status(0, "Buff", 1, new int[]{0,0,0,0}, new double[]{0,0,0,0,0}, 0, true);
		e.addStatus(s);
		logic.applyEffect(e, eff, 1.0);
		assertTrue(!e.hasStatus(0));
	}

	@Test
	public void calculatesEffectChanceRight1() throws InterruptedException {
		Effect eff = new Effect(0, "RemovesBuffs", 0, 0, true, false, false);
		Status s = new Status(0, "Buff", 1, new int[]{0,0,0,0}, new double[]{0,0,0,0,0}, 0, true);
		Resistance res = new Resistance(0, "Res", eff, null, -0.5, false);
		Unit e = new Enemy(1, "EnemyA", new int[]{2,1,1,1}, new Attack());
		e.addResistance(res);
		double chance = CombatCalculation.calcEffectChance(e, eff, 0.5);
		assertTrue(chance == 0.98);
	}
}