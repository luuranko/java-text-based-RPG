package IO;
import java.text.DecimalFormat;

public class Statics {
    
    public static int upgradeCost = 2;
    public static int defaultDeathDefiances = 2;


    // DEFAULT HIT RATE IS 70%
    public static double hr = 0.7;
    // Modifier for additional Hit Rate. Multiplied by unit's speed
    public static double hrMod = 0.05;
    // Modifier for evasion. Multiplied by unit's speed
    public static double evMod = 0.03;
    // DEFAULT CRIT RATE IS 3%
    public static double cr = 0.03;
    // Modifier for Crit Rate. Multiplied by unit's strength
    public static double crMod = 0.025;
    // Modifier for Crit Resist Rate. Multiplied by unit's defense
    public static double crreMod = 0.015;
    // Modifier for Debuff Resist Rate. Multiplied by unit's defense
    public static double dbreMod = 0.02;


    // UPGRADED ATTRIBUTE
    public static double chanceToAddPwr = 0.6;
    public static double addHr = 0.025;
    public static double addCr = 0.015;
    public static double chanceToReduceCooldown = 0.5;
    public static double addChance = 0.015;
    // Effects
    public static double addHpChange = 0.33;
    public static double chanceToChangeCooldown = 0.5;
    // Stati
    public static double chanceToIncreaseDuration = 0.5;
    public static double chanceToChangeStat = 0.5;
    public static double rateChange = 0.025;
    public static double chanceToChangeHpPerTurn = 0.7;

    public static String[] statNames = {"HP", "STR", "DEF", "SPD"};
    public static String[] rateNames = {"Hit Rate", "Evasion", "Crit Rate", "Crit Res", "Debuff Res"};

    private static String roundPattern = "##.000"; 
    private static DecimalFormat df = new DecimalFormat(roundPattern);

    public static double roundDouble(double d) {
        String s = df.format(d);
        double r = Double.parseDouble(s);
        return r;
    }

    public static int specialUpgradeCost(int level) {
        return level + upgradeCost;
    }

    public static int invocationUpgradeCost(int level) {
        return level + upgradeCost + 1;
    }

    // TEXT STYLE
    public static final String STYLE_RESET = "\033[0;0m";
    public static final String STYLE_BOLD = "\033[0;1m";
    public static final String STYLE_FAINT = "\033[0;2m";
    public static final String STYLE_ITALIC = "\033[0;3m";
    public static final String STYLE_UNDERLINE = "\033[0;4m";

    // TEXT COLORS
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String GREY = "\u001B[90m";

    // TEXT BG COLORS
    public static final String BLACK_BG = "\u001B[40m";
    public static final String RED_BG = "\u001B[41m";
    public static final String GREEN_BG = "\u001B[42m";
    public static final String YELLOW_BG = "\u001B[43m";
    public static final String BLUE_BG = "\u001B[44m";
    public static final String PURPLE_BG = "\u001B[45m";
    public static final String CYAN_BG = "\u001B[46m";
    public static final String WHITE_BG = "\u001B[47m";
}
