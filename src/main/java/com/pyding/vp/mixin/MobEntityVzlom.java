package com.pyding.vp.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

import javax.annotation.Nullable;

@Mixin(Mob.class)
public interface MobEntityVzlom {
    @Accessor("target")
    public void setTarget(LivingEntity livingEntity);
}
