package com.pyding.vp.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.entity.SillySeashell;
import com.pyding.vp.entity.VortexEntity;
import com.pyding.vp.item.vestiges.Catalyst;
import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

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

    @OnlyIn(Dist.CLIENT)
    public static final IGuiOverlay HUD_SHIELD = ((gui, pose, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Player player = Minecraft.getInstance().player;
        Font fontRenderer = Minecraft.getInstance().font;
        List<ItemStack> vestiges = VPUtil.getVestigeList(player);
        int centerHeight = y - 230;
        /*if (player.getMainHandItem().getItem() instanceof Pearl || player.getOffhandItem().getItem() instanceof Pearl){
            int count = 0;
            for(Component component: VPUtil.getFishDropList(player)) {
                pose.drawString(fontRenderer, component, x + (132 + 80), y - (83-count*10),component.getStyle().getColor().getValue());
                count++;
            }
        }*/
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
                    ItemStack stack = vestiges.get(i);
                    int vestigeNumber = vestige.vestigeNumber;
                    if(vestigeNumber == 0)
                        continue;
                    renderImage(getTexture(vestigeNumber),x+(130+i*40),y-22, 0, 0, 16, 16,
                            16, 16,pose);
                    /*int currentChargeSpecial = player.getPersistentData().getInt("VPCharge"+vestigeNumber);
                    int currentChargeUltimate = player.getPersistentData().getInt("VPChargeUlt"+vestigeNumber);
                    long time = player.getPersistentData().getLong("VPTime"+vestigeNumber);
                    long timeUlt = player.getPersistentData().getLong("VPTimeUlt"+vestigeNumber);*/
                   /* if(player.getMainHandItem().getItem() instanceof Box)
                        player.sendSystemMessage(Component.literal("number " + vestigeNumber + " numbers" +currentChargeSpecial + " " + currentChargeUltimate + " Nbt:" + player.getPersistentData()));
                   */
                    int currentChargeSpecial = stack.getOrCreateTag().getInt("VPCurrentChargeSpecial");
                    int currentChargeUltimate = stack.getOrCreateTag().getInt("VPCurrentChargeUltimate");
                    long time = stack.getOrCreateTag().getLong("VPTime");
                    long timeUlt = stack.getOrCreateTag().getLong("VPTimeUlt");
                    fontRenderer.draw(pose,""+currentChargeSpecial, x+(150+i*40),y-24, vestige.color.getColor());
                    fontRenderer.draw(pose,""+currentChargeUltimate, x+(150+i*40),y-15, vestige.color.getColor());
                    //fontRenderer.draw(poseStack, ""+vestige.currentChargeSpecial, x+(150+i*40),y-24, vestige.color.getColor());
                    //fontRenderer.draw(poseStack, ""+vestige.currentChargeUltimate, x+(150+i*40),y-15, vestige.color.getColor());
                    String info = "";
                    if(vestigeNumber == 3){
                        info = String.valueOf(player.getPersistentData().getInt("VPGravity"));
                    }
                    if(vestigeNumber == 5 && VPUtil.getHealBonus(player) < 0){
                        if(vestige.isStellar(vestiges.get(i)))
                            info = ((int)VPUtil.missingHealth(player)*8 + "%");
                        else info = ((int)VPUtil.missingHealth(player)*4 + "%");
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
                    String durationUlt = "";
                    String durationSpecial = "";
                    if(vestigeNumber == 8){
                        int number = Math.round(timeUlt-System.currentTimeMillis())/1000;
                        if(number > 0)
                            durationUlt = String.valueOf(number);
                    }
                    if(vestigeNumber == 20){
                        int number = Math.round(time-System.currentTimeMillis())/1000;
                        if(number > 0)
                            durationSpecial = String.valueOf(number);
                    }
                    if(vestigeNumber == 2){
                        int number = Math.round(timeUlt-System.currentTimeMillis())/1000;
                        if(number > 0)
                            durationUlt = String.valueOf(number);
                    }
                    if(vestigeNumber == 1){
                        int number = Math.round(timeUlt-System.currentTimeMillis())/1000;
                        if(number > 0)
                            durationUlt = String.valueOf(number);
                    }
                    if(vestigeNumber == 12){
                        int number = Math.round(time-System.currentTimeMillis())/1000;
                        if(number > 0)
                            durationSpecial = String.valueOf(number);
                    }
                    if(vestigeNumber == 13){
                        int number = Math.round(time-System.currentTimeMillis())/1000;
                        if(number > 0)
                            durationSpecial = String.valueOf(number);
                    }
                    if(vestigeNumber == 4){
                        int number = Math.round(timeUlt-System.currentTimeMillis())/1000;
                        if(number > 0)
                            durationUlt = String.valueOf(number);
                    }
                    if(vestigeNumber == 22){
                        int number = Math.round(timeUlt-System.currentTimeMillis())/1000;
                        if(number > 0)
                            durationUlt = String.valueOf(number);
                    }
                    if(!info.isEmpty())
                        fontRenderer.draw(pose,""+info, x+(132+i*40),y-33, vestige.color.getColor());
                    if(!durationSpecial.isEmpty())
                        fontRenderer.draw(pose,""+durationSpecial, x+(132+i*40),y-33, 0x00BFFF);
                    if(!durationUlt.isEmpty())
                        fontRenderer.draw(pose,""+durationUlt, x+(132+i*40),y-33, 0x9932CC);
                    info = "";
                    if(vestigeNumber == 15){
                        int show = player.getPersistentData().getInt("VPDevourerShow");
                        if(show > 0)
                            fontRenderer.draw(pose,""+show, x+(132+i*40),y-43, 0x54717B);
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
                        fontRenderer.draw(pose,""+info, x+(132+i*40),y-43, 0x9932CC);
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
                            renderImage(ORCHESTRA, x + (132 + i * 40), y - (70), 0, 0, sizeX, sizeY,
                                    pictureSizeX, pictureSizeY,pose);
                            fontRenderer.draw(pose, "" + duration, x + (132 + i * 33), y - (43), 0x9932CC);

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
                                    renderImage(getNote(s), xPos, y - (70 + moveY), 0, 0, sizeX, sizeY,
                                            pictureSizeX, pictureSizeY,pose);
                                    fontRenderer.draw(pose, "" + duration, xPos, y - (43 + moveY), 0x9932CC);
                                }
                            }
                        }
                    }
                }
            }
        }

        float targetOverShield = 0;
        float targetShield = 0;
        for(LivingEntity entity: VPUtil.ray(player,3,50,true)){
            targetShield = VPUtil.getShield(entity);
            targetOverShield = VPUtil.getOverShield(entity);
            break;
        }
        for(Object o : VPUtil.rayClass(Entity.class,player,3,20,true)){
            if(o instanceof VortexEntity vortexEntity){
                String current = (vortexEntity.getPersistentData().getString("VPVortexList"));
                String max = (player.getPersistentData().getString("VPVortex"));
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
                fontRenderer.draw(pose, currentNumber + " / " + maxNumber, x - 10, centerHeight +10, 0x9932CC);
                fontRenderer.draw(pose, VPUtil.filterAndTranslate(listMax.toString(), ChatFormatting.LIGHT_PURPLE), x - 10, centerHeight +30, 0x9932CC);
                break;
            }
            if(o instanceof SillySeashell sillySeashell){
                int wave = sillySeashell.getPersistentData().getInt("VPWave");
                if(wave > 0)
                    fontRenderer.draw(pose, Component.translatable("vp.wave",wave), x - 15, centerHeight - 60, 0xA699E6);
            }
        }
        if(targetOverShield > 0){
            int sizeX = 20;
            int sizeY = 20;
            int pictureSizeX = 20;
            int pictureSizeY = 20;
            RenderSystem.setShaderTexture(0, OVER_SHIELD);
            renderImage(OVER_SHIELD, x - 10, centerHeight, 0, 0, sizeX, sizeY,
                    pictureSizeX, pictureSizeY,pose);
            double log10 = Math.log10(targetOverShield);
            int move = (int) Math.floor(log10) + 1;
            fontRenderer.draw(pose, ""+Math.round(targetOverShield * 100.0f) / 100.0f, x - (10 + move), centerHeight - 9, 0x9932CC);
            if(targetShield > 0)
                fontRenderer.draw(pose, ""+Math.round(targetShield * 100.0f) / 100.0f, x - (10 + move), centerHeight + 22, 0x808080);
        }
        else if(targetShield > 0) {
            int sizeX = 16;
            int sizeY = 16;
            int pictureSizeX = 16;
            int pictureSizeY = 16;
            RenderSystem.setShaderTexture(0, SHIELD);
            renderImage(SHIELD, x - 8, centerHeight-3, 0, 0, sizeX, sizeY,
                    pictureSizeX, pictureSizeY,pose);
            double log10 = Math.log10(targetShield);
            int move = (int) Math.floor(log10) + 1;
            fontRenderer.draw(pose,""+Math.round(targetShield * 100.0f) / 100.0f, x - (8 + move), centerHeight + 22, 0x808080);
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
                    renderImage(HEAL1, x - 90 + (i * sizeX - 1), y - 39, 0, 0, sizeX, sizeY,
                            pictureSizeX, pictureSizeY,pose);
                else if (healBonus <= 60)
                    renderImage(HEAL2, x - 90 + (i * sizeX - 1), y - 39, 0, 0, sizeX, sizeY,
                            pictureSizeX, pictureSizeY,pose);
                else renderImage(HEAL3, x - 90 + (i * sizeX - 1), y - 39, 0, 0, sizeX, sizeY,
                            pictureSizeX, pictureSizeY,pose);
            }
        }
        float healDebt = player.getPersistentData().getFloat("VPHealDebt");
        if(healDebt > 0){
            int sizeX = 16;
            int sizeY = 16;
            int pictureSizeX = 16;
            int pictureSizeY = 16;
            RenderSystem.setShaderTexture(0, HEALDEBT);
            renderImage(HEALDEBT, x - (114), y - 43, 0, 0, sizeX, sizeY,
                    pictureSizeX, pictureSizeY,pose);
            fontRenderer.draw(pose,(int)(healDebt/player.getMaxHealth()*100)+"%", x - (117), y - 27, 0xCE5858);
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
            renderImage(OVER_SHIELD, x - (132+20), y - 42, 0, 0, sizeX, sizeY,
                    pictureSizeX, pictureSizeY,pose);
            //GuiComponent.drawString(poseStack, fontRenderer,"666 "+shield,x - 110, y - 50, 0); same shit lol
            double log10 = Math.log10(overShield);
            int move = (int) Math.floor(log10) + 1;
            fontRenderer.draw(pose, ""+Math.round(overShield * 100.0f) / 100.0f, x - (129+20 + move), y - 51, 0x9932CC);
            //fontRenderer.draw(poseStack, ""+Math.round(overShield * 100.0f) / 100.0f, x - (129+20 + move), y - 51, 0x9932CC); //0x000000 for black
            if(shield > 0)
                fontRenderer.draw(pose, ""+Math.round(shield * 100.0f) / 100.0f, x - (129+20 + move), y - 20, 0x808080);
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
            renderImage(SHIELD, x - (130+20), y - 39, 0, 0, sizeX, sizeY,
                    pictureSizeX, pictureSizeY,pose);
            double log10 = Math.log10(shield);
            int move = (int) Math.floor(log10) + 1;
            fontRenderer.draw(pose,""+Math.round(shield * 100.0f) / 100.0f, x - (129+20 + move), y - 20, 0x808080);
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


    public static void renderImage(ResourceLocation texture, int x, int y, int a, int b, int c, int d, int e, int f, PoseStack poseStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        GuiComponent.blit(poseStack, x, y, a, b, c, d, e, f);
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
        locations.add(new ResourceLocation(MODID, "textures/item/rune_static.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/lyra.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/pearl_static.png"));
        locations.add(new ResourceLocation(MODID, "textures/item/whirlpool_static.png"));

        return locations.get(vp-1);
    }
}
