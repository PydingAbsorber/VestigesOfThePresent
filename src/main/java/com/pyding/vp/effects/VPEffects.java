package com.pyding.vp.effects;

import com.pyding.vp.VestigesOfThePresent;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VPEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "vp");
    public static final RegistryObject<MobEffect> VIP_EFFECT = EFFECTS.register("vip_effect", VipEffect::new);

}
