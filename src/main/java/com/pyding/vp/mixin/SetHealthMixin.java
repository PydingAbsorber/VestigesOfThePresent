package com.pyding.vp.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(value = LivingEntity.class)
public class SetHealthMixin {
    @Shadow @Nullable protected Player lastHurtByPlayer;

    @Inject(method = "setHealth",at = @At("HEAD"), require = 1)
    private void setHealth(float p_21154_, CallbackInfo ci){
        if(this.lastHurtByPlayer != null){
            Player player = this.lastHurtByPlayer;
            if(player.getPersistentData().getLong("VPSetHealth") >= System.currentTimeMillis()) {
                ci.cancel();
            }
        }
    }
}
