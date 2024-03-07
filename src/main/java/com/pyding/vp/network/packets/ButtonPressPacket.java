package com.pyding.vp.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ButtonPressPacket {
    private int id;
    public ButtonPressPacket(int id) {
        this.id = id;
    }

    public static void encode(ButtonPressPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
    }

    public static ButtonPressPacket decode(FriendlyByteBuf buf) {
        return new ButtonPressPacket(buf.readInt());
    }

    public static boolean handle(ButtonPressPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            player.getPersistentData().putBoolean("VPButton"+msg.id,true);
            System.out.println(msg.id + " packet received");
        });
        return true;
    }
}
