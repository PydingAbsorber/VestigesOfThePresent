package com.pyding.vp.network.packets;

import com.pyding.vp.util.VPUtilParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Method;
import java.util.function.Supplier;

public class ParticlePacket {
    private final int id;
    private final double x;
    private final double y;
    private final double z;
    private final double deltaX;
    private final double deltaY;
    private final double deltaZ;
    private final float size;

    public ParticlePacket(int id, double x, double y, double z, double deltaX, double deltaY, double deltaZ) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.size = 0;
    }

    public ParticlePacket(int id, double x, double y, double z, double deltaX, double deltaY, double deltaZ,float size) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.size = size;
    }

    public static void encode(ParticlePacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.id);
        buf.writeDouble(packet.x);
        buf.writeDouble(packet.y);
        buf.writeDouble(packet.z);
        buf.writeDouble(packet.deltaX);
        buf.writeDouble(packet.deltaY);
        buf.writeDouble(packet.deltaZ);
        buf.writeFloat(packet.size);
    }

    public static ParticlePacket decode(FriendlyByteBuf buf) {
        return new ParticlePacket(buf.readInt(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble());
    }
    public static void handle(ParticlePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            handle2(msg.id,msg.x,msg.y, msg.z, msg.deltaX, msg.deltaY, msg.deltaZ,msg.size);
        });

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle2(int id, double x, double y, double z, double deltaX, double deltaY, double deltaZ,float size) {
        ParticleOptions options = VPUtilParticles.getParticleById(id);
        if (options != null) {
            if(size == 0) {
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    player.getCommandSenderWorld().addParticle(options, x, y, z, deltaX, deltaY, deltaZ);
                }
            }
        }
    }
}
