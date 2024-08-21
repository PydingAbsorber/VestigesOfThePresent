package com.pyding.vp.entity;


import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.util.VPUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class HungryOyster extends WaterAnimal {
    public HungryOyster(EntityType<? extends WaterAnimal> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.ARMOR_TOUGHNESS, 10)
                .add(Attributes.KNOCKBACK_RESISTANCE, 100)
                .add(Attributes.FOLLOW_RANGE, 255);
    }


    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(2,new WaterAvoidingRandomStrollGoal(this,1.1));
    }

    @Override
    public void tick() {
        super.tick();
        if(getCommandSenderWorld().isClientSide)
            return;
        int eat = getPersistentData().getInt("VPFish");
        setGlowingTag(false);
        if(tickCount % 2 == 0){
            VPUtil.spawnSphere(this,ParticleTypes.BUBBLE,15,3,0.1f);
            for(LivingEntity entity: VPUtil.getEntitiesAround(this,30,30,30,false)){
                if(entity instanceof TropicalFish fish){
                    if(fish.getPersistentData().getBoolean("VPEat")) {
                        if (this.distanceTo(fish) < 8) {
                            fish.discard();
                            VPUtil.play(this, SoundEvents.GENERIC_EAT);
                            this.getPersistentData().putInt("VPFish", eat + 1);
                            VPUtil.spawnSphere(this, ParticleTypes.BUBBLE, 30, 3, 1f);
                            VPUtil.spawnSphere(this, ParticleTypes.HEART, 10, 4, 0.5f);
                            for (int i = 0; i < 3; i++) {
                                TropicalFish tropicalFish = new TropicalFish(EntityType.TROPICAL_FISH, getCommandSenderWorld());
                                tropicalFish.setPos(getX(), getY(), getZ());
                                VPUtil.teleportRandomly(tropicalFish, 30, true);
                                tropicalFish.getPersistentData().putLong("VPEating", System.currentTimeMillis());
                                getCommandSenderWorld().addFreshEntity(tropicalFish);
                            }
                        }
                    }
                    /*if(fish.getPersistentData().getLong("VPEating") > 0){
                        *//*Vec3 targetVec = Vec3.atCenterOf(fish.blockPosition());
                        Vec3 entityVec = entity.position();
                        Vec3 direction = targetVec.subtract(entityVec);
                        Vec3 pullVelocity = direction.normalize().scale(2);
                        VPUtil.spawnParticle(this,ParticleTypes.GLOW_SQUID_INK,getX(),getY(),getZ(),pullVelocity.x, pullVelocity.y, pullVelocity.z);*//*
                        VPUtil.spawnGuardianParticle(this,fish);
                    }*/
                }
            }
        }
        if(eat >= 3){
            VPUtil.play(this, SoundEvents.PLAYER_LEVELUP);
            for(LivingEntity entity: VPUtil.getEntitiesAround(this,10,10,10,false)){
                if(entity instanceof Player player){
                    player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                        double chance = 0.01+Math.min(0.1,cap.getPearls()/100d);
                        if(getPersistentData().getBoolean("VPCool"))
                            chance *= 5;
                        if(random.nextDouble() < chance)
                            VPUtil.giveStack(new ItemStack(ModItems.PINKY_PEARL.get()),player);
                        else VPUtil.giveStack(new ItemStack(ModItems.HEARTY_PEARL.get()),player);
                    });
                }
            }
            this.discard();
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return super.getHurtSound(p_33034_);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return super.getDeathSound();
    }
}
