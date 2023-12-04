package com.pyding.vp.item.artifacts;

import net.minecraft.ChatFormatting;

public class Chaos extends Vestige{
    public Chaos(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(14, ChatFormatting.DARK_PURPLE, 1, 20, 1, 20, 20, 20, hasDamage);
    }
}
