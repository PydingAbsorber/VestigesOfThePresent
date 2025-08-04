package com.pyding.vp.item;

import com.pyding.vp.VestigesOfThePresent;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, VestigesOfThePresent.MODID);

    public static void register(IEventBus eventBus){
        CREATIVE_MOD_TABS.register(eventBus);
    }

    public static RegistryObject<CreativeModeTab> VP_TAB = CREATIVE_MOD_TABS.register("vp_tab",()->
        CreativeModeTab.builder()
                .icon(() ->  new ItemStack(ModItems.LOGO.get()))
                .title(Component.translatable("itemGroup.vptab"))
                .withBackgroundLocation(new ResourceLocation("vp", "textures/gui/background.png"))
                .withTabsImage(new ResourceLocation("vp", "textures/gui/logo.png"))
                .build());

}
