package com.pyding.vp.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class SendEntityNbtToClient {
    private CompoundTag tag;
    public BlockPos pos;

    public SendEntityNbtToClient(CompoundTag tag, BlockPos position) {
        this.tag = tag;
        pos = position;
    }

    public static void encode(SendEntityNbtToClient msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.tag);
        buf.writeBlockPos(msg.pos);
    }

    public static SendEntityNbtToClient decode(FriendlyByteBuf buf) {
        return new SendEntityNbtToClient(buf.readNbt(), buf.readBlockPos());
    }

    public static void handle(SendEntityNbtToClient msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            handle2(msg.tag, msg.pos);
        });

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle2(CompoundTag tag, BlockPos pos) {
        double r = 40;
        List<LivingEntity> entityList = Minecraft.getInstance().level.getNearbyEntities(LivingEntity.class,null,null, new AABB(pos.getX()+r,pos.getY()+r,pos.getZ()+r,pos.getX()-r,pos.getY()-r,pos.getZ()-r));
        System.out.println(entityList);
        for(LivingEntity entity: entityList){
            entity.getPersistentData().merge(tag);
        }
    }
}
