package com.pyding.vp.item;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.item.accessories.Accessory;
import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ChaosOrb extends Item{
    public ChaosOrb() {
        super(new Properties().stacksTo(64));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(player.getCommandSenderWorld().isClientSide() || hand == InteractionHand.OFF_HAND)
            return super.use(level, player, hand);
        VPUtil.useOrb(player.getOffhandItem(),player.getMainHandItem(),player);
        return super.use(level, player, hand);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("vp.chaos_orb.desc1").withStyle(ChatFormatting.GRAY));
            components.add(Component.translatable("vp.chaos_orb.desc2").withStyle(ChatFormatting.GRAY));
        } else if(Screen.hasAltDown())
            components.add(Component.translatable("vp.chaos_orb.desc3").withStyle(ChatFormatting.GRAY));
        else {
            components.add(Component.translatable("vp.press").append(Component.literal("SHIFT").withStyle(ChatFormatting.YELLOW).append(Component.translatable("vp.shift"))));
            components.add(Component.translatable("vp.press").append(Component.literal("ALT").withStyle(ChatFormatting.YELLOW).append(Component.translatable("vp.shift"))));
        }
        super.appendHoverText(stack, level, components, flag);
    }
}
