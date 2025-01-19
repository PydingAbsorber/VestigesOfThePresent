package com.pyding.vp.item;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.item.vestiges.SoulBlighter;
import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CelestialMirror extends Item{
    public CelestialMirror() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(player.getCommandSenderWorld().isClientSide() || hand == InteractionHand.OFF_HAND)
            return super.use(level, player, hand);
        if(!player.getOffhandItem().getOrCreateTag().getBoolean("VPMirrored") && !dupyPoPopochkam(player.getOffhandItem(),player)){
            ItemStack itemStack = player.getOffhandItem();
            itemStack.getOrCreateTag().putBoolean("VPMirrored",true);
            ItemStack clone = itemStack.copy();
            VPUtil.giveStack(clone,player);
            player.getMainHandItem().split(1);
        }
        return super.use(level, player, hand);
    }

    public static boolean dupyPoPopochkam(ItemStack stack,Player player){
        if (stack.getItem() instanceof CelestialMirror || (stack.getItem() instanceof SoulBlighter && stack.getOrCreateTag().contains("entityData"))){
            player.sendSystemMessage(Component.literal("Oh you thought you smart?"));
            return true;
        }
        for(String neDupy: ConfigHandler.COMMON.cloneWhiteList.get().toString().split(",")){
            if(stack.getDescriptionId().contains(neDupy)) {
                return false;
            }
        }
        for(String dupy: ConfigHandler.COMMON.cloneBlackList.get().toString().split(",")){
            if(stack.getDescriptionId().contains(dupy)) {
                player.sendSystemMessage(Component.literal("This item is in black list because it may contain dupes. You can change it in config."));
                return true;
            }
        }
        if(hasContainersOrInventory(stack.getClass())) {
            player.sendSystemMessage(Component.literal("This item cannot be cloned because it has containers or inventory. If it's not please write an issue on GitHub."));
            return true;
        }
        if(!stack.getItem().canFitInsideContainerItems()) {
            player.sendSystemMessage(Component.literal("Nah bro, this item is definitely has smth dirty. I wont even allow you to configure it."));
            return true;
        }
        return false;
    }

    public static boolean hasContainersOrInventory(Class clazz){
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName().toLowerCase();
            if (fieldName.contains("store") || (fieldName.contains("container") && !fieldName.contains("canfitinsidecontaineritems")) || (fieldName.contains("inventory") && !fieldName.equals("inventorytick"))) {
                return true;
            }
        }
        for (Method method : clazz.getDeclaredMethods()) {
            String methodName = method.getName().toLowerCase();
            if ((methodName.contains("container") && !methodName.contains("canfitinsidecontaineritems")) || (methodName.contains("inventory") && !methodName.equals("inventorytick") && !methodName.contains("inventoryslot"))) {
                return true;
            }

            for (Parameter parameter : method.getParameters()) {
                String paramName = parameter.getName().toLowerCase();
                if (paramName.contains("container") || (paramName.contains("inventory") && !paramName.contains("inventoryslot"))) {
                    return true;
                }
            }
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("vp.celestial_mirror.desc1").withStyle(ChatFormatting.GRAY));
            components.add(Component.translatable("vp.celestial_mirror.desc2").withStyle(ChatFormatting.GRAY));
        }
        else {
            components.add(Component.translatable("vp.press").append(Component.literal("SHIFT").withStyle(ChatFormatting.DARK_AQUA).append(Component.translatable("vp.shift"))));
        }
        super.appendHoverText(stack, level, components, flag);
    }
}
