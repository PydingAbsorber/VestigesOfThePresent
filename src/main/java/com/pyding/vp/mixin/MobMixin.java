package com.pyding.vp.mixin;

import com.pyding.vp.item.ModItems;
import com.pyding.vp.util.VPUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Mob.class)
public abstract class MobMixin {

    @Inject(method = "setTarget",at = @At("HEAD"),cancellable = true, require = 1)
    private void setTargetMixin(LivingEntity livingEntity, CallbackInfo ci){
        if(livingEntity instanceof Player player && player.getPersistentData().hasUUID("VPSlave") && player.getPersistentData().getUUID("VPSlave") != player.getUUID() && VPUtil.hasVestige(ModItems.SOULBLIGHTER.get(),player)){
            ci.cancel();
        }
    }
}
