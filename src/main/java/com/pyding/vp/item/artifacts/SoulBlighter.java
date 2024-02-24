package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

public class SoulBlighter extends Vestige{
    public SoulBlighter(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(20, ChatFormatting.LIGHT_PURPLE, 2, 30, 1, 300, 15, 300, hasDamage);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.MAGIC4.get());
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.play(player, SoundRegistry.SOUL3.get());
        super.doUltimate(seconds, player, level);
    }


    public boolean fuckNbtCheck1 = false;
    public boolean fuckNbtCheck2 = false;

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (!fuckNbtCheck1) {
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
