package com.pyding.vp.item;

import com.pyding.vp.item.artifacts.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.pyding.vp.VestigesOfPresent.MODID;

public class ModItems {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> TEST = ITEMS.register("test", () -> new Item(new Item.Properties().stacksTo(1)));
    //public static final RegistryObject<Item> ARTIFACT = ITEMS.register("artifact", () -> new Vestige(new Item.Properties().stacksTo(1).tab(null)));
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
    public static final RegistryObject<Item> STELLAR = ITEMS.register("stellar", () -> new StellarFragment(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> REFRESHER = ITEMS.register("refresher", () -> new Refresher(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }




}
