package com.pyding.vp.client.sounds;

import com.pyding.vp.VestigesOfThePresent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, VestigesOfThePresent.MODID);

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
    public static RegistryObject<SoundEvent> FLESH = registerSoundEvent("flesh");
    public static RegistryObject<SoundEvent> FLESH2 = registerSoundEvent("flesh2");
    public static RegistryObject<SoundEvent> GRAVITY = registerSoundEvent("gravity");
    public static RegistryObject<SoundEvent> OPEN = registerSoundEvent("open");
    public static RegistryObject<SoundEvent> RUNE1 = registerSoundEvent("rune1");
    public static RegistryObject<SoundEvent> RUNE2 = registerSoundEvent("rune2");
    public static RegistryObject<SoundEvent> LYREULT = registerSoundEvent("lyreult");
    public static RegistryObject<SoundEvent> BUBBLEPOP = registerSoundEvent("bubblepop");
    public static RegistryObject<SoundEvent> BUBBLE1 = registerSoundEvent("bubble1");
    public static RegistryObject<SoundEvent> BUBBLE2 = registerSoundEvent("bubble2");
    public static RegistryObject<SoundEvent> BUBBLE3 = registerSoundEvent("bubble3");
    public static RegistryObject<SoundEvent> BUBBLE4 = registerSoundEvent("bubble4");
    public static RegistryObject<SoundEvent> BUBBLE5 = registerSoundEvent("bubble5");
    public static RegistryObject<SoundEvent> LYRESONG1 = registerSoundEvent("lyresong1");
    public static RegistryObject<SoundEvent> LYRESONG2 = registerSoundEvent("lyresong2");
    public static RegistryObject<SoundEvent> LYRESONG3 = registerSoundEvent("lyresong3");
    public static RegistryObject<SoundEvent> LYRESONG4 = registerSoundEvent("lyresong4");
    public static RegistryObject<SoundEvent> LYRESONG5 = registerSoundEvent("lyresong5");
    public static RegistryObject<SoundEvent> LYRE1 = registerSoundEvent("lyre1");
    public static RegistryObject<SoundEvent> LYRE2 = registerSoundEvent("lyre2");
    public static RegistryObject<SoundEvent> LYRE3 = registerSoundEvent("lyre3");
    public static RegistryObject<SoundEvent> LYRE4 = registerSoundEvent("lyre4");
    public static RegistryObject<SoundEvent> LYRE5 = registerSoundEvent("lyre5");
    public static RegistryObject<SoundEvent> LYRE6 = registerSoundEvent("lyre6");
    public static RegistryObject<SoundEvent> LYRE7 = registerSoundEvent("lyre7");
    public static RegistryObject<SoundEvent> LYRE8 = registerSoundEvent("lyre8");
    public static RegistryObject<SoundEvent> DESPAWN = registerSoundEvent("despawn");
    public static RegistryObject<SoundEvent> DROP_CORRUPT1 = registerSoundEvent("drop_corrupt1");
    public static RegistryObject<SoundEvent> DROP_CORRUPT2 = registerSoundEvent("drop_corrupt2");
    public static RegistryObject<SoundEvent> DROP_CHAOS = registerSoundEvent("drop_chaos");
    public static RegistryObject<SoundEvent> DROP_MIRROR = registerSoundEvent("drop_mirror");
    public static RegistryObject<SoundEvent> CHEST_FALL = registerSoundEvent("chest_fall");
    public static RegistryObject<SoundEvent> CHEST_COMMON = registerSoundEvent("chest_common");
    public static RegistryObject<SoundEvent> CHEST_RARE = registerSoundEvent("chest_rare");
    public static RegistryObject<SoundEvent> CHEST_MYTHIC = registerSoundEvent("chest_mythic");
    public static RegistryObject<SoundEvent> CHEST_LEGENDARY = registerSoundEvent("chest_legendary");
    public static RegistryObject<SoundEvent> AMBIENT = registerSoundEvent("ambient");
    public static RegistryObject<SoundEvent> ARROW_READY_1 = registerSoundEvent("arrow_ready_1");
    public static RegistryObject<SoundEvent> ARROW_READY_2 = registerSoundEvent("arrow_ready_2");
    public static RegistryObject<SoundEvent> MAGIC_ARROW_1 = registerSoundEvent("magic_arrow_1");
    public static RegistryObject<SoundEvent> MAGIC_ARROW_2 = registerSoundEvent("magic_arrow_2");
    public static RegistryObject<SoundEvent> BOOK_OPEN = registerSoundEvent("book_open");
    public static RegistryObject<SoundEvent> BOOK_CLOSE = registerSoundEvent("book_close");
    public static RegistryObject<SoundEvent> BOOK_PAGE1 = registerSoundEvent("book_page1");
    public static RegistryObject<SoundEvent> VESTIGE_GUI_OPEN_1 = registerSoundEvent("vestige_gui_open_1");
    public static RegistryObject<SoundEvent> VESTIGE_GUI_OPEN_2 = registerSoundEvent("vestige_gui_open_2");
    public static RegistryObject<SoundEvent> VESTIGE_GUI_BUTTON = registerSoundEvent("vestige_gui_button");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(VestigesOfThePresent.MODID, name)));
    }
}