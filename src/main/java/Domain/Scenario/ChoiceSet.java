package Domain.Scenario;

import java.util.ArrayList;

public class ChoiceSet {
    private int id;
    private ArrayList<Choice> choices;

    public ChoiceSet(int id, ArrayList<Choice> choices) {
        this.id = id;
        this.choices = choices;
    }

    public void addChoice(Choice c) {
        if (!choices.contains(c)) {
            choices.add(c);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Choice> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<Choice> choices) {
        this.choices = choices;
    }
    
}
