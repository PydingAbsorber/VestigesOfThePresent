package com.pyding.vp.network.packets;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LorePacket(int number) implements CustomPacketPayload {

    public static final Type<LorePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "lore_packet"));

    public static final StreamCodec<FriendlyByteBuf, LorePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, LorePacket::number,
            LorePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final LorePacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            // Проверка стороны через FMLEnvironment
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClientHandler.handleLore(payload.number());
            }
        });
    }

    private static class ClientHandler {
        private static void handleLore(int number) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            String name = VPUtil.getRainbowString(VPUtil.generateRandomString("entity".length())) + ": ";
            String playerName = player.getDisplayName().getString() + ": ";

            if (number == 1)
                player.sendSystemMessage(Component.translatable("vp.lore.1"));
            else if (number == 2)
                player.sendSystemMessage(Component.translatable("vp.lore.2"));
            else if (number == 3) {
                player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.3.1")).withStyle(ChatFormatting.DARK_PURPLE));
                player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.3.2")));
                player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.3.3")).withStyle(ChatFormatting.DARK_PURPLE));
                player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.3.4")));
                player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.3.5")).withStyle(ChatFormatting.DARK_PURPLE));
                player.sendSystemMessage(Component.translatable("vp.lore.3.6"));
            } else if (number == 4)
                player.sendSystemMessage(Component.translatable("vp.lore.4"));
            else if (number == 5)
                player.sendSystemMessage(Component.translatable("vp.lore.5"));
            else if (number == 6)
                player.sendSystemMessage(Component.translatable("vp.lore.6"));
            else if (number == 7) {
                player.sendSystemMessage(Component.translatable("vp.lore.7"));
                player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.7.1")).withStyle(ChatFormatting.DARK_PURPLE));
                player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.7.2")));
                player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.7.3")).withStyle(ChatFormatting.DARK_PURPLE));
                player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.7.4")));
                player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.7.5")).withStyle(ChatFormatting.DARK_PURPLE));
                player.sendSystemMessage(Component.translatable("vp.lore.7.6"));
                player.sendSystemMessage(Component.translatable("vp.lore.7.7"));
            } else if (number == 8)
                player.sendSystemMessage(Component.translatable("vp.lore.8"));
            else if (number == 9) {
                player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.9.1")).withStyle(ChatFormatting.DARK_PURPLE));
                player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.9.2")));
                player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.9.3")).withStyle(ChatFormatting.DARK_PURPLE));
                player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.9.4")));
                player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.9.5")).withStyle(ChatFormatting.DARK_PURPLE));
                player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.9.6")));
                player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.9.7")).withStyle(ChatFormatting.DARK_PURPLE));
                player.sendSystemMessage(Component.literal(playerName).append(Component.translatable("vp.lore.9.8")));
                player.sendSystemMessage(Component.literal(name).append(Component.translatable("vp.lore.9.9")).withStyle(ChatFormatting.DARK_PURPLE));
                player.sendSystemMessage(Component.translatable("vp.lore.9.10"));
            }
        }
    }
}
