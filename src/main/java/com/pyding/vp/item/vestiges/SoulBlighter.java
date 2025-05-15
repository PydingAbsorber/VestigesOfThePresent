package com.pyding.vp.item.vestiges;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.mixin.MobEntityVzlom;
import com.pyding.vp.mixin.NearestAttackebleTargetMixinVzlom;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.*;

public class SoulBlighter extends Vestige{
    public SoulBlighter(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(20, ChatFormatting.LIGHT_PURPLE, 2, 30, 1, 300, 15, 300, true, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.MAGIC4.get());
        for(LivingEntity entity: VPUtil.ray(player,6,30,true)) {
            if (entity.getPersistentData().getLong("VPAstral") > System.currentTimeMillis()){
                float souls = stack.getOrCreateTag().getFloat("VPSoulPool");
                float price = (float) (Math.min(Integer.MAX_VALUE,Math.log10(souls)*100)+10);
                if(souls > price) {
                    VPUtil.dealDamage(entity, player, player.damageSources().magic(), 125 + price, 2);
                    stack.getOrCreateTag().putFloat("VPSoulPool", stack.getOrCreateTag().getFloat("VPSoulPool") - price);
                    VPUtil.modifySoulIntegrity(entity, player, (int) -price);
                }
            }
            entity.getPersistentData().putLong("VPAstral", System.currentTimeMillis() + specialMaxTime(stack));
            VPUtil.spawnParticles(player, ParticleTypes.SCULK_SOUL,entity.getX(),entity.getY(),entity.getZ(),8,0,-0.5,0);
            break;
        }
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.MAGIC3.get());
        if(stack.getOrCreateTag().contains("entityData")){
            CompoundTag entityData = stack.getTag().getCompound("entityData");
            stack.getTag().remove("entityData");
            fuckNbt();
            BlockPos pos = VPUtil.rayCords(player,level,6);
            entityData.remove("Pos");
            CompoundTag wrapper = new CompoundTag();
            wrapper.put("EntityTag", entityData);
            EntityType type = VPUtil.entityTypeFromNbt(entityData);
            Entity entity = type.create(level);
            entity.load(entityData);
            entity.getPersistentData().putUUID("VPPlayer",player.getUUID());
            player.getPersistentData().putUUID("VPSlave",entity.getUUID());
            entity.absMoveTo(pos.getX() + 0.5, pos.getY()+1, pos.getZ() + 0.5, 0, 0);
            VPUtil.spawnParticles(player, ParticleTypes.SCULK_SOUL,entity.getX(),entity.getY(),entity.getZ(),8,0,-0.5,0);
            level.addFreshEntity(entity);
            if(isStellar(stack)) {
                player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("55ebb7f1-2368-4b6f-a123-f3b1a9fa30ea"), 1 + stack.getOrCreateTag().getFloat("VPMaxHealth") * 0.3f, AttributeModifier.Operation.ADDITION, "vp:soulblighter_hp_boost"));
                player.setHealth(player.getMaxHealth());
            } setCdUltimateActive((int) (ultimateCd(stack)*0.2),stack);
        } else {
            player.getPersistentData().putFloat("VPHealDebt", player.getPersistentData().getFloat("VPHealDebt")+player.getMaxHealth()*20);
            for(LivingEntity entity: VPUtil.ray(player,4,30,true)){
                if((entity instanceof Player || VPUtil.isProtectedFromHit(player,entity)) && !((entity.getPersistentData().hasUUID("VPPlayer") && entity.getPersistentData().getUUID("VPPlayer").compareTo(player.getUUID()) == 0)))
                    continue;
                double chance = VPUtil.calculateCatchChance(player.getMaxHealth(),entity.getMaxHealth(),entity.getHealth());
                if(entity.getPersistentData().getLong("VPAstral") > 0)
                    chance *= 2;
                Random random = new Random();
                if(random.nextDouble() < VPUtil.getChance(chance,player)
                        || (entity.getPersistentData().hasUUID("VPPlayer") && entity.getPersistentData().getUUID("VPPlayer").compareTo(player.getUUID()) == 0)
                        || player.isCreative()){
                    fuckNbt();
                    stack.getOrCreateTag().put("entityData",entity.serializeNBT());
                    stack.getOrCreateTag().putFloat("VPMaxHealth",entity.getMaxHealth());
                    VPUtil.spawnParticles(player, ParticleTypes.SCULK_SOUL,entity.getX(),entity.getY(),entity.getZ(),8,0,-0.5,0);
                    //entity.remove(Entity.RemovalReason.DISCARDED);
                    VPUtil.despawn(entity);
                    if(isStellar(stack))
                        player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("55ebb7f1-2368-4b6f-a123-f3b1a9fa30ea"),1+stack.getOrCreateTag().getFloat("VPMaxHealth")*0.3f, AttributeModifier.Operation.ADDITION,"vp:soulblighter_hp_boost"));
                }
                break;
            }
        }
        super.doUltimate(seconds, player, level, stack);
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip,
                                TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        if (stack.getOrCreateTag().contains("entityData")) {
            EntityType<?> type = VPUtil.entityTypeFromNbt(stack.getTag().getCompound("entityData"));
            tooltip.add(Component.translatable("vp.soul.exist").withStyle(color)
                    .append(type.getDescription()).withStyle(color));
        } else {
            tooltip.add(Component.translatable("vp.soul.empty").withStyle(color));
        }
    }
    @Override
    public @Nullable CompoundTag getShareTag(ItemStack stack) {
        var tag = super.getShareTag(stack);
        if (tag != null) {
            tag = tag.copy();
            //remove all data that we do not need for client side display
            if (tag.contains("entityData")) {
                var entityData = tag.getCompound("entityData");
                var toRemove = entityData.getAllKeys().stream().filter(key -> !key.equals("id")).toArray(String[]::new);
                Arrays.stream(toRemove).forEach(entityData::remove);
            }
        }
        return tag;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if(stack.getOrCreateTag().contains("entityData") && player.tickCount % 20 == 0){
            VPUtil.regenOverShield(player, (float) ((stack.getOrCreateTag().getFloat("VPMaxHealth")*0.1f) * ConfigHandler.COMMON.soulBlighterHeal.get()));
            if(!player.getAttributes().hasModifier(Attributes.MAX_HEALTH,UUID.fromString("55ebb7f1-2368-4b6f-a123-f3b1a9fa30ea"))){
                player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("55ebb7f1-2368-4b6f-a123-f3b1a9fa30ea"),1+stack.getOrCreateTag().getFloat("VPMaxHealth")*0.3f, AttributeModifier.Operation.ADDITION,"vp:soulblighter_hp_boost"));
            }
        }
        boolean found = false;
        for (LivingEntity entity: VPUtil.getEntities(player,50,false)){
            if (!entity.getPersistentData().hasUUID("VPPlayer"))
                continue;
            if(entity.getPersistentData().getUUID("VPPlayer").equals(player.getUUID())){
                found = true;
                VPUtil.spawnParticles(player, ParticleTypes.SOUL,entity.getX(),entity.getY(),entity.getZ(),4,0,-0.5,0);
                boolean walk = true;
                if(entity instanceof Monster monster){
                    if(monster.getLastHurtByMob() == player) {
                        monster.setLastHurtByMob(null);
                    }
                    if(monster.getLastAttacker() == player)
                        monster.setLastHurtByPlayer(null);
                    if(monster.getTarget() == player)
                        ((MobEntityVzlom) monster).setTarget(null);
                    monster.targetSelector.getAvailableGoals().removeIf(goal ->
                            goal.getGoal() instanceof NearestAttackableTargetGoal<?> targetGoal &&
                                    ((NearestAttackebleTargetMixinVzlom)targetGoal).getTargetType() == Player.class
                    );

                    monster.targetSelector.getAvailableGoals().removeIf(wrapper -> {
                        Goal goal = wrapper.getGoal();
                        if (goal instanceof NearestAttackableTargetGoal<?>) {
                            NearestAttackableTargetGoal<?> natg = (NearestAttackableTargetGoal<?>) goal;
                            return ((NearestAttackebleTargetMixinVzlom)natg).getTargetType() == Player.class;
                        }
                        return false;
                    });

                    monster.targetSelector.addGoal(2, new NearestAttackableTargetGoal<Player>(
                            monster,
                            Player.class,
                            10,
                            true,
                            false,
                            candidate -> {
                                if (monster.getPersistentData().hasUUID("VPPlayer")) {
                                    UUID ownerUUID = monster.getPersistentData().getUUID("VPPlayer");
                                    return !candidate.getUUID().equals(ownerUUID);
                                }
                                return true;
                            }
                    ));

                    if (monster instanceof Warden warden && warden.getTarget() == player) {
                        warden.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
                        warden.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT);
                    }

                    /*Goal goal = new NearestAttackableTargetGoal<>(monster, Monster.class, true);
                    if(!monster.goalSelector.getAvailableGoals().contains(goal))
                        monster.goalSelector.addGoal(0,goal);*/

                    if(player.getLastHurtMob() != null && player.getLastHurtMob() != entity && entity.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) && entity.distanceTo(player) < 10){
                        ((MobEntityVzlom)monster).setTarget(player.getLastHurtMob());
                        monster.setLastHurtByMob(player.getLastHurtMob());
                        continue;
                    }
                    else if(player.getLastHurtByMob() != null && entity.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) && entity.distanceTo(player) < 10){
                        ((MobEntityVzlom)monster).setTarget(player.getLastHurtByMob());
                        monster.setLastHurtByMob(player.getLastHurtByMob());
                        continue;
                    }
                    else if(entity.distanceTo(player) < 10){
                        for(LivingEntity livingEntity: VPUtil.getEntitiesAround(player,30,30,30,false)) {
                            if(livingEntity != player && livingEntity != entity && (livingEntity instanceof Monster || livingEntity instanceof Player)) {
                                ((MobEntityVzlom) monster).setTarget(livingEntity);
                                monster.setLastHurtByMob(livingEntity);
                                walk = false;
                            }
                        }
                    }
                    if(entity.distanceTo(player) > 40){
                        entity.teleportTo(player.getX(),player.getY(),player.getZ());
                    }
                }
                if (walk){
                    Vec3 vec3;
                    if(player.isShiftKeyDown())
                        vec3 = player.getEyePosition();
                    else vec3 = player.getPosition(10);
                    if(entity instanceof Mob mob) {
                        PathNavigation pathNavigation = mob.getNavigation();
                        pathNavigation.moveTo(vec3.x, vec3.y, vec3.z, 1.5);
                        mob.setAggressive(false);
                    } else {
                        boolean flying = entity instanceof FlyingAnimal || entity instanceof FlyingMob;
                        Vec3 vec31 = vec3.subtract(entity.position());
                        boolean jumpFlag = false;
                        if (!flying && entity.horizontalCollision && entity.onGround() && vec31.y > 0) {
                            jumpFlag = true;
                        } else if (!flying && vec31.y > 0) {
                            vec31 = new Vec3(vec31.x, 0, vec31.z);
                        }
                        float yaw = -((float) Mth.atan2(vec31.x, vec31.z)) * (180F / (float) Math.PI);
                        if (vec31.length() > 1F) {
                            vec31 = vec31.normalize();
                            if (!player.getCommandSenderWorld().isClientSide) {
                                entity.setYRot(yaw);
                                entity.setYBodyRot(entity.getYRot());
                            }
                        }
                        Vec3 jumpAdd = vec31.scale(0.15F * 1.5);
                        if (jumpFlag) {
                            jumpAdd = jumpAdd.add(0, 0.6, 0);
                        }
                        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.8F).add(jumpAdd));
                    }
                }
                break;
            }
        }
        if(!found)
            player.getPersistentData().putUUID("VPSlave",player.getUUID());
        super.curioTick(slotContext, stack);
    }
}
