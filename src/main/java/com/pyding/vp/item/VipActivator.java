package com.pyding.vp.item;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ItemAnimationPacket;
import com.pyding.vp.util.GradientUtil;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.*;

public class VipActivator extends Item {
    public VipActivator(Properties p_41383_) {
        super(p_41383_);
    }

    long day = 24*60*60*1000;

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        ItemStack stack = player.getItemInHand(p_41434_);
        if(stack.getOrCreateTag().hasUUID("VPPlayer") && stack.getOrCreateTag().getUUID("VPPlayer").compareTo(player.getUUID()) != 0){
            player.sendSystemMessage(Component.literal("It's not yours."));
            return super.use(level, player, p_41434_);
        }
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            cap.setVip((day*stack.getOrCreateTag().getInt("VPDays"))+System.currentTimeMillis());
            cap.sync(player);
        });
        if(player instanceof ServerPlayer serverPlayer)
            PacketHandler.sendToClient(new ItemAnimationPacket(stack),serverPlayer);
        stack.split(1);
        VPUtil.play(player, SoundEvents.TOTEM_USE);
        VPUtil.play(player, SoundRegistry.SUCCESS.get());
        VPUtil.spawnParticles(player, ParticleTypes.GLOW, player.getX(), player.getY(), player.getZ(), 8, 0, 0, 0);
        return super.use(level, player, p_41434_);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level p_41405_, Entity entity, int p_41407_, boolean p_41408_) {
        if(entity instanceof Player player && !stack.getOrCreateTag().getBoolean("VPTrade") && !player.isCreative() && !stack.getOrCreateTag().hasUUID("VPPlayer"))
            stack.getOrCreateTag().putUUID("VPPlayer",player.getUUID());
        super.inventoryTick(stack, p_41405_, entity, p_41407_, p_41408_);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        stack.setHoverName(GradientUtil.goldenGradient(stack.getHoverName().getString()));
        components.add(GradientUtil.goldenGradient(Component.translatable("item.vip.desc1",stack.getOrCreateTag().getInt("VPDays")).getString()));
        components.add(GradientUtil.goldenGradient(Component.translatable("item.vip.desc2").getString()).copy().append(GradientUtil.stellarGradient("Stellar")));
        components.add(GradientUtil.goldenGradient(Component.translatable("item.vip.desc2.2").getString()));
        if(stack.getOrCreateTag().hasUUID("VPPlayer"))
            components.add(Component.translatable("item.vip.desc3").withStyle(ChatFormatting.DARK_RED));
        super.appendHoverText(stack, level, components, flag);
    }

    public static class BackupData {
        public List<ItemStack> main;
        public List<ItemStack> armor;
        public List<ItemStack> offhand;
        public int exp;
        public int expPoints;
    }

    private static final Map<UUID, BackupData> backups = new HashMap<>();

    public static void saveInventory(Player player) {
        BackupData data = new BackupData();

        data.main = new ArrayList<>();
        for (ItemStack stack : player.getInventory().items) {
            data.main.add(stack.copy());
        }
        data.armor = new ArrayList<>();
        for (ItemStack stack : player.getInventory().armor) {
            data.armor.add(stack.copy());
        }
        data.offhand = new ArrayList<>();
        for (ItemStack stack : player.getInventory().offhand) {
            data.offhand.add(stack.copy());
        }
        if(player instanceof ServerPlayer serverPlayer) {
            data.exp = serverPlayer.experienceLevel;
            data.expPoints = (int)(serverPlayer.experienceProgress*serverPlayer.getXpNeededForNextLevel());
        }
        backups.put(player.getUUID(), data);
    }

    public static void loadInventory(Player oldPlayer, Player newPlayer){
        BackupData data = backups.get(oldPlayer.getUUID());
        if (data != null) {
            newPlayer.getInventory().items.clear();
            for (int i = 0; i < data.main.size(); i++) {
                if (i < newPlayer.getInventory().items.size()) {
                    newPlayer.getInventory().items.set(i, data.main.get(i));
                }
            }
            newPlayer.getInventory().armor.clear();
            for (int i = 0; i < data.armor.size(); i++) {
                if (i < newPlayer.getInventory().armor.size()) {
                    newPlayer.getInventory().armor.set(i, data.armor.get(i));
                }
            }
            newPlayer.getInventory().offhand.clear();
            for (int i = 0; i < data.offhand.size(); i++) {
                if (i < newPlayer.getInventory().offhand.size()) {
                    newPlayer.getInventory().offhand.set(i, data.offhand.get(i));
                }
            }
            if(newPlayer instanceof ServerPlayer serverPlayer){
                serverPlayer.setExperienceLevels(data.exp);
                serverPlayer.setExperiencePoints(data.expPoints);
            }
            backups.remove(oldPlayer.getUUID());
        }
    }
}
