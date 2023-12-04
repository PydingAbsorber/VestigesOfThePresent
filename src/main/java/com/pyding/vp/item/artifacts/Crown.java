package com.pyding.vp.item.artifacts;

import net.minecraft.ChatFormatting;

public class Crown extends Vestige{
    public Crown(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(2, ChatFormatting.YELLOW, 1, 15, 1, 5, 1, 1, true);
    }
}
