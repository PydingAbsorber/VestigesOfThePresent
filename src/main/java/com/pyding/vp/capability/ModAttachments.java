package com.pyding.vp.capability;

import com.pyding.vp.VestigesOfThePresent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, VestigesOfThePresent.MODID);

    public static final Supplier<AttachmentType<VestigeCap>> PLAYER_DATA =
            ATTACHMENT_TYPES.register("properties", () -> AttachmentType.builder(() -> new VestigeCap())
                    // Для классов, реализующих INBTSerializable, используется этот метод:
                    .serialize(VestigeCap.CODEC)
                    .copyOnDeath()
                    .build());
}
