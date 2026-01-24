package com.pyding.vp.mixin;

import com.pyding.vp.util.VPUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
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
    private void changeDimension(DimensionTransition transition, CallbackInfoReturnable<Entity> cir){
        if(VPUtil.isNpc(((EntityVzlom)this).getTypeMix())) {
            cir.setReturnValue(null);
        }
    }
}
