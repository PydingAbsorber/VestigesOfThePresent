package com.pyding.vp.item.vestiges;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
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

import java.util.UUID;

public class CatEars extends Vestige{
    public CatEars(){
        super();
    }

    private Multimap<Attribute, AttributeModifier> createAttributeMap() {
        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();
        attributesDefault.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("5171148b-064a-4810-a7c0-1dcbf781ffdc"), "vp:speed_modifier_ears", ConfigHandler.COMMON.catSpeed.get(), AttributeModifier.Operation.ADDITION));
        return attributesDefault;
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(8, ChatFormatting.YELLOW, 1, 30, 1, 75, 30, 40, hasDamage, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player, SoundEvents.CAT_STRAY_AMBIENT);
        player.getAttributes().addTransientAttributeModifiers(this.createAttributeMap());
        player.getPersistentData().putBoolean("VPEarsSpecial",true);
        VPUtil.spawnParticles(player, ParticleTypes.POOF,4,1,0,-0.1,0,1,false);
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player, SoundEvents.CAT_AMBIENT);
        player.getPersistentData().putBoolean("VPEarsUlt",true);
        VPUtil.spawnParticles(player, ParticleTypes.NOTE,8,1,0,-0.1,0,1,false);
        super.doUltimate(seconds, player, level, stack);
    }

    @Override
    public void specialEnds(Player player, ItemStack stack) {
        player.getAttributes().removeAttributeModifiers(this.createAttributeMap());
        player.getPersistentData().putBoolean("VPEarsSpecial",false);
        super.specialEnds(player, stack);
    }

    @Override
    public void ultimateEnds(Player player, ItemStack stack) {
        player.getPersistentData().putBoolean("VPEarsUlt",false);
        super.ultimateEnds(player, stack);
    }

    @Override
    public void curioSucks(Player player, ItemStack stack) {
        player.getAttributes().removeAttributeModifiers(this.createAttributeMap());
        player.getPersistentData().putBoolean("VPEarsSpecial",false);
        player.getPersistentData().putBoolean("VPEarsUlt",false);
        super.curioSucks(player, stack);
    }
}
