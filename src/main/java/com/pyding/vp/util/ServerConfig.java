package com.pyding.vp.util;

import com.pyding.vp.capability.VestigeCap;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue firstPlayer = BUILDER.comment("Is first player logged in (For welcome book)").define("firstPlayer", false);
    public static final ModConfigSpec.BooleanValue usedBook = BUILDER.comment("Used Welcome Book. Second one can be used only with Creative.").define("usedBook", false);

    public static final ModConfigSpec.ConfigValue<List<Integer>> reduceChallenges = BUILDER
            .comment("Those are numbers for each Challenge to reduce their maximum")
            .define("reduceChallenges", getReduceList());

    public static final ModConfigSpec.BooleanValue reduceChallengesPercent = BUILDER
            .comment("If true, numbers above for reducing Challenges maximum number will count as Percent from maximum")
            .define("reduceChallengesPercent", false);

    public static final ModConfigSpec.DoubleValue maxPower = BUILDER
            .comment("Maximum power of Vestiges.")
            .defineInRange("maxPower", 100d, 1d, 2100000000d);

    public static final ModConfigSpec.ConfigValue<List<Integer>> powerScales = BUILDER
            .comment("Each Vestige Power Scale in percents.")
            .define("powerScales",getSacleList());

    public static final ModConfigSpec.DoubleValue powerBoost = BUILDER
            .comment("Power Scale increase by completing new challenges.")
            .defineInRange("powerBoost", 5d, 1d, 2100000000d);

    // --- Cruel Mode ---
    public static final ModConfigSpec.BooleanValue cruelMode = BUILDER
            .comment("Enables Cruel mode: all bosses will have x4 hp, x2 damage, 100 armor, Shields and Over Shield, Healing, damage absorption 90%")
            .define("cruelMode", false);

    public static final ModConfigSpec.DoubleValue bossHP = BUILDER
            .comment("Cruel mode Hp scale")
            .defineInRange("bossHP", 1.5d, 1d, 2100000000d);

    public static final ModConfigSpec.DoubleValue bossAttack = BUILDER
            .comment("Cruel mode attack scale")
            .defineInRange("bossAttack", 3d, 1d, 2100000000d);

    public static final ModConfigSpec.IntValue armorCruel = BUILDER
            .comment("Cruel mode armor and armor toughness")
            .defineInRange("armorCruel", 60, 1, 2100000000);

    public static final ModConfigSpec.DoubleValue damageCruel = BUILDER
            .comment("Cruel mode damage percent from maximum hp when starving, drowning. Set 0 to disable.")
            .defineInRange("damageCruel", 0.15d, 0d, 2100000000d);

    public static final ModConfigSpec.DoubleValue absorbCruel = BUILDER
            .comment("Cruel mode DPS cap from max health %, 0.1 is 10%")
            .defineInRange("absorbCruel", 0.4d, 0d, (double) Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue shieldCruel = BUILDER
            .comment("Cruel mode Shield from hp percent 1 is 100%")
            .defineInRange("shieldCruel", 0.75d, 0.1d, 2100000000d);

    public static final ModConfigSpec.DoubleValue overShieldCruel = BUILDER
            .comment("Cruel mode Over Shield from hp percent")
            .defineInRange("overShieldCruel", 0.25d, 0.1d, 2100000000d);

    public static final ModConfigSpec.DoubleValue healPercent = BUILDER
            .comment("Cruel mode Heal percent from max hp")
            .defineInRange("healPercent", 0.005d, 0d, 2100000000d);

    public static final ModConfigSpec.DoubleValue expMultiplier = BUILDER
            .comment("Exp base multiplier from bosses in Cruel mode")
            .defineInRange("expMultiplier", 10d, 1d, 2100000000d);

    public static final ModConfigSpec.DoubleValue empoweredChance = BUILDER
            .comment("Chance to spawn Empowered mob in Hardcore mode")
            .defineInRange("empoweredChance", 0.01d, 0d, 2100000000d);

    public static final ModConfigSpec.DoubleValue healthBoost = BUILDER
            .comment("Health Boost for all monsters in Cruel Mode. Leave at 1.0 to disable.")
            .defineInRange("healthBoost", 2.0d, 0d, 2100000000d);

    public static final ModConfigSpec.DoubleValue cruelItemChance = BUILDER
            .comment("Base chance for Cruel Mode exclusive item to drop, like Orb of Chaos or Mirror.")
            .defineInRange("cruelItemChance", 0.001d, 0.0001d, 1d);

    public static final ModConfigSpec.DoubleValue rareItemChance = BUILDER.defineInRange("rareItemChance", 0.025d, 0.0001d, 1d);

    public static final ModConfigSpec.BooleanValue leaderboard = BUILDER.define("leaderboard", false);

    public static List<Integer> getReduceList(){
        List<Integer> reduceList = new ArrayList<>();
        for (int i = 0; i < VestigeCap.totalVestiges; i++)
            reduceList.add(0);
        return reduceList;
    }

    public static List<Integer> getSacleList(){
        List<Integer> scaleList = new ArrayList<>();
        for(int i = 0; i < VestigeCap.totalVestiges; i++)
            scaleList.add(30);
        return scaleList;
    }

    public static int getChallengeReduceByNumber(int number) {
        return reduceChallenges.get().get(number-1);
    }

    public static int powerScale(int number) {
        return powerScales.get().get(number);
    }



    public static final ModConfigSpec SPEC = BUILDER.build();
}
