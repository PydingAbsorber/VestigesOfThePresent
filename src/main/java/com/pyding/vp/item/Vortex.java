package com.pyding.vp.item;

import com.pyding.vp.entity.VortexEntity;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Vortex extends Item {
    public Vortex(Properties p_41383_) {
        super(p_41383_);
    }

    public Vortex() {
        super(new Item.Properties().stacksTo(64).tab(ModCreativeModTab.tab));
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        if(player.getY() <= -69) {
            item.shrink(1);
            VortexEntity vortexEntity = new VortexEntity(player.getCommandSenderWorld(),player);
            vortexEntity.setPos(player.getX(),player.getY()-1,player.getZ());
            player.getCommandSenderWorld().addFreshEntity(vortexEntity);
            return false;
        }
        return super.onDroppedByPlayer(item, player);
    }
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(level != null && level.isClientSide) {
            components.add(Component.translatable("vp.vortex").withStyle(ChatFormatting.GRAY));
            components.add(Component.translatable("vp.vortex.shift").withStyle(ChatFormatting.GRAY));
            if (Screen.hasShiftDown()) {
                Player player = Minecraft.getInstance().player;
                if(player == null)
                    return;
                components.add(VPUtil.filterAndTranslate(player.getPersistentData().getString("VPVortex"),ChatFormatting.GRAY));
            }
        }
        super.appendHoverText(stack, level, components, flag);
    }
}
