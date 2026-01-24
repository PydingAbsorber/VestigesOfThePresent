package com.pyding.vp.network.packets;

import com.pyding.vp.VestigesOfThePresent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ItemAnimationPacket(ItemStack stack) implements CustomPacketPayload {

    public static final Type<ItemAnimationPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "item_animation"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemAnimationPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, ItemAnimationPacket::stack,
            ItemAnimationPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ItemAnimationPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClientProxy.displayAnimation(payload.stack());
            }
        });
    }

    private static class ClientProxy {
        public static void displayAnimation(ItemStack stack) {
            Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
        }
    }
}
