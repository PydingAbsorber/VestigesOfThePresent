package com.pyding.vp.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ResultContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemCombinerMenu.class)
public interface SmithingVzlomMixing {

    @Accessor("inputSlots")
    @Mutable
    Container getInputSlots();

    @Accessor("resultSlots")
    @Mutable
    ResultContainer getResultSlots();
}
