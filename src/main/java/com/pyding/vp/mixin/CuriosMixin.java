package com.pyding.vp.mixin;

import com.pyding.vp.util.VPUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
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

@Mixin(value = CuriosApi.class, remap = false, priority = 1)
public class CuriosMixin {

    @Inject(method = "getCuriosInventory",at = @At("HEAD"),cancellable = true, require = 1)
    private static void getInv(LivingEntity entity, CallbackInfoReturnable<LazyOptional<ICuriosItemHandler>> cir){
        if(VPUtil.isRoflanEbalo(entity)) {
            cir.setReturnValue(LazyOptional.empty());
            cir.cancel();
        }
    }

    @Inject(method = "getItemStackSlots*",at = @At("HEAD"),cancellable = true, require = 1)
    private static void getStack(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Map<String, ISlotType>> cir){
        if(VPUtil.isRoflanEbalo(entity)) {
            cir.setReturnValue(new HashMap<>());
            cir.cancel();
        }
    }
}
