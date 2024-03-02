package com.pyding.vp.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SendPlayerNbtToClient {
    private UUID playerID;
    private CompoundTag tag;

    public SendPlayerNbtToClient(UUID playerID, CompoundTag tag) {
        this.playerID = playerID;
        this.tag = tag;
    }

    public static void encode(SendPlayerNbtToClient msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerID);
        buf.writeNbt(msg.tag);
    }

    public static SendPlayerNbtToClient decode(FriendlyByteBuf buf) {
        return new SendPlayerNbtToClient(buf.readUUID(), buf.readNbt());
    }

    public static void handle(SendPlayerNbtToClient msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            handle2(msg.playerID, msg.tag);
        });

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle2(UUID playerID, CompoundTag tag) {
        Minecraft.getInstance().level.players().stream().filter(player -> player.getUUID().equals(playerID))
            .findAny().ifPresent(player -> {
                //System.out.println("from packet " + tag);
                player.getPersistentData().merge(tag);
            });
    }
}
