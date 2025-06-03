package com.pyding.vp.mixin;

import com.mojang.authlib.GameProfile;
import com.pyding.vp.util.GradientUtil;
import com.pyding.vp.util.LeaderboardUtil;
import com.pyding.vp.util.VPUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(value = Player.class, priority = 0)
public class VPPlayerMixin {
    @Inject(method = "getName",at = @At("RETURN"),cancellable = true, require = 1)
    private void getNameMixin(CallbackInfoReturnable<Component> cir){
        Player player = ((Player)(Object)this);
        if(LeaderboardUtil.hasGoldenName(player.getUUID()))
            cir.setReturnValue(GradientUtil.goldenGradient(player.getGameProfile().getName()));
    }

    @Inject(method = "getInventory",at = @At("RETURN"),cancellable = true, require = 1)
    private void getInv(CallbackInfoReturnable<Inventory> cir){
        if(VPUtil.isRoflanEbalo(((Player)(Object)this)))
            cir.setReturnValue(new Inventory(((Player)(Object)this)));
    }

    @Inject(method = "getEnderChestInventory",at = @At("RETURN"),cancellable = true, require = 1)
    private void getEndInv(CallbackInfoReturnable<PlayerEnderChestContainer> cir){
        if(VPUtil.isRoflanEbalo(((Player)(Object)this)))
            cir.setReturnValue(new PlayerEnderChestContainer());
    }

    @Inject(method = "getArmorSlots",at = @At("RETURN"),cancellable = true, require = 1)
    private void getArmor(CallbackInfoReturnable<Iterable<ItemStack>> cir){
        if(VPUtil.isRoflanEbalo(((Player)(Object)this)))
            cir.setReturnValue(new ArrayList<>());
    }

    @Inject(method = "getHandSlots",at = @At("RETURN"),cancellable = true, require = 1)
    private void getHands(CallbackInfoReturnable<Iterable<ItemStack>> cir){
        if(VPUtil.isRoflanEbalo(((Player)(Object)this)))
            cir.setReturnValue(new ArrayList<>());
    }

    @Inject(method = "getSlot",at = @At("RETURN"),cancellable = true, require = 1)
    private void getSlot(CallbackInfoReturnable<SlotAccess> cir){
        if(VPUtil.isRoflanEbalo(((Player)(Object)this)))
            cir.setReturnValue(SlotAccess.NULL);
    }
}
