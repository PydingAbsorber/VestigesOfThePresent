package com.pyding.vp.item;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.item.accessories.*;
import com.pyding.vp.item.vestiges.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, VestigesOfThePresent.MODID);

    public static final DeferredHolder<Item, TestItem> LOGO = ITEMS.register("logo", () -> new TestItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Anemoculus> ANEMOCULUS = ITEMS.register("anemoculus", Anemoculus::new);
    public static final DeferredHolder<Item, Crown> CROWN = ITEMS.register("crown", Crown::new);
    public static final DeferredHolder<Item, Atlas> ATLAS = ITEMS.register("atlas", Atlas::new);
    public static final DeferredHolder<Item, Killer> KILLER = ITEMS.register("killer", Killer::new);
    public static final DeferredHolder<Item, MaskOfDemon> MASK = ITEMS.register("mask", MaskOfDemon::new);
    public static final DeferredHolder<Item, SweetDonut> DONUT = ITEMS.register("donut", SweetDonut::new);
    public static final DeferredHolder<Item, Mark> MARK = ITEMS.register("mark", Mark::new);
    public static final DeferredHolder<Item, CatEars> EARS = ITEMS.register("ears", CatEars::new);
    public static final DeferredHolder<Item, Midas> MIDAS = ITEMS.register("midas", Midas::new);
    public static final DeferredHolder<Item, Anomaly> ANOMALY = ITEMS.register("anomaly", Anomaly::new);
    public static final DeferredHolder<Item, Armor> ARMOR = ITEMS.register("armor", Armor::new);
    public static final DeferredHolder<Item, Book> BOOK = ITEMS.register("book", Book::new);
    public static final DeferredHolder<Item, Prism> PRISM = ITEMS.register("prism", Prism::new);
    public static final DeferredHolder<Item, Chaos> CHAOS = ITEMS.register("chaos", Chaos::new);
    public static final DeferredHolder<Item, Devourer> DEVOURER = ITEMS.register("devourer", Devourer::new);
    public static final DeferredHolder<Item, Flower> FLOWER = ITEMS.register("flower", Flower::new);
    public static final DeferredHolder<Item, Catalyst> CATALYST = ITEMS.register("catalyst", Catalyst::new);
    public static final DeferredHolder<Item, Ball> BALL = ITEMS.register("ball", Ball::new);
    public static final DeferredHolder<Item, Trigon> TRIGON = ITEMS.register("trigon", Trigon::new);
    public static final DeferredHolder<Item, SoulBlighter> SOULBLIGHTER = ITEMS.register("soulblighter", SoulBlighter::new);
    public static final DeferredHolder<Item, Rune> RUNE = ITEMS.register("rune", Rune::new);
    public static final DeferredHolder<Item, Lyra> LYRA = ITEMS.register("lyra", Lyra::new);
    public static final DeferredHolder<Item, Pearl> PEARL = ITEMS.register("pearl", Pearl::new);
    public static final DeferredHolder<Item, Whirlpool> WHIRLPOOL = ITEMS.register("whirlpool", Whirlpool::new);
    public static final DeferredHolder<Item, Archlinx> ARCHLINX = ITEMS.register("archlinx", Archlinx::new);
    public static final DeferredHolder<Item, Treasure> TREASURE = ITEMS.register("treasure", Treasure::new);
    public static final DeferredHolder<Item, NightmareDevourer> NIGHTMARE_DEVOURER = ITEMS.register("nightmare_devourer", NightmareDevourer::new);
    public static final DeferredHolder<Item, StellarFragment> STELLAR = ITEMS.register("stellar", () -> new StellarFragment(new Item.Properties().stacksTo(64)));
    public static final DeferredHolder<Item, Refresher> REFRESHER = ITEMS.register("refresher", () -> new Refresher(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, InfiniteRefresher> INFINITE_REFRESHER = ITEMS.register("infinite_refresher", () -> new InfiniteRefresher(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, RingOfFallenStar> RING_OF_FALLEN_STAR = ITEMS.register("ring_of_fallen_star", RingOfFallenStar::new);
    public static final DeferredHolder<Item, EarringOfDeadHopes> EARRING_OF_DEAD_HOPES = ITEMS.register("earring_of_dead_hopes", EarringOfDeadHopes::new);
    public static final DeferredHolder<Item, NecklaceOfTorturedDreams> NECKLACE_OF_TORTURED_DREAMS = ITEMS.register("necklace_of_tortured_dreams", NecklaceOfTorturedDreams::new);
    public static final DeferredHolder<Item, BeltOfBrokenMemories> BELT_OF_BROKEN_MEMORIES = ITEMS.register("belt_of_broken_memories", BeltOfBrokenMemories::new);
    public static final DeferredHolder<Item, RingOfWibest> RING_OF_WIBEST = ITEMS.register("ring_of_wibest", () -> new RingOfWibest());
    public static final DeferredHolder<Item, Box> BOX = ITEMS.register("box", () -> new Box());
    public static final DeferredHolder<Item, BoxSaplings> BOX_SAPLINGS = ITEMS.register("box_saplings", () -> new BoxSaplings());
    public static final DeferredHolder<Item, BoxEggs> BOX_EGGS = ITEMS.register("box_eggs", () -> new BoxEggs());
    public static final DeferredHolder<Item, MysteryChest> MYSTERY_CHEST = ITEMS.register("mystery_chest", () -> new MysteryChest());
    public static final DeferredHolder<Item, Vortex> VORTEX = ITEMS.register("vortex", () -> new Vortex());
    public static final DeferredHolder<Item, EaterEgg> EASTER_EGG = ITEMS.register("easter_egg", () -> new EaterEgg());
    public static final DeferredHolder<Item, Shard> SHARD = ITEMS.register("shard", () -> new Shard());
    public static final DeferredHolder<Item, NightmareShard> NIGHTMARESHARD = ITEMS.register("nightmareshard", () -> new NightmareShard());
    public static final DeferredHolder<Item, HeartyPearl> HEARTY_PEARL = ITEMS.register("hearty_pearl", () -> new HeartyPearl());
    public static final DeferredHolder<Item, PinkyPearl> PINKY_PEARL = ITEMS.register("pinky_pearl", () -> new PinkyPearl());
    public static final DeferredHolder<Item, Seashell> SEASHELL = ITEMS.register("seashell", () -> new Seashell());
    public static final DeferredHolder<Item, Item> BLACKHOLE_ITEM = ITEMS.register("blackhole_item", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, CorruptFragment> CORRUPT_FRAGMENT = ITEMS.register("corrupted_fragment", CorruptFragment::new);
    public static final DeferredHolder<Item, CorruptItem> CORRUPT_ITEM = ITEMS.register("corrupted_item", CorruptItem::new);
    public static final DeferredHolder<Item, ChaosOrb> CHAOS_ORB = ITEMS.register("chaos_orb", ChaosOrb::new);
    public static final DeferredHolder<Item, CelestialMirror> CELESTIAL_MIRROR = ITEMS.register("celestial_mirror", CelestialMirror::new);
    public static final DeferredHolder<Item, VipActivator> VIPACTIVATOR = ITEMS.register("vip", () -> new VipActivator(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, GuideBook> GUIDE_BOOK = ITEMS.register("guide_book", () -> new GuideBook(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, NightmareBook> NIGHTMARE_BOOK = ITEMS.register("nightmare_book", () -> new NightmareBook(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, MineralCluster> MINERAL_CLUSTER = ITEMS.register("mineral_cluster", () -> new MineralCluster());
    public static final DeferredHolder<Item, EventHorizon> EVENT_HORIZON = ITEMS.register("event_horizon", () -> new EventHorizon(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
