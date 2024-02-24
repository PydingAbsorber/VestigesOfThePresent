package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

import java.util.Random;

public class Chaos extends Vestige{
    public Chaos(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(14, ChatFormatting.DARK_PURPLE, 1, 20, 1, 20, 20, 20, hasDamage);
    }

    int strelyalki = 0;
    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.CHAOS_CORE.get());
        strelyalki = 10 + new Random().nextInt(70);
        VPUtil.spawnParticles(player, ParticleTypes.NAUTILUS,12,1,0,-0.1,0,1,false);
        super.doUltimate(seconds, player, level);
    }

    @Override
    public void whileUltimate(Player player) {
        if(strelyalki > 0){
            for(Object o: VPUtil.getEntitiesAroundOfType(Projectile.class,player,5,5,5,false)){
                if(o instanceof Projectile projectile){
                    Entity entity = projectile.getOwner();
                    if(entity != player) {
                        projectile.setDeltaMovement(projectile.getDeltaMovement().reverse());
                        projectile.setOwner(player);
                        strelyalki--;
                    }
                }
            }
        }
        super.whileUltimate(player);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        Random random = new Random();
        player.getPersistentData().putInt("VPChaos",random.nextInt(10));
        VPUtil.play(player,SoundRegistry.CHAOS_CORE.get());
        VPUtil.spawnParticles(player, ParticleTypes.NAUTILUS,12,1,0,-0.1,0,1,false);
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void ultimateEnds(Player player) {
        strelyalki = 0;
        super.ultimateEnds(player);
    }
}
