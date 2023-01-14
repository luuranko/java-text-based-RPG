package Services;

import java.util.Random;
import IO.PlayerInput;
import IO.AnyIO;
import IO.AnyCombatIO;
import Data.Elements;
import Data.Scenes;

public class Locator {
	private static Random rng;
	private static PlayerInput input;
	private static AnyIO io;
	private static AnyCombatIO comIO;
	private static Elements elems;
	private static Scenes scenes;

	public static Random rng() {
		return rng;
	}

	public static PlayerInput input() {
		return input;
	}

	public static AnyIO io() {
		return io;
	}

	public static AnyCombatIO comIO() {
		return comIO;
	}

	public static Elements elems() {
		return elems;
	}

	public static Scenes scenes() {
		return scenes;
	}

	public static void provideRng(Random random) {
		rng = random;
	}

	public static void provideInput(PlayerInput i) {
		input = i;
	}

	public static void provideIO(AnyIO anyIO) {
		io = anyIO;
	}

	public static void provideComIO(AnyCombatIO anyComIO) {
		comIO = anyComIO;
	}

	public static void provideElems(Elements e) {
		elems = e;
	}

	public static void provideScenes(Scenes s) {
		scenes = s;
	}
}