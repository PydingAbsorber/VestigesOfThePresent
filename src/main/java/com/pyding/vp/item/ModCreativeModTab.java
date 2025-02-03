package com.pyding.vp.item;

import com.pyding.vp.VestigesOfThePresent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTab {
    public static final CreativeModeTab tab = new CreativeModeTab("vptab") {
        @Override
        public ItemStack makeIcon(){
            return new ItemStack(ModItems.TEST.get());
        }
    };
    /*public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, VestigesOfThePresent.MODID);

    public static void register(IEventBus eventBus){
        CREATIVE_MOD_TABS.register(eventBus);
    }*/

    /*public static RegistryObject<CreativeModeTab> VP_TAB = CREATIVE_MOD_TABS.register("vp_tab",()->
        CreativeModeTab.builder().icon(() ->  new ItemStack(ModItems.TEST.get())).title(Component.translatable("itemGroup.vptab")).build());*/

}
