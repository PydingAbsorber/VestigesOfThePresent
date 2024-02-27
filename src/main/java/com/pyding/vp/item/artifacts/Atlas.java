package com.pyding.vp.item.artifacts;

import com.pyding.vp.entity.BlackHole;
import com.pyding.vp.entity.ModEntities;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import top.theillusivec4.curios.api.SlotContext;

import java.util.ArrayList;
import java.util.List;

public class Atlas extends Vestige{
    public Atlas(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(3, ChatFormatting.RED, 2, 10, 1, 30, 1, 3, true);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        for(LivingEntity entity: VPUtil.ray(player,6,128,false)){
            if(Math.random() < 0.5)
                player.getPersistentData().putInt("VPGravity",player.getPersistentData().getInt("VPGravity")+1);
            VPUtil.fall(entity,-10);
            if(entity instanceof ServerPlayer serverPlayer) {
                PacketHandler.sendToClient(new PlayerFlyPacket(2), serverPlayer);
                PacketHandler.sendToClient(new PlayerFlyPacket(-2), serverPlayer);
            }
            VPUtil.dealDamage(entity,player, DamageSource.FALL,50,2);
        }
        VPUtil.rayParticles(player, ParticleTypes.GLOW_SQUID_INK,distance,8,1,0,-1,0,5,false);
        super.doSpecial(seconds, player, level);
    }
    public ItemStack stackLocal = null;
    int x = 0;
    int y = 0;
    int z = 0;
    int distance = 30;
    @Override
    public int setUltimateActive(long seconds, Player player) {
        long gravity = Math.max(30,player.getPersistentData().getInt("VPGravity"));
        long stellarBonus = 0;
        if(isStellar){
            for (LivingEntity entity : VPUtil.ray(player, 8 + gravity, distance, true)) {
                stellarBonus++;
            }
        }
        stellarBonus = 1 + stellarBonus/10;
        return super.setUltimateActive(seconds*stellarBonus+gravity*1000, player);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        long gravity = Math.min(30,player.getPersistentData().getInt("VPGravity"));
        if(player.level instanceof ServerLevel serverLevel) {
            BlockPos pos = VPUtil.rayCords(player,serverLevel,10);
            BlackHole blackHole = new BlackHole(serverLevel,player,gravity+1,pos);
            blackHole.setPos(pos.getX(),pos.getY(),pos.getZ());
            serverLevel.addFreshEntity(blackHole);
        }
        super.doUltimate(seconds, player, level);
    }

    @Override
    public void whileUltimate(Player player) {
        super.whileUltimate(player);
    }

    @Override
    public void ultimateEnds(Player player) {
        super.ultimateEnds(player);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(stackLocal == null)
            stackLocal = stack;
        super.curioTick(slotContext, stack);
    }

}
