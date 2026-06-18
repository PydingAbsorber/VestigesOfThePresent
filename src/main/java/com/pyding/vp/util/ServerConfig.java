package com.pyding.vp.util;

import com.pyding.vp.capability.PlayerCapabilityVP;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class ServerConfig {
    public static final ServerConfig.Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    public ServerConfig() {
    }

    static {
        Pair<ServerConfig.Common, ForgeConfigSpec> specPair = (new ForgeConfigSpec.Builder()).configure(ServerConfig.Common::new);
        COMMON_SPEC = (ForgeConfigSpec)specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {
        public final ForgeConfigSpec.BooleanValue firstPlayer;
        public final ForgeConfigSpec.BooleanValue usedBook;
        public final ForgeConfigSpec.BooleanValue cruelMode;
        public final ForgeConfigSpec.IntValue armorCruel;
        public final ForgeConfigSpec.DoubleValue damageCruel;
        public final ForgeConfigSpec.DoubleValue absorbCruel;
        public final ForgeConfigSpec.DoubleValue shieldCruel;
        public final ForgeConfigSpec.DoubleValue overShieldCruel;
        public final ForgeConfigSpec.DoubleValue expMultiplier;
        public final ForgeConfigSpec.DoubleValue empoweredChance;
        public final ForgeConfigSpec.DoubleValue healthBoost;
        public final ForgeConfigSpec.DoubleValue bossHP;
        public final ForgeConfigSpec.DoubleValue bossAttack;
        public final ForgeConfigSpec.DoubleValue healPercent;
        public final ForgeConfigSpec.DoubleValue cruelItemChance;

        public final ForgeConfigSpec.DoubleValue maxPower;
        public final ForgeConfigSpec.DoubleValue powerBoost;
        public final ForgeConfigSpec.ConfigValue<List<Integer>> reduceChallenges;
        public final ForgeConfigSpec.ConfigValue<List<Integer>> powerScales;
        public final ForgeConfigSpec.BooleanValue reduceChallengesPercent;
        public final ForgeConfigSpec.BooleanValue leaderboard;
        public final ForgeConfigSpec.DoubleValue rareItemChance;

        public Common(ForgeConfigSpec.Builder builder) {
            firstPlayer = builder.comment("Is first player logged in (For welcome book)").define("firstPlayer",false);
            usedBook = builder.comment("Used Welcome Book. Second one can be used only with Creative.").define("usedBook",false);
            cruelMode = builder.comment("Enables Cruel mode: all bosses will have x4 hp, x2 damage, 100 armor, Shields and Over Shield, Healing, damage absorption 90%").define("cruelMode", false);
            bossHP = builder.comment("Cruel mode Hp scale").defineInRange("bossHP", 1.5d, 1, 2100000000);
            bossAttack = builder.comment("Cruel mode attack scale").defineInRange("bossAttack", 3d, 1, 2100000000);
            armorCruel = builder.comment("Cruel mode armor and armor toughness").defineInRange("armorCruel", 60, 1, 2100000000);
            damageCruel = builder.comment("Cruel mode damage percent from maximum hp when starving, drowning. Set 0 to disable.").defineInRange("damageCruel", 0.15d, 0, 2100000000);
            absorbCruel = builder.comment("Cruel mode DPS cap from max health %, 0.1 is 10%").defineInRange("absorbCruel", 0.25, 0, Integer.MAX_VALUE);
            shieldCruel = builder.comment("Cruel mode Shield from hp percent 1 is 100%").defineInRange("shieldCruel", 0.75d, 0.1, 2100000000);
            overShieldCruel = builder.comment("Cruel mode Over Shield from hp percent").defineInRange("overShieldCruel", 0.25, 0.1, 2100000000);
            healPercent = builder.comment("Cruel mode Heal percent from max hp").defineInRange("healPercent", 0.005, 0, 2100000000);
            expMultiplier = builder.comment("Exp base multiplier from bosses in Cruel mode").defineInRange("expMultiplier", 10d, 1d, 2100000000);
            empoweredChance = builder.comment("Chance to spawn Empowered mob in Hardcore mode").defineInRange("empoweredChance", 0.01, 0, 2100000000);
            healthBoost = builder.comment("Health Boost for all monsters in Cruel Mode. Leave at 1.0 to disable.").defineInRange("healthBoost", 2.0, 0, 2100000000);
            cruelItemChance = builder.comment("Base chance for Cruel Mode exclusive item to drop, like Orb of Chaos or Mirror.").defineInRange("cruelItemChance", 0.001, 0, 2100000000);

            List<Integer> reduceList = new ArrayList<>();
            for(int i = 0; i < PlayerCapabilityVP.totalVestiges; i++)
                reduceList.add(0);
            reduceChallenges = builder.comment("Those are numbers for each Challenge to reduce their maximum").define("reduceChallenges",reduceList);
            reduceChallengesPercent = builder.comment("If true, numbers above for reducing Challenges maximum number will count as Percent from maximum").define("reduceChallengesPercent", true);
            maxPower = builder.comment("Maximum power of Vestiges.").defineInRange("maxPower", 100d, 1d, 2100000000d);
            List<Integer> scaleList = new ArrayList<>();
            for(int i = 0; i < PlayerCapabilityVP.totalVestiges; i++)
                scaleList.add(30);
            powerScales = builder.comment("Each Vestige Power Scale in percents.").define("powerScales",scaleList);
            powerBoost = builder.comment("Power Scale increase by completing new challenges.").defineInRange("powerBoost", 5d, 1d, 2100000000d);
            leaderboard = builder.comment("Defines if Leaderboard should be enabled: ").define("leaderboard",false);
            rareItemChance = builder.comment("Chance for Item to define as rare for Prism challenge. For example carrot has 0.025 chance to drop from zobmie").defineInRange("rareItemChance", 0.025d, 0.0001, 1);
        }

        public int getChallengeReduceByNumber(int number) {
            return reduceChallenges.get().get(number-1);
        }

        public int powerScale(int number) {
            return powerScales.get().get(number);
        }
    }
}
