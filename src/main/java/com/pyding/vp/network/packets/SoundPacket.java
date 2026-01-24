package com.pyding.vp.network.packets;

import com.pyding.vp.VestigesOfThePresent;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.pyding.vp.util.VPUtil.soundCd;

public record SoundPacket(ResourceLocation soundLocation, float volume, float pitch, double x, double y,
                          double z) implements CustomPacketPayload {

    public static final Type<SoundPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "sound_packet"));

    public static final StreamCodec<FriendlyByteBuf, SoundPacket> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, SoundPacket::soundLocation,
            ByteBufCodecs.FLOAT, SoundPacket::volume,
            ByteBufCodecs.FLOAT, SoundPacket::pitch,
            ByteBufCodecs.DOUBLE, SoundPacket::x,
            ByteBufCodecs.DOUBLE, SoundPacket::y,
            ByteBufCodecs.DOUBLE, SoundPacket::z,
            SoundPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SoundPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClientHandler.handle(payload);
            }
        });
    }

    private static class ClientHandler {
        private static void handle(SoundPacket msg) {
            SoundEvent soundEvent = BuiltInRegistries.SOUND_EVENT.get(msg.soundLocation());
            if (soundEvent != null) {
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    ResourceLocation soundLocation = msg.soundLocation();
                    double x = msg.x();
                    double y = msg.y();
                    double z = msg.z();
                    float volume = msg.volume();
                    float pitch = msg.pitch();
                    if (!soundCd.containsKey(soundLocation))
                        soundCd.put(soundLocation, 0L);
                    if (soundCd.get(soundLocation) < System.currentTimeMillis()) {
                        if (x == 0 && y == 0 && z == 0)
                            player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), soundEvent, SoundSource.MASTER, volume, pitch, false);
                        else
                            player.getCommandSenderWorld().playLocalSound(x, y, z, soundEvent, SoundSource.MASTER, volume, pitch, false);
                        soundCd.put(soundLocation, System.currentTimeMillis() + 100);
                    }
                }
            }
        }
    }
}
