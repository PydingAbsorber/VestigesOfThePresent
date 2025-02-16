package com.pyding.vp.item.vestiges;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
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
        super.dataInit(7, ChatFormatting.DARK_RED, 3, 5, 1, 120, 1, 5, hasDamage, stack);
    }

    private Multimap<Attribute, AttributeModifier> createAttributeMap() {
        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();
        attributesDefault.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("4fc18d7a-9353-45b1-ad77-29117c1d9e6f"), "vp:attack_speed_modifier_mark", 2, AttributeModifier.Operation.MULTIPLY_BASE));
        attributesDefault.put(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("78cf254b-36df-41d6-be91-ad06220d9dd8"), "vp:speed_modifier_mark", 2, AttributeModifier.Operation.MULTIPLY_BASE));
        attributesDefault.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("54b7f5ed-0851-4745-b98c-e1f08a1a2f67"), "vp:speed_modifier_mark", 2, AttributeModifier.Operation.MULTIPLY_BASE));
        return attributesDefault;
    }

    private Multimap<Attribute, AttributeModifier> overdrive(int curses) {
        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();
        curses *= ConfigHandler.COMMON.markBonus.get();
        attributesDefault.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("fd7417d3-ecd6-433c-8d76-6e0cb8bda70a"), "vp:mark1", curses, AttributeModifier.Operation.ADDITION));
        attributesDefault.put(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("f1a19717-72ab-4f79-b8b8-0c0d3df8b7d9"), "vp:mark2", curses, AttributeModifier.Operation.ADDITION));
        attributesDefault.put(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("d5206e9e-9b3c-4314-a7c2-b6097df335b5"), "vp:mark3", curses, AttributeModifier.Operation.ADDITION));
        attributesDefault.put(Attributes.LUCK, new AttributeModifier(UUID.fromString("54caeb55-3e90-4661-a2f6-571c5583a629"), "vp:mark4", curses, AttributeModifier.Operation.ADDITION));
        attributesDefault.put(Attributes.ARMOR, new AttributeModifier(UUID.fromString("a1dd7557-8941-4e78-abba-4a146bf1574f"), "vp:mark5", curses, AttributeModifier.Operation.ADDITION));
        attributesDefault.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("0743d5eb-9657-48d6-b7a7-e72055084e16"), "vp:mark6", curses, AttributeModifier.Operation.ADDITION));
        attributesDefault.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.fromString("5cc193b4-7513-43f3-9724-c3453b7681b2"), "vp:mark7", curses, AttributeModifier.Operation.ADDITION));
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
        if(player.getPersistentData().getInt("VPMadness") < ConfigHandler.COMMON.markMaximum.get())
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
            player.getAttributes().addTransientAttributeModifiers(this.overdrive(curses));
        }
        if(player.getHealth() < player.getMaxHealth()*0.3){
            player.getAttributes().addTransientAttributeModifiers(this.createAttributeMap());
        }
        player.getPersistentData().putBoolean("VPMarkUlt",true);
        player.getPersistentData().putFloat("VPHealDebt", player.getPersistentData().getFloat("VPHealDebt")+player.getMaxHealth()/100* ConfigHandler.COMMON.markHealDebt.get());
        VPUtil.spawnParticles(player, ParticleTypes.FLAME,2,1,0,-0.5,0,1,false);
        super.doUltimate(seconds, player, level, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if(!isUltimateActive(stack)) {
            player.getAttributes().removeAttributeModifiers(this.createAttributeMap());
            if(isStellar(stack))
                player.getAttributes().removeAttributeModifiers(this.overdrive(0));
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
        else setCdUltimateActive(cdUltimateActive(stack)-(int) Math.min(ultimateCd(stack) * 0.6, ultimateCd(stack) * ((VPUtil.calculatePercentageDifference(damage,heal))/100)),stack);
        player.getAttributes().removeAttributeModifiers(this.createAttributeMap());
        if(isStellar(stack))
            player.getAttributes().removeAttributeModifiers(this.overdrive(0));
        player.getPersistentData().putFloat("VPDamageReduced",0);
        player.getPersistentData().putFloat("VPHealReduced",0);
        player.getPersistentData().putFloat("VPOverdrive",0);
        super.ultimateEnds(player, stack);
    }

}
