package com.pyding.vp.mixin;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.StellarFragment;
import com.pyding.vp.item.accessories.Accessory;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = SmithingMenu.class)
public abstract class SmithingMenuMixin {

    @Inject(method = "onTake",at = @At("HEAD"),cancellable = true, require = 1)
    protected void onTakeMixin(Player player, ItemStack stack, CallbackInfo ci){
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            if(((SmithingVzlomMixing)this).getInputSlots().getItem(0).getItem() instanceof SmithingTemplateItem smithingTemplateItem &&
                    ((SmitingMixing) smithingTemplateItem).upgradeDescription().getContents() instanceof TranslatableContents translatableContents) {
                cap.addTemplate(translatableContents.getKey(), player);
            }
        });
        if(((SmithingVzlomMixing)this).getInputSlots().getItem(0).getItem() instanceof Accessory accessory)
            accessory.lvlUp(stack,player);
    }

    @Inject(method = "createInputSlotDefinitions",at = @At("RETURN"),cancellable = true, require = 1)
    protected void createInputSlotDefinitionsMix(CallbackInfoReturnable<ItemCombinerMenuSlotDefinition> cir){
        cir.setReturnValue(ItemCombinerMenuSlotDefinition.create().withSlot(0, 8, 48, (p_266643_) -> {
            return ((SmitingMenuVzlom)this).getRecipes().stream().anyMatch((p_266642_) -> {
                if(p_266643_.getItem() instanceof Accessory)
                    return true;
                return p_266642_.isTemplateIngredient(p_266643_);
            });
        }).withSlot(1, 26, 48, (p_286208_) -> {
            return ((SmitingMenuVzlom)this).getRecipes().stream().anyMatch((p_286206_) -> {
                if(p_286208_.getItem() instanceof StellarFragment)
                    return true;
                return p_286206_.isBaseIngredient(p_286208_);
            });
        }).withSlot(2, 44, 48, (p_286207_) -> {
            return ((SmitingMenuVzlom)this).getRecipes().stream().anyMatch((p_286204_) -> {
                if(p_286207_.getItem() instanceof Accessory)
                    return true;
                return p_286204_.isAdditionIngredient(p_286207_);
            });
        }).withResultSlot(3, 98, 48).build());
    }

    @Inject(method = "findSlotMatchingIngredient",at = @At("RETURN"),cancellable = true, require = 1)
    private static void findSlotMatchingIngredientMix(SmithingRecipe p_266790_, ItemStack stack, CallbackInfoReturnable<Optional<Integer>> cir){
        /*if(stack.getItem() instanceof Accessory && ((ItemCombinerMenuVzlom)this).getInputSlots().getItem(0).getItem() instanceof Accessory){
            cir.setReturnValue(Optional.of(2));
        }*/
        if (stack.getItem() instanceof Accessory) {
            cir.setReturnValue(Optional.of(0));
        } else if (stack.getItem() instanceof StellarFragment) {
            cir.setReturnValue(Optional.of(1));
        }
    }
}
