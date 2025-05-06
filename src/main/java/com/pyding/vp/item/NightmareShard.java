package com.pyding.vp.item;

import com.pyding.vp.entity.HungryOyster;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class NightmareShard extends Item {
    public NightmareShard(Properties p_41383_) {
        super(p_41383_);
    }

    public NightmareShard() {
        super(new Properties().stacksTo(64));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player player, InteractionHand p_41434_) {
        ItemStack stack = player.getItemInHand(p_41434_);
        if(player.isCrouching() && player.isCreative()) {
            VPUtil.despawn(player);
        }
        LivingEntity entity = null;
        for(LivingEntity livingEntity: VPUtil.ray(player,2,2,true)){
            entity = livingEntity;
            break;
        }
        if(entity instanceof HungryOyster){
            entity.getPersistentData().putBoolean("VPCool",true);
        } else if(entity != null && player.isCreative()) {
            VPUtil.deadInside(entity, player);
        }
        return super.use(p_41432_, player, p_41434_);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity swinger) {
        if(swinger instanceof Player player && player.isCreative()) {
            LivingEntity entity = null;
            for (LivingEntity livingEntity : VPUtil.ray(player, 2, 40, true)) {
                entity = livingEntity;
                break;
            }
            if(entity != null){
                VPUtil.dealDamage(entity,player,VPUtil.randomizeDamageType(player), Integer.MAX_VALUE,0,true);
            }
        }
        return super.onEntitySwing(stack, swinger);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("vp.nightmareshard.get").withStyle(ChatFormatting.GRAY));
        components.add(Component.translatable("vp.nightmareshard.1").withStyle(ChatFormatting.GRAY));
    }
}
