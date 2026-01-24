package com.pyding.vp.network.packets;

import com.pyding.vp.VestigesOfThePresent;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SendEntityNbtToClient(CompoundTag tag, int id) implements CustomPacketPayload {

    public static final Type<SendEntityNbtToClient> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "send_entity_nbt"));

    public static final StreamCodec<FriendlyByteBuf, SendEntityNbtToClient> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, SendEntityNbtToClient::tag,
            ByteBufCodecs.INT, SendEntityNbtToClient::id,
            SendEntityNbtToClient::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SendEntityNbtToClient payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClientHandler.handle(payload);
            }
        });
    }

    private static class ClientHandler {
        private static void handle(SendEntityNbtToClient msg) {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                Entity entity = level.getEntity(msg.id());
                if (entity != null) {
                    entity.getPersistentData().merge(msg.tag());
                }
            }
        }
    }
}
