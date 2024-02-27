package com.pyding.vp.network.packets;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerFlyPacket {
    private final int number;

    public PlayerFlyPacket(int number) {
        this.number = number;
    }

    public static void encode(PlayerFlyPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.number);
    }

    public static PlayerFlyPacket decode(FriendlyByteBuf buf) {
        return new PlayerFlyPacket(buf.readInt());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            handle2();
        });

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private void handle2() {
        LocalPlayer player = Minecraft.getInstance().player;
        if(number == 1) {
            Vec3 motion = new Vec3(0, VPUtil.commonPower, 0);
            player.lerpMotion(motion.x, motion.y, motion.z);
        }
        else if(number == 2){
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
            player.onUpdateAbilities();
        }
        else if(number == -1){
            VPUtil.fall(player,-10);
        }
        else if(number == 69){
            BlockPos pos = new BlockPos((int) player.getPersistentData().getDouble("VPDevourerX"),(int)player.getPersistentData().getDouble("VPDevourerY"),(int)player.getPersistentData().getDouble("VPDevourerZ"));
            VPUtil.suckToPos(player,pos,3);
        }
        else if(number == 3){
            VPUtil.clearEffects(player,true);
        }
        else if(number == 4){
            VPUtil.clearEffects(player,false);
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 4));
        }
        else {
            Vec3 motion = new Vec3(0, number, 0);
            player.lerpMotion(motion.x, motion.y, motion.z);
        }
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {

        });
    }
}
