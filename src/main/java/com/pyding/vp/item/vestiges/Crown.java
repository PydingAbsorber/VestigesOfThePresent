package com.pyding.vp.item.vestiges;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Crown extends Vestige{
    public Crown(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(2, ChatFormatting.YELLOW, 5, 15, 1, 35, 1, 15, true, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.CROWN.get());
        for(LivingEntity entity : VPUtil.getMonstersAround(player,15,6,15)){
            entity.getPersistentData().putBoolean("VPCrownHit",true);
            entity.getPersistentData().putBoolean("VPCrownHitDeath",true);
            float shields = VPUtil.getShield(player);
            if(entity.getPersistentData().getLong("VPDeath") > 0 && shields > 1000*10 && isStellar(stack)){
                VPUtil.dealParagonDamage(entity,player,(shields*0.1f)/1000f,2,true);
                player.getPersistentData().putFloat("VPShield",shields*0.9f);
            }
            VPUtil.adaptiveDamageHurt(entity,player,300);
            VPUtil.spawnParticles(player, ParticleTypes.GLOW,entity.getX(),entity.getY(),entity.getZ(),8,0,0.5,0);
        }
        super.doSpecial(seconds, player, level, stack);
    }
    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.CROWN_ULT.get());
        for(LivingEntity entity: VPUtil.ray(player,3,128,false)){
            entity.getPersistentData().putLong("VPDeath", ultimateMaxTime(stack) + System.currentTimeMillis());
            if(entity instanceof Player target){
                target.getPersistentData().putLong("VPForbidden",ultimateMaxTime(stack) + System.currentTimeMillis());
            }
        }
        VPUtil.rayParticles(player, ParticleTypes.GLOW_SQUID_INK,128,3,30,0,1,0,3,false);
        super.doUltimate(seconds, player, level, stack);
    }
}
