package com.pyding.vp.item;

import com.pyding.vp.client.GuideScreen;
import com.pyding.vp.client.WelcomeScreen;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.ServerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
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

public class WelcomeBook extends Item {
    public WelcomeBook(Properties p_41383_) {
        super(p_41383_);
    }

    public WelcomeBook() {
        super(new Properties().stacksTo(1));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!ServerConfig.COMMON.usedBook.get() || player.isCreative()) {
            Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new WelcomeScreen()));
            player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.BOOK_OPEN.get(), SoundSource.MASTER, 1, 1, false);
        }
        player.getItemInHand(hand).shrink(1);
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> list, TooltipFlag p_41424_) {
        list.add(Component.translatable("vp.welcome_book.desc"));
        list.add(Component.translatable("vp.welcome_book.desc2"));
        super.appendHoverText(p_41421_, p_41422_, list, p_41424_);
    }
}
