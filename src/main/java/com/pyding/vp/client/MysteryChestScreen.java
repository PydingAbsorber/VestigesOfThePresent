package com.pyding.vp.client;

import com.mojang.math.Axis;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.client.sounds.VPSoundUtil;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ButtonPressPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MysteryChestScreen extends Screen {

    private float chestY;
    private float velocityY;
    private final float gravity = 6.0F;
    private final float groundY = 0F;
    private final float bounceDamping = 0.41f;
    public static ItemStack drop;
    public static String rarity = "";
    public boolean firstDrop = false;
    float tick = 0;
    public static final int SIZE = 20;
    public static Map<ItemStack,String> randomItems = new HashMap<>();
    private float time;
    public boolean levitato = false;

    public MysteryChestScreen() {
        super(Component.literal("Feeling lucky today?!"));
    }

    @Override
    protected void init() {
        super.init();
        this.chestY = -200f;
        this.velocityY = 0f;
        firstDrop = false;
        drop = null;
        tick = 0;
        levitato = false;
        LocalPlayer player = Minecraft.getInstance().player;
        player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.CHEST_FALL.get(), SoundSource.MASTER, 1, 1, false);
        if(VPSoundUtil.bufferVolume == -1)
            player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.AMBIENT.get(), SoundSource.RECORDS, 0.2f, 1, false);
        player.getMainHandItem().getOrCreateTag().putInt("VPOpen",0);
        player.getPersistentData().putInt("VPSound",0);
        player.getPersistentData().putInt("VPSoundInc",10);
    }

    @Override
    public void tick() {
        super.tick();
        velocityY += gravity;
        chestY += velocityY;
        if (chestY > groundY) {
            chestY = groundY;
            firstDrop = true;
            velocityY = -velocityY * bounceDamping;
            if (Math.abs(velocityY) < 0.1f) {
                velocityY = 0f;
            }
        }
        if(chestY == groundY && drop == null) {
            firstDrop = true;
            if(new Random().nextDouble() < 0.02)
                this.velocityY = -24f;
        }
    }



    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        time += partialTick * 0.05f;
        LocalPlayer player = Minecraft.getInstance().player;
        ItemStack chest = player.getMainHandItem();
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        float scale = SIZE;
        var poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(-140, -180 + chestY, 100);
        poseStack.translate(centerX, centerY, 0);
        if(!firstDrop) {
            poseStack.mulPose(Axis.ZN.rotationDegrees(2));
        }
        poseStack.scale(scale, scale, scale);
        guiGraphics.renderItem(chest, 0, 0);
        poseStack.popPose();
        if(drop != null && Minecraft.getInstance().player != null){
            if(tick == 0)
                tick = player.tickCount;
            float localTick = player.tickCount - tick;
            if(localTick % 2 == 0)
                chest.getOrCreateTag().putInt("VPOpen",(int) Math.min(3,localTick/2));
            scale = 5;
            poseStack.pushPose();
            float x = -80;
            float y = -120 + chestY;
            float jumpDuration = 9.0f;
            float fallDuration = 6.0f;
            float yVelocity;
            float xVelocity;
            Random random = new Random((long)tick);
            float xMove = Math.min(jumpDuration+fallDuration+3,localTick)/10;
            if(random.nextDouble() < 0.5)
                xVelocity = Mth.lerp(Mth.sqrt(xMove), x, x - random.nextInt(this.width/3));
            else xVelocity = Mth.lerp(Mth.sqrt(xMove), x, x + random.nextInt(this.width/3));
            if(levitato)
                yVelocity = y + Mth.sin(time * Mth.TWO_PI) * 10;
            else if (localTick <= jumpDuration) {
                float progress = localTick / jumpDuration;
                yVelocity = Mth.lerp(Mth.sqrt(progress), y, y - 120);
            }
            else{ //if (localTick <= jumpDuration + fallDuration) {
                float progress = (localTick - jumpDuration) / fallDuration;
                float bottom = (float) this.height/2;
                yVelocity = Mth.lerp(progress * progress, y - 120, bottom);
                if(yVelocity >= bottom) {
                    progress = (localTick-(jumpDuration+fallDuration)) / jumpDuration;
                    yVelocity = Mth.lerp(Mth.sqrt(progress), bottom, y);
                    if(yVelocity >= (y - 120) && yVelocity <= y - 60){
                        levitato = true;
                        yVelocity = Mth.lerp(Mth.sqrt(progress), bottom,  y + Mth.sin(time * Mth.TWO_PI) * 10);
                    }
                }
            }
            poseStack.translate(xVelocity, yVelocity, 5000);
            poseStack.scale(scale, scale, scale);
            guiGraphics.renderItem(drop, (int) (centerX/scale), (int) (centerY/scale));
            poseStack.popPose();
            int size = 16;
            MysteryDropScreen.renderBack(guiGraphics,centerX+xVelocity+(size*scale)/2,centerY+yVelocity+(size*scale)/2,3200, rarity, time, scale, size);

            /// ////////////
            for(ItemStack stack: randomItems.keySet()){
                poseStack.pushPose();
                random = new Random(stack.getDescriptionId().hashCode());
                if(random.nextDouble() < 0.5)
                    xVelocity = Mth.lerp(Mth.sqrt(localTick/10), x, x - random.nextInt(this.width/3));
                else xVelocity = Mth.lerp(Mth.sqrt(localTick/10), x, x + random.nextInt(this.width/3));
                String rarity = randomItems.get(stack);
                if(tick == 0)
                    tick = player.tickCount;
                if (localTick <= jumpDuration) {
                    float progress = localTick / jumpDuration;
                    yVelocity = Mth.lerp(Mth.sqrt(progress), y, y - 120);
                }
                else {
                    float progress = (localTick - jumpDuration) / fallDuration;
                    yVelocity = Mth.lerp(progress * progress, y - 120, this.height);
                }
                yVelocity *= 1 + random.nextFloat(0.2f);
                poseStack.translate(xVelocity, yVelocity, 5000);
                poseStack.scale(scale, scale, scale);
                guiGraphics.renderItem(stack, (int) (centerX/scale), (int) (centerY/scale));
                poseStack.popPose();
                MysteryDropScreen.renderBack(guiGraphics,centerX+xVelocity+(size*scale)/2,centerY+yVelocity+(size*scale)/2,3200, rarity, time, scale, size);
            }

            if (localTick >= 80)
                this.onClose();
        }
        guiGraphics.drawCenteredString(this.font, this.title.getString(), this.width / 2, 10, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (chestY == groundY && drop == null) {
            int chestSize = SIZE*15;
            int centerX = this.width / 2;
            int centerY = this.height / 2;
            int left = centerX - chestSize / 2;
            int top = (int) (centerY - chestSize / 2 + chestY);
            int right = left + chestSize;
            int bottom = top + chestSize;
            if (mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom) {
                this.velocityY = -24f;
                PacketHandler.sendToServer(new ButtonPressPacket(6665242));
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (drop == null && (keyCode == GLFW.GLFW_KEY_E ||
                keyCode == GLFW.GLFW_KEY_W ||
                keyCode == GLFW.GLFW_KEY_A ||
                keyCode == GLFW.GLFW_KEY_S ||
                keyCode == GLFW.GLFW_KEY_D ||
                keyCode == GLFW.GLFW_KEY_ESCAPE ||
                keyCode == GLFW.GLFW_KEY_SPACE)) {
            this.onClose();
            return true;
        }
        return true;
    }

    @Override
    public void onClose() {
        super.onClose();
        LocalPlayer player = Minecraft.getInstance().player;
        player.getMainHandItem().getOrCreateTag().putInt("VPOpen",0);
        player.getPersistentData().putInt("VPSound",10);
        player.getPersistentData().putInt("VPSoundInc",0);
    }
}
