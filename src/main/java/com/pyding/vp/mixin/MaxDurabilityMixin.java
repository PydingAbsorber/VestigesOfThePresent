package com.pyding.vp.mixin;

import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.vestiges.Rune;
import com.pyding.vp.util.VPUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mixin(value = ItemStack.class)
public abstract class MaxDurabilityMixin {

    @Inject(method = "hurtAndBreak", at = @At("HEAD"), cancellable = true)
    public void onHurtAndBreak(int amount, ServerLevel level, @Nullable ServerPlayer player, Consumer<Item> onBreak, CallbackInfo ci) {
        ItemStack self = (ItemStack) (Object) this;
        CustomData customData = self.get(DataComponents.CUSTOM_DATA);
        if (customData != null && customData.copyTag().getBoolean("VPUnbreak")) {
            ci.cancel();
            return;
        }
        if (player != null && VPUtil.hasVestige(ModItems.RUNE.get(), player)) {
            ItemStack stack = VPUtil.getVestigeStack(Rune.class, player);
            if (stack.getItem() instanceof Rune rune) {
                if (rune.isUltimateActive(stack)) {
                    if (rune.isStellar(stack)) {
                        VPUtil.regenOverShield(player, amount);
                    }
                    ci.cancel();
                    return;
                }
                else if (rune.isStellar(stack)) {
                    if (VPUtil.getOverShield(player) > amount) {
                        player.getPersistentData().putFloat("VPOverShield", VPUtil.getOverShield(player) - amount);
                        ci.cancel();
                        return;
                    }
                    else if (VPUtil.getShield(player) > amount) {
                        player.getPersistentData().putFloat("VPShield", VPUtil.getShield(player) - amount);
                        ci.cancel();
                        return;
                    }
                }
            }
        }
    }
}
