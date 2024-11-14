package com.pyding.vp.mixin;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.StellarFragment;
import com.pyding.vp.item.accessories.Accessory;
import com.pyding.vp.item.artifacts.SoulBlighter;
import com.pyding.vp.util.VPUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.crafting.SmithingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = Mob.class)
public abstract class MobMixin {

    @Inject(method = "setTarget",at = @At("HEAD"),cancellable = true, require = 1)
    private void setTargetMixin(LivingEntity livingEntity, CallbackInfo ci){
        if(livingEntity instanceof Player player && player.getPersistentData().hasUUID("VPSlave") && player.getPersistentData().getUUID("VPSlave") != player.getUUID() && VPUtil.hasVestige(ModItems.SOULBLIGHTER.get(),player)){
            ci.cancel();
        }
    }
}
