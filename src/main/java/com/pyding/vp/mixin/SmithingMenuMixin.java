package com.pyding.vp.mixin;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.StellarFragment;
import com.pyding.vp.item.accessories.Accessory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
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
        /*player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            if(((SmithingVzlomMixing)this).getInputSlots().getItem(0).getItem() instanceof SmithingTemplateItem smithingTemplateItem)
                cap.addTemplate(((SmitingMixing) smithingTemplateItem).upgradeDescription().getString(),player);
        });*/
        if(((SmithingVzlomMixing)this).getInputSlots().getItem(0).getItem() instanceof Accessory accessory)
            accessory.lvlUp(stack,player);
    }

    @Inject(
            method = "shouldQuickMoveToAdditionalSlot",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void onShouldQuickMoveToAdditionalSlot(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof StellarFragment) {
            cir.setReturnValue(true);
        }
        else if (stack.getItem() instanceof Accessory) {
            cir.setReturnValue(true);
        }
    }

    /*@Inject(
            method = "createResult",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void onCreateResult(CallbackInfo ci) {
        ItemStack template = this.inputSlots.getItem(0);
        ItemStack base = this.inputSlots.getItem(1);
        ItemStack addition = this.inputSlots.getItem(2);

        if (template.getItem() instanceof Accessory
                && base.getItem() instanceof StellarFragment
                && addition.getItem() instanceof Accessory) {

            ItemStack result = new ItemStack();
            this.resultSlots.setItem(0, result);
            ci.cancel();
        }
    }*/
}
