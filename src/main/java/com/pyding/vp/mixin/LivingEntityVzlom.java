package com.pyding.vp.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityVzlom {

    @Accessor("DATA_HEALTH_ID")
    @Mutable
    EntityDataAccessor<Float> getDataHealth();

    @Accessor("dead")
    public void setDead(boolean dead);

    @Accessor("dead")
    boolean isDead();

    @Invoker("dropAllDeathLoot")
    void invokeDropAllDeathLoot(DamageSource p_21192_);

    @Invoker("getArmorSlots")
    Iterable<ItemStack> getArmorSlots();
}
