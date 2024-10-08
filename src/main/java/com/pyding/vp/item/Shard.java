package com.pyding.vp.item;

import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Shard extends Item {
    public Shard(Properties p_41383_) {
        super(p_41383_);
    }

    public Shard() {
        super(new Item.Properties().stacksTo(64));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player player, InteractionHand p_41434_) {
        ItemStack stack = player.getItemInHand(p_41434_);
        LivingEntity entity = null;
        for(LivingEntity livingEntity: VPUtil.ray(player,2,2,true)){
            entity = livingEntity;
            break;
        }
        if(!p_41432_.isClientSide && entity!= null && VPUtil.isBoss(entity) && ConfigHandler.COMMON.hardcore.get() && (!VPUtil.isNightmareBoss(entity) || player.isCreative())) {
            stack.shrink(1);
            Random random = new Random();
            if(random.nextDouble() < VPUtil.getChance(0.2,player) || player.isCreative()){
                entity.getPersistentData().putBoolean("VPNightmareBoss",true);
                entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity, Attributes.MAX_HEALTH, UUID.fromString("534c53b9-3c22-4c34-bdcd-f255a9694b34"),10, AttributeModifier.Operation.MULTIPLY_TOTAL,"vp:nightmare.hp"));
                entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity, Attributes.ATTACK_DAMAGE, UUID.fromString("1d665861-143f-4906-9ab0-e511ad377783"),10, AttributeModifier.Operation.MULTIPLY_TOTAL,"vp:nightmare.attack"));
                VPUtil.addShield(entity, (float) (entity.getMaxHealth()*ConfigHandler.COMMON.shieldHardcore.get()),false);
                entity.getPersistentData().putFloat("VPOverShield", (float) (entity.getMaxHealth()*ConfigHandler.COMMON.overShieldHardcore.get()));
                entity.setGlowingTag(true);
                entity.getPersistentData().putInt("VPBossType",random.nextInt(7)+1);
                entity.refreshDimensions();
                VPUtil.setHealth(entity,entity.getMaxHealth());
                if(player.getName().getString().equals("Pyding"))
                    player.sendSystemMessage(Component.literal(entity.getMaxHealth() + " curent" + entity.getHealth()));
            }
        }
        if(entity instanceof TropicalFish){
            stack.shrink(1);
            entity.getPersistentData().putLong("VPEating",System.currentTimeMillis());
        }
        return super.use(p_41432_, player, p_41434_);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("vp.shard.get").withStyle(ChatFormatting.GRAY));
        components.add(Component.translatable("vp.shard").withStyle(ChatFormatting.GRAY));
        components.add(Component.translatable("vp.shard.1").withStyle(ChatFormatting.GRAY));
    }
}
