package com.pyding.vp.network.packets;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SuckPacket {
    private final float number;
    private final BlockPos pos;

    public SuckPacket(float number, BlockPos pos) {
        this.number = number;
        this.pos = pos;
    }

    public static void encode(SuckPacket msg, FriendlyByteBuf buf) {
        buf.writeFloat(msg.number);
        buf.writeBlockPos(msg.pos);
    }

    public static SuckPacket decode(FriendlyByteBuf buf) {
        return new SuckPacket(buf.readFloat(),buf.readBlockPos());
    }

    public static void handle(SuckPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            handle2(msg.number, msg.pos);
        });

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle2(float number, BlockPos pos) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null)
            return;
        VPUtil.suckToPos(player,pos,number);
    }
}
