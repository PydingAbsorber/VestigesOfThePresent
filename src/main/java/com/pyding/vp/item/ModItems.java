package com.pyding.vp.item;

import com.pyding.vp.item.accessories.*;
import com.pyding.vp.item.vestiges.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;

import static com.pyding.vp.VestigesOfThePresent.MODID;

public class ModItems {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> TEST = ITEMS.register("test", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> ANEMOCULUS = ITEMS.register("anemoculus", () -> new Anemoculus());
    public static final RegistryObject<Item> CROWN = ITEMS.register("crown", () -> new Crown());
    public static final RegistryObject<Item> ATLAS = ITEMS.register("atlas", () -> new Atlas());
    public static final RegistryObject<Item> KILLER = ITEMS.register("killer", () -> new Killer());
    public static final RegistryObject<Item> MASK = ITEMS.register("mask", () -> new MaskOfDemon());
    public static final RegistryObject<Item> DONUT = ITEMS.register("donut", () -> new SweetDonut());
    public static final RegistryObject<Item> MARK = ITEMS.register("mark", () -> new Mark());
    public static final RegistryObject<Item> EARS = ITEMS.register("ears", () -> new CatEars());
    public static final RegistryObject<Item> MIDAS = ITEMS.register("midas", () -> new Midas());
    public static final RegistryObject<Item> ANOMALY = ITEMS.register("anomaly", () -> new Anomaly());
    public static final RegistryObject<Item> ARMOR = ITEMS.register("armor", () -> new Armor());
    public static final RegistryObject<Item> BOOK = ITEMS.register("book", () -> new Book());
    public static final RegistryObject<Item> PRISM = ITEMS.register("prism", () -> new Prism());
    public static final RegistryObject<Item> CHAOS = ITEMS.register("chaos", () -> new Chaos());
    public static final RegistryObject<Item> DEVOURER = ITEMS.register("devourer", () -> new Devourer());
    public static final RegistryObject<Item> FLOWER = ITEMS.register("flower", () -> new Flower());
    public static final RegistryObject<Item> CATALYST = ITEMS.register("catalyst", () -> new Catalyst());
    public static final RegistryObject<Item> BALL = ITEMS.register("ball", () -> new Ball());
    public static final RegistryObject<Item> TRIGON = ITEMS.register("trigon", () -> new Trigon());
    public static final RegistryObject<Item> SOULBLIGHTER = ITEMS.register("soulblighter", () -> new SoulBlighter());
    public static final RegistryObject<Item> RUNE = ITEMS.register("rune", () -> new Rune());
    public static final RegistryObject<Item> LYRA = ITEMS.register("lyra", () -> new Lyra());
    public static final RegistryObject<Item> PEARL = ITEMS.register("pearl", () -> new Pearl());
    public static final RegistryObject<Item> WHIRLPOOL = ITEMS.register("whirlpool", () -> new Whirlpool());
    public static final RegistryObject<Item> ARCHLINX = ITEMS.register("archlinx", () -> new Archlinx());
    public static final RegistryObject<Item> STELLAR = ITEMS.register("stellar", () -> new StellarFragment(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> REFRESHER = ITEMS.register("refresher", () -> new Refresher(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> INFINITE_REFRESHER = ITEMS.register("infinite_refresher", () -> new InfiniteRefresher(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> RING_OF_FALLEN_STAR = ITEMS.register("ring_of_fallen_star", () -> new RingOfFallenStar());
    public static final RegistryObject<Item> EARRING_OF_DEAD_HOPES = ITEMS.register("earring_of_dead_hopes", () -> new EarringOfDeadHopes());
    public static final RegistryObject<Item> NECKLACE_OF_TORTURED_DREAMS = ITEMS.register("necklace_of_tortured_dreams", () -> new NecklaceOfTorturedDreams());
    public static final RegistryObject<Item> BELT_OF_BROKEN_MEMORIES = ITEMS.register("belt_of_broken_memories", () -> new BeltOfBrokenMemories());
    public static final RegistryObject<Item> RING_OF_WIBEST = ITEMS.register("ring_of_wibest", () -> new RingOfWibest());
    public static final RegistryObject<Item> BOX = ITEMS.register("box", () -> new Box());
    public static final RegistryObject<Item> BOX_SAPLINGS = ITEMS.register("box_saplings", () -> new BoxSaplings());
    public static final RegistryObject<Item> BOX_EGGS = ITEMS.register("box_eggs", () -> new BoxEggs());
    public static final RegistryObject<Item> MYSTERY_CHEST = ITEMS.register("mystery_chest", () -> new MysteryChest());
    public static final RegistryObject<Item> VORTEX = ITEMS.register("vortex", () -> new Vortex());
    public static final RegistryObject<Item> EASTER_EGG = ITEMS.register("easter_egg", () -> new EaterEgg());
    public static final RegistryObject<Item> SHARD = ITEMS.register("shard", () -> new Shard());
    public static final RegistryObject<Item> NIGHTMARESHARD = ITEMS.register("nightmareshard", () -> new NightmareShard());
    public static final RegistryObject<Item> HEARTY_PEARL = ITEMS.register("hearty_pearl", () -> new HeartyPearl());
    public static final RegistryObject<Item> PINKY_PEARL = ITEMS.register("pinky_pearl", () -> new PinkyPearl());
    public static final RegistryObject<Item> SEASHELL = ITEMS.register("seashell", () -> new Seashell());
    public static final RegistryObject<Item> BLACKHOLE_ITEM = ITEMS.register("blackhole_item", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CORRUPT_FRAGMENT = ITEMS.register("corrupted_fragment", () -> new CorruptFragment());
    public static final RegistryObject<Item> CORRUPT_ITEM = ITEMS.register("corrupted_item", () -> new CorruptItem());
    public static final RegistryObject<Item> CHAOS_ORB = ITEMS.register("chaos_orb", () -> new ChaosOrb());
    public static final RegistryObject<Item> CELESTIAL_MIRROR = ITEMS.register("celestial_mirror", () -> new CelestialMirror());
    public static final RegistryObject<Item> VIPACTIVATOR = ITEMS.register("vip", () -> new VipActivator(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GUIDE_BOOK = ITEMS.register("guide_book", () -> new GuideBook(new Item.Properties().stacksTo(1)));

    @ObjectHolder(value = MODID + ":seashell", registryName = "item")
    public static final Seashell SEASHELL_HOLDER = null;

    @ObjectHolder(value = MODID + ":mystery_chest", registryName = "item")
    public static final MysteryChest MYSTERY_CHEST_HOLDER = null;

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }




}
