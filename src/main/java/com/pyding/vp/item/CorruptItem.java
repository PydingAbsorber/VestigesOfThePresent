package com.pyding.vp.item;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.capability.PlayerCapabilityVP;
import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CoralBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CorruptItem extends Item{
    public CorruptItem() {
        super(new Item.Properties().stacksTo(64));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!player.getOffhandItem().getOrCreateTag().getBoolean("VPCursed")){
            if(VPUtil.isEnchantable(player.getOffhandItem()) && !level.isClientSide()){
                int modifier = 2;
                if(VPUtil.hasVestige(ModItems.BOOK.get(),player))
                    modifier *= 2;
                ItemStack itemStack = player.getOffhandItem();
                List<Enchantment> list = new ArrayList<>(ForgeRegistries.ENCHANTMENTS.getValues());
                list.removeIf(enchantment -> !enchantment.isCurse());
                Enchantment curse = list.get(new Random().nextInt(list.size()));
                itemStack.enchant(curse,curse.getMaxLevel()*modifier);
                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                    cap.setChallenge(7,player);
                });
                list = new ArrayList<>(ForgeRegistries.ENCHANTMENTS.getValues());
                list.removeIf(Enchantment::isCurse);
                int attempts = 0;
                while (attempts < 5) {
                    int random = new Random().nextInt(list.size());
                    Enchantment enchantment = list.get(random);
                    int original = itemStack.getEnchantmentLevel(enchantment);
                    if(original > 0) {
                        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
                        enchantments.remove(enchantment);
                        EnchantmentHelper.setEnchantments(enchantments, itemStack);
                    }
                    itemStack.enchant(enchantment, enchantment.getMaxLevel() * modifier + original);
                    attempts++;
                }
                itemStack.getOrCreateTag().putBoolean("VPCursed",true);
                player.getMainHandItem().split(1);
                player.getPersistentData().putBoolean("VPBlockHand",true);
            }
            if(player.getOffhandItem().getItem() instanceof Vestige){
                if(VPUtil.curseVestige(player.getOffhandItem(),new Random().nextInt(Vestige.maxCurses)+1,player))
                    player.getMainHandItem().split(1);
            }
        }
        return super.use(level, player, hand);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("vp.corrupt_item.desc1").withStyle(ChatFormatting.GRAY));
            components.add(Component.translatable("vp.corrupt_item.desc2").withStyle(ChatFormatting.GRAY));
        }
        else {
            components.add(Component.translatable("vp.press").append(Component.literal("SHIFT").withStyle(ChatFormatting.RED).append(Component.translatable("vp.shift"))));
        }
        super.appendHoverText(stack, level, components, flag);
    }
}
