package com.pyding.vp.mixin;

import com.pyding.vp.util.VPUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.DamageTypeTags;
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
    @Shadow public abstract Iterable<ItemStack> getArmorSlots();

    @Shadow public abstract AttributeMap getAttributes();

    @Shadow public abstract void remove(Entity.RemovalReason p_276115_);

    @Shadow public abstract float getMaxHealth();

    @Shadow @Nullable protected Player lastHurtByPlayer;

    @Inject(method = "getDamageAfterMagicAbsorb",at = @At("RETURN"),cancellable = true, require = 1)
    protected void fuckEnchantmentsFinallyIHope(DamageSource p_21193_, float p_21194_,CallbackInfoReturnable<Float> info){
        if(!p_21193_.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
            for (ItemStack stack : this.getArmorSlots()) {
                for (Enchantment enchantment : stack.getAllEnchantments().keySet()) {
                    if (stack.getEnchantmentLevel(enchantment) < 0) {
                        float k = (float)EnchantmentHelper.getDamageProtection(this.getArmorSlots(), p_21193_);
                        info.setReturnValue(p_21194_ * (1.0F - k / 25.0F));
                    }
                }
            }
        }
    }

    @Inject(method = "setHealth",at = @At("HEAD"),cancellable = true, require = 1)
    protected void setHealthMix(float amount, CallbackInfo ci){
        if(this.getAttributes().hasModifier(Attributes.MAX_HEALTH, UUID.fromString("534c53b9-3c22-4c34-bdcd-f255a9694b34"))
        && amount < this.getMaxHealth()){
            if(this.lastHurtByPlayer != null && this.lastHurtByPlayer.getPersistentData().getBoolean("VPAttacked")) {
                this.lastHurtByPlayer.getPersistentData().putBoolean("VPAttacked",false);
                return;
            }
            ci.cancel();
        }
    }

    @Inject(method = "getScale",at = @At("RETURN"),cancellable = true, require = 1)
    protected void getScaleMix(CallbackInfoReturnable<Float> cir){
        if(this.getAttributes().hasModifier(Attributes.MAX_HEALTH, UUID.fromString("534c53b9-3c22-4c34-bdcd-f255a9694b34"))){
            cir.setReturnValue(3f);
        }
    }
}
