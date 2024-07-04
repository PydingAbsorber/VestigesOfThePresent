package com.pyding.vp.mixin;

import com.pyding.vp.util.VPUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class)
public abstract class MovementMixin {

    @Inject(method = "setDeltaMovement",at = @At("HEAD"),cancellable = true, require = 1)
    private void setDeltaMovement(Vec3 p_20257_, CallbackInfo info){
        if(VPUtil.isNpc(((EntityVzlom)this).getTypeMix())) {
            info.cancel();
        }
    }

    @Inject(method = "moveTo",at = @At("HEAD"),cancellable = true, require = 1)
    private void moveTo(Vec3 p_20257_, CallbackInfo info){
        if(VPUtil.isNpc(((EntityVzlom)this).getTypeMix())) {
            info.cancel();
        }
    }

    @Inject(method = "changeDimension",at = @At("HEAD"), require = 1, cancellable = true)
    private void changeDimension(ServerLevel p_20118_, CallbackInfoReturnable<Entity> info){
        if(VPUtil.isNpc(((EntityVzlom)this).getTypeMix())) {
            info.setReturnValue(null);
        }
    }
}
