package com.pyding.vp.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EnchantmentHelper.class)
public class EnchantmentsMixin {
    @Inject(method = "getEnchantmentLevel*",at = @At("RETURN"),cancellable = true, require = 1)
    private static void fuckEnchantments(CompoundTag p_182439_, CallbackInfoReturnable<Integer> info){
        info.setReturnValue(p_182439_.getInt("lvl"));
    }
}
