package com.pyding.vp.item.artifacts;

import net.minecraft.ChatFormatting;

public class Atlas extends Vestige{
    public Atlas(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(3, ChatFormatting.RED, 2, 10, 1, 30, 1, 3, true);
    }
}
