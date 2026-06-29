package com.pyding.vp.item;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.entity.HungryOyster;
import com.pyding.vp.entity.ModEntities;
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

public class OysterSummon extends Item {
    public OysterSummon(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Random random = new Random();
        HungryOyster oyster = new HungryOyster(ModEntities.OYSTER.get(),level);
        oyster.setPos(player.getX(),player.getY(),player.getZ());
        if(VPUtil.teleportRandomly(oyster,30,true)) {
            level.addFreshEntity(oyster);
            if (random.nextDouble() < VPUtil.getChance(0.1, player)) {
                oyster.getPersistentData().putBoolean("VPCool", true);
                VPUtil.syncEntity(oyster);
            }
            VPUtil.play(player, SoundRegistry.BUBBLEPOP.get(), oyster.getX(), oyster.getY(), oyster.getZ(), 0.4f);
            for (int i = 0; i < 3; i++) {
                TropicalFish fish = new TropicalFish(EntityType.TROPICAL_FISH, level);
                fish.setPos(player.getX(), player.getY(), player.getZ());
                VPUtil.teleportRandomly(fish, 30, true);
                fish.getPersistentData().putLong("VPEating", System.currentTimeMillis());
                level.addFreshEntity(fish);
            }
        }
        stack.shrink(1);
        return super.use(level,player,hand);
    }
}
