package com.pyding.vp.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.capability.VestigeCap;
import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class VestigeScreen extends Screen {

    private static final ResourceLocation FRAME = ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID,
            "textures/gui/vestigegui.png");
    private static final ResourceLocation BACK = ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID,
            "textures/gui/leaderboard_back.png");
    private static final ResourceLocation SCROLL = ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID,
            "textures/gui/scroll.png");
    private static final ResourceLocation ZOOM_IN = ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/zoom-in.png");
    private static final ResourceLocation ZOOM_OUT = ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/zoom-out.png");
    private static final ResourceLocation VAPROSI = ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/vaprosi.png");
    private static final ResourceLocation CHALLENGE_BUTTON = ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/challenge_button.png");
    private static final ResourceLocation LEADERBOARD_BUTTON = ResourceLocation.fromNamespaceAndPath("vp", "textures/gui/leaderboard_button.png");

    long time = 0;
    ItemStack stack;
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
    int vestigeNumber = 0;
    Vestige vestige;

    public VestigeScreen(ItemStack stack, Player player) {
        super(Component.empty());
        this.stack = stack;
        this.player = player;
    }

    @Override
    protected void init() {
        super.init();
        if(stack != null && stack.getItem() instanceof Vestige vestigeLol) {
            vestige = vestigeLol;
        }
        if(vestigeNumber == 0)
            vestigeNumber = vestige.vestigeNumber;
        int buttonSize = 32;
        int padding = 5;
        int center = this.width/2 - buttonSize/2;
        int top = this.height - padding - buttonSize;
        time = System.currentTimeMillis();
        int right = this.width - padding - buttonSize;
        Button zoomInButton = new NiceButton(
                right - buttonSize - padding, top,
                buttonSize, buttonSize,
                0, 0, 0,
                ZOOM_IN,
                buttonSize, buttonSize,
                button -> ClientConfig.guiScaleVestige.set(Math.min(2.0, ClientConfig.guiScaleVestige.get() + 0.1))
        );
        Button zoomOutButton = new NiceButton(
                right, top,
                buttonSize, buttonSize,
                0, 0, 0,
                ZOOM_OUT,
                buttonSize, buttonSize,
                button -> ClientConfig.guiScaleVestige.set(Math.max(0.1, ClientConfig.guiScaleVestige.get() - 0.1))
        );
        this.addRenderableWidget(zoomInButton);
        this.addRenderableWidget(zoomOutButton);
        buttonSize = 16;
        question = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                VAPROSI,
                buttonSize, buttonSize,
                button -> Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new GuideScreen(10,stack)))
        );
        this.addRenderableWidget(question);
        question2 = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                VAPROSI,
                buttonSize, buttonSize,
                button -> Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new GuideScreen(1,stack)))
        );
        this.addRenderableWidget(question2);
        question3 = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                VAPROSI,
                buttonSize, buttonSize,
                button -> Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new GuideScreen(9,stack)))
        );
        this.addRenderableWidget(question3);
        question4 = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                VAPROSI,
                buttonSize, buttonSize,
                button -> Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new GuideScreen(1,stack)))
        );
        this.addRenderableWidget(question4);
        question5 = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                VAPROSI,
                buttonSize, buttonSize,
                button -> Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new GuideScreen(7,stack)))
        );
        this.addRenderableWidget(question5);
        question6 = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                VAPROSI,
                buttonSize, buttonSize,
                button -> {}
        );
        this.addRenderableWidget(question6);
        question.setTooltip(Tooltip.create(Component.translatable("vp.info.10")));
        question2.setTooltip(Tooltip.create(Component.translatable("vp.info.1")));
        question3.setTooltip(Tooltip.create(Component.translatable("vp.info.9")));
        question4.setTooltip(Tooltip.create(Component.translatable("vp.condition."+vestigeNumber).withStyle(ChatFormatting.GRAY)));
        question5.setTooltip(Tooltip.create(Component.translatable("vp.info.7")));
        buttonSize = 64;
        challenge = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                CHALLENGE_BUTTON,
                buttonSize, buttonSize,
                button -> {
                    if(stack.getItem() instanceof Vestige vestige){
                        int vestigeNumber = vestige.vestigeNumber;
                        List<ItemStack> list = VPUtil.getChallengeList(vestigeNumber, player);
                        VestigeCap cap = VPUtil.getCap(player);
                        Object[] data = new Object[3];
                        data[2] = VPUtil.getChallengeString(vestigeNumber,player,cap);
                        if(vestigeNumber == 9)
                            data[0] = cap.getGoldenChance();
                        else if(vestigeNumber == 24) {
                            data[0] = VPUtil.filterString(VPUtil.getAxolotlVariantsLeft(cap.getSea()).toString());
                            data[2] = VPUtil.filterString(VPUtil.getTropiclVariantsLeft(cap.getSea()).toString());
                        }
                        data[1] = " " + cap.getChallenge(vestigeNumber) + " / " + player.getPersistentData().getInt("VPMaxChallenge" + vestigeNumber);
                        Minecraft.getInstance().setScreen(new ChallengeScreen(vestigeNumber,list,data,stack));
                    }
                }
        );
        this.addRenderableWidget(challenge);
        leaderboard = new NiceButton(
                0, 0,
                buttonSize, buttonSize,
                0, 0, 0,
                LEADERBOARD_BUTTON,
                buttonSize, buttonSize,
                button -> Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new LeaderboardScreen(stack)))
        );
        this.addRenderableWidget(leaderboard);
        leaderboard.setTooltip(Tooltip.create(Component.literal("Leaderboard")));
        if(!KeyBinding.FIRST_KEY.getKeyModifier().name().equals("NONE"))
            firstKey += KeyBinding.FIRST_KEY.getKeyModifier().name() + "+";
        firstKey += KeyBinding.FIRST_KEY.getKey().getDisplayName().getString();
        if(!KeyBinding.FIRST_KEY_ULT.getKeyModifier().name().equals("NONE"))
            secondKey += KeyBinding.FIRST_KEY_ULT.getKeyModifier().name() + "+";
        secondKey += KeyBinding.FIRST_KEY_ULT.getKey().getDisplayName().getString();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics,mouseX,mouseY,partialTicks);
        double scale = ClientConfig.guiScaleVestige.get()+0.3;
        int infoWidth = (int) (480*scale);
        int infoHeight = (int) (480*scale);
        int infoPadding = (int) (30*scale);
        Font font = this.font;
        int x = this.width/2 - infoWidth/2;
        int y = this.height/2 - infoHeight/2;
        guiGraphics.blit(FRAME, x, y, 0, 0, infoWidth, infoHeight,infoWidth,infoHeight);
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
        guiGraphics.drawString(font, name, (int) (x+infoWidth/3.1)-font.width(name)/2, (int) (y+infoHeight/2.7), vestige.color.getColor());

        double baseX = 2.3;
        double baseY = 2.55+scale/20;
        challenge.setX((int)(x+infoWidth/(baseX-0.725)));
        challenge.setY((int) (y+infoHeight/(baseY-0.1)));
        question6.setX((int)(x+infoWidth/(baseX-0.725)) + 24);
        question6.setY((int) (y+infoHeight/(baseY-0.5)) + 16);
        //challenge.setTooltip(Tooltip.create(Component.translatable("vp.get."+vestigeNumber)));
        VestigeCap cap = VPUtil.getCap(player);
        List<Component> tooltip = new ArrayList<>();
        if(vestigeNumber == 9)
            tooltip.add(Component.translatable("vp.get." + vestigeNumber).withStyle(ChatFormatting.GRAY).append(Component.literal(cap.getGoldenChance()+"%").withStyle(ChatFormatting.GRAY)));
        else if(vestigeNumber == 13)
            tooltip.add(Component.translatable("vp.get." + vestigeNumber,ConfigHandler.rareItemChance.get()*100+"%").withStyle(ChatFormatting.GRAY));
        else if(vestigeNumber == 14){
            tooltip.add(Component.translatable("vp.get." + vestigeNumber,ConfigHandler.chaostime.get(),player.getPersistentData().getInt("VPMaxChallenge"+vestigeNumber)).append(".\n").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("vp.chaos").withStyle(ChatFormatting.GRAY).append(Component.literal(cap.getRandomEntity())).append(".\n"));
            tooltip.add(Component.translatable("vp.chaos2").withStyle(ChatFormatting.GRAY).append(VPUtil.formatMilliseconds(cap.getChaosTime()+VPUtil.getChaosTime()-System.currentTimeMillis())));
        }
        else if(vestigeNumber == 16){
            tooltip.add(Component.translatable("vp.get." + vestigeNumber).withStyle(ChatFormatting.GRAY));
            if(ConfigHandler.failFlowers.get())
                tooltip.add(Component.translatable("vp.get.16.fail").withStyle(ChatFormatting.GRAY));
        }
        else {
            tooltip.add(Component.translatable("vp.get." + vestigeNumber).withStyle(ChatFormatting.GRAY));
        }
        String tip = "";
        for(Component component: tooltip)
            tip += component.getString();
        this.challenge.setTooltip(Tooltip.create(Component.literal(tip).withStyle(ChatFormatting.GOLD)));
        tooltip.clear();
        double stellarChance = cap.getChance();
        if(VPUtil.getSet(player) == 9)
            stellarChance += 5;
        if(cap.getVip() > System.currentTimeMillis())
            stellarChance += 10;
        if(LeaderboardUtil.hasGoldenName(player.getUUID()))
            stellarChance += 10;
        Component coldown = Component.empty();
        if (cap.hasCoolDown(vestigeNumber))
            coldown = Component.translatable("vp.getText2").append(Component.literal((VPUtil.formatMilliseconds(VPUtil.coolDown(player)-(System.currentTimeMillis() - cap.getTimeCd()))))).withStyle(ChatFormatting.RED);
        Component component = Component.translatable("vp.challenge.obtain").append("\n")
                .append(Component.literal((int)stellarChance+"% ").withStyle(vestige.color).append(Component.translatable("vp.chance").withStyle(ChatFormatting.GRAY).append(GradientUtil.stellarGradient("Stellar.")).append("\n"))).withStyle(ChatFormatting.GRAY)
                .append(Component.translatable("vp.chance2").append(Component.literal(ConfigHandler.stellarChanceIncrease.get() + "%.")).append("\n"))
                .append(Component.translatable("vp.getText1").append(Component.literal(VPUtil.formatMilliseconds(VPUtil.coolDown(player))+".\n").withStyle(ChatFormatting.GRAY)))
                        .append(coldown);
        question6.setTooltip(Tooltip.create(component));
        guiGraphics.drawString(font, Component.translatable("vp.progress").append(cap.getChallenge(vestigeNumber) + " / " + player.getPersistentData().getInt("VPMaxChallenge" + vestigeNumber)), (int)(x+infoWidth/(baseX-0.725)-8),(int) (y+infoHeight/(baseY)), ChatFormatting.GOLD.getColor());

        leaderboard.setX((int)(x+infoWidth/(baseX-1.2)));
        leaderboard.setY((int) (y+infoHeight/(baseY+0.5) + offsetY));
        int backSize = (int) (112*scale);
        guiGraphics.blit(BACK, (int)(x+infoWidth/(baseX-1.1)), (int) (y+infoHeight/(baseY+1.0)), 0, 0, backSize, backSize,backSize,backSize);

        guiGraphics.drawString(font, Component.translatable("vp.power").append(": " + (int)VPUtil.getPower(vestigeNumber, player) + " "), (int)(x+infoWidth/baseX+8), (int) (y+infoHeight/baseY), vestige.color.getColor());
        question.setX((int)(x+infoWidth/baseX-16));
        question.setY((int)(y+infoHeight/baseY-4));
        guiGraphics.drawString(font, Component.translatable("vp.radiance").append(" " + (int)vestige.getMaxRadiance(stack)), (int)(x+infoWidth/baseX+8), (int) (y+infoHeight/(baseY-0.16)), vestige.color.getColor());
        question2.setX((int)(x+infoWidth/baseX-16));
        question2.setY((int)(y+infoHeight/(baseY-0.16)-4));
        guiGraphics.drawString(font, Component.translatable("vp.condition"), (int)(x+infoWidth/baseX+8), (int) (y+infoHeight/(baseY-0.295)), vestige.color.getColor());
        question4.setX((int)(x+infoWidth/(baseX)-16));
        question4.setY((int)(y+infoHeight/(baseY-0.295)-4));
        question3.visible = vestige.damageType;
        question5.visible = false;
        if(vestige.damageType) {
            int xMod = 0;
            if(vestigeNumber == 5 || vestigeNumber == 19){
                question5.visible = true;
                question5.setX((int) (x + infoWidth / baseX + 4));
                question5.setY((int) (y + infoHeight / (baseY-0.415) - 4));
                xMod += 16;
            }
            Component damage = Component.translatable("vp.damage").append(Component.translatable("vp.damagetype."+vestigeNumber));
            List<FormattedCharSequence> lines = font.split(damage, (int) (infoWidth/3.28 - 2 * infoPadding));
            for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
                int textX = (int) (x + infoWidth / baseX + (xMod + 8));
                int textY = (int) (y + infoHeight /(baseY-0.415)) + lineIndex * font.lineHeight;
                guiGraphics.drawString(font, lines.get(lineIndex), textX, textY, vestige.color.getColor());
            }
            //guiGraphics.drawString(font, damage, (int) (x + infoWidth / baseX + (xMod + 8)), (int) (y + infoHeight /(baseY-0.415)), vestige.color.getColor());
            question3.setX((int) (x + infoWidth / baseX - 16));
            question3.setY((int) (y + infoHeight / (baseY-0.415) - 4));
        }

        int textX = (int) (vestigeX - infoWidth / 9 + infoPadding + infoWidth / 25);
        baseY = 2.7;
        
        List<Component> components = new ArrayList<>();
        components.add(Component.translatable("vp.passive").append(": \n").withStyle(vestige.color));
        if(vestigeNumber == 18)
            components.add(Component.translatable("vp.passive." + vestigeNumber,(int)(ConfigHandler.ballShield.get()*100)+"%",(int)(ConfigHandler.ballOverShield.get()*100)+"%",(int)(ConfigHandler.ballDebuff.get()+0)+"%").withStyle(ChatFormatting.GRAY));
        else if (vestigeNumber == 20) {
            components.add(Component.translatable("vp.passive." + vestigeNumber).withStyle(ChatFormatting.GRAY).append("\n").append(Component.translatable("vp.dop." + vestigeNumber, (int) VPUtil.getNbtF(stack,"VPSoulPool")).withStyle(ChatFormatting.GRAY)));
        } else  components.add(Component.translatable("vp.passive." + vestigeNumber).withStyle(ChatFormatting.GRAY));
        if(vestigeNumber == 10)
            components.add(Component.translatable(lang + vestigeNumber).append("\n").append(Component.translatable("vp.return").withStyle(vestige.color).append("\n"
                    + VPUtil.getNbtS(stack,"VPReturnKey") + " "
                    + VPUtil.getNbtD(stack,"VPReturnX") + "X, "
                    + VPUtil.getNbtD(stack,"VPReturnY") + "Y, "
                    + VPUtil.getNbtD(stack,"VPReturnZ") + "Z, ").withStyle(ChatFormatting.GRAY)));
        else if(vestigeNumber == 25 && System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("linux")){
            components.add(Component.translatable(lang + vestigeNumber).append("\n").append(GradientUtil.customGradient(Component.translatable("vp.archlinx.easter").getString(),GradientUtil.BLUE_LIGHT_BLUE)));
        }
        components.add(Component.literal(" "));
        components.add(Component.translatable("vp.special").append(": ").append(Component.translatable("vp.charges")).append(vestige.specialCharges(stack)+"").withStyle(vestige.color));
        components.add(Component.literal(" "));
        components.add(Component.translatable("vp.charges2").append(" " + vestige.specialCd(stack) / 20).append(Component.translatable("vp.seconds"))
                .append(Component.translatable("vp.keys", firstKey)).withStyle(vestige.color));
        components.add(Component.literal(" "));
        if(vestigeNumber == 2){
            components.add(Component.translatable("vp.special." + vestigeNumber,ConfigHandler.crownShield.get()+"%").withStyle(ChatFormatting.GRAY));
        } else components.add(Component.translatable("vp.special." + vestigeNumber));

        components.add(Component.literal(" "));
        components.add(Component.translatable("vp.ultimate").append(": ").append(Component.translatable("vp.charges")).append(vestige.ultimateCharges(stack) + "").withStyle(vestige.color));
        components.add(Component.literal(" "));
        components.add(Component.translatable("vp.keys", secondKey).withStyle(vestige.color));
        components.add(Component.literal(" "));
        double textYBase = y+infoHeight/(baseY-0.2);
        if(vestigeNumber == 15){
            components.add(Component.translatable("vp.ultimate." + vestigeNumber,ConfigHandler.devourer.get(),ConfigHandler.devourerChance.get()*100+"%").withStyle(ChatFormatting.GRAY));
        }
        else if (vestigeNumber == 23){
            components.add(Component.translatable("vp.ultimate." + vestigeNumber,Math.min(30,10+VPUtil.getLuck(player)*5)).withStyle(ChatFormatting.GRAY));
        }
        else if (vestigeNumber == 6){
            components.add(Component.translatable("vp.ultimate." + vestigeNumber,ConfigHandler.donutMaxSaturation.get()).withStyle(ChatFormatting.GRAY));
        } else components.add(Component.translatable("vp.ultimate." + vestigeNumber));

        components.add(Component.literal(" "));
        if(Vestige.isTripleStellar(stack))
            components.add(GradientUtil.stellarGradient("Stellar:\n\n" + (Component.translatable("vp.stellar." + vestigeNumber).append("\n\nDouble Stellar:\n\n").append(GradientUtil.stellarGradient(Component.translatable("vp.double_stellar").getString())).append("\n\nTriple Stellar:\n\n")).append(GradientUtil.stellarGradient(Component.translatable("vp.triple_stellar").getString())).getString()));
        else if(Vestige.isDoubleStellar(stack))
            components.add(GradientUtil.stellarGradient("Stellar:\n\n" + (Component.translatable("vp.stellar." + vestigeNumber).append("\n\nDouble Stellar:\n\n").append(GradientUtil.stellarGradient(Component.translatable("vp.double_stellar").getString()))).getString()));
        else if (Vestige.isStellar(stack)) {
            components.add(GradientUtil.stellarGradient("Stellar:\n\n" + Component.translatable("vp.stellar." + vestigeNumber).getString()));
        } else components.add(Component.translatable("vp.stellar").append(": ").withStyle(vestige.color).append("\n\n").append(Component.translatable("vp.stellar." + vestigeNumber).withStyle(ChatFormatting.GRAY)));

        renderDescription(guiGraphics,font,components,textX,(int) (textYBase + infoPadding + infoHeight/10),(int) (infoWidth/2), (int) (infoHeight/6),ChatFormatting.GRAY.getColor());

        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private int scrollOffset = 0;
    private int maxScroll = 0;

    private void renderDescription(GuiGraphics guiGraphics, Font font, List<Component> components, int x, int y, int maxWidth, int maxHeight, int color) {
        Minecraft mc = Minecraft.getInstance();
        Window window = mc.getWindow();
        int scale = (int) window.getGuiScale();
        RenderSystem.enableScissor(x * scale, (window.getGuiScaledHeight() - (y + maxHeight)) * scale, maxWidth * scale, maxHeight * scale);
        List<FormattedCharSequence> lines = new ArrayList<>();
        for (Component component : components) {
            lines.addAll(font.split(component, maxWidth));
        }
        int fullTextHeight = lines.size() * font.lineHeight;
        maxScroll = Math.max(0, fullTextHeight - maxHeight);
        scrollOffset = Mth.clamp(scrollOffset, 0, maxScroll);
        int drawY = y - scrollOffset;
        for (FormattedCharSequence line : lines) {
            if (drawY + font.lineHeight >= y && drawY <= y + maxHeight) {
                guiGraphics.drawString(font, line, x, drawY, color);
            }
            drawY += font.lineHeight;
        }
        RenderSystem.disableScissor();
        guiGraphics.blit(SCROLL, (int) (x + maxWidth*0.95), (int) (y+maxHeight/3 - 20), 0, 0, 64, 64,64,64);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double delta) {
        scrollOffset -= delta * 10;
        scrollOffset = Mth.clamp(scrollOffset, 0, maxScroll);
        return true;
    }


    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
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
