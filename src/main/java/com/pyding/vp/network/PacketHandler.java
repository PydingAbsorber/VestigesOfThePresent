package com.pyding.vp.network;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.network.packets.*;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = VestigesOfThePresent.MODID)
public class PacketHandler {

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0");

        registrar.playToClient(SendPlayerNbtToClient.TYPE, SendPlayerNbtToClient.STREAM_CODEC, SendPlayerNbtToClient::handle);
        registrar.playToClient(SendPlayerCapaToClient.TYPE, SendPlayerCapaToClient.STREAM_CODEC, SendPlayerCapaToClient::handle);
        registrar.playToClient(PlayerFlyPacket.TYPE, PlayerFlyPacket.STREAM_CODEC, PlayerFlyPacket::handle);
        registrar.playToClient(SendEntityNbtToClient.TYPE, SendEntityNbtToClient.STREAM_CODEC, SendEntityNbtToClient::handle);
        registrar.playToClient(SoundPacket.TYPE, SoundPacket.STREAM_CODEC, SoundPacket::handle);
        registrar.playToClient(ParticlePacket.TYPE, ParticlePacket.STREAM_CODEC, ParticlePacket::handle);
        registrar.playToClient(LorePacket.TYPE, LorePacket.STREAM_CODEC, LorePacket::handle);
        registrar.playToClient(SuckPacket.TYPE, SuckPacket.STREAM_CODEC, SuckPacket::handle);
        registrar.playToClient(ItemAnimationPacket.TYPE, ItemAnimationPacket.STREAM_CODEC, ItemAnimationPacket::handle);
        registrar.playToClient(StackNbtSync.TYPE, StackNbtSync.STREAM_CODEC, StackNbtSync::handle);
        registrar.playToClient(SendStackToClient.TYPE, SendStackToClient.STREAM_CODEC, SendStackToClient::handle);

        registrar.playToServer(ButtonPressPacket.TYPE, ButtonPressPacket.STREAM_CODEC, ButtonPressPacket::handle);
        registrar.playToServer(SendClientDataToServerPacket.TYPE, SendClientDataToServerPacket.STREAM_CODEC, SendClientDataToServerPacket::handle);
    }

    public static void sendToClient(CustomPacketPayload packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    public static void sendToServer(CustomPacketPayload packet) {
        PacketDistributor.sendToServer(packet);
    }

    public static void sendToPlayer(CustomPacketPayload packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    public static void sendToAllAround(CustomPacketPayload packet, Player player) {
        if(player.level() instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersNear(
                    serverLevel,
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    64,
                    packet
            );
        }
    }

    public static void sendToAllAround(CustomPacketPayload packet, Entity entity) {
        Player player = entity.level().getNearestPlayer(entity,32);
        if(player != null && player.level() instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersNear(
                    serverLevel,
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    64,
                    packet
            );
        }
    }
}
