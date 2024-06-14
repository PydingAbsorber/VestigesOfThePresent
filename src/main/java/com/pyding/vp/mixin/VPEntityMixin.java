package com.pyding.vp.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class)
public abstract class VPEntityMixin {

    @Shadow public abstract CompoundTag getPersistentData();

    @Inject(method = "isInWaterRainOrBubble",at = @At("RETURN"),cancellable = true, require = 1)
    protected void isInWaterRainOrBubble(CallbackInfoReturnable<Boolean> cir){
        if(this.getPersistentData().getLong("VPWet") > System.currentTimeMillis()){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isInWaterOrRain",at = @At("RETURN"),cancellable = true, require = 1)
    protected void isInWaterOrRain(CallbackInfoReturnable<Boolean> cir){
        if(this.getPersistentData().getLong("VPWet") > System.currentTimeMillis()){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isInBubbleColumn",at = @At("RETURN"),cancellable = true, require = 1)
    protected void isInBubbleColumn(CallbackInfoReturnable<Boolean> cir){
        if(this.getPersistentData().getLong("VPWet") > System.currentTimeMillis()){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isInRain",at = @At("RETURN"),cancellable = true, require = 1)
    protected void isInRain(CallbackInfoReturnable<Boolean> cir){
        if(this.getPersistentData().getLong("VPWet") > System.currentTimeMillis()){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isInWater",at = @At("RETURN"),cancellable = true, require = 1)
    protected void isInWater(CallbackInfoReturnable<Boolean> cir){
        if(this.getPersistentData().getLong("VPWet") > System.currentTimeMillis()){
            cir.setReturnValue(true);
        }
    }
}
