package com.pyding.vp.mixin;

import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootItemRandomChanceWithLootingCondition.class)
public interface LootItemEnchantMixin {

    @Accessor("percent")
    @Mutable
    float getChance();
}
