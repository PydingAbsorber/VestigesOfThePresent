package com.pyding.vp.client.sounds;

import com.pyding.vp.VestigesOfPresent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, VestigesOfPresent.MODID);

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
    public static RegistryObject<SoundEvent> WIND1 = registerSoundEvent("wind1");
    public static RegistryObject<SoundEvent> WIND2 = registerSoundEvent("wind2");
    public static RegistryObject<SoundEvent> BLACK_HOLE = registerSoundEvent("black_hole");
    public static RegistryObject<SoundEvent> HORN1 = registerSoundEvent("horn1");
    public static RegistryObject<SoundEvent> HORN2 = registerSoundEvent("horn2");
    public static RegistryObject<SoundEvent> SHIELD = registerSoundEvent("shield");
    public static RegistryObject<SoundEvent> SHIELD_BREAK = registerSoundEvent("shield_break");
    public static RegistryObject<SoundEvent> DEATH1 = registerSoundEvent("death1");
    public static RegistryObject<SoundEvent> DEATH2 = registerSoundEvent("death2");
    public static RegistryObject<SoundEvent> CHAOS_CORE = registerSoundEvent("chaos_core");
    public static RegistryObject<SoundEvent> CROWN = registerSoundEvent("crown");
    public static RegistryObject<SoundEvent> CROWN_ULT = registerSoundEvent("crown_ult");
    public static RegistryObject<SoundEvent> DEVOURER_BIND = registerSoundEvent("devourer_bind");
    public static RegistryObject<SoundEvent> EXPLODE1 = registerSoundEvent("explode1");
    public static RegistryObject<SoundEvent> EXPLODE2 = registerSoundEvent("explode2");
    public static RegistryObject<SoundEvent> RAGE = registerSoundEvent("rage");
    public static RegistryObject<SoundEvent> SUCCESS = registerSoundEvent("success");
    public static RegistryObject<SoundEvent> IMPACT = registerSoundEvent("impact");
    public static RegistryObject<SoundEvent> STOLAS1 = registerSoundEvent("stolas1");
    public static RegistryObject<SoundEvent> HEAL1 = registerSoundEvent("heal1");
    public static RegistryObject<SoundEvent> HEAL2 = registerSoundEvent("heal2");
    public static RegistryObject<SoundEvent> HEAL3 = registerSoundEvent("heal3");
    public static RegistryObject<SoundEvent> MAGIC_EFFECT1 = registerSoundEvent("magic_effect1");
    public static RegistryObject<SoundEvent> MAGIC_EFFECT2 = registerSoundEvent("magic_effect2");
    public static RegistryObject<SoundEvent> MAGIC1 = registerSoundEvent("magic1");
    public static RegistryObject<SoundEvent> MAGIC2 = registerSoundEvent("magic2");
    public static RegistryObject<SoundEvent> MAGIC3 = registerSoundEvent("magic3");
    public static RegistryObject<SoundEvent> SOUL = registerSoundEvent("soul");
    public static RegistryObject<SoundEvent> SOUL2 = registerSoundEvent("soul2");
    public static RegistryObject<SoundEvent> SOUL3 = registerSoundEvent("soul3");
    public static RegistryObject<SoundEvent> WIND3 = registerSoundEvent("wind3");
    public static RegistryObject<SoundEvent> OVERSHIELD = registerSoundEvent("overshield");
    public static RegistryObject<SoundEvent> OVERSHIELD_BREAK = registerSoundEvent("overshield_break");
    public static RegistryObject<SoundEvent> BOLT = registerSoundEvent("bolt");
    public static RegistryObject<SoundEvent> CATALYST1 = registerSoundEvent("catalyst1");
    public static RegistryObject<SoundEvent> CATALYST2 = registerSoundEvent("catalyst2");
    public static RegistryObject<SoundEvent> MAGIC4 = registerSoundEvent("magic4");
    public static RegistryObject<SoundEvent> MAGIC5 = registerSoundEvent("magic5");
    public static RegistryObject<SoundEvent> TELEPORT1 = registerSoundEvent("teleport1");
    public static RegistryObject<SoundEvent> TELEPORT2 = registerSoundEvent("teleport2");
    public static RegistryObject<SoundEvent> TRIGON2 = registerSoundEvent("trigon2");


    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(VestigesOfPresent.MODID, name)));
    }
}