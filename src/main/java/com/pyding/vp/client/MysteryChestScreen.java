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
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class MysteryChestScreen extends Screen {

    private float chestY;
    private float velocityY;
    private final float gravity = 6.0F;
    private final float groundY = 0F;
    private final float bounceDamping = 0.41f;
    private final ItemStack mysteryChestItem;
    public static ItemStack drop;
    public static String rarity = "";
    public boolean firstDrop = false;
    float tick = 0;
    public static final int SIZE = 20;

    public MysteryChestScreen() {
        super(Component.literal("Feeling lucky today?!"));
        this.mysteryChestItem = Minecraft.getInstance().player.getMainHandItem();
    }

    @Override
    protected void init() {
        super.init();
        this.chestY = -200f;
        this.velocityY = 0f;
        firstDrop = false;
        drop = null;
        tick = 0;
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
            chest.getOrCreateTag().putInt("VPOpen",(int) Math.min(3,localTick));
            scale = Math.min(10,3+localTick/2);
            poseStack.pushPose();
            float x = -20 - Math.min(80,localTick*2);
            float y = -60 - Math.min(160,localTick*4) + chestY;
            poseStack.translate(x, y, 2000);
            poseStack.scale(scale, scale, scale);
            guiGraphics.renderItem(drop, (int) (centerX/scale), (int) (centerY/scale));
            poseStack.popPose();

            ItemStack stack = switch (rarity) {
                case "legendary" -> new ItemStack(ModItems.LEGENDARY.get());
                case "mythic" -> new ItemStack(ModItems.MYTHIC.get());
                case "rare" -> new ItemStack(ModItems.RARE.get());
                default -> new ItemStack(ModItems.COMMON.get());
            };
            poseStack.pushPose();
            poseStack.translate(x, y, 1800);
            poseStack.scale(scale, scale, scale);
            guiGraphics.renderItem(stack, (int) (centerX/scale), (int) (centerY/scale));
            poseStack.popPose();
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
    }
}
