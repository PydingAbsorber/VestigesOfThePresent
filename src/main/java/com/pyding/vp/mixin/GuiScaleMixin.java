package com.pyding.vp.mixin;

import com.mojang.blaze3d.platform.Window;
import com.pyding.vp.client.*;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Window.class)
public abstract class GuiScaleMixin {

    boolean isCoolGui(){
        return Minecraft.getInstance().screen instanceof VestigeScreen
                || Minecraft.getInstance().screen instanceof LeaderboardScreen
                || Minecraft.getInstance().screen instanceof MysteryChestScreen
                || Minecraft.getInstance().screen instanceof MysteryDropScreen
                || Minecraft.getInstance().screen instanceof ChallengeScreen
                || Minecraft.getInstance().screen instanceof GuideScreen
                || Minecraft.getInstance().screen instanceof NightmareScreen;
    }

    @Inject(method = "getGuiScaledHeight", at = @At("HEAD"), cancellable = true)
    public void guiHeightMixin(CallbackInfoReturnable<Integer> cir) {
        if (isCoolGui()) {
            Window window = (Window) (Object) (this);
            double guiScale = window.calculateScale(2, Minecraft.getInstance().isEnforceUnicode());
            cir.setReturnValue((int) Math.floor(window.getHeight() / guiScale)+1);
        }
    }

    @Inject(method = "getGuiScaledWidth", at = @At("HEAD"), cancellable = true)
    public void guiWidthMixin(CallbackInfoReturnable<Integer> cir) {
        if (isCoolGui()) {
            Window window = (Window) (Object) (this);
            double guiScale = window.calculateScale(2, Minecraft.getInstance().isEnforceUnicode());
            cir.setReturnValue((int) Math.floor(window.getWidth() / guiScale)+1);
        }
    }

    @Inject(method = "getGuiScale", at = @At("HEAD"), cancellable = true)
    public void guiScaleMixin(CallbackInfoReturnable<Double> cir) {
        if (isCoolGui()) {
            cir.setReturnValue((double) ((Window)(Object)(this)).calculateScale(2, Minecraft.getInstance().isEnforceUnicode()));
        }
    }
}
