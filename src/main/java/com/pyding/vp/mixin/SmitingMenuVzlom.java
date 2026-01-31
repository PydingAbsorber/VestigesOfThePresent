package com.pyding.vp.mixin;

import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmithingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SmithingMenu.class)
public interface SmitingMenuVzlom {

    @Accessor("recipes")
    @Mutable
    List<RecipeHolder<SmithingRecipe>> getRecipes();
}
