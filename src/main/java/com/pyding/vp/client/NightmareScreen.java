package com.pyding.vp.client;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.ClientConfig;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class NightmareScreen extends Screen {
    private static final ResourceLocation FRAME = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/frame.png");
    private static final ResourceLocation SHARD = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/item/shard.png");
    private static final ResourceLocation NIGHTMARE_SHARD = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/item/nightmare_shard.png");
    int page = 1;
    int maxPages = 8;

    public NightmareScreen() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        super.init();
        int buttonSize = 32;
        int padding = 5;
        int center = this.width/2 - buttonSize/2;
        int top = this.height - padding - buttonSize;
        LocalPlayer player = Minecraft.getInstance().player;
        Button nextPage = new NiceButton(
                center + (buttonSize - padding) * 2, top - this.height / 8,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/point_r.png"),
                buttonSize, buttonSize,
                button -> {
                    page = Math.min(maxPages, page + 1);
                    player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.BOOK_PAGE1.get(), SoundSource.RECORDS, 1f, 1, false);
                }
        );
        Button prevPage = new NiceButton(
                center - (buttonSize - padding) * 2, top - this.height / 8,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/point_l.png"),
                buttonSize, buttonSize,
                button -> {
                    page = Math.max(1, page - 1);
                    player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.BOOK_PAGE1.get(), SoundSource.RECORDS, 1f, 1, false);
                }
        );
        this.addRenderableWidget(nextPage);
        this.addRenderableWidget(prevPage);
        int right = this.width - padding - buttonSize;
        Button zoomInButton = new NiceButton(
                right - buttonSize - padding, top,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/zoom-in.png"),
                buttonSize, buttonSize,
                button -> ClientConfig.COMMON.guiScaleGuide.set(Math.min(2.0, ClientConfig.COMMON.guiScaleGuide.get() + 0.1))
        );
        Button zoomOutButton = new NiceButton(
                right, top,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/zoom-out.png"),
                buttonSize, buttonSize,
                button -> ClientConfig.COMMON.guiScaleGuide.set(Math.max(0.1, ClientConfig.COMMON.guiScaleGuide.get() - 0.1))
        );
        this.addRenderableWidget(zoomInButton);
        this.addRenderableWidget(zoomOutButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        double scale = ClientConfig.COMMON.guiScaleGuide.get();
        int infoWidth = (int) (512*scale);
        int infoHeight = (int) (512*scale);
        int infoPadding = (int) (60*scale);
        Font font = this.font;
        int x = this.width/2 - infoWidth/2;
        int y = this.height/2 - infoHeight/2;
        guiGraphics.blit(FRAME, x, y, 0, 0, infoWidth, infoHeight,infoWidth,infoHeight);
        Component comp = Component.translatable("vp.nightmare."+(page-1));
        List<net.minecraft.util.FormattedCharSequence> lines = font.split(comp, infoWidth - 2 * infoPadding);
        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            int textX = x + infoPadding + infoWidth/25;
            int textY = y + infoPadding + lineIndex * font.lineHeight + infoHeight/6;
            guiGraphics.drawString(font, lines.get(lineIndex), textX, textY, ChatFormatting.GRAY.getColor());
        }
        if(page == 1){
            int xPos, yPos;
            int size = (int) (64*scale);
            xPos = this.width/2 - size/2;
            yPos = this.height/2 - size/2 + infoHeight/4;
            guiGraphics.blit(SHARD, xPos-size, yPos, 0, 0, size, size, size, size);
            guiGraphics.blit(NIGHTMARE_SHARD, xPos+size, yPos, 0, 0, size, size, size, size);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
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
        player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.BOOK_CLOSE.get(), SoundSource.RECORDS, 1f, 1, false);
    }
}
