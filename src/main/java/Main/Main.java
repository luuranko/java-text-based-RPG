package Main;

import java.util.Random;
import java.util.Scanner;

import Data.Elements;
import Data.FileReader;
import Data.Scenes;
import IO.*;
import Services.Locator;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("GAME [1] / AUTOGAME [2] / MASSIVE PLAYTEST [3]?");
        String answer = sc.nextLine();

        boolean proceed = false;

        switch(answer) {
            case "1":
                proceed = init(new CommandLineInput(), new IO(), new CombatIO());
                break;
            case "2":
                proceed = init(new AutoInput(), new IO(), new CombatIO());
                break;
            case "3":
                proceed = init(new AutoInput(), new AutoIO(), new AutoCombatIO());
                break;
            default:
                break;
        }

        if (!proceed) {
            System.out.println("Game initialization failed.");
        } else {
            if (answer.equals("1")) {
                play(game());
            } else {
                int runs = answer.equals("2") ? 3 : 1000;
                System.out.println("HOW MANY RUNS? (Default: " + runs + ")");
                try {
                    runs = Integer.parseInt(sc.nextLine());
                } catch(NumberFormatException e) {
                    System.out.println("Runs: " + runs);
                }
                playTest(runs, game());
            }
        }
        sc.close();
    }

    private static boolean init(PlayerInput input, AnyIO io, AnyCombatIO comIO) {
        Locator.provideRng(new Random());
        Locator.provideInput(input);
        Locator.provideIO(io);
        Locator.provideComIO(comIO);
        io.init();
        comIO.init();
        boolean ok = initElems();
        if (!ok) {
            return false;
        }
        ok = initScenes();
        return ok;
    }

    private static boolean initElems() {
        Locator.provideElems(new Elements());
        FileReader reader = new FileReader();
        reader.initElems();

        String effectFile = "Files/Elems/Effects.txt";
        String statusFile = "Files/Elems/Stati.txt";
        String attackFile = "Files/Elems/Attacks.txt";
        String specialFile = "Files/Elems/Specials.txt";
        String enemyFile = "Files/Elems/Enemies.txt";
        String invocationFile = "Files/Elems/Invocations.txt";
        String resistanceFile = "Files/Elems/Resistances.txt";

        boolean ok = reader.loadElems(effectFile, statusFile, attackFile, specialFile, enemyFile, invocationFile, resistanceFile);
        
        return ok;
    }

    private static boolean initScenes() {
        Locator.provideScenes(new Scenes());
        FileReader reader = new FileReader();
        reader.initElems();
        reader.initScenes();

        String descripFile = "Files/Scenes/Descriptions.txt";
        String choiceSetFile = "Files/Scenes/ChoiceSets.txt";
        String scenesFile = "Files/Scenes/Scenarios.txt";

        boolean ok = reader.loadScenes(descripFile, choiceSetFile, scenesFile);
        
        return ok;
    }

    private static Game game() throws InterruptedException {
        Game game = new Game();
        return game;
    }

    private static void play(Game game) throws InterruptedException {
        game.run();
    }

    private static void playTest(int tries, Game game) throws InterruptedException {
        int victories = 0;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < tries; i++) {
            boolean victory = game.run();
            if (victory) {
                victories++;
            }
        }
        long endTime = System.currentTimeMillis();
        double winningPercentage = (victories/(double)tries*1.0);
        winningPercentage *= 100;
        System.out.println("VICTORY %: " + winningPercentage + "%");
        long time = endTime - startTime;
        double seconds = (double)time*1.0 / 1000;
        System.out.println("Simulation took: " + seconds + " s");
    }
}