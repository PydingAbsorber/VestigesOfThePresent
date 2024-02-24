package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class Ball extends Vestige{
    public Ball(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(18, ChatFormatting.AQUA, 5, 25, 2, 30, 1, 1, true);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        VPUtil.play(player, SoundRegistry.BOLT.get());
        for(LivingEntity entity: VPUtil.getEntities(player,5,true)){
            VPUtil.dealDamage(entity,player, DamageSource.LIGHTNING_BOLT,500);
        }
        player.hurt(DamageSource.LIGHTNING_BOLT,VPUtil.getAttack(player,true)*500);
        if(level instanceof ServerLevel serverLevel)
            VPUtil.spawnLightning(serverLevel, player.getX(),player.getY(),player.getZ());
        VPUtil.randomTeleportChorus(player);
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        super.doUltimate(seconds, player, level);
    }
}
