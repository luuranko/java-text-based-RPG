package Domain.Scenario;

import java.util.ArrayList;
import java.util.Collections;

public class SceneSet {
	private String name;
	private ArrayList<Scenario> scenes;

	public SceneSet(String name) {
		this.name = name;
		this.scenes = new ArrayList<>();
	}

	public SceneSet(String name, ArrayList<Scenario> scenes) {
		this.name = name;
		this.scenes = scenes;
		setNextScenes();
	}

	public String getName() {
		return this.name;
	}

	public void addScene(Scenario scene) {
		scenes.add(scene);
	}

	public void finish() {
		Collections.reverse(scenes);
		setNextScenes();
	}

	public Scenario first() {
		return scenes.get(0);
	}

	public boolean containsScene(Scenario scene) {
		if (scene == null) {
			return false;
		}
		for (Scenario s: scenes) {
			if (s.getId() == scene.getId()) {
				return true;
			}
		}
		return false;
	}

	private void setNextScenes() {
		for (int i = 0; i < scenes.size()-1; i++) {
			if (scenes.get(i).getNextScene() == null) {
				scenes.get(i).setNextScene(scenes.get(i+1));
			}
		}
	}
}