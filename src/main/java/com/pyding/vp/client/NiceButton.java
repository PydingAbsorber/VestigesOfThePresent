package com.pyding.vp.client;

import com.pyding.vp.client.sounds.SoundRegistry;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class NiceButton extends ImageButton {


    public NiceButton(int p_169011_, int p_169012_, int p_169013_, int p_169014_, int p_169015_, int p_169016_, ResourceLocation p_169017_, OnPress p_169018_) {
        super(p_169011_, p_169012_, p_169013_, p_169014_, p_169015_, p_169016_, p_169017_, p_169018_);
    }

    public NiceButton(int p_94269_, int p_94270_, int p_94271_, int p_94272_, int p_94273_, int p_94274_, int p_94275_, ResourceLocation p_94276_, Button.OnPress p_94277_) {
        super(p_94269_, p_94270_, p_94271_, p_94272_, p_94273_, p_94274_, p_94275_, p_94276_, 256, 256, p_94277_);
    }

    public NiceButton(int p_94230_, int p_94231_, int p_94232_, int p_94233_, int p_94234_, int p_94235_, int p_94236_, ResourceLocation p_94237_, int p_94238_, int p_94239_, Button.OnPress p_94240_) {
        super(p_94230_, p_94231_, p_94232_, p_94233_, p_94234_, p_94235_, p_94236_, p_94237_, p_94238_, p_94239_, p_94240_, CommonComponents.EMPTY);
    }

    @Override
    public void playDownSound(net.minecraft.client.sounds.SoundManager handler) {
        handler.play(net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(SoundRegistry.VESTIGE_GUI_BUTTON.get(), 1.0F));
    }
}
