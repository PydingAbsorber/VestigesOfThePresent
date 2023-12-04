package com.pyding.vp.item.artifacts;

import net.minecraft.ChatFormatting;

public class Killer extends Vestige{
    public Killer(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(4, ChatFormatting.YELLOW, 1, 10, 1, 60, 1, 20, true);
    }
}
