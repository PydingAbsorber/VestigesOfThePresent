package com.pyding.vp.mixin;

import com.pyding.vp.util.VPUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class)
public abstract class VPEntityMixin {


    @Shadow public abstract CompoundTag getPersistentData();

    @Inject(method = "isInBubbleColumn",at = @At("RETURN"),cancellable = true, require = 1)
    protected void isInBubbleColumn(CallbackInfoReturnable<Boolean> cir){
        if(this.getPersistentData().getLong("VPWet") > System.currentTimeMillis()){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isInRain",at = @At("RETURN"),cancellable = true, require = 1)
    protected void isInRain(CallbackInfoReturnable<Boolean> cir){
        if(this.getPersistentData().getLong("VPWet") > System.currentTimeMillis()){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isInWater",at = @At("RETURN"),cancellable = true, require = 1)
    protected void isInWater(CallbackInfoReturnable<Boolean> cir){
        if(this.getPersistentData().getLong("VPWet") > System.currentTimeMillis()){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "teleportTo*", at = @At("HEAD"), cancellable = true, require = 1)
    protected void teleportMixin(CallbackInfoReturnable<?> ci) {
        if (!VPUtil.canTeleport(((Entity) (Object) this))) {
            ci.cancel();
        }
    }

    /*@Inject(method = "moveTo*", at = @At("HEAD"), cancellable = true, require = 1)
    protected void moveMixin(CallbackInfoReturnable<?> ci) {
        if (!VPUtil.canTeleport(((Entity) (Object) this))) {
            ci.cancel();
        }
    }*/

}
