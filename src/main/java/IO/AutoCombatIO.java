package IO;

import java.util.ArrayList;

import Domain.*;
import Services.Locator;

public class AutoCombatIO implements AnyCombatIO {

	private PlayerInput input;

	public AutoCombatIO() {
	}

    public void init() {
        this.input = Locator.input();
    }

	public void turnStart(int turn) throws InterruptedException {

	}

    public void turnDeclaration(Unit unit) throws InterruptedException {

    }

    public void printUnitState(Unit unit) throws InterruptedException {
        
    }

    public String unitState(Unit unit) throws InterruptedException {
        return "";   
    }
    
    public void heroTurnStart(ArrayList<Enemy> enemies) throws InterruptedException {

    }
    
    public void healing(Unit u, int hp) throws InterruptedException {
    	
    }

    public void healingFromStatus(Unit u, int hp, Status s) throws InterruptedException {

    }
    
    public void damage(Unit u, int hp) throws InterruptedException {
    	
    }

    
    public void damageFromStatus(Unit u, int hp, Status s) throws InterruptedException {

    }

    public void savedFromDeath(Unit u, Status s) throws InterruptedException {
        
    }
	
	public void deathByStatus(Unit u, Status s) throws InterruptedException {
    	
    }
    
    public void announceStatusWoreOut(Unit u, Status status) throws InterruptedException {
    	
    }
    
    public void announceSpecialReady(Special sp) throws InterruptedException {
    	
    }

    
    public void noAvailableSpecials() throws InterruptedException {
    	
    }
    
    public void noAvailableInvocations() throws InterruptedException {
    	
    }
    
    public void cannotInvokeAgain() throws InterruptedException {
    	
    }
    
    public int actionChoice(Unit hero, ArrayList<Enemy> enemies, String... actions) throws InterruptedException {
    	return input.selectedAction(hero, enemies, actions);
    }
    
    public Special specialChoice(Unit unit, ArrayList<Special> specials) throws InterruptedException {
    	return input.selectedSpecial(unit, specials);
    }
    
    public Invocation invocationChoice(Unit unit, ArrayList<Invocation> invocations) throws InterruptedException {
    	return input.selectedInvocation(unit, invocations);
    }
    
    public Unit selectUnit(ArrayList<Unit> units) throws InterruptedException {
    	return input.selectedUnit(units);
    }
    
    public Enemy selectEnemy(Unit hero, Attack atk, ArrayList<Enemy> enemies) throws InterruptedException {
    	return input.selectedEnemy(hero, atk, enemies);
    }
    
    public boolean confirmTargetAll(Unit hero, Special sp, ArrayList<Enemy> enemies) throws InterruptedException {
    	return input.confirm();
    }
    
    public boolean confirmInvocationUse(Unit hero, Invocation inv, ArrayList<Enemy> enemies) throws InterruptedException {
    	return input.confirm();
    }
    
    public void alreadyHaveInvokeStatus() throws InterruptedException {
    	
    }


    public void printCommandsUnits(ArrayList<Unit> units) throws InterruptedException {
    	
    }
    
    public void printCommandsEnemies(Unit hero, Attack atk, ArrayList<Enemy> units) throws InterruptedException {
    	
    }
    
    public void printEnemyTargetWInvocation(Invocation inv, ArrayList<Enemy> units) throws InterruptedException {
    	
    }
    
	public void printCommandsEnemiesTargetAll(Unit hero, Attack atk, ArrayList<Enemy> units) throws InterruptedException {
    	
    }
    
    public void printCommandsSpecials(ArrayList<Special> specials) throws InterruptedException {
    	
    }
    
    public void printCommandsInvocations(ArrayList<Invocation> invocations) throws InterruptedException {
    	
    }
    
    public void printCommands(String... commands) throws InterruptedException {
    	
    }

    public void usedDeathDefiance(Player hero) throws InterruptedException {
        
    }
    

    public void invoke(Unit unit, Invocation inv) throws InterruptedException {
    	
    }
    
    public void allTarget(Unit unit, Attack atk) throws InterruptedException {
    	
    }
    
    public void singleTarget(Unit unit, Unit target, Attack atk) throws InterruptedException {
    	
    }
    
    public void announceChances(double hit, double crit) throws InterruptedException {
    	
    }
    
    public void announceDamage(Unit unit, Unit target, Attack atk, int damage, boolean crit) throws InterruptedException {
    	
    }
    
    public void announceDeath(Unit target) throws InterruptedException {
    	
    }

    public void announceEffectStatusChances(Effect e, double effChance, Status s, double statChance) throws InterruptedException {

    }
    
    public void resistsEffect(Unit unit, Effect e) throws InterruptedException {
    	
    }
    
    public void resistsStatus(Unit unit, Status s) throws InterruptedException {
    	
    }
    
    public void announceEffect(Unit unit, Effect e) throws InterruptedException {
    	
    }
    
    public void announceCooldownChange(Unit unit, Effect e) throws InterruptedException {
    	
    }
    
    public void announceStatus(boolean addedStatus, Unit unit, Status status) throws InterruptedException {
    	
    }
    
    public void missedAttack(Unit unit, Unit target) throws InterruptedException {
    	
    }


    public void victory() throws InterruptedException {
    	
    }
    
    public void gameOver() throws InterruptedException {
    	
    }
}