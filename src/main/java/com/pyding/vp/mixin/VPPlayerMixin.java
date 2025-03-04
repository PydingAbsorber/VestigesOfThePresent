package com.pyding.vp.mixin;

import com.mojang.authlib.GameProfile;
import com.pyding.vp.util.GradientUtil;
import com.pyding.vp.util.LeaderboardUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Player.class)
public class VPPlayerMixin {
    @Shadow @Final private GameProfile gameProfile;

    @Inject(method = "getName",at = @At("RETURN"),cancellable = true, require = 1)
    private void getNameMixin(CallbackInfoReturnable<Component> cir){
        if(LeaderboardUtil.hasGoldenName(((Player)(Object)this).getUUID()))
            cir.setReturnValue(GradientUtil.buildGradientComponent(gameProfile.getName()));
    }
}
