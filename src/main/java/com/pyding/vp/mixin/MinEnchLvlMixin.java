package com.pyding.vp.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Enchantment.class)
public class MinEnchLvlMixin {
    @Inject(method = "getMinLevel",at = @At("HEAD"),cancellable = true, require = 1)
    private void fuckEnchantmentsTwice(CallbackInfoReturnable<Integer> info){
        //info.setReturnValue(-255);
    }
}
