package com.pyding.vp.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ButtonPacket {
    private int id;

    public ButtonPacket(int identifier) {
        id = identifier;
    }

    public static void encode(ButtonPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
    }

    public static ButtonPacket decode(FriendlyByteBuf buf) {
        return new ButtonPacket(buf.readInt());
    }

    public static void handle(ButtonPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getSender() != null)
                handle2(ctx.get().getSender(),msg.id);
        });

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    private static void handle2(ServerPlayer player, int id) {
        player.getPersistentData().putBoolean("VPButton"+id,true);
        System.out.println("Special" + id + " packet received");
    }
}
