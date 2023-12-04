package com.pyding.vp.item.artifacts;

import net.minecraft.ChatFormatting;

public class Armor extends Vestige{
    public Armor(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(11, ChatFormatting.RED, 4, 10, 1, 60, 60, 1, true);
    }
}
