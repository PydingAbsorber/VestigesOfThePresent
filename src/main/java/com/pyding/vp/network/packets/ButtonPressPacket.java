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
                        if(finalDrop.getItem() instanceof CelestialMirror){
                            ItemStack mirror = new ItemStack(ModItems.CELESTIAL_MIRROR.get(), 1);
                            UUID uuid = UUID.randomUUID();
                            mirror.getOrCreateTag().putUUID("VPMirror",uuid);
                            VPUtil.giveStack(mirror,player);
                        } else if(finalDrop.getItem() instanceof VipActivator){
                            finalDrop.getOrCreateTag().putInt("VPDays",3);
                            VPUtil.giveStack(finalDrop,player);
                        }
                        else VPUtil.giveStack(finalDrop,player);
                    }, CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS));
                }
            } else if(msg.id == 42){
                if(player.hasPermissions(2)){
                    ConfigHandler.COMMON.lootDrops.set(ConfigHandler.Common.DEFAULT_LOOT);
                    MysteryChest.init();
                }
            }
            else player.getPersistentData().putBoolean("VPButton"+msg.id,true);
        });
        return true;
    }
}
