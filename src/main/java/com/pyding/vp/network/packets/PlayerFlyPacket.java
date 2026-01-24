package com.pyding.vp.network.packets;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.client.LeaderboardScreen;
import com.pyding.vp.util.ClientConfig;
import com.pyding.vp.util.LeaderboardUtil;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Set;

public class PlayerFlyPacket implements CustomPacketPayload {

    public static final Type<PlayerFlyPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "player_fly"));

    public static final StreamCodec<FriendlyByteBuf, PlayerFlyPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PlayerFlyPacket::getNumber,
            PlayerFlyPacket::new
    );

    private final int number;

    public PlayerFlyPacket(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final PlayerFlyPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClientHandler.handle(payload.getNumber());
            }
        });
    }

    private static class ClientHandler {
        private static void handle(int number) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            if (number == 1) {
                player.lerpMotion(0, 3, 0);
            }
            else if (number == 2) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
            }
            else if (number == -1) {
                VPUtil.fall(player, -10);
            }
            else if (number == 69) {
                BlockPos pos = new BlockPos(
                        (int) player.getPersistentData().getDouble("VPDevourerX"),
                        (int) player.getPersistentData().getDouble("VPDevourerY"),
                        (int) player.getPersistentData().getDouble("VPDevourerZ")
                );
                VPUtil.suckToPos(player, pos, 3);
            }
            else if (number == 3) {
                VPUtil.clearEffects(player, true);
            }
            else if (number == 4) {
                VPUtil.clearEffects(player, false);
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400, 4));
            }
            else if (number == 5) {
                player.addEffect(new MobEffectInstance(VPUtil.getRandomEffect(false), 1200));
            }
            else if (number == 6) {
                player.getAbilities().mayfly = true;
                player.getAbilities().flying = true;
                player.onUpdateAbilities();
            }
            else if (number == 7) {
                LeaderboardUtil.refreshTopPlayers();
            }
            else if (number == 8) {
                if (Minecraft.getInstance().screen != null) {
                    Minecraft.getInstance().screen.onClose();
                }
                long deathTime = System.currentTimeMillis() + VPUtil.deathTime;
                VPUtil.antiResurrect(player, deathTime);
                VPUtil.setRoflanEbalo(player, deathTime);
                VPUtil.setHealth(player, 0);
                player.die(player.damageSources().genericKill());
                VPUtil.despawn(player);
                Minecraft.getInstance().forceSetScreen(new DeathScreen(Component.literal("Death by Paragon Damage"), false));
            }
            else if (number == 9) {
                VPUtil.roflan.put(player.getUUID(), 0L);
            }
            else if (number == 10) {
                VPUtil.antiResurrect(player, -1);
            }
            else if (number == 11) {
                VPUtil.antiTp(player, -1);
            }
            else if (number == 12) {
                player.setShowDeathScreen(true);
            }
            else if (number == 13) {
                ClientConfig.renderSoulIntegrity.set(!ClientConfig.renderSoulIntegrity.get());
            }
            else if (number == 14) {
                Minecraft.getInstance().setScreen(new LeaderboardScreen());
            }
            else if (number == 278) {
                if (Minecraft.getInstance().level != null) {
                    Set<ResourceKey<Biome>> biomes = Minecraft.getInstance().level.registryAccess()
                            .registryOrThrow(Registries.BIOME).registryKeySet();
                    VPUtil.biomeNames.addAll(biomes);
                }
            }
            else if (number > 300) {
                BlockPos pos = new BlockPos(
                        (int) player.getPersistentData().getDouble("VPDevourerX"),
                        (int) player.getPersistentData().getDouble("VPDevourerY"),
                        (int) player.getPersistentData().getDouble("VPDevourerZ")
                );
                VPUtil.suckToPos(player, pos, number - 300);
            }
        }
    }
}
