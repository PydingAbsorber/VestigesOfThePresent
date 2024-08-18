package com.pyding.vp.network;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.network.packets.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(VestigesOfThePresent.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.registerMessage(id(),SendPlayerNbtToClient.class, SendPlayerNbtToClient::encode, SendPlayerNbtToClient::decode, SendPlayerNbtToClient::handle);
        net.registerMessage(id(),SendPlayerCapaToClient.class, SendPlayerCapaToClient::encode, SendPlayerCapaToClient::decode, SendPlayerCapaToClient::handle);
        net.messageBuilder(ClientToServerPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ClientToServerPacket::new)
                .encoder(ClientToServerPacket::toBytes)
                .consumerMainThread(ClientToServerPacket::handle)
                .add();
        net.registerMessage(id(),PlayerFlyPacket.class, PlayerFlyPacket::encode, PlayerFlyPacket::decode, PlayerFlyPacket::handle);
        net.registerMessage(id(),SendEntityNbtToClient.class, SendEntityNbtToClient::encode, SendEntityNbtToClient::decode, SendEntityNbtToClient::handle);
        net.registerMessage(id(),SoundPacket.class, SoundPacket::encode, SoundPacket::decode, SoundPacket::handle);
        net.registerMessage(id(),ParticlePacket.class, ParticlePacket::encode, ParticlePacket::decode, ParticlePacket::handle);
        net.registerMessage(id(),ButtonPressPacket.class, ButtonPressPacket::encode, ButtonPressPacket::decode, ButtonPressPacket::handle);
        net.registerMessage(id(),LorePacket.class, LorePacket::encode, LorePacket::decode, LorePacket::handle);
        net.registerMessage(id(),SuckPacket.class, SuckPacket::encode, SuckPacket::decode, SuckPacket::handle);
        //net.registerMessage(id(),ItemAnimationPacket.class, ItemAnimationPacket::encode, ItemAnimationPacket::decode, ItemAnimationPacket::handle);
        net.messageBuilder(ItemAnimationPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ItemAnimationPacket::new)
                .encoder(ItemAnimationPacket::toBytes)
                .consumerMainThread(ItemAnimationPacket::handle)
                .add();
        net.registerMessage(id(),StackNbtSync.class, StackNbtSync::encode, StackNbtSync::decode, StackNbtSync::handle);
    }
    public static void sendToClient(Object packet, ServerPlayer player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToAllAround(MSG message, Player player) {
        INSTANCE.send(PacketDistributor.NEAR.with(() ->
                new PacketDistributor.TargetPoint(player.getX(), player.getY(), player.getZ(), 64,
                        player.getCommandSenderWorld().dimension())),message);
    }

    public static void sendToClients(PacketDistributor.PacketTarget target, Object packet) {
        INSTANCE.send(target, packet);
    }
}
