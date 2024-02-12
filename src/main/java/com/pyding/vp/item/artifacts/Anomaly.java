package com.pyding.vp.item.artifacts;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

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

    public boolean fuckNbt1 = false;
    public boolean fuckNbt2 = false;
    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        if(player.getMainHandItem().getItem() instanceof EnderEyeItem){
            fuckNbt1 = true;
            fuckNbt2 = true;
            ICuriosHelper api = CuriosApi.getCuriosHelper();
            List list = api.findCurios(player, (stackInSlot) -> {
                if(stackInSlot.getItem() instanceof Anomaly) {
                    stackInSlot.getOrCreateTag().putDouble("VPReturnX", player.getX());
                    stackInSlot.getOrCreateTag().putDouble("VPReturnY", player.getY());
                    stackInSlot.getOrCreateTag().putDouble("VPReturnZ", player.getZ());
                    stackInSlot.getOrCreateTag().putString("VPReturnKey", player.level.dimension().location().getPath());
                    return true;
                }
                return false;
            });
        } else {
            for(LivingEntity entity: VPUtil.ray(player,3,60,true)){
                if(player instanceof ServerPlayer serverPlayer){
                    serverPlayer.teleportTo(entity.getX()-1,entity.getY(),entity.getZ()-1);
                    VPUtil.dealDamage(entity,player,DamageSource.playerAttack(player).bypassMagic().bypassArmor(),400);
                    entity.getPersistentData().putLong("VPAntiTP",seconds+System.currentTimeMillis());
                }
            }
        }
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        if(player instanceof ServerPlayer serverPlayer){
            serverPlayer.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                if(isStellar && Math.random() < 0.05){
                    for(ServerPlayer victim: serverPlayer.level.getServer().getPlayerList().getPlayers()){
                        if(victim != serverPlayer){
                            serverPlayer.teleportTo(victim.getLevel(),victim.getX(),victim.getY(),victim.getZ(),0,0);
                            break;
                        }
                    }
                }
                else {
                    String key = cap.getRandomDimension();
                    ServerLevel serverLevel = serverPlayer.level.getServer().getLevel(VPUtil.getWorldKey(key));
                    if (serverLevel == null) {
                        serverLevel = serverPlayer.level.getServer().getLevel(Level.OVERWORLD);
                        cap.removeDimension(key);
                    }
                    Random random = new Random();
                    double x;
                    double z;
                    if (Math.random() < 0.5) {
                        x = random.nextInt((int) level.getWorldBorder().getMaxX());
                    } else {
                        x = random.nextInt((int) level.getWorldBorder().getMinX() * -1);
                        x *= -1;
                    }
                    if (Math.random() < 0.5) {
                        z = random.nextInt((int) level.getWorldBorder().getMaxZ());
                    } else {
                        z = random.nextInt((int) level.getWorldBorder().getMinX() * -1);
                        z *= -1;
                    }
                    double y = random.nextInt(260);
                    serverPlayer.teleportTo(serverLevel, x, y, z, 0, 0);
                }
            });
        }
        super.doUltimate(seconds, player, level);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if(!fuckNbt1) {
            super.onUnequip(slotContext, newStack, stack);
        }
        else fuckNbt1 = false;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if(!fuckNbt2) {
            super.onEquip(slotContext, prevStack, stack);
        }
        else fuckNbt2 = false;
    }
}