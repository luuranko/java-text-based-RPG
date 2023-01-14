package Domain.Scenario;

public class Scenario {
    private int id;
    private String name;
    private Description descrip;
    private Scenario nextScene;

    public Scenario(int id, String name, Description descrip) {
        this.setId(id);
        this.name = name;
        this.descrip = descrip;
    }

    // COPY CONSTRUCTOR
    public Scenario(Scenario base) {
        this(base.getId(), base.getName(), base.getDescrip());
        this.setNextScene(base.getNextScene());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Description getDescrip() {
        return descrip;
    }

    public void setDescrip(Description descrip) {
        this.descrip = descrip;
    }

    public Scenario getNextScene() {
        return nextScene;
    }

    public void setNextScene(Scenario nextScene) {
        this.nextScene = nextScene;
    }
}
