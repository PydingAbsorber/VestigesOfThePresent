package com.pyding.vp.item.vestiges;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.mixin.BucketMixin;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ItemAnimationPacket;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.GradientUtil;
import com.pyding.vp.util.KeyBinding;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
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

    public static int maxCurses = 5;

    public Boolean damageType;
    public int ultimateChargesBase;
    public int specialChargesBase;

    public long ultimateDurationBase;
    public long specialDurationBase;

    public int specialCdBase;
    public int ultimateCdBase;

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

    public static void setStellar(ItemStack stack, Player player) {
        stack.getOrCreateTag().putBoolean("Stellar", true);
        if(stack.getItem() instanceof Vestige vestige)
            vestige.applyBonus(stack,player);
    }

    public static boolean isStellar(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("Stellar");
    }

    public static void setDoubleStellar(ItemStack stack, Player player) {
        stack.getOrCreateTag().putBoolean("DoubleStellar", true);
        if(stack.getItem() instanceof Vestige vestige)
            vestige.applyBonus(stack,player);
    }

    public static boolean isDoubleStellar(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("DoubleStellar");
    }

    public static void setTripleStellar(ItemStack stack, Player player) {
        stack.getOrCreateTag().putBoolean("TripleStellar", true);
        if(stack.getItem() instanceof Vestige vestige)
            vestige.applyBonus(stack,player);
    }

    public static boolean isTripleStellar(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("TripleStellar");
    }

    public static void increaseStars(ItemStack stack, Player player){
        if(isDoubleStellar(stack))
            setTripleStellar(stack, player);
        else if(isStellar(stack))
            setDoubleStellar(stack,player);
        else setStellar(stack,player);
    }

    public static void decreaseStars(ItemStack stack){
        if(isTripleStellar(stack))
            stack.getOrCreateTag().putBoolean("TripleStellar", false);
        else if(isDoubleStellar(stack))
            stack.getOrCreateTag().putBoolean("DoubleStellar", false);
        else if(isStellar(stack))
            stack.getOrCreateTag().putBoolean("Stellar", false);
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
        if(player.getPersistentData().getLong("VPForbidden") > System.currentTimeMillis()){
            if(player instanceof ServerPlayer serverPlayer)
                PacketHandler.sendToClient(new ItemAnimationPacket(new ItemStack(Blocks.BARRIER)),serverPlayer);
            return 0;
        }
        if(currentChargeSpecial(stack) > 0) {
            if(!player.getCommandSenderWorld().isClientSide) {
                setTime(System.currentTimeMillis() + seconds,stack);  //active time in real seconds
                setSpecialActive(true,stack);
                setCdSpecialActive(cdSpecialActive(stack)+specialCd(stack),stack);     //time until cd recharges in seconds*tps
                Random random = new Random();
                if(!(VPUtil.getSet(player) == 3 && random.nextDouble() < VPUtil.getChance(0.3,player)) || !(VPUtil.getSet(player) == 6 && random.nextDouble() < VPUtil.getChance(0.5,player)) || random.nextDouble() < VPUtil.getChance(player.getPersistentData().getFloat("VPDepth")/10,player))
                    setCurrentChargeSpecial(currentChargeSpecial(stack) - 1, stack);
                if(damageType == null)
                    init(stack);
                this.doSpecial(seconds, player, player.getCommandSenderWorld(), stack);
                if(VPUtil.hasCurse(player,3))
                    player.getPersistentData().putLong("VPForbidden",System.currentTimeMillis()+3000);
            } else this.localSpecial(player);
        }
        return 0;
    }

    public int setUltimateActive(long seconds, Player player, ItemStack stack){
        if(player.getPersistentData().getLong("VPForbidden") > System.currentTimeMillis()){
            if(player instanceof ServerPlayer serverPlayer)
                PacketHandler.sendToClient(new ItemAnimationPacket(new ItemStack(Blocks.BARRIER)),serverPlayer);
            return 0;
        }
        if(currentChargeUltimate(stack) > 0) {
            if(!player.getCommandSenderWorld().isClientSide) {
                setTimeUlt(System.currentTimeMillis() + seconds,stack);  //active time in real seconds
                setUltimateActive(true,stack);
                setCdUltimateActive(cdUltimateActive(stack)+ultimateCd(stack),stack);     //time until cd recharges in seconds*tps
                Random random = new Random();
                if(!(VPUtil.getSet(player) == 3 && random.nextDouble() < VPUtil.getChance(0.3,player)) || !(VPUtil.getSet(player) == 6 && random.nextDouble() < VPUtil.getChance(0.5,player)) || random.nextDouble() < VPUtil.getChance(player.getPersistentData().getFloat("VPDepth")/10,player))
                    setCurrentChargeUltimate(currentChargeUltimate(stack)-1,stack);
                long bonus = 1+(long)player.getPersistentData().getFloat("VPDurationBonusDonut")/1000;
                if(damageType == null)
                    init(stack);
                this.doUltimate(seconds*bonus, player, player.getCommandSenderWorld(), stack);
            } else this.localSpecial(player);
        }
        return 0;
    }
    public float specialTimeBonus = 1;
    public float ultimateTimeBonus = 1;
    public void applyBonus(ItemStack stack,Player player){
        int specialBonus = 0;
        int ultimateBonus = 0;
        if(isDoubleStellar(stack)){
            specialBonus += 1;
            ultimateBonus += 1;
        }
        int spAcsBonus = 0;
        int ultAcsBonus = 0;
        float specialCdBonus = 1;
        float ultimateCdBonus = 1;
        int set = VPUtil.getSet(player);
        float curse = VPUtil.getCurseMultiplier(player,6);
        if(curse > 0){
            specialCdBonus += curse;
            ultimateCdBonus += curse;
        }
        if(set == 1){
            spAcsBonus += 1;
            specialTimeBonus += 0.2f;
            ultimateTimeBonus += 0.2f;
        }
        else if(set == 6){
            spAcsBonus -= 1;
        }
        else if(set == 7){
            //spAcsBonus += 1;
            ultAcsBonus += 1;
            specialTimeBonus -= 0.4f;
            ultimateTimeBonus -= 0.4f;
        }
        else if(set == 8){
            specialTimeBonus += 1f;
            ultimateTimeBonus += 1f;
            specialCdBonus += 0.4f;
            ultimateCdBonus += 0.4f;
        }
        if(player.getPersistentData().getLong("VPJuke") >= System.currentTimeMillis() && VPUtil.hasVestige(ModItems.LYRA.get(), player)){
            specialTimeBonus += 0.4f;
            ultimateTimeBonus += 0.4f;
            specialCdBonus -= 0.4f;
            ultimateCdBonus -= 0.4f;
        }
        if(VPUtil.hasLyra(player,7)){
            specialCdBonus -= 0.2f;
            ultimateCdBonus -= 0.2f;
        }
        if(VPUtil.isPoisonedByNightmare(player)) {
            specialTimeBonus -= 0.5f;
            ultimateTimeBonus -= 0.5f;
        }
        setSpecialCd((int) (specialCdBase(stack)*Math.max(0.1,specialCdBonus)),stack); //cauton!!!
        setUltimateCd((int) (ultimateCdBase(stack)*Math.max(0.1,ultimateCdBonus)),stack);
        setSpecialMaxTime((int) (specialDurationBase(stack)*Math.max(0.1,specialTimeBonus)),stack);
        setUltimateMaxTime((int) (ultimateDurationBase(stack)*Math.max(0.1,ultimateTimeBonus)),stack);
        setSpecialCharges(specialChargesBase(stack)+specialBonus+specialBonusModifier(stack)+spAcsBonus,stack);
        setUltimateCharges(ultimateChargesBase(stack)+ultimateBonus+ultimateBonusModifier(stack)+ultAcsBonus,stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof  Player player && player.getCommandSenderWorld().isClientSide) {
            ICurioItem.super.curioTick(slotContext, stack);
            return;
        }
        if (color == null || specialCharges(stack) == 0 || ultimateCharges(stack) == 0 || specialCdBase == 0 || ultimateChargesBase == 0 || damageType == null) {
            this.init(stack);
        }
        ServerPlayer playerServer = (ServerPlayer) slotContext.entity();
        if(!isStellar(stack) && playerServer.isCreative())
            setStellar(stack,playerServer);
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
        if((currentChargeUltimate(stack) == 0 && cdUltimateActive(stack) == 0) || (currentChargeSpecial(stack) == 0 && cdSpecialActive(stack) == 0) || specialCdBase == 0) {
            setTime(0, stack);
            setTimeUlt(0, stack);
            setSpecialActive(false, stack);
            setUltimateActive(false, stack);
            setCurrentChargeSpecial(0, stack);
            setCurrentChargeUltimate(0, stack);
            setCdSpecialActive(specialCd(stack) * specialCharges(stack), stack);
            setCdUltimateActive(ultimateCd(stack) * ultimateCharges(stack), stack);
            curioSucks(playerServer, stack);
        }
        ICurioItem.super.curioTick(slotContext, stack);
    }
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (color == null || specialCharges(stack) == 0 || ultimateCharges(stack) == 0 || specialCdBase == 0 || ultimateChargesBase == 0 || damageType == null) {
            this.init(stack);
        }
        Player player = Minecraft.getInstance().player;
        boolean stellar = isStellar(stack);
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
                String firstKey = "";
                String secondKey = "";
                if(!VPUtil.getFirstVestige(player).isEmpty() && VPUtil.getFirstVestige(player).get(0) == stack){
                    if(!KeyBinding.FIRST_KEY.getKeyModifier().name().equals("NONE"))
                        firstKey += KeyBinding.FIRST_KEY.getKeyModifier().name() + "+";
                    firstKey += KeyBinding.FIRST_KEY.getKey().getDisplayName().getString();
                    if(!KeyBinding.FIRST_KEY_ULT.getKeyModifier().name().equals("NONE"))
                        secondKey += KeyBinding.FIRST_KEY_ULT.getKeyModifier().name() + "+";
                    secondKey += KeyBinding.FIRST_KEY_ULT.getKey().getDisplayName().getString();
                } else {
                    if(!KeyBinding.SECOND_KEY.getKeyModifier().name().equals("NONE"))
                        firstKey += KeyBinding.SECOND_KEY.getKeyModifier().name() + "+";
                    firstKey += KeyBinding.SECOND_KEY.getKey().getDisplayName().getString();
                    if(!KeyBinding.SECOND_KEY_ULT.getKeyModifier().name().equals("NONE"))
                        secondKey += KeyBinding.SECOND_KEY_ULT.getKeyModifier().name() + "+";
                    secondKey += KeyBinding.SECOND_KEY_ULT.getKey().getDisplayName().getString();
                }
                components.add(Component.translatable("vp.special").withStyle(color)
                        .append(Component.translatable("vp.charges").withStyle(color))
                        .append(Component.literal(" " + spCharges ).withStyle(color))
                        .append(Component.translatable("vp.charges2").withStyle(color))
                        .append(Component.literal(" " + spCd / 20).withStyle(color))
                        .append(Component.translatable("vp.seconds").withStyle(color))
                        .append(Component.literal(" "))
                        .append(Component.translatable("vp.activation"))
                        .append(Component.literal(" " + firstKey)));
                if(vestigeNumber == 2){
                    components.add(Component.translatable("vp.special." + vestigeNumber,ConfigHandler.COMMON.crownShield.get()+"%").withStyle(ChatFormatting.GRAY));
                }
                else components.add(Component.translatable("vp.special." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                if(vestigeNumber == 25 && System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("linux")){
                    components.add(GradientUtil.customGradient(Component.translatable("vp.archlinx.easter").getString(),GradientUtil.BLUE_LIGHT_BLUE));
                }
                components.add(Component.translatable("vp.ultimate").withStyle(color)
                        .append(Component.translatable("vp.charges").withStyle(color))
                        .append(Component.literal(" " + ultCharges ).withStyle(color))
                        .append(Component.translatable("vp.charges2").withStyle(color))
                        .append(Component.literal(" " + ultCd / 20).withStyle(color))
                        .append(Component.translatable("vp.seconds").withStyle(color))
                        .append(Component.literal(" "))
                        .append(Component.translatable("vp.activation"))
                        .append(Component.literal(" " + secondKey)));
                if(vestigeNumber == 15){
                    components.add(Component.translatable("vp.ultimate." + vestigeNumber,ConfigHandler.COMMON.devourer.get(),ConfigHandler.COMMON.devourerChance.get()*100+"%").withStyle(ChatFormatting.GRAY));
                }
                else if (vestigeNumber == 23){
                    components.add(Component.translatable("vp.ultimate." + vestigeNumber,10+cap.getPearls()*5).withStyle(ChatFormatting.GRAY));
                }
                else if (vestigeNumber == 6){
                    components.add(Component.translatable("vp.ultimate." + vestigeNumber,ConfigHandler.COMMON.donutMaxSaturation.get()).withStyle(ChatFormatting.GRAY));
                }
                else components.add(Component.translatable("vp.ultimate." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                if(vestigeNumber == 10)
                    components.add(Component.translatable("vp.worlds").withStyle(color).append(Component.literal("\n" + cap.getDimensions()).withStyle(ChatFormatting.GRAY)));
                if (damageType) {
                    components.add(Component.translatable("vp.damage").withStyle(color));
                    components.add(Component.translatable("vp.damagetype." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                }
                if (stellar) {
                    if(isTripleStellar(stack))
                        components.add(GradientUtil.stellarGradient("Triple Stellar: "));
                    else if(isDoubleStellar(stack))
                        components.add(GradientUtil.stellarGradient("Double Stellar: "));
                    else components.add(GradientUtil.stellarGradient("Stellar: "));
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
                if(isDoubleStellar(stack))
                    components.add(GradientUtil.stellarGradient(Component.translatable("vp.double_stellar").getString()));
                if(isTripleStellar(stack))
                    components.add(GradientUtil.stellarGradient(Component.translatable("vp.triple_stellar").getString()));
                components.add(Component.translatable("config").withStyle(ChatFormatting.GRAY));
            } else if (Screen.hasControlDown()) {
                components.add(Component.translatable("vp.challenge").withStyle(ChatFormatting.GRAY).append(GradientUtil.stellarGradient(VPUtil.generateRandomString(7) + " :")));
                if(vestigeNumber == 9)
                    components.add(Component.translatable("vp.get." + vestigeNumber).withStyle(ChatFormatting.GRAY).append(Component.literal(cap.getGoldenChance()+"%").withStyle(ChatFormatting.GRAY)));
                else if(vestigeNumber == 14){
                    components.add(Component.translatable("vp.get." + vestigeNumber,ConfigHandler.COMMON.chaostime.get(),player.getPersistentData().getInt("VPMaxChallenge"+vestigeNumber)).withStyle(ChatFormatting.GRAY));
                    components.add(Component.translatable("vp.chaos").withStyle(ChatFormatting.GRAY).append(Component.literal(cap.getRandomEntity())));
                    components.add(Component.translatable("vp.chaos2").withStyle(ChatFormatting.GRAY).append(VPUtil.formatMilliseconds(cap.getChaosTime()+VPUtil.getChaosTime()-System.currentTimeMillis())));
                }
                else if(vestigeNumber == 16){
                    components.add(Component.translatable("vp.get." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                    if(ConfigHandler.COMMON.failFlowers.get())
                        components.add(Component.translatable("vp.get.16.fail").withStyle(ChatFormatting.GRAY));
                }
                else {
                    int[] hobbyHorsing = {1,4,5,7,14,15,18,19,23,25};
                    boolean yesHorsing = false;
                    for (int horse : hobbyHorsing) {
                        if (horse == vestigeNumber) {
                            yesHorsing = true;
                            break;
                        }
                    }
                    if(yesHorsing)
                        components.add(Component.translatable("vp.get." + vestigeNumber,player.getPersistentData().getInt("VPMaxChallenge"+vestigeNumber)).withStyle(ChatFormatting.GRAY));
                    else if(vestigeNumber == 13)
                        components.add(Component.translatable("vp.get." + vestigeNumber,player.getPersistentData().getInt("VPMaxChallenge"+vestigeNumber),ConfigHandler.COMMON.rareItemChance.get()*100+"%").withStyle(ChatFormatting.GRAY));
                    else components.add(Component.translatable("vp.get." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                }
                int progress = cap.getChallenge(vestigeNumber);
                if(vestigeNumber == 12)
                    progress = VPUtil.getCurseAmount(player);
                if(vestigeNumber == 24){
                    boolean fuckThisStupidGame = false;
                    boolean fuckThisStupidGame2 = false;
                    for(MobBucketItem bucketItem: VPUtil.getBuckets()){
                        List<String> allFish = new ArrayList<>(VPUtil.fishTypesFromBucket(bucketItem));
                        EntityType<?> type = ((BucketMixin)bucketItem).getFishSup().get();
                        List<String> bucketFish = new ArrayList<>();
                        for(String fish: cap.getSea().split(",")){
                            for (String name: allFish) {
                                if (fish.trim().contains(name))
                                    bucketFish.add(fish.trim());
                            }
                        }
                        boolean axolotl = ((BucketMixin)bucketItem).getFishSup().get().getDescriptionId().contains("entity.minecraft.axolotl");
                        boolean tropic = ((BucketMixin)bucketItem).getFishSup().get().getDescriptionId().contains("entity.minecraft.tropical_fish");
                        if(axolotl){
                            if(fuckThisStupidGame)
                                continue;
                            else fuckThisStupidGame = true;
                        }
                        if(tropic){
                            if(fuckThisStupidGame2)
                                continue;
                            else fuckThisStupidGame2 = true;
                        }
                        if(allFish.size() <= 2)
                            continue;
                        int bucketProgress = bucketFish.size();
                        components.add(Component.translatable(type.getDescriptionId()).append(Component.literal(":")).withStyle(color));
                        components.add(Component.translatable("vp.progress").withStyle(ChatFormatting.GRAY)
                                .append(Component.literal(" " + bucketProgress))
                                .append(Component.literal(" / " + VPUtil.fishTypesFromBucket(bucketItem).size())));
                    }
                    components.add(Component.translatable("vp.fish").withStyle(color)
                            .append(Component.literal(" " + progress))
                            .append(Component.literal(" / " + player.getPersistentData().getInt("VPMaxChallenge" + vestigeNumber))));
                } else {
                    components.add(Component.translatable("vp.progress").withStyle(color)
                            .append(Component.literal(" " + progress))
                            .append(Component.literal(" / " + player.getPersistentData().getInt("VPMaxChallenge" + vestigeNumber))));
                }
                double stellarChance = VPUtil.getChance((double) cap.getChance()/100,player)*100;
                if(VPUtil.getSet(player) == 9)
                    stellarChance += 5;
                if(cap.getVip() > System.currentTimeMillis())
                    stellarChance += 10;
                components.add(Component.literal((int)stellarChance+"% ").withStyle(color).append(Component.translatable("vp.chance").withStyle(ChatFormatting.GRAY).append(GradientUtil.stellarGradient("Stellar"))));
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
                        text = VPUtil.filterAndTranslate(VPUtil.getFoodLeft(cap.getFoodEaten()).toString(),ChatFormatting.GRAY).getString();
                        break;
                    }
                    case 11:{
                        text = VPUtil.getDamageKindsLeft(cap.getDamageDie()).toString();
                        break;
                    }
                    case 13:{
                        text = VPUtil.getRaresClient(player).toString();
                        break;
                    }
                    case 15: {
                        text = VPUtil.getBossClient(player).toString();
                        break;
                    }
                    case 16: {
                        text = VPUtil.filterAndTranslate(VPUtil.getFlowersLeft(cap.getFlowers()).toString(),ChatFormatting.GRAY).getString();
                        break;
                    }
                    case 17: {
                        text = VPUtil.filterAndTranslate(VPUtil.getEffectsLeft(cap.getEffects()).toString(),ChatFormatting.GRAY).getString();
                        break;
                    }
                    case 20:{
                        text = VPUtil.getMobsClient(player).toString();
                        break;
                    }
                    case 21:{
                        text = VPUtil.filterAndTranslate(VPUtil.getTemplatesLeft(cap.getTemplates()).toString()).getString();
                        break;
                    }
                    case 22:{
                        text =  VPUtil.filterAndTranslate(VPUtil.getMusicDisksLeft(cap.getMusic()).toString()).getString();
                        break;
                    }
                    case 24:{
                        text =  VPUtil.filterAndTranslate(VPUtil.getSeaLeft(cap.getSea()).toString()).getString();
                        break;
                    }
                    default:
                        text = null;
                }
                if(vestigeNumber == 24)
                    components.add(Component.translatable("vp.fish.2").withStyle(ChatFormatting.BLUE));
                if(vestigeNumber == 22)
                    components.add(Component.translatable("vp.lyre.authors").withStyle(ChatFormatting.BLUE));
                if (text != null)
                    components.add(Component.literal(text).withStyle(ChatFormatting.GRAY));
                if(vestigeNumber == 24){
                    boolean fuckThisStupidGame = false;
                    boolean fuckThisStupidGame2 = false;
                    for(MobBucketItem bucketItem: VPUtil.getBuckets()){
                        EntityType<?> type = ((BucketMixin)bucketItem).getFishSup().get();
                        List<String> bucketFish = new ArrayList<>();
                        List<String> allFish = new ArrayList<>(VPUtil.fishTypesFromBucket(bucketItem));
                        for(String fish: cap.getSea().split(",")){
                            for (String name: allFish) {
                                if (fish.trim().contains(name))
                                    bucketFish.add(fish.trim());
                            }
                        }
                        boolean axolotl = ((BucketMixin)bucketItem).getFishSup().get().getDescriptionId().contains("entity.minecraft.axolotl");
                        boolean tropic = ((BucketMixin)bucketItem).getFishSup().get().getDescriptionId().contains("entity.minecraft.tropical_fish");
                        if(axolotl){
                            if(fuckThisStupidGame)
                                continue;
                            else fuckThisStupidGame = true;
                        }
                        if(tropic){
                            if(fuckThisStupidGame2)
                                continue;
                            else fuckThisStupidGame2 = true;
                        }
                        if(allFish.size() == bucketFish.size() || allFish.size() <= 2)
                            continue;
                        allFish.removeAll(bucketFish);
                        components.add(Component.translatable(type.getDescriptionId()).append(Component.literal(":")).withStyle(ChatFormatting.BLUE));
                        components.add(VPUtil.filterAndTranslate(allFish.toString()));
                    }
                }
            } else {
                if(stellar && !Component.translatable("vp.meme."+vestigeNumber).getString().isEmpty())
                    components.add(Component.translatable("vp.meme."+vestigeNumber).withStyle(color));
                components.add(Component.translatable("vp.short." + vestigeNumber).withStyle(color));
                components.add(Component.translatable("vp.press").append(Component.literal("SHIFT").withStyle(color).append(Component.translatable("vp.shift"))));
                components.add(Component.translatable("vp.press").append(Component.literal("CTRL").withStyle(color).append(Component.translatable("vp.ctrl"))));
                if (vestigeNumber == 2 || vestigeNumber == 6 || vestigeNumber == 10 || vestigeNumber == 11 || vestigeNumber == 13 || vestigeNumber == 15 || vestigeNumber == 16 || vestigeNumber == 17 || vestigeNumber == 20)
                    components.add(Component.translatable("vp.press").append(Component.literal("ALT").withStyle(color).append(Component.translatable("vp.alt"))));
                if(vestigeNumber == 3)
                    components.add(Component.translatable("vp.press").append(Component.literal("ALT").withStyle(color).append(Component.translatable("vp.alt.atlas"))));
                //if(vestigeNumber == 13)
                    //components.add(Component.translatable("vp.press").append(Component.literal("ALT").withStyle(color).append(Component.translatable("vp.alt.prism"))));
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

            int curse = VPUtil.getVestigeCurse(stack);
            if(curse > 0) {
                int multiplier = 0;
                if(curse == 1) {
                    multiplier = 90;
                    if(stellar)
                        multiplier = 75;
                    if(isDoubleStellar(stack))
                        multiplier = 45;
                    if(isTripleStellar(stack))
                        multiplier = 35;
                }
                else if (curse == 2){
                    multiplier = -80;
                    if(stellar)
                        multiplier = -65;
                    if(isDoubleStellar(stack))
                        multiplier = -50;
                    if(isTripleStellar(stack))
                        multiplier = -40;
                }
                else if(curse == 3){
                    multiplier = 3;
                    if(stellar)
                        multiplier = 2;
                    if(isDoubleStellar(stack))
                        multiplier = 1;
                    if(isTripleStellar(stack))
                        multiplier = 1;
                }
                else if(curse == 4){
                    multiplier = 40;
                    if(stellar)
                        multiplier = 70;
                    if(isDoubleStellar(stack))
                        multiplier = 110;
                    if(isTripleStellar(stack))
                        multiplier = 160;
                }
                else if(curse == 5){
                    multiplier = 50;
                    if(stellar)
                        multiplier = 125;
                    if(isDoubleStellar(stack))
                        multiplier = 250;
                    if(isTripleStellar(stack))
                        multiplier = 350;
                }
                else if(curse == 6){
                    multiplier = 60;
                    if(isDoubleStellar(stack))
                        multiplier = 50;
                    if(isTripleStellar(stack))
                        multiplier = 40;
                }
                components.add(Component.translatable("vp.curse." + curse,multiplier+"%").withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.RED));
            }

            components.add(Component.translatable("vp.info").withStyle(ChatFormatting.GRAY));
            /*if (stellar && player.level().isClientSide()) {
                Component component = GradientUtil.stellarGradient(I18n.get("vp.name."+vestigeNumber).substring(2));
                stack.setHoverName(component);
            }*/
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

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return isTripleStellar(stack);
    }

    public void curioSucks(Player player, ItemStack stack){
        if(player.getCommandSenderWorld().isClientSide)
            return;
        if(!isTripleStellar(stack)){
            setTime(0, stack);
            setTimeUlt(0, stack);
            setSpecialActive(false, stack);
            setUltimateActive(false, stack);
            setCurrentChargeSpecial(0, stack);
            setCurrentChargeUltimate(0, stack);
            setCdSpecialActive(specialCd(stack) * specialCharges(stack), stack);
            setCdUltimateActive(ultimateCd(stack) * ultimateCharges(stack), stack);
        }
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
        applyBonus(stack,player); //cauton!!!
    }
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack){
        player.getPersistentData().putFloat("VPDurationBonusDonut", 0);
        if(vestigeNumber != 3)
            player.getPersistentData().putInt("VPGravity",0);
        applyBonus(stack,player); //cauton!!!
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
