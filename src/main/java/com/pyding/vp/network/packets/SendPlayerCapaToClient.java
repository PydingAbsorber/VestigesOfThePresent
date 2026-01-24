package com.pyding.vp.network.packets;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.capability.VestigeCapProvider;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SendPlayerCapaToClient(CompoundTag tag) implements CustomPacketPayload {

    public static final Type<SendPlayerCapaToClient> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "send_player_capa"));

    public static final StreamCodec<FriendlyByteBuf, SendPlayerCapaToClient> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, SendPlayerCapaToClient::tag,
            SendPlayerCapaToClient::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SendPlayerCapaToClient payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().level().isClientSide()) {
                ClientHandler.handle(payload.tag());
            }
        });
    }

    private static class ClientHandler {
        public static void handle(CompoundTag tag) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                if (player.hasData(VestigeCapProvider.PLAYER_DATA)) {
                    var cap = player.getData(VestigeCapProvider.PLAYER_DATA);
                    cap.deserializeNBT(player.registryAccess(), tag);
                    VPUtil.updatePowerList(player);
                }
            }
        }
    }
}
