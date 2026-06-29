package com.pyding.vp.item;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.entity.HungryOyster;
import com.pyding.vp.entity.ModEntities;
import com.pyding.vp.entity.SillySeashell;
import com.pyding.vp.util.VPUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

public class SeashellSummon extends Item {
    public SeashellSummon(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        SillySeashell shell = new SillySeashell(ModEntities.SEASHELL.get(),level);
        shell.setPos(player.getX(),player.getY(),player.getZ());
        if(VPUtil.teleportRandomly(shell,30,true)) {
            level.addFreshEntity(shell);
            VPUtil.play(player, SoundRegistry.BUBBLEPOP.get(), shell.getX(), shell.getY(), shell.getZ(), 0.4f);
        }
        stack.shrink(1);
        return super.use(level,player,hand);
    }
}
