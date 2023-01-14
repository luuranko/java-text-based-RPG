package Domain.Scenario;

public class RewardScenario extends Scenario {
    private Reward reward;
    
    public RewardScenario(int id, String name, Description descrip, Reward reward) {
        super(id, name, descrip);
        this.reward = reward;
    }

    // COPY CONSTRUCTOR
    public RewardScenario(RewardScenario base) {
        this(base.getId(), base.getName(), base.getDescrip(), new Reward(base.getReward()));
        this.setNextScene(base.getNextScene());
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }


    
}
