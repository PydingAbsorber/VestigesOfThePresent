package com.pyding.vp.item.vestiges;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

public class Devourer extends Vestige{
    public Devourer(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(15, ChatFormatting.DARK_RED, 1, 30, 1, 115, 5, 130, hasDamage, stack);
    }
    int souls = 0;

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        souls = stack.getOrCreateTag().getInt("VPDevoured");
        super.curioTick(slotContext, stack);
    }

    @Override
    public int setSpecialActive(long seconds, Player player, ItemStack stack) {
        seconds += 100L *(Math.min(150,souls));
        return super.setSpecialActive(seconds, player, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.DEVOURER_BIND.get());
        for (LivingEntity entity : VPUtil.ray(player, 6, 30, true)) {
            if(!VPUtil.isProtectedFromHit(player,entity)) {
                VPUtil.bindEntity(entity,seconds);
            }
        }
        VPUtil.rayParticles(player, ParticleTypes.DRAGON_BREATH,30,6,30,0,-1,0,5,false);
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.SOUL2.get());
        player.getPersistentData().putInt("VPDevourerHits", VPUtil.scalePower(ConfigHandler.COMMON.devourer.get(),15,player));
        VPUtil.spawnParticles(player, ParticleTypes.GLOW_SQUID_INK,8,1,0,0.1,0,1,false);
        super.doUltimate(seconds, player, level, stack);
    }
}
