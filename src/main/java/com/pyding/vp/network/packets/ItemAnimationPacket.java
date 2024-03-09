package com.pyding.vp.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ItemAnimationPacket {
    private final ItemStack stack;

    public ItemAnimationPacket(FriendlyByteBuf buf) {
        stack = buf.readItem();
    }

    public ItemAnimationPacket(ItemStack stack) {
        this.stack = stack;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(stack);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> Minecraft.getInstance().gameRenderer.displayItemActivation(stack));
        return true;
    }
}
