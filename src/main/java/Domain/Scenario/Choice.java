package Domain.Scenario;

public class Choice {
    private String descrip;
    private String result;

    public Choice(String descrip, String result) {
        this.descrip = descrip;
        this.result = result;
    }

    @Override
    public String toString() {
        return descrip + " -> " + result;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
