package com.pyding.vp.item.artifacts;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Anomaly extends Vestige{
    public Anomaly(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(10, ChatFormatting.LIGHT_PURPLE, 2, 60, 1, 360, 30, 1, true);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.TELEPORT1.get());
        if(player.getMainHandItem().getItem() instanceof EnderEyeItem){
            fuckNbt();
            ItemStack stackInSlot = VPUtil.getVestigeStack(this,player);
            stackInSlot.getOrCreateTag().putDouble("VPReturnX", player.getX());
            stackInSlot.getOrCreateTag().putDouble("VPReturnY", player.getY());
            stackInSlot.getOrCreateTag().putDouble("VPReturnZ", player.getZ());
            stackInSlot.getOrCreateTag().putString("VPReturnKey", player.getCommandSenderWorld().dimension().location().getPath());
            stackInSlot.getOrCreateTag().putString("VPReturnDir", player.getCommandSenderWorld().dimension().location().getNamespace());
        } else {
            for(LivingEntity entity: VPUtil.ray(player,3,60,true)){
                if(player instanceof ServerPlayer serverPlayer){
                    serverPlayer.teleportTo(entity.getX()-1,entity.getY(),entity.getZ()-1);
                    VPUtil.dealDamage(entity,player,player.damageSources().dragonBreath(),400,2);
                    entity.getPersistentData().putLong("VPAntiTP",seconds+System.currentTimeMillis());
                }
            }
        }
        VPUtil.spawnParticles(player, ParticleTypes.PORTAL,3,1,0,-0.1,0,1,false);
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        if(player instanceof ServerPlayer serverPlayer){
            serverPlayer.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                if(isStellar && (Math.random() < 0.05)){
                    int counter = 0;
                    for(ServerPlayer victim: serverPlayer.getCommandSenderWorld().getServer().getPlayerList().getPlayers()){
                        if(victim != serverPlayer){
                            counter++;
                            serverPlayer.teleportTo((ServerLevel) victim.getCommandSenderWorld(),victim.getX(),victim.getY(),victim.getZ(),0,0);
                            break;
                        }
                    }
                    if(counter <= 1)
                        player.sendSystemMessage(Component.literal("There are no other players!"));
                    VPUtil.play(player, SoundRegistry.TELEPORT2.get());
                }
                else {
                    List<String> list = new ArrayList<>(cap.getRandomDimension());
                    if(list.isEmpty()) {
                        player.sendSystemMessage(Component.literal("Somehow world list is empty?!?!?!?!?!?!"));
                        return;
                    }
                    String key = list.get(0);
                    String path = list.get(1);
                    ServerLevel serverLevel = serverPlayer.getCommandSenderWorld().getServer().getLevel(VPUtil.getWorldKey(path, key));
                    if (serverLevel == null) {
                        serverLevel = serverPlayer.getCommandSenderWorld().getServer().getLevel(Level.OVERWORLD);
                    }
                    Random random = new Random();
                    double x;
                    double z;
                    double borderX;
                    double borderZ;
                    if(ConfigHandler.COMMON.anomalyBorder.get() == 0){
                        borderX = level.getWorldBorder().getMaxX();
                        borderZ = random.nextInt((int) level.getWorldBorder().getMaxZ());
                    } else {
                        borderX = ConfigHandler.COMMON.anomalyBorder.get();
                        borderZ = borderX;
                    }
                    if (Math.random() < 0.5) {
                        x = random.nextInt((int) borderX);
                    } else {
                        x = random.nextInt((int) borderX);
                        x *= -1;
                    }
                    if (Math.random() < 0.5) {
                        z = borderZ;
                    } else {
                        z = random.nextInt((int) borderZ);
                        z *= -1;
                    }
                    double y = random.nextInt(220);
                    serverPlayer.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,5*20));
                    serverPlayer.teleportTo(serverLevel, x, y, z, 0, 0);
                    VPUtil.teleportRandomly(serverPlayer,50);
                    VPUtil.spawnParticles(player, ParticleTypes.PORTAL,8,1,0,-0.1,0,1,false);
                    VPUtil.play(player, SoundRegistry.TELEPORT2.get());
                }
            });
        }
        super.doUltimate(seconds, player, level);
    }
}
