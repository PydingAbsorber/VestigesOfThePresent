package com.pyding.vp.item.artifacts;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.capability.PlayerCapabilityVP;
import com.pyding.vp.item.ModCreativeModTab;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class Vestige extends Item implements ICurioItem {
    public Vestige() {
        super(new Item.Properties().stacksTo(1).tab(ModCreativeModTab.tab));
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


    public int currentChargeSpecial;
    public int currentChargeUltimate;

    public boolean isStellar(ItemStack stack){
        CompoundTag tag = stack.getTag();
        if(tag == null)
            tag = new CompoundTag();
        return tag.getBoolean("Stellar");
    }
    public String stellarBoost;
    public int vestigeNumber;
    public void dataInit(int vestigeNumber,ChatFormatting color,int specialCharges,int specialCd,int ultimateCharges,int ultimateCd,int specialMaxTime,int ultimateMaxTime,boolean hasDamage){
        this.ultimateCharges = ultimateCharges;
        this.ultimateCd = ultimateCd*20;
        this.specialCharges = specialCharges;
        this.specialCd = specialCd*20;
        this.damageType = hasDamage;
        this.vestigeNumber = vestigeNumber;
        this.color = color;
        this.specialMaxTime = specialMaxTime*20;
        this.ultimateMaxTime = ultimateMaxTime*20;
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
    public void init(){
        this.dataInit(0,null,0,0,0,0,0,0,false);
    }

    public boolean isSpecialActive;
    public boolean isUltimateActive;

    public long time;
    public long timeUlt;
    public Player player;
    public int progress = 0;

    public int specialMaxTime = 0;
    public int ultimateMaxTime = 0;
    public int getSpecialMaxTime(){
        return this.specialMaxTime;
    }
    public int getUltimateMaxTime(){
        return this.ultimateMaxTime;
    }

    public int setSpecialActive(long seconds, Player player){
        if(this.currentChargeSpecial > 0) {
            seconds *= 1000;
            this.time = System.currentTimeMillis() + seconds;
            this.isSpecialActive = true;
            this.cdSpecialActive += this.specialCd;
            this.currentChargeSpecial -= 1;
            this.doSpecial(seconds, player, player.level);
            return 0;
        } else return cdSpecialActive;
    }

    public int setUltimateActive(long seconds, Player player){
        if(this.currentChargeUltimate > 0){
            this.timeUlt = System.currentTimeMillis() + seconds;
            this.isUltimateActive = true;
            this.cdUltimateActive += this.ultimateCd;
            this.currentChargeUltimate -= 1;
            this.doUltimate(seconds, player, player.level);
            return 0;
        } else return this.cdUltimateActive;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(this.time > 0 && this.time <= System.currentTimeMillis()) {
            this.time = 0;
            this.isSpecialActive = false;
        }
        if(this.timeUlt > 0 && this.timeUlt <= System.currentTimeMillis()) {
            this.timeUlt = 0;
            this.isUltimateActive = false;
        }
        if (this.cdSpecialActive > 0) {
            this.cdSpecialActive -= 1;
            if ((this.cdSpecialActive > this.specialCd ? (this.cdSpecialActive % this.specialCd == 0) : (this.cdSpecialActive - (this.specialCd) == 0 || this.cdSpecialActive == 0)) && this.specialCharges > this.currentChargeSpecial) {
                this.currentChargeSpecial += 1;
            }
        }
        if (this.cdUltimateActive > 0) {
            this.cdUltimateActive -= 1;
            if ((this.cdUltimateActive > this.ultimateCd ? (this.cdUltimateActive % this.ultimateCd == 0) : (this.cdUltimateActive - (this.ultimateCd) == 0 || this.cdUltimateActive == 0)) && this.ultimateCharges > this.currentChargeUltimate) {
                this.currentChargeUltimate += 1;
            }
        }
        if(this.currentChargeSpecial == 0 && this.cdSpecialActive == 0)
            this.currentChargeSpecial = this.specialCharges;
        if(this.currentChargeUltimate == 0 && this.cdUltimateActive == 0)
            this.currentChargeUltimate = this.ultimateCharges;
        if(this.ultimateCharges == 0 || this.specialCharges == 0)
            this.init();
        if(slotContext.entity() != null && slotContext.entity() instanceof Player && (player == null || player instanceof LocalPlayer)) {
            Player playerServer = (Player) slotContext.entity();
        }
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
        ICurioItem.super.curioTick(slotContext, stack);
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (color == null) {
            this.init();
        }

        if (player == null)
            player = Minecraft.getInstance().player;
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            if (stack == null)
                System.out.println("Something went wrong :(((");
            if (Screen.hasShiftDown()) {
                components.add(Component.translatable("vp.passive").withStyle(color));
                components.add(Component.translatable("vp.passive." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                components.add(Component.translatable("vp.special").withStyle(color)
                        .append(Component.literal(" " + this.specialCharges).withStyle(color))
                        .append(Component.translatable("vp.charges").withStyle(color))
                        .append(Component.literal(" " + this.specialCd / 20).withStyle(color)));
                components.add(Component.translatable("vp.special." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                components.add(Component.translatable("vp.ultimate").withStyle(color)
                        .append(Component.literal(" " + this.ultimateCharges).withStyle(color))
                        .append(Component.translatable("vp.charges").withStyle(color))
                        .append(Component.literal(" " + this.ultimateCd / 20).withStyle(color)));
                components.add(Component.translatable("vp.ultimate." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                if (damageType) {
                    components.add(Component.translatable("vp.damage").withStyle(color));
                    components.add(Component.translatable("vp.damagetype." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                }
                if (isStellar(stack)) {
                    components.add(Component.literal(VPUtil.getRainbowString("Stellar: ")));
                    components.add(Component.translatable(("vp.stellarText")).withStyle(ChatFormatting.GRAY).append(Component.translatable("vp.stellar." + vestigeNumber)));
                } else {
                    components.add(Component.translatable(("Stellar")).withStyle(ChatFormatting.GRAY));
                    components.add(Component.translatable(("vp.stellarText2")).withStyle(ChatFormatting.GRAY));
                }
                int visualUlt = this.cdUltimateActive;
                while (visualUlt > this.ultimateCd) {
                    visualUlt -= this.ultimateCd;
                }
                int visualSpecial = this.cdSpecialActive;
                while (visualSpecial > this.specialCd) {
                    visualSpecial -= this.specialCd;
                }
                components.add(Component.literal("Special Charges: " + this.currentChargeSpecial).withStyle(ChatFormatting.GRAY));
                components.add(Component.literal("Special Cd: " + visualSpecial).withStyle(ChatFormatting.GRAY));
                components.add(Component.literal("Ultimate Charges: " + this.currentChargeUltimate).withStyle(ChatFormatting.GRAY));
                components.add(Component.literal("Ultimate Cd: " + visualUlt).withStyle(ChatFormatting.GRAY));
            } else if (Screen.hasControlDown()) {
                components.add(Component.translatable("vp.challenge").withStyle(ChatFormatting.GRAY).append(Component.literal(VPUtil.getRainbowString(VPUtil.generateRandomString(7)) + " :")));
                components.add(Component.translatable("vp.get." + vestigeNumber).withStyle(ChatFormatting.GRAY));
                if (player != null) {
                    CompoundTag nbt;
                    if (player.getPersistentData() != null) {
                        nbt = player.getPersistentData();
                    } else {
                        nbt = new CompoundTag();
                    }
                    //progress = nbt.getInt("challenge" + (vestigeNumber));
                    progress = cap.getChallenge(vestigeNumber);
                    components.add(Component.translatable("vp.progress").withStyle(ChatFormatting.GRAY)
                            .append(Component.literal(" " + progress))
                            .append(Component.literal(" / " + PlayerCapabilityVP.getMaximum(vestigeNumber))));
                }
                components.add(Component.literal(cap.getChance()+" ").append(Component.translatable("vp.chance").withStyle(ChatFormatting.GRAY).append(Component.literal(VPUtil.getRainbowString("Stellar")))));
                components.add(Component.translatable("vp.chance2").withStyle(ChatFormatting.GRAY));
                components.add(Component.literal("Each Vestige can be obtained once per 4 days").withStyle(ChatFormatting.GRAY));
            } else if (Screen.hasAltDown()) {
                String text = "";
                switch (vestigeNumber) {
                    case 2: {
                        text = VPUtil.getMonsterLeft(cap.getMonstersKilled()).toString();
                        break;
                    }
                    case 3: {
                        text = VPUtil.getBiomesLeft(cap.getBiomesFound()).toString();
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
                    case 15: {
                        text = VPUtil.getMobsLeft(cap.getMobsTamed()).toString();
                        break;
                    }
                    case 16: {
                        text = VPUtil.getFlowersLeft(cap.getFlowers()).toString();
                        break;
                    }
                    default:
                        text = null;
                }
                if (text != null)
                    components.add(Component.literal(text).withStyle(ChatFormatting.GRAY));
            } else {
                components.add(Component.translatable("vp.shift"));
                components.add(Component.translatable("vp.ctrl"));
                if (vestigeNumber == 2 || vestigeNumber == 3 || vestigeNumber == 6 || vestigeNumber == 10 || vestigeNumber == 15 || vestigeNumber == 16)
                    components.add(Component.translatable("vp.alt"));
            }
            if (isStellar(stack)) {
                String name = symbolsRandom(stack.getDisplayName().getString());
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

    public String symbolsRandom(String text){
        char[] starChars = {'*', '\u2731', '\u2606', '\u2605'};
        Random random = new Random();
        StringBuilder result;
        if(this.originalText == null){
            result = new StringBuilder(text);
            result.deleteCharAt(0);
            result.deleteCharAt(0);
            result.deleteCharAt(0);
            result.deleteCharAt(result.length()-1);
            originalText = String.valueOf(result);
        }
        result = new StringBuilder(originalText);
        int randomNumber = random.nextInt(originalText.length()-1);
        int randomNumber2 = random.nextInt(originalText.length()-1);
        for (int i = 0; i < originalText.length(); i++){
            if (i == randomNumber)
                result.setCharAt(i,starChars[random.nextInt(starChars.length)]);
            if (i == randomNumber2)
                result.setCharAt(i,starChars[random.nextInt(starChars.length)]);
        }
        return result.toString();
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        this.time = 0;
        this.timeUlt = 0;
        this.isSpecialActive = false;
        this.isUltimateActive = false;
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        this.cdUltimateActive = this.ultimateCd*this.ultimateCharges;
        this.cdSpecialActive = this.specialCd*this.specialCharges;
        this.currentChargeUltimate = 0;
        this.currentChargeSpecial = 0;
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
        if(I18n.get("vp.lang").equals("ru")) {

        } else {

        }
    }

    public void doSpecial(long seconds, Player player, Level level){}
    public void doUltimate(long seconds, Player player, Level level){}

    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_ELYTRA, 1.0f, 1.0f);
    }
}
