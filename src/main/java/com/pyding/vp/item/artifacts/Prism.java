package com.pyding.vp.item.artifacts;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.SlotContext;

import java.util.ArrayList;
import java.util.List;

public class Prism extends Vestige{
    public Prism(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(13, ChatFormatting.RED, 4, 60, 1, 300, 40, 60, hasDamage);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        LivingEntity entity = VPUtil.getRandomEntityNear(player,isStellar);
        if(entity != null) {
            entity.getPersistentData().putLong("VPPrismBuff", System.currentTimeMillis()+specialMaxTime);
            entity.getPersistentData().putString("VPPrismDamage",VPUtil.generateRandomDamageType());
        }
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        super.doUltimate(seconds, player, level);
    }

    public boolean fuckNbt1 = false;
    public boolean fuckNbt2 = false;
    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if(!fuckNbt2)
            super.onEquip(slotContext, prevStack, stack);
        else fuckNbt2 = false;
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if(!fuckNbt1){
            player.getPersistentData().putInt("VPPrism", 0);
            super.onUnequip(slotContext, newStack, stack);
        } else fuckNbt1 = false;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
            return super.onLeftClickEntity(stack, player, entity);
    }
}
