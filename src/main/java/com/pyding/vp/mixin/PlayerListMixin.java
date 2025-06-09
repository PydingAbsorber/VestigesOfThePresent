package com.pyding.vp.mixin;

import com.pyding.vp.util.VPUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerList.class, priority = 0)
public class PlayerListMixin {

    @Inject(method = "respawn",at = @At("HEAD"),cancellable = true, require = 1)
    private void respawn(ServerPlayer player, boolean p_11238_, CallbackInfoReturnable<ServerPlayer> cir){
        VPUtil.printTrack("Was Revive",player);
        if(VPUtil.isRoflanEbalo(player))
            cir.cancel();
    }
}
