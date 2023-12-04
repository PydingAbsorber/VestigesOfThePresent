package com.pyding.vp.item.artifacts;

import net.minecraft.ChatFormatting;

public class Book extends Vestige{
    public Book(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(12, ChatFormatting.LIGHT_PURPLE, 1, 10, 1, 200, 15, 60, hasDamage);
    }
}
