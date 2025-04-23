package com.pyding.vp.network.packets;

import com.pyding.vp.item.CelestialMirror;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.MysteryChest;
import com.pyding.vp.item.VipActivator;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class SendClientDataToServerPacket {
    private int id;
    private String message;
    public SendClientDataToServerPacket(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public static void encode(SendClientDataToServerPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
        buf.writeUtf(msg.message);
    }

    public static SendClientDataToServerPacket decode(FriendlyByteBuf buf) {
        return new SendClientDataToServerPacket(buf.readInt(),buf.readUtf());
    }

    public static boolean handle(SendClientDataToServerPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if(msg.id == 1){
                VPUtil.osMap.put(player.getUUID(),msg.message);
            }
        });
        return true;
    }
}
