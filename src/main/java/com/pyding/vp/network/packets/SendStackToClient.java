package com.pyding.vp.network.packets;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.client.MysteryChestScreen;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.item.MysteryChest;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public record SendStackToClient(UUID playerID, ItemStack stack, String info) implements CustomPacketPayload {

    public static final Type<SendStackToClient> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "send_stack_to_client"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SendStackToClient> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, SendStackToClient::playerID,
            ItemStack.OPTIONAL_STREAM_CODEC, SendStackToClient::stack,
            ByteBufCodecs.STRING_UTF8, SendStackToClient::info,
            SendStackToClient::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SendStackToClient payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClientHandler.handle(payload);
            }
        });
    }

    private static class ClientHandler {
        private static void handle(SendStackToClient msg) {
            var level = Minecraft.getInstance().level;
            if (level == null) return;

            level.players().stream()
                    .filter(player -> player.getUUID().equals(msg.playerID()))
                    .findAny()
                    .ifPresent(player -> {
                        MysteryChestScreen.drop = msg.stack();

                        String info = msg.info();
                        if (!info.isEmpty()) {
                            MysteryChestScreen.rarity = info;

                            SoundEvent sound = switch (info) {
                                case "legendary" -> SoundRegistry.CHEST_LEGENDARY.get();
                                case "mythic" -> SoundRegistry.CHEST_MYTHIC.get();
                                case "rare" -> SoundRegistry.CHEST_RARE.get();
                                default -> SoundRegistry.CHEST_COMMON.get();
                            };

                            Map<ItemStack, String> mapList = new HashMap<>();
                            Random random = new Random();
                            int count = random.nextInt(10) + 15;
                            for (int i = 0; i < count; i++) {
                                mapList.putAll(MysteryChest.getRandomDrop());
                            }

                            MysteryChestScreen.randomItems.clear();
                            MysteryChestScreen.randomItems.putAll(mapList);

                            player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), sound, SoundSource.MASTER, 1, 1, false);
                        }
                    });
        }
    }
}
