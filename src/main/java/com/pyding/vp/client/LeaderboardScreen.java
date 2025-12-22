package com.pyding.vp.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.ClientConfig;
import com.pyding.vp.util.GradientUtil;
import com.pyding.vp.util.LeaderboardUtil;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@OnlyIn(Dist.CLIENT)
public class LeaderboardScreen extends Screen {
    private static final ResourceLocation FRAME = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/leaderboard.png");
    private static final ResourceLocation SCROLL = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/scroll.png");
    long time = 0;
    String text = "";
    Button back;

    public LeaderboardScreen() {
        super(Component.empty());
    }

    ItemStack stack;

    public LeaderboardScreen(ItemStack stack) {
        super(Component.empty());
        this.stack = stack;
    }

    @Override
    protected void init() {
        super.init();
        double scale = ClientConfig.COMMON.guiScaleLeaderboard.get()+0.3;
        CompletableFuture.runAsync(() -> text = VPUtil.filterString(LeaderboardUtil.getAll()));
        int buttonSize = 32;
        int padding = 5;
        int top = this.height - padding - buttonSize;
        time = System.currentTimeMillis();
        int right = this.width - padding - buttonSize;
        Button zoomInButton = new ImageButton(
                right - buttonSize - padding, top,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/zoom-in.png"),
                buttonSize, buttonSize,
                button -> ClientConfig.COMMON.guiScaleLeaderboard.set(Math.min(2.0, ClientConfig.COMMON.guiScaleLeaderboard.get() + 0.1))
        );
        Button zoomOutButton = new ImageButton(
                right, top,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/zoom-out.png"),
                buttonSize, buttonSize,
                button -> ClientConfig.COMMON.guiScaleLeaderboard.set(Math.max(0.1, ClientConfig.COMMON.guiScaleLeaderboard.get() - 0.1))
        );
        this.addRenderableWidget(zoomInButton);
        this.addRenderableWidget(zoomOutButton);
        buttonSize = 128;
        back = new ImageButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/back.png"),
                buttonSize, buttonSize,
                button -> Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new VestigeScreen(stack, getMinecraft().player)))
        );
        this.addRenderableWidget(back);
        back.visible = false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        guiGraphics.pose().translate(0, 0, 10);
        double scale = ClientConfig.COMMON.guiScaleLeaderboard.get()+0.3;
        int infoWidth = (int) (512*scale);
        int infoHeight = (int) (512*scale);
        int x = this.width/2 - infoWidth/2;
        int y = this.height/2 - infoHeight/2;
        guiGraphics.blit(FRAME, x, y, 0, 0, infoWidth, infoHeight,infoWidth,infoHeight);
        guiGraphics.blit(SCROLL, this.width/2 + infoWidth/2, this.height/2 - infoHeight/6 + 35, 0, 0, infoWidth/4, infoHeight/4,infoWidth/4,infoHeight/4);

        String[] names = text.split(",");
        Font font = this.font;
        float textScale = 1.5f;
        int lineHeight = (int) ((font.lineHeight + 4) * textScale);
        int visibleAreaHeight = infoHeight - infoHeight/2 - 120;
        int totalHeight = names.length * lineHeight;
        int maxScroll = Math.max(0, totalHeight - visibleAreaHeight);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
        int textStartY = y + infoHeight/4 + 90;
        int textCenterX = x + infoWidth / 2;

        if(stack != null){
            back.visible = true;
            back.setX(x - back.getWidth() + (int)(58 * scale));
            back.setY(textStartY-64);
        }

        enableScissor( x + 10, y, infoWidth - 25, infoHeight - infoHeight/4 - 25);
        pose.pushPose();
        pose.scale(textScale, textScale, 1.0f);
        int startIndex = scrollOffset / lineHeight;
        int endIndex = Math.min(names.length, startIndex + (visibleAreaHeight / lineHeight) + 1);
        for (int i = startIndex; i < endIndex; i++) {
            String name = names[i].trim();
            int textWidth = font.width(name);
            int textX = (int) ((textCenterX - textWidth * textScale / 2) / textScale);
            int textY = (int) ((textStartY + (i * lineHeight) - scrollOffset) / textScale);
            if(i < 10)
                guiGraphics.drawString(font, GradientUtil.goldenGradient(name.replaceAll("^\"|\"$", "")), textX, textY, 0xFFFFFF, false);
            else guiGraphics.drawString(font, name, textX, textY, 0xFFFFFF, false);
        }
        pose.popPose();
        RenderSystem.disableScissor();
        pose.popPose();
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private int scrollOffset = 0;

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        int scrollSpeed = 15;
        scrollOffset -= delta * scrollSpeed;
        return true;
    }

    private void enableScissor(int x, int y, int width, int height) {
        double guiScale = Minecraft.getInstance().getWindow().getGuiScale();
        int screenHeight = Minecraft.getInstance().getWindow().getScreenHeight();
        RenderSystem.enableScissor(
                (int) (x * guiScale),
                (int) (screenHeight - (y + height) * guiScale),
                (int) (width * guiScale),
                (int) (height * guiScale)
        );
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0x80000000, 0x80000000);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_E ||
                keyCode == GLFW.GLFW_KEY_W ||
                keyCode == GLFW.GLFW_KEY_A ||
                keyCode == GLFW.GLFW_KEY_S ||
                keyCode == GLFW.GLFW_KEY_D ||
                keyCode == GLFW.GLFW_KEY_ESCAPE ||
                keyCode == GLFW.GLFW_KEY_SPACE) {
            this.onClose();
            return true;
        }
        return true;
    }

    @Override
    public void onClose() {
        super.onClose();
        LocalPlayer player = Minecraft.getInstance().player;
        //player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.BOOK_CLOSE.get(), SoundSource.RECORDS, 1f, 1, false);
    }
}
