package com.pyding.vp.item.vestiges;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

public class Killer extends Vestige{
    public Killer(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(4, ChatFormatting.YELLOW, 2, 10, 1, 60, 1, 20, true, stack);
    }


    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        Random random = new Random();
        if(random.nextDouble() < 0.5)
            VPUtil.play(player,SoundRegistry.EXPLODE1.get());
        else VPUtil.play(player,SoundRegistry.EXPLODE2.get());
        for(LivingEntity entity: VPUtil.getEntitiesAround(player,20,20,20)){
            VPUtil.dealDamage(entity,player, player.damageSources().explosion(entity,player),400,2);
            entity.getPersistentData().putBoolean("VPKillerQueen",true);
        }
        VPUtil.spawnParticles(player, ParticleTypes.EXPLOSION,8,1,0,0,0,0,false);
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.MAGIC3.get());
        player.getPersistentData().putLong("VPQueenDeath",System.currentTimeMillis()+seconds);
        VPUtil.spawnParticles(player, ParticleTypes.EXPLOSION,4,1,0,0,0,0,false);
        super.doUltimate(seconds, player, level, stack);
    }

    @Override
    public void ultimateRecharges(Player player, ItemStack stack) {
        player.getPersistentData().putLong("VPQueenDeath",0);
        super.ultimateRecharges(player, stack);
    }
}
