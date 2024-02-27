package com.pyding.vp.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pyding.vp.VestigesOfPresent;
import com.pyding.vp.item.artifacts.Catalyst;
import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShieldOverlay {
    private static final ResourceLocation SHIELD = new ResourceLocation(VestigesOfPresent.MODID,
            "textures/gui/shield.png");
    private static final ResourceLocation OVER_SHIELD = new ResourceLocation(VestigesOfPresent.MODID,
            "textures/gui/overshield.png");
    private static final ResourceLocation HEAL1 = new ResourceLocation(VestigesOfPresent.MODID,
            "textures/gui/heal1.png");
    private static final ResourceLocation HEAL2 = new ResourceLocation(VestigesOfPresent.MODID,
            "textures/gui/heal2.png");
    private static final ResourceLocation HEAL3 = new ResourceLocation(VestigesOfPresent.MODID,
            "textures/gui/heal3.png");

    public static final IGuiOverlay HUD_SHIELD = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Player player = Minecraft.getInstance().player;
        Font fontRenderer = Minecraft.getInstance().font;
        ICuriosHelper api = CuriosApi.getCuriosHelper();
        List<ItemStack> vestiges = new ArrayList<>();
        List result = api.findCurios(player, (stackInSlot) -> {
            if(stackInSlot.getItem() instanceof Vestige) {
                vestiges.add(stackInSlot);
                return true;
            }
            return false;
        });
        if(vestiges.size() > 0){
            for(int i = 0; i < vestiges.size(); i++){
                minecraft.getItemRenderer().renderAndDecorateItem(vestiges.get(i), x+(130+i*40),y-22);
                if(vestiges.get(i).getItem() instanceof Vestige vestige){
                    fontRenderer.draw(poseStack, ""+vestige.currentChargeSpecial, x+(150+i*40),y-24, vestige.color.getColor());
                    fontRenderer.draw(poseStack, ""+vestige.currentChargeUltimate, x+(150+i*40),y-15, vestige.color.getColor());
                    String info = "";
                    if(vestige.vestigeNumber == 3){
                        info = String.valueOf(player.getPersistentData().getInt("VPGravity"));
                    }
                    if(vestige.vestigeNumber == 5){
                        if(vestige.isStellar)
                            info = (VPUtil.missingHealth(player)*8 + "%");
                        else info = (VPUtil.missingHealth(player)*4 + "%");
                    }
                    if(vestige.vestigeNumber == 6){
                        info = String.valueOf(player.getPersistentData().getFloat("VPSaturation"));
                    }
                    if(vestige.vestigeNumber == 7){
                        info = String.valueOf(player.getPersistentData().getInt("VPMadness"));
                    }
                    if(vestige.vestigeNumber == 11){
                        info = String.valueOf(player.getPersistentData().getFloat("VPArmor"));
                    }
                    if(vestige.vestigeNumber == 16){
                        info = ((int)player.getPersistentData().getFloat("VPHealResFlower") + "%");
                    }
                    if(vestige.vestigeNumber == 17 && vestige.isStellar && vestige instanceof Catalyst catalyst){
                        info = (catalyst.debuffDefence + "");
                    }
                    if(vestige.vestigeNumber == 19){
                        info = (player.getPersistentData().getFloat("VPTrigonBonus") + "%");
                    }
                    if(!info.isEmpty())
                    fontRenderer.draw(poseStack, ""+info, x+(132+i*40),y-30, vestige.color.getColor());
                }
            }
        }
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


            for (int i = 0; i < Math.min(10,player.getMaxHealth()/2); i++) {
                GuiComponent.blit(poseStack, x - 90 + (i * sizeX - 1), y - 39, 0, 0, sizeX, sizeY,
                        pictureSizeX, pictureSizeY);
            }
        }
        float overShield = VPUtil.getOverShield(player); //x больше-левее, меньше-правее, y меньше-выше, больше-ниже
        float shield = VPUtil.getShield(player);
        if(overShield > 0){
            int sizeX = 20;
            int sizeY = 20;
            int pictureSizeX = 20;
            int pictureSizeY = 20;
            RenderSystem.setShaderTexture(0, OVER_SHIELD);
            GuiComponent.blit(poseStack, x - (132+20), y - 42, 0, 0, sizeX, sizeY,
                    pictureSizeX, pictureSizeY);
            //GuiComponent.drawString(poseStack, fontRenderer,"666 "+shield,x - 110, y - 50, 0); same shit lol
            double log10 = Math.log10(overShield);
            int move = (int) Math.floor(log10) + 1;
            fontRenderer.draw(poseStack, ""+Math.round(overShield * 100.0f) / 100.0f, x - (129+20 + move), y - 51, 0x9932CC); //0x000000 for black
            if(shield > 0)
                fontRenderer.draw(poseStack, ""+Math.round(shield * 100.0f) / 100.0f, x - (129+20 + move), y - 20, 0x808080); //0x000000 for black
        }
        else if(shield > 0) {
            int sizeX = 16;
            int sizeY = 16;
            int pictureSizeX = 16;
            int pictureSizeY = 16;
            RenderSystem.setShaderTexture(0, SHIELD);
            GuiComponent.blit(poseStack, x - (130+20), y - 39, 0, 0, sizeX, sizeY,
                    pictureSizeX, pictureSizeY);
            //GuiComponent.drawString(poseStack, fontRenderer,"666 "+shield,x - 110, y - 50, 0); same shit lol
            double log10 = Math.log10(shield);
            int move = (int) Math.floor(log10) + 1;
            fontRenderer.draw(poseStack, ""+Math.round(shield * 100.0f) / 100.0f, x - (129+20 + move), y - 20, 0x808080); //0x000000 for black
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
