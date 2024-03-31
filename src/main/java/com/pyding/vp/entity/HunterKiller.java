package com.pyding.vp.entity;


import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class HunterKiller extends Monster {
    public Player target;
    public HunterKiller(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    public void setPlayer(Player player) {
        this.target = player;
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 666)
                .add(Attributes.ARMOR, 100)
                .add(Attributes.MOVEMENT_SPEED, 1.5)
                .add(Attributes.ATTACK_DAMAGE, 100)
                .add(Attributes.ARMOR_TOUGHNESS, 100)
                .add(Attributes.KNOCKBACK_RESISTANCE, 100)
                .add(Attributes.JUMP_STRENGTH, 2)
                .add(Attributes.ATTACK_SPEED, 2)
                .add(Attributes.FOLLOW_RANGE, 255);
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0,new FloatGoal(this));
        this.goalSelector.addGoal(1,new LookAtPlayerGoal(this,Player.class,40));
        this.goalSelector.addGoal(2,new WaterAvoidingRandomStrollGoal(this,1.1));
        this.goalSelector.addGoal(3,new RandomLookAroundGoal(this));
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
