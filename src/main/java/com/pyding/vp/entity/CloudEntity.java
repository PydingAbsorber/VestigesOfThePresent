package com.pyding.vp.entity;

import com.pyding.vp.util.VPUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class CloudEntity extends Projectile {

    public CloudEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public CloudEntity(Level pLevel, LivingEntity owner) {
        this(ModEntities.CLOUD_ENTITY.get(), pLevel);
        setOwner(owner);
    }
    @Override
    public void refreshDimensions() {
        super.refreshDimensions();
        this.setBoundingBox(new AABB(this.getX(), this.getY(), this.getZ(), this.getX() + 1.0, this.getY() + 1.0, this.getZ() + 1.0));
        this.makeBoundingBox();
    }


    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        if(this.getCommandSenderWorld().isClientSide)
            return;
        LivingEntity entity = (LivingEntity) getOwner();
        double r = 10;
        Player player = null;
        if(tickCount % 20 == 0) {
            for(LivingEntity livingEntity: getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(getX()+r,getY()+r,getZ()+r,getX()-r,getY()-r,getZ()-r))){
                if(livingEntity != entity){
                    if(livingEntity instanceof Player player1) {
                        player = player1;
                        if(player.isCreative())
                            continue;
                    }
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON,255,255));
                    livingEntity.hurtTime = 0;
                    VPUtil.equipmentDurability(5,livingEntity);
                }
            }
        }
        if(tickCount % 2 == 0) {
            VPUtil.spawnSphere(this, ParticleTypes.COMPOSTER, 30, 5, 0);
        }
        if(tickCount > 20*20) {
            discard();
            kill();
        }
        if(tickCount % 4 == 0 && player != null)
            VPUtil.suckToPos(this,player.blockPosition(),3);
    }

    private static final int loopSoundDurationInTicks = 6 * 20;

    @Override
    public boolean displayFireAnimation() {
        return false;
    }
}
