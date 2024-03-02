package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Iterator;

public class Armor extends Vestige{
    public Armor(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(11, ChatFormatting.RED, 4, 10, 1, 60, 60, 1, true);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        VPUtil.play(player, SoundRegistry.FLESH.get());
        player.addEffect(new MobEffectInstance(VPUtil.getRandomEffect(false), 60 * 20));
        int debuffCount = 1;
        Iterator<MobEffectInstance> iterator = player.getActiveEffects().iterator();
        while (iterator.hasNext()) {
            MobEffectInstance effectInstance = iterator.next();
            MobEffect effect = effectInstance.getEffect();
            if (!effect.isBeneficial()) {
                debuffCount++;
            }
        }
        player.hurt(player.damageSources().cactus(),VPUtil.getAttack(player,true)*(1 + debuffCount));
        player.getPersistentData().putFloat("VPArmor",player.getPersistentData().getFloat("VPArmor")+100);
        VPUtil.spawnParticles(player, ParticleTypes.CRIMSON_SPORE,3,1,0,-0.1,0,0,false);
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.play(player, SoundRegistry.FLESH2.get());
        int pain = (int)player.getPersistentData().getFloat("VPArmor");
        VPUtil.repairAll(player,pain);
        player.getPersistentData().putFloat("VPArmor",0);
        VPUtil.spawnParticles(player, ParticleTypes.CRIT,3,1,0,0,0,0,false);
        super.doUltimate(seconds, player, level);
    }
}
