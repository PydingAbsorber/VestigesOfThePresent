package com.pyding.vp.mixin;

import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FishingHook.class)
public interface FishingHookMixin {

    @Accessor("timeUntilLured")
    int getTimeLured();

    @Accessor("timeUntilLured")
    void setTimeLured(int number);

    @Accessor("nibble")
    int getNibble();

    @Accessor("nibble")
    void setNibble(int number);
}
