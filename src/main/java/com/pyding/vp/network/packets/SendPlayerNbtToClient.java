package com.pyding.vp.network.packets;

import com.pyding.vp.VestigesOfThePresent;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record SendPlayerNbtToClient(UUID playerID, CompoundTag tag) implements CustomPacketPayload {

    public static final Type<SendPlayerNbtToClient> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "send_player_nbt"));

    public static final StreamCodec<FriendlyByteBuf, SendPlayerNbtToClient> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, (p) -> p.playerID,
            ByteBufCodecs.COMPOUND_TAG, (p) -> p.tag,
            SendPlayerNbtToClient::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SendPlayerNbtToClient payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().level().isClientSide) {
                var level = Minecraft.getInstance().level;
                if (level != null) {
                    level.players().stream()
                            .filter(p -> p.getUUID().equals(payload.playerID()))
                            .findAny()
                            .ifPresent(p -> p.getPersistentData().merge(payload.tag()));
                }
            }
        });
    }
}
