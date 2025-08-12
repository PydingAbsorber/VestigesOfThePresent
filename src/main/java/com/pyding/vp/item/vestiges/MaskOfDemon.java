package com.pyding.vp.item.vestiges;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Random;
import java.util.UUID;

public class MaskOfDemon extends Vestige{
    public MaskOfDemon(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        vestigeNumber = 5;
        color = ChatFormatting.BLUE;
        specialCharges = 1;
        specialCd = 1;
        specialMaxTime = 666;
        ultimateMaxTime = 1;
        ultimateCharges = 1;
        ultimateCd = 60;
        super.dataInit(vestigeNumber, color, specialCharges, specialCd, ultimateCharges, ultimateCd, specialMaxTime, ultimateMaxTime, true, stack);
    }

    private Multimap<Attribute, AttributeModifier> createAttributeMap(Player player, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();
        double attackMultiplier = VPUtil.scalePower(4,5,player);
        double speedMultiplier = VPUtil.scalePower(1,5,player);
        if(isStellar(stack)){
            attackMultiplier *= 1.5;
            speedMultiplier *= 1.5;
        }
        float missingHealth = VPUtil.missingHealth(player);
        if(isStellar(stack))
            missingHealth*=2;
        float attackScale = (float) ((missingHealth * attackMultiplier) + 1)/100;
        float speedScale = (float) ((missingHealth * speedMultiplier) + 1)/100;

        attributesDefault.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("ec62548c-5b26-401e-83fd-693e4aafa532"), "vp:attack_speed_modifier", attackScale, AttributeModifier.Operation.MULTIPLY_TOTAL));
        attributesDefault.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("f4ece564-d2c0-40d2-a96a-dc68b493137c"), "vp:speed_modifier", speedScale, AttributeModifier.Operation.MULTIPLY_BASE));

        return attributesDefault;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if(player.getCommandSenderWorld().isClientSide)
            return;
        if(isSpecialActive(stack)) {
            boolean hurt = false;
            if (player.tickCount % 20 == 0) {
                if(VPUtil.getHealDebt(player) > player.getMaxHealth() * ConfigHandler.COMMON.maskRotAmount.get()/100 + 1){
                    VPUtil.setHealDebt(player,Math.max(0,VPUtil.getHealDebt(player)-player.getMaxHealth() * ConfigHandler.COMMON.maskRotAmount.get()/100 + 1));
                    hurt = true;
                }
                else if (player.getHealth() > player.getMaxHealth() * ConfigHandler.COMMON.maskRotAmount.get()/100 + 1) {
                    VPUtil.dealParagonDamage(player,player,VPUtil.scalePower(player.getMaxHealth() * ConfigHandler.COMMON.maskRotAmount.get()/100,5,player),0,false);
                    hurt = true;
                }
                player.getAttributes().addTransientAttributeModifiers(this.createAttributeMap(player, stack));
            }
            float missingHealth = VPUtil.scalePower(VPUtil.missingHealth(player),5,player);
            if(isStellar(stack))
                missingHealth*=2;
            for(LivingEntity entity: VPUtil.getEntities(player,30,false)){
                if(VPUtil.isProtectedFromHit(player,entity))
                    continue;
                CompoundTag tag = entity.getPersistentData();
                if (tag == null) {
                    tag = new CompoundTag();
                }
                tag.putFloat("VPHealResMask",0-missingHealth);
                if(isStellar(stack))
                    tag.putBoolean("MaskStellar",true);
                if(hurt && isStellar(stack) && player.getHealth() <= player.getMaxHealth()*0.5){
                    VPUtil.dealParagonDamage(entity,player,VPUtil.scalePower(player.getMaxHealth() * ConfigHandler.COMMON.maskRotAmount.get()/100,5,player),1,false);
                    VPUtil.spawnParticles(player, ParticleTypes.DAMAGE_INDICATOR,entity.getX(),entity.getY(),entity.getZ(),1,0,0.1,0);
                }
                entity.getPersistentData().merge(tag);
            }
            player.getPersistentData().putFloat("VPHealResMask",0-missingHealth);
        } else player.getAttributes().removeAttributeModifiers(this.createAttributeMap(player, stack));
        super.curioTick(slotContext, stack);
    }

    @Override
    public int setSpecialActive(long seconds, Player player, ItemStack stack) {
        if(isSpecialActive(stack) && !player.getCommandSenderWorld().isClientSide) {
            if(currentChargeSpecial(stack) > 0) {
                if(!player.getCommandSenderWorld().isClientSide) {
                    setTime(1,stack);
                    setSpecialActive(true,stack);
                    setCdSpecialActive(cdSpecialActive(stack)+specialCd(stack),stack);     //time until cd recharges in seconds*tps
                    Random random = new Random();
                    if(!(VPUtil.getSet(player) == 3 && random.nextDouble() < VPUtil.getChance(0.3,player)) || !(VPUtil.getSet(player) == 6 && random.nextDouble() < VPUtil.getChance(0.5,player)) || random.nextDouble() < VPUtil.getChance(player.getPersistentData().getFloat("VPDepth")/10,player))
                        setCurrentChargeSpecial(currentChargeSpecial(stack) - 1, stack);
                    if(damageType == null)
                        init(stack);
                    this.doSpecial(seconds, player, player.getCommandSenderWorld(), stack);
                    if(VPUtil.hasCurse(player,3))
                        player.getPersistentData().putLong("VPForbidden",System.currentTimeMillis()+3000);
                } else this.localSpecial(player);
            }
            return 0;
        }
        return super.setSpecialActive(seconds, player, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.STOLAS1.get());
        VPUtil.spawnParticles(player, ParticleTypes.ENCHANTED_HIT,8,1,0,-0.1,0,1,false);
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.IMPACT.get());
        float damage = VPUtil.scalePower(300,5,player);
        float healDebt = VPUtil.scalePower(player.getMaxHealth()*3,5,player);
        if(player.getHealth() <= player.getMaxHealth()*0.5) {
            damage *= 2;
            healDebt *= 2;
        }
        VPUtil.setHealDebt(player,VPUtil.getHealDebt(player)+healDebt);
        for (LivingEntity entity: VPUtil.ray(player,8,60,false)){
            if(!VPUtil.isProtectedFromHit(player,entity)) {
                VPUtil.setHealDebt(entity,VPUtil.getHealDebt(entity)+healDebt);
                VPUtil.dealDamage(entity, player, player.damageSources().sonicBoom(player), damage, 3);
                VPUtil.addRadiance(MaskOfDemon.class,VPUtil.getRadianceUltimate(),player);
                VPUtil.spawnParticles(player, ParticleTypes.SONIC_BOOM, entity.getX(), entity.getY(), entity.getZ(), 1, 0, -0.1, 0);
            }
        }
        super.doUltimate(seconds, player, level, stack);
    }
}
