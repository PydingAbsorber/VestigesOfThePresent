package com.pyding.vp.network.packets;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.item.CelestialMirror;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.MysteryChest;
import com.pyding.vp.item.VipActivator;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public record ButtonPressPacket(int id) implements CustomPacketPayload {

    public static final Type<ButtonPressPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "button_press"));

    public static final StreamCodec<FriendlyByteBuf, ButtonPressPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, ButtonPressPacket::id,
            ButtonPressPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ButtonPressPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;
            int msgId = payload.id();
            if (msgId == 6665242) {
                ItemStack mainHand = player.getMainHandItem();
                if (mainHand.is(ModItems.MYSTERY_CHEST.get())) {
                    mainHand.shrink(1);
                    Map<ItemStack, String> map = MysteryChest.getRandomDrop();
                    ItemStack drop = null;
                    // Сохраняем твой цикл получения последнего элемента
                    for (ItemStack stack : map.keySet()) {
                        drop = stack;
                    }
                    if (drop != null) {
                        PacketHandler.sendToClient(new SendStackToClient(player.getUUID(), drop, map.get(drop)), player);
                        final ItemStack finalDrop = drop;
                        CompletableFuture.runAsync(() -> {
                            if (finalDrop.getItem() instanceof CelestialMirror) {
                                ItemStack mirror = new ItemStack(ModItems.CELESTIAL_MIRROR.get(), 1);
                                UUID uuid = UUID.randomUUID();
                                CustomData.update(DataComponents.CUSTOM_DATA, mirror, tag -> {
                                    tag.putUUID("VPMirror", uuid);
                                });
                                VPUtil.giveStack(mirror, player);
                            } else if (finalDrop.getItem() instanceof VipActivator) {
                                CustomData.update(DataComponents.CUSTOM_DATA, finalDrop, tag -> {
                                    tag.putInt("VPDays", 3);
                                });

                                VPUtil.giveStack(finalDrop, player);
                            } else {
                                VPUtil.giveStack(finalDrop, player);
                            }
                        }, CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS));
                    }
                }
            } else if (msgId == 42) {
                if (player.hasPermissions(2)) {
                    ConfigHandler.lootDrops.set(ConfigHandler.getDefaultLoot());
                    MysteryChest.init();
                }
            } else {
                player.getPersistentData().putBoolean("VPButton" + msgId, true);
            }
        });
    }
}
