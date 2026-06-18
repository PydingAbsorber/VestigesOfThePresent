package com.pyding.vp.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class OrchestraEffect extends MobEffect {

    public OrchestraEffect() {
        super(MobEffectCategory.NEUTRAL, 0xffd700);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        return true;
    }
}
