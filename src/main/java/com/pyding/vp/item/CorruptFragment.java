package com.pyding.vp.item;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CorruptFragment extends Item{
    public CorruptFragment() {
        super(new Item.Properties().stacksTo(64));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level p_41405_, Entity entity, int p_41407_, boolean p_41408_) {
        super.inventoryTick(stack, p_41405_, entity, p_41407_, p_41408_);
        if(stack.getCount() >= 64 && entity instanceof Player player){
            stack.setCount(0);
            player.addItem(new ItemStack(ModItems.CORRUPT_ITEM.get()));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(VPUtil.getCurseAmount(player.getOffhandItem()) > 0){
            return super.use(level, player, hand);
        }
        if(VPUtil.isEnchantable(player.getOffhandItem())){
            ItemStack itemStack = player.getOffhandItem();
            Random random = new Random();
            if(random.nextDouble() < VPUtil.getChance(0.8,player)){
                List<Enchantment> list = new ArrayList<>(ForgeRegistries.ENCHANTMENTS.getValues());
                list.removeIf(Enchantment::isCurse);
                Enchantment enchantment = list.get(random.nextInt(list.size()));
                if (itemStack.getEnchantmentLevel(enchantment) >= enchantment.getMaxLevel())
                    return super.use(level, player, hand);
                int lvl = itemStack.getEnchantmentLevel(enchantment);
                if (lvl > 0) {
                    Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
                    enchantments.remove(enchantment);
                    EnchantmentHelper.setEnchantments(enchantments, itemStack);
                    if (lvl + 1 > enchantment.getMaxLevel())
                        lvl--;
                }
                itemStack.enchant(enchantment, 1 + lvl);
            } else {
                List<Enchantment> list = new ArrayList<>(ForgeRegistries.ENCHANTMENTS.getValues());
                list.removeIf(enchantment -> !enchantment.isCurse());
                Enchantment curse = list.get(new Random().nextInt(list.size()-1));
                itemStack.enchant(curse,curse.getMaxLevel());
                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                    cap.setChallenge(7,player);
                });
            }
            player.getMainHandItem().split(1);
        }
        return super.use(level, player, hand);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("vp.corrupt_frag.desc").withStyle(ChatFormatting.GRAY));
            components.add(Component.translatable("vp.corrupt_frag.use").withStyle(ChatFormatting.GRAY));
        }
        else if(Screen.hasControlDown()){
            components.add(Component.translatable("vp.corrupt_frag.obtain").withStyle(ChatFormatting.GRAY));
        }
        else {
            components.add(Component.translatable("vp.press").append(Component.literal("SHIFT").withStyle(ChatFormatting.DARK_RED).append(Component.translatable("vp.shift"))));
            components.add(Component.translatable("vp.press").append(Component.literal("CTRL").withStyle(ChatFormatting.DARK_RED).append(Component.translatable("vp.ctrl"))));
        }
        super.appendHoverText(stack, level, components, flag);
    }
}
