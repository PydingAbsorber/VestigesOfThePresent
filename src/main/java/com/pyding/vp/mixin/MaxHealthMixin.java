package com.pyding.vp.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RangedAttribute.class)
public class MaxHealthMixin {
    @Mutable
    @Shadow @Final private double maxValue;

    @Inject(method = "getMaxValue",at = @At("RETURN"), require = 1)
    public void getMaxValue(CallbackInfoReturnable<Double> cir){
        this.maxValue = 99999D;
        cir.setReturnValue(99999D);
    }
}
