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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Artifact extends Item implements ICurioItem {
    public Artifact() {
        super(new Item.Properties().stacksTo(1).tab(ModCreativeModTab.tab));
    }
    public Artifact(Properties properties){
        super(properties);
    }
    List<String> passiveText;
    List<String> specialText;
    List<String> ultimateText;
    public int specialCharges;
    public int ultimateCharges;
    public int specialCd;
    public int ultimateCd;
    public ChatFormatting color;
    public int cdSpecialActive;
    public int cdUltimateActive;
    public String damageType;

    public String getting;

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

    public int getVestigeNumber(){
        return this.vestigeNumber;
    }
    public void setVestigeNumber(int number){
        this.vestigeNumber = number;
    }

    public void setStellar(String text, ItemStack stack){
        this.stellarBoost = text;
        CompoundTag tag = stack.getTag();
        if(tag == null)
            tag = new CompoundTag();
        tag.putBoolean("Stellar", true);
        stack.setTag(tag);
    }
    public void init(){
        this.setPassiveText(null);
        this.setSpecialText(null,0,0);
        this.setUltimateText(null,0,0);
        this.setColor(null);
        this.setLastInfo(null,null);
        this.setVestigeNumber(0);
    }
    public void setLastInfo(String damageType, String getting){
        this.damageType = damageType;
        this.getting = getting;
    }

    public void setPassiveText(List<String> text){
        this.passiveText = text;
    }

    public void setSpecialText(List<String> text,int charges,int cd){
        this.specialText = text;
        this.specialCharges = charges;
        this.specialCd = cd;
    }

    public void setUltimateText(List<String> text,int charges,int cd){
        this.ultimateText = text;
        this.ultimateCharges = charges;
        this.ultimateCd = cd;
    }

    public void setColor(ChatFormatting color) {
        this.color = color;
    }

    public boolean isSpecialActive;
    public boolean isUltimateActive;

    public long time;
    public long timeUlt;
    public Player player;
    public int progress = 0;

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
        if(slotContext.entity() != null && slotContext.entity() instanceof Player && (player == null || player instanceof LocalPlayer))
            this.player = (Player) slotContext.entity();
        ICurioItem.super.curioTick(slotContext, stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (specialText == null || color == null) {
            this.init();
        }
        if(player == null)
            player = Minecraft.getInstance().player;
        if(stack == null)
            System.out.println("Something went wrong :(((");
        if(Screen.hasShiftDown()) {
            if (passiveText != null) {
                components.add(Component.translatable("vp.passive").withStyle(color));
                for (String text : passiveText) {
                    components.add(Component.literal(text).withStyle(ChatFormatting.GRAY));
                }
            }
            components.add(Component.translatable("vp.special").withStyle(color)
                    .append(Component.literal(" " + this.specialCharges).withStyle(color))
                    .append(Component.translatable("vp.charges").withStyle(color))
                    .append(Component.literal(" " + this.specialCd).withStyle(color)));
            for (String text : specialText) {
                components.add(Component.literal(text).withStyle(ChatFormatting.GRAY));
            }
            components.add(Component.translatable("vp.ultimate").withStyle(color)
                    .append(Component.literal(" " + this.ultimateCharges).withStyle(color))
                    .append(Component.translatable("vp.charges").withStyle(color))
                    .append(Component.literal(" " + this.ultimateCd).withStyle(color)));
            for (String text : ultimateText) {
                components.add(Component.literal(text).withStyle(ChatFormatting.GRAY));
            }
            if(damageType != null){
                components.add(Component.translatable("vp.damage").withStyle(color));
                components.add(Component.literal(damageType).withStyle(ChatFormatting.GRAY));
            }
            if(isStellar(stack)){
                if(stellarBoost == null)
                    this.setStellar("",stack);
                components.add(Component.literal(VPUtil.getRainbowString("Stellar: ")));
                components.add(Component.translatable(("vp.stellarText")).withStyle(ChatFormatting.GRAY).append(Component.literal(stellarBoost)));
            } else {
                components.add(Component.translatable(("Stellar")).withStyle(ChatFormatting.GRAY));
                components.add(Component.translatable(("vp.stellarText2")).withStyle(ChatFormatting.GRAY));
            }
        }else if(Screen.hasControlDown()){
            components.add(Component.translatable("vp.challenge").withStyle(ChatFormatting.GRAY).append(Component.literal(VPUtil.getRainbowString(VPUtil.generateRandomString(7)) +" :")));
            components.add(Component.literal(getting).withStyle(ChatFormatting.GRAY));
            if(this.player != null) {
                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                    if(cap.getChallenge(vestigeNumber) != 0)
                    progress = cap.getChallenge(vestigeNumber);
                    components.add(Component.translatable("vp.progress").withStyle(ChatFormatting.GRAY).append(Component.literal(" "+progress)));
                });
            }
            components.add(Component.translatable("vp.chance").withStyle(ChatFormatting.GRAY).append(Component.literal(VPUtil.getRainbowString("Stellar"))));
            components.add(Component.translatable("vp.chance2").withStyle(ChatFormatting.GRAY));
        }else {
            components.add(Component.translatable("vp.shift"));
            components.add(Component.translatable("vp.ctrl"));
        }
        if(isStellar(stack)) {
            String name = symbolsRandom(stack.getDisplayName().getString());
            Component component = Component.nullToEmpty(VPUtil.getRainbowString(name));
            stack.setHoverName(component);
        }
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
        } else result = new StringBuilder(originalText);
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

    public void doSpecial(long seconds, Player player, Level level){}
    public void doUltimate(long seconds, Player player, Level level){}
}
