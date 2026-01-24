package com.pyding.vp.mixin;

import net.minecraft.world.level.storage.loot.LootPool;

import java.util.List;

public interface ILootTable {
    List<LootPool> vp$getPools();
}
