package com.pyding.vp.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.MobBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobBucketItem.class)
public interface BucketVzlom {

    @Accessor("entityTypeSupplier")
    @Mutable
    java.util.function.Supplier<? extends EntityType<?>> getFishSup();
}
