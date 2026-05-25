package com.pyding.vp.item.vestiges;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.entity.CloudEntity;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Catalyst extends Vestige{
    public Catalyst(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(17, ChatFormatting.GREEN, 2, 15, 1, 75, 1, 1, hasDamage, stack);
    }
    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player, SoundRegistry.CATALYST1.get());
        int clouds = 0;
        for(LivingEntity entity: VPUtil.getEntities(player,20,false)){
            if(VPUtil.isProtectedFromHit(player,entity))
                continue;
            List<MobEffectInstance> list = new ArrayList<>(VPUtil.getEffectsHas(entity, false));
            VPUtil.clearEffects(entity,false);
            for(MobEffectInstance effectInstance: list){
                if(effectInstance.getAmplifier() >= 4){
                    Random random = new Random();
                    entity.addEffect(new MobEffectInstance(VPUtil.getRandomEffect(false),10*20,random.nextInt(3)));
                    entity.addEffect(new MobEffectInstance(VPUtil.getRandomEffect(false),10*20,random.nextInt(3)));
                    VPUtil.dealDamage(entity,player,player.damageSources().indirectMagic(entity,player),400,2);
                    if(clouds < 3){
                        CloudEntity cloudEntity = new CloudEntity(player.getCommandSenderWorld(), entity);
                        cloudEntity.teleportTo(entity.getX(),entity.getY()+3,entity.getZ());
                        player.getCommandSenderWorld().addFreshEntity(cloudEntity);
                        clouds++;
                    }
                }
                else {
                    int duration = (int) (effectInstance.getDuration() * 2);
                    entity.addEffect(new MobEffectInstance(effectInstance.getEffect(), duration, effectInstance.getAmplifier() + 5));
                }
            }
            VPUtil.spawnParticles(player, ParticleTypes.BUBBLE,entity.getX(),entity.getY(),entity.getZ(),8,0,-0.5,0);
        }
        if(isStellar(stack))
            player.getPersistentData().putInt("VPDebuffDefence", VPUtil.scalePower(ConfigHandler.catalystDeffence.get(),17,player));
        Random random = new Random();
        int duration = random.nextInt(140)+60;
        int power = random.nextInt(5);
        player.addEffect(new MobEffectInstance(VPUtil.getRandomEffect(true),VPUtil.scalePower(duration*20,17,player),VPUtil.scalePower(power,17,player)));
        PotionContents myContents = stack.get(DataComponents.POTION_CONTENTS);
        if (myContents != null && myContents.potion().isPresent()) {
            List<MobEffectInstance> effects = myContents.potion().get().value().getEffects();
            if (!effects.isEmpty()) {
                for (MobEffectInstance effect : effects) {
                    player.addEffect(new MobEffectInstance(
                            effect.getEffect(),
                            200,
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.isVisible(),
                            effect.showIcon()
                    ));
                }
            }
        }
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player, SoundRegistry.CATALYST2.get());
        Random random = new Random();
        int stolen = 0;
        for(LivingEntity entity: VPUtil.getEntities(player,25,false)){
            if(VPUtil.isProtectedFromHit(player,entity))
                continue;
            List<MobEffectInstance> list = new ArrayList<>();
            for(MobEffectInstance instance: VPUtil.getEffectsHas(entity, true)){
                if(instance.getAmplifier() <= 4 || (isStellar(stack) && instance.getAmplifier() <= ConfigHandler.catalystLvlLimit.get())) {
                    list.add(instance);
                    entity.removeEffect(instance.getEffect());
                    stolen++;
                }
            }
            for(MobEffectInstance effectInstance: list){
                player.addEffect(new MobEffectInstance(effectInstance.getEffect(),effectInstance.getDuration(),effectInstance.getAmplifier()));
                entity.addEffect(new MobEffectInstance(VPUtil.getRandomEffect(false),10*20,random.nextInt(3)));
                addRadiance(3,stack,player);
            }
            VPUtil.spawnParticles(player, ParticleTypes.BUBBLE_COLUMN_UP,entity.getX(),entity.getY(),entity.getZ(),8,0,-0.5,0);
        }
        if(stolen > 0){
            List<MobEffectInstance> list = new ArrayList<>();
            for(MobEffectInstance instance: VPUtil.getEffectsHas(player, true)){
                if(instance.getAmplifier() <= 4 || isStellar(stack)) {
                    list.add(instance);
                    player.removeEffect(instance.getEffect());
                }
            }
            for(MobEffectInstance effectInstance: list){
                player.addEffect(new MobEffectInstance(effectInstance.getEffect(), (int) Math.min((effectInstance.getDuration()*(1+stolen*0.4)+40*20),effectInstance.getDuration()+10*60*20),effectInstance.getAmplifier()));
            }
        }
        super.doUltimate(seconds, player, level, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        int debuffDefence = player.getPersistentData().getInt("VPDebuffDefence");
        if(debuffDefence > 0) {
            if(player.getPersistentData().getLong("VPForbidden") > System.currentTimeMillis()) {
                player.getPersistentData().putLong("VPForbidden",0);
                debuffDefence--;
            }
            if(player.getPersistentData().getLong("VPDeath") > System.currentTimeMillis()) {
                player.getPersistentData().putLong("VPDeath",0);
                debuffDefence--;
            }
            if(VPUtil.getHealDebt(player) > 0) {
                VPUtil.setHealDebt(player,0);
                debuffDefence--;
            }
            for (MobEffectInstance instance : VPUtil.getEffectsHas(player, false)){
                for(LivingEntity livingEntity: VPUtil.getEntitiesAround(player,15,15,15,false)){
                    livingEntity.addEffect(new MobEffectInstance(instance.getEffect(),instance.getDuration(),instance.getAmplifier()));
                }
                player.removeEffect(instance.getEffect());
                debuffDefence--;
            }
            player.getPersistentData().putInt("VPDebuffDefence",debuffDefence);
        }
        super.curioTick(slotContext, stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        String potion = "empty";
        if (contents != null && contents.potion().isPresent())
            potion = Component.translatable(Potion.getName(contents.potion(), "item.minecraft.potion.effect.")).getString();
        tooltip.add(Component.translatable("vp.potion",potion).withStyle(ChatFormatting.DARK_GREEN));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
