package com.pyding.vp.item;

import com.pyding.vp.client.GuideScreen;
import com.pyding.vp.client.sounds.SoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class GuideBook extends Item {
    public GuideBook(Properties p_41383_) {
        super(p_41383_);
    }

    public GuideBook() {
        super(new Properties().stacksTo(1));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new GuideScreen()));
        player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.BOOK_OPEN.get(), SoundSource.MASTER, 1, 1, false);
        return super.use(level, player, p_41434_);
    }
}
