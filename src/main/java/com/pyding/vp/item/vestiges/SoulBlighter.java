package com.pyding.vp.item.vestiges;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.mixin.MobEntityVzlom;
import com.pyding.vp.mixin.NearestAttackebleTargetMixinVzlom;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SoulBlighter extends Vestige{
    public SoulBlighter(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(20, ChatFormatting.LIGHT_PURPLE, 2, 30, 1, 330, 15, 300, true, stack);
    }

    public static float getPrice(float souls){
        return (float) (Math.min(Integer.MAX_VALUE,Math.log10(souls)*100)/3+10);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.MAGIC4.get());
        for(LivingEntity entity: VPUtil.ray(player,6,30,true)) {
            if (entity.getPersistentData().getLong("VPAstral") > System.currentTimeMillis()){
                float souls = VPUtil.getTag(stack).getFloat("VPSoulPool")+1;
                float price = getPrice(souls);
                if(souls > price) {
                    VPUtil.dealDamage(entity, player, player.damageSources().magic(), VPUtil.scalePower(125 + price,20,player), 2);
                    VPUtil.addRadiance(SoulBlighter.class,VPUtil.getRadianceSpecial(),player);
                    VPUtil.getTag(stack).putFloat("VPSoulPool", VPUtil.getTag(stack).getFloat("VPSoulPool") - price);
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
        VPUtil.play(player, SoundRegistry.MAGIC3.get());
        CustomData entityDataComponent = stack.get(DataComponents.ENTITY_DATA);
        if (entityDataComponent != null) {
            CompoundTag entityData = entityDataComponent.copyTag();
            stack.remove(DataComponents.ENTITY_DATA);
            BlockPos pos = VPUtil.rayCords(player, level, 6);
            entityData.remove("Pos");
            EntityType.by(entityData).ifPresent(type -> {
                Entity entity = type.create(level);
                if (entity != null) {
                    entity.load(entityData);
                    entity.getPersistentData().putUUID("VPPlayer", player.getUUID());
                    player.getPersistentData().putUUID("VPSlave", entity.getUUID());
                    entity.absMoveTo(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 0, 0);
                    VPUtil.spawnParticles(player, ParticleTypes.SCULK_SOUL, entity.getX(), entity.getY(), entity.getZ(), 8, 0, -0.5, 0);
                    level.addFreshEntity(entity);
                }
            });
            if (isStellar(stack)) {
                var hpBoostId = ResourceLocation.fromNamespaceAndPath("vp", "soulblighter_hp_boost");
                var healthAttr = player.getAttribute(Attributes.MAX_HEALTH);
                if (healthAttr != null) {
                    healthAttr.removeModifier(hpBoostId);
                }
                player.setHealth(player.getMaxHealth());
            }
        } else {
            VPUtil.setHealDebt(player, VPUtil.getHealDebt(player) + player.getMaxHealth() * 20);
            for (LivingEntity entity : VPUtil.ray(player, 4, 30, true)) {
                if ((entity instanceof Player || VPUtil.isProtectedFromHit(player, entity)) &&
                        !(entity.getPersistentData().hasUUID("VPPlayer") && entity.getPersistentData().getUUID("VPPlayer").equals(player.getUUID())))
                    continue;
                double chance = VPUtil.calculateCatchChance(player.getMaxHealth(), entity.getMaxHealth(), entity.getHealth());
                if (entity.getPersistentData().getLong("VPAstral") > 0) chance *= 2;
                if (level.random.nextDouble() < VPUtil.getChance(chance, player)
                        || (entity.getPersistentData().hasUUID("VPPlayer") && entity.getPersistentData().getUUID("VPPlayer").equals(player.getUUID()))
                        || player.isCreative()) {
                    CompoundTag saveTag = new CompoundTag();
                    entity.saveWithoutId(saveTag);
                    saveTag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
                    stack.set(DataComponents.ENTITY_DATA, CustomData.of(saveTag));
                    VPUtil.setNbt(stack,"VPMaxHealth",entity.getMaxHealth());
                    VPUtil.spawnParticles(player, ParticleTypes.SCULK_SOUL, entity.getX(), entity.getY(), entity.getZ(), 8, 0, -0.5, 0);
                    VPUtil.despawn(entity);
                    if (isStellar(stack)) {
                        var hpBoostId = ResourceLocation.fromNamespaceAndPath("vp", "soulblighter_hp_boost");
                        float amount = 1 + entity.getMaxHealth() * 0.3f;
                        var modifier = new AttributeModifier(hpBoostId, amount, AttributeModifier.Operation.ADD_VALUE);
                        player.getAttribute(Attributes.MAX_HEALTH).addTransientModifier(modifier);
                    }
                }
                break;
            }
        }
        super.doUltimate(seconds, player, level, stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        if (VPUtil.getTag(stack).contains("entityData")) {
            EntityType<?> type = VPUtil.entityTypeFromNbt(VPUtil.getTag(stack).getCompound("entityData"));
            tooltip.add(Component.translatable("vp.soul.exist").withStyle(color)
                    .append(type.getDescription()).withStyle(color));
        } else {
            tooltip.add(Component.translatable("vp.soul.empty").withStyle(color));
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if(VPUtil.getTag(stack).contains("entityData") && player.tickCount % 20 == 0){
            VPUtil.regenOverShield(player, (float) ((VPUtil.getTag(stack).getFloat("VPMaxHealth")*0.1f) * ConfigHandler.soulBlighterHeal.get()));
            if(!player.getAttributes().hasModifier(Attributes.MAX_HEALTH, ResourceLocation.fromNamespaceAndPath(VestigesOfThePresent.MODID,"vp:soulblighter_hp_boost"))){
                player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(Attributes.MAX_HEALTH,VPUtil.scalePower(1+VPUtil.getTag(stack).getFloat("VPMaxHealth")*0.3f,20,player), AttributeModifier.Operation.ADD_VALUE,"vp:soulblighter_hp_boost"));
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
