package com.pyding.vp.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pyding.vp.VestigesOfThePresent;
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
import net.minecraft.world.item.EggItem;
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

    private static final ResourceLocation FRAME = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/frame.png");
    private static final ResourceLocation SCROLL = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/scroll.png");
    private List<ItemStack> list = new ArrayList<>();
    private float time;
    private static final ResourceLocation COMMON_TEXTURE = new ResourceLocation("vp", "textures/item/common.png");
    int startY = 0;
    private Button zoomInButton;
    private Button zoomOutButton;
    private int challenge;
    Object[] data;
    Button back;
    private double scrollAmount = 0;
    private int totalContentHeight = 0;

    public ChallengeScreen(int challenge, List<ItemStack> list, Object[] data) {
        super(Component.empty());
        this.challenge = challenge;
        this.list = list;
        this.data = data;
    }

    ItemStack stack;

    public ChallengeScreen(int challenge, List<ItemStack> list, Object[] data, ItemStack stack) {
        super(Component.empty());
        this.challenge = challenge;
        this.list = list;
        this.data = data;
        this.stack = stack;
    }

    @Override
    protected void init() {
        super.init();
        int buttonSize = 32;
        int padding = 5;
        int right = this.width - padding - buttonSize;
        int top = this.height - padding - buttonSize;
        zoomInButton = new NiceButton(
                right - buttonSize - padding, top,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/zoom-in.png"),
                buttonSize, buttonSize,
                button -> ClientConfig.COMMON.guiScaleChallenge.set(Math.min(2.0, ClientConfig.COMMON.guiScaleChallenge.get() + 0.1))
        );
        zoomOutButton = new NiceButton(
                right, top,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/zoom-out.png"),
                buttonSize, buttonSize,
                button -> ClientConfig.COMMON.guiScaleChallenge.set(Math.max(0.1, ClientConfig.COMMON.guiScaleChallenge.get() - 0.1))
        );
        this.addRenderableWidget(zoomInButton);
        this.addRenderableWidget(zoomOutButton);
        buttonSize = 128;
        back = new NiceButton(
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
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 10);
        Component titleComponent;
        if(challenge == 13)
            titleComponent = Component.translatable("vp.get." + challenge, ConfigHandler.COMMON.rareItemChance.get()*100+"%").append(Component.literal((String)data[1])).withStyle(ChatFormatting.GRAY);
        else titleComponent = Component.translatable("vp.get."+challenge).append(Component.literal((String)data[1])).withStyle(ChatFormatting.GRAY);
        if(challenge != 9) {
            List<net.minecraft.util.FormattedCharSequence> lines = font.split(titleComponent, this.width / 2);
            for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
                int lineWidth = font.width(lines.get(lineIndex));
                int textX = (this.width / 2) - (lineWidth / 2);
                int textY = (int) (10 + lineIndex * font.lineHeight);
                guiGraphics.drawString(font, lines.get(lineIndex), textX, textY, ChatFormatting.GOLD.getColor());
            }
        }
        float scale = (float) (1.5f * ClientConfig.COMMON.guiScaleChallenge.get());
        int size = (int) (420*scale);
        int x = this.width/2 - size/2;
        int y = this.height/2 - size/2;
        guiGraphics.blit(FRAME, x, y, 0, 0, size, size,size,size);
        guiGraphics.blit(SCROLL, this.width/2 + size/2, this.height/2 - size/6 + 35, 0, 0, size/6, size/6,size/6,size/6);

        int scissorTop = (int) (y+size/5.3);
        int scissorBottom = (int) (scissorTop+size/1.5); //this.height - 70;
        guiGraphics.enableScissor(0, scissorTop, this.width, scissorBottom);

        int scaledItemSize = (int)(16 * scale);
        int itemSpacing = (int)((scaledItemSize + 2) * scale);
        int lineSpacing = (int)((scaledItemSize * 2 + 2) * scale);
        int textOffsetY = (int)(20 * scale);
        int startX = (int) (this.width/2 - (int) (160*scale));
        int currentStartY = (int) (this.height/1.85-size/2.95);
        int screenWidth = (int) (this.width/2-size*1.25);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, -scrollAmount, 0);
        time += partialTicks * 0.05f;

        if(data[2] instanceof String && list.isEmpty()){
            List<net.minecraft.util.FormattedCharSequence> lines = font.split(VPUtil.filterAndTranslate((String) data[2]), (int) (this.width/9.4+size/1.6));
            for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
                int textX = startX;
                int textY = (int) (scissorTop + font.lineHeight * 3 + lineIndex * font.lineHeight * 2);
                guiGraphics.drawString(font, lines.get(lineIndex), textX, textY, ChatFormatting.GRAY.getColor());
            }
        }

        /*List<Component> componentList = new ArrayList<>();
        componentList.add(VPUtil.filterAndTranslate((String) data[2]));
        renderDescription(guiGraphics,font,componentList,startX,scissorTop + font.lineHeight*3,screenWidth,scissorTop*2,ChatFormatting.GRAY.getColor());*/

        if(challenge == 9) {
            Component goldenComp = Component.translatable("vp.golden").withStyle(ChatFormatting.GRAY).append(Component.literal(data[0] + "%").append(Component.literal((String) data[1])).withStyle(ChatFormatting.GRAY));
            renderCategory(guiGraphics, goldenComp.getString(), ChatFormatting.GRAY, list, startX, currentStartY, screenWidth, scaledItemSize, itemSpacing, lineSpacing, textOffsetY, scale);
        } else {
            renderCategory(guiGraphics, Component.translatable("vp.left").getString(), ChatFormatting.GRAY, list, startX, currentStartY, screenWidth, scaledItemSize, itemSpacing, lineSpacing, textOffsetY, scale);
        }
        this.totalContentHeight = this.startY + 50;
        if (challenge == 24){
            int extraY = this.startY + this.width/12;
            guiGraphics.drawString(font, Component.translatable("vp.tropic").getString(), startX, extraY, ChatFormatting.BLUE.getColor());
            extraY += this.width/30;
            guiGraphics.drawString(font, (String) data[2], startX, extraY, ChatFormatting.GRAY.getColor());
            extraY += this.width/12;
            guiGraphics.drawString(font, Component.translatable("vp.axolotl").getString(), startX, extraY, ChatFormatting.BLUE.getColor());
            extraY += this.width/30;
            guiGraphics.drawString(font, (String) data[0], startX, extraY, ChatFormatting.GRAY.getColor());
            this.totalContentHeight = extraY + 40;
        }

        guiGraphics.pose().popPose();
        guiGraphics.disableScissor();
        if(stack != null){
            back.visible = true;
            back.setX(x - back.getWidth() + (int)(52 * scale));
            back.setY((int)(y+size/5.1));
        }
        guiGraphics.pose().popPose();
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private void renderDescription(GuiGraphics guiGraphics, Font font, List<Component> components, int x, int y, int maxWidth, int maxHeight, int color) {
        Minecraft mc = Minecraft.getInstance();
        Window window = mc.getWindow();
        int scale = (int) window.getGuiScale();
        RenderSystem.enableScissor(x * scale, (window.getGuiScaledHeight() - (y + maxHeight)) * scale, maxWidth * scale, maxHeight * scale);
        List<FormattedCharSequence> lines = new ArrayList<>();
        for (Component component : components) {
            lines.addAll(font.split(component, maxWidth));
        }
        int drawY = (int) (y - scrollAmount);
        for (FormattedCharSequence line : lines) {
            if (drawY + font.lineHeight >= y && drawY <= y + maxHeight) {
                guiGraphics.drawString(font, line, x, drawY, color);
            }
            drawY += font.lineHeight;
        }
        RenderSystem.disableScissor();
        guiGraphics.blit(SCROLL, (int) (x + maxWidth*0.95), (int) (y+maxHeight/3 - 20), 0, 0, 64, 64,64,64);
    }

    private void renderCategory(GuiGraphics guiGraphics, String title, ChatFormatting color,
                                List<ItemStack> items, int startX, int startY, int screenWidth,
                                int itemSize, int itemSpacing, int lineSpacing, int textOffsetY, float scale) {

        Font font = Minecraft.getInstance().font;
        int currentX = startX;
        int currentY = startY + (int)(10 * scale);
        int maxInRow = (screenWidth - startX - itemSize) / (itemSize + itemSpacing);
        if(maxInRow == 0)
            return;
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

            if(ClientConfig.COMMON.guiScaleChallenge.get() >= 0.8) {
                Component name = VPUtil.filterAndTranslate(stack.getDescriptionId(), color);
                if(!stack.getOrCreateTag().getString("EggName").isEmpty())
                    name = VPUtil.filterAndTranslate(stack.getOrCreateTag().getString("EggName"), color);
                if (stack.getItem() instanceof SmithingTemplateItem templateItem)
                    name = ((SmitingMixing) templateItem).upgradeDescription();
                else if (stack.getItem() instanceof RecordItem recordItem)
                    name = recordItem.getDisplayName();

                int maxWidth = (int) ((itemSpacing - 8) * scale);
                List<FormattedCharSequence> lines = font.split(name, maxWidth);
                int textY = currentY + textOffsetY;
                for (FormattedCharSequence line : lines) {
                    int textX = currentX + (itemSize / 2) - (font.width(line) / 2);
                    guiGraphics.drawString(font, line, textX, textY, color.getColor());
                    textY += font.lineHeight + 2;
                }
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

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        scrollAmount -= delta * 20;
        scrollAmount = Mth.clamp(scrollAmount, 0, Math.max(0, totalContentHeight - (this.height - 60)));
        return true;
    }
}
