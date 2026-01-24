package com.pyding.vp.client.sounds;

import com.pyding.vp.VestigesOfThePresent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, VestigesOfThePresent.MODID);

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

    public static final DeferredHolder<SoundEvent, SoundEvent> WIND1 = registerSoundEvent("wind1");
    public static final DeferredHolder<SoundEvent, SoundEvent> WIND2 = registerSoundEvent("wind2");
    public static final DeferredHolder<SoundEvent, SoundEvent> BLACK_HOLE = registerSoundEvent("black_hole");
    public static final DeferredHolder<SoundEvent, SoundEvent> HORN1 = registerSoundEvent("horn1");
    public static final DeferredHolder<SoundEvent, SoundEvent> HORN2 = registerSoundEvent("horn2");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHIELD = registerSoundEvent("shield");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHIELD_BREAK = registerSoundEvent("shield_break");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEATH1 = registerSoundEvent("death1");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEATH2 = registerSoundEvent("death2");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHAOS_CORE = registerSoundEvent("chaos_core");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROWN = registerSoundEvent("crown");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROWN_ULT = registerSoundEvent("crown_ult");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEVOURER_BIND = registerSoundEvent("devourer_bind");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLODE1 = registerSoundEvent("explode1");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLODE2 = registerSoundEvent("explode2");
    public static final DeferredHolder<SoundEvent, SoundEvent> RAGE = registerSoundEvent("rage");
    public static final DeferredHolder<SoundEvent, SoundEvent> SUCCESS = registerSoundEvent("success");
    public static final DeferredHolder<SoundEvent, SoundEvent> IMPACT = registerSoundEvent("impact");
    public static final DeferredHolder<SoundEvent, SoundEvent> STOLAS1 = registerSoundEvent("stolas1");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEAL1 = registerSoundEvent("heal1");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEAL2 = registerSoundEvent("heal2");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEAL3 = registerSoundEvent("heal3");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAGIC_EFFECT1 = registerSoundEvent("magic_effect1");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAGIC_EFFECT2 = registerSoundEvent("magic_effect2");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAGIC1 = registerSoundEvent("magic1");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAGIC2 = registerSoundEvent("magic2");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAGIC3 = registerSoundEvent("magic3");
    public static final DeferredHolder<SoundEvent, SoundEvent> SOUL = registerSoundEvent("soul");
    public static final DeferredHolder<SoundEvent, SoundEvent> SOUL2 = registerSoundEvent("soul2");
    public static final DeferredHolder<SoundEvent, SoundEvent> SOUL3 = registerSoundEvent("soul3");
    public static final DeferredHolder<SoundEvent, SoundEvent> WIND3 = registerSoundEvent("wind3");
    public static final DeferredHolder<SoundEvent, SoundEvent> OVERSHIELD = registerSoundEvent("overshield");
    public static final DeferredHolder<SoundEvent, SoundEvent> OVERSHIELD_BREAK = registerSoundEvent("overshield_break");
    public static final DeferredHolder<SoundEvent, SoundEvent> BOLT = registerSoundEvent("bolt");
    public static final DeferredHolder<SoundEvent, SoundEvent> CATALYST1 = registerSoundEvent("catalyst1");
    public static final DeferredHolder<SoundEvent, SoundEvent> CATALYST2 = registerSoundEvent("catalyst2");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAGIC4 = registerSoundEvent("magic4");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAGIC5 = registerSoundEvent("magic5");
    public static final DeferredHolder<SoundEvent, SoundEvent> TELEPORT1 = registerSoundEvent("teleport1");
    public static final DeferredHolder<SoundEvent, SoundEvent> TELEPORT2 = registerSoundEvent("teleport2");
    public static final DeferredHolder<SoundEvent, SoundEvent> TRIGON2 = registerSoundEvent("trigon2");
    public static final DeferredHolder<SoundEvent, SoundEvent> FLESH = registerSoundEvent("flesh");
    public static final DeferredHolder<SoundEvent, SoundEvent> FLESH2 = registerSoundEvent("flesh2");
    public static final DeferredHolder<SoundEvent, SoundEvent> GRAVITY = registerSoundEvent("gravity");
    public static final DeferredHolder<SoundEvent, SoundEvent> OPEN = registerSoundEvent("open");
    public static final DeferredHolder<SoundEvent, SoundEvent> RUNE1 = registerSoundEvent("rune1");
    public static final DeferredHolder<SoundEvent, SoundEvent> RUNE2 = registerSoundEvent("rune2");
    public static final DeferredHolder<SoundEvent, SoundEvent> LYREULT = registerSoundEvent("lyreult");
    public static final DeferredHolder<SoundEvent, SoundEvent> BUBBLEPOP = registerSoundEvent("bubblepop");
    public static final DeferredHolder<SoundEvent, SoundEvent> BUBBLE1 = registerSoundEvent("bubble1");
    public static final DeferredHolder<SoundEvent, SoundEvent> BUBBLE2 = registerSoundEvent("bubble2");
    public static final DeferredHolder<SoundEvent, SoundEvent> BUBBLE3 = registerSoundEvent("bubble3");
    public static final DeferredHolder<SoundEvent, SoundEvent> BUBBLE4 = registerSoundEvent("bubble4");
    public static final DeferredHolder<SoundEvent, SoundEvent> BUBBLE5 = registerSoundEvent("bubble5");
    public static final DeferredHolder<SoundEvent, SoundEvent> LYRESONG1 = registerSoundEvent("lyresong1");
    public static final DeferredHolder<SoundEvent, SoundEvent> LYRESONG2 = registerSoundEvent("lyresong2");
    public static final DeferredHolder<SoundEvent, SoundEvent> LYRESONG3 = registerSoundEvent("lyresong3");
    public static final DeferredHolder<SoundEvent, SoundEvent> LYRESONG4 = registerSoundEvent("lyresong4");
    public static final DeferredHolder<SoundEvent, SoundEvent> LYRESONG5 = registerSoundEvent("lyresong5");
    public static final DeferredHolder<SoundEvent, SoundEvent> LYRE1 = registerSoundEvent("lyre1");
    public static final DeferredHolder<SoundEvent, SoundEvent> LYRE2 = registerSoundEvent("lyre2");
    public static final DeferredHolder<SoundEvent, SoundEvent> LYRE3 = registerSoundEvent("lyre3");
    public static final DeferredHolder<SoundEvent, SoundEvent> LYRE4 = registerSoundEvent("lyre4");
    public static final DeferredHolder<SoundEvent, SoundEvent> LYRE5 = registerSoundEvent("lyre5");
    public static final DeferredHolder<SoundEvent, SoundEvent> LYRE6 = registerSoundEvent("lyre6");
    public static final DeferredHolder<SoundEvent, SoundEvent> LYRE7 = registerSoundEvent("lyre7");
    public static final DeferredHolder<SoundEvent, SoundEvent> LYRE8 = registerSoundEvent("lyre8");
    public static final DeferredHolder<SoundEvent, SoundEvent> DESPAWN = registerSoundEvent("despawn");
    public static final DeferredHolder<SoundEvent, SoundEvent> DROP_CORRUPT1 = registerSoundEvent("drop_corrupt1");
    public static final DeferredHolder<SoundEvent, SoundEvent> DROP_CORRUPT2 = registerSoundEvent("drop_corrupt2");
    public static final DeferredHolder<SoundEvent, SoundEvent> DROP_CHAOS = registerSoundEvent("drop_chaos");
    public static final DeferredHolder<SoundEvent, SoundEvent> DROP_MIRROR = registerSoundEvent("drop_mirror");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHEST_FALL = registerSoundEvent("chest_fall");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHEST_COMMON = registerSoundEvent("chest_common");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHEST_RARE = registerSoundEvent("chest_rare");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHEST_MYTHIC = registerSoundEvent("chest_mythic");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHEST_LEGENDARY = registerSoundEvent("chest_legendary");
    public static final DeferredHolder<SoundEvent, SoundEvent> AMBIENT = registerSoundEvent("ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> ARROW_READY_1 = registerSoundEvent("arrow_ready_1");
    public static final DeferredHolder<SoundEvent, SoundEvent> ARROW_READY_2 = registerSoundEvent("arrow_ready_2");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAGIC_ARROW_1 = registerSoundEvent("magic_arrow_1");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAGIC_ARROW_2 = registerSoundEvent("magic_arrow_2");
    public static final DeferredHolder<SoundEvent, SoundEvent> BOOK_OPEN = registerSoundEvent("book_open");
    public static final DeferredHolder<SoundEvent, SoundEvent> BOOK_CLOSE = registerSoundEvent("book_close");
    public static final DeferredHolder<SoundEvent, SoundEvent> BOOK_PAGE1 = registerSoundEvent("book_page1");
    public static final DeferredHolder<SoundEvent, SoundEvent> VESTIGE_GUI_OPEN_1 = registerSoundEvent("vestige_gui_open_1");
    public static final DeferredHolder<SoundEvent, SoundEvent> VESTIGE_GUI_OPEN_2 = registerSoundEvent("vestige_gui_open_2");
    public static final DeferredHolder<SoundEvent, SoundEvent> VESTIGE_GUI_BUTTON = registerSoundEvent("vestige_gui_button");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, name)));
    }
}