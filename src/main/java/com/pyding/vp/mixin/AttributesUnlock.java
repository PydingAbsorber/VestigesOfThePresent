package com.pyding.vp.mixin;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(Attributes.class)
public abstract class AttributesUnlock {


    /*@Shadow @Final @Mutable
    public static Attribute MAX_HEALTH;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyMaxHealth(CallbackInfo ci) {
        try {
            Field maxHealthField = Attributes.class.getDeclaredField("MAX_HEALTH");
            maxHealthField.setAccessible(true);
            MAX_HEALTH = new RangedAttribute("attribute.name.generic.max_health", 20.0, 1.0, Double.MAX_VALUE).setSyncable(true);
            maxHealthField.set(null, MAX_HEALTH);

            Registry.register(BuiltInRegistries.ATTRIBUTE, new ResourceLocation("minecraft", "generic.max_health"), MAX_HEALTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
