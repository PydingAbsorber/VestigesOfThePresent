package com.pyding.vp.mixin;

import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = Enchantment.class)
public class MinEnchLvlMixin {
    /*@Inject(method = "getMinLevel",at = @At("HEAD"),cancellable = true, require = 1)
    private void fuckEnchantmentsTwice(CallbackInfoReturnable<Integer> info){
        //info.setReturnValue(-255); i fucked only myself with this
    }*/
}
