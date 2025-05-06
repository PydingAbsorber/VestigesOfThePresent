package com.pyding.vp.mixin;

import com.mojang.authlib.GameProfile;
import com.pyding.vp.util.GradientUtil;
import com.pyding.vp.util.LeaderboardUtil;
import com.pyding.vp.util.VPUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = PlayerList.class)
public class PlayerListMixin {

    @Inject(method = "respawn",at = @At("HEAD"),cancellable = true, require = 1)
    private void respawn(ServerPlayer player, boolean p_11238_, CallbackInfoReturnable<ServerPlayer> cir){
        if(VPUtil.isRoflanEbalo(player))
            player.getPersistentData().putLong("VPMirnoeReshenie",0);
    }
}
