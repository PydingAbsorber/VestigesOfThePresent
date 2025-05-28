package com.pyding.vp.item;

import com.pyding.vp.mixin.EntityVzlom;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class EventHorizon extends Item {
    public EventHorizon(Properties p_41383_) {
        super(p_41383_);
    }

    public EventHorizon() {
        super(new Properties().stacksTo(1));
    }

    public static void destroy(Entity entity, Player player){
        if(entity instanceof ItemEntity)
            entity.kill();
        else if (entity instanceof LivingEntity livingEntity && (!VPUtil.isProtectedFromHit(player,entity) || player.getOffhandItem().is(ModItems.STELLAR.get())))
            VPUtil.deadInside(livingEntity);
        else {
            entity.kill();
            ((EntityVzlom) entity).setPersistentData(null);
            entity.invalidateCaps();
            ((EntityVzlom) entity).getLevelCallback().onRemove(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player player, InteractionHand p_41434_) {
        if(!player.isCreative() || player.getServer() == null)
            return super.use(p_41432_, player, p_41434_);
        if(player.isCrouching()) {
            VPUtil.deadInside(player,player);
        }
        else if(player instanceof ServerPlayer serverPlayer) {
            try {
                List<Entity> list = new ArrayList<>();
                serverPlayer.serverLevel().getAllEntities().forEach(list::add);
                for(Entity entity: list){
                    if(entity != player)
                        destroy(entity,player);
                }
            } catch (Exception e) {
                player.sendSystemMessage(Component.literal(e.getMessage()).withStyle(ChatFormatting.DARK_RED));
            }
        }
        return super.use(p_41432_, player, p_41434_);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity swinger) {
        if(swinger instanceof Player player && player.isCreative()) {
            LivingEntity entity = null;
            for (LivingEntity livingEntity : VPUtil.ray(player, 2, 60, true)) {
                entity = livingEntity;
                break;
            }
            if(entity != null && !VPUtil.isProtectedFromHit(player,entity)){
                VPUtil.deadInside(entity, player);
            }
        }
        return super.onEntitySwing(stack, swinger);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("vp.event_horizon.desc").withStyle(ChatFormatting.GRAY));
    }
}
