package com.pyding.vp.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pyding.vp.item.MysteryChest;
import com.pyding.vp.item.Vortex;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ButtonPressPacket;
import com.pyding.vp.util.ClientConfig;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
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

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class VortexScreen extends Screen {
    private float time;
    private static final ResourceLocation COMMON_TEXTURE = new ResourceLocation("vp", "textures/item/common.png");
    private static final ResourceLocation RARE_TEXTURE = new ResourceLocation("vp", "textures/item/rare.png");
    private static final ResourceLocation MYTHIC_TEXTURE = new ResourceLocation("vp", "textures/item/mythic.png");
    private static final ResourceLocation LEGENDARY_TEXTURE = new ResourceLocation("vp", "textures/item/legendary.png");
    int startY = 0;
    private Button zoomInButton;
    private Button zoomOutButton;

    public VortexScreen() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        super.init();
        int buttonSize = 32;
        int padding = 5;
        int right = this.width - padding - buttonSize;
        int top = this.height - padding - buttonSize;
        zoomInButton = new ImageButton(
                right - buttonSize - padding, top,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/zoom-in.png"),
                buttonSize, buttonSize,
                button -> ClientConfig.COMMON.guiScaleVortex.set(Math.min(2.0, ClientConfig.COMMON.guiScaleVortex.get() + 0.1))
        );
        zoomOutButton = new ImageButton(
                right, top,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/zoom-out.png"),
                buttonSize, buttonSize,
                button -> ClientConfig.COMMON.guiScaleVortex.set(Math.max(0.1, ClientConfig.COMMON.guiScaleVortex.get() - 0.1))
        );
        this.addRenderableWidget(zoomInButton);
        this.addRenderableWidget(zoomOutButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        time += partialTicks * 0.05f;
        float scaleMultiplier = (float) (ClientConfig.COMMON.guiScaleVortex.get()+0f);
        float scale = 1.5f*scaleMultiplier;
        int scaledItemSize = (int)(16 * scale);
        int itemSpacing = (int)((scaledItemSize+2) * scale);
        int lineSpacing = (int)((scaledItemSize*2+2) * scale);
        int textOffsetY = (int)(20 * scale);
        int startX = scaledItemSize;
        startY = scaledItemSize;
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        renderCategory(guiGraphics, "Items for Vortex:", ChatFormatting.GRAY,
                Vortex.getItems(), startX, startY, screenWidth, scaledItemSize, itemSpacing, lineSpacing, textOffsetY,scale,"common");
        startY += 15;
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private void renderCategory(GuiGraphics guiGraphics, String title, ChatFormatting color,
                                List<ItemStack> items, int startX, int startY, int screenWidth,
                                int itemSize, int itemSpacing, int lineSpacing, int textOffsetY, float scale, String rarity) {

        Font font = Minecraft.getInstance().font;
        int currentX = startX;
        int currentY = startY + (int)(10 * scale);
        int maxInRow = (screenWidth - startX - itemSize) / (itemSize + itemSpacing);

        guiGraphics.drawString(font, title, startX, startY, color.getColor());
        for(int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if(i > 0 && i % maxInRow == 0) {
                currentX = startX;
                currentY += lineSpacing;
            }
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate(currentX, currentY, 100);
            poseStack.scale(scale, scale, 1);
            guiGraphics.renderItem(stack, 0, 0);
            poseStack.popPose();
            renderBack(guiGraphics,currentX+ (float) itemSize /2,currentY+ (float) itemSize /2,0,rarity,time,scale/2,itemSize);
            Component name = VPUtil.filterAndTranslate(stack.getDescriptionId(),color);
            int maxWidth = (int)((itemSpacing - 8) * scale);
            List<FormattedCharSequence> lines = font.split(name, maxWidth);

            int textY = currentY + textOffsetY;
            for (FormattedCharSequence line : lines) {
                int textX = currentX + (itemSize/2) - (font.width(line)/2);
                guiGraphics.drawString(font, line, textX, textY, color.getColor());
                textY += font.lineHeight+2;
            }
            String count = "x" + stack.getCount();
            int countX = currentX + (itemSize/2) - (font.width(count)/2);
            guiGraphics.drawString(font, count, countX, textY, color.getColor());
            currentX += itemSize + itemSpacing;
        }
        this.startY = currentY;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0x80000000, 0x80000000);
    }

    public static void renderBack(GuiGraphics guiGraphics, float centerX, float centerY, float zOffset, String rarity, float time, float baseScale, int size) {
        ResourceLocation texture = switch (rarity) {
            case "legendary" -> LEGENDARY_TEXTURE;
            case "mythic" -> MYTHIC_TEXTURE;
            case "rare" -> RARE_TEXTURE;
            default -> COMMON_TEXTURE;
        };
        PoseStack poseStack = guiGraphics.pose();
        float scale = baseScale + Mth.sin(time * Mth.PI * 3) * 0.2f;
        float alpha = 1.0f + Mth.sin(time * Mth.PI * 3) * 0.3f;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
        poseStack.pushPose();
        poseStack.translate(centerX, centerY, zOffset);
        poseStack.scale(scale, scale, 1.0f);
        poseStack.mulPose(Axis.ZP.rotationDegrees(time * 240));
        guiGraphics.blit(
                texture,
                -size/2, -size/2,
                0, 0,
                size, size,
                size, size
        );
        poseStack.popPose();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if ((keyCode == GLFW.GLFW_KEY_E ||
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
