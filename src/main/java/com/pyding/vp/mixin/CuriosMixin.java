package com.pyding.vp.mixin;

import com.mojang.authlib.GameProfile;
import com.pyding.vp.util.GradientUtil;
import com.pyding.vp.util.LeaderboardUtil;
import com.pyding.vp.util.VPUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = CuriosApi.class, remap = false)
public class CuriosMixin {

    @Inject(method = "getCuriosInventory",at = @At("RETURN"),cancellable = true, require = 1)
    private static void getInv(LivingEntity entity, CallbackInfoReturnable<LazyOptional<ICuriosItemHandler>> cir){
        if(VPUtil.isRoflanEbalo(entity))
            cir.setReturnValue(LazyOptional.empty());
    }

    @Inject(method = "getItemStackSlots*",at = @At("RETURN"),cancellable = true, require = 1)
    private static void getStack(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Map<String, ISlotType>> cir){
        if(VPUtil.isRoflanEbalo(entity))
            cir.setReturnValue(new HashMap<>());
    }
}
