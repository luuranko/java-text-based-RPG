package IO;

import java.util.ArrayList;

import Domain.*;

public interface AnyCombatIO {
    void init();
	void turnStart(int turn) throws InterruptedException;
    void turnDeclaration(Unit unit) throws InterruptedException;
    void printUnitState(Unit unit) throws InterruptedException;
    String unitState(Unit unit) throws InterruptedException;
    void heroTurnStart(ArrayList<Enemy> enemies) throws InterruptedException;
    void healing(Unit u, int hp) throws InterruptedException;
    void healingFromStatus(Unit u, int hp, Status s) throws InterruptedException;
    void damage(Unit u, int hp) throws InterruptedException;
    void damageFromStatus(Unit u, int hp, Status s) throws InterruptedException;
    void savedFromDeath(Unit u, Status s) throws InterruptedException;
	void deathByStatus(Unit u, Status s) throws InterruptedException;
    void announceStatusWoreOut(Unit u, Status status) throws InterruptedException;
    void announceSpecialReady(Special sp) throws InterruptedException;

    void noAvailableSpecials() throws InterruptedException;
    void noAvailableInvocations() throws InterruptedException;
    void cannotInvokeAgain() throws InterruptedException;
    int actionChoice(Unit hero, ArrayList<Enemy> enemies, String... actions) throws InterruptedException;
    Special specialChoice(Unit unit, ArrayList<Special> specials) throws InterruptedException;
    Invocation invocationChoice(Unit unit, ArrayList<Invocation> invocations) throws InterruptedException;
    Unit selectUnit(ArrayList<Unit> units) throws InterruptedException;
    Enemy selectEnemy(Unit hero, Attack atk, ArrayList<Enemy> units) throws InterruptedException;
    boolean confirmTargetAll(Unit hero, Special sp, ArrayList<Enemy> enemies) throws InterruptedException;
    boolean confirmInvocationUse(Unit hero, Invocation inv, ArrayList<Enemy> enemies) throws InterruptedException;
    void alreadyHaveInvokeStatus() throws InterruptedException;

    void printCommandsUnits(ArrayList<Unit> units) throws InterruptedException;
    void printCommandsEnemies(Unit hero, Attack atk, ArrayList<Enemy> units) throws InterruptedException;
    void printEnemyTargetWInvocation(Invocation inv, ArrayList<Enemy> units) throws InterruptedException;
    void printCommandsEnemiesTargetAll(Unit hero, Attack atk, ArrayList<Enemy> units) throws InterruptedException;
    void printCommandsSpecials(ArrayList<Special> specials) throws InterruptedException;
    void printCommandsInvocations(ArrayList<Invocation> invocations) throws InterruptedException;
    void printCommands(String... commands) throws InterruptedException;
    
    void usedDeathDefiance(Player hero) throws InterruptedException;
    void invoke(Unit unit, Invocation inv) throws InterruptedException;
    void allTarget(Unit unit, Attack atk) throws InterruptedException;
    void singleTarget(Unit unit, Unit target, Attack atk) throws InterruptedException;
    void announceChances(double hit, double crit) throws InterruptedException;
    void announceDamage(Unit unit, Unit target, Attack atk, int damage, boolean crit) throws InterruptedException;
    void announceDeath(Unit target) throws InterruptedException;
    void announceEffectStatusChances(Effect e, double effChance, Status s, double statChance) throws InterruptedException;
    void resistsEffect(Unit unit, Effect e) throws InterruptedException;
    void resistsStatus(Unit unit, Status s) throws InterruptedException;
    void announceEffect(Unit unit, Effect e) throws InterruptedException;
    void announceCooldownChange(Unit unit, Effect e) throws InterruptedException;
    void announceStatus(boolean addedStatus, Unit unit, Status status) throws InterruptedException;
    void missedAttack(Unit unit, Unit target) throws InterruptedException;

    void victory() throws InterruptedException;
    void gameOver() throws InterruptedException;
}