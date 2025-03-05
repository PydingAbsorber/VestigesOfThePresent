package com.pyding.vp.item.vestiges;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.entity.BlackHole;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

public class Atlas extends Vestige{
    public Atlas(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(3, ChatFormatting.RED, 2, 10, 1, 30, 1, 4, true, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player, SoundRegistry.GRAVITY.get());
        for(LivingEntity entity: VPUtil.ray(player,6,128,false)){
            //player.getPersistentData().putInt("VPGravity",player.getPersistentData().getInt("VPGravity")+1);
            VPUtil.fall(entity,-10);
            if(entity instanceof ServerPlayer serverPlayer) {
                PacketHandler.sendToClient(new PlayerFlyPacket(2), serverPlayer);
            }
            VPUtil.dealDamage(entity,player, player.damageSources().fall(),50,2);
        }
        VPUtil.rayParticles(player, ParticleTypes.GLOW_SQUID_INK,distance,8,30,0,-1,0,5,false);
        super.doSpecial(seconds, player, level, stack);
    }
    int distance = 30;
    int gravityBonus = 0;
    @Override
    public int setUltimateActive(long seconds, Player player, ItemStack stack) {
        long gravity = Math.max(30,player.getPersistentData().getInt("VPGravity"));
        long stellarBonus = 0;
        if(isStellar(stack)){
            for (LivingEntity entity : VPUtil.ray(player, 8 + gravity, distance, true)) {
                stellarBonus++;
            }
        }
        gravityBonus = (int) stellarBonus;
        stellarBonus = 1 + stellarBonus/10;
        return super.setUltimateActive(seconds*stellarBonus+gravity*1000, player, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        long gravity = Math.min(30,player.getPersistentData().getInt("VPGravity"));
        if(player.getCommandSenderWorld() instanceof ServerLevel serverLevel) {
            BlockPos pos = VPUtil.rayCords(player,serverLevel,10);
            int time = (int) (seconds/50);
            BlackHole blackHole = new BlackHole(serverLevel,player,gravity+1,pos,time);
            blackHole.setPos(pos.getX(),pos.getY(),pos.getZ());
            serverLevel.addFreshEntity(blackHole);
        }
        player.getPersistentData().putInt("VPGravity", 0);
        if(isStellar(stack))
            player.getPersistentData().putInt("VPGravity", Math.min(30, gravityBonus));
        super.doUltimate(seconds, player, level, stack);
    }

}
