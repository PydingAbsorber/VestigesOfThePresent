package com.pyding.vp.item.artifacts;

import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Whirlpool extends Vestige{
    public Whirlpool(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(24, ChatFormatting.BLUE, 3, 40, 1, 60, 30, 20, true, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        player.getPersistentData().putInt("VPWhirlpool",3 + Math.min(10,Math.max(0,player.getMaxAirSupply()/100)));
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        for(LivingEntity entity: VPUtil.getEntities(player,20,false)){
            entity.getPersistentData().putLong("VPBubble",System.currentTimeMillis()+seconds);
            entity.setLastHurtByPlayer(player);
        }
        super.doUltimate(seconds, player, level, stack);
    }
}
