package com.pyding.vp.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Mixin(value = Item.class)
public abstract class MaxDurabilityMixin {

    @Inject(method = "mineBlock",at = @At("RETURN"),cancellable = true, require = 1)
    public void mineBlock(ItemStack p_41416_, Level p_41417_, BlockState p_41418_, BlockPos p_41419_, LivingEntity entity, CallbackInfoReturnable<Boolean> cir) throws NoSuchFieldException, IllegalAccessException {
        if(entity.getPersistentData().getBoolean("VPVortexMake")){
            entity.getPersistentData().putBoolean("VPVortexMake",false);
            try {
                Field field = Item.class.getDeclaredField("maxDamage");
                field.setAccessible(true);
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                field.set(this, 0);
            }
            catch (NoSuchFieldException exception){
                System.out.println("fuck that gone wrong im sorry (Vestiges)");
            }

            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canBeDepleted",at = @At("RETURN"),cancellable = true, require = 1)
    public void canBeDepleted(CallbackInfoReturnable<Boolean> cir){

    }
}
