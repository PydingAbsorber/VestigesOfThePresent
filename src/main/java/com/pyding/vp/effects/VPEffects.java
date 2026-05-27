package com.pyding.vp.effects;

import com.pyding.vp.VestigesOfThePresent;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VPEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "vp");
    public static final RegistryObject<MobEffect> VIP_EFFECT = EFFECTS.register("vip_effect", VipEffect::new);
    public static final RegistryObject<MobEffect> ANTI_SHIELD = EFFECTS.register("anti_shield", AntiShieldEffect::new);
    public static final RegistryObject<MobEffect> ANTI_TELEPORT = EFFECTS.register("anti_teleport", AntiTeleportEffect::new);
    public static final RegistryObject<MobEffect> BOUND = EFFECTS.register("bound", BoundEffect::new);
    public static final RegistryObject<MobEffect> DISAPPOINED = EFFECTS.register("disappointed", DisappointedEffect::new);
    public static final RegistryObject<MobEffect> SILENCE = EFFECTS.register("silence", SilenceEffect::new);
    public static final RegistryObject<MobEffect> ORCHESTRA = EFFECTS.register("orchestra", SilenceEffect::new);
}
