package com.pyding.vp.mixin;

import com.pyding.vp.item.ModItems;
import com.pyding.vp.util.GradientUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CreativeModeTab.class)
public abstract class VPCreativeTabMixin {


    @Shadow public abstract ItemStack getIconItem();

    @Inject(method = "getDisplayName",at = @At("HEAD"),cancellable = true, require = 1)
    private void descMixin(CallbackInfoReturnable<Component> cir){
        if(ModItems.LOGO.isPresent() && getIconItem().is(ModItems.LOGO.get())) {
            cir.setReturnValue(GradientUtil.customGradient(Component.translatable("itemGroup.vptab").getString().substring(2),GradientUtil.PURPLE_DARK_PURPLE));
        }
    }
}
