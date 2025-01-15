package com.pyding.vp.item;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ChaosOrb extends Item{
    public ChaosOrb() {
        super(new Properties().stacksTo(64));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(VPUtil.isEnchantable(player.getOffhandItem()) && player.getOffhandItem().getAllEnchantments() != null){
            Random random = new Random();
            ItemStack itemStack = player.getOffhandItem();
            List<Enchantment> cursedList = new ArrayList<>(ForgeRegistries.ENCHANTMENTS.getValues());
            cursedList.removeIf(enchantment -> !enchantment.isCurse());
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
            List<Enchantment> goodEnchant = new ArrayList<>(ForgeRegistries.ENCHANTMENTS.getValues());
            goodEnchant.removeIf(Enchantment::isCurse);
            for(Enchantment enchantment: enchantments.keySet()){
                Enchantment randomEnchant = goodEnchant.get(random.nextInt(goodEnchant.size()-1));
                int lvl = enchantments.get(enchantment);
                if(randomEnchant.getDescriptionId().equals(randomEnchant.getDescriptionId())){
                    lvl = lvl*2;
                }
                enchantments.remove(enchantment);
                enchantments.put(randomEnchant,lvl);
            }
            double negativeChance = 0.05 * cursedList.size()+1;
            if(random.nextDouble() < negativeChance) {
                int count = 0;
                int numba = random.nextInt(enchantments.size()-1);
                for(Enchantment enchantment: enchantments.keySet()){
                    count++;
                    if(count == numba){
                        int original = enchantments.get(enchantment);
                        if(original > 0)
                            original *= -1;
                        enchantments.remove(enchantment);
                        enchantments.put(enchantment, original);
                        break;
                    }
                }
            }
            double splitChance = 0.03;
            if(random.nextDouble() < VPUtil.getChance(splitChance,player)) {
                int lvl = 0;
                int count = 0;
                int numba = random.nextInt(enchantments.size()-1);
                for(Enchantment enchantment: enchantments.keySet()){
                    count++;
                    if(count == numba){
                        lvl = enchantments.get(enchantment);
                        enchantments.remove(enchantment);
                    }
                }
                while (lvl > 0){
                    count = 0;
                    numba = random.nextInt(enchantments.size()-1);
                    for(Enchantment enchantment: enchantments.keySet()){
                        count++;
                        if(count == numba){
                            int original = enchantments.get(enchantment);
                            enchantments.remove(enchantment);
                            enchantments.put(enchantment, original+1);
                            lvl--;
                            break;
                        }
                    }
                }
            }
            double bookChance = 0.01;
            if(random.nextDouble() < VPUtil.getChance(bookChance,player)) {
                int count = 0;
                int numba = random.nextInt(enchantments.size()-1);
                for(Enchantment enchantment: enchantments.keySet()){
                    count++;
                    if(count == numba){
                        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
                        EnchantmentHelper.setEnchantments(Map.of(enchantment, enchantments.get(enchantment)), enchantedBook);
                        VPUtil.giveStack(enchantedBook,player);
                        enchantments.clear();
                    }
                }
            }
            EnchantmentHelper.setEnchantments(enchantments, itemStack);
            player.getMainHandItem().split(1);
        }
        return super.use(level, player, hand);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("vp.chaos_orb.desc1").withStyle(ChatFormatting.GRAY));
            components.add(Component.translatable("vp.chaos_orb.desc2").withStyle(ChatFormatting.GRAY));
        }
        else {
            components.add(Component.translatable("vp.press").append(Component.literal("SHIFT").withStyle(ChatFormatting.YELLOW).append(Component.translatable("vp.shift"))));
        }
        super.appendHoverText(stack, level, components, flag);
    }
}
