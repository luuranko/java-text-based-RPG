package Domain.Scenario;

public class ChoiceScenario extends Scenario {
    private ChoiceSet choices;

    public ChoiceScenario(int id, String name, Description descrip, ChoiceSet choices) {
        super(id, name, descrip);
        this.setChoices(choices);
    }

    // COPY CONSTRUCTOR
    public ChoiceScenario(ChoiceScenario base) {
        this(base.getId(), base.getName(), base.getDescrip(), base.getChoices());
        this.setNextScene(base.getNextScene());
    }

    public ChoiceSet getChoices() {
        return choices;
    }

    public void setChoices(ChoiceSet choices) {
        this.choices = choices;
    }
}
