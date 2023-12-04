package com.pyding.vp.network.packets;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SendPlayerCapaToClient {
    private CompoundTag tag;

    public SendPlayerCapaToClient(CompoundTag tag) {
        this.tag = tag;
    }

    public static void encode(SendPlayerCapaToClient msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.tag);
    }

    public static SendPlayerCapaToClient decode(FriendlyByteBuf buf) {
        return new SendPlayerCapaToClient(buf.readNbt());
    }

    public static void handle(SendPlayerCapaToClient msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;

            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                cap.saveNBT(msg.tag);
                System.out.println("packet sync");
                System.out.println(msg.tag);
            });
        });

        ctx.get().setPacketHandled(true);
    }

}
