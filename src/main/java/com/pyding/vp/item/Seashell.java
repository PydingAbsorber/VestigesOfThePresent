package com.pyding.vp.item;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class Seashell extends Item {
    public Seashell(Properties p_41383_) {
        super(p_41383_);
    }

    public Seashell() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player player, InteractionHand p_41434_) {
        System.out.println(ConfigHandler.COMMON.reduceChallenges.get());
        ItemStack stack = player.getItemInHand(p_41434_);
        VPUtil.spawnSphere(player, ParticleTypes.BUBBLE,30,3,0.5f);
        switch (stack.getOrCreateTag().getInt("VPSType")){
            case 1: {
                VPUtil.play(player, SoundRegistry.BUBBLE1.get());
                break;
            }
            case 2: {
                VPUtil.play(player, SoundRegistry.BUBBLE2.get());
                break;
            }
            case 3: {
                VPUtil.play(player, SoundRegistry.BUBBLE3.get());
                break;
            }
            case 4: {
                VPUtil.play(player, SoundRegistry.BUBBLE4.get());
                break;
            }
            default: {
                VPUtil.play(player, SoundRegistry.BUBBLE5.get());
            }
        }
        for(LivingEntity entity: VPUtil.getEntities(player,20,false)){
            if(entity instanceof WaterAnimal waterAnimal){
                waterAnimal.goalSelector.addGoal(1,new TemptGoal(waterAnimal,2d, Ingredient.of(stack),false));
            }
        }
        return super.use(p_41432_, player, p_41434_);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            components.add(Component.translatable("vp.seashell.use").withStyle(ChatFormatting.GRAY));
        }
        else if (Screen.hasControlDown()){
            components.add(Component.translatable("vp.seashell.obtain").withStyle(ChatFormatting.GRAY));
        }
        else {
            components.add(Component.translatable("vp.press").append(Component.literal("SHIFT").withStyle(ChatFormatting.BLUE).append(Component.translatable("vp.shift"))));
            components.add(Component.translatable("vp.press").append(Component.literal("CTRL").withStyle(ChatFormatting.BLUE).append(Component.translatable("vp.ctrl"))));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        if(stack.getOrCreateTag().getInt("VPSType") == 0){
            Random random = new Random();
            stack.getOrCreateTag().putInt("VPSType",random.nextInt(5)+1);
        }
        super.inventoryTick(stack, p_41405_, p_41406_, p_41407_, p_41408_);
    }

    @OnlyIn(Dist.CLIENT)
    public void registerChick() {
        ItemProperties.register(this, new ResourceLocation(VestigesOfThePresent.MODID, "shell"),
                (stack, world, entity, number) -> stack.getOrCreateTag().getInt("VPSType"));
    }
}
