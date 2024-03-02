package com.pyding.vp.entity;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.SendEntityNbtToClient;
import com.pyding.vp.util.VPUtil;
import net.minecraft.core.BlockPos;
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
        if(tickCount <= 2 && !getCommandSenderWorld().isClientSide)
            PacketHandler.sendToClients(PacketDistributor.TRACKING_ENTITY.with(() -> this), new SendEntityNbtToClient(getPersistentData(),getId()));
        getPersistentData().putLong("VPAntiTP",System.currentTimeMillis()+10000);
        setGlowingTag(true);
        for(LivingEntity entity: getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(getX()+r,getY()+r,getZ()+r,getX()-r,getY()-r,getZ()-r))){
            if(entity != player) {
                VPUtil.suckToPos(entity,blockPosition(),r/(entity.distanceTo(this)*2));
                //VPUtil.moveSpiral(entity,blockPosition(),1);
                if (entity.distanceTo(this) <= Math.max(10,gravity-10) && player != null)
                VPUtil.dealDamage(entity,player, player.damageSources().fellOutOfWorld(),10/entity.distanceTo(this),3);
            }
        }
        if (!getCommandSenderWorld().isClientSide) {
            if (tickCount > 20 * (gravity+2)) {
                this.discard();
                //MagicManager.spawnParticles(level, ParticleHelper.UNSTABLE_ENDER, getX(), getY() + getRadius(), getZ(), 200, 1, 1, 1, 1, true);
            } else if ((tickCount - 1) % loopSoundDurationInTicks == 0) {
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
