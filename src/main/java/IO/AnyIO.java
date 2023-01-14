package IO;

import java.util.ArrayList;

import Domain.*;
import Domain.Scenario.*;

public interface AnyIO {
    void init();

	void secWait() throws InterruptedException;
	void halfWait() throws InterruptedException;
	void smallWait() throws InterruptedException;

	void print(String s) throws InterruptedException;
	void println(String s) throws InterruptedException;
    void printFast(String s) throws InterruptedException;
	void printArrow(String s) throws InterruptedException;
	void printInspect(String s) throws InterruptedException;
	boolean printAndWaitInput(String s) throws InterruptedException;

	String formatDouble(double d) throws InterruptedException;

	String bold(String s) throws InterruptedException;
    String italic(String s) throws InterruptedException;
    String underline(String s) throws InterruptedException;
    String faint(String s) throws InterruptedException;

    String red(String s) throws InterruptedException;
    String green(String s) throws InterruptedException;
    String yellow(String s) throws InterruptedException;
    String blue(String s) throws InterruptedException;
    String purple(String s) throws InterruptedException;
    String cyan(String s) throws InterruptedException;
    String grey(String s) throws InterruptedException;

    String ifNegThenRedDouble(double num) throws InterruptedException;
    String ifNegThenRedInt(int num) throws InterruptedException;
    String unitHpChange(Unit unit) throws InterruptedException;
    String hitChance(double hit) throws InterruptedException;
    String critChance(double crit) throws InterruptedException;

    void printSceneDescrip(Scenario scene) throws InterruptedException;
    Choice makeChoice(Player hero, ArrayList<Choice> choices) throws InterruptedException;
	void announceReward(Player hero, Reward r) throws InterruptedException;
    void announceUnitState(Player hero) throws InterruptedException;
    void longRest(Player hero) throws InterruptedException;
    void shortRest(Player hero) throws InterruptedException;
    void invocationRegained(Player hero, Invocation inv) throws InterruptedException;
    int outOfCombatMenu(Player hero) throws InterruptedException;
    void notEnoughSkillPoints() throws InterruptedException;
    int upgradeMenu() throws InterruptedException;
    Invocation chooseUpgradeInvocation(Player hero) throws InterruptedException;
    Special chooseUpgradeSpecial(Player hero) throws InterruptedException;
    void upgradedInvocation(Invocation inv) throws InterruptedException;
    void upgradedSpecial(Special sp) throws InterruptedException;
    void skillPointsChange(Player hero, int amount) throws InterruptedException;
    
	String unitHp(Unit u) throws InterruptedException;

	void inspectUnit(Unit u) throws InterruptedException;
    void inspectUnitBasicAttack(Unit u) throws InterruptedException;
    void inspectUnitSpecials(Unit u) throws InterruptedException;
    void inspectUnitInvs(Unit u) throws InterruptedException;
    int printInspectOptions() throws InterruptedException;
	void inspectResistance(Resistance r) throws InterruptedException;
    void inspectAttack(Attack a) throws InterruptedException;
    void inspectSpecial(Special sp) throws InterruptedException;
    void inspectInvocation(Invocation inv) throws InterruptedException;
    void inspectEffect(Effect e) throws InterruptedException;
    void inspectStatus(Status s) throws InterruptedException;
    boolean hasStatus(Unit u) throws InterruptedException;

    void gameEnd() throws InterruptedException;
}
