package com.pyding.vp.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModTab {
    public static final CreativeModeTab tab = new CreativeModeTab("vptab") {
        @Override
        public ItemStack makeIcon(){
            return new ItemStack(ModItems.TEST.get());
        }
    };
}
