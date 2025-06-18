package com.pyding.vp.mixin;

import com.pyding.vp.util.VPUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = ForgeHooks.class, remap = false, priority = 1)
public class ForgeMixin {

    @Inject(method = "onLivingDeath",at = @At("HEAD"),cancellable = true, require = 1)
    private static void onDeath(LivingEntity entity, DamageSource src, CallbackInfoReturnable<Boolean> cir){
        if(entity instanceof Player && VPUtil.isRoflanEbalo(entity)) {
            cir.setReturnValue(true);
        }
    }
}
