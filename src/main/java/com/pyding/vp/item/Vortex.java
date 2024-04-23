package com.pyding.vp.item;

import com.pyding.vp.entity.ModEntities;
import com.pyding.vp.entity.VortexEntity;
import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ItemAnimationPacket;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
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

public class Vortex extends Item {
    public Vortex(Properties p_41383_) {
        super(p_41383_);
    }

    public Vortex() {
        super(new Item.Properties().stacksTo(64));
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        if(player.getY() > 0) {
            item.shrink(1);
            VortexEntity vortexEntity = new VortexEntity(player.getCommandSenderWorld(),player);
            vortexEntity.setPos(player.getX(),player.getY()-1,player.getZ());
            player.getCommandSenderWorld().addFreshEntity(vortexEntity);
            return false;
        }
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        Player player = Minecraft.getInstance().player;
        components.add(Component.translatable("vp.vortex").withStyle(ChatFormatting.GRAY));
        //components.add(Component.translatable("vp.vortex.max").withStyle(ChatFormatting.GRAY).append(Component.literal(" " + VPUtil.vortexItems().size())));
        components.add(Component.translatable("vp.vortex.shift").withStyle(ChatFormatting.GRAY));
        if(Screen.hasShiftDown())
            components.add(Component.literal(player.getPersistentData().getString("VPVortex")));
        super.appendHoverText(stack, level, components, flag);
    }
}
