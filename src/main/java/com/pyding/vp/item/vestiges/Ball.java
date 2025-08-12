package com.pyding.vp.item.vestiges;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Ball extends Vestige{
    public Ball(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(18, ChatFormatting.AQUA, 5, 25, 2, 30, 1, 1, true, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player, SoundRegistry.BOLT.get());
        for(LivingEntity entity: VPUtil.getEntities(player,5,true)){
            if(!VPUtil.isProtectedFromHit(player,entity)) {
                VPUtil.dealDamage(entity, player, player.damageSources().lightningBolt(), VPUtil.scalePower(500,18,player), 2);
                VPUtil.addRadiance(Ball.class,VPUtil.getRadianceSpecial(),player);
            }
        }
        player.hurt(player.damageSources().lightningBolt(),VPUtil.getAttack(player,true)*5);
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
        VPUtil.teleportRandomly(player,30);
        double dx = originalX - player.getX();
        double dy = originalY - (player.getY() + player.getEyeHeight());
        double dz = originalZ - player.getZ();
        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float)(Math.atan2(dz, dx) * (180 / Math.PI)) - 90.0F;
        float pitch = (float)-(Math.atan2(dy, distanceXZ) * (180 / Math.PI));
        player.setYRot(yaw);
        player.setXRot(pitch);
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player, SoundRegistry.BOLT.get());
        for(LivingEntity entity: VPUtil.getEntities(player,30,true)){
            if(VPUtil.isProtectedFromHit(player,entity))
                continue;
            float shield = VPUtil.getShield(entity) + VPUtil.getOverShield(entity);
            if(shield > 0 || entity.getHealth() < player.getHealth() || entity.getArmorCoverPercentage() > 0 || entity.isInWaterRainOrBubble()){
                float damageBonus = 1+(shield*0.001f)+(entity.getArmorCoverPercentage()*2)*(entity.getArmorValue()*0.1f);
                VPUtil.dealDamage(entity,player,player.damageSources().lightningBolt(),VPUtil.scalePower(1000*damageBonus,18,player),3);
                VPUtil.addRadiance(Ball.class,VPUtil.getRadianceUltimate(),player);
                if(level instanceof ServerLevel serverLevel)
                    VPUtil.spawnLightning(serverLevel, entity.getX(),entity.getY(),entity.getZ());
                if(entity.isInWaterRainOrBubble())
                    VPUtil.dealParagonDamage(entity,player,VPUtil.scalePower(damageBonus/10,18,player),3,true);
            }
        }
        super.doUltimate(seconds, player, level, stack);
    }
}
