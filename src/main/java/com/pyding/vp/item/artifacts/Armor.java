package com.pyding.vp.item.artifacts;

import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

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
        player.addEffect(new MobEffectInstance(VPUtil.getRandomEffect(false), 60 * 20));
        int debuffCount = 0;
        for (MobEffectInstance effectInstance : player.getActiveEffects()) {
            MobEffect effect = effectInstance.getEffect();
            if (!effect.isBeneficial()) {
                debuffCount++;
            }
        }
        VPUtil.dealDamage(player,player, DamageSource.playerAttack(player).CACTUS,100*debuffCount);
        player.getPersistentData().putFloat("VPArmor",player.getPersistentData().getFloat("VPArmor")+100);
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        int pain = (int)player.getPersistentData().getFloat("VPArmor");
        VPUtil.repairAll(player,pain);
        super.doUltimate(seconds, player, level);
    }
}