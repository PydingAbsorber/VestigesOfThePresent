package com.pyding.vp.item;

import com.pyding.vp.util.ConfigHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeartyPearl extends Item {
    public HeartyPearl(Properties p_41383_) {
        super(p_41383_);
    }

    public HeartyPearl() {
        super(new Properties().stacksTo(64));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player player, InteractionHand p_41434_) {
        ItemStack stack = player.getItemInHand(p_41434_);

        return super.use(p_41432_, player, p_41434_);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            components.add(Component.translatable("vp.hearty_pearl.use").withStyle(ChatFormatting.GRAY));
        }
        else if (Screen.hasControlDown()){
            components.add(Component.translatable("vp.hearty_pearl.obtain", ConfigHandler.COMMON.eatingMinutes.get()).withStyle(ChatFormatting.GRAY));
        }
        else {
            components.add(Component.translatable("vp.press").append(Component.literal("SHIFT").withStyle(ChatFormatting.BLUE).append(Component.translatable("vp.shift"))));
            components.add(Component.translatable("vp.press").append(Component.literal("CTRL").withStyle(ChatFormatting.BLUE).append(Component.translatable("vp.ctrl"))));
        }
    }
}
