package com.pyding.vp.mixin;

import com.ibm.icu.impl.CollectionSet;
import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.item.VipActivator;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(value = LivingEntity.class)
public abstract class VPLivingEntityMixin {

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
        LivingEntity entity = (LivingEntity)(Object)this;
        if(VPUtil.isRoflanEbalo(entity)) {
            ci.cancel();
            return;
        }
        if(entity.getHealth() < amount)
            return;
        if(VPUtil.isNpc(entity.getType()) || VPUtil.getOverShield(entity) > 0){
            ci.cancel();
            return;
        }
        if(ConfigHandler.COMMON_SPEC.isLoaded() && VPUtil.isNightmareBoss(entity)){
            ci.cancel();
            float damage = VPUtil.nightmareAbsorption(entity,entity.getHealth() - amount);
            ((EntityVzlom)this).getEntityData().set(((LivingEntityVzlom)this).getDataHealth(),entity.getHealth() - damage);
        }
    }

    @Inject(method = "getMaxHealth",at = @At("RETURN"),cancellable = true, require = 1)
    protected void getMaxHealthMix(CallbackInfoReturnable<Float> cir){
        LivingEntity entity = (LivingEntity)(Object)this;
        if(ConfigHandler.COMMON_SPEC.isLoaded() && ConfigHandler.COMMON.cruelMode.get() && (ConfigHandler.COMMON.unlockHp.get() || (cir.getReturnValue() <= 2048 && VPUtil.isNightmareBoss(entity))) && VPUtil.getBaseHealth(((EntityVzlom)this).getTypeMix()) != 0){
            float maxHealth = Math.max(600,VPUtil.getBaseHealth(((EntityVzlom)this).getTypeMix())) * ConfigHandler.COMMON.bossHP.get();
            if(entity.getAttributes() != null){
                maxHealth *= 10;
            }
            cir.setReturnValue(maxHealth); //i hate this so fucking much, some bullshit server mod locks hp to 2048 and thats it
        }
    }

    @Inject(method = "getScale",at = @At("RETURN"),cancellable = true, require = 1)
    protected void getScaleMix(CallbackInfoReturnable<Float> cir){
        LivingEntity entity = (LivingEntity)(Object)this;
        if(VPUtil.isNightmareBoss(entity)){
            cir.setReturnValue(3f);
        }
    }

    @Inject(method = "die",at = @At("HEAD"),cancellable = true, require = 1)
    protected void dieMix(DamageSource p_21014_, CallbackInfo ci){
        LivingEntity entity = (LivingEntity)(Object)this;
        if(VPUtil.isNightmareBoss(entity) && (VPUtil.getOverShield(entity) > 0 || entity.getHealth() > 0) && VPUtil.canResurrect(entity)){
            ci.cancel();
        }
    }

    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"), cancellable = true)
    private void onDropAllDeathLoot(DamageSource p_21192_, CallbackInfo cir) {
        if((Object)this instanceof Player player) {
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                if (cap.getVip() > System.currentTimeMillis()) {
                    VipActivator.saveInventory(player);
                    cir.cancel();
                }
            });
        }
    }

    @Inject(method = "hasEffect",at = @At("RETURN"),cancellable = true, require = 1)
    private void hasEffect(MobEffect p_21024_, CallbackInfoReturnable<Boolean> cir){
        if(VPUtil.isRoflanEbalo(((LivingEntity)(Object)this)))
            cir.setReturnValue(false);
    }

    @Inject(method = "getEffect",at = @At("RETURN"),cancellable = true, require = 1)
    private void getEffect(MobEffect p_21024_, CallbackInfoReturnable<MobEffectInstance> cir){
        if(VPUtil.isRoflanEbalo(((LivingEntity)(Object)this)))
            cir.setReturnValue(new MobEffectInstance(MobEffects.HARM,0,0));
    }

    @Inject(method = "getActiveEffects",at = @At("RETURN"),cancellable = true, require = 1)
    private void getEffects(CallbackInfoReturnable<Collection<MobEffectInstance>> cir){
        if(VPUtil.isRoflanEbalo(((LivingEntity)(Object)this))) {
            Collection<MobEffectInstance> collection = new ArrayList<>();
            collection.add(new MobEffectInstance(MobEffects.HARM,0,0));
            cir.setReturnValue(collection);
        }
    }

    @Inject(method = "getActiveEffectsMap",at = @At("RETURN"),cancellable = true, require = 1)
    private void getEffectsMap(CallbackInfoReturnable<Map<MobEffect, MobEffectInstance>> cir){
        if(VPUtil.isRoflanEbalo(((LivingEntity)(Object)this))) {
            Map<MobEffect, MobEffectInstance> map = new HashMap<>();
            map.put(MobEffects.HARM,new MobEffectInstance(MobEffects.HARM,0,0));
            cir.setReturnValue(map);
        }
    }
}
