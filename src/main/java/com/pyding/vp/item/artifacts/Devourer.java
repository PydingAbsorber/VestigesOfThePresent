package com.pyding.vp.item.artifacts;

import net.minecraft.ChatFormatting;

public class Devourer extends Vestige{
    public Devourer(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(15, ChatFormatting.DARK_RED, 1, 70, 1, 130, 5, 130, hasDamage);
    }
}
