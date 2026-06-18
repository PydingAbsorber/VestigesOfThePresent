package com.pyding.vp.mixin;

import com.pyding.vp.effects.VipEffect;
import com.pyding.vp.util.GradientUtil;
import com.pyding.vp.util.VPUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MobEffect.class)
public abstract class MobEffectMixin {


    @Shadow public abstract String getDescriptionId();

    @Inject(method = "getDisplayName",at = @At("HEAD"),cancellable = true, require = 1)
    private void descMixin(CallbackInfoReturnable<Component> cir){
        if((MobEffect)(Object)this instanceof VipEffect){
            cir.setReturnValue(GradientUtil.goldenGradient(Component.translatable("effect.vp.vip_effect").getString()));
        }
    }

    @Inject(method = "applyInstantenousEffect",at = @At("HEAD"),cancellable = true, require = 1)
    private void applyInstantenousEffectMixin(Entity p_19462_, Entity p_19463_, LivingEntity entity, int p_19465_, double p_19466_, CallbackInfo ci){
        if(entity instanceof Player player){
            VPUtil.getCap(player).addEffect(this.getDescriptionId(), player);
        }
    }
}
