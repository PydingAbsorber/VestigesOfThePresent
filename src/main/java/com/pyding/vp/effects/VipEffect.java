package com.pyding.vp.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class VipEffect extends MobEffect {

    public VipEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xffd700);
    }

    @Override
    public boolean applyEffectTick(net.minecraft.world.entity.LivingEntity entity, int amplifier) {
        return true;
    }
}
