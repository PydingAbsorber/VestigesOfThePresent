package com.pyding.vp.item;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StellarFragment extends Item {
    public StellarFragment(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        ItemStack stack = player.getItemInHand(p_41434_);
        stack.split(1);
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            cap.setChance(cap.getChance()+1);
            cap.sync(player);
        });
        VPUtil.spawnParticles(player,ParticleTypes.GLOW,player.getX(),player.getY(),player.getZ(),8,0,0,0);
        return super.use(level, player, p_41434_);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("vp.frag", 1+"%").withStyle(ChatFormatting.GRAY).append(Component.literal(VPUtil.getRainbowString("Stellar"))));
        components.add(Component.translatable("vp.frag.get").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, components, flag);
    }
}
