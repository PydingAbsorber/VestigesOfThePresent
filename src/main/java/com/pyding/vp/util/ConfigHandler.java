package com.pyding.vp.util;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigHandler {
    public static final ConfigHandler.Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    public ConfigHandler() {
    }

    static {
        Pair<ConfigHandler.Common, ForgeConfigSpec> specPair = (new ForgeConfigSpec.Builder()).configure(ConfigHandler.Common::new);
        COMMON_SPEC = (ForgeConfigSpec)specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {
        //public final ForgeConfigSpec.ConfigValue<String> challengeReduction;
        public final ForgeConfigSpec.BooleanValue hardcore;
        public final ForgeConfigSpec.BooleanValue anomaly;
        public final ForgeConfigSpec.IntValue cooldown;
        public final ForgeConfigSpec.IntValue chaostime;
        public final ForgeConfigSpec.IntValue devourer;
        public final ForgeConfigSpec.IntValue blackhole;
        public final ForgeConfigSpec.IntValue stellarChanceIncrease;
        public final ForgeConfigSpec.IntValue challengeReduce1;
        public final ForgeConfigSpec.IntValue challengeReduce2;
        public final ForgeConfigSpec.IntValue challengeReduce3;
        public final ForgeConfigSpec.IntValue challengeReduce4;
        public final ForgeConfigSpec.IntValue challengeReduce5;
        public final ForgeConfigSpec.IntValue challengeReduce6;
        public final ForgeConfigSpec.IntValue challengeReduce7;
        public final ForgeConfigSpec.IntValue challengeReduce8;
        public final ForgeConfigSpec.IntValue challengeReduce9;
        public final ForgeConfigSpec.IntValue challengeReduce10;
        public final ForgeConfigSpec.IntValue challengeReduce11;
        public final ForgeConfigSpec.IntValue challengeReduce12;
        public final ForgeConfigSpec.IntValue challengeReduce13;
        public final ForgeConfigSpec.IntValue challengeReduce14;
        public final ForgeConfigSpec.IntValue challengeReduce15;
        public final ForgeConfigSpec.IntValue challengeReduce16;
        public final ForgeConfigSpec.IntValue challengeReduce17;
        public final ForgeConfigSpec.IntValue challengeReduce18;
        public final ForgeConfigSpec.IntValue challengeReduce19;
        public final ForgeConfigSpec.IntValue challengeReduce20;

        public Common(ForgeConfigSpec.Builder builder) {
            //challengeReduction = builder.comment("Number for challenge №").define("challengeReduction", "shop_table");
            hardcore = builder.comment("Enables hardcore mode: all bosses will have x10 hp and x2 damage").define("hardcore", false);
            cooldown = builder.comment("Challenge cooldown in hours").defineInRange("cooldown", 8, 0, 2100000000);
            stellarChanceIncrease = builder.comment("How many % of stellar chance will you get on failure").defineInRange("stellarChanceIncrease", 10, 0, 100);
            challengeReduce1 = builder.comment("This is the number on how many challenge 1 maximum progress will be reduced").defineInRange("challengeReduce1", 0, 0, 2100000000);
            challengeReduce2 = builder.comment("This is the number on how many challenge 2 maximum progress will be reduced").defineInRange("challengeReduce2", 0, 0, 2100000000);
            challengeReduce3 = builder.comment("This is the number on how many challenge 3 maximum progress will be reduced").defineInRange("challengeReduce3", 0, 0, 2100000000);
            challengeReduce4 = builder.comment("This is the number on how many challenge 4 maximum progress will be reduced").defineInRange("challengeReduce4", 0, 0, 2100000000);
            challengeReduce5 = builder.comment("This is the number on how many challenge 5 maximum progress will be reduced").defineInRange("challengeReduce5", 0, 0, 2100000000);
            challengeReduce6 = builder.comment("This is the number on how many challenge 6 maximum progress will be reduced").defineInRange("challengeReduce6", 0, 0, 2100000000);
            challengeReduce7 = builder.comment("This is the number on how many challenge 7 maximum progress will be reduced").defineInRange("challengeReduce7", 0, 0, 2100000000);
            challengeReduce8 = builder.comment("This is the number on how many challenge 8 maximum progress will be reduced").defineInRange("challengeReduce8", 0, 0, 2100000000);
            challengeReduce9 = builder.comment("This is the number on how many challenge 9 maximum progress will be reduced").defineInRange("challengeReduce9", 0, 0, 2100000000);
            challengeReduce10 = builder.comment("This is the number on how many challenge 10 maximum progress will be reduced").defineInRange("challengeReduce10", 0, 0, 2100000000);
            challengeReduce11 = builder.comment("This is the number on how many challenge 11 maximum progress will be reduced").defineInRange("challengeReduce11", 0, 0, 2100000000);
            challengeReduce12 = builder.comment("This is the number on how many challenge 12 maximum progress will be reduced").defineInRange("challengeReduce12", 0, 0, 2100000000);
            challengeReduce13 = builder.comment("This is the number on how many challenge 13 maximum progress will be reduced").defineInRange("challengeReduce13", 0, 0, 2100000000);
            challengeReduce14 = builder.comment("This is the number on how many challenge 14 maximum progress will be reduced").defineInRange("challengeReduce14", 0, 0, 2100000000);
            challengeReduce15 = builder.comment("This is the number on how many challenge 15 maximum progress will be reduced").defineInRange("challengeReduce15", 0, 0, 2100000000);
            challengeReduce16 = builder.comment("This is the number on how many challenge 16 maximum progress will be reduced").defineInRange("challengeReduce16", 0, 0, 2100000000);
            challengeReduce17 = builder.comment("This is the number on how many challenge 17 maximum progress will be reduced").defineInRange("challengeReduce17", 0, 0, 2100000000);
            challengeReduce18 = builder.comment("This is the number on how many challenge 18 maximum progress will be reduced").defineInRange("challengeReduce18", 0, 0, 2100000000);
            challengeReduce19 = builder.comment("This is the number on how many challenge 19 maximum progress will be reduced").defineInRange("challengeReduce19", 0, 0, 2100000000);
            challengeReduce20 = builder.comment("This is the number on how many challenge 20 maximum progress will be reduced").defineInRange("challengeReduce20", 0, 0, 2100000000);
            devourer = builder.comment("How many hits can cause Soul Rotting from Devourer").defineInRange("devourer", 30, 0, 2100000000);
            blackhole = builder.comment("How many ticks must pass before Black Hole hits").defineInRange("blackhole", 4, 0, 2100000000);
            anomaly = builder.comment("Should anomaly teleport only living entities").define("anomaly", false);
            chaostime = builder.comment("Minutes before Chaos Core challenge reset").defineInRange("chaostime", 15, 1, 2100000000);
        }

        public ForgeConfigSpec.IntValue getChallengeReduceByNumber(int number) {
            switch (number) {
                case 1:
                    return challengeReduce1;
                case 2:
                    return challengeReduce2;
                case 3:
                    return challengeReduce3;
                case 4:
                    return challengeReduce4;
                case 5:
                    return challengeReduce5;
                case 6:
                    return challengeReduce6;
                case 7:
                    return challengeReduce7;
                case 8:
                    return challengeReduce8;
                case 9:
                    return challengeReduce9;
                case 10:
                    return challengeReduce10;
                case 11:
                    return challengeReduce11;
                case 12:
                    return challengeReduce12;
                case 13:
                    return challengeReduce13;
                case 14:
                    return challengeReduce14;
                case 15:
                    return challengeReduce15;
                case 16:
                    return challengeReduce16;
                case 17:
                    return challengeReduce17;
                case 18:
                    return challengeReduce18;
                case 19:
                    return challengeReduce19;
                case 20:
                    return challengeReduce20;
                default:
                    return null;
            }
        }
    }
}
