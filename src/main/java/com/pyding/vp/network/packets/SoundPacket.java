package com.pyding.vp.network.packets;

import com.pyding.vp.util.VPUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class SoundPacket {
    private final ResourceLocation soundLocation;
    private final float volume;
    private final float pitch;

    public SoundPacket(ResourceLocation soundLocation, float volume, float pitch) {
        this.soundLocation = soundLocation;
        this.volume = volume;
        this.pitch = pitch;
    }

    public static void encode(SoundPacket packet, FriendlyByteBuf buf) {
        buf.writeResourceLocation(packet.soundLocation);
        buf.writeFloat(packet.volume);
        buf.writeFloat(packet.pitch);
    }

    public static SoundPacket decode(FriendlyByteBuf buf) {
        return new SoundPacket(buf.readResourceLocation(), buf.readFloat(), buf.readFloat());
    }

    public static void handle(SoundPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Проверьте, необходимо ли добавить дополнительные проверки безопасности
            SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(packet.soundLocation);
            if (soundEvent != null) {
                // Воспроизвести звук на клиенте
                // Может потребоваться доступ к клиентскому миру или Minecraft.getInstance() для воспроизведения звука
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), soundEvent, SoundSource.MASTER, packet.volume, packet.pitch, false);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
