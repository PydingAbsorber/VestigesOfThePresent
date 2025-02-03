package com.pyding.vp.item;

import com.pyding.vp.util.VPUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EaterEgg extends Item {
    public EaterEgg(Properties p_41383_) {
        super(p_41383_);
    }

    public EaterEgg() {
        super(new Item.Properties().stacksTo(64).tab(null));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) {
        ItemStack stack = p_41433_.getItemInHand(p_41434_);
        if(!p_41432_.isClientSide) {
            if (VPUtil.spawnEgg(p_41433_))
                stack.shrink(1);
        }
        return super.use(p_41432_, p_41433_, p_41434_);
    }
}
