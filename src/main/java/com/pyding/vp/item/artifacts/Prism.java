package com.pyding.vp.item.artifacts;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.SlotContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Prism extends Vestige{
    public Prism(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(13, ChatFormatting.RED, 4, 60, 1, 300, 40, 60, hasDamage);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.SOUL2.get());
        boolean found = false;
        for(LivingEntity entity: VPUtil.ray(player,6,60,true)) {
            if (entity instanceof Player && !isStellar)
                continue;
            if (entity != null) {
                entity.getPersistentData().putLong("VPPrismBuff", System.currentTimeMillis() + specialMaxTime());
                entity.getPersistentData().putInt("VPPrismDamage", new Random().nextInt(VPUtil.playerDamageSources(player,player).size()));
                VPUtil.spawnParticles(player, ParticleTypes.SOUL_FIRE_FLAME, entity.getX(), entity.getY(), entity.getZ(), 20, 0, 0.5, 0);
                found = true;
                break;
            }
        }
        if(isStellar && !found){
            player.getPersistentData().putLong("VPPrismBuff", System.currentTimeMillis() + specialMaxTime());
            player.getPersistentData().putString("VPPrismDamage", VPUtil.generateRandomDamageType());
            VPUtil.spawnParticles(player, ParticleTypes.SOUL_FIRE_FLAME, player.getX(), player.getY(), player.getZ(), 20, 0, 0.5, 0);
        }
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.SOUL.get());
        VPUtil.spawnParticles(player, ParticleTypes.SOUL,6,1,0,-0.1,0,1,false);
        super.doUltimate(seconds, player, level);
    }


    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
            return super.onLeftClickEntity(stack, player, entity);
    }
}
