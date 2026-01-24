package com.pyding.vp.network.packets;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SuckPacket(float number, BlockPos pos) implements CustomPacketPayload {

    public static final Type<SuckPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "suck_packet"));

    public static final StreamCodec<FriendlyByteBuf, SuckPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, SuckPacket::number,
            BlockPos.STREAM_CODEC, SuckPacket::pos,
            SuckPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SuckPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClientHandler.handle(payload);
            }
        });
    }

    private static class ClientHandler {
        private static void handle(SuckPacket msg) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                VPUtil.suckToPos(player, msg.pos(), msg.number());
            }
        }
    }
}
