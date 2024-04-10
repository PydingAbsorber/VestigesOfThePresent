package com.pyding.vp.item.artifacts;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.event.EventHandler;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.*;

public class Vestige extends Item implements ICurioItem {
    public Vestige() {
        super(new Item.Properties().stacksTo(1));
    }
    public Vestige(Properties properties){
        super(properties);
    }
    List<String> specialText;
    public Boolean damageType;
    public int ultimateChargesBase = 0;
    public int specialChargesBase = 0;

    public long ultimateDurationBase = 0;
    public long specialDurationBase = 0;

    public int specialCdBase = 0;
    public int ultimateCdBase = 0;

    public int vestigeNumber;

    public ChatFormatting color;

    public int specialCharges(ItemStack stack) {
        return stack.getOrCreateTag().getInt("VPSpecialCharges");
    }

    public void setSpecialCharges(int number, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPSpecialCharges", number);
    }

    public int ultimateCharges(ItemStack stack) {
        return stack.getOrCreateTag().getInt("VPUltimateCharges");
    }

    public void setUltimateCharges(int number, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPUltimateCharges", number);
    }

    public int specialCd(ItemStack stack) {
        return stack.getOrCreateTag().getInt("VPSpecialCd");
    }

    public void setSpecialCd(int number, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPSpecialCd", number);
    }

    public int ultimateCd(ItemStack stack) {
        return stack.getOrCreateTag().getInt("VPUltimateCd");
    }

    public void setUltimateCd(int number, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPUltimateCd", number);
    }

    public int cdSpecialActive(ItemStack stack) {
        return stack.getOrCreateTag().getInt("VPCdSpecialActive");
    }

    public void setCdSpecialActive(int number, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPCdSpecialActive", number);
    }

    public int cdUltimateActive(ItemStack stack) {
        return stack.getOrCreateTag().getInt("VPCdUltimateActive");
    }

    public void setCdUltimateActive(int number, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPCdUltimateActive", number);
    }

    public int specialBonusModifier(ItemStack stack) {
        return stack.getOrCreateTag().getInt("VPSpecialBonusModifier");
    }

    public void setSpecialBonusModifier(int number, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPSpecialBonusModifier", number);
    }

    public int ultimateBonusModifier(ItemStack stack) {
        return stack.getOrCreateTag().getInt("VPUltimateBonusModifier");
    }

    public void setUltimateBonusModifier(int number, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPUltimateBonusModifier", number);
    }

    public int currentChargeSpecial(ItemStack stack) {
        return stack.getOrCreateTag().getInt("VPCurrentChargeSpecial");
    }

    public void setCurrentChargeSpecial(int number, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPCurrentChargeSpecial", number);
    }

    public int currentChargeUltimate(ItemStack stack) {
        return stack.getOrCreateTag().getInt("VPCurrentChargeUltimate");
    }

    public void setCurrentChargeUltimate(int number, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPCurrentChargeUltimate", number);
    }

    public int ultimateChargesBase(ItemStack stack) {
        return stack.getOrCreateTag().getInt("VPUltimateChargesBase");
    }

    public void setUltimateChargesBase(int number, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPUltimateChargesBase", number);
    }

    public int specialChargesBase(ItemStack stack) {
        return stack.getOrCreateTag().getInt("VPSpecialChargesBase");
    }

    public void setSpecialChargesBase(int number, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPSpecialChargesBase", number);
    }

    public long ultimateDurationBase(ItemStack stack) {
        return stack.getOrCreateTag().getLong("VPUltimateDurationBase");
    }

    public void setUltimateDurationBase(long number, ItemStack stack) {
        stack.getOrCreateTag().putLong("VPUltimateDurationBase", number);
    }

    public long specialDurationBase(ItemStack stack) {
        return stack.getOrCreateTag().getLong("VPSpecialDurationBase");
    }

    public void setSpecialDurationBase(long number, ItemStack stack) {
        stack.getOrCreateTag().putLong("VPSpecialDurationBase", number);
    }

    public int specialCdBase(ItemStack stack) {
        return stack.getOrCreateTag().getInt("VPSpecialCdBase");
    }

    public void setSpecialCdBase(int number, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPSpecialCdBase", number);
    }

    public int ultimateCdBase(ItemStack stack) {
        return stack.getOrCreateTag().getInt("VPUltimateCdBase");
    }

