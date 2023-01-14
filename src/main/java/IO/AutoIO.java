package IO;

import Domain.*;
import Domain.Scenario.*;
import java.util.ArrayList;
import Services.Locator;

public class AutoIO implements AnyIO {

	private PlayerInput input;

	public AutoIO() {
	}

	public void init() {
        this.input = Locator.input();
    }

	public void secWait() throws InterruptedException {

	}

	public void halfWait() throws InterruptedException {

	}

	public void smallWait() throws InterruptedException {

	}
	

	public void print(String s) throws InterruptedException {

	}
	
	public void println(String s) throws InterruptedException {

	}

	public void printFast(String s) throws InterruptedException {
		
	}
	
	public void printArrow(String s) throws InterruptedException {

	}
	
	public void printInspect(String s) throws InterruptedException {

	}
	
	public boolean printAndWaitInput(String s) throws InterruptedException {
		return true;
	}
	

	public String formatDouble(double d) throws InterruptedException {
		return "";
	}
	

	public String bold(String s) throws InterruptedException {
		return "";
	}
	
    public String italic(String s) throws InterruptedException {
		return "";
	}
	
    public String underline(String s) throws InterruptedException {
		return "";
	}
	
	public String faint(String s) throws InterruptedException {
        return "";
    }

    public String red(String s) throws InterruptedException {
		return "";
	}
	
    public String green(String s) throws InterruptedException {
		return "";
	}
	
    public String yellow(String s) throws InterruptedException {
		return "";
	}
	
    public String blue(String s) throws InterruptedException {
		return "";
	}
	
    public String purple(String s) throws InterruptedException {
		return "";
	}
	
    public String cyan(String s) throws InterruptedException {
		return "";
	}
	
    public String grey(String s) throws InterruptedException {
		return "";
	}
	
    public String ifNegThenRedDouble(double num) throws InterruptedException {
		return "";
	}
	
    public String ifNegThenRedInt(int num) throws InterruptedException {
		return "";
	}
	
    public String unitHpChange(Unit unit) throws InterruptedException {
		return "";
	}
	
    public String hitChance(double hit) throws InterruptedException {
		return "";
	}
	
    public String critChance(double crit) throws InterruptedException {
		return "";
	}
	

    public void printSceneDescrip(Scenario scene) throws InterruptedException {

	}
	
    public Choice makeChoice(Player hero, ArrayList<Choice> choices) throws InterruptedException {
		return input.selectedChoice(hero, choices);
	}
	
	public void announceReward(Player hero, Reward r) throws InterruptedException {

	}
	
    public void announceUnitState(Player hero) throws InterruptedException {

	}
	public void longRest(Player hero) throws InterruptedException {
    }

    public void shortRest(Player hero) throws InterruptedException {
    }

    public void invocationRegained(Player hero, Invocation inv) throws InterruptedException {
    }
	
	public int intInput(int max) throws InterruptedException {
		return input.inputInteger(max);
	}

	public int outOfCombatMenu(Player hero) throws InterruptedException {
        return input.outOfCombatChoice(2, hero);
    }

    public void notEnoughSkillPoints() throws InterruptedException {
    	
    }

    public int upgradeMenu() throws InterruptedException {
        return input.chooseToUpgrade(2);
    }

    public Invocation chooseUpgradeInvocation(Player hero) throws InterruptedException {
        return input.upgradableInvocation(hero);
    }

	public Special chooseUpgradeSpecial(Player hero) throws InterruptedException {
        return input.upgradableSpecial(hero);
    }

    public void upgradedInvocation(Invocation inv) throws InterruptedException {
    }

    public void upgradedSpecial(Special sp) throws InterruptedException {
        
    }

    public void skillPointsChange(Player hero, int amount) throws InterruptedException {
        
    }
	
	public String unitHp(Unit u) throws InterruptedException {
		return "";
	}
	

	public void inspectUnit(Unit u) throws InterruptedException {

	}

	public void inspectUnitBasicAttack(Unit u) throws InterruptedException {
        
    }

    public void inspectUnitSpecials(Unit u) throws InterruptedException {
        
    }

    public void inspectUnitInvs(Unit u) throws InterruptedException {
        
    }

    public int printInspectOptions() throws InterruptedException {
        return 0;
    }
	
	public void inspectResistance(Resistance r) throws InterruptedException {

	}
	
    public void inspectAttack(Attack a) throws InterruptedException {

	}
	
    public void inspectSpecial(Special sp) throws InterruptedException {

	}
	
    public void inspectInvocation(Invocation inv) throws InterruptedException {

	}
	
    public void inspectEffect(Effect e) throws InterruptedException {

	}
	
    public void inspectStatus(Status s) throws InterruptedException {

	}
	
    public boolean hasStatus(Unit u) throws InterruptedException {
    	return !u.getStati().isEmpty();
	}
	

    public void gameEnd() throws InterruptedException {

	}
	
}