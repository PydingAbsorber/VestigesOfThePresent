package com.pyding.vp.item.artifacts;

import net.minecraft.ChatFormatting;

public class Flower extends Vestige{
    public Flower(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(16, ChatFormatting.DARK_GREEN, 2, 10, 1, 30, 5, 30, hasDamage);
    }
}
