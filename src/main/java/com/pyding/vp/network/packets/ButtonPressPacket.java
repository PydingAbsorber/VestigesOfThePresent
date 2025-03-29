package com.pyding.vp.network.packets;

import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.MysteryChest;
import com.pyding.vp.network.PacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
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
            if(msg.id == 6665242){
                if(player.getMainHandItem().is(ModItems.MYSTERY_CHEST.get())) {
                    player.getMainHandItem().shrink(1);
                    Map<ItemStack,String> map = MysteryChest.getRandomDrop();
                    ItemStack drop = null;
                    for(ItemStack stack: map.keySet())
                        drop = stack;
                    PacketHandler.sendToClient(new SendStackToClient(player.getUUID(),drop,map.get(drop)),player);
                    ItemStack finalDrop = drop;
                    CompletableFuture.runAsync(() -> {
                        player.addItem(finalDrop);
                    }, CompletableFuture.delayedExecutor(4, TimeUnit.SECONDS));
                }
            }
            else player.getPersistentData().putBoolean("VPButton"+msg.id,true);
        });
        return true;
    }
}
