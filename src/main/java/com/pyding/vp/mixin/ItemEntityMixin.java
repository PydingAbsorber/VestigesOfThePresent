package com.pyding.vp.mixin;

import com.pyding.vp.item.CelestialMirror;
import com.pyding.vp.item.ChaosOrb;
import com.pyding.vp.item.CorruptFragment;
import com.pyding.vp.item.CorruptItem;
import com.pyding.vp.util.VPUtil;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemEntity.class)
public abstract class ItemEntityMixin {

    @Shadow public abstract ItemStack getItem();

    @Inject(method = "tick",at = @At("HEAD"),cancellable = true, require = 1)
    private void tickMixin(CallbackInfo ci){
        if(!getItem().isEmpty()){
            Item item = getItem().getItem();
            SimpleParticleType type = null;
            if(item instanceof CorruptFragment)
                type = ParticleTypes.CRIMSON_SPORE;
            else if(item instanceof CorruptItem)
                type = ParticleTypes.FLAME;
            else if(item instanceof ChaosOrb)
                type = ParticleTypes.WAX_ON;
            else if(item instanceof CelestialMirror)
                type = ParticleTypes.GLOW_SQUID_INK;
            VPUtil.spawnAura((ItemEntity)(Object)this,3, type,0.1);
        }
    }
}