    public void setUltimateCdBase(int number, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPUltimateCdBase", number);
    }
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        setUltimateChargesBase(ultimateCharges, stack);
        setSpecialChargesBase(specialCharges, stack);
        setUltimateCharges(ultimateCharges, stack);
        setUltimateCdBase(ultimateCd * 20, stack);
        setUltimateCd(ultimateCd * 20, stack);
        setSpecialCharges(specialCharges, stack);
        setSpecialCdBase(specialCd * 20, stack);
        setSpecialCd(specialCd * 20, stack);
        this.damageType = hasDamage;
        this.vestigeNumber = vestigeNumber;
        setVestigeNumber(vestigeNumber, stack);
        this.color = color;
        setSpecialMaxTime((long) specialMaxTime * 1000, stack);
        setSpecialDurationBase((long) specialMaxTime * 1000, stack);
        setUltimateMaxTime((long) ultimateMaxTime * 1000, stack);
        setUltimateDurationBase((long) ultimateMaxTime * 1000, stack);
        this.ultimateChargesBase = ultimateCharges;
        this.specialChargesBase = specialCharges;
        this.ultimateCdBase = ultimateCd * 20;
        this.specialCdBase = specialCd * 20;
        this.specialDurationBase = (long) specialMaxTime * 1000;
        this.ultimateDurationBase = (long) ultimateMaxTime * 1000;
    }

    public int getVestigeNumber(ItemStack stack) {
        return stack.getOrCreateTag().getInt("VPVestigeNumber");
    }

    public void setVestigeNumber(int number, ItemStack stack) {
        stack.getOrCreateTag().putInt("VPVestigeNumber", number);
    }

    public static void setStellar(ItemStack stack) {
        stack.getOrCreateTag().putBoolean("Stellar", true);
    }

    public static void setDoubleStellar(ItemStack stack) {
        stack.getOrCreateTag().putBoolean("DoubleStellar", true);
    }

    public boolean isStellar(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Stellar");
    }

    public boolean isDoubleStellar(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("DoubleStellar");
    }

    public void init(ItemStack stack) {
        this.dataInit(0, null, 0, 0, 0, 0, 0, 0, false, stack);
    }

    public boolean isSpecialActive(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("VPSpecialActive");
    }

    public void setSpecialActive(boolean state, ItemStack stack) {
        stack.getOrCreateTag().putBoolean("VPSpecialActive", state);
    }

    public boolean isUltimateActive(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("VPUltimateActive");
    }

    public void setUltimateActive(boolean state, ItemStack stack) {
        stack.getOrCreateTag().putBoolean("VPUltimateActive", state);
    }

    public long time(ItemStack stack) {
        return stack.getOrCreateTag().getLong("VPTime");
    }

    public void setTime(long time, ItemStack stack) {
        stack.getOrCreateTag().putLong("VPTime", time);
    }

    public long timeUlt(ItemStack stack) {
        return stack.getOrCreateTag().getLong("VPTimeUlt");
    }

    public void setTimeUlt(long time, ItemStack stack){
        stack.getOrCreateTag().putLong("VPTimeUlt",time);
    }

    public long specialMaxTime(ItemStack stack){
        return stack.getOrCreateTag().getLong("VPSpecialMaxTime");
    }

    public void setSpecialMaxTime(long time, ItemStack stack){
        stack.getOrCreateTag().putLong("VPSpecialMaxTime",time);
    }
    public long ultimateMaxTime(ItemStack stack){
        return stack.getOrCreateTag().getLong("VPUltimateMaxTime");
    }

    public void setUltimateMaxTime(long time, ItemStack stack){
        stack.getOrCreateTag().putLong("VPUltimateMaxTime",time);
    }

    public int setSpecialActive(long seconds, Player player, ItemStack stack){
        if(currentChargeSpecial(stack) > 0) {
            if(!player.getCommandSenderWorld().isClientSide) {
                setTime(System.currentTimeMillis() + seconds,stack);  //active time in real seconds
                setSpecialActive(true,stack);
                setCdSpecialActive(cdSpecialActive(stack)+specialCd(stack),stack);     //time until cd recharges in seconds*tps
                if(!(VPUtil.getSet(player) == 3 && Math.random() < 0.3) || (VPUtil.getSet(player) == 6 && Math.random() < 0.5))
                    setCurrentChargeSpecial(currentChargeSpecial(stack)-1,stack);
                this.doSpecial(seconds, player, player.getCommandSenderWorld(), stack);
            } else this.localSpecial(player);
        }
        return 0;
    }

    public int setUltimateActive(long seconds, Player player, ItemStack stack){
        if(currentChargeUltimate(stack) > 0) {
            if(!player.getCommandSenderWorld().isClientSide) {
                setTimeUlt(System.currentTimeMillis() + seconds,stack);  //active time in real seconds
                setUltimateActive(true,stack);
                setCdUltimateActive(cdUltimateActive(stack)+ultimateCd(stack),stack);     //time until cd recharges in seconds*tps
                setCurrentChargeUltimate(currentChargeUltimate(stack)-1,stack);
                long bonus = 1+(long)player.getPersistentData().getFloat("VPDurationBonusDonut")/1000;
                this.doUltimate(seconds*bonus, player, player.getCommandSenderWorld(), stack);
            } else this.localSpecial(player);
        }
        return 0;
    }

    public void applyBonus(ItemStack stack,Player player){
        setSpecialMaxTime(specialDurationBase(stack),stack);
        setUltimateMaxTime(ultimateDurationBase(stack),stack);
        setSpecialCd(specialCdBase(stack),stack);
        setUltimateCd(ultimateCdBase(stack),stack);
        int specialBonus = 0;
        int ultimateBonus = 0;
        if(isDoubleStellar(stack)){
            specialBonus += 1;
            ultimateBonus += 1;
        }
        int spAcsBonus = 0;
        int ultAcsBonus = 0;
        int set = VPUtil.getSet(player);
        if(set == 1){
            spAcsBonus += 1;
            setSpecialMaxTime((long) (specialDurationBase(stack)*1.2),stack);
            setUltimateMaxTime((long) (ultimateDurationBase(stack)*1.2),stack);
        }
        else if(set == 6){
            spAcsBonus -= 1;
            ultAcsBonus -= 1;
        }
        else if(set == 7){
            //spAcsBonus += 1;
            ultAcsBonus += 1;
            setSpecialMaxTime((long) (specialDurationBase(stack)*0.6),stack);
            setUltimateMaxTime((long) (ultimateDurationBase(stack)*0.6),stack);
        }
        else if(set == 8){
            spAcsBonus -= 1;
            ultAcsBonus -= 1;
            setSpecialCd((int) (specialCdBase(stack)*1.4),stack);
            setUltimateCd((int) (ultimateCdBase(stack)*1.4),stack);
        }
        setSpecialCharges(specialChargesBase(stack)+specialBonus+specialBonusModifier(stack)+spAcsBonus,stack);
        setUltimateCharges(ultimateChargesBase(stack)+ultimateBonus+ultimateBonusModifier(stack)+ultAcsBonus,stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof  Player player && player.getCommandSenderWorld().isClientSide) {
            ICurioItem.super.curioTick(slotContext, stack);
            return;
        }
        if(this.ultimateCharges(stack) == 0 || this.specialCharges(stack) == 0 || specialCd(stack) == 0 || ultimateCd(stack) == 0)
            this.init(stack);
        ServerPlayer playerServer = (ServerPlayer) slotContext.entity();
        if(!isStellar(stack) && playerServer.isCreative())
            setStellar(stack);
        if(playerServer != null) {
            if (isSpecialActive(stack))
                whileSpecial(playerServer, stack);
            if (isUltimateActive(stack))
                whileUltimate(playerServer, stack);
        }
        if(time(stack) > 0 && time(stack) <= System.currentTimeMillis()) {
            setTime(0,stack);
            setSpecialActive(false,stack);
            if(playerServer != null)
                specialEnds(playerServer, stack);
        }
        if(timeUlt(stack) > 0 && timeUlt(stack) <= System.currentTimeMillis()) {
            setTimeUlt(0,stack);
            setUltimateActive(false,stack);
            if(playerServer != null)
                ultimateEnds(playerServer, stack);
        }
        if (cdSpecialActive(stack) > 0) {
            setCdSpecialActive(cdSpecialActive(stack)-1,stack);
            if ((this.cdSpecialActive(stack) > this.specialCd(stack) ? (this.cdSpecialActive(stack) % this.specialCd(stack) == 0) : (this.cdSpecialActive(stack) - (this.specialCd(stack)) == 0 || this.cdSpecialActive(stack) == 0)) && this.specialCharges(stack) > this.currentChargeSpecial(stack)) {
                setCurrentChargeSpecial(currentChargeSpecial(stack)+1,stack);
                if(playerServer != null)
                    specialRecharges(playerServer, stack);
            }
        }
        if (cdUltimateActive(stack) > 0) {
            setCdUltimateActive(cdUltimateActive(stack)-1,stack);
            if ((this.cdUltimateActive(stack) > this.ultimateCd(stack) ? (this.cdUltimateActive(stack) % this.ultimateCd(stack) == 0) : (this.cdUltimateActive(stack) - (this.ultimateCd(stack)) == 0 || this.cdUltimateActive(stack) == 0)) && this.ultimateCharges(stack) > this.currentChargeUltimate(stack)) {
                setCurrentChargeUltimate(currentChargeUltimate(stack)+1,stack);
                if(playerServer != null)
                    ultimateRecharges(playerServer, stack);
            }
        }
        if((currentChargeUltimate(stack) == 0 && cdUltimateActive(stack) == 0) || (currentChargeSpecial(stack) == 0 && cdSpecialActive(stack) == 0))
            curioSucks(playerServer,stack);
        ICurioItem.super.curioTick(slotContext, stack);
    }
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (color == null) {
            this.init(stack);
        }
        Player player = Minecraft.getInstance().player;
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            if (Screen.hasShiftDown()) {
                components.add(Component.translatable("vp.passive").withStyle(color));
                components.add(Component.translatable("vp.passive." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                int spCharges;
                int ultCharges;
                int spCd;
                int ultCd;
                spCharges = specialCharges(stack);
                spCd = specialCd(stack);
                ultCharges = ultimateCharges(stack);
                ultCd = ultimateCd(stack);
                if(vestigeNumber == 10)
                    components.add(Component.translatable("vp.return").withStyle(color).append(Component.literal("\n"
                            + stack.getOrCreateTag().getString("VPReturnKey") + " "
                            + stack.getOrCreateTag().getDouble("VPReturnX") + "X, "
                            + stack.getOrCreateTag().getDouble("VPReturnY") + "Y, "
                            + stack.getOrCreateTag().getDouble("VPReturnZ") + "Z, ").withStyle(ChatFormatting.GRAY)));
                components.add(Component.translatable("vp.special").withStyle(color)
                        .append(Component.translatable("vp.charges").withStyle(color))
                        .append(Component.literal(" " + spCharges ).withStyle(color))
                        .append(Component.translatable("vp.charges2").withStyle(color))
                        .append(Component.literal(" " + spCd / 20).withStyle(color))
                        .append(Component.translatable("vp.seconds").withStyle(color)));
                components.add(Component.translatable("vp.special." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                components.add(Component.translatable("vp.ultimate").withStyle(color)
                        .append(Component.translatable("vp.charges").withStyle(color))
                        .append(Component.literal(" " + ultCharges ).withStyle(color))
                        .append(Component.translatable("vp.charges2").withStyle(color))
                        .append(Component.literal(" " + ultCd / 20).withStyle(color))
                        .append(Component.translatable("vp.seconds").withStyle(color)));
                components.add(Component.translatable("vp.ultimate." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                if(vestigeNumber == 10)
                    components.add(Component.translatable("vp.worlds").withStyle(color).append(Component.literal("\n" + cap.getDimensions()).withStyle(ChatFormatting.GRAY)));
                if (damageType) {
                    components.add(Component.translatable("vp.damage").withStyle(color));
                    components.add(Component.translatable("vp.damagetype." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                }
                if (isStellar(stack)) {
                    if(isDoubleStellar(stack))
                        components.add(Component.literal(VPUtil.getRainbowString("Double Stellar: ")));
                    else components.add(Component.literal(VPUtil.getRainbowString("Stellar: ")));
                } else {
                    components.add(Component.translatable(("Stellar")).withStyle(color));
                }
                components.add(Component.translatable(("vp.stellarText")).withStyle(ChatFormatting.GRAY).append(Component.translatable("vp.stellar." + vestigeNumber)));
                int visualUlt = this.cdUltimateActive(stack);
                while (visualUlt > this.ultimateCd(stack)) {
                    visualUlt -= this.ultimateCd(stack);
                }
                int visualSpecial = this.cdSpecialActive(stack);
                while (visualSpecial > this.specialCd(stack)) {
                    visualSpecial -= this.specialCd(stack);
                }
                if(cap.getDebug()){
                    components.add(Component.literal("Special Charges: " + this.currentChargeSpecial(stack)).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("Special Cd: " + visualSpecial).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("Ultimate Charges: " + this.currentChargeUltimate(stack)).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("Ultimate Cd: " + visualUlt).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("IsSpecialActive: " + isSpecialActive(stack)).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("IsUltimateActive: " + isUltimateActive(stack)).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("TimeSpecial: " + (time(stack) - System.currentTimeMillis())).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("TimeUltimate: " + (timeUlt(stack) - System.currentTimeMillis())).withStyle(ChatFormatting.GRAY));
                }
                components.add(Component.translatable("config").withStyle(ChatFormatting.GRAY));
            } else if (Screen.hasControlDown()) {
                components.add(Component.translatable("vp.challenge").withStyle(ChatFormatting.GRAY).append(Component.literal(VPUtil.getRainbowString(VPUtil.generateRandomString(7)) + " :")));
                if(vestigeNumber == 9)
                    components.add(Component.translatable("vp.get." + vestigeNumber).withStyle(ChatFormatting.GRAY).append(Component.literal(cap.getGoldenChance()+"%").withStyle(ChatFormatting.GRAY)));
                else if(vestigeNumber == 14){
                    components.add(Component.translatable("vp.get." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                    components.add(Component.translatable("vp.chaos").withStyle(ChatFormatting.GRAY).append(Component.literal(cap.getRandomEntity())));
                    components.add(Component.translatable("vp.chaos2").withStyle(ChatFormatting.GRAY).append(VPUtil.formatMilliseconds(cap.getChaosTime()+VPUtil.getChaosTime()-System.currentTimeMillis())));
                }
                else components.add(Component.translatable("vp.get." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                int progress = 0;
                progress = cap.getChallenge(vestigeNumber);
                if(vestigeNumber == 12)
                    progress = EventHandler.getCurses(player);
                components.add(Component.translatable("vp.progress").withStyle(color)
                        .append(Component.literal(" " + progress))
                        .append(Component.literal(" / " + player.getPersistentData().getInt("VPMaxChallenge"+vestigeNumber))));  //PlayerCapabilityVP.getMaximum(vestigeNumber,player)
                int stellarChance = cap.getChance();
                if(VPUtil.getSet(player) == 9)
                    stellarChance += 5;
                components.add(Component.literal(stellarChance+"% ").withStyle(color).append(Component.translatable("vp.chance").withStyle(ChatFormatting.GRAY).append(Component.literal(VPUtil.getRainbowString("Stellar")))));
                components.add(Component.translatable("vp.chance2").withStyle(ChatFormatting.GRAY).append(Component.literal(ConfigHandler.COMMON.stellarChanceIncrease.get() + "%")));
                components.add(Component.translatable("vp.getText1").withStyle(ChatFormatting.GRAY).append(Component.literal(VPUtil.formatMilliseconds(VPUtil.coolDown(player))+" ").withStyle(ChatFormatting.GRAY)));
                if (cap.hasCoolDown(vestigeNumber))
                    components.add(Component.translatable("vp.getText2").append(Component.literal((VPUtil.formatMilliseconds(VPUtil.coolDown(player)-(System.currentTimeMillis() - cap.getTimeCd()))))));
                if(player.isCreative()) {
                    components.add(Component.translatable("vp.creative").withStyle(ChatFormatting.DARK_PURPLE));
                }
            } else if (Screen.hasAltDown()) {
                String text = "";
                switch (vestigeNumber) {
                    case 2: {
                        text = VPUtil.getMonsterClient(player).toString();
                        break;
                    }
                    case 3: {
                        text = VPUtil.getBiomesClient(player).toString();
                        break;
                    }
                    case 6: {
                        text = VPUtil.getFoodLeft(cap.getFoodEaten()).toString();
                        break;
                    }
                    case 10: {
                        text = VPUtil.getToolLeft(cap.getTools()).toString();
                        break;
                    }
                    case 11:{
                        text = VPUtil.getDamageKindsLeft(cap.getDamageDie()).toString();
                        break;
                    }
                    case 13:{
                        text = VPUtil.getDamageDoLeft(cap.getDamageDo()).toString();
                        break;
                    }
                    case 15: {
                        text = VPUtil.getBossClient(player).toString();
                        break;
                    }
                    case 16: {
                        text = VPUtil.getFlowersLeft(cap.getFlowers()).toString();
                        break;
                    }
                    case 17: {
                        text = VPUtil.getEffectsLeft(cap.getEffects()).toString();
                        break;
                    }
                    case 20:{
                        text = VPUtil.getMobsLeft(cap.getMobsTamed()).toString();
                        break;
                    }
                    default:
                        text = null;
                }
                if (text != null)
                    components.add(Component.literal(text).withStyle(ChatFormatting.GRAY));
            } else {
                components.add(Component.translatable("vp.short." + vestigeNumber).withStyle(color));
                components.add(Component.translatable("vp.press").append(Component.literal("SHIFT").withStyle(color).append(Component.translatable("vp.shift"))));
                components.add(Component.translatable("vp.press").append(Component.literal("CTRL").withStyle(color).append(Component.translatable("vp.ctrl"))));
                if (vestigeNumber == 2 || vestigeNumber == 6 || vestigeNumber == 10 || vestigeNumber == 11 || vestigeNumber == 15 || vestigeNumber == 16 || vestigeNumber == 17 || vestigeNumber == 20)
                    components.add(Component.translatable("vp.press").append(Component.literal("ALT").withStyle(color).append(Component.translatable("vp.alt"))));
                if(vestigeNumber == 3)
                    components.add(Component.translatable("vp.press").append(Component.literal("ALT").withStyle(color).append(Component.translatable("vp.alt.atlas"))));
                if(vestigeNumber == 13)
                    components.add(Component.translatable("vp.press").append(Component.literal("ALT").withStyle(color).append(Component.translatable("vp.alt.prism"))));
            }
            if(vestigeNumber == 9){
                int luck = stack.getOrCreateTag().getInt("VPLuck");
                int kills = stack.getOrCreateTag().getInt("VPKills");
                components.add(Component.literal("Kills: " + kills).withStyle(color));
                components.add(Component.literal("Luck: " + luck).withStyle(color));
            }
            if(vestigeNumber == 13)
                components.add(Component.translatable("vp.prism").withStyle(color).append(Component.literal(stack.getOrCreateTag().getInt("VPPrismKill")+"").withStyle(ChatFormatting.GRAY)));
            if(vestigeNumber == 15)
                components.add(Component.translatable("vp.devourer").withStyle(color).append(Component.literal(stack.getOrCreateTag().getInt("VPDevoured")+"").withStyle(ChatFormatting.GRAY)));

            components.add(Component.translatable("vp.info").withStyle(ChatFormatting.GRAY));
            if (isStellar(stack)) {
                String name = symbolsRandom(stack);
                Component component = Component.nullToEmpty(VPUtil.getRainbowString(name));
                stack.setHoverName(component);
            }
        });
        super.appendHoverText(stack, level, components, flag);
    }

    @Override
    public ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        if(isStellar(stack))
            return ICurio.DropRule.ALWAYS_KEEP;
        return ICurio.DropRule.DEFAULT;
    }

    public String originalText;

    public String symbolsRandom(ItemStack stack){
        char[] starChars = {'*', '\u2731', '\u2606', '\u2605'};
        Random random = new Random();
        /*if(!stack.getOrCreateTag().getString("VPName").equals("")) {
            originalText = stack.getOrCreateTag().getString("VPName");
        }
        if(originalText == null){
            StringBuilder result;
            result = new StringBuilder(stack.getDisplayName().getString());
            result.deleteCharAt(0);
            result.deleteCharAt(0);
            result.deleteCharAt(0);
            result.deleteCharAt(result.length()-1);
            originalText = String.valueOf(result);
            stack.getOrCreateTag().putString("VPName",originalText);
        }*/
        originalText = I18n.get("vp.name."+vestigeNumber).substring(2);
        int randomNumber = random.nextInt(originalText.length()-1);
        int randomNumber2 = random.nextInt(originalText.length()-1);
        /*result = new StringBuilder(originalText);
        for (int i = 0; i < originalText.length(); i++){
            if (i == randomNumber)
                result.setCharAt(i,starChars[random.nextInt(starChars.length)]);
            if (i == randomNumber2)
                result.setCharAt(i,starChars[random.nextInt(starChars.length)]);
        }*/
        String finalName = "";
        int counter = 0;
        for (String element: originalText.split("")){
            finalName += element;
            if(counter == randomNumber || counter == randomNumber2)
                finalName += starChars[random.nextInt(starChars.length)];
            counter++;
        }
        return finalName;
    }

    public boolean fuckNbt1(Player player){
        return player.getPersistentData().getBoolean("VPFuckNbt1");
    }
    public boolean fuckNbt2(Player player){
        return player.getPersistentData().getBoolean("VPFuckNbt2");
    }

    public void fuckNbt(){
        /*if(vestigePlayer != null) {
            vestigePlayer.getPersistentData().putBoolean("VPFuckNbt1", true);
            vestigePlayer.getPersistentData().putBoolean("VPFuckNbt2", true);
        }*/
    }

    public void curioSucks(Player player, ItemStack stack){
        if(player.getCommandSenderWorld().isClientSide)
            return;
        setTime(0,stack);
        setTimeUlt(0,stack);
        setSpecialActive(false,stack);
        setUltimateActive(false,stack);
        setCurrentChargeSpecial(0,stack);
        setCurrentChargeUltimate(0,stack);
        setCdSpecialActive(specialCd(stack)*specialCharges(stack),stack);
        setCdUltimateActive(ultimateCd(stack)*ultimateCharges(stack),stack);
        VPUtil.vestigeNullify(player);
        applyBonus(stack,player);
    }
    /*@Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if(!fuckNbt1(player)) {
            setTime(0);
            setTimeUlt(0);
            setSpecialActive(false);
            setUltimateActive(false);
            setCurrentChargeSpecial(0);
            setCurrentChargeUltimate(0);
            setCdSpecialActive(specialCd()*specialCharges());
            setCdUltimateActive(ultimateCd()*ultimateCharges());
            player.getPersistentData().putFloat("VPShield", 0);
            player.getPersistentData().putFloat("VPOverShield", 0);
        } else player.getPersistentData().putBoolean("VPFuckNbt1",true);
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
    }*/

    /*@Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if(this.ultimateCharges() == 0 || this.specialCharges() == 0)
            this.init();
        if (!isStellar(stack) && player.isCreative()) {
            setStellar(stack);
        }
        if(!fuckNbt2(player)) {
            setTime(0);
            setTimeUlt(0);
            setSpecialActive(false);
            setUltimateActive(false);
            setCurrentChargeSpecial(0);
            setCurrentChargeUltimate(0);
            setCdSpecialActive(specialCd()*specialCharges());
            setCdUltimateActive(ultimateCd()*ultimateCharges());
            player.getPersistentData().putFloat("VPShield", 0);
            player.getPersistentData().putFloat("VPOverShield", 0);
            applyBonus(stack);
        } else player.getPersistentData().putBoolean("VPFuckNbt2",true);
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
    }*/

    public void doSpecial(long seconds, Player player, Level level, ItemStack stack){
        player.getPersistentData().putLong("VPAcsSpecial",System.currentTimeMillis()+5000);
    }
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack){
        player.getPersistentData().putFloat("VPDurationBonusDonut", 0);
        if(vestigeNumber != 3)
            player.getPersistentData().putInt("VPGravity",0);
    }
    public void specialEnds(Player player, ItemStack stack){

    }
    public void ultimateEnds(Player player, ItemStack stack){

    }

    public void whileSpecial(Player player, ItemStack stack){

    }
    public void whileUltimate(Player player, ItemStack stack){

    }

    public void specialRecharges(Player player, ItemStack stack){

    }

    public void ultimateRecharges(Player player, ItemStack stack){

    }

    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_ELYTRA, 1.0f, 1.0f);
    }

    public void refresh(Player player, ItemStack stack){
        setCdSpecialActive(0,stack);
        setCdUltimateActive(0,stack);
        setCurrentChargeSpecial(specialCharges(stack),stack);
        setCurrentChargeUltimate(ultimateCharges(stack),stack);
        ultimateEnds(player, stack);
        specialEnds(player, stack);
        specialRecharges(player, stack);
        ultimateRecharges(player, stack);
    }

    public void localSpecial(Player player){

    }

    public void localUltimate(Player player){

    }
}
