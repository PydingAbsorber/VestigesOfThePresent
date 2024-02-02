package com.pyding.vp.item.artifacts;

import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

public class SweetDonut extends Vestige{
    public SweetDonut(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(6, ChatFormatting.RED, 2, 50, 1, 60, 1, 10, hasDamage);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        player.heal(player.getMaxHealth()*0.4f);
        for (MobEffectInstance effectInstance : player.getActiveEffects()){
            MobEffect effect = effectInstance.getEffect();
            if (!effect.isBeneficial()) {
                player.removeEffect(effect);
            }
        }
        if(player.getHealth() <= player.getMaxHealth()*0.5)
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20,4));
        float shieldBonus = (player.getPersistentData().getFloat("VPShieldBonusDonut"));
        if(isStellar && VPUtil.getShield(player) < player.getMaxHealth()*3*(1+shieldBonus/100))
            VPUtil.addShield(player,player.getMaxHealth()*3,false);
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        player.getPersistentData().putBoolean("VPSweetUlt",true);
        super.doUltimate(seconds, player, level);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        float bonus = 40;
        if(player.getHealth() <= player.getMaxHealth()*0.5)
            bonus *= 2;
        player.getPersistentData().putFloat("VPHealBonusDonutPassive",bonus);
        super.curioTick(slotContext, stack);
    }

    @Override
    public void whileUltimate(Player player) {
        if(VPUtil.getShield(player) > 0) {
            float saturation = player.getPersistentData().getFloat("VPSaturation");
            player.getPersistentData().putFloat("VPHealBonusDonut", saturation / 10);
            player.getPersistentData().putFloat("VPShieldBonusDonut", saturation);
            player.getPersistentData().putFloat("VPDurationBonusDonut", saturation / 10);
        }
        super.whileUltimate(player);
    }

    @Override
    public void ultimateRecharges(Player player) {
        reset(player);
        super.ultimateRecharges(player);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        reset(player);
        super.onUnequip(slotContext, newStack, stack);
    }

    public void reset(Player player){
        player.getPersistentData().putBoolean("VPSweetUlt",false);
        player.getPersistentData().putFloat("VPSaturation",0);
        player.getPersistentData().putFloat("VPHealBonusDonut", 0);
        player.getPersistentData().putFloat("VPShieldBonusDonut", 0);
        player.getPersistentData().putFloat("VPDurationBonusDonut", 0);
        player.getPersistentData().putFloat("VPHealBonusDonutPassive",0);
    }
}
