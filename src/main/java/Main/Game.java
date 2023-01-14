package Main;

import Logic.ScenarioHandler;
import Services.Locator;

public class Game {
    private ScenarioHandler sceneHandler;

    public Game() throws InterruptedException {
        this.sceneHandler = new ScenarioHandler();
    }

    public boolean run() throws InterruptedException {
        boolean running = true;
        sceneHandler.runPlayerChoose();
        while(running) {
            running = sceneHandler.runScene(sceneHandler.getCurrent());
        }
        Locator.io().gameEnd();
        if (sceneHandler.getHero().isAlive()) {
            return true;
        } else {
            return false;
        }
    }
}
