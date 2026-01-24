package com.pyding.vp.item.vestiges;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class Mark extends Vestige{
    public Mark(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(7, ChatFormatting.DARK_RED, 3, 5, 1, 80, 1, 5, hasDamage, stack);
    }

    private Multimap<Holder<Attribute>, AttributeModifier> createAttributeMap() {
        Multimap<Holder<Attribute>, AttributeModifier> attributesDefault = HashMultimap.create();
        attributesDefault.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID,"vp:attack_speed_modifier_mark"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        attributesDefault.put(Attributes.ATTACK_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID,"vp:speed_modifier_mark"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        attributesDefault.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID,"vp:speed_modifier_mark"), 2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        return attributesDefault;
    }

    private Multimap<Holder<Attribute>, AttributeModifier> overdrive(int curses,Player player) {
        Multimap<Holder<Attribute>, AttributeModifier> attributesDefault = HashMultimap.create();
        curses *= VPUtil.scalePower(ConfigHandler.markBonus.get(),7,player);
        attributesDefault.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID,"vp:mark1") , curses, AttributeModifier.Operation.ADD_VALUE));
        attributesDefault.put(Attributes.ATTACK_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID,"vp:mark2") , curses, AttributeModifier.Operation.ADD_VALUE));
        attributesDefault.put(Attributes.MAX_HEALTH, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID,"vp:mark3") , curses, AttributeModifier.Operation.ADD_VALUE));
        attributesDefault.put(Attributes.LUCK, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID,"vp:mark4") , curses, AttributeModifier.Operation.ADD_VALUE));
        attributesDefault.put(Attributes.ARMOR, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID,"vp:mark5") , curses, AttributeModifier.Operation.ADD_VALUE));
        attributesDefault.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID,"vp:mark6") , curses, AttributeModifier.Operation.ADD_VALUE));
        attributesDefault.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID,"vp:mark7") , curses, AttributeModifier.Operation.ADD_VALUE));
        return attributesDefault;
    }

    @Override
    public int setUltimateActive(long seconds, Player player, ItemStack stack) {
        if(player.getHealth() < player.getMaxHealth()*0.3){
            seconds += 15 * 1000;
        }
        else if(player.getHealth() > player.getMaxHealth()*0.5){
            seconds += (long) Math.min(10,player.getPersistentData().getInt("VPMadness")) * 1000;
        }
        return super.setUltimateActive(seconds, player, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundEvents.WARDEN_HEARTBEAT);
        if(player.getHealth() > player.getMaxHealth()*0.2)
            player.setHealth(player.getHealth()-player.getMaxHealth()*0.2f);
        else player.setHealth(1);
        if(player.getPersistentData().getInt("VPMadness") < ConfigHandler.markMaximum.get())
            player.getPersistentData().putInt("VPMadness",player.getPersistentData().getInt("VPMadness")+1);
        VPUtil.spawnParticles(player, ParticleTypes.DAMAGE_INDICATOR,1,1,0,-0.5,0,1,false);
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.RAGE.get());
        int curses = VPUtil.getCurseAmount(player);
        if(curses > 20 && isStellar(stack)) {
            player.getPersistentData().putFloat("VPOverdrive", curses);
            player.getAttributes().addTransientAttributeModifiers(this.overdrive(curses,player));
        }
        if(player.getHealth() < player.getMaxHealth()*0.3){
            player.getAttributes().addTransientAttributeModifiers(this.createAttributeMap());
        }
        player.getPersistentData().putBoolean("VPMarkUlt",true);
        VPUtil.setHealDebt(player,VPUtil.getHealDebt(player)+player.getMaxHealth()/100* ConfigHandler.markHealDebt.get());
        VPUtil.spawnParticles(player, ParticleTypes.FLAME,2,1,0,-0.5,0,1,false);
        super.doUltimate(seconds, player, level, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if(!isUltimateActive(stack)) {
            player.getAttributes().removeAttributeModifiers(this.createAttributeMap());
            if(isStellar(stack))
                player.getAttributes().removeAttributeModifiers(this.overdrive(0,player));
            player.getPersistentData().putBoolean("VPMarkUlt",false);
        }
        super.curioTick(slotContext, stack);
    }

    @Override
    public void ultimateEnds(Player player, ItemStack stack) {
        player.getPersistentData().putBoolean("VPMarkUlt",false);
        float damage = player.getPersistentData().getFloat("VPDamageReduced");
        float heal = player.getPersistentData().getFloat("VPHealReduced");
        float damageFinal = damage-heal;
        if(damageFinal > 0) {
            player.hurt(player.damageSources().fellOutOfWorld(), damageFinal);
        }
        else addRadiance(getMaxRadiance(stack)*0.3f,stack,player);
        //setCdUltimateActive(cdUltimateActive(stack)-(int) Math.min(ultimateCd(stack) * 0.6, ultimateCd(stack) * ((VPUtil.calculatePercentageDifference(damage,heal))/100)),stack);
        player.getAttributes().removeAttributeModifiers(this.createAttributeMap());
        if(isStellar(stack))
            player.getAttributes().removeAttributeModifiers(this.overdrive(0,player));
        player.getPersistentData().putFloat("VPDamageReduced",0);
        player.getPersistentData().putFloat("VPHealReduced",0);
        player.getPersistentData().putFloat("VPOverdrive",0);
        super.ultimateEnds(player, stack);
    }

}
