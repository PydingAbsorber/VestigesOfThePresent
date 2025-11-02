package com.pyding.vp.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.entity.SillySeashell;
import com.pyding.vp.entity.VortexEntity;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.Vortex;
import com.pyding.vp.item.vestiges.Catalyst;
import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.util.ClientConfig;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static com.pyding.vp.VestigesOfThePresent.MODID;

public class ShieldOverlay {
    private static final ResourceLocation HEALDEBT = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/heal_debt.png");
    private static final ResourceLocation SHIELD = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/shield.png");
    private static final ResourceLocation OVER_SHIELD = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/overshield.png");
    private static final ResourceLocation HEAL1 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/heal1.png");
    private static final ResourceLocation HEAL2 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/heal2.png");
    private static final ResourceLocation HEAL3 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/heal3.png");
    private static final ResourceLocation ORCHESTRA = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/orchestra.png");
    private static final ResourceLocation NOTE1 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/note1.png");
    private static final ResourceLocation NOTE2 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/note2.png");
    private static final ResourceLocation NOTE3 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/note3.png");
    private static final ResourceLocation NOTE4 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/note4.png");
    private static final ResourceLocation NOTE5 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/note5.png");
    private static final ResourceLocation NOTE6 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/note6.png");
    private static final ResourceLocation NOTE7 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/note7.png");
    private static final ResourceLocation NOTE8 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/note8.png");
    private static final ResourceLocation SOUL = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/soul.png");
    private static final ResourceLocation ULT = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/ult.png");
    private static final ResourceLocation ULT1 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/ult1.png");
    private static final ResourceLocation ULT2 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/ult2.png");
    private static final ResourceLocation ULT3 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/ult3.png");
    private static final ResourceLocation ULT4 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/ult4.png");
    private static final ResourceLocation ULT5 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/ult5.png");
    private static final ResourceLocation ULT6 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/ult6.png");
    private static final ResourceLocation ULT7 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/ult7.png");
    private static final ResourceLocation ULT8 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/ult8.png");
    private static final ResourceLocation ULT9 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/ult9.png");
    private static final ResourceLocation ULT10 = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/ult10.png");

    public static ResourceLocation getNote(int number) {
        return switch (number) {
            case 1 -> NOTE1;
            case 2 -> NOTE2;
            case 3 -> NOTE3;
            case 4 -> NOTE4;
            case 5 -> NOTE5;
            case 6 -> NOTE6;
            case 7 -> NOTE7;
            case 8 -> NOTE8;
            default -> ORCHESTRA;
        };
    }

    public static ResourceLocation getUlt(int number) {
        return switch (number) {
            case 1 -> ULT1;
            case 2 -> ULT2;
            case 3 -> ULT3;
            case 4 -> ULT4;
            case 5 -> ULT5;
            case 6 -> ULT6;
            case 7 -> ULT7;
            case 8 -> ULT8;
            case 9 -> ULT9;
            case 10 -> ULT10;
            default -> ULT;
        };
    }

