package com.pyding.vp.network.packets;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.client.LeaderboardScreen;
import com.pyding.vp.client.NightmareScreen;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.ClientConfig;
import com.pyding.vp.util.LeaderboardUtil;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.Set;
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

    public static void handle(PlayerFlyPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            handle2(msg.number);
        });

        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle2(int number) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null)
            return;
        if(number == 1) {
            Vec3 motion = new Vec3(0, 3, 0);
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
        else if(number == 5){
            player.addEffect(new MobEffectInstance(VPUtil.getRandomEffect(false), 60 * 20));
        }
        else if(number == 6){
            player.getAbilities().mayfly = true;
            player.getAbilities().flying = true;
            player.onUpdateAbilities();
        }
        else if(number == 7){
            LeaderboardUtil.refreshTopPlayers();
        }
        else if(number == 8){
            if (Minecraft.getInstance().screen != null){
                Minecraft.getInstance().screen.onClose();
            }
            VPUtil.antiResurrect(player,System.currentTimeMillis()+VPUtil.deathTime);
            VPUtil.setRoflanEbalo(player,System.currentTimeMillis()+VPUtil.deathTime);
            VPUtil.setHealth(player,0);
            player.die(player.damageSources().genericKill());
            VPUtil.despawn(player);
            Minecraft.getInstance().forceSetScreen(new DeathScreen(Component.literal("Death by Paragon Damage"),false));
        }
        else if(number == 9){
            VPUtil.roflan.put(player.getUUID(),0l);
        }
        else if(number == 10){
            VPUtil.antiResurrect(player,-1);
        }
        else if(number == 11){
            VPUtil.antiTp(player,-1);
        }
        else if(number == 12){
            player.setShowDeathScreen(true);
        }
        else if(number == 13){
            ClientConfig.COMMON.renderSoulIntegrity.set(!ClientConfig.COMMON.renderSoulIntegrity.get());
        }
        else if(number == 14){
            Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new LeaderboardScreen()));
            //player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.BOOK_OPEN.get(), SoundSource.MASTER, 1, 1, false);
        }
        else if(number == 278){
            if(Minecraft.getInstance().level != null) {
                Set<ResourceKey<Biome>> biomes = Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.BIOME).registryKeySet();
                VPUtil.biomeNames.addAll(biomes);
            }
        }
        else if(number > 300){
            BlockPos pos = new BlockPos((int) player.getPersistentData().getDouble("VPDevourerX"),(int)player.getPersistentData().getDouble("VPDevourerY"),(int)player.getPersistentData().getDouble("VPDevourerZ"));
            VPUtil.suckToPos(player,pos,number-300);
        }
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {

        });
    }
}
