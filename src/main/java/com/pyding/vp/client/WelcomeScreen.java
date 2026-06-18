package com.pyding.vp.client;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.capability.VestigeCap;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.SendClientDataToServerPacket;
import com.pyding.vp.util.ClientConfig;
import com.pyding.vp.util.GradientUtil;
import com.pyding.vp.util.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class WelcomeScreen extends Screen {
    private static final ResourceLocation FRAME = ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "textures/gui/frame.png");
    private static final ResourceLocation SCROLL = ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "textures/gui/scroll.png");
    private static final ResourceLocation STELLAR = ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "textures/item/stellar.png");

    private Button zoomInButton;
    private Button zoomOutButton;
    private double scrollAmount = 0;
    private int totalContentHeight = 0;
    private int scissorHeight = 0;
    private Button choseButton1;
    private Button choseButton2;
    private Button choseButton3;
    private Button choseButton4;
    int challengeDifficulty = 3;
    private Button choseButton11;
    private Button choseButton12;
    private Button choseButton13;
    int vestigePower = 2;
    private Button choseButton31;
    private Button choseButton32;
    private Button choseButton33;
    private Button choseButton34;
    int worldDifficulty = 1;
    private Button exit;

    public WelcomeScreen() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        super.init();
        int buttonSize = 32;
        int padding = 5;
        int right = this.width - padding - buttonSize;
        int top = this.height - padding - buttonSize;
        Player player = Minecraft.getInstance().player;
        zoomInButton = new NiceButton(
                right - buttonSize - padding, top,
                buttonSize, buttonSize,
                0, 0, 0,
                ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/zoom-in.png"),
                buttonSize, buttonSize,
                button -> ClientConfig.guiScaleWelcome.set(Math.min(2.0, ClientConfig.guiScaleWelcome.get() + 0.1))
        );
        zoomOutButton = new NiceButton(
                right, top,
                buttonSize, buttonSize,
                0, 0, 0,
                ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/zoom-out.png"),
                buttonSize, buttonSize,
                button -> ClientConfig.guiScaleWelcome.set(Math.max(0.1, ClientConfig.guiScaleWelcome.get() - 0.1))
        );
        this.addRenderableWidget(zoomInButton);
        this.addRenderableWidget(zoomOutButton);
        buttonSize = 86;
        choseButton1 = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/info_button.png"),
                buttonSize, buttonSize,
                button -> {
                    challengeDifficulty = 1;
                    if(worldDifficulty == 3)
                        worldDifficulty = 1;
                    else if(worldDifficulty == 4)
                        worldDifficulty = 2;
                }
        );
        this.addWidget(choseButton1);
        choseButton2 = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/info_button.png"),
                buttonSize, buttonSize,
                button -> {
                    challengeDifficulty = 2;
                    if(worldDifficulty == 3)
                        worldDifficulty = 1;
                    else if(worldDifficulty == 4)
                        worldDifficulty = 2;
                }
        );
        this.addWidget(choseButton2);
        choseButton3 = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/info_button.png"),
                buttonSize, buttonSize,
                button -> challengeDifficulty = 3
        );
        this.addWidget(choseButton3);
        choseButton4 = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/info_button.png"),
                buttonSize, buttonSize,
                button -> challengeDifficulty = 4
        );
        this.addWidget(choseButton4);
        choseButton11 = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/info_button.png"),
                buttonSize, buttonSize,
                button -> vestigePower = 1
        );
        this.addWidget(choseButton11);
        choseButton12 = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/info_button.png"),
                buttonSize, buttonSize,
                button -> vestigePower = 2
        );
        this.addWidget(choseButton12);
        choseButton13 = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/info_button.png"),
                buttonSize, buttonSize,
                button -> vestigePower = 3
        );
        this.addWidget(choseButton13);
        choseButton31 = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/info_button.png"),
                buttonSize, buttonSize,
                button -> worldDifficulty = 1
        );
        this.addWidget(choseButton31);
        choseButton32 = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/info_button.png"),
                buttonSize, buttonSize,
                button -> worldDifficulty = 2
        );
        this.addWidget(choseButton32);
        choseButton33 = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/info_button.png"),
                buttonSize, buttonSize,
                button -> {
                    if(player != null && !player.isCreative()) {
                        worldDifficulty = 3;
                        if(challengeDifficulty < 3)
                            challengeDifficulty = 3;
                    }
                }
        );
        this.addWidget(choseButton33);
        choseButton34 = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/info_button.png"),
                buttonSize, buttonSize,
                button -> {
                    if(player != null && !player.isCreative()) {
                        worldDifficulty = 4;
                        if(challengeDifficulty < 3)
                            challengeDifficulty = 3;
                    }
                }
        );
        this.addWidget(choseButton34);
        exit = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/info_button.png"),
                buttonSize, buttonSize,
                button -> onClose()
        );
        this.addWidget(exit);
        choseButton1.setTooltip(Tooltip.create(Component.translatable("vp.chdif.easy").withStyle(ChatFormatting.GREEN)));
        choseButton2.setTooltip(Tooltip.create(Component.translatable("vp.chdif.normal").withStyle(ChatFormatting.GOLD)));
        choseButton3.setTooltip(Tooltip.create(Component.translatable("vp.chdif.hard").withStyle(ChatFormatting.RED)));
        choseButton4.setTooltip(Tooltip.create(Component.translatable("vp.chdif.original").withStyle(ChatFormatting.DARK_PURPLE)));
        choseButton11.setTooltip(Tooltip.create(Component.translatable("vp.vpower.weak").withStyle(ChatFormatting.GREEN)));
        choseButton12.setTooltip(Tooltip.create(Component.translatable("vp.vpower.normal").withStyle(ChatFormatting.GOLD)));
        choseButton13.setTooltip(Tooltip.create(Component.translatable("vp.vpower.strong").withStyle(ChatFormatting.RED)));
        choseButton31.setTooltip(Tooltip.create(Component.translatable("vp.worldfid.default").withStyle(ChatFormatting.GREEN)));
        choseButton32.setTooltip(Tooltip.create(Component.translatable("vp.worldfid.cruel").withStyle(ChatFormatting.RED).append(Component.literal("\n§7You take additional Paragon Damage from your max health when you take drowning, lava, starving and void damage.\n§7All bosses max hp is §cx" + ServerConfig.bossHP.get() + " §7and attack is §cx" + ServerConfig.bossHP.get() + " §7armor and armor toughness is §cx" + ServerConfig.bossHP.get() + " \n§7All bosses now have Shields from max hp percent §cx" + ServerConfig.shieldCruel.get() + " §7and Over Shields §cx" + ServerConfig.overShieldCruel.get() + " \n§7All bosses now are also Healing §c" + ServerConfig.bossHP.get() +"% §7from max hp per second.\nAll bosses also have DPS cap from max health §c" + ServerConfig.absorbCruel.get()*100 + "%" + " that can be exceeded by Vestige's Passive/Special/Ultimate damage by x2/x4/x6. \nAll monsters also have x" + ServerConfig.healthBoost.get() + " max health and chance to spawn with random armor."))));
        choseButton33.setTooltip(Tooltip.create(Component.translatable("vp.worldfid.leaderboard").withStyle(ChatFormatting.RED)));
        choseButton34.setTooltip(Tooltip.create(Component.translatable("vp.worldfid.leaderboard_cruel").withStyle(ChatFormatting.DARK_PURPLE)));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics,mouseX,mouseY,partialTicks);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(-15, 0, 40);
        double scale = ClientConfig.guiScaleWelcome.get();
        int size = (int) (420 * scale);
        int x = this.width / 2 - size / 2;
        int y = (int) (this.height / 2 - size / 2);
        guiGraphics.blit(FRAME, x, y, 0, 0, size, size, size, size);
        guiGraphics.blit(SCROLL, this.width / 2 + size / 2, this.height / 2 - size / 6 + 35, 0, 0, size / 6, size / 6, size / 6, size / 6);
        int scissorTop = (int) (y + size / 4.0);
        this.scissorHeight = (int) (size / 1.7);
        int scissorBottom = scissorTop + this.scissorHeight;
        guiGraphics.enableScissor(0, scissorTop, this.width, scissorBottom);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, -scrollAmount, 0);
        int currentY = scissorTop + 10;

        List<Component> list = new ArrayList<>();
        list.add(Component.translatable("vp.welcome.1"));
        list.add(Component.translatable("vp.welcome.2"));
        list.add(Component.translatable("vp.welcome.3"));
        list.add(Component.translatable("vp.welcome.4"));
        list.add(Component.literal("========================================================"));
        list.add(Component.translatable("vp.welcome.5"));
        list.add(Component.translatable("vp.welcome.6"));
        int number = 0;
        int secondColor = 0x592340;
        for (Component component: list) {
            if(number == 0)
                component = GradientUtil.stellarGradient(component.getString());
            number++;
            List<FormattedCharSequence> lines = font.split(component, (int) (size * 0.85));
            for (FormattedCharSequence line : lines) {
                int lineWidth = font.width(line);
                int textX = (this.width / 2) - (lineWidth / 2);
                int mainColor = 0xFFB673;
                if(number > 1) {
                    guiGraphics.drawString(font, line, textX - 1, currentY, secondColor, false);
                    guiGraphics.drawString(font, line, textX + 1, currentY, secondColor, false);
                    guiGraphics.drawString(font, line, textX, currentY - 1, secondColor, false);
                    guiGraphics.drawString(font, line, textX, currentY + 1, secondColor, false);
                }
                if(number >= 5)
                    mainColor = 0xC5B4E3;
                guiGraphics.drawString(font, line, textX, currentY, mainColor, false); //0xC5B4E3   0xFFB673
                currentY += font.lineHeight + 2;
            }
            currentY += 10;
        }
        int iconSize = 24;
        int stellarX = -24;
        int stellarY = -8;

        int btnWidth = choseButton1.getWidth();
        int btnHeight = choseButton1.getHeight();
        currentY -= btnHeight/4;
        int spacing = 10;
        int totalRowWidth = (btnWidth * 4) + (spacing * 3);
        int startRowX = x + (size / 2) - (totalRowWidth / 2) + 10;
        int actualYForMouse = currentY;
        int b1X = startRowX;
        choseButton1.setX(b1X);
        choseButton1.setY(currentY - (int)scrollAmount);
        int backupY1 = choseButton1.getY();
        choseButton1.setY(currentY);
        choseButton1.render(guiGraphics, mouseX, mouseY + (int)scrollAmount, partialTicks);
        choseButton1.setY(backupY1);

        guiGraphics.pose().translate(0,0,40);
        String t1 = "Easy";
        guiGraphics.drawString(font, t1, b1X + (btnWidth / 2) - (font.width(t1) / 2), currentY + (btnHeight / 2) - (font.lineHeight / 2), 0x78BE21, false);
        if (challengeDifficulty == 1) {
            guiGraphics.blit(STELLAR, b1X + (btnWidth / 2) - (font.width(t1) / 2) + stellarX, currentY + (btnHeight / 2) - (font.lineHeight / 2) + stellarY, 0, 0, iconSize, iconSize, iconSize, iconSize);
        }

        guiGraphics.pose().translate(0,0,-40);
        int b2X = startRowX + btnWidth + spacing;
        choseButton2.setX(b2X);
        choseButton2.setY(currentY - (int)scrollAmount);
        int backupY2 = choseButton2.getY();
        choseButton2.setY(currentY);
        choseButton2.render(guiGraphics, mouseX, mouseY + (int)scrollAmount, partialTicks);
        choseButton2.setY(backupY2);
        guiGraphics.pose().translate(0,0,40);
        String t2 = "Normal";
        guiGraphics.drawString(font, t2, b2X + (btnWidth / 2) - (font.width(t2) / 2), currentY + (btnHeight / 2) - (font.lineHeight / 2), 0xFC4C02, false);
        if (challengeDifficulty == 2) {
            guiGraphics.blit(STELLAR, b2X + (btnWidth / 2) - (font.width(t2) / 2)+ stellarX, currentY + (btnHeight / 2) - (font.lineHeight / 2)+ stellarY, 0, 0, iconSize, iconSize, iconSize, iconSize);
        }

        guiGraphics.pose().translate(0,0,-40);
        int b3X = startRowX + (btnWidth + spacing) * 2;
        choseButton3.setX(b3X);
        choseButton3.setY(currentY - (int)scrollAmount);
        int backupY3 = choseButton3.getY();
        choseButton3.setY(currentY);
        choseButton3.render(guiGraphics, mouseX, mouseY + (int)scrollAmount, partialTicks);
        choseButton3.setY(backupY3);
        guiGraphics.pose().translate(0,0,40);
        String t3 = "Hard";
        guiGraphics.drawString(font, t3, b3X + (btnWidth / 2) - (font.width(t3) / 2), currentY + (btnHeight / 2) - (font.lineHeight / 2), 0xE10600, false);
        if (challengeDifficulty == 3) {
            guiGraphics.blit(STELLAR, b3X + (btnWidth / 2) - (font.width(t3) / 2)+ stellarX, currentY + (btnHeight / 2) - (font.lineHeight / 2)+ stellarY, 0, 0, iconSize, iconSize, iconSize, iconSize);
        }

        guiGraphics.pose().translate(0,0,-40);
        int b4X = startRowX + (btnWidth + spacing) * 3;
        choseButton4.setX(b4X);
        choseButton4.setY(currentY - (int)scrollAmount);
        int backupY4 = choseButton4.getY();
        choseButton4.setY(currentY);
        choseButton4.render(guiGraphics, mouseX, mouseY + (int)scrollAmount, partialTicks);
        choseButton4.setY(backupY4);
        guiGraphics.pose().translate(0,0,40);
        String t4 = "Original";
        guiGraphics.drawString(font, t4, b4X + (btnWidth / 2) - (font.width(t4) / 2), currentY + (btnHeight / 2) - (font.lineHeight / 2), 0x8031A7, false);
        currentY += btnHeight;
        if (challengeDifficulty == 4) {
            guiGraphics.blit(STELLAR, b4X + (btnWidth / 2) - (font.width(t4) / 2)+ stellarX, currentY + (btnHeight / 2) - (font.lineHeight / 2)+ stellarY - btnHeight, 0, 0, iconSize, iconSize, iconSize, iconSize);
        }

        Component component = Component.translatable("vp.welcome.7");
        int textX = (this.width / 2) - (component.getString().length()*2);
        guiGraphics.drawString(font, component, textX, currentY, 0xC5B4E3, false);
        List<FormattedCharSequence> lines = font.split(component, (int) (size * 0.85));
        for (FormattedCharSequence line : lines) {
            int mainColor = 0xFFB673;
            if(number > 1) {
                guiGraphics.drawString(font, line, textX - 1, currentY, secondColor, false);
                guiGraphics.drawString(font, line, textX + 1, currentY, secondColor, false);
                guiGraphics.drawString(font, line, textX, currentY - 1, secondColor, false);
                guiGraphics.drawString(font, line, textX, currentY + 1, secondColor, false);
            }
            if(number >= 5)
                mainColor = 0xC5B4E3;
            guiGraphics.drawString(font, line, textX, currentY, mainColor, false);
            currentY += font.lineHeight + 2;
        }
        currentY += font.lineHeight;

        guiGraphics.pose().translate(0,0,-40);
        choseButton11.setX(b1X);
        choseButton11.setY(currentY - (int)scrollAmount);
        int backupY11 = choseButton11.getY();
        choseButton11.setY(currentY);
        choseButton11.render(guiGraphics, mouseX, mouseY + (int)scrollAmount, partialTicks);
        choseButton11.setY(backupY11);
        guiGraphics.pose().translate(0,0,40);
        t1 = "Weak";
        guiGraphics.drawString(font, t1, b1X + (btnWidth / 2) - (font.width(t1) / 2), currentY + (btnHeight / 2) - (font.lineHeight / 2), 0x78BE21, false);
        if (vestigePower == 1) {
            guiGraphics.blit(STELLAR, b1X + (btnWidth / 2) - (font.width(t1) / 2) + stellarX, currentY + (btnHeight / 2) - (font.lineHeight / 2) + stellarY, 0, 0, iconSize, iconSize, iconSize, iconSize);
        }

        guiGraphics.pose().translate(0,0,-40);
        choseButton12.setX(b2X);
        choseButton12.setY(currentY - (int)scrollAmount);
        int backupY12 = choseButton12.getY();
        choseButton12.setY(currentY);
        choseButton12.render(guiGraphics, mouseX, mouseY + (int)scrollAmount, partialTicks);
        choseButton12.setY(backupY12);
        guiGraphics.pose().translate(0,0,40);
        t2 = "Normal";
        guiGraphics.drawString(font, t2, b2X + (btnWidth / 2) - (font.width(t2) / 2), currentY + (btnHeight / 2) - (font.lineHeight / 2), 0xFC4C02, false);
        if (vestigePower == 2) {
            guiGraphics.blit(STELLAR, b2X + (btnWidth / 2) - (font.width(t2) / 2)+ stellarX, currentY + (btnHeight / 2) - (font.lineHeight / 2)+ stellarY, 0, 0, iconSize, iconSize, iconSize, iconSize);
        }

        guiGraphics.pose().translate(0,0,-40);
        choseButton13.setX(b3X);
        choseButton13.setY(currentY - (int)scrollAmount);
        int backupY13 = choseButton13.getY();
        choseButton13.setY(currentY);
        choseButton13.render(guiGraphics, mouseX, mouseY + (int)scrollAmount, partialTicks);
        choseButton13.setY(backupY13);
        guiGraphics.pose().translate(0,0,40);
        t3 = "Strong";
        guiGraphics.drawString(font, t3, b3X + (btnWidth / 2) - (font.width(t3) / 2), currentY + (btnHeight / 2) - (font.lineHeight / 2), 0xE10600, false);
        currentY += btnHeight;
        if (vestigePower == 3) {
            guiGraphics.blit(STELLAR, b3X + (btnWidth / 2) - (font.width(t3) / 2)+ stellarX, currentY + (btnHeight / 2) - (font.lineHeight / 2)+ stellarY - btnHeight, 0, 0, iconSize, iconSize, iconSize, iconSize);
        }

        component = Component.translatable("vp.welcome.8");
        textX = (this.width / 2) - (component.getString().length()*2);
        guiGraphics.drawString(font, component, textX, currentY, 0xC5B4E3, false);
        lines = font.split(component, (int) (size * 0.85));
        for (FormattedCharSequence line : lines) {
            int mainColor = 0xFFB673;
            if(number > 1) {
                guiGraphics.drawString(font, line, textX - 1, currentY, secondColor, false);
                guiGraphics.drawString(font, line, textX + 1, currentY, secondColor, false);
                guiGraphics.drawString(font, line, textX, currentY - 1, secondColor, false);
                guiGraphics.drawString(font, line, textX, currentY + 1, secondColor, false);
            }
            if(number >= 5)
                mainColor = 0xC5B4E3;
            guiGraphics.drawString(font, line, textX, currentY, mainColor, false);
            currentY += font.lineHeight + 2;
        }
        currentY += font.lineHeight;

        guiGraphics.pose().translate(0,0,-40);
        choseButton31.setX(b1X);
        choseButton31.setY(currentY - (int)scrollAmount);
        choseButton31.setY(currentY);
        choseButton31.render(guiGraphics, mouseX, mouseY + (int)scrollAmount, partialTicks);
        guiGraphics.pose().translate(0,0,40);
        t1 = "Default";
        guiGraphics.drawString(font, t1, b1X + (btnWidth / 2) - (font.width(t1) / 2), currentY + (btnHeight / 2) - (font.lineHeight / 2), 0x78BE21, false);
        if (worldDifficulty == 1) {
            guiGraphics.blit(STELLAR, b1X + (btnWidth / 2) - (font.width(t1) / 2) + stellarX, currentY + (btnHeight / 2) - (font.lineHeight / 2) + stellarY, 0, 0, iconSize, iconSize, iconSize, iconSize);
        }

        guiGraphics.pose().translate(0,0,-40);
        choseButton32.setX(b2X);
        choseButton32.setY(currentY - (int)scrollAmount);
        choseButton32.setY(currentY);
        choseButton32.render(guiGraphics, mouseX, mouseY + (int)scrollAmount, partialTicks);
        guiGraphics.pose().translate(0,0,40);
        t2 = "Cruel";
        guiGraphics.drawString(font, t2, b2X + (btnWidth / 2) - (font.width(t2) / 2), currentY + (btnHeight / 2) - (font.lineHeight / 2), 0xFC4C02, false);
        if (worldDifficulty == 2) {
            guiGraphics.blit(STELLAR, b2X + (btnWidth / 2) - (font.width(t2) / 2)+ stellarX, currentY + (btnHeight / 2) - (font.lineHeight / 2)+ stellarY, 0, 0, iconSize, iconSize, iconSize, iconSize);
        }

        guiGraphics.pose().translate(0,0,-40);
        choseButton33.setX(b3X);
        choseButton33.setY(currentY - (int)scrollAmount);
        choseButton33.setY(currentY);
        choseButton33.render(guiGraphics, mouseX, mouseY + (int)scrollAmount, partialTicks);
        guiGraphics.pose().translate(0,0,40);
        t3 = "Leaderboard";
        guiGraphics.drawString(font, t3, b3X + (btnWidth / 2) - (font.width(t3) / 2), currentY + (btnHeight / 2) - (font.lineHeight / 2), 0xE10600, false);
        if (worldDifficulty == 3) {
            guiGraphics.blit(STELLAR, b3X + (btnWidth / 2) - (font.width(t3) / 2)+ stellarX, currentY + (btnHeight / 2) - (font.lineHeight / 2)+ stellarY, 0, 0, iconSize, iconSize, iconSize, iconSize);
        }

        guiGraphics.pose().translate(0,0,-40);
        choseButton34.setX(b4X);
        choseButton34.setY(currentY - (int)scrollAmount);
        choseButton34.setY(currentY);
        choseButton34.render(guiGraphics, mouseX, mouseY + (int)scrollAmount, partialTicks);
        guiGraphics.pose().translate(0,0,40);
        t4 = "Cruel+Leaderboard";
        guiGraphics.drawString(font, t4, b4X + (btnWidth / 2) - (font.width(t4) / 2), currentY + (btnHeight / 2) - (font.lineHeight / 2), 0xE10600, false);
        currentY += btnHeight;
        if (worldDifficulty == 4) {
            guiGraphics.blit(STELLAR, b4X + (btnWidth / 2) - (font.width(t4) / 2)+ stellarX, currentY + (btnHeight / 2) - (font.lineHeight / 2)+ stellarY - btnHeight, 0, 0, iconSize, iconSize, iconSize, iconSize);
        }

        this.totalContentHeight = currentY - scissorTop;
        guiGraphics.pose().popPose();
        guiGraphics.disableScissor();

        guiGraphics.pose().translate(15, 0, -40);
        exit.setX(this.width/2 - exit.getWidth()/2);
        exit.setY((int) (scissorBottom));
        exit.render(guiGraphics, mouseX, mouseY + (int)scrollAmount, partialTicks);
        guiGraphics.pose().translate(0,0,40);
        t3 = "Exit";
        guiGraphics.drawString(font, t3, exit.getX() + (btnWidth / 2) - (font.width(t3) / 2), exit.getY() - (font.lineHeight / 2) + (btnHeight / 2), 0xE10600, false);

        guiGraphics.pose().popPose();
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        this.scrollAmount -= scrollY * 15;
        int maxScrollAllowed = Math.max(0, this.totalContentHeight - this.scissorHeight);
        this.scrollAmount = Mth.clamp(this.scrollAmount, 0, maxScrollAllowed);
        return true;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, 0x80000000, 0x80000000);
    }

    @Override
    public void onClose() {
        onExit();
        super.onClose();
    }

    public void onExit(){
        List<Integer> reduceList = new ArrayList<>();
        ServerConfig.reduceChallengesPercent.set(true);
        if (challengeDifficulty == 1){
            for(int i = 0; i < VestigeCap.totalVestiges; i++)
                reduceList.add(50);
        } else if (challengeDifficulty == 2){
            for(int i = 0; i < VestigeCap.totalVestiges; i++)
                reduceList.add(25);
        } else if (challengeDifficulty == 3){
            for(int i = 0; i < VestigeCap.totalVestiges; i++)
                reduceList.add(10);
        } else if (challengeDifficulty == 4){
            for(int i = 0; i < VestigeCap.totalVestiges; i++)
                reduceList.add(0);
        }
        ServerConfig.reduceChallenges.set(reduceList);
        List<Integer> scaleList = new ArrayList<>();
        if(vestigePower == 1){
            for(int i = 0; i < VestigeCap.totalVestiges; i++)
                scaleList.add(5);
            ServerConfig.powerBoost.set(2D);
        } else if(vestigePower == 2){
            for(int i = 0; i < VestigeCap.totalVestiges; i++)
                scaleList.add(30);
            ServerConfig.powerBoost.set(5D);
        }else if(vestigePower == 3){
            for(int i = 0; i < VestigeCap.totalVestiges; i++)
                scaleList.add(100);
            ServerConfig.powerBoost.set(0D);
        }
        ServerConfig.powerScales.set(scaleList);
        if(worldDifficulty == 2){
            ServerConfig.cruelMode.set(true);
        } else if(worldDifficulty == 3){
            ServerConfig.leaderboard.set(true);
        } else if(worldDifficulty == 4){
            ServerConfig.cruelMode.set(true);
            ServerConfig.leaderboard.set(true);
        }
        ServerConfig.SPEC.save();
        PacketHandler.sendToServer(new SendClientDataToServerPacket(2,""));
    }
}
