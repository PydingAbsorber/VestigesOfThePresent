package com.pyding.vp.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BoundEffect extends MobEffect {

    public BoundEffect() {
        super(MobEffectCategory.NEUTRAL, 0xffd700);
    }

    @Override
    public boolean applyEffectTick(net.minecraft.world.entity.LivingEntity entity, int amplifier) {
        return true;
    }
}
