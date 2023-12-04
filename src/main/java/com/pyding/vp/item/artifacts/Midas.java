package com.pyding.vp.item.artifacts;

import net.minecraft.ChatFormatting;

public class Midas extends Vestige{
    public Midas(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(9, ChatFormatting.YELLOW, 1, 40, 1, 120, 40, 1, hasDamage);
    }
}
