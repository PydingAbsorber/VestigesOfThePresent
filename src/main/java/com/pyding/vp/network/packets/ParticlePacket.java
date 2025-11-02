package com.pyding.vp.network.packets;

import com.pyding.vp.util.VPUtilParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ParticlePacket {
    private final int id;
    private final double x;
    private final double y;
    private final double z;
    private final double deltaX;
    private final double deltaY;
    private final double deltaZ;
    private final double color1;
    private final double color2;
    private final double color3;

    public ParticlePacket(int id, double x, double y, double z, double deltaX, double deltaY, double deltaZ) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.color1 = 0;
        this.color2 = 0;
        this.color3 = 0;
    }

    public ParticlePacket(int id, double x, double y, double z, double deltaX, double deltaY, double deltaZ,double color1,double color2,double color3) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
    }

    public static void encode(ParticlePacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.id);
        buf.writeDouble(packet.x);
        buf.writeDouble(packet.y);
        buf.writeDouble(packet.z);
        buf.writeDouble(packet.deltaX);
        buf.writeDouble(packet.deltaY);
        buf.writeDouble(packet.deltaZ);
        buf.writeDouble(packet.color1);
        buf.writeDouble(packet.color2);
        buf.writeDouble(packet.color3);
    }

    public static ParticlePacket decode(FriendlyByteBuf buf) {
        return new ParticlePacket(buf.readInt(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble(),buf.readDouble());
    }
    public static void handle(ParticlePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            handle2(msg.id,msg.x,msg.y, msg.z, msg.deltaX, msg.deltaY, msg.deltaZ,msg.color1,msg.color2,msg.color3);
        });

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle2(int id, double x, double y, double z, double deltaX, double deltaY, double deltaZ,double color1,double color2,double color3) {
        ParticleOptions options = VPUtilParticles.getParticleById(id);
        if (options != null) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player != null) {
                if(id == 666){
                    mc.particleEngine.crack(new BlockPos((int) x, (int) y, (int) z), Direction.UP);
                } else player.getCommandSenderWorld().addParticle(options, x, y, z, deltaX, deltaY, deltaZ);
            }
        }
    }
}
