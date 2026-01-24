package com.pyding.vp.mixin;

import com.pyding.vp.effects.VipEffect;
import com.pyding.vp.util.GradientUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MobEffect.class)
public abstract class MobEffectMixin {


    @Inject(method = "getDisplayName",at = @At("HEAD"),cancellable = true, require = 1)
    private void descMixin(CallbackInfoReturnable<Component> cir){
        if((MobEffect)(Object)this instanceof VipEffect){
            cir.setReturnValue(GradientUtil.goldenGradient(Component.translatable("effect.vp.vip_effect").getString()));
        }
    }
}