    @OnlyIn(Dist.CLIENT)
    public static final IGuiOverlay HUD_SHIELD = ((gui, pose, partialTick, width, height) -> {
        int x = width / 2;
        int y = height-6;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Player player = Minecraft.getInstance().player;
        Font fontRenderer = Minecraft.getInstance().font;
        List<ItemStack> vestiges = VPUtil.getVestigeList(player);
        int centerHeight = y - 230;
        if(vestiges.size() > 0){
            for(int i = 0; i < vestiges.size(); i++){
                if(vestiges.get(i).getItem() instanceof Vestige vestige){
                    ItemStack stack = vestiges.get(i);
                    int vestigeNumber = vestige.vestigeNumber;
                    if(vestigeNumber == 0)
                        continue;
                    pose.blit(getTexture(vestigeNumber),x+(130+i*40),y-22, 0, 0, 16, 16,
                            16, 16);
                    int currentChargeSpecial = stack.getOrCreateTag().getInt("VPCurrentChargeSpecial");
                    int currentChargeUltimate = stack.getOrCreateTag().getInt("VPCurrentChargeUltimate");
                    long timeUlt = stack.getOrCreateTag().getLong("VPTimeUlt");
                    pose.drawString(fontRenderer,""+currentChargeSpecial, x+(150+i*40),y-24, vestige.color.getColor());
                    pose.drawString(fontRenderer,""+currentChargeUltimate, x+(150+i*40),y-15, vestige.color.getColor());
                    //fontRenderer.draw(poseStack, ""+vestige.currentChargeSpecial, x+(150+i*40),y-24, vestige.color.getColor());
                    //fontRenderer.draw(poseStack, ""+vestige.currentChargeUltimate, x+(150+i*40),y-15, vestige.color.getColor());
                    String info = "";
                    if(vestigeNumber == 3){
                        info = String.valueOf(player.getPersistentData().getInt("VPGravity"));
                    }
                    if(vestigeNumber == 5 && vestige.isSpecialActive(stack)){
                        if(vestige.isStellar(vestiges.get(i)))
                            info = ((int)VPUtil.scalePower(VPUtil.missingHealth(player)*8,5,player) + "%");
                        else info = ((int)VPUtil.scalePower(VPUtil.missingHealth(player)*4,5,player) + "%");
                    }
                    if(vestigeNumber == 6){
                        info = String.valueOf((int)player.getPersistentData().getFloat("VPSaturation"));
                    }
                    if(vestigeNumber == 7){
                        info = String.valueOf(player.getPersistentData().getInt("VPMadness"));
                    }
                    if(vestigeNumber == 11){
                        info = String.valueOf((int)stack.getOrCreateTag().getFloat("VPArmor"));
                    }
                    if(vestigeNumber == 15){
                        info = String.valueOf(player.getPersistentData().getInt("VPDevourerHits"));
                    }
                    if(vestigeNumber == 16){
                        info = ((int)player.getPersistentData().getFloat("VPHealResFlower") + "%");
                    }
                    if(vestigeNumber == 17 && vestige.isStellar(vestiges.get(i)) && vestige instanceof Catalyst catalyst){
                        info = (player.getPersistentData().getInt("VPDebuffDefence") + "");
                    }
                    if(vestigeNumber == 19){
                        info = ((int)player.getPersistentData().getFloat("VPTrigonBonus") + "%");
                    }
                    if(vestigeNumber == 23){
                        info = (player.getPersistentData().getInt("VPLures") + "");
                    }
                    if(vestigeNumber == 24){
                        info = (player.getPersistentData().getInt("VPWhirlpool") + "");
                    }
                    if(vestigeNumber == 25){
                        info = (player.getPersistentData().getInt("VPArchdamage") + "");
                    }
                    String durationUlt = "";
                    String durationSpecial = "";
                    int[] ultTime = {1,2,4,8,12,13,20,22,26};
                    for(int id: ultTime){
                        if(id == vestigeNumber){
                            int number = Math.round(timeUlt-System.currentTimeMillis())/1000;
                            if(number > 0)
                                durationUlt = String.valueOf(number);
                        }
                    }
                    if(!info.isEmpty())
                        pose.drawString(fontRenderer,""+info, x+(132+i*40),y-33, vestige.color.getColor());
                    if(!durationSpecial.isEmpty())
                        pose.drawString(fontRenderer,""+durationSpecial, x+(132+i*40),y-33, 0x00BFFF);
                    if(!durationUlt.isEmpty())
                        pose.drawString(fontRenderer,""+durationUlt, x+(132+i*40),y-33, 0x9932CC);
                    info = "";
                    if(vestigeNumber == 15){
                        int show = player.getPersistentData().getInt("VPDevourerShow");
                        if(show > 0)
                            pose.drawString(fontRenderer,""+show, x+(132+i*40),y-43, 0x54717B);
                    }
                    if(vestigeNumber == 13){
                        int number = Math.round(timeUlt-System.currentTimeMillis())/1000;
                        if(number > 0)
                            info = String.valueOf(number);
                    }
                    if(vestigeNumber == 7){
                        int number = Math.round(timeUlt-System.currentTimeMillis())/1000;
                        if(number > 0)
                            info = String.valueOf(number);
                    }
                    if(vestigeNumber == 12){
                        int number = Math.round(timeUlt-System.currentTimeMillis())/1000;
                        if(number > 0)
                            info = String.valueOf(number);
                    }
                    if(!info.isEmpty())
                        pose.drawString(fontRenderer,""+info, x+(132+i*40),y-43, 0x9932CC);
                    //fontRenderer.draw(poseStack, ""+info, x+(132+i*40),y-30, vestige.color.getColor());
                    if(vestigeNumber == 22){
                        int sizeX = 20;
                        int sizeY = 20;
                        int pictureSizeX = 20;
                        int pictureSizeY = 20;
                        if(player.getPersistentData().getLong("VPOrchestra") > System.currentTimeMillis()){
                            long song = player.getPersistentData().getLong("VPOrchestra");
                            long duration = Math.round(song - System.currentTimeMillis()) / 1000;
                            RenderSystem.setShaderTexture(0, ORCHESTRA);
                            pose.blit(ORCHESTRA, x + (132 + i * 40), y - (70), 0, 0, sizeX, sizeY,
                                    pictureSizeX, pictureSizeY);
                            pose.drawString(fontRenderer, "" + duration, x + (132 + i * 33), y - (43), 0x9932CC);

                        } else {
                            for (int s = 1; s < 9; s++) {
                                long song = player.getPersistentData().getLong("VPLyra" + s);
                                long duration = Math.round(song - System.currentTimeMillis()) / 1000;
                                int moveY = 0;
                                int moveX = s * 20;
                                if (s > 4) {
                                    moveY = 30;
                                    moveX = (s-4) * 20;
                                }
                                int xPos = x - moveX + (183 + i * 40);
                                if (duration > 0) {
                                    RenderSystem.setShaderTexture(0, getNote(s));
                                    pose.blit(getNote(s), xPos, y - (70 + moveY), 0, 0, sizeX, sizeY,
                                            pictureSizeX, pictureSizeY);
                                    pose.drawString(fontRenderer, "" + duration, xPos, y - (43 + moveY), 0x9932CC);
                                }
                            }
                        }
                    }
                    int radiance = (int)(vestige.getRadiance(stack) / vestige.getMaxRadiance(stack) * 10);
                    if(currentChargeUltimate == vestige.ultimateCharges(stack))
                        radiance = 10;
                    RenderSystem.setShaderTexture(0, getUlt(radiance));
                    int size = 24;
                    pose.blit(getUlt(radiance), x+(126+i*40),y-14, 0, 0, size, size,
                            size, size);
                }
            }
        }

        float targetShield = player.getPersistentData().getFloat("VPRender1");
        float targetOverShield = player.getPersistentData().getFloat("VPRender2");
        float targetHealingDebt = player.getPersistentData().getFloat("VPRender3");
        String targetSoul = player.getPersistentData().getString("VPRender4");
        LivingEntity target = null;
        for(Object o : VPUtil.rayClass(Entity.class,player,3,20,true)){
            if(o instanceof LivingEntity livingEntity)
                target = livingEntity;
            if(o instanceof VortexEntity vortexEntity){
                String current = (vortexEntity.getPersistentData().getString("VPVortexList"));
                String max = "";
                for(ItemStack stack: Vortex.getItems()){
                    max += stack.getDescriptionId() + ",";
                }
                int currentNumber = 0;
                int maxNumber = 0;
                List<String> listMax = new ArrayList<>();
                List<String> listCurrent = new ArrayList<>();
                if(!current.isEmpty()) {
                    for (String name : current.split(",")) {
                        currentNumber++;
                        listCurrent.add(name.trim());
                    }
                }
                for(String name: max.split(",")) {
                    maxNumber++;
                    listMax.add(name.trim());
                }
                listMax.removeAll(listCurrent);
                pose.drawString(fontRenderer, currentNumber + " / " + maxNumber, x - 10, centerHeight +10, 0x9932CC);
                pose.drawString(fontRenderer, VPUtil.filterAndTranslate(listMax.toString(), ChatFormatting.LIGHT_PURPLE), x - 10, centerHeight +30, 0x9932CC);
                break;
            }
            if(o instanceof SillySeashell sillySeashell){
                int wave = sillySeashell.getPersistentData().getInt("VPWave");
                if(wave > 0)
                    pose.drawString(fontRenderer, Component.translatable("vp.wave",wave), x - 15, centerHeight - 60, 0xA699E6);
            }
        }
        if(targetOverShield > 0){
            int sizeX = 20;
            int sizeY = 20;
            int pictureSizeX = 20;
            int pictureSizeY = 20;
            RenderSystem.setShaderTexture(0, OVER_SHIELD);
            pose.blit(OVER_SHIELD, x - 10, centerHeight, 0, 0, sizeX, sizeY,
                    pictureSizeX, pictureSizeY);
            int move = (int) Math.floor(Math.log10(targetOverShield))*2 + 1;
            pose.drawString(fontRenderer, ""+Math.round(targetOverShield * 100.0f) / 100.0f, x - (10 + move), centerHeight - 9, 0x9932CC);
            if(targetShield > 0)
                pose.drawString(fontRenderer, ""+Math.round(targetShield * 100.0f) / 100.0f, x - (10 + move), centerHeight + 22, 0x808080);
        }
        else if(targetShield > 0) {
            int sizeX = 16;
            int sizeY = 16;
            int pictureSizeX = 16;
            int pictureSizeY = 16;
            RenderSystem.setShaderTexture(0, SHIELD);
            pose.blit(SHIELD, x - 8, centerHeight-3, 0, 0, sizeX, sizeY,
                    pictureSizeX, pictureSizeY);
            int move = (int) Math.floor(Math.log10(targetShield))*2 + 1;
            pose.drawString(fontRenderer,""+Math.round(targetShield * 100.0f) / 100.0f, x - (8 + move), centerHeight + 22, 0x808080);
        }
        if(targetHealingDebt > 0 && target != null){
            int sizeX = 16;
            int sizeY = 16;
            int pictureSizeX = 16;
            int pictureSizeY = 16;
            RenderSystem.setShaderTexture(0, HEALDEBT);
            pose.blit(HEALDEBT, x - (40), centerHeight-3, 0, 0, sizeX, sizeY,
                    pictureSizeX, pictureSizeY);
            int move = (int) Math.floor(Math.log10(targetHealingDebt))*2 + 1;
            pose.drawString(fontRenderer,(int)(targetHealingDebt/target.getMaxHealth()*100)+"%", x - (40+move), centerHeight + 22, 0xCE5858);
        }
        if(!targetSoul.isEmpty() && target != null && Integer.parseInt(targetSoul.split("/")[0]) < Integer.parseInt(targetSoul.split("/")[1])){
            int size = 32;
            RenderSystem.setShaderTexture(0, SOUL);
            pose.blit(SOUL, x + (32), centerHeight-7, 0, 0, size, size,
                    size, size);
            int move = (int) Math.floor(Math.log10(Integer.parseInt(targetSoul.split("/")[1])))*2 + 1;
            pose.drawString(fontRenderer,targetSoul, x + (32+move), centerHeight + 22, 0x808080);
        }

        if(player.isCreative() || player.isSpectator())
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
        float healDebt = VPUtil.getHealDebt(player);
        if(healDebt > 0){
            int sizeX = 16;
            int sizeY = 16;
            int pictureSizeX = 16;
            int pictureSizeY = 16;
            RenderSystem.setShaderTexture(0, HEALDEBT);
            pose.blit(HEALDEBT, x - (114), y - 43, 0, 0, sizeX, sizeY,
                    pictureSizeX, pictureSizeY);
            int move = (int) Math.floor(Math.log10(healDebt))*2 + 1;
            pose.drawString(fontRenderer,(int)(healDebt/player.getMaxHealth()*100)+"%", x - (117+ move) , y - 27, 0xCE5858);
        }
        float overShield = VPUtil.getOverShield(player); //x больше-левее, меньше-правее, y меньше-выше, больше-ниже
        float shield = VPUtil.getShield(player);
        int soul = VPUtil.getSoulIntegrity(player);
        int maxSoul = VPUtil.getMaxSoulIntegrity(player);
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
            int move = (int) Math.floor(Math.log10(overShield))*2 + 1;
            pose.drawString(fontRenderer, ""+Math.round(overShield * 100.0f) / 100.0f, x - (129+20 + move), y - 51, 0x9932CC);
            //fontRenderer.draw(poseStack, ""+Math.round(overShield * 100.0f) / 100.0f, x - (129+20 + move), y - 51, 0x9932CC); //0x000000 for black
            if(shield > 0)
                pose.drawString(fontRenderer, ""+Math.round(shield * 100.0f) / 100.0f, x - (129+20 + move), y - 20, 0x808080);
                //fontRenderer.draw(poseStack, ""+Math.round(shield * 100.0f) / 100.0f, x - (129+20 + move), y - 20, 0x808080); //0x000000 for black
        }
        else if(shield > 0) {
            int sizeX = 20;
            int sizeY = 20;
            int pictureSizeX = 20;
            int pictureSizeY = 20;
            RenderSystem.setShaderTexture(0, SHIELD);
            /*poseStack.pushPose();
            renderTextureFromCenter(poseStack,x - (130+20), y - 39,width,height,16,16,16,16,1);*/
            pose.blit(SHIELD, x - (130+20), y - 42, 0, 0, sizeX, sizeY,
                    pictureSizeX, pictureSizeY);
            int move = (int) Math.floor(Math.log10(shield))*2 + 1;
            pose.drawString(fontRenderer,""+Math.round(shield * 100.0f) / 100.0f, x - (129+20 + move), y - 20, 0x808080);
            //fontRenderer.(poseStack, ""+Math.round(shield * 100.0f) / 100.0f, x - (129+20 + move), y - 20, 0x808080); //0x000000 for black
        }
        if(ClientConfig.COMMON.renderSoulIntegrity.get() && ((soul < maxSoul && player.getPersistentData().getLong("VPSoulShow") > System.currentTimeMillis()) || VPUtil.hasVestige(ModItems.DEVOURER.get(),player) || VPUtil.hasVestige(ModItems.NIGHTMARE_DEVOURER.get(),player) || VPUtil.hasVestige(ModItems.SOULBLIGHTER.get(),player))){
            int size = 32;
            RenderSystem.setShaderTexture(0, SOUL);
            pose.blit(SOUL, x - (130+80), y - 50, 0, 0, size, size,
                    size, size);
            int move = (int) Math.floor(Math.log10(maxSoul))*2 + 1;
            pose.drawString(fontRenderer,soul+"/"+maxSoul, x - (129+80 + move), y - 20, 0x808080);
        }
    });

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
        locations.add(new ResourceLocation(MODID, "textures/item/rune_static.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/lyra.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/pearl_static.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/whirlpool_static.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/archlinx.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/treasure.png"));
        if(vp == 666)
            return new ResourceLocation(MODID, "textures/item/n_devourer.png");
        return locations.get(vp-1);
    }
}
