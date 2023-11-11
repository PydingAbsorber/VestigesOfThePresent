package com.pyding.vp.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public class PlayerCapabilityVP {
    private int[] challenges = new int[12];

    public int getChallenge(int vp) {
        if (vp >= 1 && vp <= 12) {
            return challenges[vp - 1];
        } else {
            return 0;
        }
    }

    public void setChallenge(int vp, int value) {
        if (vp >= 1 && vp <= 12) {
            challenges[vp - 1] = value;
        }
    }

    public void copyNBT(PlayerCapabilityVP source){
        this.challenges = source.challenges;
    }

    public void saveNBT(CompoundTag nbt){
        for(int i = 0; i < challenges.length; i++){
            nbt.putInt("challenge"+i,challenges[i]);
        }
    }

    public void loadNBT(CompoundTag nbt){
        for(int i = 0; i < challenges.length; i++){
            challenges[i] = nbt.getInt("challenge"+i);
        }
    }
}
