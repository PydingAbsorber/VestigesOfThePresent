package com.pyding.vp.mixin;

import com.pyding.vp.util.GradientUtil;
import com.pyding.vp.util.LeaderboardUtil;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(value = LocalPlayer.class, priority = 0)
public class VPLocalPlayerMixin {

    @Inject(method = "shouldShowDeathScreen",at = @At("HEAD"),cancellable = true, require = 1)
    private void deathScreen(CallbackInfoReturnable<Boolean> cir){
        Player player = ((Player)(Object)this);
        if(VPUtil.isRoflanEbalo(player))
            cir.setReturnValue(true);
    }

}
