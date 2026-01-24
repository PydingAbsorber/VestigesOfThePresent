package com.pyding.vp.mixin;

import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public interface ILootPool {
    List<LootPoolEntryContainer> vp$getEntries();
    List<LootItemCondition> vp$getConditions();
}
