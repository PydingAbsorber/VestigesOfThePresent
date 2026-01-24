package com.pyding.vp.network.packets;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.util.VPUtilParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ParticlePacket implements CustomPacketPayload {

    public static final Type<ParticlePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "particle_packet"));

    public static final StreamCodec<FriendlyByteBuf, ParticlePacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ParticlePacket decode(FriendlyByteBuf buf) {
            return new ParticlePacket(
                    buf.readInt(),    // id
                    buf.readDouble(), // x
                    buf.readDouble(), // y
                    buf.readDouble(), // z
                    buf.readDouble(), // deltaX
                    buf.readDouble(), // deltaY
                    buf.readDouble() // deltaZ
            );
        }

        @Override
        public void encode(FriendlyByteBuf buf, ParticlePacket p) {
            buf.writeInt(p.id);
            buf.writeDouble(p.x);
            buf.writeDouble(p.y);
            buf.writeDouble(p.z);
            buf.writeDouble(p.deltaX);
            buf.writeDouble(p.deltaY);
            buf.writeDouble(p.deltaZ);
        }
    };

    private final int id;
    private final double x, y, z;
    private final double deltaX, deltaY, deltaZ;

    public ParticlePacket(int id, double x, double y, double z, double deltaX, double deltaY, double deltaZ) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ParticlePacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClientHandler.handle(payload);
            }
        });
    }

    private static class ClientHandler {
        private static void handle(ParticlePacket msg) {
            ParticleOptions options = VPUtilParticles.getParticleById(msg.id);
            if (options != null) {
                Minecraft mc = Minecraft.getInstance();
                Player player = mc.player;
                if (player != null) {
                    if (msg.id == 666) {
                        mc.particleEngine.crack(new BlockPos((int) msg.x, (int) msg.y, (int) msg.z), Direction.UP);
                    } else {
                        player.level().addParticle(options, msg.x, msg.y, msg.z, msg.deltaX, msg.deltaY, msg.deltaZ);
                    }
                }
            }
        }
    }
}
