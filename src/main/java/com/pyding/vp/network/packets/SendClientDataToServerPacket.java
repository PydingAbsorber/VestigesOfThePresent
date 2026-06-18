package com.pyding.vp.network.packets;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.capability.VestigeCap;
import com.pyding.vp.item.WelcomeBook;
import com.pyding.vp.util.ServerConfig;
import com.pyding.vp.util.VPUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record SendClientDataToServerPacket(int id, String message) implements CustomPacketPayload {

    public static final Type<SendClientDataToServerPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID, "send_client_data"));

    public static final StreamCodec<FriendlyByteBuf, SendClientDataToServerPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SendClientDataToServerPacket::id,
            ByteBufCodecs.STRING_UTF8, SendClientDataToServerPacket::message,
            SendClientDataToServerPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // Обработка на стороне сервера
    public static void handle(final SendClientDataToServerPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                if (payload.id() == 1) {
                    VPUtil.osMap.put(player.getUUID(), payload.message());
                } else if(payload.id() == 2){
                    if((!ServerConfig.usedBook.get() || player.isCreative())){
                        ServerConfig.usedBook.set(true);
                        int challengeDifficulty = 0;
                        int vestigePower = 0;
                        int worldDifficulty = 0;
                        int count = 0;
                        for (String id : payload.message.split(",")) {
                            if (count == 0)
                                challengeDifficulty = Integer.parseInt(id);
                            else if (count == 1)
                                vestigePower = Integer.parseInt(id);
                            else worldDifficulty = Integer.parseInt(id);
                            count++;
                        }
                        List<Integer> reduceList = new ArrayList<>();
                        ServerConfig.reduceChallengesPercent.set(true);
                        if (challengeDifficulty == 1){
                            for(int i = 0; i < VestigeCap.totalVestiges; i++)
                                reduceList.add(50);
                        } else if (challengeDifficulty == 2){
                            for(int i = 0; i < VestigeCap.totalVestiges; i++)
                                reduceList.add(25);
                        } else if (challengeDifficulty == 3){
                            for(int i = 0; i < VestigeCap.totalVestiges; i++)
                                reduceList.add(10);
                        } else if (challengeDifficulty == 4){
                            for(int i = 0; i < VestigeCap.totalVestiges; i++)
                                reduceList.add(0);
                        }
                        ServerConfig.reduceChallenges.set(reduceList);
                        List<Integer> scaleList = new ArrayList<>();
                        if(vestigePower == 1){
                            for(int i = 0; i < VestigeCap.totalVestiges; i++)
                                scaleList.add(5);
                            ServerConfig.powerBoost.set(2D);
                        } else if(vestigePower == 2){
                            for(int i = 0; i < VestigeCap.totalVestiges; i++)
                                scaleList.add(30);
                            ServerConfig.powerBoost.set(5D);
                        }else if(vestigePower == 3){
                            for(int i = 0; i < VestigeCap.totalVestiges; i++)
                                scaleList.add(100);
                            ServerConfig.powerBoost.set(0D);
                        }
                        ServerConfig.powerScales.set(scaleList);
                        if(worldDifficulty == 2){
                            ServerConfig.cruelMode.set(true);
                        } else if(worldDifficulty == 3){
                            ServerConfig.cruelMode.set(true);
                            ServerConfig.armorCruel.set(120);
                            ServerConfig.damageCruel.set(0.3d);
                            ServerConfig.absorbCruel.set(0.1d);
                            ServerConfig.shieldCruel.set(1.5d);
                            ServerConfig.overShieldCruel.set(0.5d);
                            ServerConfig.expMultiplier.set(20d);
                            ServerConfig.empoweredChance.set(0.02d);
                            ServerConfig.healthBoost.set(4.0d);
                            ServerConfig.bossHP.set(3.0d);
                            ServerConfig.bossAttack.set(6.0d);
                            ServerConfig.healPercent.set(0.01d);
                            ServerConfig.cruelItemChance.set(0.002d);
                        }
                        ServerConfig.SPEC.save();
                    }
                    VestigeCap cap = VPUtil.getCap(player);
                    cap.initMaximum(player);
                    cap.sync(player);
                    VPUtil.sync(player);
                }
            }
        });
    }
}
