package com.pyding.vp.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SendEntityNbtToClient {
    private CompoundTag tag;
    private int id;

    public SendEntityNbtToClient(CompoundTag tag, int identifier) {
        this.tag = tag;
        id = identifier;
    }

    public static void encode(SendEntityNbtToClient msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.tag);
        buf.writeInt(msg.id);
    }

    public static SendEntityNbtToClient decode(FriendlyByteBuf buf) {
        return new SendEntityNbtToClient(buf.readNbt(), buf.readInt());
    }

    public static void handle(SendEntityNbtToClient msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            handle2(msg.tag, msg.id);
        });

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle2(CompoundTag tag, int id) {
        Level level = Minecraft.getInstance().level;
        Entity entity = level.getEntity(id);
        if(entity != null) {
            entity.getPersistentData().merge(tag);
        }
    }
}
