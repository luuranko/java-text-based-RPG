package Domain.Scenario;

import Domain.Player;

public class HeroScenario extends Scenario {
	Scenario firstHeroScene;
	Scenario secondHeroScene;
	Scenario thirdHeroScene;

	public HeroScenario(int id, String name) {
		super(id, name, null);
	}

	// COPY CONSTRUCTOR
	public HeroScenario(HeroScenario scene) {
		super(scene.getId(), scene.getName(), null);
		this.setFirstHeroScene(scene.getFirstHeroScene());
		this.setSecondHeroScene(scene.getSecondHeroScene());
		this.setThirdHeroScene(scene.getThirdHeroScene());
	}

	public void setFirstHeroScene(Scenario scene) {
		this.firstHeroScene = scene;
	}

	public void setSecondHeroScene(Scenario scene) {
		this.secondHeroScene = scene;
	}

	public void setThirdHeroScene(Scenario scene) {
		this.thirdHeroScene = scene;
	}

	public Scenario getHeroScene(Player hero) {
		if (hero.getName().equals("Shula")) {
			return firstHeroScene;
		} else if (hero.getName().equals("Nuray")) {
			return secondHeroScene;
		} else if (hero.getName().equals("Esther")) {
			return thirdHeroScene;
		}
		return null;
	}

	public Scenario getFirstHeroScene() {
		return firstHeroScene;
	}

	public Scenario getSecondHeroScene() {
		return secondHeroScene;
	}

	public Scenario getThirdHeroScene() {
		return thirdHeroScene;
	}
}