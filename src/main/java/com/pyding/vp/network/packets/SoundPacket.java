package com.pyding.vp.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;
import java.util.function.Supplier;

public class SoundPacket {
    private final ResourceLocation soundLocation;
    private final float volume;
    private final float pitch;
    private final double x;
    private final double y;
    private final double z;

    public SoundPacket(ResourceLocation soundLocation, float volume, float pitch,double x, double y, double z) {
        this.soundLocation = soundLocation;
        this.volume = volume;
        this.pitch = pitch;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void encode(SoundPacket packet, FriendlyByteBuf buf) {
        buf.writeResourceLocation(packet.soundLocation);
        buf.writeFloat(packet.volume);
        buf.writeFloat(packet.pitch);
        buf.writeDouble(packet.x);
        buf.writeDouble(packet.y);
        buf.writeDouble(packet.z);
    }

    public static SoundPacket decode(FriendlyByteBuf buf) {
        return new SoundPacket(buf.readResourceLocation(), buf.readFloat(), buf.readFloat(),buf.readDouble(),buf.readDouble(),buf.readDouble());
    }
    public static void handle(SoundPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            handle2(msg.soundLocation, msg.volume,msg.pitch,msg.x,msg.y, msg.z);
        });

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle2(ResourceLocation soundLocation, float volume, float pitch, double x, double y, double z) {
        SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(soundLocation);
        if (soundEvent != null) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                if(x == 0 && y == 0 && z == 0)
                    player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), soundEvent, SoundSource.MASTER, volume, pitch, false);
                else player.getCommandSenderWorld().playLocalSound(x, y, z, soundEvent, SoundSource.MASTER, volume, pitch, false);
            }
        }
    }
}
