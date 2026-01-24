package com.pyding.vp.mixin;

import com.pyding.vp.util.VPUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LocalPlayer.class, priority = 0)
public class VPLocalPlayerMixin {

    @Inject(method = "shouldShowDeathScreen",at = @At("HEAD"),cancellable = true, require = 1)
    private void deathScreen(CallbackInfoReturnable<Boolean> cir){
        Player player = ((Player)(Object)this);
        if(VPUtil.isRoflanEbalo(player))
            cir.setReturnValue(true);
    }

}
