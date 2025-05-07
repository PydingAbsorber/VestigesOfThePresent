package com.pyding.vp.item;

import com.pyding.vp.client.GuideScreen;
import com.pyding.vp.client.NightmareScreen;
import com.pyding.vp.client.sounds.SoundRegistry;
import net.minecraft.ChatFormatting;
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

public class NightmareBook extends Item {
    public NightmareBook(Properties p_41383_) {
        super(p_41383_);
    }

    public NightmareBook() {
        super(new Properties().stacksTo(1));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new NightmareScreen()));
        player.getCommandSenderWorld().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.CHEST_FALL.get(), SoundSource.MASTER, 1, 1, false);
        return super.use(level, player, p_41434_);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("vp.nightmare_book.desc").withStyle(ChatFormatting.GRAY));
    }
}
