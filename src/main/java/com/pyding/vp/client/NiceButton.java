package com.pyding.vp.client;

import com.pyding.vp.client.sounds.SoundRegistry;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceLocation;

public class NiceButton extends ImageButton {

    public NiceButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex, ResourceLocation resourceLocation, OnPress onPress) {
        super(x, y, width, height, new WidgetSprites(resourceLocation, resourceLocation), onPress);
    }

    public NiceButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex, ResourceLocation resourceLocation, int textureWidth, int textureHeight, OnPress onPress) {
        super(x, y, width, height, new WidgetSprites(resourceLocation, resourceLocation), onPress, CommonComponents.EMPTY);
    }

    @Override
    public void playDownSound(SoundManager handler) {
        handler.play(SimpleSoundInstance.forUI(SoundRegistry.VESTIGE_GUI_BUTTON.get(), 1.0F));
    }
}
