package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class SoulBlighter extends Vestige{
    public SoulBlighter(){
        super();
    }

    LivingEntity prisoner = null;

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(20, ChatFormatting.LIGHT_PURPLE, 2, 30, 1, 300, 15, 300, hasDamage);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.MAGIC4.get());
        if(player.isShiftKeyDown())
            player.getPersistentData().putLong("VPAstral",System.currentTimeMillis()+specialMaxTime);
        else {
            for(LivingEntity entity: VPUtil.ray(player,6,30,true))
                entity.getPersistentData().putLong("VPAstral",System.currentTimeMillis()+specialMaxTime);
        }
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.play(player, SoundRegistry.SOUL3.get());
        if(prisoner != null){
            player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("06406f20-b639-471c-aa2f-a251a67fecab"),1+prisoner.getMaxHealth()*0.3f, AttributeModifier.Operation.MULTIPLY_TOTAL,"vp:soulblighter_hp_boost"));
        } else {
            player.getPersistentData().putFloat("HealDebt", player.getPersistentData().getFloat("HealDebt")+player.getMaxHealth()*20);
            for(LivingEntity entity: VPUtil.ray(player,4,30,true)){
                System.out.println(VPUtil.calculateCatchChance(player.getMaxHealth(),entity.getMaxHealth(),entity.getHealth()));
                if(Math.random() < VPUtil.calculateCatchChance(player.getMaxHealth(),entity.getMaxHealth(),entity.getHealth())){
                    entity.remove(Entity.RemovalReason.UNLOADED_TO_CHUNK);
                }
            }
        }
        super.doUltimate(seconds, player, level);
    }


    public boolean fuckNbtCheck1 = false;
    public boolean fuckNbtCheck2 = false;

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        Player player1 = (Player) slotContext.entity();
        if (!fuckNbtCheck1) {
            super.onUnequip(slotContext, newStack, stack);
            if(prisoner != null)
                player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player1, Attributes.MAX_HEALTH, UUID.fromString("06406f20-b639-471c-aa2f-a251a67fecab"),1+prisoner.getMaxHealth()*0.3f, AttributeModifier.Operation.MULTIPLY_TOTAL,"vp:soulblighter_hp_boost"));
        } else fuckNbtCheck1 = false;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        Player player1 = (Player) slotContext.entity();
        if(!fuckNbtCheck2) {
            super.onEquip(slotContext, prevStack, stack);
        } else fuckNbtCheck2 = false;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player1 = (Player) slotContext.entity();
        if(prisoner != null && player1.tickCount % 20 == 0){
            VPUtil.regenOverShield(player1,prisoner.getMaxHealth()*0.1f);
            player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player1, Attributes.MAX_HEALTH, UUID.fromString("06406f20-b639-471c-aa2f-a251a67fecab"),1+prisoner.getMaxHealth()*0.3f, AttributeModifier.Operation.MULTIPLY_TOTAL,"vp:soulblighter_hp_boost"));
        }
        super.curioTick(slotContext, stack);
    }
}
