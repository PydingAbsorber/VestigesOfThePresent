package com.pyding.vp.mixin;

import com.pyding.vp.effects.VipEffect;
import com.pyding.vp.item.StellarFragment;
import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.util.GradientUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class)
public abstract class VPItemStackMixin {


    @Inject(method = "getHoverName",at = @At("HEAD"),cancellable = true, require = 1)
    private void descMixin(CallbackInfoReturnable<Component> cir){
        ItemStack stack = (ItemStack)(Object)this;
        if(Vestige.isStellar(stack)){
            cir.setReturnValue(GradientUtil.stellarGradient(stack.getItem().getName(stack).getString().substring(2)));
        }
        if(stack.getItem() instanceof StellarFragment)
            cir.setReturnValue(GradientUtil.stellarGradient(stack.getItem().getName(stack).getString()));
    }
}
