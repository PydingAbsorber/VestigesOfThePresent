package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Ball extends Vestige{
    public Ball(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(18, ChatFormatting.AQUA, 5, 25, 2, 30, 1, 1, true);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        VPUtil.play(player, SoundRegistry.BOLT.get());
        for(LivingEntity entity: VPUtil.getEntities(player,5,true)){
            VPUtil.dealDamage(entity,player, DamageSource.LIGHTNING_BOLT,500,2);
        }
        player.hurt(DamageSource.LIGHTNING_BOLT,VPUtil.getAttack(player,true)*500);
        if(level instanceof ServerLevel serverLevel)
            VPUtil.spawnLightning(serverLevel, player.getX(),player.getY(),player.getZ());
        Vec3 motion = new Vec3(0, 1, 0);
        player.lerpMotion(motion.x, motion.y, motion.z);
        if(player instanceof ServerPlayer serverPlayer){
            PacketHandler.sendToClient(new PlayerFlyPacket(1),serverPlayer);
        }
        double originalX = player.getX();
        double originalY = player.getY();
        double originalZ = player.getZ();
        VPUtil.teleportRandomly(player,10);
        double dx = originalX - player.getX();
        double dy = originalY - (player.getY() + player.getEyeHeight());
        double dz = originalZ - player.getZ();
        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float)(Math.atan2(dz, dx) * (180 / Math.PI)) - 90.0F;
        float pitch = (float)-(Math.atan2(dy, distanceXZ) * (180 / Math.PI));
        player.setYRot(yaw);
        player.setXRot(pitch);
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        VPUtil.play(player, SoundRegistry.BOLT.get());
        for(LivingEntity entity: VPUtil.getEntities(player,30,true)){
            float shield = VPUtil.getShield(entity);
            if(shield > 0 || entity.getHealth() < player.getHealth()){
                float damageBonus = 1+(shield*0.001f)+(entity.getArmorCoverPercentage()*2)*(entity.getArmorValue()*0.1f);
                VPUtil.dealDamage(entity,player,DamageSource.LIGHTNING_BOLT,1000*damageBonus,3);
                if(level instanceof ServerLevel serverLevel)
                    VPUtil.spawnLightning(serverLevel, entity.getX(),entity.getY(),entity.getZ());
                if(entity.isInWaterRainOrBubble())
                    VPUtil.dealParagonDamage(entity,player,damageBonus/10,3,true);
            }
        }
        super.doUltimate(seconds, player, level);
    }
}
