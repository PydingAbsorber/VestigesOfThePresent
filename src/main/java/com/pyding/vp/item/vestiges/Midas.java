package com.pyding.vp.item.vestiges;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Random;
import java.util.UUID;

public class Midas extends Vestige{
    public Midas(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(9, ChatFormatting.GOLD, 1, 40, 1, 320, 40, 1, hasDamage, stack);
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
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.MAGIC1.get());
        player.getPersistentData().putFloat("VPMidasTouch",10);
        VPUtil.spawnParticles(player, ParticleTypes.GLOW,8,1,0,-0.1,0,1,false);
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.spawnParticles(player, ParticleTypes.GLOW,8,1,0,-0.1,0,1,false);
        int kills = stack.getOrCreateTag().getInt("VPKills");
        Random random = new Random();
        if(random.nextDouble() < VPUtil.getChance((ConfigHandler.COMMON.midasChance.get())*kills,player)) {
            stack.getOrCreateTag().putInt("VPLuck", stack.getOrCreateTag().getInt("VPLuck") + 1);
            VPUtil.play(player,SoundRegistry.SUCCESS.get());
        } else VPUtil.play(player,SoundEvents.IRON_GOLEM_DEATH);
        while (kills > 0 && isStellar(stack)) {
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

        super.doUltimate(seconds, player, level, stack);
    }

    private Multimap<Attribute, AttributeModifier> createAttributeMap(ItemStack stack,Player player) {
        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();
        int luck = VPUtil.scalePower(stack.getOrCreateTag().getInt("VPLuck"),9,player);
        attributesDefault.put(Attributes.LUCK, new AttributeModifier(UUID.fromString("f55f3429-0399-4d9e-9f84-0d7156cc0593"), "vp:luck", luck, AttributeModifier.Operation.ADDITION));
        return attributesDefault;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if (player.tickCount % 20 == 0) {
            player.getAttributes().addTransientAttributeModifiers(this.createAttributeMap(stack,player));
        }
        if(!isSpecialActive(stack))
            player.getPersistentData().putFloat("VPMidasTouch",0);
        super.curioTick(slotContext, stack);
    }
}
