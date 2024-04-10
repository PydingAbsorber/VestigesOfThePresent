package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
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
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SoulBlighter extends Vestige{
    public SoulBlighter(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(20, ChatFormatting.LIGHT_PURPLE, 2, 30, 1, 300, 15, 300, hasDamage, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.MAGIC4.get());
        if(player.getHealth() < player.getMaxHealth()*0.5f)
            player.getPersistentData().putLong("VPAstral",System.currentTimeMillis()+specialMaxTime(stack));
        else {
            for(LivingEntity entity: VPUtil.ray(player,6,30,true)) {
                entity.getPersistentData().putLong("VPAstral", System.currentTimeMillis() + specialMaxTime(stack));
                VPUtil.spawnParticles(player, ParticleTypes.SCULK_SOUL,entity.getX(),entity.getY(),entity.getZ(),8,0,-0.5,0);
                break;
            }
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
            entity.absMoveTo(pos.getX() + 0.5, pos.getY()+1, pos.getZ() + 0.5, 0, 0);
            VPUtil.spawnParticles(player, ParticleTypes.SCULK_SOUL,entity.getX(),entity.getY(),entity.getZ(),8,0,-0.5,0);
            level.addFreshEntity(entity);
            if(isStellar(stack)) {
                player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("55ebb7f1-2368-4b6f-a123-f3b1a9fa30ea"), 1 + stack.getOrCreateTag().getFloat("VPMaxHealth") * 0.3f, AttributeModifier.Operation.ADDITION, "vp:soulblighter_hp_boost"));
                player.setHealth(player.getMaxHealth());
            } setCdUltimateActive((int) (ultimateCd(stack)*0.2),stack);
        } else {
            player.getPersistentData().putFloat("HealDebt", player.getPersistentData().getFloat("HealDebt")+player.getMaxHealth()*20);
            for(LivingEntity entity: VPUtil.ray(player,4,30,true)){
                if(entity instanceof Player || VPUtil.isProtectedFromHit(player,entity))
                    continue;
                double chance = VPUtil.calculateCatchChance(player.getMaxHealth(),entity.getMaxHealth(),entity.getHealth());
                if(entity.getPersistentData().getLong("VPAstral") > 0)
                    chance *= 2;
                if(Math.random() < chance
                        || (entity.getPersistentData().hasUUID("VPPlayer") && entity.getPersistentData().getUUID("VPPlayer").equals(player.getUUID()))
                        || player.isCreative()){
                    fuckNbt();
                    stack.getOrCreateTag().put("entityData",entity.serializeNBT());
                    stack.getOrCreateTag().putFloat("VPMaxHealth",entity.getMaxHealth());
                    VPUtil.spawnParticles(player, ParticleTypes.SCULK_SOUL,entity.getX(),entity.getY(),entity.getZ(),8,0,-0.5,0);
                    entity.remove(Entity.RemovalReason.DISCARDED);
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
        Player player1 = (Player) slotContext.entity();
        if(stack.getOrCreateTag().contains("entityData") && player1.tickCount % 20 == 0){
            VPUtil.regenOverShield(player1, (float) ((stack.getOrCreateTag().getFloat("VPMaxHealth")*0.1f) * ConfigHandler.COMMON.soulBlighterHeal.get()));
            if(!player1.getAttributes().hasModifier(Attributes.MAX_HEALTH,UUID.fromString("55ebb7f1-2368-4b6f-a123-f3b1a9fa30ea"))){
                player1.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player1, Attributes.MAX_HEALTH, UUID.fromString("55ebb7f1-2368-4b6f-a123-f3b1a9fa30ea"),1+stack.getOrCreateTag().getFloat("VPMaxHealth")*0.3f, AttributeModifier.Operation.ADDITION,"vp:soulblighter_hp_boost"));
            }
        }
        for (LivingEntity entity: VPUtil.getEntities(player1,50,false)){
            if (!entity.getPersistentData().hasUUID("VPPlayer"))
                continue;
            if(entity.getPersistentData().getUUID("VPPlayer").equals(player1.getUUID())){
                VPUtil.spawnParticles(player1, ParticleTypes.SOUL,entity.getX(),entity.getY(),entity.getZ(),4,0,-0.5,0);
                if(entity instanceof Mob mob) {
                    mob.getBrain().removeAllBehaviors();
                    if(mob.getTarget() == player1)
                        mob.setTarget(null);
                }
                if(player1.getLastHurtMob() != null && player1.getLastHurtMob() != entity && entity.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) && entity instanceof Mob mob && entity.distanceTo(player1) < 30){
                    mob.setTarget(player1.getLastHurtMob());
                    mob.setLastHurtByMob(player1.getLastHurtMob());
                }
                else if(player1.getLastHurtByMob() != null && entity.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) && entity instanceof Mob mob && entity.distanceTo(player1) < 30){
                    mob.setTarget(player1.getLastHurtByMob());
                    mob.setLastHurtByMob(player1.getLastHurtByMob());
                }
                else if(VPUtil.getRandomEntityNear(player1,entity,false) != null && VPUtil.getRandomEntityNear(player1,entity,false).getType().getCategory() == MobCategory.MONSTER && entity.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) && entity instanceof Mob mob && entity.distanceTo(player1) < 30){
                    mob.setTarget(VPUtil.getRandomEntityNear(player1,entity,false));
                    mob.setLastHurtByMob(VPUtil.getRandomEntityNear(player1,entity,false));
                }
                else {
                    Vec3 vec3;
                    if(player1.isShiftKeyDown())
                        vec3 = player1.getEyePosition();
                    else vec3 = player1.getPosition(10);
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
                            if (!player1.getCommandSenderWorld().isClientSide) {
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
        super.curioTick(slotContext, stack);
    }
}
