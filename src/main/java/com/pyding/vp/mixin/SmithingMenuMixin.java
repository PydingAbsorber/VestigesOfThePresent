package com.pyding.vp.mixin;


import com.pyding.vp.item.StellarFragment;
import com.pyding.vp.item.accessories.Accessory;
import com.pyding.vp.util.VPUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.SmithingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;
import java.util.UUID;

@Mixin(value = SmithingMenu.class)
public abstract class SmithingMenuMixin {

    @Inject(method = "onTake",at = @At("HEAD"),cancellable = true, require = 1)
    protected void onTakeMixin(Player player, ItemStack stack, CallbackInfo ci){
        if(((SmithingVzlomMixing)this).getInputSlots().getItem(0).getItem() instanceof SmithingTemplateItem smithingTemplateItem &&
                VPUtil.getSmithingDescription(smithingTemplateItem).getContents() instanceof TranslatableContents translatableContents) {
            VPUtil.getCap(player).addTemplate(translatableContents.getKey(), player);
        }
        if(((SmithingVzlomMixing)this).getInputSlots().getItem(0).getItem() instanceof Accessory accessory) {
            stack.set(DataComponents.CUSTOM_DATA, ((SmithingVzlomMixing)this).getInputSlots().getItem(2).get(DataComponents.CUSTOM_DATA));
            accessory.lvlUp(stack, player);
        }
    }

    @Inject(method = "createInputSlotDefinitions", at = @At("RETURN"), cancellable = true)
    protected void createInputSlotDefinitionsMix(CallbackInfoReturnable<ItemCombinerMenuSlotDefinition> cir) {
        cir.setReturnValue(ItemCombinerMenuSlotDefinition.create().withSlot(0, 8, 48, (p_266643_) -> {
            return ((SmitingMenuVzlom)this).getRecipes().stream().anyMatch((p_266642_) -> {
                if(p_266643_.getItem() instanceof Accessory)
                    return true;
                return p_266642_.value().isTemplateIngredient(p_266643_);
            });
        }).withSlot(1, 26, 48, (p_286208_) -> {
            return ((SmitingMenuVzlom)this).getRecipes().stream().anyMatch((p_286206_) -> {
                if(p_286208_.getItem() instanceof StellarFragment)
                    return true;
                return p_286206_.value().isBaseIngredient(p_286208_);
            });
        }).withSlot(2, 44, 48, (p_286207_) -> {
            return ((SmitingMenuVzlom)this).getRecipes().stream().anyMatch((p_286204_) -> {
                if(p_286207_.getItem() instanceof Accessory)
                    return true;
                return p_286204_.value().isAdditionIngredient(p_286207_);
            });
        }).withResultSlot(3, 98, 48).build());
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void createCustomAccessoryResult(CallbackInfo ci) {
        SmithingMenu menu = (SmithingMenu)(Object)this;
        Container inputs = ((SmithingVzlomMixing)menu).getInputSlots();
        ItemStack slot0 = inputs.getItem(0);
        ItemStack slot1 = inputs.getItem(1);
        ItemStack slot2 = inputs.getItem(2);
        if (slot0.getItem() instanceof Accessory acc0 && slot2.getItem() instanceof Accessory acc2) {
            if (slot1.getItem() instanceof StellarFragment) {
                ((SmithingVzlomMixing)menu).getResultSlots().setItem(0, slot2);
                ci.cancel();
            }
        }
    }

    @Inject(method = "findSlotMatchingIngredient", at = @At("RETURN"), cancellable = true)
    private static void findSlotMatchingIngredientMix(SmithingRecipe recipe, ItemStack stack, CallbackInfoReturnable<OptionalInt> cir) {
        if (stack.getItem() instanceof Accessory) {
            cir.setReturnValue(OptionalInt.of(0));
        } else if (stack.getItem() instanceof StellarFragment) {
            cir.setReturnValue(OptionalInt.of(1));
        }
    }
}
