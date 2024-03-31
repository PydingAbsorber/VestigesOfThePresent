package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
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
        VPUtil.play(player,SoundRegistry.MAGIC_EFFECT2.get());
        for(LivingEntity entity: VPUtil.getEntitiesAround(player,30,30,30,false)){
            VPUtil.negativnoEnchant(entity);
        }
        VPUtil.spawnParticles(player, ParticleTypes.ENCHANT,10,1,0,-0.1,0,1,false);
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.MAGIC_EFFECT1.get());
        VPUtil.spawnParticles(player, ParticleTypes.ENCHANTED_HIT,8,1,0,-0.1,0,1,false);
        super.doUltimate(seconds, player, level);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
    }
}
