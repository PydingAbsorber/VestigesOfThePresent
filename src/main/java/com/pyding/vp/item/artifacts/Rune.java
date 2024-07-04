package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class Rune extends Vestige{
    public Rune(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(21, ChatFormatting.DARK_PURPLE, 1, 30, 1, 160, 10, 40, false, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player, SoundRegistry.RUNE1.get());
        VPUtil.spawnParticles(player, ParticleTypes.SMALL_FLAME,5,30,0.1,0.1,0.1,0.5,false);
        float bonus = 0;
        if(player.getAttributes().hasAttribute(Attributes.KNOCKBACK_RESISTANCE))
            bonus += (float) player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
        bonus += VPUtil.equipmentDurability(20,player,player,false);
        if (isUltimateActive(stack))
            bonus /= 2;
        player.getPersistentData().putFloat("VPRuneBonus",bonus/10);
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player, SoundRegistry.RUNE2.get());
        for(LivingEntity entity: VPUtil.getEntitiesAround(player,20,20,20,true)){
            VPUtil.spawnSphere(entity,ParticleTypes.FLAME,30,5,1);
            entity.getPersistentData().putLong("VPRuneUlt",seconds);
            entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity, Attributes.ARMOR, UUID.fromString("4cfa176b-4d5b-43bf-bd9b-9d717ffd7689"),20*(1 + VPUtil.getShieldBonus(entity)), AttributeModifier.Operation.ADDITION,"vp:rune"));
            entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity, Attributes.ARMOR_TOUGHNESS, UUID.fromString("16c73772-b469-4600-ae01-946807a719f7"),20*(1 + VPUtil.getShieldBonus(entity)), AttributeModifier.Operation.ADDITION,"vp:rune2"));
        }
        super.doUltimate(seconds, player, level, stack);
    }

    @Override
    public void ultimateEnds(Player player, ItemStack stack) {
        player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ARMOR, UUID.fromString("4cfa176b-4d5b-43bf-bd9b-9d717ffd7689"),20*(1 + VPUtil.getShieldBonus(player)), AttributeModifier.Operation.ADDITION,"vp:rune"));
        player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ARMOR_TOUGHNESS, UUID.fromString("16c73772-b469-4600-ae01-946807a719f7"),20*(1 + VPUtil.getShieldBonus(player)), AttributeModifier.Operation.ADDITION,"vp:rune2"));
        super.ultimateEnds(player, stack);
    }

    @Override
    public void specialEnds(Player player, ItemStack stack) {
        player.getPersistentData().putFloat("VPRuneBonus",0);
        super.specialEnds(player, stack);
    }

    @Override
    public void curioSucks(Player player, ItemStack stack) {
        player.getPersistentData().putFloat("VPRuneBonus",0);
        player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ARMOR, UUID.fromString("4cfa176b-4d5b-43bf-bd9b-9d717ffd7689"),20*(1 + VPUtil.getShieldBonus(player)), AttributeModifier.Operation.ADDITION,"vp:rune"));
        player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ARMOR_TOUGHNESS, UUID.fromString("16c73772-b469-4600-ae01-946807a719f7"),20*(1 + VPUtil.getShieldBonus(player)), AttributeModifier.Operation.ADDITION,"vp:rune2"));
        super.curioSucks(player, stack);
    }
}
