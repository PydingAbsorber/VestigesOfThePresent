package com.pyding.vp.client;

import com.mojang.math.Axis;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ButtonPressPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public class MysteryChestScreen extends Screen {

    private float chestY;
    private float velocityY;
    private final float gravity = 6.0F;
    private final float groundY = 0F;
    private final float bounceDamping = 0.41f;
    private final ItemStack mysteryChestItem;
    private final ItemStack mysteryChestOpenItem;
    public static ItemStack drop;
    public static String rarity = "";
    public boolean firstDrop = false;
    float tick = 0;
    public static final int SIZE = 20;

    public MysteryChestScreen() {
        super(Component.literal("Feeling lucky today?!"));
        this.mysteryChestItem = new ItemStack(ModItems.MYSTERY_CHEST.get());
        this.mysteryChestOpenItem = new ItemStack(ModItems.MYSTERY_CHEST_OPEN.get());
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
        if(chestY == groundY)
            firstDrop = true;
    }



    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
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
        if(drop != null)
            guiGraphics.renderItem(mysteryChestOpenItem, 0, 0);
        else guiGraphics.renderItem(mysteryChestItem, 0, 0);
        poseStack.popPose();
        if(drop != null && Minecraft.getInstance().player != null){
            LocalPlayer player = Minecraft.getInstance().player;
            if(tick == 0)
                tick = player.tickCount;
            float localTick = player.tickCount - tick;
            scale = Math.min(10,3+localTick/2);
            /*if (localTick <= 3)
                scale += localTick*2;
            else if(localTick > 3 && localTick <= 6)
                scale = scale+(6-localTick*2);*/
            poseStack.pushPose();
            poseStack.translate(-20 - Math.min(60,localTick*2), -130 - Math.min(80,localTick*2) + chestY, 2000);
            /*if(localTick < 40)
                poseStack.mulPose(Axis.ZN.rotationDegrees(tick));*/
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
            poseStack.translate(-20 - Math.min(60,localTick*2), -130 - Math.min(80,localTick*2) + chestY, 1800);
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
}
