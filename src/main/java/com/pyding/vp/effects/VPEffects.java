package com.pyding.vp.effects;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class VPEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, "vp");

    public static final DeferredHolder<MobEffect, MobEffect> VIP_EFFECT =
            EFFECTS.register("vip_effect", VipEffect::new);

    public static final DeferredHolder<MobEffect, MobEffect> ANTI_SHIELD = EFFECTS.register("anti_shield", AntiShieldEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> ANTI_TELEPORT = EFFECTS.register("anti_teleport", AntiTeleportEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> BOUND = EFFECTS.register("bound", BoundEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> DISAPPOINED = EFFECTS.register("disappointed", DisappointedEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> SILENCE = EFFECTS.register("silence", SilenceEffect::new);
    public static final DeferredHolder<MobEffect, MobEffect> ORCHESTRA = EFFECTS.register("orchestra", SilenceEffect::new);

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
