package com.pyding.vp.item.artifacts;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CatEars extends Vestige{
    public CatEars(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(8, ChatFormatting.YELLOW, 1, 30, 1, 60, 30, 40, hasDamage);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        super.doUltimate(seconds, player, level);
    }
}
