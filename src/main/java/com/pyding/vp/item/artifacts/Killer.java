package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Killer extends Vestige{
    public Killer(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(4, ChatFormatting.YELLOW, 1, 10, 1, 60, 1, 20, true);
    }


    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        if(Math.random() < 0.5)
            VPUtil.play(player,SoundRegistry.EXPLODE1.get());
        else VPUtil.play(player,SoundRegistry.EXPLODE2.get());
        for(LivingEntity entity: VPUtil.getEntitiesAround(player,20,20,20)){
            VPUtil.dealDamage(entity,player, DamageSource.playerAttack(player).setExplosion(),400,2);
            entity.getPersistentData().putBoolean("VPKillerQueen",true);
        }
        VPUtil.spawnParticles(player, ParticleTypes.EXPLOSION,8,1,0,0,0,0,false);
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.MAGIC3.get());
        player.getPersistentData().putLong("VPQueenDeath",System.currentTimeMillis()+20000);
        VPUtil.spawnParticles(player, ParticleTypes.EXPLOSION,4,1,0,0,0,0,false);
        super.doUltimate(seconds, player, level);
    }
}
