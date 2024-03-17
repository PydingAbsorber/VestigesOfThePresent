package com.pyding.vp.item.artifacts;

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
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(15, ChatFormatting.DARK_RED, 1, 30, 1, 130, 5, 130, hasDamage);
    }
    int souls = 0;

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        souls = stack.getOrCreateTag().getInt("VPDevoured");
        super.curioTick(slotContext, stack);
    }

    @Override
    public int setSpecialActive(long seconds, Player player) {
        seconds += 100L *(Math.min(150,souls));
        return super.setSpecialActive(seconds, player);
    }

    LivingEntity victim = null;
    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.DEVOURER_BIND.get());
        for (LivingEntity entity : VPUtil.ray(player, 6, 30, true)) {
            entity.getPersistentData().putLong("VPAntiTP", System.currentTimeMillis() + seconds);
            victim = entity;
            entity.getPersistentData().putDouble("VPDevourerX",entity.getX());
            entity.getPersistentData().putDouble("VPDevourerY",entity.getY());
            entity.getPersistentData().putDouble("VPDevourerZ",entity.getZ());
        }
        VPUtil.rayParticles(player, ParticleTypes.DRAGON_BREATH,30,6,1,0,-1,0,5,false);
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void whileSpecial(Player player) {
        if(victim != null){
            BlockPos pos = new BlockPos((int) victim.getPersistentData().getDouble("VPDevourerX"),(int)victim.getPersistentData().getDouble("VPDevourerY"),(int)victim.getPersistentData().getDouble("VPDevourerZ"));
            VPUtil.suckToPos(victim,pos,3);
            if(victim instanceof ServerPlayer serverPlayer)
                PacketHandler.sendToClient(new PlayerFlyPacket(69),serverPlayer);
        }
        super.whileSpecial(player);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.SOUL2.get());
        player.getPersistentData().putInt("VPDevourerHits", ConfigHandler.COMMON.devourer.get());
        VPUtil.spawnParticles(player, ParticleTypes.GLOW_SQUID_INK,8,1,0,0.1,0,1,false);
        super.doUltimate(seconds, player, level);
    }

    @Override
    public void curioSucks(Player player, ItemStack stack) {
        player.getPersistentData().putInt("VPDevourerHits",0);
        super.curioSucks(player, stack);
    }
}
