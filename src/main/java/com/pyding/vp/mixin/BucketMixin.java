package com.pyding.vp.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.SmithingTemplateItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobBucketItem.class)
public interface BucketMixin {

    @Accessor("entityTypeSupplier")
    @Mutable
    java.util.function.Supplier<? extends EntityType<?>> getFishSup();
}
