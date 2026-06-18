package com.pyding.vp.network.packets;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.capability.PlayerCapabilityVP;
import com.pyding.vp.item.*;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.ServerConfig;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class SendClientDataToServerPacket {
    private int id;
    private String message;
    public SendClientDataToServerPacket(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public static void encode(SendClientDataToServerPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
        buf.writeUtf(msg.message);
    }

    public static SendClientDataToServerPacket decode(FriendlyByteBuf buf) {
        return new SendClientDataToServerPacket(buf.readInt(),buf.readUtf());
    }

    public static boolean handle(SendClientDataToServerPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if(msg.id == 1){
                VPUtil.osMap.put(player.getUUID(),msg.message);
            } else if(msg.id == 2){
                if(player.getMainHandItem().getItem() instanceof WelcomeBook){
                    int challengeDifficulty = 0;
                    int vestigePower = 0;
                    int worldDifficulty = 0;
                    int count = 0;
                    for (String id : msg.message.split(",")) {
                        if (count == 0)
                            challengeDifficulty = Integer.parseInt(id);
                        else if (count == 1)
                            vestigePower = Integer.parseInt(id);
                        else worldDifficulty = Integer.parseInt(id);
                        count++;
                    }
                    List<Integer> reduceList = new ArrayList<>();
                    ServerConfig.COMMON.reduceChallengesPercent.set(true);
                    if (challengeDifficulty == 1) {
                        for (int i = 0; i < PlayerCapabilityVP.totalVestiges; i++)
                            reduceList.add(50);
                    } else if (challengeDifficulty == 2) {
                        for (int i = 0; i < PlayerCapabilityVP.totalVestiges; i++)
                            reduceList.add(25);
                    } else if (challengeDifficulty == 3) {
                        for (int i = 0; i < PlayerCapabilityVP.totalVestiges; i++)
                            reduceList.add(10);
                    } else if (challengeDifficulty == 4) {
                        for (int i = 0; i < PlayerCapabilityVP.totalVestiges; i++)
                            reduceList.add(0);
                    }
                    ServerConfig.COMMON.reduceChallenges.set(reduceList);
                    List<Integer> scaleList = new ArrayList<>();
                    if (vestigePower == 1) {
                        for (int i = 0; i < PlayerCapabilityVP.totalVestiges; i++)
                            scaleList.add(5);
                        ServerConfig.COMMON.powerBoost.set(2D);
                    } else if (vestigePower == 2) {
                        for (int i = 0; i < PlayerCapabilityVP.totalVestiges; i++)
                            scaleList.add(30);
                        ServerConfig.COMMON.powerBoost.set(5D);
                    } else if (vestigePower == 3) {
                        for (int i = 0; i < PlayerCapabilityVP.totalVestiges; i++)
                            scaleList.add(100);
                        ServerConfig.COMMON.powerBoost.set(0D);
                    }
                    ServerConfig.COMMON.powerScales.set(scaleList);
                    if (worldDifficulty == 2) {
                        ServerConfig.COMMON.cruelMode.set(true);
                    } else if (worldDifficulty == 3) {
                        ServerConfig.COMMON.cruelMode.set(true);
                        ServerConfig.COMMON.armorCruel.set(120);
                        ServerConfig.COMMON.damageCruel.set(0.3d);
                        ServerConfig.COMMON.absorbCruel.set(0.1d);
                        ServerConfig.COMMON.shieldCruel.set(1.5d);
                        ServerConfig.COMMON.overShieldCruel.set(0.5d);
                        ServerConfig.COMMON.expMultiplier.set(20d);
                        ServerConfig.COMMON.empoweredChance.set(0.02d);
                        ServerConfig.COMMON.healthBoost.set(4.0d);
                        ServerConfig.COMMON.bossHP.set(3.0d);
                        ServerConfig.COMMON.bossAttack.set(6.0d);
                        ServerConfig.COMMON.healPercent.set(0.01d);
                        ServerConfig.COMMON.cruelItemChance.set(0.002d);
                    }
                    player.getMainHandItem().shrink(1);
                }
                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                    cap.initMaximum(player);
                    cap.sync(player);
                });
                VPUtil.sync(player);
            }
        });
        return true;
    }
}
