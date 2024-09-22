package com.pyding.vp.mixin;

import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootItemRandomChanceCondition.class)
public interface LootRandomItemMixin {

    @Accessor("probability")
    @Mutable
    float getChance();
}
