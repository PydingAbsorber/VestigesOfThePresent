package com.pyding.vp.item;

import com.pyding.vp.VestigesOfPresent;
import com.pyding.vp.item.artifacts.Artifact;
import com.pyding.vp.item.artifacts.MaskOfDemon;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VestigesOfPresent.MODID);

    public static final RegistryObject<Item> TEST = ITEMS.register("test", () -> new Item(new Item.Properties().stacksTo(1).tab(null)));
    //public static final RegistryObject<Item> ARTIFACT = ITEMS.register("artifact", () -> new Artifact(new Item.Properties().stacksTo(1).tab(null)));
    public static final RegistryObject<Item> MASK = ITEMS.register("mask", () -> new MaskOfDemon());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
