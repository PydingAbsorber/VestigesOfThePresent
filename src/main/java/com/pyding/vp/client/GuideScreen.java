package com.pyding.vp.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.item.MysteryChest;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ButtonPressPacket;
import com.pyding.vp.util.ClientConfig;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuideScreen extends Screen {

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
    private static final ResourceLocation FRAME = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/frame.png");
    int page = 0;
    boolean showEverything = false;
    int maxPages = 8;
    private Button nextPage;
    private Button prevPage;
    private Button showAll;

    public GuideScreen() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        super.init();
        int buttonSize = 32;
        int padding = 5;
        int center = this.width/2 - buttonSize;
        int top = this.height - padding - buttonSize;
        nextPage = new ImageButton(
                center + (buttonSize + padding)*3, top-this.height/8,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/point_r.png"),
                buttonSize, buttonSize,
                button -> {
                    page = Math.min(maxPages, page + 1);
                    showEverything = false;
                }
        );
        prevPage = new ImageButton(
                center - (buttonSize - padding)*3, top-this.height/8,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/point_l.png"),
                buttonSize, buttonSize,
                button -> {
                    page = Math.max(1, page - 1);
                    showEverything = false;
                }
        );
        showAll = new ImageButton(
                center + padding*3, top-this.height/8,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/button_all.png"),
                buttonSize, buttonSize,
                button -> showEverything = !showEverything
        );
        this.addRenderableWidget(nextPage);
        this.addRenderableWidget(prevPage);
        this.addRenderableWidget(showAll);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        int infoWidth = 240;
        int infoHeight = 220;
        int infoPadding = 20;
        if(page == 0)
            showEverything = true;
        if(showEverything){
            int marginX = infoPadding + 20;
            int marginY = infoPadding;
            int availableWidth = this.width - marginX * 2;
            int perRow = Math.max(1, availableWidth / (infoWidth + infoPadding));

            int start = showEverything ? 1 : page;
            int end = showEverything ? maxPages : page;

            Font font = this.font;
            for (int i = start; i <= end; i++) {
                int index = i - start;
                int col = index % perRow;
                int row = index / perRow;
                int x = marginX + col * (infoWidth + infoPadding);
                int y = marginY + row * (infoHeight + infoPadding);
                guiGraphics.blit(FRAME, x, y, 0, 0, infoWidth, infoHeight);
                Component comp = Component.translatable("vp.info." + i);
                List<net.minecraft.util.FormattedCharSequence> lines = font.split(comp, infoWidth - 2 * infoPadding);
                for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
                    int textX = x + infoPadding;
                    int textY = y + infoPadding + lineIndex * font.lineHeight + infoWidth/4;
                    guiGraphics.drawString(font, lines.get(lineIndex), textX, textY, ChatFormatting.GRAY.getColor());
                }
            }
        } else {

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
}
