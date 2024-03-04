package com.pyding.vp.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.pyding.vp.VestigesOfPresent;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.artifacts.Catalyst;
import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static com.pyding.vp.VestigesOfPresent.MODID;

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
    public static final IGuiOverlay HUD_SHIELD = ((gui, pose, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;
        PoseStack poseStack = pose.pose();
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Player player = Minecraft.getInstance().player;
        Font fontRenderer = Minecraft.getInstance().font;
        List<ItemStack> vestiges = VPUtil.getVestigeList(player);
        if(vestiges.size() > 0){
            for(int i = 0; i < vestiges.size(); i++){
                    /*minecraft.getItemRenderer().render(stack, ItemDisplayContext.GROUND, true, poseStack, MultiBufferSource.immediate(new BufferBuilder(1)), 0, 1,
                        minecraft.getItemRenderer().getModel(stack, Minecraft.getInstance().level, Minecraft.getInstance().player, 0)); */
                    //0% chance this will fucking work... yeah that didn't fucking work
                //minecraft.getItemRenderer().render(vestiges.get(i), x+(130+i*40),y-22);
                /*RenderSystem.setShaderTexture(0, SHIELD);
                RenderSystem.enableBlend();
                poseStack.pushPose();
                renderTextureFromCenter(poseStack,x+(130+i*40),y-22,width,height,16,16,16,16,1);*/
                if(vestiges.get(i).getItem() instanceof Vestige vestige){
                    if(vestige.vestigeNumber == 0)
                        continue;
                    pose.blit(getTexture(vestige.vestigeNumber),x+(130+i*40),y-22, 0, 0, 16, 16,
                            16, 16);
                    int currentChargeSpecial = player.getPersistentData().getInt("VPCharge"+vestige.vestigeNumber);
                    int currentChargeUltimate = player.getPersistentData().getInt("VPChargeUlt"+vestige.vestigeNumber);
                    long time = player.getPersistentData().getLong("VPTime"+vestige.vestigeNumber);
                    long timeUlt = player.getPersistentData().getLong("VPTimeUlt"+vestige.vestigeNumber);
                    pose.drawString(fontRenderer,""+currentChargeSpecial, x+(150+i*40),y-24, vestige.color.getColor());
                    pose.drawString(fontRenderer,""+currentChargeUltimate, x+(150+i*40),y-15, vestige.color.getColor());
                    //fontRenderer.draw(poseStack, ""+vestige.currentChargeSpecial, x+(150+i*40),y-24, vestige.color.getColor());
                    //fontRenderer.draw(poseStack, ""+vestige.currentChargeUltimate, x+(150+i*40),y-15, vestige.color.getColor());
                    String info = "";
                    if(vestige.vestigeNumber == 3){
                        info = String.valueOf(player.getPersistentData().getInt("VPGravity"));
                    }
                    if(vestige.vestigeNumber == 5 && vestige.isSpecialActive){
                        if(vestige.isStellar)
                            info = ((int)VPUtil.missingHealth(player)*8 + "%");
                        else info = ((int)VPUtil.missingHealth(player)*4 + "%");
                    }
                    if(vestige.vestigeNumber == 6){
                        info = String.valueOf((int)player.getPersistentData().getFloat("VPSaturation"));
                    }
                    if(vestige.vestigeNumber == 7){
                        info = String.valueOf(player.getPersistentData().getInt("VPMadness"));
                    }
                    if(vestige.vestigeNumber == 11){
                        info = String.valueOf((int)player.getPersistentData().getFloat("VPArmor"));
                    }
                    if(vestige.vestigeNumber == 15){
                        info = String.valueOf(player.getPersistentData().getInt("VPDevourerHits"));
                    }
                    if(vestige.vestigeNumber == 16){
                        info = ((int)player.getPersistentData().getFloat("VPHealResFlower") + "%");
                    }
                    if(vestige.vestigeNumber == 17 && vestige.isStellar && vestige instanceof Catalyst catalyst){
                        info = (catalyst.debuffDefence + "");
                    }
                    if(vestige.vestigeNumber == 19){
                        info = ((int)player.getPersistentData().getFloat("VPTrigonBonus") + "%");
                    }
                    String durationUlt = "";
                    String durationSpecial = "";
                    if(vestige.vestigeNumber == 8){
                        int number = Math.round(timeUlt-System.currentTimeMillis())/1000;
                        if(number > 0)
                            durationUlt = String.valueOf(number);
                    }
                    if(vestige.vestigeNumber == 20){
                        int number = Math.round(time-System.currentTimeMillis())/1000;
                        if(number > 0)
                            durationSpecial = String.valueOf(number);
                    }
                    if(vestige.vestigeNumber == 2){
                        int number = Math.round(timeUlt-System.currentTimeMillis())/1000;
                        if(number > 0)
                            durationUlt = String.valueOf(number);
                    }
                    if(vestige.vestigeNumber == 1){
                        int number = Math.round(timeUlt-System.currentTimeMillis())/1000;
                        if(number > 0)
                            durationUlt = String.valueOf(number);
                    }
                    if(vestige.vestigeNumber == 13){
                        int number = Math.round(time-System.currentTimeMillis())/1000;
                        if(number > 0)
                            durationSpecial = String.valueOf(number);
                    }
                    if(vestige.vestigeNumber == 7){
                        int number = Math.round(timeUlt-System.currentTimeMillis())/1000;
                        if(number > 0)
                            durationUlt = String.valueOf(number);
                    }
                    if(!info.isEmpty())
                        pose.drawString(fontRenderer,""+info, x+(132+i*40),y-33, vestige.color.getColor());
                    if(!durationSpecial.isEmpty())
                        pose.drawString(fontRenderer,""+durationSpecial, x+(132+i*40),y-33, 0x00BFFF);
                    if(!durationUlt.isEmpty())
                        pose.drawString(fontRenderer,""+durationUlt, x+(132+i*40),y-33, 0x9932CC);
                    info = "";
                    if(vestige.vestigeNumber == 15){
                        int show = player.getPersistentData().getInt("VPDevourerShow");
                        if(show > 0)
                            pose.drawString(fontRenderer,""+show, x+(132+i*40),y-43, 0x54717B);
                    }
                    if(vestige.vestigeNumber == 13){
                        int number = Math.round(timeUlt-System.currentTimeMillis())/1000;
                        if(number > 0)
                            info = String.valueOf(number);
                    }
                    if(!info.isEmpty())
                        pose.drawString(fontRenderer,""+info, x+(132+i*40),y-43, 0x9932CC);
                    //fontRenderer.draw(poseStack, ""+info, x+(132+i*40),y-30, vestige.color.getColor());
                }
            }
        }
        if(player.isCreative())
            return;
        float healBonus = VPUtil.getHealBonus(player);
        if(healBonus < 0) {
            healBonus *= -1;
            int sizeX = 8;
            int sizeY = 8;
            int pictureSizeX = 9;
            int pictureSizeY = 9;


            for (int i = 0; i < Math.min(10,player.getMaxHealth()/2); i++) {
                /*poseStack.pushPose();
                renderTextureFromCenter(poseStack,x - 90 + (i * sizeX - 1), y - 39,width,height,16,16,16,16,1);*/
                if (healBonus <= 30)
                    pose.blit(HEAL1, x - 90 + (i * sizeX - 1), y - 39, 0, 0, sizeX, sizeY,
                            pictureSizeX, pictureSizeY);
                else if (healBonus <= 60)
                    pose.blit(HEAL2, x - 90 + (i * sizeX - 1), y - 39, 0, 0, sizeX, sizeY,
                            pictureSizeX, pictureSizeY);
                else pose.blit(HEAL3, x - 90 + (i * sizeX - 1), y - 39, 0, 0, sizeX, sizeY,
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
            /*poseStack.pushPose();
            renderTextureFromCenter(poseStack,x - (132+20), y - 42,width,height,16,16,16,16,1);*/
            pose.blit(OVER_SHIELD, x - (132+20), y - 42, 0, 0, sizeX, sizeY,
                    pictureSizeX, pictureSizeY);
            //GuiComponent.drawString(poseStack, fontRenderer,"666 "+shield,x - 110, y - 50, 0); same shit lol
            double log10 = Math.log10(overShield);
            int move = (int) Math.floor(log10) + 1;
            pose.drawString(fontRenderer, ""+Math.round(overShield * 100.0f) / 100.0f, x - (129+20 + move), y - 51, 0x9932CC);
            //fontRenderer.draw(poseStack, ""+Math.round(overShield * 100.0f) / 100.0f, x - (129+20 + move), y - 51, 0x9932CC); //0x000000 for black
            if(shield > 0)
                pose.drawString(fontRenderer, ""+Math.round(shield * 100.0f) / 100.0f, x - (129+20 + move), y - 20, 0x808080);
                //fontRenderer.draw(poseStack, ""+Math.round(shield * 100.0f) / 100.0f, x - (129+20 + move), y - 20, 0x808080); //0x000000 for black
        }
        else if(shield > 0) {
            int sizeX = 16;
            int sizeY = 16;
            int pictureSizeX = 16;
            int pictureSizeY = 16;
            RenderSystem.setShaderTexture(0, SHIELD);
            /*poseStack.pushPose();
            renderTextureFromCenter(poseStack,x - (130+20), y - 39,width,height,16,16,16,16,1);*/
            pose.blit(SHIELD, x - (130+20), y - 39, 0, 0, sizeX, sizeY,
                    pictureSizeX, pictureSizeY);
            double log10 = Math.log10(shield);
            int move = (int) Math.floor(log10) + 1;
            pose.drawString(fontRenderer,""+Math.round(shield * 100.0f) / 100.0f, x - (129+20 + move), y - 20, 0x808080);
            //fontRenderer.(poseStack, ""+Math.round(shield * 100.0f) / 100.0f, x - (129+20 + move), y - 20, 0x808080); //0x000000 for black
        }

        //94 54
        //90 60 высота выше брони, правее
        //92 42 высота ниже брони едва касается, левее на 2 пикселя
        //91 52 выше, 1 пиксель левее
        //90 35 слишком низко, 1 пиксель правее, что за хуйня
        //90 40 1 пиксель выше
        //90 39 идеальная высота
    });


    public static void renderTextureFromCenter(PoseStack matrix, float centerX, float centerY, float texOffX, float texOffY, float texWidth, float texHeight, float width, float height, float scale) {
        BufferBuilder builder = Tesselator.getInstance().getBuilder();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        matrix.pushPose();

        matrix.translate(centerX, centerY, 0);
        matrix.scale(scale, scale, scale);

        Matrix4f m = matrix.last().pose();

        float u1 = texOffX / texWidth;
        float u2 = (texOffX + width) / texWidth;
        float v1 = texOffY / texHeight;
        float v2 = (texOffY + height) / texHeight;

        float w2 = width / 2F;
        float h2 = height / 2F;

        builder.vertex(m, -w2, +h2, 0).uv(u1, v2).endVertex();
        builder.vertex(m, +w2, +h2, 0).uv(u2, v2).endVertex();
        builder.vertex(m, +w2, -h2, 0).uv(u2, v1).endVertex();
        builder.vertex(m, -w2, -h2, 0).uv(u1, v1).endVertex();

        matrix.popPose();

        BufferUploader.drawWithShader(builder.end());
    }

    public static ResourceLocation getTexture(int vp) {
        List<ResourceLocation> locations = new ArrayList<>();

        // Создание и добавление ResourceLocation в список
        locations.add(new ResourceLocation(MODID, "textures/item/anemoculus.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/crown.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/atlas.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/killer.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/mask.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/donut_static.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/mark.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/ears.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/midas.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/anomaly.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/armor.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/book.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/prism.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/chaos.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/devourer.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/flower.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/catalyst.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/ball.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/trigon.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/soulblighter.png"));

        return locations.get(vp-1);
    }
}
