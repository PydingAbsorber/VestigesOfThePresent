package com.pyding.vp.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface IVPCapability extends INBTSerializable<CompoundTag> {

    class VPCapability implements IVPCapability {
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

        @Override
        public CompoundTag serializeNBT() {
            final CompoundTag tag = new CompoundTag();

            for(int i = 0; i < challenges.length; i++){
                tag.putInt("challenge"+i,challenges[i]);
            }

            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            for(int i = 0; i < challenges.length; i++){
                challenges[i] = nbt.getInt("challenge"+i);
            }
        }

    }

    class VPCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        private final IVPCapability backend = new IVPCapability.VPCapability();

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return CapabilityRegistry.DATA.orEmpty(cap, LazyOptional.of(() -> backend));
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }
    }

}
