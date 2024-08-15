package com.pyding.vp.entity;


import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.item.HeartyPearl;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.util.VPUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SillySeashell extends WaterAnimal {
    public SillySeashell(EntityType<? extends WaterAnimal> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 1000)
                .add(Attributes.ARMOR, 20)
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.ARMOR_TOUGHNESS, 15)
                .add(Attributes.KNOCKBACK_RESISTANCE, 100)
                .add(Attributes.FOLLOW_RANGE, 255);
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2,new LookAtPlayerGoal(this, Player.class,15f));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if(player.getCommandSenderWorld().isClientSide)
            return super.mobInteract(player,hand);
        ItemStack stack = player.getMainHandItem();
        if(stack.getItem() instanceof HeartyPearl && getPersistentData().getInt("VPPearls") < 3){
            stack.shrink(1);
            getPersistentData().putInt("VPPearls",getPersistentData().getInt("VPPearls")+1);
            VPUtil.syncEntity(this);
        }
        return super.mobInteract(player,hand);
    }

    @Override
    public void tick() {
        super.tick();
        if(getCommandSenderWorld().isClientSide)
            return;
        if(tickCount % 2 == 0){
            VPUtil.spawnSphere(this,ParticleTypes.BUBBLE,15,3,0.1f);
        }
        if(this.getPersistentData().getInt("VPShellHeal") >= 10){
            this.getPersistentData().putInt("VPShellHeal",0);
            this.heal(100);
            VPUtil.addShield(this,100,true);
        }
        if(tickCount % 20 == 0){
            for(LivingEntity entity: VPUtil.getEntitiesAround(this,40,40,40,false)){
                if(entity instanceof Monster monster){
                    monster.goalSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, SillySeashell.class, true));
                    monster.setTarget(this);
                    monster.setLastHurtByMob(this);
                }
            }
        }
        if(getPersistentData().getInt("VPPearls") == 3) {
            if(getPersistentData().getInt("VPWave") == 0) {
                getPersistentData().putInt("VPWave", 1);
                VPUtil.addShield(this, 100, true);
            }
            int wave = getPersistentData().getInt("VPWave");
            if(!getPersistentData().getBoolean("VPActivated")) {
                getPersistentData().putBoolean("VPActivated",true);
                int shellHeals = 0;
                int radius = 25;
                for(Object entity: getCommandSenderWorld().getEntitiesOfClass(ShellHealEntity.class, new AABB(getX()+radius,getY()+radius,getZ()+radius,getX()-radius,getY()-radius,getZ()-radius))){
                    shellHeals++;
                }
                if(shellHeals < 10) {
                    for (int i = 0; i < random.nextInt(7) + 10; i++) {
                        ShellHealEntity shell = new ShellHealEntity(getCommandSenderWorld());
                        shell.setPos(getX(), getY(), getZ());
                        VPUtil.teleportRandomly(shell, 20, true);
                        getCommandSenderWorld().addFreshEntity(shell);
                    }
                }
                List<EntityType> list = new ArrayList<>();
                for(EntityType type: VPUtil.getEntitiesListOfType(MobCategory.MONSTER)){
                    Entity entity = type.create(getCommandSenderWorld());
                    if(entity instanceof Monster monster && monster.getMobType() == MobType.WATER)
                        list.add(type);
                }
                if(wave > 6){
                    for(EntityType type: VPUtil.bossList){
                        Entity entity = type.create(getCommandSenderWorld());
                        if(entity instanceof Monster monster && monster.getMobType() == MobType.WATER)
                            list.add(type);
                    }
                }
                for (int i = 0; i < wave * 3; i++) {
                    Entity entity = list.get(random.nextInt(list.size())).create(getCommandSenderWorld());
                    if (entity == null)
                        continue;
                    entity.setPos(getX(),getY(),getZ());
                    VPUtil.teleportRandomly(entity,15,true);
                    getCommandSenderWorld().addFreshEntity(entity);
                    entity.getPersistentData().putBoolean("VPWaved",true);
                    if(entity instanceof Monster monster){
                        monster.addEffect(new MobEffectInstance(MobEffects.GLOWING,99999));
                        monster.goalSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, SillySeashell.class, true));
                        monster.setTarget(this);
                        monster.setLastHurtByMob(this);
                        if(random.nextDouble() < 0.1 && !VPUtil.isBoss(monster))
                            VPUtil.boostEntity(monster,4,200,100);
                    }
                }
            } else if(getPersistentData().getInt("VPWaveKilled") == wave*3){
                if(wave == 10){
                    dropLoot();
                    return;
                }
                getPersistentData().putInt("VPWaveKilled",0);
                getPersistentData().putInt("VPWave",wave+1);
                getPersistentData().putBoolean("VPActivated",false);
            }
        }
    }

    public void dropLoot(){
        VPUtil.spawnSphere(this,ParticleTypes.BUBBLE,20,3,0.2f);
        VPUtil.spawnSphere(this,ParticleTypes.BUBBLE,20,5,0.5f);
        VPUtil.spawnSphere(this,ParticleTypes.BUBBLE,20,4,1f);
        VPUtil.spawnSphere(this,ParticleTypes.BUBBLE_COLUMN_UP,20,4,0.2f);
        for(LivingEntity entity: VPUtil.getEntitiesAround(this,40,40,40,false)){
            if(entity instanceof Player player){
                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                    if(random.nextDouble() < VPUtil.getChance(0.05,player))
                        VPUtil.giveStack(new ItemStack(ModItems.PEARL.get()),player);
                    VPUtil.giveStack(new ItemStack(ModItems.SEASHELL.get()),player);
                    VPUtil.giveStack(new ItemStack(ModItems.STELLAR.get(),random.nextInt(4)+4),player);
                    for(int i = 0; i < 20; i++){
                        VPUtil.giveStack(VPUtil.getFishDrop(player),player);
                    }
                    List<EntityType> list = new ArrayList<>();
                    if(random.nextDouble() < VPUtil.getChance(0.1,player)){
                        list.addAll(VPUtil.bossList);
                    }
                    else list.addAll(VPUtil.getEntitiesList());
                    if(list.get(random.nextInt(list.size())).create(player.getCommandSenderWorld()) instanceof LivingEntity livingEntity)
                        VPUtil.dropEntityLoot(livingEntity,player,false);
                    cap.setChallenge(23,player);
                });
            }
        }
        this.discard();
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
