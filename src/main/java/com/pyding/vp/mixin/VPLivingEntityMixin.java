package com.pyding.vp.mixin;

import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(value = LivingEntity.class)
public abstract class VPLivingEntityMixin {

    @Shadow public abstract AttributeMap getAttributes();

    @Shadow public abstract float getMaxHealth();

    @Shadow @Nullable protected Player lastHurtByPlayer;

    @Shadow public abstract float getHealth();

    @Shadow @Nullable private DamageSource lastDamageSource;

    @Inject(method = "getDamageAfterMagicAbsorb",at = @At("RETURN"),cancellable = true, require = 1)
    protected void fuckEnchantmentsFinallyIHope(DamageSource p_21193_, float p_21194_,CallbackInfoReturnable<Float> info){
        if(!p_21193_.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
            for (ItemStack stack : ((LivingEntityVzlom)this).getArmorSlots()) {
                for (Enchantment enchantment : stack.getAllEnchantments().keySet()) {
                    if (stack.getEnchantmentLevel(enchantment) < 0) {
                        float k = (float)EnchantmentHelper.getDamageProtection(((LivingEntityVzlom)this).getArmorSlots(), p_21193_);
                        info.setReturnValue(p_21194_ * (1.0F - k / 25.0F));
                    }
                }
            }
        }
    }

    @Inject(method = "setHealth",at = @At("HEAD"),cancellable = true, require = 1)
    protected void setHealthMix(float amount, CallbackInfo ci){
        if(this.getAttributes().hasModifier(Attributes.MAX_HEALTH, UUID.fromString("534c53b9-3c22-4c34-bdcd-f255a9694b34"))){
            if(amount < this.getHealth()) {
                if (this.lastHurtByPlayer != null && this.lastHurtByPlayer.getPersistentData().getBoolean("VPAttacked")) {
                    this.lastHurtByPlayer.getPersistentData().putBoolean("VPAttacked", false);
                }
                else if (this.lastDamageSource != null) {
                    this.lastDamageSource = null;
                }
                else ci.cancel();
            } else {
                ci.cancel();
            }
        }
    }

    @Inject(method = "getMaxHealth",at = @At("RETURN"),cancellable = true, require = 1)
    protected void getMaxHealthMix(CallbackInfoReturnable<Float> cir){
        if(cir.getReturnValue() == 2048 && this.getAttributes().hasModifier(Attributes.MAX_HEALTH, UUID.fromString("ee3a5be4-dfe5-4756-b32b-3e3206655f47"))
        || (cir.getReturnValue() < 2048 && this.getAttributes().hasModifier(Attributes.MAX_HEALTH, UUID.fromString("534c53b9-3c22-4c34-bdcd-f255a9694b34")))){
            float maxHealth = 2048 * ConfigHandler.COMMON.bossHP.get();
            if(this.getAttributes().hasModifier(Attributes.MAX_HEALTH, UUID.fromString("534c53b9-3c22-4c34-bdcd-f255a9694b34"))){
                maxHealth *= 10;
            }
            cir.setReturnValue(maxHealth);
        }
    }

    @Inject(method = "getScale",at = @At("RETURN"),cancellable = true, require = 1)
    protected void getScaleMix(CallbackInfoReturnable<Float> cir){
        if(this.getAttributes().hasModifier(Attributes.MAX_HEALTH, UUID.fromString("534c53b9-3c22-4c34-bdcd-f255a9694b34"))){
            cir.setReturnValue(3f);
        }
    }
}
