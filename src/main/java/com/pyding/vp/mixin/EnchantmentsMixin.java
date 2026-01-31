package com.pyding.vp.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EnchantmentHelper.class)
public class EnchantmentsMixin {

    @Inject(method = "getItemEnchantmentLevel", at = @At("RETURN"), cancellable = true)
    private static void onGetItemEnchantmentLevel(Holder<Enchantment> enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);
        if (enchantments != null) {
            int level = enchantments.getLevel(enchantment);
            if (level != 0) {
                cir.setReturnValue(level);
            }
        }
    }

    @Inject(method = "getEnchantmentLevel(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/LivingEntity;)I", at = @At("HEAD"), cancellable = true)
    private static void onGetEntityEnchantmentLevel(Holder<Enchantment> enchantment, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        Iterable<ItemStack> items = enchantment.value().getSlotItems(entity).values();
        int resultLevel = 0;
        boolean found = false;
        for (ItemStack stack : items) {
            int currentLvl = EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
            if (currentLvl != 0) {
                if (!found || Math.abs(currentLvl) > Math.abs(resultLevel)) {
                    resultLevel = currentLvl;
                    found = true;
                }
            }
        }
        if (found) {
            cir.setReturnValue(resultLevel);
        }
    }
}
