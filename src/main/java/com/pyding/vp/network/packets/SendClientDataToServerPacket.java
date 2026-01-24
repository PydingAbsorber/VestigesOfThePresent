package com.pyding.vp.network.packets;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.util.VPUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SendClientDataToServerPacket(int id, String message) implements CustomPacketPayload {

    public static final Type<SendClientDataToServerPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "send_client_data"));

    public static final StreamCodec<FriendlyByteBuf, SendClientDataToServerPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SendClientDataToServerPacket::id,
            ByteBufCodecs.STRING_UTF8, SendClientDataToServerPacket::message,
            SendClientDataToServerPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // Обработка на стороне сервера
    public static void handle(final SendClientDataToServerPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (payload.id() == 1) {
                    VPUtil.osMap.put(player.getUUID(), payload.message());
                }
            }
        });
    }
}
