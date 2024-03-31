package com.pyding.vp.item.artifacts;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.capability.PlayerCapabilityVP;
import com.pyding.vp.event.EventHandler;
import com.pyding.vp.item.ModCreativeModTab;
import com.pyding.vp.item.accessories.Accessory;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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

import javax.annotation.Nonnull;
import java.util.*;

public class Vestige extends Item implements ICurioItem {
    public Vestige() {
        super(new Item.Properties().stacksTo(1));
    }
    public Vestige(Properties properties){
        super(properties);
    }
    List<String> specialText;
    public int specialCharges(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getInt("VPSpecialCharges");
        }
        return 0;
    }

    public void setSpecialCharges(int number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putInt("VPSpecialCharges",number);
        }
    }
    public int ultimateCharges(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getInt("VPUltimateCharges");
        }
        return 0;
    }

    public void setUltimateCharges(int number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putInt("VPUltimateCharges",number);
        }
    }
    public int specialCd(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getInt("VPSpecialCd");
        }
        return 0;
    }

    public void setSpecialCd(int number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putInt("VPSpecialCd",number);
        }
    }
    public int ultimateCd(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getInt("VPUltimateCd");
        }
        return 0;
    }

    public void setUltimateCd(int number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putInt("VPUltimateCd",number);
        }
    }
    public ChatFormatting color;
    public int cdSpecialActive(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getInt("VPCdSpecialActive");
        }
        return 0;
    }

    public void setCdSpecialActive(int number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putInt("VPCdSpecialActive",number);
        }
    }
    public int cdUltimateActive(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getInt("VPCdUltimateActive");
        }
        return 0;
    }

    public void setCdUltimateActive(int number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putInt("VPCdUltimateActive",number);
        }
    }
    public Boolean damageType;

    public int specialBonusModifier(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getInt("VPSpecialBonusModifier");
        }
        return 0;
    }

    public void setSpecialBonusModifier(int number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putInt("VPSpecialBonusModifier",number);
        }
    }
    public int ultimateBonusModifier(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getInt("VPUltimateBonusModifier");
        }
        return 0;
    }

    public void setUltimateBonusModifier(int number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putInt("VPUltimateBonusModifier",number);
        }
    }

    //////////
    public int currentChargeSpecial(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getInt("VPCurrentChargeSpecial");
        }
        return 0;
    }

    public void setCurrentChargeSpecial(int number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putInt("VPCurrentChargeSpecial",number);
        }
    }
    public int currentChargeUltimate(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getInt("VPCurrentChargeUltimate");
        }
        return 0;
    }

    public void setCurrentChargeUltimate(int number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putInt("VPCurrentChargeUltimate",number);
        }
    }
    public boolean isStellar = false;
    public ItemStack vestigeStack = null;

    public int ultimateChargesBase(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getInt("VPUltimateChargesBase");
        }
        return 0;
    }

    public void setUltimateChargesBase(int number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putInt("VPUltimateChargesBase",number);
        }
    }

    public int specialChargesBase(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getInt("VPSpecialChargesBase");
        }
        return 0;
    }

    public void setSpecialChargesBase(int number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putInt("VPSpecialChargesBase",number);
        }
    }

    public long ultimateDurationBase(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getLong("VPUltimateDurationBase");
        }
        return 0;
    }

    public void setUltimateDurationBase(long number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putLong("VPUltimateDurationBase",number);
        }
    }

    public long specialDurationBase(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getLong("VPSpecialDurationBase");
        }
        return 0;
    }

    public void setSpecialDurationBase(long number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putLong("VPSpecialDurationBase",number);
        }
    }
    public int specialCdBase(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getInt("VPSpecialCdBase");
        }
        return 0;
    }

    public void setSpecialCdBase(int number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putInt("VPSpecialCdBase",number);
        }
    }
    public int ultimateCdBase(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getInt("VPUltimateCdBase");
        }
        return 0;
    }

    public void setUltimateCdBase(int number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putInt("VPUltimateCdBase",number);
        }
    }

    public int ultimateChargesBase = 0;
    public int specialChargesBase = 0;

    public long ultimateDurationBase = 0;
    public long specialDurationBase = 0;

    public int specialCdBase = 0;
    public int ultimateCdBase = 0;

    public int vestigeNumber;
    public void dataInit(int vestigeNumber,ChatFormatting color,int specialCharges,int specialCd,int ultimateCharges,int ultimateCd,int specialMaxTime,int ultimateMaxTime,boolean hasDamage){
        setUltimateChargesBase(ultimateCharges);
        setSpecialChargesBase(specialCharges);
        setUltimateCharges(ultimateCharges);
        setUltimateCdBase(ultimateCd* 20);
        setUltimateCd(ultimateCd*20);
        setSpecialCharges(specialCharges);
        setSpecialCdBase(specialCd* 20);
        setSpecialCd(specialCd*20);      //time until recharge in seconds*tps
        this.damageType = hasDamage;
        this.vestigeNumber = vestigeNumber;
        setVestigeNumber(vestigeNumber);
        this.color = color;
        setSpecialMaxTime((long)specialMaxTime*1000);  //max active time in real seconds L
        setSpecialDurationBase((long)specialMaxTime*1000);
        setUltimateMaxTime((long)ultimateMaxTime*1000);
        setUltimateDurationBase((long)ultimateMaxTime*1000);
        this.ultimateChargesBase = ultimateCharges;
        this.specialChargesBase = specialCharges;
        this.ultimateCdBase = ultimateCd*20;
        this.specialCdBase = specialCd*20;
        this.specialDurationBase = (long)specialMaxTime*1000;
        this.ultimateDurationBase = (long)ultimateMaxTime*1000;
    }

    public int getVestigeNumber(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getInt("VPVestigeNumber");
        }
        return 0;
    }

    public void setVestigeNumber(int number){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putInt("VPVestigeNumber",number);
        }
    }

    public static void setStellar(ItemStack stack){
        stack.getOrCreateTag().putBoolean("Stellar", true);
    }

    public static void setDoubleStellar(ItemStack stack){
        stack.getOrCreateTag().putBoolean("DoubleStellar", true);
    }
    public boolean isStellar(ItemStack stack){
        return stack.getOrCreateTag().getBoolean("Stellar");
    }

    public boolean isDoubleStellar(ItemStack stack){
        return stack.getOrCreateTag().getBoolean("DoubleStellar");
    }
    public void init(){
        this.dataInit(0,null,0,0,0,0,0,0,false);
    }

    public boolean isSpecialActive(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getBoolean("VPSpecialActive");
        }
        return false;
    }
    public void setSpecialActive(boolean state){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putBoolean("VPSpecialActive",state);
        }
    }
    public boolean isUltimateActive(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getBoolean("VPUltimateActive");
        }
        return false;
    }

    public void setUltimateActive(boolean state){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putBoolean("VPUltimateActive",state);
        }
    }

    public long time(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getLong("VPTime");
        }
        return 0;
    }
    public void setTime(long time){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putLong("VPTime",time);
        }
    }
    public long timeUlt(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getLong("VPTimeUlt");
        }
        return 0;
    }
    public void setTimeUlt(long time){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putLong("VPTimeUlt",time);
        }
    }
    public int progress = 0;

    public long specialMaxTime(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getLong("VPSpecialMaxTime");
        }
        return 0;
    }

    public void setSpecialMaxTime(long time){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putLong("VPSpecialMaxTime",time);
        }
    }
    public long ultimateMaxTime(){
        if(vestigeStack != null){
            return vestigeStack.getOrCreateTag().getLong("VPUltimateMaxTime");
        }
        return 0;
    }

    public void setUltimateMaxTime(long time){
        if(vestigeStack != null){
            fuckNbt();
            vestigeStack.getOrCreateTag().putLong("VPUltimateMaxTime",time);
        }
    }

    public int setSpecialActive(long seconds, Player player){
        if(currentChargeSpecial() > 0) {
            if(!player.getCommandSenderWorld().isClientSide) {
                setTime(System.currentTimeMillis() + seconds);  //active time in real seconds
                setSpecialActive(true);
                setCdSpecialActive(cdSpecialActive()+specialCd());     //time until cd recharges in seconds*tps
                if(!(VPUtil.getSet(player) == 3 && Math.random() < 0.3) || (VPUtil.getSet(player) == 6 && Math.random() < 0.5))
                    setCurrentChargeSpecial(currentChargeSpecial()-1);
                this.doSpecial(seconds, player, player.getCommandSenderWorld());
            } else this.localSpecial(player);
        }
        return 0;
    }

    public int setUltimateActive(long seconds, Player player){
        if(currentChargeUltimate() > 0) {
            if(!player.getCommandSenderWorld().isClientSide) {
                System.out.println(ultimateMaxTime() + " max time");
                System.out.println(seconds + " seconds");
                System.out.println(ultimateDurationBase() + " duration base");
                System.out.println(timeUlt() + " time before");
                setTimeUlt(System.currentTimeMillis() + seconds);  //active time in real seconds
                System.out.println(timeUlt() + " time after");
                setUltimateActive(true);
                setCdUltimateActive(cdUltimateActive()+ultimateCd());     //time until cd recharges in seconds*tps
                setCurrentChargeUltimate(currentChargeUltimate()-1);
                long bonus = 1+(long)player.getPersistentData().getFloat("VPDurationBonusDonut")/1000;
                this.doUltimate(seconds*bonus, player, player.getCommandSenderWorld());
            } else this.localSpecial(player);
        }
        return 0;
    }

    public void applyBonus(ItemStack stack,Player player){
        setSpecialMaxTime(specialDurationBase());
        setUltimateMaxTime(ultimateDurationBase());
        setSpecialCd(specialCdBase());
        setUltimateCd(ultimateCdBase());
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
            setSpecialMaxTime((long) (specialDurationBase()*1.2));
            setUltimateMaxTime((long) (ultimateDurationBase()*1.2));
        }
        else if(set == 6){
            spAcsBonus -= 1;
            ultAcsBonus -= 1;
        }
        else if(set == 7){
            spAcsBonus += 1;
            ultAcsBonus += 1;
            setSpecialMaxTime((long) (specialDurationBase()*0.6));
            setUltimateMaxTime((long) (ultimateDurationBase()*0.6));
        }
        else if(set == 8){
            spAcsBonus -= 1;
            ultAcsBonus -= 1;
            setSpecialCd((int) (specialCdBase()*1.4));
            setUltimateCd((int) (ultimateCdBase()*1.4));
        }
        setSpecialCharges(specialChargesBase()+specialBonus+specialBonusModifier()+spAcsBonus);
        setUltimateCharges(ultimateChargesBase()+ultimateBonus+ultimateBonusModifier()+ultAcsBonus);
    }

    public Player vestigePlayer = null;

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof  Player player && player.getCommandSenderWorld().isClientSide) {
            ICurioItem.super.curioTick(slotContext, stack);
            return;
        }
        vestigeStack = stack;
        if(this.ultimateCharges() == 0 || this.specialCharges() == 0 || specialCd() == 0 || ultimateCd() == 0)
            this.init();
        isStellar = isStellar(stack);
        Player playerServer = (ServerPlayer) slotContext.entity();
        vestigePlayer = playerServer;
        if(!isStellar(stack) && playerServer.isCreative())
            setStellar(stack);
        if(playerServer != null) {
            if (isSpecialActive())
                whileSpecial(playerServer);
            if (isUltimateActive())
                whileUltimate(playerServer);
        }
        if(time() > 0 && time() <= System.currentTimeMillis()) {
            setTime(0);
            setSpecialActive(false);
            if(playerServer != null)
                specialEnds(playerServer);
        }
        if(timeUlt() > 0 && timeUlt() <= System.currentTimeMillis()) {
            setTimeUlt(0);
            setUltimateActive(false);
            if(playerServer != null)
                ultimateEnds(playerServer);
        }
        if (cdSpecialActive() > 0) {
            setCdSpecialActive(cdSpecialActive()-1);
            if ((this.cdSpecialActive() > this.specialCd() ? (this.cdSpecialActive() % this.specialCd() == 0) : (this.cdSpecialActive() - (this.specialCd()) == 0 || this.cdSpecialActive() == 0)) && this.specialCharges() > this.currentChargeSpecial()) {
                setCurrentChargeSpecial(currentChargeSpecial()+1);
                if(playerServer != null)
                    specialRecharges(playerServer);
            }
        }
        if (cdUltimateActive() > 0) {
            setCdUltimateActive(cdUltimateActive()-1);
            if ((this.cdUltimateActive() > this.ultimateCd() ? (this.cdUltimateActive() % this.ultimateCd() == 0) : (this.cdUltimateActive() - (this.ultimateCd()) == 0 || this.cdUltimateActive() == 0)) && this.ultimateCharges() > this.currentChargeUltimate()) {
                setCurrentChargeUltimate(currentChargeUltimate()+1);
                if(playerServer != null)
                    ultimateRecharges(playerServer);
            }
        }
        if((currentChargeUltimate() == 0 && cdUltimateActive() == 0) || (currentChargeSpecial() == 0 && cdSpecialActive() == 0))
            curioSucks(playerServer,stack);
        /*if(this.currentChargeSpecial() == 0 && this.cdSpecialActive() == 0)
            setCurrentChargeSpecial(specialCharges());
        if(this.currentChargeUltimate() == 0 && this.cdUltimateActive() == 0)
            setCurrentChargeUltimate(ultimateCharges());*/
        /*if(slotContext.entity() != null){
            CompoundTag tag = stack.getTag();
            if(tag == null)
                tag = new CompoundTag();
            tag.putInt("SpecialCharges", this.specialCharges);
            tag.putInt("UltimateCharges", this.ultimateCharges);
            tag.putInt("SpecialCd", this.specialCd);
            tag.putInt("UltimateCd", this.ultimateCd);
            stack.setTag(tag);
        }*/
        playerServer.getPersistentData().putInt("VPCharge"+vestigeNumber,currentChargeSpecial());
        playerServer.getPersistentData().putInt("VPChargeUlt"+vestigeNumber,currentChargeUltimate());
        playerServer.getPersistentData().putLong("VPTime"+vestigeNumber,time());
        playerServer.getPersistentData().putLong("VPTimeUlt"+vestigeNumber,timeUlt());
        ICurioItem.super.curioTick(slotContext, stack);
    }
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (color == null) {
            this.init();
        }
        Player player = Minecraft.getInstance().player;
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            if (stack == null)
                System.out.println("Something went wrong :(((");
            if (Screen.hasShiftDown()) {
                components.add(Component.translatable("vp.passive").withStyle(color));
                components.add(Component.translatable("vp.passive." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                int spCharges;
                int ultCharges;
                int spCd;
                int ultCd;
                if(vestigeStack != null){
                    spCharges = specialCharges();
                    spCd = specialCd();
                    ultCharges = ultimateCharges();
                    ultCd = ultimateCd();
                } else {
                    spCharges = specialChargesBase;
                    spCd = specialCdBase;
                    ultCharges = ultimateChargesBase;
                    ultCd = ultimateCdBase;
                }
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
                int visualUlt = this.cdUltimateActive();
                while (visualUlt > this.ultimateCd()) {
                    visualUlt -= this.ultimateCd();
                }
                int visualSpecial = this.cdSpecialActive();
                while (visualSpecial > this.specialCd()) {
                    visualSpecial -= this.specialCd();
                }
                if(cap.getDebug()){
                    components.add(Component.literal("Special Charges: " + this.currentChargeSpecial()).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("Special Cd: " + visualSpecial).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("Ultimate Charges: " + this.currentChargeUltimate()).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("Ultimate Cd: " + visualUlt).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("IsSpecialActive: " + isSpecialActive()).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("IsUltimateActive: " + isUltimateActive()).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("TimeSpecial: " + (time() - System.currentTimeMillis())).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("TimeUltimate: " + (timeUlt() - System.currentTimeMillis())).withStyle(ChatFormatting.GRAY));
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
                progress = cap.getChallenge(vestigeNumber);
                if(vestigeNumber == 12)
                    progress = EventHandler.getCurses(player);
                components.add(Component.translatable("vp.progress").withStyle(color)
                        .append(Component.literal(" " + progress))
                        .append(Component.literal(" / " + PlayerCapabilityVP.getMaximum(vestigeNumber,player))));
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
        setTime(0);
        setTimeUlt(0);
        setSpecialActive(false);
        setUltimateActive(false);
        setCurrentChargeSpecial(0);
        setCurrentChargeUltimate(0);
        setCdSpecialActive(specialCd()*specialCharges());
        setCdUltimateActive(ultimateCd()*ultimateCharges());
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

    public void doSpecial(long seconds, Player player, Level level){
        player.getPersistentData().putLong("VPAcsSpecial",System.currentTimeMillis()+5000);
    }
    public void doUltimate(long seconds, Player player, Level level){
        player.getPersistentData().putFloat("VPDurationBonusDonut", 0);
        if(vestigeNumber != 3)
            player.getPersistentData().putInt("VPGravity",0);
    }
    public void specialEnds(Player player){

    }
    public void ultimateEnds(Player player){

    }

    public void whileSpecial(Player player){

    }
    public void whileUltimate(Player player){

    }

    public void specialRecharges(Player player){

    }

    public void ultimateRecharges(Player player){

    }

    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_ELYTRA, 1.0f, 1.0f);
    }

    public void refresh(Player player){
        setCdSpecialActive(0);
        setCdUltimateActive(0);
        setCurrentChargeSpecial(specialCharges());
        setCurrentChargeUltimate(ultimateCharges());
        ultimateEnds(player);
        specialEnds(player);
        specialRecharges(player);
        ultimateRecharges(player);
    }

    public void localSpecial(Player player){

    }

    public void localUltimate(Player player){

    }
}
