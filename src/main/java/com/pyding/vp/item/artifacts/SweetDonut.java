package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
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
        VPUtil.play(player,SoundRegistry.HEAL3.get());
        VPUtil.spawnParticles(player, ParticleTypes.HEART,1,1,0,-0.1,0,1,false);
        player.heal(player.getMaxHealth()*0.4f);
        VPUtil.clearEffects(player,false);
        if(player instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.getHealth() <= serverPlayer.getMaxHealth() * 0.5) {
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 4));
                PacketHandler.sendToClient(new PlayerFlyPacket(4), serverPlayer);
            }
        }
        float shieldBonus = (player.getPersistentData().getFloat("VPShieldBonusDonut"));
        if(isStellar && VPUtil.getShield(player) < player.getMaxHealth()*3*(1+shieldBonus/100))
            VPUtil.addShield(player,player.getMaxHealth()*3,false);
        super.doSpecial(seconds, player, level);
    }

    @Override
    public int setUltimateActive(long seconds, Player player) {
        long bonus = 1+(long)player.getPersistentData().getFloat("VPDurationBonusDonut")/1000;
        return super.setUltimateActive(seconds*bonus, player);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.HEAL1.get());
        player.getPersistentData().putFloat("VPDurationBonusDonut", 0);
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
            if(player.tickCount % 20 == 0)
                VPUtil.spawnParticles(player, ParticleTypes.HEART,1,1,0,-0.1,0,1,false);
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
        player.getPersistentData().putFloat("VPHealBonusDonutPassive",0);
    }
}
