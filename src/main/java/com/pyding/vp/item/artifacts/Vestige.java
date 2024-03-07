package com.pyding.vp.item.artifacts;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.capability.PlayerCapabilityVP;
import com.pyding.vp.event.EventHandler;
import com.pyding.vp.item.ModCreativeModTab;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Vestige extends Item implements ICurioItem {
    public Vestige() {
        super(new Item.Properties().stacksTo(1));
    }
    public Vestige(Properties properties){
        super(properties);
    }
    List<String> specialText;
    public int specialCharges;
    public int ultimateCharges;
    public int specialCd;
    public int ultimateCd;
    public ChatFormatting color;
    public int cdSpecialActive;
    public int cdUltimateActive;
    public Boolean damageType;

    public float specialBonusModifier = 0;
    public float ultimateBonusModifier = 0;
    public int currentChargeSpecial;
    public int currentChargeUltimate;
    public boolean isStellar = false;
    public boolean isDoubleStellar = false;
    public ServerPlayer serverPlayerFromVestige = null;
    public ServerLevel serverLevelFromVestige = null;

    public boolean isStellar(ItemStack stack){
        CompoundTag tag = stack.getTag();
        if(tag == null)
            tag = new CompoundTag();
        return tag.getBoolean("Stellar");
    }

    public boolean isDoubleStellar(ItemStack stack){
        CompoundTag tag = stack.getTag();
        if(tag == null)
            tag = new CompoundTag();
        return tag.getBoolean("DoubleStellar");
    }
    public int vestigeNumber;
    public void dataInit(int vestigeNumber,ChatFormatting color,int specialCharges,int specialCd,int ultimateCharges,int ultimateCd,int specialMaxTime,int ultimateMaxTime,boolean hasDamage){
        if(isDoubleStellar){
            specialCharges += 1;
            ultimateCharges += 1;
        }
        this.ultimateCharges = ultimateCharges;
        this.ultimateCd = ultimateCd*20;
        this.specialCharges = specialCharges;
        this.specialCd = specialCd*20;      //time until recharge in seconds*tps
        this.damageType = hasDamage;
        this.vestigeNumber = vestigeNumber;
        this.color = color;
        this.specialMaxTime = (long)specialMaxTime*1000;  //max active time in real seconds L
        this.ultimateMaxTime = (long)ultimateMaxTime*1000;
    }

    public int getVestigeNumber(){
        return this.vestigeNumber;
    }
    public static void setStellar(ItemStack stack){
        CompoundTag tag = stack.getTag();
        if(tag == null)
            tag = new CompoundTag();
        tag.putBoolean("Stellar", true);
        stack.setTag(tag);
    }

    public static void setDoubleStellar(ItemStack stack){
        CompoundTag tag = stack.getTag();
        if(tag == null)
            tag = new CompoundTag();
        tag.putBoolean("DoubleStellar", true);
        stack.setTag(tag);
    }
    public void init(){
        this.dataInit(0,null,0,0,0,0,0,0,false);
    }

    public boolean isSpecialActive;
    public boolean isUltimateActive;

    public long time;
    public long timeUlt;
    public int progress = 0;

    public long specialMaxTime = 0;
    public long ultimateMaxTime = 0;
    public long getSpecialMaxTime(){
        return this.specialMaxTime;
    }
    public long getUltimateMaxTime(){
        return this.ultimateMaxTime;
    }

    public int setSpecialActive(long seconds, Player player){
        //System.out.println(isSpecialActive+"from Vestige");
        if(this.currentChargeSpecial > 0) {
            if(!player.getCommandSenderWorld().isClientSide) {
                this.time = System.currentTimeMillis() + seconds;  //active time in real seconds
                this.isSpecialActive = true;
                this.cdSpecialActive += this.specialCd;     //time until cd recharges in seconds*tps
                this.currentChargeSpecial -= 1;
                this.doSpecial(seconds, player, player.getCommandSenderWorld());
            } else this.localSpecial(player);
            return 0;
        } else return cdSpecialActive;
    }

    public int setUltimateActive(long seconds, Player player){
        if(this.currentChargeUltimate > 0){
            if(!player.getCommandSenderWorld().isClientSide) {
                this.timeUlt = System.currentTimeMillis() + seconds;
                this.isUltimateActive = true;
                this.cdUltimateActive += this.ultimateCd;
                this.currentChargeUltimate -= 1;
                this.doUltimate(seconds, player, player.getCommandSenderWorld());
            } else this.localUltimate(player);
            return 0;
        } else return this.cdUltimateActive;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof  Player player && player.getCommandSenderWorld().isClientSide) {
            ICurioItem.super.curioTick(slotContext, stack);
            return;
        }
        ServerPlayer playerServer = (ServerPlayer) slotContext.entity();
        if(playerServer != null) {
            if (isSpecialActive)
                whileSpecial(playerServer);
            if (isUltimateActive)
                whileUltimate(playerServer);
            serverPlayerFromVestige = playerServer;
            serverLevelFromVestige = (ServerLevel) playerServer.getCommandSenderWorld();
        }
        if(this.time > 0 && this.time <= System.currentTimeMillis()) {
            this.time = 0;
            this.isSpecialActive = false;
            if(playerServer != null)
                specialEnds(playerServer);
        }
        if(this.timeUlt > 0 && this.timeUlt <= System.currentTimeMillis()) {
            this.timeUlt = 0;
            this.isUltimateActive = false;
            if(playerServer != null)
                ultimateEnds(playerServer);
        }
        if (this.cdSpecialActive > 0) {
            this.cdSpecialActive -= 1;
            if ((this.cdSpecialActive > this.specialCd ? (this.cdSpecialActive % this.specialCd == 0) : (this.cdSpecialActive - (this.specialCd) == 0 || this.cdSpecialActive == 0)) && this.specialCharges > this.currentChargeSpecial) {
                this.currentChargeSpecial += 1;
                if(playerServer != null)
                    specialRecharges(playerServer);
            }
        }
        if (this.cdUltimateActive > 0) {
            this.cdUltimateActive -= 1;
            if ((this.cdUltimateActive > this.ultimateCd ? (this.cdUltimateActive % this.ultimateCd == 0) : (this.cdUltimateActive - (this.ultimateCd) == 0 || this.cdUltimateActive == 0)) && this.ultimateCharges > this.currentChargeUltimate) {
                this.currentChargeUltimate += 1;
                if(playerServer != null)
                    ultimateRecharges(playerServer);
            }
        }
        if(this.currentChargeSpecial == 0 && this.cdSpecialActive == 0)
            this.currentChargeSpecial = this.specialCharges;
        if(this.currentChargeUltimate == 0 && this.cdUltimateActive == 0)
            this.currentChargeUltimate = this.ultimateCharges;
        if(this.ultimateCharges == 0 || this.specialCharges == 0)
            this.init();
        if(slotContext.entity() != null){
            CompoundTag tag = stack.getTag();
            if(tag == null)
                tag = new CompoundTag();
            tag.putInt("SpecialCharges", this.specialCharges);
            tag.putInt("UltimateCharges", this.ultimateCharges);
            tag.putInt("SpecialCd", this.specialCd);
            tag.putInt("UltimateCd", this.ultimateCd);
            stack.setTag(tag);
        }
        isStellar = isStellar(stack);
        isDoubleStellar = isDoubleStellar(stack);
        playerServer.getPersistentData().putInt("VPCharge"+vestigeNumber,currentChargeSpecial);
        playerServer.getPersistentData().putInt("VPChargeUlt"+vestigeNumber,currentChargeUltimate);
        playerServer.getPersistentData().putLong("VPTime"+vestigeNumber,time);
        playerServer.getPersistentData().putLong("VPTimeUlt"+vestigeNumber,timeUlt);
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
                if(vestigeNumber == 10)
                    components.add(Component.translatable("vp.return").withStyle(color).append(Component.literal("\n"
                            + stack.getOrCreateTag().getString("VPReturnKey") + " "
                            + stack.getOrCreateTag().getDouble("VPReturnX") + "X, "
                            + stack.getOrCreateTag().getDouble("VPReturnY") + "Y, "
                            + stack.getOrCreateTag().getDouble("VPReturnZ") + "Z, ").withStyle(ChatFormatting.GRAY)));
                components.add(Component.translatable("vp.special").withStyle(color)
                        .append(Component.translatable("vp.charges").withStyle(color))
                        .append(Component.literal(" " + this.specialCharges).withStyle(color))
                        .append(Component.translatable("vp.charges2").withStyle(color))
                        .append(Component.literal(" " + this.specialCd / 20).withStyle(color))
                        .append(Component.translatable("vp.seconds").withStyle(color)));
                components.add(Component.translatable("vp.special." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                components.add(Component.translatable("vp.ultimate").withStyle(color)
                        .append(Component.translatable("vp.charges").withStyle(color))
                        .append(Component.literal(" " + this.ultimateCharges).withStyle(color))
                        .append(Component.translatable("vp.charges2").withStyle(color))
                        .append(Component.literal(" " + this.ultimateCd / 20).withStyle(color))
                        .append(Component.translatable("vp.seconds").withStyle(color)));
                components.add(Component.translatable("vp.ultimate." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                if(vestigeNumber == 10)
                    components.add(Component.translatable("vp.worlds").withStyle(color).append(Component.literal("\n" + cap.getDimensions()).withStyle(ChatFormatting.GRAY)));
                if (damageType) {
                    components.add(Component.translatable("vp.damage").withStyle(color));
                    components.add(Component.translatable("vp.damagetype." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                }
                if (isStellar(stack)) {
                    components.add(Component.literal(VPUtil.getRainbowString("Stellar: ")));
                } else {
                    components.add(Component.translatable(("Stellar")).withStyle(color));
                }
                components.add(Component.translatable(("vp.stellarText")).withStyle(ChatFormatting.GRAY).append(Component.translatable("vp.stellar." + vestigeNumber)));
                int visualUlt = this.cdUltimateActive;
                while (visualUlt > this.ultimateCd) {
                    visualUlt -= this.ultimateCd;
                }
                int visualSpecial = this.cdSpecialActive;
                while (visualSpecial > this.specialCd) {
                    visualSpecial -= this.specialCd;
                }
                if(cap.getDebug()){
                    components.add(Component.literal("Special Charges: " + this.currentChargeSpecial).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("Special Cd: " + visualSpecial).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("Ultimate Charges: " + this.currentChargeUltimate).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("Ultimate Cd: " + visualUlt).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("IsSpecialActive: " + isSpecialActive).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("IsUltimateActive: " + isUltimateActive).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("TimeSpecial: " + (time - System.currentTimeMillis())).withStyle(ChatFormatting.GRAY));
                    components.add(Component.literal("TimeUltimate: " + (timeUlt - System.currentTimeMillis())).withStyle(ChatFormatting.GRAY));
                }
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
                components.add(Component.literal(cap.getChance()+"% ").withStyle(color).append(Component.translatable("vp.chance").withStyle(ChatFormatting.GRAY).append(Component.literal(VPUtil.getRainbowString("Stellar")))));
                components.add(Component.translatable("vp.chance2").withStyle(ChatFormatting.GRAY).append(Component.literal(ConfigHandler.COMMON.stellarChanceIncrease.get() + "%")));
                components.add(Component.translatable("vp.getText1").withStyle(ChatFormatting.GRAY).append(Component.literal(VPUtil.formatMilliseconds(VPUtil.coolDown())+" ").withStyle(ChatFormatting.GRAY)));
                if (cap.hasCoolDown(vestigeNumber))
                    components.add(Component.translatable("vp.getText2").append(Component.literal((VPUtil.formatMilliseconds(VPUtil.coolDown()-(System.currentTimeMillis() - cap.getTimeCd()))))));
                if(player.isCreative())
                    components.add(Component.translatable("vp.creative").withStyle(ChatFormatting.DARK_PURPLE));
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

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        this.time = 0;
        this.timeUlt = 0;
        this.isSpecialActive = false;
        this.isUltimateActive = false;
        Player player = (Player) slotContext.entity();
        player.getPersistentData().putFloat("VPShield",0);
        player.getPersistentData().putFloat("VPOverShield",0);
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        this.cdUltimateActive = this.ultimateCd*this.ultimateCharges;
        this.cdSpecialActive = this.specialCd*this.specialCharges;
        this.currentChargeUltimate = 0;
        this.currentChargeSpecial = 0;
        Player player = (Player) slotContext.entity();
        if(!isStellar(stack) && player.isCreative()) {
            setStellar(stack);
        }
        player.getPersistentData().putFloat("VPShield",0);
        player.getPersistentData().putFloat("VPOverShield",0);
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
    }

    public void doSpecial(long seconds, Player player, Level level){}
    public void doUltimate(long seconds, Player player, Level level){
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
        this.cdSpecialActive = 0;
        this.cdUltimateActive = 0;
        this.currentChargeUltimate = this.ultimateCharges;
        this.currentChargeSpecial = this.specialCharges;
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
