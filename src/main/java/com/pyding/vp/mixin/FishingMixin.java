package com.pyding.vp.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LootTable.class)
public interface FishingMixin {

    @Accessor("pools")
    @Mutable
    List<LootPool> getPools();
}
