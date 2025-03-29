package com.pyding.vp.network.packets;

import com.pyding.vp.client.MysteryChestScreen;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SendStackToClient {
    private UUID playerID;

    private ItemStack stack;
    private String info;

    public SendStackToClient(UUID playerID, ItemStack stack, String info) {
        this.playerID = playerID;
        this.stack = stack;
        this.info = info;
    }

    public static void encode(SendStackToClient msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerID);
        buf.writeItemStack(msg.stack,false);
        buf.writeUtf(msg.info);
    }

    public static SendStackToClient decode(FriendlyByteBuf buf) {
        return new SendStackToClient(buf.readUUID(),buf.readItem(), buf.readUtf());
    }

    public static void handle(SendStackToClient msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            handle2(msg.playerID, msg.stack,msg.info);
        });

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle2(UUID playerID, ItemStack stack, String info) {
        Minecraft.getInstance().level.players().stream().filter(player -> player.getUUID().equals(playerID))
            .findAny().ifPresent(player -> {
                    MysteryChestScreen.drop = stack;
                    if(!info.isEmpty()) {
                        MysteryChestScreen.rarity = info;
                        SoundEvent sound = switch (info) {
                            case "legendary" -> SoundRegistry.CHEST_LEGENDARY.get();
                            case "mythic" -> SoundRegistry.CHEST_MYTHIC.get();
                            case "rare" -> SoundRegistry.CHEST_RARE.get();
                            default -> SoundRegistry.CHEST_COMMON.get();
                        };
                        player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), sound, SoundSource.MASTER, 1, 1, false);
                    }
            });
    }
}
