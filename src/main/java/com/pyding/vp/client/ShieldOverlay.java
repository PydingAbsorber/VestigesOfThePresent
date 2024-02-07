package com.pyding.vp.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pyding.vp.VestigesOfPresent;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.awt.*;

public class ShieldOverlay {
    private static final ResourceLocation SHIELD = new ResourceLocation(VestigesOfPresent.MODID,
            "textures/gui/shield.png");
    private static final ResourceLocation HEAL1 = new ResourceLocation(VestigesOfPresent.MODID,
            "textures/gui/heal1.png");
    private static final ResourceLocation HEAL2 = new ResourceLocation(VestigesOfPresent.MODID,
            "textures/gui/heal2.png");
    private static final ResourceLocation HEAL3 = new ResourceLocation(VestigesOfPresent.MODID,
            "textures/gui/heal3.png");

    public static final IGuiOverlay HUD_SHIELD = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Player player = Minecraft.getInstance().player;
        if(player.isCreative())
            return;
        float healBonus = VPUtil.getHealBonus(player);
        if(healBonus < 0) {
            healBonus *= -1;
            if (healBonus <= 30)
                RenderSystem.setShaderTexture(0, HEAL1);
            else if (healBonus <= 60)
                RenderSystem.setShaderTexture(0, HEAL2);
            else RenderSystem.setShaderTexture(0, HEAL3);
            int sizeX = 8;
            int sizeY = 8;
            int pictureSizeX = 9;
            int pictureSizeY = 9;


            for (int i = 0; i < 10; i++) {
                GuiComponent.blit(poseStack, x - 90 + (i * sizeX - 1), y - 39, 0, 0, sizeX, sizeY,
                        pictureSizeX, pictureSizeY);
            }
        }

        float shield = VPUtil.getShield(player);
        if(shield > 0) {
            int sizeX = 16;
            int sizeY = 16;
            int pictureSizeX = 16;
            int pictureSizeY = 16;
            RenderSystem.setShaderTexture(0, SHIELD);
            GuiComponent.blit(poseStack, x - (130+20), y - 39, 0, 0, sizeX, sizeY,
                    pictureSizeX, pictureSizeY);
            Font fontRenderer = Minecraft.getInstance().font;
            //GuiComponent.drawString(poseStack, fontRenderer,"666 "+shield,x - 110, y - 50, 0); same shit lol
            double log10 = Math.log10(shield);
            int move = (int) Math.floor(log10) + 1;
            fontRenderer.draw(poseStack, ""+Math.round(shield * 100.0f) / 100.0f, x - (131+20 + move), y - 20, 0x808080); //0x000000 for black
        }

        //94 54
        //90 60 высота выше брони, правее
        //92 42 высота ниже брони едва касается, левее на 2 пикселя
        //91 52 выше, 1 пиксель левее
        //90 35 слишком низко, 1 пиксель правее, что за хуйня
        //90 40 1 пиксель выше
        //90 39 идеальная высота
    });
}
