package com.pyding.vp.mixin;

import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.vestiges.Rune;
import com.pyding.vp.util.VPUtil;
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

    @Shadow public abstract boolean hasTag();

    @ModifyVariable(at = @At(value = "INVOKE", target = "net/minecraft/world/item/ItemStack.getDamageValue()I"), method = "hurt", argsOnly = true, ordinal = 0)
    public int duration(int amount, int amountCopy, RandomSource pRandom, @Nullable ServerPlayer player) {
        if(this.hasTag()) {
            if(this.getOrCreateTag().getBoolean("VPUnbreak"))
                return 0;
        }
        if(player != null && VPUtil.hasVestige(ModItems.RUNE.get(), player)){
            ItemStack stack = VPUtil.getVestigeStack(Rune.class,player);
            if(stack.getItem() instanceof Rune rune){
                if(rune.isUltimateActive(stack)) {
                    if(rune.isStellar(stack))
                        VPUtil.regenOverShield(player,amount);
                    return 0;
                }
                else if(rune.isStellar(stack)){
                    if(VPUtil.getOverShield(player) > amount){
                        player.getPersistentData().putFloat("VPOverShield",VPUtil.getOverShield(player)-amount);
                        return 0;
                    }
                    else if(VPUtil.getShield(player) > amount){
                        player.getPersistentData().putFloat("VPShield",VPUtil.getShield(player)-amount);
                        return 0;
                    }
                }
            }
        }
        return amount;
    }
}
