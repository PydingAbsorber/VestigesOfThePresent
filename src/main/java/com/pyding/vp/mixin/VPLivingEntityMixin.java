package com.pyding.vp.mixin;

import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(value = LivingEntity.class)
public abstract class VPLivingEntityMixin {

    @Shadow public abstract AttributeMap getAttributes();

    @Shadow public abstract float getMaxHealth();

    @Shadow public abstract float getHealth();


    @Inject(method = "getDamageAfterMagicAbsorb",at = @At("RETURN"),cancellable = true, require = 1)
    protected void fuckEnchantmentsFinallyIHope(DamageSource p_21193_, float p_21194_,CallbackInfoReturnable<Float> info){
        if(!p_21193_.isBypassEnchantments()) {
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
        if(ConfigHandler.COMMON_SPEC.isLoaded() && this.getAttributes() != null && this.getAttributes().hasModifier(Attributes.MAX_HEALTH, UUID.fromString("534c53b9-3c22-4c34-bdcd-f255a9694b34"))){
            if(getHealth()+amount < getHealth() && amount > ConfigHandler.COMMON.nightmareDamageCap.get()) {
                amount = (float)(getHealth() - ConfigHandler.COMMON.nightmareDamageCap.get());
                ci.cancel();
                ((LivingEntity)(Object)this).getEntityData().set(((LivingEntityVzlom)this).getDataHealth(),amount);
            }
        }
    }

    @Inject(method = "getMaxHealth",at = @At("RETURN"),cancellable = true, require = 1)
    protected void getMaxHealthMix(CallbackInfoReturnable<Float> cir){
        if(ConfigHandler.COMMON_SPEC.isLoaded() && (ConfigHandler.COMMON.unlockHp.get() || cir.getReturnValue() <= 2048) && VPUtil.getBaseHealth(((EntityVzlom)this).getTypeMix()) != 0){
            float maxHealth = Math.max(600,VPUtil.getBaseHealth(((EntityVzlom)this).getTypeMix())) * ConfigHandler.COMMON.bossHP.get();
            if(this.getAttributes() != null && this.getAttributes().hasModifier(Attributes.MAX_HEALTH, UUID.fromString("534c53b9-3c22-4c34-bdcd-f255a9694b34"))){
                maxHealth *= 10;
            }
            cir.setReturnValue(maxHealth); //i hate this so fucking much, some bullshit server mod locks hp to 2048 and thats it
        }
    }

    @Inject(method = "getScale",at = @At("RETURN"),cancellable = true, require = 1)
    protected void getScaleMix(CallbackInfoReturnable<Float> cir){
        if(this.getAttributes() != null && this.getAttributes().hasModifier(Attributes.MAX_HEALTH, UUID.fromString("534c53b9-3c22-4c34-bdcd-f255a9694b34"))){
            cir.setReturnValue(3f);
        }
    }

    @Inject(method = "die",at = @At("HEAD"),cancellable = true, require = 1)
    protected void dieMix(DamageSource p_21014_, CallbackInfo ci){
        if(this.getAttributes() != null && this.getAttributes().hasModifier(Attributes.MAX_HEALTH, UUID.fromString("534c53b9-3c22-4c34-bdcd-f255a9694b34"))){
            if(getHealth() > 0){
                ci.cancel();
                return;
            }
        }
    }
}
