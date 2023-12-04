package com.pyding.vp.item.artifacts;

import net.minecraft.ChatFormatting;

public class Anemoculus extends Vestige{
    public Anemoculus(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(1, ChatFormatting.DARK_AQUA, 1, 30, 5, 60, 1, 10, hasDamage);
    }
}
