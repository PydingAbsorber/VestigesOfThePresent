package com.pyding.vp.mixin;

import com.pyding.vp.item.ModItems;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Mob.class)
public abstract class MobMixin {

    @Inject(method = "setTarget",at = @At("HEAD"),cancellable = true, require = 1)
    private void setTargetMixin(LivingEntity livingEntity, CallbackInfo ci){
        if(livingEntity instanceof Player player && player.getPersistentData().hasUUID("VPSlave") && player.getPersistentData().getUUID("VPSlave") != player.getUUID() && VPUtil.hasVestige(ModItems.SOULBLIGHTER.get(),player)){
            ci.cancel();
        }
    }

    @Inject(method = "getTarget",at = @At("HEAD"),cancellable = true, require = 1)
    private void getTargetMixin(CallbackInfoReturnable<LivingEntity> cir){
        Mob mob = ((Mob)(Object)this);
        if(VPUtil.isNightmareBoss(mob) && !(cir.getReturnValue() instanceof Player)){
            Player player = mob.getCommandSenderWorld().getNearestPlayer(mob,45);
            if(player != null)
                cir.setReturnValue(player);
        }
        else if(cir.getReturnValue() instanceof Player player && mob.getPersistentData().hasUUID("VPPlayer") && player.getUUID().compareTo(mob.getPersistentData().getUUID("VPPlayer")) == 0){
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "getEquipmentDropChance",at = @At("HEAD"),cancellable = true, require = 1)
    private void getEquipmentDropChanceMixin(EquipmentSlot p_21520_, CallbackInfoReturnable<Float> cir){
        if(ConfigHandler.COMMON_SPEC.isLoaded() && ConfigHandler.COMMON.cruelMode.get())
            cir.setReturnValue(0f);
    }
}
