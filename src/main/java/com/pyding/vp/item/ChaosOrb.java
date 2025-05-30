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
        ItemStack stack = player.getOffhandItem();
        if(VPUtil.isEnchantable(player.getOffhandItem()) && !player.getOffhandItem().getAllEnchantments().isEmpty()){
            Random random = new Random();
            ItemStack itemStack = player.getOffhandItem();
            List<Enchantment> cursedList = new ArrayList<>(ForgeRegistries.ENCHANTMENTS.getValues());
            cursedList.removeIf(enchantment -> !enchantment.isCurse());
            List<Enchantment> goodEnchant = new ArrayList<>(ForgeRegistries.ENCHANTMENTS.getValues());
            goodEnchant.removeIf(Enchantment::isCurse);
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
            for(Enchantment enchantment: new HashSet<>(enchantments.keySet())){
                if(enchantment.isCurse())
                    continue;
                Enchantment randomEnchant = goodEnchant.get(random.nextInt(goodEnchant.size()));
                int lvl = enchantments.get(enchantment);
                if(enchantments.containsKey(randomEnchant)){
                    lvl = lvl+enchantments.get(randomEnchant);
                    enchantments.remove(enchantment);
                    enchantments.remove(randomEnchant);
                    enchantments.put(randomEnchant, lvl);
                }
                else {
                    enchantments.remove(enchantment);
                    enchantments.put(randomEnchant, lvl);
                }
            }
            double negativeChance = 0.02 * (cursedList.size()+1);
            if(random.nextDouble() < negativeChance) {
                int count = 0;
                int numba = random.nextInt(enchantments.size());
                for(Enchantment enchantment: new HashSet<>(enchantments.keySet())){
                    if(count == numba){
                        int original = enchantments.get(enchantment);
                        if(original > 0)
                            original *= -1;
                        enchantments.remove(enchantment);
                        enchantments.put(enchantment, original);
                        break;
                    }
                    count++;
                }
            }
            double splitChance = 0.05;
            if(random.nextDouble() < VPUtil.getChance(splitChance,player)) {
                int lvl = 0;
                int count = 0;
                int numba = random.nextInt(enchantments.size());
                for(Enchantment enchantment: new HashSet<>(enchantments.keySet())){
                    if(count == numba){
                        lvl = enchantments.get(enchantment);
                        enchantments.remove(enchantment);
                        break;
                    }
                    count++;
                }
                while (lvl > 0){
                    count = 0;
                    numba = random.nextInt(enchantments.size());
                    for(Enchantment enchantment: new HashSet<>(enchantments.keySet())){
                        if(count == numba){
                            int original = enchantments.get(enchantment);
                            enchantments.remove(enchantment);
                            enchantments.put(enchantment, original+1);
                            lvl--;
                        }
                        count++;
                    }
                }
                while (lvl < 0){
                    count = 0;
                    numba = random.nextInt(enchantments.size());
                    for(Enchantment enchantment: new HashSet<>(enchantments.keySet())){
                        if(count == numba){
                            int original = enchantments.get(enchantment);
                            enchantments.remove(enchantment);
                            enchantments.put(enchantment, original-1);
                            lvl++;
                        }
                        count++;
                    }
                }
            }
            double bookChance = 0.005;
            if(random.nextDouble() < VPUtil.getChance(bookChance,player)) {
                int count = 0;
                int numba = random.nextInt(enchantments.size());
                for(Enchantment enchantment: new HashSet<>(enchantments.keySet())){
                    if(count == numba){
                        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
                        EnchantmentHelper.setEnchantments(Map.of(enchantment, enchantments.get(enchantment)), enchantedBook);
                        VPUtil.giveStack(enchantedBook,player);
                    }
                    count++;
                }
            }
            double bookEnchantChance = 0.001;
            if(random.nextDouble() < VPUtil.getChance(bookEnchantChance,player)) {
                for (ItemStack inventoryStack : player.getInventory().items) {
                    if (inventoryStack.getItem() instanceof EnchantedBookItem) {
                        Map<Enchantment, Integer> bookEnchantments = EnchantmentHelper.getEnchantments(inventoryStack);
                        for (Map.Entry<Enchantment, Integer> entry : bookEnchantments.entrySet()) {
                            Enchantment enchantment = entry.getKey();
                            int bookLevel = entry.getValue();
                            int lvl = enchantments.get(enchantment);
                            if(lvl > 0){
                                lvl = lvl+bookLevel;
                                enchantments.remove(enchantment);
                                enchantments.put(enchantment, lvl);
                            }
                            else {
                                enchantments.put(enchantment, bookLevel);
                            }
                        }
                        break;
                    }
                }
            }
            EnchantmentHelper.setEnchantments(enchantments, itemStack);
            player.getMainHandItem().split(1);
            player.getPersistentData().putBoolean("VPBlockHand",true);
        } else if(stack.getItem() instanceof Vestige vestige && VPUtil.getVestigeCurse(stack) != 0){
            if(VPUtil.getVestigeCurse(stack) == 6)
                Vestige.decreaseStars(stack);
            if(Math.random() < 0.05) {
                Vestige.increaseStars(stack,player);
                stack.getOrCreateTag().putInt("VPCursed",6);
            } else stack.getOrCreateTag().putInt("VPCursed",new Random().nextInt(Vestige.maxCurses)+1);
            player.getMainHandItem().split(1);
        } else if(stack.getItem() instanceof Accessory accessory){
            Random random = new Random();
            int lvl = accessory.getLvl(stack);
            if(new Random().nextDouble() < VPUtil.getChance(0.03,player)){
                stack.getOrCreateTag().putInt("VPType",random.nextInt(5)+1);
            }
            int type = stack.getOrCreateTag().getInt("VPType");
            stack.getOrCreateTag().putFloat("VPStat",0);
            for(int i = 0; i < lvl+1; i++){
                switch (type) {
                    case 1 -> stack.getOrCreateTag().putFloat("VPStat", (float) (VPUtil.getChance(random.nextDouble(),player)*3 + 1)+stack.getOrCreateTag().getFloat("VPStat"));
                    case 2 -> stack.getOrCreateTag().putFloat("VPStat", (float) (VPUtil.getChance(random.nextDouble(),player)*6 + 2)+stack.getOrCreateTag().getFloat("VPStat"));
                    case 3 -> stack.getOrCreateTag().putFloat("VPStat", (float) (VPUtil.getChance(random.nextDouble(),player)*15 + 5)+stack.getOrCreateTag().getFloat("VPStat"));
                    case 4 -> stack.getOrCreateTag().putFloat("VPStat", (float) (VPUtil.getChance(random.nextDouble(),player)*9 + 3)+stack.getOrCreateTag().getFloat("VPStat"));
                    case 5 -> stack.getOrCreateTag().putFloat("VPStat", (float) (VPUtil.getChance(random.nextDouble(),player)*12 + 4)+stack.getOrCreateTag().getFloat("VPStat"));
                    default -> {
                    }
                }
            }
            player.sendSystemMessage(Component.literal(""+stack.getOrCreateTag().getFloat("VPStat")));
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
        } else if(Screen.hasAltDown())
            components.add(Component.translatable("vp.chaos_orb.desc3").withStyle(ChatFormatting.GRAY));
        else {
            components.add(Component.translatable("vp.press").append(Component.literal("SHIFT").withStyle(ChatFormatting.YELLOW).append(Component.translatable("vp.shift"))));
            components.add(Component.translatable("vp.press").append(Component.literal("ALT").withStyle(ChatFormatting.YELLOW).append(Component.translatable("vp.shift"))));
        }
        super.appendHoverText(stack, level, components, flag);
    }
}
