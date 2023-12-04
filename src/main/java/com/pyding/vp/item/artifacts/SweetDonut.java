package com.pyding.vp.item.artifacts;

import net.minecraft.ChatFormatting;

public class SweetDonut extends Vestige{
    public SweetDonut(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(6, ChatFormatting.RED, 2, 50, 1, 60, 1, 10, hasDamage);
    }
}
