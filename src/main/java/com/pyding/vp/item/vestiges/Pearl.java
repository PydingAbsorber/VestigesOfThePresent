package com.pyding.vp.item.vestiges;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.mixin.FishingHookMixin;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.lang.reflect.Field;

public class Pearl extends Vestige{
    public Pearl(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(23, ChatFormatting.DARK_BLUE, 4, 40, 1, 360, 30, 30, false, stack);
    }

    boolean later = false;

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player, SoundRegistry.BUBBLE3.get());
        VPUtil.spawnSphere(player, ParticleTypes.FISHING,30,3,0);
        VPUtil.spawnCircleParticles(player, 30,ParticleTypes.FISHING,3,0);
        if(player.fishing != null){
            FishingHook hook = player.fishing;
            if(hook.isOpenWaterFishing() && hook.isInFluidType()){
                ((FishingHookMixin)hook).setTimeLured(100);
                ((FishingHookMixin)hook).setNibble(100);
                player.setAirSupply(Math.max(0,player.getAirSupply()-(int)(player.getMaxAirSupply()*0.3)));
            } else later = true;
        } else later = true;
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void whileSpecial(Player player, ItemStack stack) {
        if(later){
            if(player.fishing != null){
                FishingHook hook = player.fishing;
                if(hook.isOpenWaterFishing() && hook.isInFluidType()){
                    ((FishingHookMixin)hook).setTimeLured(100);
                    ((FishingHookMixin)hook).setNibble(100);
                    player.setAirSupply(Math.max(0,player.getAirSupply()-(int)(player.getMaxAirSupply()*0.3)));
                    later = false;
                }
            }
        }
        super.whileSpecial(player, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player, SoundRegistry.BUBBLE4.get());
        VPUtil.spawnSphere(player, ParticleTypes.FISHING,30,3,0);
        VPUtil.spawnCircleParticles(player, 30,ParticleTypes.FISHING,3,0);
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            player.getPersistentData().putInt("VPLures",VPUtil.scalePower(Math.min(30,10+cap.getPearls()*5),23,player));
        });
        player.getPersistentData().putFloat("VPDepth", VPUtil.getWaterDepth(player));
        super.doUltimate(seconds, player, level, stack);
    }

    @Override
    public void ultimateRecharges(Player player, ItemStack stack) {
        player.getPersistentData().putInt("VPLures",0);
        super.ultimateRecharges(player, stack);
    }

    @Override
    public void ultimateEnds(Player player, ItemStack stack) {
        player.getPersistentData().putFloat("VPDepth",0);
        super.ultimateEnds(player, stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        if(!level.isClientSide)
            VPUtil.printFishDrop(player);
        return super.use(level, player, p_41434_);
    }
}
