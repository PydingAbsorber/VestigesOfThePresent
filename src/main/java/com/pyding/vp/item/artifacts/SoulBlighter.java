package com.pyding.vp.item.artifacts;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SoulBlighter extends Vestige{
    public SoulBlighter(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(20, ChatFormatting.LIGHT_PURPLE, 2, 30, 1, 300, 15, 300, hasDamage);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level) {
        VPUtil.play(player,SoundRegistry.MAGIC4.get());
        if(player.getHealth() < player.getMaxHealth()*0.5f)
            player.getPersistentData().putLong("VPAstral",System.currentTimeMillis()+specialMaxTime);
        else {
            for(LivingEntity entity: VPUtil.ray(player,6,30,true)) {
                entity.getPersistentData().putLong("VPAstral", System.currentTimeMillis() + specialMaxTime);
                if(Minecraft.getInstance().player != null)
                    VPUtil.spawnParticles(Minecraft.getInstance().player, ParticleTypes.SCULK_SOUL,entity.getX(),entity.getY(),entity.getZ(),8,0,-0.5,0);
                break;
            }
        }
        super.doSpecial(seconds, player, level);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level) {
        ICuriosHelper api = CuriosApi.getCuriosHelper();
        VPUtil.play(player, SoundRegistry.SOUL3.get());
        List list = api.findCurios(player, (stackInSlot) -> {
            if(stackInSlot.getItem() instanceof SoulBlighter blighter) {
                ItemStack stack = stackInSlot;
                if(stack.getOrCreateTag().contains("entityData")){
                    CompoundTag entityData = stack.getTag().getCompound("entityData");
                    stack.getTag().remove("entityData");
                    fuckNbtCheck1 = true;
                    fuckNbtCheck2 = true;
                    BlockPos pos = VPUtil.rayCords(player,level,6);
                    entityData.remove("Pos");
                    CompoundTag wrapper = new CompoundTag();
                    wrapper.put("EntityTag", entityData);
                    EntityType type = VPUtil.entityTypeFromNbt(entityData);
                    Entity entity = type.create(level);
                    entity.load(entityData);
                    entity.getPersistentData().putUUID("VPPlayer",player.getUUID());
                    entity.absMoveTo(pos.getX() + 0.5, pos.getY()+1, pos.getZ() + 0.5, 0, 0);
                    if(Minecraft.getInstance().player != null)
                        VPUtil.spawnParticles(Minecraft.getInstance().player, ParticleTypes.SCULK_SOUL,entity.getX(),entity.getY(),entity.getZ(),8,0,-0.5,0);
                    level.addFreshEntity(entity);
                    if(isStellar)
                        player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("06406f20-b639-471c-aa2f-a251a67fecab"),1+stack.getOrCreateTag().getFloat("VPMaxHealth")*0.3f, AttributeModifier.Operation.ADDITION,"vp:soulblighter_hp_boost"));
                } else {
                    player.getPersistentData().putFloat("HealDebt", player.getPersistentData().getFloat("HealDebt")+player.getMaxHealth()*20);
                    for(LivingEntity entity: VPUtil.ray(player,4,30,true)){
                        double chance = VPUtil.calculateCatchChance(player.getMaxHealth(),entity.getMaxHealth(),entity.getHealth());
                        if(entity.getPersistentData().getLong("VPAstral") > 0)
                            chance *= 2;
                        if(Math.random() < chance
                                || (entity.getPersistentData().hasUUID("VPPlayer") && entity.getPersistentData().getUUID("VPPlayer").equals(player.getUUID()))
                                || player.isCreative()){
                            fuckNbtCheck1 = true;
                            fuckNbtCheck2 = true;
                            stack.getOrCreateTag().put("entityData",entity.serializeNBT());
                            stack.getOrCreateTag().putFloat("VPMaxHealth",entity.getMaxHealth());
                            if(Minecraft.getInstance().player != null)
                                VPUtil.spawnParticles(Minecraft.getInstance().player, ParticleTypes.SCULK_SOUL,entity.getX(),entity.getY(),entity.getZ(),8,0,-0.5,0);
                            entity.remove(Entity.RemovalReason.DISCARDED);
                            if(isStellar)
                                player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("06406f20-b639-471c-aa2f-a251a67fecab"),1+stack.getOrCreateTag().getFloat("VPMaxHealth")*0.3f, AttributeModifier.Operation.ADDITION,"vp:soulblighter_hp_boost"));
                        }
                        break;
                    }
                }
                return true;
            }
            return false;
        });
        super.doUltimate(seconds, player, level);
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

    public boolean fuckNbtCheck1 = false;
    public boolean fuckNbtCheck2 = false;

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        Player player1 = (Player) slotContext.entity();
        if (!fuckNbtCheck1) {
            super.onUnequip(slotContext, newStack, stack);
            if(stack.getOrCreateTag().contains("entityData") && isStellar)
                player1.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player1, Attributes.MAX_HEALTH, UUID.fromString("06406f20-b639-471c-aa2f-a251a67fecab"),1+stack.getOrCreateTag().getFloat("VPMaxHealth")*0.3f, AttributeModifier.Operation.ADDITION,"vp:soulblighter_hp_boost"));
        } else fuckNbtCheck1 = false;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        Player player1 = (Player) slotContext.entity();
        if(!fuckNbtCheck2) {
            super.onEquip(slotContext, prevStack, stack);
        } else fuckNbtCheck2 = false;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player1 = (Player) slotContext.entity();
        if(stack.getOrCreateTag().contains("entityData") && player1.tickCount % 20 == 0){
            VPUtil.regenOverShield(player1,stack.getOrCreateTag().getFloat("VPMaxHealth")*0.1f);
            if(!player1.getAttributes().hasModifier(Attributes.MAX_HEALTH,UUID.fromString("06406f20-b639-471c-aa2f-a251a67fecab"))){
                player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("06406f20-b639-471c-aa2f-a251a67fecab"),1+stack.getOrCreateTag().getFloat("VPMaxHealth")*0.3f, AttributeModifier.Operation.ADDITION,"vp:soulblighter_hp_boost"));
            }
        }
        for (LivingEntity entity: VPUtil.getEntities(player1,50,false)){
            if (!entity.getPersistentData().hasUUID("VPPlayer"))
                continue;
            if(entity.getPersistentData().getUUID("VPPlayer").equals(player1.getUUID())){
                VPUtil.spawnParticles(player1, ParticleTypes.SOUL,entity.getX(),entity.getY(),entity.getZ(),4,0,-0.5,0);
                if(entity instanceof Mob mob) {
                    mob.getBrain().removeAllBehaviors();
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
                        if (!flying && entity.horizontalCollision && entity.isOnGround() && vec31.y > 0) {
                            jumpFlag = true;
                        } else if (!flying && vec31.y > 0) {
                            vec31 = new Vec3(vec31.x, 0, vec31.z);
                        }
                        float yaw = -((float) Mth.atan2(vec31.x, vec31.z)) * (180F / (float) Math.PI);
                        if (vec31.length() > 1F) {
                            vec31 = vec31.normalize();
                            if (!player1.level.isClientSide) {
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
