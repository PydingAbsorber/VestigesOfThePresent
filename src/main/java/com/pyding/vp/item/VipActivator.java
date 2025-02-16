package com.pyding.vp.item;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ItemAnimationPacket;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VipActivator extends Item {
    public VipActivator(Properties p_41383_) {
        super(p_41383_);
    }

    long day = System.currentTimeMillis()+24*360000;

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        ItemStack stack = player.getItemInHand(p_41434_);
        if(stack.getOrCreateTag().hasUUID("VPPlayer") && stack.getOrCreateTag().getUUID("VPPlayer").compareTo(player.getUUID()) != 0){
            player.sendSystemMessage(Component.literal("It's not yours."));
            return super.use(level, player, p_41434_);
        }
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            cap.setVip(day*stack.getOrCreateTag().getInt("VPDays"));
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
        components.add(Component.translatable("item.vip.desc1",stack.getOrCreateTag().getInt("VPDays")).withStyle(ChatFormatting.GOLD));
        components.add(Component.translatable("item.vip.desc2").withStyle(ChatFormatting.GOLD));
        if(stack.getOrCreateTag().hasUUID("VPPlayer"))
            components.add(Component.translatable("item.vip.desc3").withStyle(ChatFormatting.DARK_RED));
        super.appendHoverText(stack, level, components, flag);
    }
}
