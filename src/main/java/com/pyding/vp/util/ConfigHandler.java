package com.pyding.vp.util;

import com.pyding.vp.capability.PlayerCapabilityVP;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

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
        public final ForgeConfigSpec.BooleanValue easter;
        public final ForgeConfigSpec.IntValue armorHardcore;
        public final ForgeConfigSpec.DoubleValue ballShield;
        public final ForgeConfigSpec.DoubleValue ballOverShield;
        public final ForgeConfigSpec.DoubleValue absorbHardcore;
        public final ForgeConfigSpec.DoubleValue shieldHardcore;
        public final ForgeConfigSpec.DoubleValue overShieldHardcore;
        public final ForgeConfigSpec.DoubleValue healPercent;
        public final ForgeConfigSpec.BooleanValue anomaly;
        public final ForgeConfigSpec.IntValue cooldown;
        public final ForgeConfigSpec.IntValue bossHP;
        public final ForgeConfigSpec.IntValue bossAttack;
        public final ForgeConfigSpec.IntValue chaostime;
        public final ForgeConfigSpec.IntValue devourer;
        public final ForgeConfigSpec.IntValue blackhole;
        public final ForgeConfigSpec.IntValue stellarChanceIncrease;
        public final ForgeConfigSpec.IntValue armorAbsorbBase;
        public final ForgeConfigSpec.DoubleValue armorAbsorbPercent;
        public final ForgeConfigSpec.IntValue anomalyBorder;
        public final ForgeConfigSpec.DoubleValue atlasChance;
        public final ForgeConfigSpec.IntValue catalystLvlLimit;
        public final ForgeConfigSpec.IntValue catalystDeffence;
        public final ForgeConfigSpec.DoubleValue catSpeed;
        public final ForgeConfigSpec.IntValue catEvadeCap;
        public final ForgeConfigSpec.IntValue chaosDamageCap;
        public final ForgeConfigSpec.DoubleValue chaosChance;
        public final ForgeConfigSpec.IntValue crownShield;
        public final ForgeConfigSpec.DoubleValue devourerChance;
        public final ForgeConfigSpec.DoubleValue flowerShield;
        public final ForgeConfigSpec.DoubleValue killerRes;
        public final ForgeConfigSpec.IntValue markMaximum;
        public final ForgeConfigSpec.IntValue markHealDebt;
        public final ForgeConfigSpec.IntValue maskRotAmount;
        public final ForgeConfigSpec.DoubleValue midasChance;
        public final ForgeConfigSpec.DoubleValue prismChance;
        public final ForgeConfigSpec.DoubleValue soulBlighterChance;
        public final ForgeConfigSpec.DoubleValue soulBlighterHeal;
        public final ForgeConfigSpec.DoubleValue trigonHeal;
        public final ForgeConfigSpec.IntValue donutMaxSaturation;
        public final ForgeConfigSpec.IntValue donutHealBonus;
        public final ForgeConfigSpec.IntValue markBonus;

        public final ForgeConfigSpec.IntValue chaosCharges;
        public final ForgeConfigSpec.LongValue devourerCdTime;

        public final ForgeConfigSpec.DoubleValue refresherChance;
        public final ForgeConfigSpec.IntValue vortexReduction;
        public final ForgeConfigSpec.IntValue easterChance;
        public final ForgeConfigSpec.ConfigValue bosses;
        public final ForgeConfigSpec.ConfigValue repairObjects;
        public final ForgeConfigSpec.ConfigValue repairBlackList;
        public final ForgeConfigSpec.ConfigValue rareItems;
        public final ForgeConfigSpec.ConfigValue fishingBlacklist;
        public final ForgeConfigSpec.DoubleValue nightmareBoxChance;
        public final ForgeConfigSpec.DoubleValue nightmareRefresherChance;
        public final ForgeConfigSpec.IntValue nightmareFrags;
        public final ForgeConfigSpec.IntValue nightmareLoot;
        public final ForgeConfigSpec.IntValue nightmareLootMin;
        public final ForgeConfigSpec.IntValue nightmareFragsMin;
        public final ForgeConfigSpec.IntValue nightmareBoxes;
        public final ForgeConfigSpec.IntValue nightmareBoxesMin;
        public final ForgeConfigSpec.IntValue eatingMinutes;
        public final ForgeConfigSpec.BooleanValue failFlowers;
        public final ForgeConfigSpec.ConfigValue<List<Integer>> reduceChallenges;
        public final ForgeConfigSpec.BooleanValue reduceChallengesPercent;
        public final ForgeConfigSpec.ConfigValue fishObjects;
        public final ForgeConfigSpec.DoubleValue hardcoreDamage;
        public final ForgeConfigSpec.DoubleValue nightmareDamageCap;
        public final ForgeConfigSpec.BooleanValue unlockHp;
        public final ForgeConfigSpec.BooleanValue eventMode;
        public final ForgeConfigSpec.DoubleValue rareItemChance;
        public final ForgeConfigSpec.DoubleValue empoweredChance;
        public final ForgeConfigSpec.ConfigValue debuffBlacklist;

        public Common(ForgeConfigSpec.Builder builder) {
            refresherChance = builder.comment("Chance for Refresher after completing Stellar challenge. 1 is 100%, 0.5 is 50%.").defineInRange("refresherChance", 0.5d, 0, 1);
            List<Integer> reduceList = new ArrayList<>();
            for(int i = 0; i < PlayerCapabilityVP.totalVestiges; i++)
                reduceList.add(0);
            reduceChallenges = builder.comment("Those are numbers for each Challenge to reduce their maximum").define("reduceChallenges",reduceList);
            reduceChallengesPercent = builder.comment("If true, numbers above for reducing Challenges maximum number will count as Percent from maximum").define("reduceChallengesPercent", false);

            hardcore = builder.comment("Enables hardcore mode: all bosses will have x4 hp, x2 damage, 100 armor, Shields and Over Shield, Healing, damage absorption 90%").define("hardcore", false);
            bossHP = builder.comment("Hardcore mode Hp scale").defineInRange("bossHP", 4, 1, 2100000000);
            bossAttack = builder.comment("Hardcore mode attack scale").defineInRange("bossAttack", 2, 1, 2100000000);
            armorHardcore = builder.comment("Hardcore mode armor and armor toughness").defineInRange("armorHardcore", 100, 1, 2100000000);
            absorbHardcore = builder.comment("Hardcore mode damage absorb percent").defineInRange("absorbHardcore", 0.9, 0, 1);
            shieldHardcore = builder.comment("Hardcore mode Shield from hp percent 1 is 100%").defineInRange("shieldHardcore", 1.5d, 0.1, 2100000000);
            overShieldHardcore = builder.comment("Hardcore mode Over Shield from hp percent").defineInRange("overShieldHardcore", 0.5, 0.1, 2100000000);
            healPercent = builder.comment("Hardcore mode Heal percent from max hp").defineInRange("healPercent", 0.005, 0, 2100000000);
            hardcoreDamage = builder.comment("Hardcore mode damage percent from maximum hp when starving, drowning. Set 0 to disable.").defineInRange("hardcoreDamage", 0.15d, 0, 2100000000);


            nightmareBoxChance = builder.comment("Nightmare Bosses boxes chance 0.5 is 50%").defineInRange("nightmareBoxChance", 0.5, 0, 1);
            nightmareRefresherChance = builder.comment("Nightmare Bosses Refresher chance 0.1 is 10%").defineInRange("nightmareRefresherChance", 0.1, 0, 1);
            nightmareFrags = builder.comment("Nightmare Bosses max fragments").defineInRange("nightmareFrags", 26, 1, 2100000000);
            nightmareFragsMin = builder.comment("Nightmare Bosses min fragments").defineInRange("nightmareFragsMin", 13, 1, 2100000000);
            nightmareLoot = builder.comment("Nightmare Bosses max loot multiplier").defineInRange("nightmareLoot", 20, 1, 2100000000);
            nightmareLootMin = builder.comment("Nightmare Bosses min loot multiplier").defineInRange("nightmareLootMin", 10, 1, 2100000000);
            nightmareBoxes = builder.comment("Nightmare Bosses max boxes").defineInRange("nightmareBoxes", 8, 1, 2100000000);
            nightmareBoxesMin = builder.comment("Nightmare Bosses min boxes").defineInRange("nightmareBoxesMin", 4, 1, 2100000000);


            cooldown = builder.comment("Challenge cooldown in hours").defineInRange("cooldown", 8, 0, 2100000000);
            stellarChanceIncrease = builder.comment("How many % of stellar chance will you get on failure").defineInRange("stellarChanceIncrease", 10, 0, 100);
            armorAbsorbBase = builder.comment("Base Martyr's Habergeon(armor) value").defineInRange("armorAbsorbBase", 40, 0, Integer.MAX_VALUE);
            armorAbsorbPercent = builder.comment("Martyr's Habergeon absorption increase %. 1 is 100%, 0.1 is 10%.").defineInRange("armorAbsorbPercent", 1d, 0, 1d);
            anomalyBorder = builder.comment("Borders for Anomaly's Ultimate teleportation. Leave at 0 for world's max border.").defineInRange("anomalyBorder", 0, 0, Integer.MAX_VALUE);
            atlasChance = builder.comment("Chance for Atlas to gain Gravity. 1 is 100%, 0.2 is 20%.").defineInRange("atlasChance", 0.2d, 0, 1d);
            catalystLvlLimit = builder.comment("Level limit for stellar Catalyst").defineInRange("catalystLvlLimit", 255, 0, 255);
            catalystDeffence = builder.comment("How many debuffs can Catalyst absorb.").defineInRange("catalystDeffence", 5, 0, Integer.MAX_VALUE);
            catSpeed = builder.comment("How many speed will Cat Ears give.").defineInRange("catSpeed", 1d, 0, 10d);
            catEvadeCap = builder.comment("Cap for Cat Ears evasion.").defineInRange("catEvadeCap", 69, 0, 100);
            chaosDamageCap = builder.comment("Damage cap for Chaos Core").defineInRange("chaosDamageCap", Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
            chaosChance = builder.comment("Chance for Chaos Core reflexion or damage change. 1 is 100%, 0.1 is 10%.").defineInRange("chaosChance", 0.1, 0, 1);
            crownShield = builder.comment("How many % from enemy's health will crown give upon kill").defineInRange("crownShield", 50, 0, Integer.MAX_VALUE);
            devourerChance = builder.comment("Chance for Devourer's Ultimate per 1 kill. 1 is 100%, 0.001 is 0.1%").defineInRange("devourerChance", 0.01, 0, 100);
            flowerShield = builder.comment("Multiplier for Shield that will Flower give by Ultimate.").defineInRange("flowerShield", 1d, 0, Integer.MAX_VALUE);
            killerRes = builder.comment("Killer Queens' Special explosion damage resistance shred").defineInRange("killerRes", 70d, 0, Integer.MAX_VALUE);
            markMaximum = builder.comment("Maximum value of Cursed Mark Madness stacks").defineInRange("markMaximum", 10, 0, Integer.MAX_VALUE);
            markHealDebt = builder.comment("Heal debt of stellar Cursed Mark Ultimate in max health %").defineInRange("markHealDebt", 2000, 0, Integer.MAX_VALUE);
            maskRotAmount = builder.comment("Mask of Demon's Special hp consumption value in max health %").defineInRange("maskRotAmount", 10, 0, Integer.MAX_VALUE);
            midasChance = builder.comment("Chance for Midas getting luck from Ultimate per 1 gold nugget.").defineInRange("midasChance", 0.0001d, 0, 1);
            prismChance = builder.comment("Prism loot chance multiplier").defineInRange("prismChance", 1d, 0, Integer.MAX_VALUE);
            soulBlighterChance = builder.comment("Soul Blighter's Ultimate catch chance multiplier").defineInRange("soulBlighterChance", 1d, 0, Integer.MAX_VALUE);
            soulBlighterHeal = builder.comment("Soul Blighter Over Shield heal multiplier").defineInRange("soulBlighterHeal", 1d, 0, Integer.MAX_VALUE);
            trigonHeal = builder.comment("Trigon Over Shield heal multiplier").defineInRange("trigonHeal", 1d, 0, Integer.MAX_VALUE);
            donutMaxSaturation = builder.comment("Sweet Donut max Saturation stacks.").defineInRange("donutMaxSaturation", 400, 0, Integer.MAX_VALUE);
            donutHealBonus = builder.comment("Base heal bonus of Sweet Donut.").defineInRange("donutHealBonus", 40, 0, Integer.MAX_VALUE);
            ballShield = builder.comment("How many Shield % will Ball Lightning reduce").defineInRange("ballShield", 0.1d, 0.01, 1);
            ballOverShield = builder.comment("Hardcore mode Shield from hp percent 1 is 100%").defineInRange("ballOverShield", 0.2d, 0.01, 1);
            chaosCharges = builder.comment("Amount of Chaos Core reflection hits from Special").defineInRange("chaosCharges", 20, 1, Integer.MAX_VALUE);
            devourerCdTime = builder.comment("Devourer cooldown time between rotting hits. 1000 is 1 sec").defineInRange("devourerCdTime", 200, 1, Long.MAX_VALUE);
            markBonus = builder.comment("Amount of stats per Curse for Mark's Overdrive").defineInRange("markBonus", 10, 1, Integer.MAX_VALUE);


            devourer = builder.comment("How many hits can cause Soul Rotting from Devourer").defineInRange("devourer", 30, 0, 2100000000);
            blackhole = builder.comment("How many ticks must pass before Black Hole hits").defineInRange("blackhole", 4, 0, 2100000000);
            anomaly = builder.comment("Should anomaly teleport only living entities").define("anomaly", false);
            chaostime = builder.comment("Minutes before Chaos Core challenge reset").defineInRange("chaostime", 15, 1, 2100000000);
            eatingMinutes = builder.comment("Minutes for fish to fed up").defineInRange("eatingMinutes", 1, 1, 2100000000);

            bosses = builder.comment("additional bosses: ").define("bosses","hullbreaker,tremorzilla,nucleeper, luxtructosaurus,atlatitan,forsaken,void_worm");
            repairObjects = builder.comment("repairObjectsId: ").define("repairObjects","mending,repair,unbreak,restore,heal,ingot");
            repairBlackList = builder.comment("repairBlackListId: ").define("repairBlackList","");
            vortexReduction = builder.comment("Reduction of maximum amount needed for Vortex").defineInRange("vortexReduction", 0, 0, Integer.MAX_VALUE);
            easter = builder.comment("Enables Easter event").define("easter", false);
            easterChance = builder.comment("Additional chance for Easter Egg 10 is 10%").defineInRange("easterChance", 0, 0, Integer.MAX_VALUE);
            fishObjects = builder.comment("fishObjects: ").define("fishObjects","fish,shell,pearl,boot,treasure,sunken,drown,lure,prismarin,water,ocean,coral,shark,whale,manta,rain,abyss,deep,sea,pirate,ship,bottle,wet,river");
            rareItems = builder.comment("Rare Items for fishing by Abyssal Pearl: ").define("rareItems","item.vp.hearty_pearl,item.vp.pinky_pearl,item.vp.seashell,abyssal_heart,ichor_bottle,boot");
            fishingBlacklist = builder.comment("Fishing Blacklist for Abyssal Pearl: ").define("fishingBlacklist","item.vp.pearl");
            failFlowers = builder.comment("Fails flowers Challenge when they are being placed.").define("failFlowers", false);
            nightmareDamageCap = builder.comment("Damage cap for Nightmare Bosses").defineInRange("nightmareDamageCap", 1000,0,Float.MAX_VALUE);
            unlockHp = builder.comment("For servers with maxed out desync and hp lock to 2048 or if you just have problems with MaxHp.").define("unlockHp", false);
            eventMode = builder.comment("Event mode that disables all teleportations and fly.").define("eventMode", false);
            rareItemChance = builder.comment("Chance for Item to define as rare for Prism challenge. For example carrot has 0.025 chance to drop from zobmie").defineInRange("rareItemChance", 0.025d, 0.0001, 1);
            empoweredChance = builder.comment("Chance to spawn Empowered mob in Hardcore mode").defineInRange("empoweredChance", 0.001, 0, 2100000000);
            debuffBlacklist = builder.comment("Defines blacklist for random potion effects as from Heirloom's Special: ").define("debuffBlacklist","crystallized,");
        }

        public int getChallengeReduceByNumber(int number) {
            return reduceChallenges.get().get(number-1);
        }
    }
}
