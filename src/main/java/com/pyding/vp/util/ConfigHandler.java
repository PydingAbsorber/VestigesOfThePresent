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
        public final ForgeConfigSpec.DoubleValue armorAbsorbBase;
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
        public final ForgeConfigSpec.ConfigValue blacklistBosses;
        public final ForgeConfigSpec.ConfigValue repairObjects;
        public final ForgeConfigSpec.ConfigValue repairBlackList;
        public final ForgeConfigSpec.ConfigValue rareItems;
        public final ForgeConfigSpec.ConfigValue fishingBlacklist;
        public final ForgeConfigSpec.DoubleValue rareFishingDropChance;
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
        public final ForgeConfigSpec.ConfigValue cloneBlackList;
        public final ForgeConfigSpec.ConfigValue cloneWhiteList;
        public final ForgeConfigSpec.ConfigValue mirrorUUIDList;
        public final ForgeConfigSpec.ConfigValue dupersList;

        public final ForgeConfigSpec.ConfigValue leaderboardHost;
        public final ForgeConfigSpec.ConfigValue leaderboardPort;
        public final ForgeConfigSpec.BooleanValue leaderboard;
        public final ForgeConfigSpec.DoubleValue chaosCoreStellarHpRes;
        public final ForgeConfigSpec.DoubleValue oysterChance;
        public final ForgeConfigSpec.DoubleValue seashellChance;
        public final ForgeConfigSpec.ConfigValue lootDrops;
        public final ForgeConfigSpec.BooleanValue strictOptimization;
        public final ForgeConfigSpec.DoubleValue mysteryChestAdvancementChance;
        public final ForgeConfigSpec.DoubleValue mysteryChestAdvancementBoost;
        public final ForgeConfigSpec.DoubleValue mysteryChestChallengeChance;

        public Common(ForgeConfigSpec.Builder builder) {
            refresherChance = builder.comment("Chance for Refresher after completing Stellar challenge. 1 is 100%, 0.5 is 50%.").defineInRange("refresherChance", 0.5d, 0, 1);
            List<Integer> reduceList = new ArrayList<>();
            for(int i = 0; i < PlayerCapabilityVP.totalVestiges; i++)
                reduceList.add(0);
            reduceChallenges = builder.comment("Those are numbers for each Challenge to reduce their maximum").define("reduceChallengesList",reduceList);
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
            armorAbsorbBase = builder.comment("Base Martyr's Habergeon damage absorption from Passive").defineInRange("armorAbsorbBase", 0.1d, 0, 1d);
            armorAbsorbPercent = builder.comment("Martyr's Habergeon Pain Gauge increase % from Ultimate. 1 is 100%, 0.1 is 10%.").defineInRange("armorAbsorbPercent", 0.1d, 0, 1d);
            anomalyBorder = builder.comment("Borders for Anomaly's Ultimate teleportation. Leave at 0 for world's max border.").defineInRange("anomalyBorder", 0, 0, Integer.MAX_VALUE);
            atlasChance = builder.comment("Chance for Atlas to gain Gravity. 1 is 100%, 0.2 is 20%.").defineInRange("atlasChance", 0.2d, 0, 1d);
            catalystLvlLimit = builder.comment("Level limit for stellar Catalyst").defineInRange("catalystLvlLimit", 255, 0, 255);
            catalystDeffence = builder.comment("How many debuffs can Catalyst absorb.").defineInRange("catalystDefence", 20, 0, Integer.MAX_VALUE);
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

            bosses = builder.comment("additional bosses: ").define("bosses","hullbreaker,tremorzilla,nucleeper, luxtructosaurus,atlatitan,forsaken,void_worm,");
            blacklistBosses = builder.comment("blacklist for bosses: ").define("blacklistBosses","void_worm_part,");
            repairObjects = builder.comment("repairObjectsId: ").define("repairObjects","mending,repair,unbreak,restore,heal,ingot");
            repairBlackList = builder.comment("repairBlackListId: ").define("repairBlackList","");
            vortexReduction = builder.comment("Reduction of maximum amount needed for Vortex").defineInRange("vortexReduction", 0, 0, Integer.MAX_VALUE);
            easter = builder.comment("Enables Easter event").define("easter", false);
            easterChance = builder.comment("Additional chance for Easter Egg 10 is 10%").defineInRange("easterChance", 0, 0, Integer.MAX_VALUE);
            fishObjects = builder.comment("fishObjects: ").define("fishObjects","fish,shell,pearl,boot,treasure,sunken,drown,lure,prismarin,water,ocean,coral,shark,whale,manta,rain,abyss,deep,sea,pirate,ship,bottle,wet,river");
            rareItems = builder.comment("Rare Items for fishing by Abyssal Pearl: ").define("rareItems","item.vp.hearty_pearl,item.vp.seashell,abyssal_heart,ichor_bottle,boot");
            fishingBlacklist = builder.comment("Fishing Blacklist for Abyssal Pearl: ").define("fishingBlacklist","item.vp.pearl");
            rareFishingDropChance = builder.comment("Chance of rare drop in current biome from Fishing with Pearl or by defending Silly Seashell.").defineInRange("rareFishingDropChance", 0.001, 0, 1);
            failFlowers = builder.comment("Fails flowers Challenge when they are being placed.").define("failFlowers", false);
            nightmareDamageCap = builder.comment("Damage cap for Nightmare Bosses").defineInRange("nightmareDamageCap", 1000,0,Float.MAX_VALUE);
            unlockHp = builder.comment("For servers with maxed out desync and hp lock to 2048 or if you just have problems with MaxHp.").define("unlockHp", false);
            eventMode = builder.comment("Event mode that disables all teleportations and fly.").define("eventMode", false);
            rareItemChance = builder.comment("Chance for Item to define as rare for Prism challenge. For example carrot has 0.025 chance to drop from zobmie").defineInRange("rareItemChance", 0.025d, 0.0001, 1);
            empoweredChance = builder.comment("Chance to spawn Empowered mob in Hardcore mode").defineInRange("empoweredChance", 0.001, 0, 2100000000);
            debuffBlacklist = builder.comment("Defines blacklist for random potion effects as from Heirloom's Special: ").define("debuffBlacklist","crystallized,");
            cloneBlackList = builder.comment("Defines blacklist for items that cannot be cloned with Celestial Mirror: ").define("cloneBlackList","pouch,bag,backpack,chest,box,pocket,store,storage,satchel,knapsack,cargo,vault,locker,crate,trunk,barrel,bin,safe,drawer,compartment,cache,case,basket,haversack,receptacle,container,ghostly_pickaxe,soul_gem,horse_flute,sack,broom");
            cloneWhiteList = builder.comment("Defines whitelist for items that can be cloned with Celestial Mirror with highest priority. F.e. chestplate to not be blocked by chest in blacklist: ").define("cloneWhiteList","chestplate,box_eggs,box_saplings,item.vp.box,item.vp.mystery_chest");
            mirrorUUIDList = builder.comment("List of existing mirrors UUID: ").define("mirrorUUIDList","");
            dupersList = builder.comment("List of dupers: ").define("dupersList","");
            leaderboardHost = builder.comment("Ip address for leaderboard: ").define("leaderboardHost","");
            leaderboardPort = builder.comment("Ip port for leaderboard: ").define("leaderboardPort","");
            leaderboard = builder.comment("Defines if Leaderboard should be enabled: ").define("leaderboard",false);
            chaosCoreStellarHpRes = builder.comment("Reduce modifier for Chaos Core Stellar ability of max hp lowering from Healing Debt").defineInRange("chaosCoreStellarHpRes", 10d, 0, 2100000000);
            oysterChance = builder.comment("Base chance for spawning Hungry Oyster per 8000 ticks").defineInRange("oysterChance", 0.05d, 0, 1);
            seashellChance = builder.comment("Base chance for spawning Silly Seashell per 8000 ticks").defineInRange("seashellChance", 0.05d, 0, 1);
            lootDrops = builder.comment("Loot tables for Mystery Chest common/rare/mystic/legendary. All chances are independent so 0.7(70% chance) on common cause dropping air in 30% cases.").define("lootDrops",DEFAULT_LOOT);
            strictOptimization = builder.comment("Enables strict optimization that may increase performance but break a lot of mechanics. WIP ").define("strictOptimization",false);
            mysteryChestAdvancementChance = builder.comment("Chance to obtain Mystery Chest from advancement").defineInRange("mysteryChestAdvancementChance", 0.01d, 0, 1);
            mysteryChestAdvancementBoost = builder.comment("Chance increase per each advancement").defineInRange("mysteryChestAdvancementBoost", 0.0005d, 0, 1);
            mysteryChestChallengeChance = builder.comment("Chance to obtain Mystery Chest from challenge").defineInRange("mysteryChestChallengeChance", 0.33d, 0, 1);
        }

        public int getChallengeReduceByNumber(int number) {
            return reduceChallenges.get().get(number-1);
        }

        public static final String DEFAULT_LOOT = "1<item.vp.corrupted_fragment,item.vp.box_saplings,item.irons_spellbooks.blank_rune4,item.irons_spellbooks.rare_ink5,block.minecraft.wither_skeleton_skull,item.minecraft.end_crystal4,item.minecraft.netherite_scrap2,block.aether.enchanted_gravitite8,item.irons_spellbooks.arcane_salvage,item.minecraft.diamond4,item.botania.black_lotus8,item.bloodmagic.blankslate16,item.bloodmagic.reinforcedslate8,item.bloodmagic.infusedslate4,item.eidolon.soul_shard8,item.alexscaves.darkened_apple,block.minecraft.glass32,block.minecraft.quartz_block16,block.minecraft.gold_block4>0.3<item.vp.corrupted_item,item.minecraft.totem_of_undying,item.vp.shard,item.vp.stellar,item.vp.corrupted_fragment16,item.aquamirae.ship_graveyard_echo4,item.enigmaticlegacy.earth_heart,item.enigmaticlegacy.etherium_ore5,item.skilltree.wisdom_scroll4,block.occultism.storage_stabilizer_tier3,item.celestisynth.supernal_netherite_ingot4,item.enigmaticlegacy.angel_blessing,item.enigmaticlegacy.ocean_stone,item.enigmaticlegacy.blazing_core,item.enigmaticlegacy.eye_of_nebula,item.irons_spellbooks.lightning_upgrade_orb,item.irons_spellbooks.ice_upgrade_orb,item.irons_spellbooks.protection_upgrade_orb,item.irons_spellbooks.mana_upgrade_orb,item.irons_spellbooks.ender_upgrade_orb,item.irons_spellbooks.cooldown_upgrade_orb,item.irons_spellbooks.nature_upgrade_orb,item.irons_spellbooks.evocation_upgrade_orb,item.irons_spellbooks.fire_upgrade_orb,item.irons_spellbooks.holy_upgrade_orb,item.irons_spellbooks.blood_upgrade_orb,item.irons_spellbooks.epic_ink5,item.vp.stellar2,item.vp.stellar3,item.botania.terrasteel_ingot,item.botania.blacker_lotus8,item.bloodmagic.demonslate8,item.bloodmagic.etherealslate4,item.bloodmagic.hellforgedparts4,item.eidolon.lesser_soul_gem4,item.twilightforest.charm_of_life_1,item.twilightforest.charm_of_life_2,item.twilightforest.charm_of_keeping_1>0.05<item.vp.hearty_pearl,item.vp.vortex,item.vp.seashell,item.vp.corrupted_item8,item.enigmaticlegacy.cosmic_heart,item.aquamirae.abyssal_amethyst8,item.enigmaticlegacy.astral_fruit,item.enigmaticlegacy.ichor_bottle,item.enigmaticlegacy.void_pearl,item.irons_spellbooks.legendary_ink5,item.vp.stellar10,item.vp.stellar16,block.occultism.storage_stabilizer_tier4,item.vp.chaos_orb,block.minecraft.dragon_egg,item.twilightforest.charm_of_keeping_64,item.vp.box,item.vp.box_eggs2,item.vp.refresher,item.mythicbotany.alfsteel_ingot,block.alexscaves.tremorzilla_egg,item.enigmaticlegacy.soul_crystal,item.enigmaticlegacy.abyssal_heart,item.vp.vip,item.alexsmobs.warped_mixture>0.001<item.vp.chaos_orb64,item.vp.celestial_mirror,item.vp.pinky_pearl20,item.enigmaticlegacy.the_cube>";
    }
}
