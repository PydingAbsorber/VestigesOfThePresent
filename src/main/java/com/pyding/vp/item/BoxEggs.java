package com.pyding.vp.item;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoxEggs extends Item {
    public BoxEggs(Properties p_41383_) {
        super(p_41383_);
    }

    public BoxEggs() {
        super(new Properties().stacksTo(64));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        if(!level.isClientSide) {
            VPUtil.play(player, SoundRegistry.OPEN.get());
            ItemStack stack = player.getItemInHand(p_41434_);
            stack.split(1);
            List<Item> list = new ArrayList<>();
            for (Item stack1 : VPUtil.items) {
                if (stack1.getDescriptionId().contains("egg") && !stack1.getDescriptionId().contains("leg") && !stack1.getDescriptionId().contains("eggplant"))
                    list.add(stack1);
            }
            if (list.isEmpty())
                return super.use(level, player, p_41434_);
            Random random = new Random();
            int numba = random.nextInt(list.size());
            if (player.getInventory().getFreeSlot() != -1)
                player.addItem(list.get(numba).getDefaultInstance());
            else player.drop(list.get(numba).getDefaultInstance(), true);
        }
        return super.use(level, player, p_41434_);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("box.egg.info.1").withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}
