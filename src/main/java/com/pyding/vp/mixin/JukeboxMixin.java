package com.pyding.vp.mixin;

import com.pyding.vp.item.ModItems;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LevelRenderer.class)
public abstract class JukeboxMixin {

    @Inject(method = "notifyNearbyEntities",at = @At("HEAD"),cancellable = true, require = 1)
    protected void notifyNearbyEntities(Level p_109551_, BlockPos p_109552_, boolean p_109553_, CallbackInfo ci){
        for(LivingEntity livingentity : p_109551_.getEntitiesOfClass(LivingEntity.class, (new AABB(p_109552_)).inflate(3.0D))) {
            if(livingentity instanceof Player player && VPUtil.hasVestige(ModItems.LYRA.get(), player)){
                player.getPersistentData().putLong("VPJuke",System.currentTimeMillis()+5000);
            }
        }
    }
}
