package com.pyding.vp.item.artifacts;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class Midas extends Vestige{
    public Midas(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(9, ChatFormatting.GOLD, 1, 40, 1, 120, 40, 1, hasDamage);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player player, InteractionHand p_41434_) {
        ItemStack stack = player.getMainHandItem();
        int kills = stack.getOrCreateTag().getInt("VPKills");
        if(kills != 0) {
            if (player.isShiftKeyDown()) {
                if(kills > 9*9){
                    player.addItem(new ItemStack(Items.GOLD_BLOCK, 1));
                    stack.getOrCreateTag().putFloat("VPKills",kills-9*9);
                }
            } else {
                if(kills > 9) {
                    player.addItem(new ItemStack(Items.GOLD_INGOT, 1));
                    stack.getOrCreateTag().putFloat("VPKills",kills-9);
                }
            }
        }
        return super.use(p_41432_, player, p_41434_);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.MAGIC1.get());
        player.getPersistentData().putFloat("VPMidasTouch",10);
        VPUtil.spawnParticles(player, ParticleTypes.GLOW,8,1,0,-0.1,0,1,false);
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.spawnParticles(player, ParticleTypes.GLOW,8,1,0,-0.1,0,1,false);
        ItemStack stack = VPUtil.getVestigeStack(this,player);
        int kills = stack.getOrCreateTag().getInt("VPKills");
        if(Math.random() < (0.01/100)*kills) {
            stack.getOrCreateTag().putInt("VPLuck", stack.getOrCreateTag().getInt("VPLuck") + 1);
            VPUtil.play(player,SoundRegistry.SUCCESS.get());
        } else VPUtil.play(player,SoundEvents.IRON_GOLEM_DEATH);
        while (kills > 0 && isStellar) {
            if (kills > 9 * 9 * 9) {
                player.addItem(new ItemStack(Items.GOLD_BLOCK, 9));
                kills -= 9 * 9 * 9;
            } else if (kills > 9 * 9) {
                player.addItem(new ItemStack(Items.GOLD_BLOCK, 1));
                kills -= 9 * 9;
            } else if (kills > 9) {
                player.addItem(new ItemStack(Items.GOLD_INGOT, 1));
                kills -= 9;
            } else {
                player.addItem(new ItemStack(Items.GOLD_NUGGET, 1));
                kills -= 1;
            }
        }
        stack.getOrCreateTag().putInt("VPKills",0);

        super.doUltimate(seconds, player, level);
    }

    private Multimap<Attribute, AttributeModifier> createAttributeMap(ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();
        int luck = stack.getOrCreateTag().getInt("VPLuck");
        attributesDefault.put(Attributes.LUCK, new AttributeModifier(UUID.fromString("f55f3429-0399-4d9e-9f84-0d7156cc0593"), "vp:luck", luck, AttributeModifier.Operation.ADDITION));
        return attributesDefault;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if (player.tickCount % 20 == 0) {
            player.getAttributes().addTransientAttributeModifiers(this.createAttributeMap(stack));
        }
        if(!isSpecialActive)
            player.getPersistentData().putFloat("VPMidasTouch",0);
        super.curioTick(slotContext, stack);
    }

    public boolean fuckNbtCheck1 = false;
    public boolean fuckNbtCheck2 = false;

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if (!fuckNbtCheck1) {
            player.getAttributes().removeAttributeModifiers(this.createAttributeMap(stack));
            super.onUnequip(slotContext, newStack, stack);
        } else fuckNbtCheck1 = false;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if(!fuckNbtCheck2) {
            super.onEquip(slotContext, prevStack, stack);
        } else fuckNbtCheck2 = false;
    }
}
