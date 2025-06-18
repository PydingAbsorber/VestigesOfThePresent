package com.pyding.vp.mixin;

import com.pyding.vp.item.ModItems;
import com.pyding.vp.util.VPUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Inventory.class,priority = 0)
public abstract class InventoryMixin {

    @Shadow @Final public Player player;

    @Inject(method = "tick",at = @At("HEAD"),cancellable = true, require = 1)
    private void tick(CallbackInfo ci){
        if(VPUtil.isRoflanEbalo(this.player)){
            ci.cancel();
        }
    }
}
