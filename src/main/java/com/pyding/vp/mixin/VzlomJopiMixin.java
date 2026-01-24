package com.pyding.vp.mixin;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RangedAttribute.class)
public interface VzlomJopiMixin {

    @Accessor("maxValue")
    @Mutable
    void vzlomatJopu(double maxValue);
}
