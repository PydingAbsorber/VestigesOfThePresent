package com.pyding.vp.item.vestiges;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Random;

public class Archlinx extends Vestige{
    public Archlinx(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(25, ChatFormatting.BLUE, 2, 25, 1, 1200, 9999, 9999, true, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {

        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.WIND3.get());
        VPUtil.spawnParticles(player, ParticleTypes.CAMPFIRE_COSY_SMOKE,3,1,0,0.1,0,1,false);
        super.doUltimate(seconds, player, level, stack);
    }

    @Override
    public void ultimateEnds(Player player, ItemStack stack) {
        super.ultimateEnds(player, stack);
    }

}
