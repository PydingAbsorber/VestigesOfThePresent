package com.pyding.vp.item.vestiges;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Trigon extends Vestige{
    public Trigon(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(19, ChatFormatting.GOLD, 3, 13, 1, 60, 1, 1, true, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player, SoundRegistry.MAGIC5.get());
        for(LivingEntity entity: VPUtil.ray(player,6,30,true)){
            if(!VPUtil.isProtectedFromHit(player,entity))
                VPUtil.dealParagonDamage(entity,player,player.getMaxHealth()/10,2,true);
        }
        VPUtil.rayParticles(player,ParticleTypes.WAX_ON,30,3,30,0,-0.5,0,1,false);
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.TRIGON2.get());
        float overshields = 0;
        List<LivingEntity> list = VPUtil.getEntities(player,30,true);
        Random random = new Random();
        for (LivingEntity entity: list){
            float shield = VPUtil.getShield(entity);
            overshields += shield*0.3f;
            entity.getPersistentData().putFloat("VPShield",shield*0.6f);
            VPUtil.spawnParticles(player, ParticleTypes.WAX_ON,entity.getX(),entity.getY(),entity.getZ(),8,0,-0.5,0);
        }
        int numba = random.nextInt(list.size());
        if(isStellar(stack) && VPUtil.getOverShield(player) > 0 && player.getPersistentData().getFloat("VPOverShieldMax") > 0) {
            float amount = 1+(1-(VPUtil.getOverShield(player)/player.getPersistentData().getFloat("VPOverShieldMax")))/2;
            player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("8dac9436-c37f-4b74-bf64-8666258605b9"), amount, AttributeModifier.Operation.MULTIPLY_TOTAL, "vp:trigon_hp_boost"));
        }
        VPUtil.addOverShield(list.get(numba),overshields,false);
        super.doUltimate(seconds, player, level, stack);
    }
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player1 = (Player) slotContext.entity();
        if(VPUtil.getOverShield(player1) > 0)
            player1.getPersistentData().putFloat("VPTrigonBonus",(100-VPUtil.getOverShield(player1)/(player1.getPersistentData().getFloat("VPOverShieldMax")/100))*2);
        else player1.getPersistentData().putFloat("VPTrigonBonus",0);
        super.curioTick(slotContext, stack);
    }

    @Override
    public void ultimateRecharges(Player player, ItemStack stack) {
        player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("8dac9436-c37f-4b74-bf64-8666258605b9"),1, AttributeModifier.Operation.MULTIPLY_TOTAL,"vp:trigon_hp_boost"));
        player.setHealth(player.getMaxHealth());
        super.ultimateRecharges(player, stack);
    }
}
