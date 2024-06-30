package com.pyding.vp.mixin;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SmithingMenu.class)
public abstract class SmithingMenuMixin {

    @Inject(method = "onTake",at = @At("HEAD"),cancellable = true, require = 1)
    protected void onTakeMixin(Player player, ItemStack stack, CallbackInfo ci){
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            if(((SmithingVzlomMixing)this).getInputSlots().getItem(0).getItem() instanceof SmithingTemplateItem smithingTemplateItem)
                cap.addTemplate(((SmitingMixing) smithingTemplateItem).upgradeDescription().getString(),player);
        });
    }
}
