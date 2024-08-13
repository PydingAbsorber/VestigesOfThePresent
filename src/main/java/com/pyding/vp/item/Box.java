package com.pyding.vp.item;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class Box extends Item {
    public Box(Properties p_41383_) {
        super(p_41383_);
    }

    public Box() {
        super(new Item.Properties().stacksTo(64));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        VPUtil.play(player, SoundRegistry.OPEN.get());
        ItemStack stack = player.getItemInHand(p_41434_);
        stack.split(1);
        Random random = new Random();
        ItemStack accessory;
        if(random.nextDouble() > 0.5){
            if(random.nextDouble() > 0.5){
                accessory = new ItemStack(ModItems.RING_OF_FALLEN_STAR.get());
            } else {
                accessory = new ItemStack(ModItems.BELT_OF_BROKEN_MEMORIES.get());
            }
        } else {
            if(random.nextDouble() > 0.5){
                accessory = new ItemStack(ModItems.EARRING_OF_DEAD_HOPES.get());
            } else {
                accessory = new ItemStack(ModItems.NECKLACE_OF_TORTURED_DREAMS.get());
            }
        }
        player.addItem(accessory);
        return super.use(level, player, p_41434_);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        Player player = Minecraft.getInstance().player;
        components.add(Component.translatable("box.info.1").withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}
