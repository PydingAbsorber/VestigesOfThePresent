package com.pyding.vp.capability;

import com.pyding.vp.VestigesOfThePresent;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import java.util.function.Supplier;

public class VestigeCapProvider {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, VestigesOfThePresent.MODID);

    public static final Supplier<AttachmentType<VestigeCap>> PLAYER_DATA = ATTACHMENT_TYPES.register(
            "player_data",
            () -> AttachmentType.builder(VestigeCap::new)
                    .serialize(new IAttachmentSerializer<CompoundTag, VestigeCap>() {
                        @Override
                        public VestigeCap read(IAttachmentHolder iAttachmentHolder, CompoundTag compoundTag, HolderLookup.Provider provider) {
                            VestigeCap cap = new VestigeCap();
                            cap.deserializeNBT(provider, compoundTag);
                            return cap;
                        }

                        @Override
                        public CompoundTag write(VestigeCap attachment, HolderLookup.Provider provider) {
                            return attachment.serializeNBT(provider);
                        }
                    })
                    .copyOnDeath()
                    .build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
