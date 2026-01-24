package com.pyding.vp.network.packets;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record StackNbtSync(UUID playerID, CompoundTag tag, ItemStack stack) implements CustomPacketPayload {

    public static final Type<StackNbtSync> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "stack_nbt_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, StackNbtSync> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, StackNbtSync::playerID,
            ByteBufCodecs.COMPOUND_TAG, StackNbtSync::tag,
            ItemStack.OPTIONAL_STREAM_CODEC, StackNbtSync::stack,
            StackNbtSync::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final StackNbtSync payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClientHandler.handle(payload);
            }
        });
    }

    private static class ClientHandler {
        private static void handle(StackNbtSync msg) {
            var level = Minecraft.getInstance().level;
            if (level == null) return;

            level.players().stream()
                    .filter(player -> player.getUUID().equals(msg.playerID()))
                    .findAny()
                    .ifPresent(player -> {
                        if (msg.stack().getItem() instanceof Vestige vestige) {
                            ItemStack vestigeStack = VPUtil.getVestigeStack(vestige, player);
                            CustomData.update(DataComponents.CUSTOM_DATA, vestigeStack, currentTag -> {
                                currentTag.merge(msg.tag());
                            });
                        }
                    });
        }
    }
}
