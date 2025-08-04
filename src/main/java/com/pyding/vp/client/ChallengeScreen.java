package com.pyding.vp.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pyding.vp.item.MysteryChest;
import com.pyding.vp.mixin.SmitingMixing;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ButtonPressPacket;
import com.pyding.vp.util.ClientConfig;
import com.pyding.vp.util.ConfigHandler;
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
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ChallengeScreen extends Screen {

    private List<ItemStack> list = new ArrayList<>();
    private float time;
    private static final ResourceLocation COMMON_TEXTURE = new ResourceLocation("vp", "textures/item/common.png");
    int startY = 0;
    private Button zoomInButton;
    private Button zoomOutButton;
    private int challenge;
    Object[] data;

    public ChallengeScreen(int challenge, List<ItemStack> list, Object[] data) {
        super(Component.empty());
        this.challenge = challenge;
        this.list = list;
        this.data = data;
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
                button -> ClientConfig.COMMON.guiScaleChallenge.set(Math.min(2.0, ClientConfig.COMMON.guiScaleChallenge.get() + 0.1))
        );
        zoomOutButton = new ImageButton(
                right, top,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/zoom-out.png"),
                buttonSize, buttonSize,
                button -> ClientConfig.COMMON.guiScaleChallenge.set(Math.max(0.1, ClientConfig.COMMON.guiScaleChallenge.get() - 0.1))
        );
        this.addRenderableWidget(zoomInButton);
        this.addRenderableWidget(zoomOutButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        time += partialTicks * 0.05f;
        float scaleMultiplier = (float) (ClientConfig.COMMON.guiScaleChallenge.get()+0f);
        float scale = 1.5f*scaleMultiplier;
        int scaledItemSize = (int)(16 * scale);
        int itemSpacing = (int)((scaledItemSize+2) * scale);
        int lineSpacing = (int)((scaledItemSize*2+2) * scale);
        int textOffsetY = (int)(20 * scale);
        int startX = scaledItemSize;
        startY = scaledItemSize;

        Component component;
        if(challenge == 13)
            component = Component.translatable("vp.get." + challenge, ConfigHandler.COMMON.rareItemChance.get()*100+"%").append(Component.literal((String)data[1])).withStyle(ChatFormatting.GRAY);
        else component = Component.translatable("vp.get."+challenge).append(Component.literal((String)data[1])).withStyle(ChatFormatting.GRAY);
        if(challenge != 9)
            guiGraphics.drawCenteredString(this.font, component, this.width / 2, 10, 0xFFFFFF);
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        if(challenge == 9) {
            component = Component.translatable("vp.golden").withStyle(ChatFormatting.GRAY).append(Component.literal(data[0] + "%").append(Component.literal((String) data[1])).withStyle(ChatFormatting.GRAY));
            renderCategory(guiGraphics, component.getString(), ChatFormatting.GRAY,
                    list, startX, startY, screenWidth, scaledItemSize, itemSpacing, lineSpacing, textOffsetY,scale);
        }
        else renderCategory(guiGraphics, Component.translatable("vp.left").getString(), ChatFormatting.GRAY,
                list, startX, startY, screenWidth, scaledItemSize, itemSpacing, lineSpacing, textOffsetY,scale);
        startY += this.width/12;
        if (challenge == 24){
            guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("vp.tropic").getString(), startX, startY, ChatFormatting.BLUE.getColor());
            startY += this.width/30;
            guiGraphics.drawString(Minecraft.getInstance().font, (String) data[2], startX, startY, ChatFormatting.GRAY.getColor());
            startY += this.width/12;
            guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("vp.axolotl").getString(), startX, startY, ChatFormatting.BLUE.getColor());
            startY += this.width/30;
            guiGraphics.drawString(Minecraft.getInstance().font, (String) data[0], startX, startY, ChatFormatting.GRAY.getColor());
        }
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private void renderCategory(GuiGraphics guiGraphics, String title, ChatFormatting color,
                                List<ItemStack> items, int startX, int startY, int screenWidth,
                                int itemSize, int itemSpacing, int lineSpacing, int textOffsetY, float scale) {

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
            renderBack(guiGraphics,currentX+ (float) itemSize /2,currentY+ (float) itemSize /2,0,time,scale/2,itemSize);

            Component name = VPUtil.filterAndTranslate(stack.getDescriptionId(),color);
            if (stack.getItem() instanceof SmithingTemplateItem templateItem)
                name = ((SmitingMixing) templateItem).upgradeDescription();
            else if(stack.getItem() instanceof RecordItem recordItem)
                name = recordItem.getDisplayName();

            int maxWidth = (int)((itemSpacing - 8) * scale);
            List<FormattedCharSequence> lines = font.split(name, maxWidth);
            int textY = currentY + textOffsetY;
            for (FormattedCharSequence line : lines) {
                int textX = currentX + (itemSize/2) - (font.width(line)/2);
                guiGraphics.drawString(font, line, textX, textY, color.getColor());
                textY += font.lineHeight+2;
            }
            currentX += itemSize + itemSpacing;
        }
        this.startY = currentY;
    }

    public static void renderBack(GuiGraphics guiGraphics, float centerX, float centerY, float zOffset, float time, float baseScale, int size) {
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
                COMMON_TEXTURE,
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
    public void renderBackground(GuiGraphics guiGraphics) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0x80000000, 0x80000000);
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
