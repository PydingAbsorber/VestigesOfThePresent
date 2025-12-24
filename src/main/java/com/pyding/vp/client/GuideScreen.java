package com.pyding.vp.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.client.sounds.SoundRegistry;
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
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    int page = 1;
    boolean showEverything = false;
    int maxPages = 9;
    long time = 0;
    ItemStack stack;
    Button back;

    public GuideScreen() {
        super(Component.empty());
    }

    public GuideScreen(int page) {
        super(Component.empty());
        this.page = page;
    }

    public GuideScreen(int page, ItemStack stack) {
        super(Component.empty());
        this.page = page;
        this.stack = stack;
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
                center + (buttonSize - padding) * 3, top - this.height / 8,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/point_r.png"),
                buttonSize, buttonSize,
                button -> {
                    page = Math.min(maxPages, page + 1);
                    showEverything = false;
                    player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.BOOK_PAGE1.get(), SoundSource.NEUTRAL, 1f, 1, false);
                }
        );
        Button prevPage = new NiceButton(
                center - (buttonSize - padding) * 3, top - this.height / 8,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/point_l.png"),
                buttonSize, buttonSize,
                button -> {
                    page = Math.max(1, page - 1);
                    showEverything = false;
                    player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.BOOK_PAGE1.get(), SoundSource.NEUTRAL, 1f, 1, false);
                }
        );
        Button showAll = new NiceButton(
                center, top - this.height / 8,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/button_all.png"),
                buttonSize, buttonSize,
                button -> {
                    showEverything = !showEverything;
                    player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.BOOK_PAGE1.get(), SoundSource.NEUTRAL, 1f, 1, false);
                }
        );
        this.addRenderableWidget(nextPage);
        this.addRenderableWidget(prevPage);
        this.addRenderableWidget(showAll);
        time = System.currentTimeMillis();
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
        buttonSize = 78;
        back = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/back.png"),
                buttonSize, buttonSize,
                button -> Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new VestigeScreen(stack, getMinecraft().player)))
        );
        this.addRenderableWidget(back);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        double scale = ClientConfig.COMMON.guiScaleGuide.get();
        back.visible = !showEverything;
        if(!showEverything)
            scale *= 1.5f;
        int infoWidth = (int) (256*scale);
        int infoHeight = (int) (256*scale);
        int infoPadding = (int) (30*scale);
        if(showEverything){
            int marginX = infoPadding;
            int marginY = infoPadding;
            int availableWidth = this.width - marginX * 2;
            int perRow = Math.max(1, availableWidth / (infoWidth + infoPadding));
            Font font = this.font;
            for (int i = 1; i <= maxPages; i++) {
                int index = i - 1;
                int col = index % perRow;
                int row = index / perRow;
                int x = marginX + col * (infoWidth + infoPadding) + infoWidth/5;
                int y = marginY + row * (infoHeight + infoPadding);
                guiGraphics.blit(FRAME, x, y, 0, 0, infoWidth, infoHeight,infoWidth,infoHeight);
                Component comp = Component.translatable("vp.info." + i);
                List<net.minecraft.util.FormattedCharSequence> lines = font.split(comp, infoWidth - 2 * infoPadding);
                for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
                    int textX = x + infoPadding + infoWidth/25;
                    int textY = y + infoPadding + lineIndex * font.lineHeight + infoHeight/6;
                    guiGraphics.drawString(font, lines.get(lineIndex), textX, textY, ChatFormatting.GRAY.getColor());
                }
            }
        } else {
            Font font = this.font;
            int x = this.width/2 - infoWidth/2;
            int y = this.height/2 - infoHeight/2;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, 10);
            guiGraphics.blit(FRAME, x, y, 0, 0, infoWidth, infoHeight, infoWidth, infoHeight);


            if(stack != null){
                back.visible = true;
                back.setX((int) (x - back.getWidth()/2.8));
                back.setY(this.height/3);
            }

            Component comp = Component.translatable("vp.info." + page);
            List<net.minecraft.util.FormattedCharSequence> lines = font.split(comp, infoWidth - 2 * infoPadding);
            for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
                int textX = x + infoPadding + infoWidth/25;
                int textY = y + infoPadding + lineIndex * font.lineHeight + infoHeight/6;
                guiGraphics.drawString(font, lines.get(lineIndex), textX, textY, ChatFormatting.GRAY.getColor());
            }
            List<ResourceLocation> pic = new ArrayList<>();
            if(page == 2) {
                pic.add(HEALDEBT);
                pic.add(ShieldOverlay.getTexture(20));
                pic.add(ShieldOverlay.getTexture(11));
                pic.add(ShieldOverlay.getTexture(6));
                pic.add(ShieldOverlay.getTexture(5));
                pic.add(ShieldOverlay.getTexture(7));
                pic.add(ShieldOverlay.getTexture(14));
                pic.add(ShieldOverlay.getTexture(17));
            }
            if (page == 3) {
                pic.add(HEAL1);
                pic.add(HEAL2);
                pic.add(HEAL3);
            }
            if (page == 4) {
                pic.add(ShieldOverlay.getTexture(16));
                pic.add(ShieldOverlay.getTexture(21));
                pic.add(ShieldOverlay.getTexture(6));
                pic.add(ShieldOverlay.getTexture(22));
            }
            if(page == 5){
                pic.add(SHIELD);
            }
            if(page == 6){
                pic.add(OVER_SHIELD);
            }
            if (page == 7) {
                pic.add(ShieldOverlay.getTexture(19));
                pic.add(ShieldOverlay.getTexture(5));
                pic.add(ShieldOverlay.getTexture(18));
                pic.add(ShieldOverlay.getTexture(3));
                pic.add(ShieldOverlay.getTexture(2));
                pic.add(ShieldOverlay.getTexture(21));
                pic.add(ShieldOverlay.getTexture(11));
            }
            if(page == 8){
                pic.add(ShieldOverlay.getTexture(15));
                pic.add(ShieldOverlay.getTexture(20));
            }
            if(page == 9){
                pic.add(ShieldOverlay.getTexture(11));
            }
            if(!pic.isEmpty()){
                Random random = new Random(time+page);
                for(int i = 0; i < pic.size();i++) {
                    int xPos, yPos;
                    int size = (int) (64*scale);
                    xPos = this.width/2 - size/2;
                    yPos = this.height/2 - size/2 + infoHeight/4;
                    if(i != 0) {
                        int bound = 200;
                        if (random.nextDouble() < 0.5)
                            xPos += infoWidth / 3 + random.nextInt(bound/2)+bound/4;
                        else xPos -= infoWidth / 3 - random.nextInt(bound/2)+bound/4;
                        if (random.nextDouble() < 0.5)
                            yPos += infoHeight / 3 + random.nextInt(bound/2)+bound/4;
                        else yPos -= infoHeight - random.nextInt(bound/2)+bound/4;
                    }
                    guiGraphics.blit(pic.get(i), xPos, yPos, 0, 0, size, size, size, size);
                }
            }
            guiGraphics.pose().popPose();
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
