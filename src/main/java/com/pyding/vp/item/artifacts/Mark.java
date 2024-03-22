package com.pyding.vp.item.artifacts;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
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
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(7, ChatFormatting.DARK_RED, 3, 5, 1, 120, 1, 5, hasDamage);
    }

    private Multimap<Attribute, AttributeModifier> createAttributeMap() {
        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();
        attributesDefault.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("4fc18d7a-9353-45b1-ad77-29117c1d9e6f"), "vp:attack_speed_modifier_mark", 2, AttributeModifier.Operation.MULTIPLY_BASE));
        attributesDefault.put(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("78cf254b-36df-41d6-be91-ad06220d9dd8"), "vp:speed_modifier_mark", 2, AttributeModifier.Operation.MULTIPLY_BASE));
        attributesDefault.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("54b7f5ed-0851-4745-b98c-e1f08a1a2f67"), "vp:speed_modifier_mark", 2, AttributeModifier.Operation.MULTIPLY_BASE));
        return attributesDefault;
    }

    @Override
    public int setUltimateActive(long seconds, Player player) {
        if(player.getHealth() < player.getMaxHealth()*0.3){
            seconds += 15 * 1000;
        }
        else if(player.getHealth() > player.getMaxHealth()*0.5){
            seconds += (long) Math.min(10,player.getPersistentData().getInt("VPMadness")) * 1000;
        }
        return super.setUltimateActive(seconds, player);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundEvents.WARDEN_HEARTBEAT);
        if(player.getHealth() > player.getMaxHealth()*0.2)
            player.setHealth(player.getHealth()-player.getMaxHealth()*0.2f);
        else player.setHealth(1);
        if(player.getPersistentData().getInt("VPMadness") < 10)
            player.getPersistentData().putInt("VPMadness",player.getPersistentData().getInt("VPMadness")+1);
        VPUtil.spawnParticles(player, ParticleTypes.DAMAGE_INDICATOR,1,1,0,-0.5,0,1,false);
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.RAGE.get());
        if(player.getHealth() < player.getMaxHealth()*0.3){
            player.getAttributes().addTransientAttributeModifiers(this.createAttributeMap());
        }
        player.getPersistentData().putBoolean("VPMarkUlt",true);
        player.getPersistentData().putFloat("HealDebt", player.getPersistentData().getFloat("HealDebt")+player.getMaxHealth()*10);
        VPUtil.spawnParticles(player, ParticleTypes.FLAME,2,1,0,-0.5,0,1,false);
        super.doUltimate(seconds, player, level);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if(!isUltimateActive()) {
            player.getAttributes().removeAttributeModifiers(this.createAttributeMap());
            player.getPersistentData().putBoolean("VPMarkUlt",false);
        }
        super.curioTick(slotContext, stack);
    }

    @Override
    public void ultimateEnds(Player player) {
        player.getPersistentData().putBoolean("VPMarkUlt",false);
        float damage = player.getPersistentData().getFloat("VPDamageReduced");
        float heal = player.getPersistentData().getFloat("VPHealReduced");
        float damageFinal = damage-heal;
        if(damageFinal > 0) {
            player.hurt(player.damageSources().fellOutOfWorld(), damageFinal);
        }
        else setCdUltimateActive(cdUltimateActive()-(int) Math.min(ultimateCd() * 0.6, ultimateCd() * ((VPUtil.calculatePercentageDifference(damage,heal))/100)));
        player.getAttributes().removeAttributeModifiers(this.createAttributeMap());
        player.getPersistentData().putFloat("VPDamageReduced",0);
        player.getPersistentData().putFloat("VPHealReduced",0);
        super.ultimateEnds(player);
    }

}
