package com.pyding.vp.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;

@Mixin(value = ItemStack.class)
public abstract class MaxDurabilityMixin {

    @Shadow public abstract CompoundTag getOrCreateTag();

    @ModifyVariable(at = @At(value = "INVOKE", target = "net/minecraft/world/item/ItemStack.getDamageValue()I"), method = "hurt", argsOnly = true, ordinal = 0)
    public int duration(int amount, int amountCopy, RandomSource pRandom, @Nullable ServerPlayer player) {
        if(this.getOrCreateTag().getBoolean("VPUnbreak"))
            return 0;
        return amount;
    }
}
