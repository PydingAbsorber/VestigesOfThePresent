package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Crown extends Vestige{
    public Crown(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(2, ChatFormatting.YELLOW, 5, 15, 1, 50, 1, 40, true);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.CROWN.get());
        for(LivingEntity entity : VPUtil.getMonstersAround(player,20,6,20)){
            VPUtil.adaptiveDamageHurt(entity,player,300);
            VPUtil.spawnParticles(player, ParticleTypes.GLOW,entity.getX(),entity.getY(),entity.getZ(),8,0,0.5,0);
        }
        super.doSpecial(seconds, player, level);
    }
    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.CROWN_ULT.get());
        for(LivingEntity entity: VPUtil.ray(player,3,128,false)){
            entity.getPersistentData().putLong("VPDeath", ultimateMaxTime + System.currentTimeMillis());
        }
        VPUtil.rayParticles(player, ParticleTypes.GLOW_SQUID_INK,128,3,3,0,1,0,3,false);
        super.doUltimate(seconds, player, level);
    }
}
