package Domain.Scenario;

import java.util.ArrayList;

public class Description {
    private int id;
    private ArrayList<String> lines;

    public Description(int id, ArrayList<String> lines) {
        this.id = id;
        this.lines = lines;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
    } 
}
