package com.pyding.vp.item;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ItemAnimationPacket;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Refresher extends Item {
    public Refresher(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        ItemStack stack = player.getItemInHand(p_41434_);
        List<ItemStack> stackList = VPUtil.getVestigeList(player);
        if(stackList.isEmpty())
            return super.use(level, player, p_41434_);
        for(ItemStack itemStack: stackList){
            if(itemStack.getItem() instanceof Vestige vestige)
                vestige.refresh(player);
        }
        if(player instanceof ServerPlayer serverPlayer)
            PacketHandler.sendToClient(new ItemAnimationPacket(stack),serverPlayer);
        stack.split(1);
        VPUtil.play(player, SoundEvents.TOTEM_USE);
        VPUtil.spawnParticles(player, ParticleTypes.GLOW, player.getX(), player.getY(), player.getZ(), 8, 0, 0, 0);
        return super.use(level, player, p_41434_);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("vp.refresher").withStyle(ChatFormatting.GRAY));
        components.add(Component.translatable("vp.refresher.get").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, components, flag);
    }
}
