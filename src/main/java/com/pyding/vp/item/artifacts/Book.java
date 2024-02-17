package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.ArrayList;
import java.util.List;

public class Book extends Vestige{
    public Book(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(12, ChatFormatting.LIGHT_PURPLE, 1, 10, 1, 200, 15, 60, hasDamage);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        for(LivingEntity entity: VPUtil.getEntitiesAround(player,30,30,30,false)){
            VPUtil.negativnoEnchant(entity);
        }
        VPUtil.play(player,SoundRegistry.MAGIC_EFFECT2.get());
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.MAGIC_EFFECT1.get());
        super.doUltimate(seconds, player, level);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        player.getPersistentData().putBoolean("VPBook", isUltimateActive);
        super.curioTick(slotContext, stack);
    }
}
