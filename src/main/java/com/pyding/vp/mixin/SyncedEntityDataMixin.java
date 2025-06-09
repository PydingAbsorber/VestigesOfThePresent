package com.pyding.vp.mixin;

import com.pyding.vp.util.VPUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SynchedEntityData.class, priority = 0)
public class SyncedEntityDataMixin {

    @Shadow @Final private Entity entity;

    @Inject(method = "get",at = @At("HEAD"),cancellable = true, require = 1)
    private void get(EntityDataAccessor accessor, CallbackInfoReturnable cir){
        if(entity instanceof LivingEntity livingEntity && VPUtil.isRoflanEbalo(livingEntity) && ((LivingEntityVzlom) livingEntity).getDataHealth().getId() == accessor.getId())
            cir.setReturnValue(0f);
    }
}
