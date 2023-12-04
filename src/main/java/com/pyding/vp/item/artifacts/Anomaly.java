package com.pyding.vp.item.artifacts;

import net.minecraft.ChatFormatting;

public class Anomaly extends Vestige{
    public Anomaly(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(10, ChatFormatting.DARK_GREEN, 2, 60, 1, 360, 1, 1, hasDamage);
    }
}
