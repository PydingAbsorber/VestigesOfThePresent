package com.pyding.vp.item.artifacts;

import net.minecraft.ChatFormatting;

public class Mark extends Vestige{
    public Mark(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(7, ChatFormatting.DARK_RED, 3, 5, 1, 120, 1, 5, hasDamage);
    }
}
