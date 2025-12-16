package com.pyding.vp.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

@OnlyIn(Dist.CLIENT)
public class VestigeScreen extends Screen {

    private static final ResourceLocation FRAME = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/vestigegui.png");
    private static final ResourceLocation BACK = new ResourceLocation(VestigesOfThePresent.MODID,
            "textures/gui/leaderboard_back.png");
    long time = 0;
    ItemStack stack;
    Button passive;
    Button special;
    Button ultimate;
    Button stellar;
    Button question;
    Button question2;
    Button question3;
    Button question4;
    Button question5;
    Button question6;
    Button leaderboard;
    Button challenge;
    String lang = "vp.short.";
    String firstKey = "";
    String secondKey = "";
    Player player;

    public VestigeScreen(ItemStack stack, Player player) {
        super(Component.empty());
        this.stack = stack;
        this.player = player;
    }

    @Override
    protected void init() {
        super.init();
        int buttonSize = 32;
        int padding = 5;
        int center = this.width/2 - buttonSize/2;
        int top = this.height - padding - buttonSize;
        time = System.currentTimeMillis();
        int right = this.width - padding - buttonSize;
        Button zoomInButton = new ImageButton(
                right - buttonSize - padding, top,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/zoom-in.png"),
                buttonSize, buttonSize,
                button -> ClientConfig.COMMON.guiScaleVestige.set(Math.min(2.0, ClientConfig.COMMON.guiScaleVestige.get() + 0.1))
        );
        Button zoomOutButton = new ImageButton(
                right, top,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/zoom-out.png"),
                buttonSize, buttonSize,
                button -> ClientConfig.COMMON.guiScaleVestige.set(Math.max(0.1, ClientConfig.COMMON.guiScaleVestige.get() - 0.1))
        );
        this.addRenderableWidget(zoomInButton);
        this.addRenderableWidget(zoomOutButton);
        buttonSize = 16;
        question = new ImageButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/vaprosi.png"),
                buttonSize, buttonSize,
                button -> {}
        );
        this.addRenderableWidget(question);
        question2 = new ImageButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/vaprosi.png"),
                buttonSize, buttonSize,
                button -> {}
        );
        this.addRenderableWidget(question2);
        question3 = new ImageButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/vaprosi.png"),
                buttonSize, buttonSize,
                button -> {}
        );
        this.addRenderableWidget(question3);
        question4 = new ImageButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/vaprosi.png"),
                buttonSize, buttonSize,
                button -> {}
        );
        this.addRenderableWidget(question4);
        question5 = new ImageButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/vaprosi.png"),
                buttonSize, buttonSize,
                button -> {}
        );
        this.addRenderableWidget(question5);
        question6 = new ImageButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/vaprosi.png"),
                buttonSize, buttonSize,
                button -> {}
        );
        this.addRenderableWidget(question6);
        buttonSize = 64;
        passive = new ImageButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/info_button.png"),
                buttonSize, buttonSize,
                button -> lang = "vp.passive."
        );
        special = new ImageButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/info_button.png"),
                buttonSize, buttonSize,
                button -> lang = "vp.special."
        );
        ultimate = new ImageButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/info_button.png"),
                buttonSize, buttonSize,
                button -> lang = "vp.ultimate."
        );
        stellar = new ImageButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/info_button.png"),
                buttonSize, buttonSize,
                button -> lang = "vp.stellar."
        );
        this.addRenderableWidget(passive);
        this.addRenderableWidget(special);
        this.addRenderableWidget(ultimate);
        this.addRenderableWidget(stellar);
        challenge = new ImageButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/challenge_button.png"),
                buttonSize, buttonSize,
                button -> {
                    if(stack.getItem() instanceof Vestige vestige){
                        int vestigeNumber = vestige.vestigeNumber;
                        List<ItemStack> list = VPUtil.getChallengeList(vestigeNumber, player);
                        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                            Object[] data = new Object[3];
                            if(vestigeNumber == 9)
                                data[0] = cap.getGoldenChance();
                            else if(vestigeNumber == 24) {
                                data[0] = VPUtil.filterString(VPUtil.getAxolotlVariantsLeft(cap.getSea()).toString());
                                data[2] = VPUtil.filterString(VPUtil.getTropiclVariantsLeft(cap.getSea()).toString());
                            }
                            data[1] = " " + cap.getChallenge(vestigeNumber) + " / " + player.getPersistentData().getInt("VPMaxChallenge" + vestigeNumber);
                            Minecraft.getInstance().setScreen(new ChallengeScreen(vestigeNumber,list,data));
                        });
                    }
                }
        );
        this.addRenderableWidget(challenge);
        leaderboard = new ImageButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                new ResourceLocation("vp", "textures/gui/leaderboard_button.png"),
                buttonSize, buttonSize,
                button -> Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new LeaderboardScreen()))
        );
        this.addRenderableWidget(leaderboard);
        if(!KeyBinding.FIRST_KEY.getKeyModifier().name().equals("NONE"))
            firstKey += KeyBinding.FIRST_KEY.getKeyModifier().name() + "+";
        firstKey += KeyBinding.FIRST_KEY.getKey().getDisplayName().getString();
        if(!KeyBinding.FIRST_KEY_ULT.getKeyModifier().name().equals("NONE"))
            secondKey += KeyBinding.FIRST_KEY_ULT.getKeyModifier().name() + "+";
        secondKey += KeyBinding.FIRST_KEY_ULT.getKey().getDisplayName().getString();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        double scale = ClientConfig.COMMON.guiScaleVestige.get()+0.3;
        int infoWidth = (int) (480*scale);
        int infoHeight = (int) (480*scale);
        int infoPadding = (int) (30*scale);
        Font font = this.font;
        int x = this.width/2 - infoWidth/2;
        int y = this.height/2 - infoHeight/2;
        guiGraphics.blit(FRAME, x, y, 0, 0, infoWidth, infoHeight,infoWidth,infoHeight);
        Vestige vestige = null;
        if(stack != null && stack.getItem() instanceof Vestige vestigeLol) {
            vestige = vestigeLol;
        }
        int vestigeNumber = vestige.vestigeNumber;
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        double vestigeX = x + infoWidth / 3.9;
        double vestigeY = y + infoHeight / 2.6;
        double time = System.currentTimeMillis() / 1000.0;
        double offsetY = Math.sin(time * 1.2) * 8;
        poseStack.translate(vestigeX-12, vestigeY + offsetY, 100);
        poseStack.scale((float) (5 * scale), (float) (5 * scale), 1);
        guiGraphics.renderItem(stack, 0, 0);
        poseStack.popPose();
        Component name = stack.getHoverName();
        double count = 0;
        for(Byte bytes :name.getString().getBytes())
            count++;
        if(count < 12)
            count /= 4;
        guiGraphics.drawString(font, name, (int) (x+infoWidth/3.4-count), (int) (y+infoHeight/2.7), vestige.color.getColor());
        int buttonHeight = 32;
        int buttonXBase = (int) (x+infoWidth/2.3);
        guiGraphics.drawString(font, Component.translatable("vp.passive"),buttonXBase, (int) (y+infoHeight/2.6), vestige.color.getColor());
        guiGraphics.drawString(font, Component.translatable("vp.special"), buttonXBase+buttonHeight*2, (int) (y+infoHeight/2.6), vestige.color.getColor());
        guiGraphics.drawString(font, Component.translatable("vp.ultimate"), buttonXBase, (int) (y+infoHeight/2.6+buttonHeight*1.1), vestige.color.getColor());
        guiGraphics.drawString(font, GradientUtil.stellarGradient("Stellar"), buttonXBase+buttonHeight*2, (int) (y+infoHeight/2.6+buttonHeight*1.1), vestige.color.getColor());
        int buttonX = buttonXBase-14;
        int buttonY = (int) (y+infoHeight/2.6)-29;
        passive.setX(buttonX);
        passive.setY(buttonY);
        special.setX(buttonX+buttonHeight*2);
        special.setY(buttonY);
        ultimate.setX(buttonX);
        ultimate.setY((int) (buttonY+buttonHeight*1.1));
        stellar.setX(buttonX+buttonHeight*2);
        stellar.setY((int) (buttonY+buttonHeight*1.1));

        double baseX = 2.3;
        double baseY = 1.95+scale/20;
        challenge.setX((int)(x+infoWidth/(baseX-0.725)));
        challenge.setY((int) (y+infoHeight/(baseY+0.5)));
        challenge.setTooltip(Tooltip.create(Component.translatable("vp.get."+vestigeNumber)));
        AtomicInteger challenge = new AtomicInteger();
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            challenge.set(cap.getChallenge(vestigeNumber));
        });
        guiGraphics.drawString(font, Component.translatable("vp.progress").append(challenge.get() + " / " + player.getPersistentData().getInt("VPMaxChallenge" + vestigeNumber)), (int)(x+infoWidth/(baseX-0.725)-8),(int) (y+infoHeight/(baseY+0.6)), ChatFormatting.GOLD.getColor());

        leaderboard.setX((int)(x+infoWidth/(baseX-1.2)));
        leaderboard.setY((int) (y+infoHeight/(baseY+0.5) + offsetY));
        guiGraphics.blit(BACK, (int)(x+infoWidth/(baseX-1.1)), (int) (y+infoHeight/(baseY+1.0)), 0, 0, 168, 168,168,168);

        guiGraphics.drawString(font, Component.translatable("vp.power").append(": " + (int)VPUtil.getPower(vestigeNumber, player) + " "), (int)(x+infoWidth/baseX+8), (int) (y+infoHeight/baseY), vestige.color.getColor());
        question.setX((int)(x+infoWidth/baseX-16));
        question.setY((int)(y+infoHeight/baseY-4));
        question.setTooltip(Tooltip.create(Component.translatable("vp.info.10")));
        guiGraphics.drawString(font, Component.translatable("vp.radiance").append(" " + (int)vestige.getMaxRadiance(stack)), (int)(x+infoWidth/baseX+8), (int) (y+infoHeight/(baseY-0.15)), vestige.color.getColor());
        question2.setX((int)(x+infoWidth/baseX-16));
        question2.setY((int)(y+infoHeight/(baseY-0.15)-4));
        question2.setTooltip(Tooltip.create(Component.translatable("vp.info.1")));
        guiGraphics.drawString(font, Component.translatable("vp.condition"), (int)(x+infoWidth/(baseX-0.6)+8), (int) (y+infoHeight/(baseY-0.15)), vestige.color.getColor());
        question4.setX((int)(x+infoWidth/(baseX-0.6)-16));
        question4.setY((int)(y+infoHeight/(baseY-0.15)-4));
        question4.setTooltip(Tooltip.create(Component.translatable("vp.condition."+vestigeNumber).withStyle(ChatFormatting.GRAY)));
        question3.visible = vestige.damageType;
        question5.visible = false;
        if(vestige.damageType) {
            int xMod = 0;
            if(vestigeNumber == 5 || vestigeNumber == 19){
                question5.visible = true;
                question5.setX((int) (x + infoWidth / baseX + 4));
                question5.setY((int) (y + infoHeight / (baseY-0.27) - 4));
                question5.setTooltip(Tooltip.create(Component.translatable("vp.info.7")));
                xMod += 16;
            }
            guiGraphics.drawString(font, Component.translatable("vp.damage").append(Component.translatable("vp.damagetype."+vestigeNumber)), (int) (x + infoWidth / baseX + (xMod + 8)), (int) (y + infoHeight /(baseY-0.27)), vestige.color.getColor());
            question3.setX((int) (x + infoWidth / baseX - 16));
            question3.setY((int) (y + infoHeight / (baseY-0.27) - 4));
            question3.setTooltip(Tooltip.create(Component.translatable("vp.info.9")));
        }

        int textX = (int) (vestigeX - infoWidth / 9 + infoPadding + infoWidth / 25);
        baseY = 2.7;
        double textYBase = y+infoHeight/baseY;

        Component component = Component.translatable(lang + vestigeNumber);
        if(lang.equals("vp.passive.")) {
            if(vestigeNumber == 18)
                component = (Component.translatable("vp.passive." + vestigeNumber,(int)(ConfigHandler.COMMON.ballShield.get()*100)+"%",(int)(ConfigHandler.COMMON.ballOverShield.get()*100)+"%",(int)(ConfigHandler.COMMON.ballDebuff.get()+0)+"%").withStyle(ChatFormatting.GRAY));
            else if (vestigeNumber == 20) {
                component = (Component.translatable("vp.passive." + vestigeNumber).withStyle(ChatFormatting.GRAY)).append("\n").append(Component.translatable("vp.dop." + vestigeNumber, (int) stack.getOrCreateTag().getFloat("VPSoulPool")).withStyle(ChatFormatting.GRAY));
            }
            else if(vestigeNumber == 10)
                component = Component.translatable(lang + vestigeNumber).append("\n").append(Component.translatable("vp.return").withStyle(vestige.color).append("\n"
                        + stack.getOrCreateTag().getString("VPReturnKey") + " "
                        + stack.getOrCreateTag().getDouble("VPReturnX") + "X, "
                        + stack.getOrCreateTag().getDouble("VPReturnY") + "Y, "
                        + stack.getOrCreateTag().getDouble("VPReturnZ") + "Z, ").withStyle(ChatFormatting.GRAY));
            else if(vestigeNumber == 25 && System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("linux")){
                component = Component.translatable(lang + vestigeNumber).append("\n").append(GradientUtil.customGradient(Component.translatable("vp.archlinx.easter").getString(),GradientUtil.BLUE_LIGHT_BLUE));
            }
        }
        else if(lang.equals("vp.special.")) {
            guiGraphics.drawString(font, Component.translatable("vp.charges").append(vestige.specialCharges(stack)+""), textX, (int) (y + infoHeight / 1.75), vestige.color.getColor());
            guiGraphics.drawString(font, Component.translatable("vp.charges2").append(" " + vestige.specialCd(stack) / 20).append(Component.translatable("vp.seconds"))
                    .append(Component.translatable("vp.keys", firstKey)), textX, (int) (y + infoHeight / 1.75 + font.lineHeight*2), vestige.color.getColor());
            textYBase = y+infoHeight/(baseY-0.2);
            if(vestigeNumber == 2){
                component = (Component.translatable("vp.special." + vestigeNumber,ConfigHandler.COMMON.crownShield.get()+"%").withStyle(ChatFormatting.GRAY));
            }
        }
        else if(lang.equals("vp.ultimate.")) {
            guiGraphics.drawString(font, Component.translatable("vp.charges").append(vestige.ultimateCharges(stack) + ""), textX, (int) (y + infoHeight / 1.75), vestige.color.getColor());
            guiGraphics.drawString(font, Component.translatable("vp.keys", secondKey), textX, (int) (y + infoHeight / 1.75 + font.lineHeight*2), vestige.color.getColor());
            textYBase = y+infoHeight/(baseY-0.2);
            if(vestigeNumber == 15){
                component = (Component.translatable("vp.ultimate." + vestigeNumber,ConfigHandler.COMMON.devourer.get(),ConfigHandler.COMMON.devourerChance.get()*100+"%").withStyle(ChatFormatting.GRAY));
            }
            else if (vestigeNumber == 23){
                component = (Component.translatable("vp.ultimate." + vestigeNumber,Math.min(30,10+VPUtil.getLuck(player)*5)).withStyle(ChatFormatting.GRAY));
            }
            else if (vestigeNumber == 6){
                component = (Component.translatable("vp.ultimate." + vestigeNumber,ConfigHandler.COMMON.donutMaxSaturation.get()).withStyle(ChatFormatting.GRAY));
            }
        }
        else if(lang.equals("vp.stellar.")){
            if(Vestige.isTripleStellar(stack))
                component = (GradientUtil.stellarGradient("Stellar:\n" + (Component.translatable(lang + vestigeNumber).append("\nDouble Stellar:\n").append(GradientUtil.stellarGradient(Component.translatable("vp.double_stellar").getString())).append("\nTriple Stellar:\n")).append(GradientUtil.stellarGradient(Component.translatable("vp.triple_stellar").getString())).getString()));
            else if(Vestige.isDoubleStellar(stack))
                component = (GradientUtil.stellarGradient("Stellar:\n" + (Component.translatable(lang + vestigeNumber).append("\nDouble Stellar:\n").append(GradientUtil.stellarGradient(Component.translatable("vp.double_stellar").getString()))).getString()));
            else if (Vestige.isStellar(stack)) {
                component = (GradientUtil.stellarGradient("Stellar:\n" + Component.translatable(lang + vestigeNumber).getString()));
            }
        }

        List<net.minecraft.util.FormattedCharSequence> lines = font.split(component, (int) (infoWidth*0.65 - 2 * infoPadding));
        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            int textY = (int) (textYBase + infoPadding + lineIndex * font.lineHeight + infoHeight/6);
            guiGraphics.drawString(font, lines.get(lineIndex), textX, textY, ChatFormatting.GRAY.getColor());
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
