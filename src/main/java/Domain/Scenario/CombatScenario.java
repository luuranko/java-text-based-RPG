package Domain.Scenario;

public class CombatScenario extends Scenario {
    private Combat combat;

    public CombatScenario(int id, String name, Description descrip, Combat combat) {
        super(id, name, descrip);
        this.setCombat(combat);
    }

    // COPY CONSTRUCTOR
    public CombatScenario(CombatScenario base) {
        this(base.getId(), base.getName(), base.getDescrip(), new Combat(base.getCombat()));
        this.setNextScene(base.getNextScene());
    }

    public Combat getCombat() {
        return combat;
    }

    public void setCombat(Combat combat) {
        this.combat = combat;
    }
    
}
