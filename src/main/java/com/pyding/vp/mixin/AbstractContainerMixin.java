package com.pyding.vp.mixin;

import com.pyding.vp.item.ChaosOrb;
import com.pyding.vp.item.CorruptFragment;
import com.pyding.vp.item.CorruptItem;
import com.pyding.vp.util.VPUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMixin {

    @Inject(at = @At(value = "HEAD"), method = "doClick", cancellable = true)
    protected void onClick(int index, int action, ClickType clickType, Player player, CallbackInfo ci) {
        if (clickType == ClickType.PICKUP && index >= 0) {
            AbstractContainerMenu menu = (AbstractContainerMenu) (Object) this;
            ItemStack carried = menu.getCarried();
            if (!carried.isEmpty() && (carried.getItem() instanceof CorruptFragment || carried.getItem() instanceof CorruptItem || carried.getItem() instanceof ChaosOrb)) {
                Slot targetSlot = menu.slots.get(index);
                ItemStack targetStack = targetSlot.getItem();
                if (!targetStack.isEmpty() && !(targetStack.getItem() instanceof CorruptFragment || targetStack.getItem() instanceof CorruptItem || targetStack.getItem() instanceof ChaosOrb)) {
                    VPUtil.useOrb(targetStack,carried,player);
                    ci.cancel();
                }
            }
        }
    }
}
