package com.pyding.vp.item;

import com.pyding.vp.util.VPUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TestItem extends Item {
    public TestItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        super.inventoryTick(itemStack, level, entity, p_41407_, p_41408_);
        if(entity instanceof Player player){
            if(player.getMainHandItem().is(itemStack.getItem())){
                player.sendSystemMessage(Component.literal("IsClient: " + level.isClientSide + " health: " + player.getHealth() + " isAlive: " + player.isAlive() + " isRoflan: " + VPUtil.isRoflanEbalo(player)));
            }
        }
    }
}
