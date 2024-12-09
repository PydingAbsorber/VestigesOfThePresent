package com.pyding.vp.network.packets;

import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class StackNbtSync {
    private UUID playerID;
    private CompoundTag tag;

    private ItemStack stack;

    public StackNbtSync(UUID playerID, CompoundTag tag, ItemStack stack) {
        this.playerID = playerID;
        this.tag = tag;
        this.stack = stack;
    }

    public static void encode(StackNbtSync msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerID);
        buf.writeNbt(msg.tag);
        buf.writeItemStack(msg.stack,false);
    }

    public static StackNbtSync decode(FriendlyByteBuf buf) {
        return new StackNbtSync(buf.readUUID(), buf.readNbt(),buf.readItem());
    }

    public static void handle(StackNbtSync msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            handle2(msg.playerID, msg.tag,msg.stack);
        });

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle2(UUID playerID, CompoundTag tag,ItemStack stack) {
        Minecraft.getInstance().level.players().stream().filter(player -> player.getUUID().equals(playerID))
            .findAny().ifPresent(player -> {
                if(stack.getItem() instanceof Vestige vestige) {
                    ItemStack vestigeStack = VPUtil.getVestigeStack(vestige, player);
                    vestigeStack.getOrCreateTag().merge(tag);
                }
            });
    }
}
