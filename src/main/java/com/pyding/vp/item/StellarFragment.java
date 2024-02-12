package com.pyding.vp.item;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.entity.BlackHole;
import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.SendPlayerNbtToClient;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
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
        if(level instanceof ServerLevel serverLevel){
            float gravitation = 20;
            BlackHole blackHole = new BlackHole(serverLevel,player,gravitation,player.blockPosition());
            BlockPos pos = VPUtil.rayCords(player,serverLevel,30);
            System.out.println(pos);
            blackHole.setPos(pos.getX(),pos.getY(),pos.getZ());
            serverLevel.addFreshEntity(blackHole);
            if(player instanceof ServerPlayer serverPlayer)
                VPUtil.spawnParticles(serverPlayer, ParticleTypes.PORTAL, player.getX(), player.getY(), player.getZ(), 200, 1, 1, 1, 1, true);
        }
        return super.use(level, player, p_41434_);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("vp.frag").withStyle(ChatFormatting.GRAY).append(Component.literal(VPUtil.getRainbowString("Stellar"))));
        components.add(Component.translatable("vp.frag.get").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, level, components, flag);
    }
}
