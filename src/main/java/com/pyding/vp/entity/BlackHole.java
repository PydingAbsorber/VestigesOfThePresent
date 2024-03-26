package com.pyding.vp.entity;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.network.packets.SendEntityNbtToClient;
import com.pyding.vp.network.packets.SuckPacket;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class BlackHole extends Projectile {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(BlackHole.class, EntityDataSerializers.FLOAT);

    public BlackHole(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    float gravity;
    BlockPos pos;

    public ServerPlayer serverPlayer;
    public BlackHole(Level pLevel, LivingEntity owner, float gravitation, BlockPos position) {
        this(ModEntities.BLACK_HOLE.get(), pLevel);
        gravity = gravitation;
        pos = position;
        setOwner(owner);
        serverPlayer = (ServerPlayer) getOwner();
        getPersistentData().putFloat("VPGravity",gravitation);
    }
    @Override
    public void refreshDimensions() {
        super.refreshDimensions();
        this.setBoundingBox(new AABB(this.getX(), this.getY(), this.getZ(), this.getX() + 1.0, this.getY() + 1.0, this.getZ() + 1.0));
        this.makeBoundingBox();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_RADIUS.equals(pKey)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(pKey);
    }
    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable((1+gravity) * 2.0F, (1+gravity) * 2.0F);
    }

    protected void defineSynchedData() {
        this.getEntityData().define(DATA_RADIUS, 5F);
    }

    @Override
    public void tick() {
        super.tick();
        double r = gravity;
        Player player = (Player) getOwner();
        if(player == null)
            return;
        if(tickCount <= 2 && !getCommandSenderWorld().isClientSide)
            PacketHandler.sendToClients(PacketDistributor.TRACKING_ENTITY.with(() -> this), new SendEntityNbtToClient(getPersistentData(),getId()));
        getPersistentData().putLong("VPAntiTP",System.currentTimeMillis()+10000);
        setGlowingTag(true);
        for(LivingEntity entity: getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(getX()+r,getY()+r,getZ()+r,getX()-r,getY()-r,getZ()-r))){
            if(entity.getUUID() != player.getUUID()) {
                VPUtil.suckToPos(entity,blockPosition(),r/(entity.distanceTo(this)*2));
                if(entity instanceof ServerPlayer serverPlayer1)
                    PacketHandler.sendToClient(new SuckPacket((float) (r/(entity.distanceTo(this)*2)),blockPosition()),serverPlayer1);
                if (entity.distanceTo(this) <= Math.max(10,gravity-10) && tickCount % ConfigHandler.COMMON.blackhole.get() == 0)
                    VPUtil.dealDamage(entity,player, player.damageSources().fellOutOfWorld(),10/entity.distanceTo(this),3);
            }
        }
        if (!getCommandSenderWorld().isClientSide) {
            if (tickCount > 20 * (gravity+2)) {
                this.discard();
                for(LivingEntity entity: getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(getX()+r,getY()+r,getZ()+r,getX()-r,getY()-r,getZ()-r))){
                    if(entity.getUUID() != player.getUUID()) {
                        if (entity.distanceTo(this) <= Math.max(20,gravity-10))
                            VPUtil.dealParagonDamage(entity,player,10/entity.distanceTo(this),3,true);
                    }
                }
                VPUtil.spawnParticles(player, ParticleTypes.EXPLOSION,r,20,0,0,0,0,false);
            }
            if ((tickCount - 1) % loopSoundDurationInTicks == 0) {
                this.playSound(SoundRegistry.BLACK_HOLE.get(), gravity, 1);
            }
        }
    }

    private static final int loopSoundDurationInTicks = 6 * 20;

    @Override
    public boolean displayFireAnimation() {
        return false;
    }
}
