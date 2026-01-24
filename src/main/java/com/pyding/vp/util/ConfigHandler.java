package com.pyding.vp.util;

import com.pyding.vp.capability.VestigeCap;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static boolean isLoaded() {
        if (SPEC == null) return false;
        if (!SPEC.isLoaded()) return false;
        try {
            return bosses != null && bosses.next() != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static final ModConfigSpec.BooleanValue lore = BUILDER
            .comment("Set false to disable chat messages with Lore")
            .define("lore", true);

    public static final ModConfigSpec.LongValue vestigesCooldown = BUILDER
            .comment("Cooldown in milliseconds for any Vestiges abilities. 100 is 0.1 sec.")
            .defineInRange("vestigesCooldown", 50L, 0L, Long.MAX_VALUE);

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

    // --- Nightmare ---
    public static final ModConfigSpec.DoubleValue nightmareBoxChance = BUILDER
            .comment("Nightmare Bosses boxes chance 0.5 is 50%")
            .defineInRange("nightmareAllBoxChance", 1d, 0d, 1d);

    public static final ModConfigSpec.DoubleValue nightmareRefresherChance = BUILDER
            .comment("Nightmare Bosses Refresher chance 0.1 is 10%")
            .defineInRange("nightmareRefresherChance", 0.1d, 0d, 1d);

    public static final ModConfigSpec.IntValue nightmareFrags = BUILDER.defineInRange("nightmareFrags", 26, 1, 2100000000);
    public static final ModConfigSpec.IntValue nightmareFragsMin = BUILDER.defineInRange("nightmareFragsMin", 13, 1, 2100000000);
    public static final ModConfigSpec.IntValue nightmareLoot = BUILDER.defineInRange("nightmareLoot", 20, 1, 2100000000);
    public static final ModConfigSpec.IntValue nightmareLootMin = BUILDER.defineInRange("nightmareLootMin", 10, 1, 2100000000);
    public static final ModConfigSpec.IntValue nightmareBoxes = BUILDER.defineInRange("nightmareBoxes", 8, 1, 2100000000);
    public static final ModConfigSpec.IntValue nightmareBoxesMin = BUILDER.defineInRange("nightmareBoxesMin", 4, 1, 2100000000);
    public static final ModConfigSpec.DoubleValue refresherChance = BUILDER.defineInRange("refresherChance", 0.25d, 0d, 1d);

    // --- Параметры предметов и механик ---
    public static final ModConfigSpec.IntValue cooldown = BUILDER.defineInRange("cooldown", 8, 0, 2100000000);
    public static final ModConfigSpec.IntValue stellarChanceIncrease = BUILDER.defineInRange("stellarChanceIncrease", 10, 0, 100);
    public static final ModConfigSpec.DoubleValue armorAbsorbPercent = BUILDER.defineInRange("armorAbsorbPercent", 0.5d, 0d, (double) Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue anomalyBorder = BUILDER.defineInRange("anomalyBorder", 0, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue atlasChance = BUILDER.defineInRange("atlasChance", 0.2d, 0d, 1d);
    public static final ModConfigSpec.IntValue catalystLvlLimit = BUILDER.defineInRange("catalystLvlLimit", 255, 0, 255);
    public static final ModConfigSpec.IntValue catalystDeffence = BUILDER.defineInRange("catalystDefence", 20, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue catSpeed = BUILDER.defineInRange("catSpeed", 1d, 0d, 10d);
    public static final ModConfigSpec.IntValue catEvadeCap = BUILDER.defineInRange("catEvadeCap", 69, 0, 100);
    public static final ModConfigSpec.IntValue chaosDamageCap = BUILDER.defineInRange("chaosDamageCap", Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue chaosChance = BUILDER.defineInRange("chaosChance", 0.1d, 0d, 1d);
    public static final ModConfigSpec.IntValue crownShield = BUILDER.defineInRange("crownShield", 50, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue devourerChance = BUILDER.defineInRange("devourerChance", 0.01d, 0d, 100d);
    public static final ModConfigSpec.DoubleValue flowerShield = BUILDER.defineInRange("flowerShield", 1d, 0d, (double) Integer.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue killerRes = BUILDER.defineInRange("killerRes", 70d, 0d, (double) Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue markMaximum = BUILDER.defineInRange("markMaximum", 10, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue markHealDebt = BUILDER.defineInRange("markHealDebt", 2000, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue maskRotAmount = BUILDER.defineInRange("maskRotAmount", 10, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue midasChance = BUILDER.defineInRange("midasChance", 0.0001d, 0d, 1d);
    public static final ModConfigSpec.DoubleValue prismChance = BUILDER.defineInRange("prismChance", 1d, 0d, (double) Integer.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue soulBlighterChance = BUILDER.defineInRange("soulBlighterChance", 1d, 0d, (double) Integer.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue soulBlighterHeal = BUILDER.defineInRange("soulBlighterHeal", 1d, 0d, (double) Integer.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue trigonHeal = BUILDER.defineInRange("trigonHeal", 1d, 0d, (double) Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue donutMaxSaturation = BUILDER.defineInRange("donutMaxSaturation", 400, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.IntValue donutHealBonus = BUILDER.defineInRange("donutHealBonus", 40, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue ballShield = BUILDER.defineInRange("ballShield", 0.1d, 0.01d, 1d);
    public static final ModConfigSpec.DoubleValue ballDebuff = BUILDER.defineInRange("ballDebuff", 75d, 0d, (double) Integer.MAX_VALUE);
    public static final ModConfigSpec.DoubleValue ballOverShield = BUILDER.defineInRange("ballOverShield", 0.2d, 0.01d, 1d);
    public static final ModConfigSpec.IntValue chaosCharges = BUILDER.defineInRange("chaosCharges", 20, 1, Integer.MAX_VALUE);
    public static final ModConfigSpec.LongValue devourerCdTime = BUILDER.defineInRange("devourerCdTime", 200L, 1L, Long.MAX_VALUE);
    public static final ModConfigSpec.IntValue markBonus = BUILDER.defineInRange("markBonus", 10, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue devourer = BUILDER.defineInRange("devourer", 30, 0, 2100000000);
    public static final ModConfigSpec.IntValue blackhole = BUILDER.defineInRange("blackhole", 4, 0, 2100000000);
    public static final ModConfigSpec.BooleanValue anomaly = BUILDER.define("anomaly", false);
    public static final ModConfigSpec.IntValue chaostime = BUILDER.defineInRange("chaostime", 15, 1, 2100000000);
    public static final ModConfigSpec.IntValue eatingMinutes = BUILDER.defineInRange("eatingMinutes", 1, 1, 2100000000);

    // --- Списки и ID ---
    public static final ModConfigSpec.ConfigValue<String> bosses = BUILDER.define("bosses", "hullbreaker,tremorzilla,nucleeper,luxtructosaurus,atlatitan,forsaken,void_worm,");
    public static final ModConfigSpec.ConfigValue<String> blacklistBosses = BUILDER.define("blacklistBosses", "void_worm_part,");
    public static final ModConfigSpec.ConfigValue<String> repairObjects = BUILDER.define("repairObjects", "mending,repair,unbreak,restore,heal,ingot");
    public static final ModConfigSpec.ConfigValue<String> repairBlackList = BUILDER.define("repairBlackList", "");
    public static final ModConfigSpec.IntValue vortexReduction = BUILDER.defineInRange("vortexReduction", 0, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.BooleanValue easter = BUILDER.define("easter", false);
    public static final ModConfigSpec.IntValue easterChance = BUILDER.defineInRange("easterChance", 0, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.ConfigValue<String> fishObjects = BUILDER.define("fishObjects", "fish,shell,pearl,boot,treasure,sunken,drown,lure,prismarin,water,ocean,coral,shark,whale,manta,rain,abyss,deep,sea,pirate,ship,bottle,wet,river");
    public static final ModConfigSpec.ConfigValue<String> rareItems = BUILDER.define("rareItems", "item.vp.hearty_pearl,item.vp.seashell,abyssal_heart,ichor_bottle,boot");
    public static final ModConfigSpec.ConfigValue<String> fishingBlacklist = BUILDER.define("fishingBlacklist", "bottle_of_forfeiture");
    public static final ModConfigSpec.DoubleValue rareFishingDropChance = BUILDER.defineInRange("rareFishingDropChance", 0.001d, 0d, 1d);
    public static final ModConfigSpec.BooleanValue failFlowers = BUILDER.define("failFlowers", false);
    public static final ModConfigSpec.DoubleValue nightmareDpsCap = BUILDER.defineInRange("nightmareDpsCap", 0.01d, 0d, (double) Float.MAX_VALUE);
    public static final ModConfigSpec.BooleanValue unlockHp = BUILDER.define("unlockHp", false);
    public static final ModConfigSpec.BooleanValue eventMode = BUILDER.define("eventMode", false);
    public static final ModConfigSpec.DoubleValue rareItemChance = BUILDER.defineInRange("rareItemChance", 0.025d, 0.0001d, 1d);
    public static final ModConfigSpec.ConfigValue<String> debuffBlacklist = BUILDER.define("debuffBlacklist", "crystallized,");
    public static final ModConfigSpec.ConfigValue<String> cloneBlackList = BUILDER.define("cloneBlackList2", "bundle,pouch,bag,backpack,chest,box,pocket,store,storage,satchel,knapsack,cargo,vault,locker,crate,trunk,barrel,bin,safe,drawer,compartment,cache,case,basket,haversack,receptacle,container,ghostly_pickaxe,soul_gem,horse_flute,sack,broom,jar,bottle");
    public static final ModConfigSpec.ConfigValue<String> cloneWhiteList = BUILDER.define("cloneWhiteList", "chestplate,box_eggs,box_saplings,item.vp.box,item.vp.mystery_chest");
    public static final ModConfigSpec.ConfigValue<String> mirrorUUIDList = BUILDER.define("mirrorUUIDList", "");
    public static final ModConfigSpec.ConfigValue<String> dupersList = BUILDER.define("dupersList", "");
    public static final ModConfigSpec.ConfigValue<String> leaderboardHost = BUILDER.define("leaderboardHost", "");
    public static final ModConfigSpec.ConfigValue<String> leaderboardPort = BUILDER.define("leaderboardPort", "");
    public static final ModConfigSpec.BooleanValue leaderboard = BUILDER.define("leaderboard", false);
    public static final ModConfigSpec.DoubleValue chaosCoreStellarHpRes = BUILDER.defineInRange("chaosCoreStellarHpRes", 10d, 0d, 2100000000d);
    public static final ModConfigSpec.DoubleValue oysterChance = BUILDER.defineInRange("oysterChance", 0.15d, 0d, 1d);
    public static final ModConfigSpec.DoubleValue seashellChance = BUILDER.defineInRange("seashellChance", 0.15d, 0d, 1d);

    public static final ModConfigSpec.ConfigValue<String> lootDrops = BUILDER.define("lootDrops", getDefaultLoot());

    public static final ModConfigSpec.BooleanValue strictOptimization = BUILDER.define("strictOptimization", false);
    public static final ModConfigSpec.DoubleValue mysteryChestAdvancementChance = BUILDER.defineInRange("mysteryChestAdvancementChance", 0.1d, 0d, 1d);
    public static final ModConfigSpec.DoubleValue mysteryChestAdvancementBoost = BUILDER.defineInRange("mysteryChestAdvancementBoost", 0.0002d, 0d, 1d);
    public static final ModConfigSpec.DoubleValue mysteryChestChallengeChance = BUILDER.defineInRange("mysteryChestChallengeChance", 0.33d, 0d, 1d);
    public static final ModConfigSpec.ConfigValue<String> mineralCluster = BUILDER.define("mineralClusterList", "_gem_,mineral,diamond,emerald,jadeite,quartz,feldspar,mica,fluorite,halite,gypsum,_talc,graphite,pyrite,_galena,hematite,magnetite,bauxite,corundum,sapphire,ruby,topaz,amethyst,citrine,agate,jasper,opal,garnet,zircon,olivine,tourmaline,beryl,aquamarine,biotite,muscovite,orthoclase,plagioclase,amphibole,pyroxene,apatite,barite,sulfur,malachite,azurite,bornite,chalcopyrite,sphalerite,cassiterite,rutile,ilmenite,chromite,kaolinite,serpentine,epidote,staurolite,kyanite,andalusite,sillimanite");
    public static final ModConfigSpec.ConfigValue<String> mineralClusterBlacklist = BUILDER.define("mineralClusterBlacklist", "");
    public static final ModConfigSpec.DoubleValue anomalyPlayerTeleportChance = BUILDER.defineInRange("anomalyPlayerTeleportChance", 0.05d, 0d, 1d);
    public static final ModConfigSpec.IntValue clearEntities = BUILDER.defineInRange("clearEntities", 0, 0, Integer.MAX_VALUE);
    public static final ModConfigSpec.ConfigValue<String> clearEntitiesBlacklist = BUILDER.define("clearEntitiesBlacklist", "spectrum,twilightforest");
    public static final ModConfigSpec.ConfigValue<String> catFood = BUILDER.define("catFood", "fish,salmon,tuna,cod,trout,herring,mackerel,sardine,anchovy,haddock,halibut,sole,flounder,swordfish,marlin,perch,pike,carp,catfish,eel,sturgeon,tilapia,seabass,snapper,grouper,dorado,barracuda,mullet,monkfish,turbot,zander,bream,roach,tench,guppy,goldfish,angelfish,tetra,barb,molly,platy,danio,betta,discus,oscar,cichlid,clownfish,surgeonfish,goby,blenny,pufferfish,lionfish,scorpionfish,stingray,shark,skate,lungfish,coelacanth,arapaima,piranha,arowana,burbot,grayling,char,smelt,capelin,pollock,whiting,hake,ling,bluefish,amberjack,wahoo,mahimahi,pompano,drum,croaker,sheepshead,tarpon,bonefish,permit,barramundi,loach,gudgeon,ide,asp,chub,rudd,dace,minnow,stickleback,sculpin,lumpsucker,gar,bowfin,paddlefish,hagfish,lamprey,anchoveta,sprat");


    public static final ModConfigSpec SPEC = BUILDER.build();

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

    public static final String getDefaultLoot(){
        return "1<item.vp.corrupted_fragment,item.vp.box_saplings,item.irons_spellbooks.blank_rune4,item.irons_spellbooks.rare_ink5,block.minecraft.wither_skeleton_skull,item.minecraft.end_crystal4,item.minecraft.netherite_scrap2,block.aether.enchanted_gravitite8,item.irons_spellbooks.arcane_salvage,item.minecraft.diamond4,item.botania.black_lotus8,item.bloodmagic.blankslate16,item.bloodmagic.reinforcedslate8,item.bloodmagic.infusedslate4,item.eidolon.soul_shard8,item.alexscaves.darkened_apple,block.minecraft.glass32,block.minecraft.quartz_block16,block.minecraft.gold_block4>0.3<item.vp.corrupted_item,item.minecraft.totem_of_undying,item.vp.shard,item.vp.stellar,item.vp.corrupted_fragment16,item.aquamirae.ship_graveyard_echo4,item.enigmaticlegacy.earth_heart,item.enigmaticlegacy.etherium_ore5,item.skilltree.wisdom_scroll4,block.occultism.storage_stabilizer_tier3,item.celestisynth.supernal_netherite_ingot4,item.enigmaticlegacy.angel_blessing,item.enigmaticlegacy.ocean_stone,item.enigmaticlegacy.blazing_core,item.enigmaticlegacy.eye_of_nebula,item.irons_spellbooks.lightning_upgrade_orb,item.irons_spellbooks.ice_upgrade_orb,item.irons_spellbooks.protection_upgrade_orb,item.irons_spellbooks.mana_upgrade_orb,item.irons_spellbooks.ender_upgrade_orb,item.irons_spellbooks.cooldown_upgrade_orb,item.irons_spellbooks.nature_upgrade_orb,item.irons_spellbooks.evocation_upgrade_orb,item.irons_spellbooks.fire_upgrade_orb,item.irons_spellbooks.holy_upgrade_orb,item.irons_spellbooks.blood_upgrade_orb,item.irons_spellbooks.epic_ink5,item.vp.stellar2,item.vp.stellar3,item.botania.terrasteel_ingot,item.botania.blacker_lotus8,item.bloodmagic.demonslate8,item.bloodmagic.etherealslate4,item.bloodmagic.hellforgedparts4,item.eidolon.lesser_soul_gem4,item.twilightforest.charm_of_life_1,item.twilightforest.charm_of_life_2,item.twilightforest.charm_of_keeping_1>0.05<item.vp.hearty_pearl,item.vp.vortex,item.vp.seashell,item.vp.corrupted_item8,item.enigmaticlegacy.cosmic_heart,item.aquamirae.abyssal_amethyst8,item.enigmaticlegacy.astral_fruit,item.enigmaticlegacy.ichor_bottle,item.enigmaticlegacy.void_pearl,item.irons_spellbooks.legendary_ink5,item.vp.stellar10,item.vp.stellar16,block.occultism.storage_stabilizer_tier4,item.vp.chaos_orb,block.minecraft.dragon_egg,item.twilightforest.charm_of_keeping_64,item.vp.box,item.vp.box_eggs2,item.vp.refresher,item.mythicbotany.alfsteel_ingot,block.alexscaves.tremorzilla_egg,item.enigmaticlegacy.soul_crystal,item.enigmaticlegacy.abyssal_heart,item.vp.vip,item.alexsmobs.warped_mixture>0.001<item.vp.chaos_orb64,item.vp.celestial_mirror,item.vp.pinky_pearl20,item.enigmaticlegacy.the_cube>";
    }
}
