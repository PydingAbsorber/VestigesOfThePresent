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

public class Whirlpool extends Vestige{
    public Whirlpool(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(24, ChatFormatting.BLUE, 3, 40, 1, 115, 30, 20, true, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        Random random = new Random();
        if(random.nextDouble() < 0.5)
            VPUtil.play(player, SoundRegistry.BUBBLE2.get());
        else VPUtil.play(player, SoundRegistry.BUBBLE5.get());
        VPUtil.spawnSphere(player, ParticleTypes.SPLASH,40,3,0.2f);
        player.getPersistentData().putInt("VPWhirlpool",VPUtil.scalePower(3 + Math.min(10,Math.max(0,player.getMaxAirSupply()/100)),24,player));
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player, SoundRegistry.BUBBLEPOP.get());
        VPUtil.spawnCircleParticles(player, 50,ParticleTypes.SPLASH,3,1);
        for(LivingEntity entity: VPUtil.getEntities(player,20,false)){
            if(!VPUtil.isProtectedFromHit(player,entity)) {
                entity.getPersistentData().putLong("VPBubble", System.currentTimeMillis() + seconds);
                entity.setLastHurtByPlayer(player);
            }
        }
        super.doUltimate(seconds, player, level, stack);
    }
}
