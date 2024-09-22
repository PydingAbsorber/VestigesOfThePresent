package com.pyding.vp.event;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.capability.PlayerCapabilityVP;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.commands.VPCommands;
import com.pyding.vp.entity.EasterEggEntity;
import com.pyding.vp.entity.HungryOyster;
import com.pyding.vp.entity.ModEntities;
import com.pyding.vp.entity.SillySeashell;
import com.pyding.vp.item.HeartyPearl;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.accessories.Accessory;
import com.pyding.vp.item.artifacts.*;
import com.pyding.vp.mixin.LootItemEnchantMixin;
import com.pyding.vp.mixin.LootItemMixin;
import com.pyding.vp.mixin.LootPoolMixin;
import com.pyding.vp.mixin.LootRandomItemMixin;
import com.pyding.vp.mixin.LootTableVzlom;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ItemAnimationPacket;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.network.packets.SendEntityNbtToClient;
import com.pyding.vp.network.packets.SendPlayerNbtToClient;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CoralBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

@Mod.EventBusSubscriber(modid = VestigesOfThePresent.MODID)
public class EventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void damageEventLowest(LivingDamageEvent event){
        if(!event.isCanceled()) {
            LivingEntity entity = event.getEntity();
            if(VPUtil.isNpc(entity.getType())) {
                event.setCanceled(true);
                event.setAmount(0);
            }
            CompoundTag tag = entity.getPersistentData();
            VPUtil.damageAdoption(entity, event);
            Random random = new Random();
            if(VPUtil.hasLyra(entity,8))
                event.setAmount(event.getAmount()*0.05f);
            if (entity.getPersistentData().getBoolean("VPKillerQueen")) {
                entity.getPersistentData().putBoolean("VPKillerQueen", false);
                event.setAmount((float) (event.getAmount() * (1+ConfigHandler.COMMON.killerRes.get()/10)));
            }
            if (entity.getPersistentData().getBoolean("VPFlowerStellar") && VPUtil.isDamagePhysical(event.getSource())) {
                event.setAmount(event.getAmount()*0.1f);
                entity.getPersistentData().putBoolean("VPFlowerStellar", false);
            }
            if (event.getSource() == null)
                return;
            if(entity.getPersistentData().getLong("VPAstral") > 0 || (event.getSource().getEntity() != null && event.getSource().getEntity().getPersistentData().getLong("VPAstral") > 0)){
                if(!event.getSource().is(DamageTypes.MAGIC)) {
                    event.setAmount(0);
                } else event.setAmount(event.getAmount()*10);
            }
            if(entity.getPersistentData().getLong("VPWhirlOther") > System.currentTimeMillis()){
                event.setAmount(event.getAmount()*2);
            }
            if(VPUtil.isBoss(entity) && ConfigHandler.COMMON.hardcore.get())
                event.setAmount(event.getAmount()-(float) (event.getAmount()*ConfigHandler.COMMON.absorbHardcore.get()));
            LivingEntity attacker = null;
            if(event.getSource().getEntity() instanceof LivingEntity)
                attacker = (LivingEntity) event.getSource().getEntity();
            if (event.getSource().getEntity() instanceof Player player) {
                /*if(random.nextDouble() < 0.2 && VPUtil.hasStellarVestige(ModItems.KILLER.get(), player) && event.getSource().is(DamageTypeTags.IS_EXPLOSION)){
                    if(random.nextDouble() < 0.5)
                        VPUtil.play(player,SoundRegistry.EXPLODE1.get());
                    else VPUtil.play(player,SoundRegistry.EXPLODE2.get());
                    for(LivingEntity livingEntity: VPUtil.getEntitiesAround(entity,8,8,8,true)){
                        VPUtil.dealDamage(livingEntity,player, player.damageSources().explosion(entity,player),100,3);
                        VPUtil.spawnParticles(player, ParticleTypes.EXPLOSION,8,1,0,0,0,0,false);
                    }
                }*/
                event.setAmount(event.getAmount()*(1+player.getPersistentData().getFloat("VPAcsDamage")/100));
                if (event.getSource().is(DamageTypes.FALL) && player.getPersistentData().getInt("VPGravity") < 30 && random.nextDouble() < VPUtil.getChance(ConfigHandler.COMMON.atlasChance.get(),player))
                    player.getPersistentData().putInt("VPGravity", Math.min(30, player.getPersistentData().getInt("VPGravity") + 1));
                if (VPUtil.hasVestige(ModItems.ANEMOCULUS.get(), player) && !entity.onGround()) {
                    event.setAmount(event.getAmount() * 7);
                    VPUtil.equipmentDurability(10, entity, player, VPUtil.hasStellarVestige(ModItems.ANEMOCULUS.get(), player));
                }
                float curses = player.getPersistentData().getFloat("VPOverdrive");
                if(VPUtil.hasStellarVestige(ModItems.MARK.get(), player) && curses > 0){
                    float VPHealDebt = player.getPersistentData().getFloat("VPHealDebt");
                    VPUtil.dealDamage(entity,player,player.damageSources().magic(),curses*10+VPHealDebt,3,true);
                    player.getPersistentData().putFloat("VPHealDebt",VPHealDebt*0.9f);
                }
                if (VPUtil.hasVestige(ModItems.MASK.get(), player))
                    entity.getPersistentData().putFloat("VPHealDebt", (float) (entity.getPersistentData().getFloat("VPHealDebt") + entity.getPersistentData().getFloat("VPHealDebt") * 0.01));
                if (player.getPersistentData().getInt("VPMadness") > 0 && VPUtil.hasVestige(ModItems.MARK.get(), player)) {
                    //madness duplicate
                    if (entity.getHealth() <= entity.getMaxHealth() * 0.5 && random.nextDouble() < 0.5) {
                        player.getPersistentData().putInt("VPMadness", player.getPersistentData().getInt("VPMadness") - 1);
                    } else {
                        player.getPersistentData().putInt("VPMadness", player.getPersistentData().getInt("VPMadness") - 1);
                    }
                    entity.invulnerableTime = 0;
                    player.attack(entity);
                }
                if(player.getPersistentData().getInt("VPMadness") < 0)
                    player.getPersistentData().putInt("VPMadness",0);
                if (VPUtil.hasStellarVestige(ModItems.MIDAS.get(), player)) {
                    float luck = (float) player.getAttribute(Attributes.LUCK).getValue();
                    event.setAmount(event.getAmount() * (1 + luck / 10));
                }
                if (VPUtil.hasVestige(ModItems.DEVOURER.get(), player) && player.getPersistentData().getInt("VPDevourerHits") > 0
                        && player.getPersistentData().getLong("VPDevourerCd") < System.currentTimeMillis()) {
                    player.getPersistentData().putLong("VPDevourerCd",System.currentTimeMillis()+ConfigHandler.COMMON.devourerCdTime.get());
                    ItemStack stack = VPUtil.getVestigeStack(Devourer.class,player);
                    if(stack != null) {
                        int souls = stack.getOrCreateTag().getInt("VPDevoured");
                        int attributesBetter = 0;
                        if (VPUtil.hasStellarVestige(ModItems.DEVOURER.get(), player))
                            attributesBetter = VPUtil.compareStats(player, entity, true);
                        double chance = ConfigHandler.COMMON.devourerChance.get() * souls;
                        if(VPUtil.isNightmareBoss(entity) && chance > 0.1)
                            chance = 0.1;
                        if (random.nextDouble() < VPUtil.getChance(chance,player))
                            entity.getPersistentData().putInt("VPSoulRotting", entity.getPersistentData().getInt("VPSoulRotting") + 1 + attributesBetter);
                        if (VPUtil.hasStellarVestige(ModItems.DEVOURER.get(), player))
                            entity.getPersistentData().putInt("VPSoulRottingStellar", entity.getPersistentData().getInt("VPSoulRotting"));
                        player.getPersistentData().putInt("VPDevourerHits", player.getPersistentData().getInt("VPDevourerHits") - 1);
                        player.getPersistentData().putInt("VPDevourerShow",entity.getPersistentData().getInt("VPSoulRotting"));
                    }
                }
                int cap = 100;
                if(entity instanceof Player || VPUtil.isBoss(entity))
                    cap += (int) (entity.getMaxHealth()/100);
                if (entity.getPersistentData().getInt("VPSoulRotting") >= cap) {
                    entity.getPersistentData().putInt("VPSoulRotting",0);
                    VPUtil.deadInside(entity, player);
                }
                if (entity.getPersistentData().getLong("VPEnchant") > 0)
                    event.setAmount(event.getAmount() * 1.5f);
                if (VPUtil.hasVestige(ModItems.BALL.get(), player)) {
                    if(event.getAmount() > entity.getHealth() && entity.getPersistentData().getLong("VPBallCd") >= System.currentTimeMillis() && player.getPersistentData().getLong("VPBallCd") >= System.currentTimeMillis()) {
                        int counter = 0;
                        for (LivingEntity livingEntity : VPUtil.getEntitiesAround(entity, 15, 15, 15, false)) {
                            livingEntity.getPersistentData().putLong("VPBallCd", System.currentTimeMillis() + 1000);
                            livingEntity.hurt(event.getSource(), event.getAmount());
                            counter++;
                            if (counter >= 3)
                                break;
                        }
                        player.getPersistentData().putLong("VPBallCd", System.currentTimeMillis() + 1000);
                    }
                    if(event.getSource().is(DamageTypes.LIGHTNING_BOLT)){
                        float shield = VPUtil.getShield(entity);
                        if(shield > 0)
                            entity.getPersistentData().putFloat("VPShield", shield - (float)(shield*ConfigHandler.COMMON.ballShield.get()));
                        float overShield = VPUtil.getOverShield(entity);
                        if(overShield > 0)
                            entity.getPersistentData().putFloat("VPOverShield", overShield - (float)(overShield*ConfigHandler.COMMON.ballOverShield.get()));
                    }
                }
                if (event.getAmount() <= 3) {
                    tag.putBoolean("VPCrownDR", true);
                    double chance = 0.05;
                    if(entity.getPersistentData().getLong("VPDeath") > 0)
                        chance = 0.2;
                    if(random.nextDouble() < VPUtil.getChance(chance,player) && VPUtil.hasVestige(ModItems.CROWN.get(), player)){
                        ItemStack stack = VPUtil.getVestigeStack(Crown.class,player);
                        if(stack != null && stack.getItem() instanceof Crown crown && crown.currentChargeSpecial(stack) < crown.specialCharges(stack))
                            crown.setCurrentChargeSpecial(crown.currentChargeSpecial(stack)+1,stack);
                    }
                }
                else {
                    entity.getPersistentData().putBoolean("VPCrownDR", false);
                }
                if(entity.getPersistentData().getLong("VPBubble") > System.currentTimeMillis() && !event.getSource().is(DamageTypeTags.IS_DROWNING)){
                    entity.getPersistentData().putLong("VPBubble",0);
                    VPUtil.dealDamage(entity,player,player.damageSources().drown(),1000,3);
                    entity.getPersistentData().putLong("VPWet",System.currentTimeMillis()+20000);
                    long time = 10000;
                    if(VPUtil.hasStellarVestige(ModItems.WHIRLPOOL.get(),player)){
                        if(VPUtil.getVestigeStack(Whirlpool.class,player).getItem() instanceof Vestige vestige && vestige.ultimateTimeBonus > 0)
                            time *= (long) vestige.ultimateTimeBonus;
                    }
                    if(VPUtil.hasStellarVestige(ModItems.WHIRLPOOL.get(), player)){
                        DamageSource source = event.getSource();
                        if(source.is(DamageTypes.WITHER))
                            entity.getPersistentData().putLong("VPWhirlHeal",System.currentTimeMillis()+time);
                        else if(source.is(DamageTypes.MAGIC)) {
                            entity.getPersistentData().putLong("VPAntiShield", System.currentTimeMillis() + time);
                            entity.getPersistentData().putLong("VPDeath", System.currentTimeMillis() + time);
                            entity.getPersistentData().putLong("VPAntiTp", System.currentTimeMillis() + time);
                        }
                        else if(source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
                            entity.getPersistentData().putLong("VPWhirlVoid", System.currentTimeMillis() + time);
                            entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity, Attributes.MAX_HEALTH,UUID.fromString("951043f3-5872-4dd5-a99e-7358b6c619d6"),0.2f, AttributeModifier.Operation.MULTIPLY_TOTAL,"vp:whirlHealth"));
                        }
                        else entity.getPersistentData().putLong("VPWhirlOther",System.currentTimeMillis()+time);
                    }
                }
            }
            if(VPUtil.isNightmareBoss(entity)){
                float absorb = entity.getPersistentData().getFloat("VPDreadAbsorb");
                event.setAmount(VPUtil.dreadAbsorbtion(event.getAmount(),absorb));
                entity.getPersistentData().putFloat("VPDreadAbsorb",Math.max(1,absorb-(absorb*0.01f+random.nextInt(10))));
                int type = entity.getPersistentData().getInt("VPBossType");
                if(type == 3 && attacker != null) {
                    attacker.hurt(event.getSource(), event.getAmount() * 0.1f);
                    if(entity.getPersistentData().getLong("VPDef") < System.currentTimeMillis()) {
                        event.setAmount(0);
                        event.setCanceled(true);
                    }
                }
                if(type == 4){
                    int freezeShield = entity.getPersistentData().getInt("VPFreezeShield");
                    if(freezeShield > 0){
                        if(attacker != null && freezeShield == 1)
                            attacker.hurt(entity.damageSources().freeze(),attacker.getMaxHealth()*20);
                        VPUtil.play(entity,SoundEvents.GLASS_BREAK);
                        event.setAmount(0);
                        event.setCanceled(true);
                        entity.getPersistentData().putInt("VPFreezeShield",freezeShield-1);
                    }
                }
            }
            if (entity instanceof Player player) {
                if(attacker != null && VPUtil.isNightmareBoss(attacker)){
                    VPUtil.nightmareDamageEvent(attacker,player,event);
                }
                if(ConfigHandler.COMMON.hardcore.get() && ConfigHandler.COMMON.hardcoreDamage.get() > 0 && ((event.getSource().is(DamageTypes.STARVE) && player.getFoodData().getFoodLevel() <= 1)
                || (event.getSource().is(DamageTypes.DROWN) && player.getAirSupply() <= 1))){
                    VPUtil.dealParagonDamage(player,player,(float)(player.getMaxHealth()*ConfigHandler.COMMON.hardcoreDamage.get()),0,false);
                }
                double damagePlayer = player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
                double entityDamage = 0;
                if(VPUtil.isPoisonedByNightmare(player) && event.getSource().is(DamageTypes.MAGIC)) {
                    if(player.getHealth() > event.getAmount() * 20)
                        event.setAmount(event.getAmount() * 20);
                    else VPUtil.deadInside(player);
                }
                if(attacker != null && attacker.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE))
                    entityDamage = attacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
                if(VPUtil.hasVestige(ModItems.PEARL.get(),player) && event.getSource().is(DamageTypeTags.IS_DROWNING) && random.nextDouble() < VPUtil.getChance(0.2,player))
                    player.setAirSupply(Math.min(player.getMaxAirSupply(),player.getAirSupply()+(int)(player.getMaxAirSupply()*0.1)));
                if(VPUtil.hasVestige(ModItems.CROWN.get(), player)) {
                    if (damagePlayer > entityDamage)
                        event.setAmount(Math.max(0,event.getAmount() - (float) (damagePlayer - entityDamage)));
                    else event.setAmount(Math.max(0,event.getAmount() - (float) (entityDamage - damagePlayer)));
                }
                if (player.getPersistentData().getBoolean("VPMarkUlt")) {
                    player.getPersistentData().putFloat("VPDamageReduced", event.getAmount() + player.getPersistentData().getFloat("VPDamageReduced"));
                    event.setAmount(0);
                }
                if (VPUtil.hasVestige(ModItems.EARS.get(), player)) {
                    float armor = (float) player.getArmorValue();
                    float chance = (Math.min(ConfigHandler.COMMON.catEvadeCap.get(), armor) / 100);
                    if (VPUtil.hasStellarVestige(ModItems.EARS.get(), player)) {
                        event.setAmount(event.getAmount() - (event.getAmount() * chance));
                    }
                }
                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                    if (event.getSource().is(DamageTypeTags.IS_EXPLOSION))
                        cap.failChallenge(18, player);
                });
                if (VPUtil.hasVestige(ModItems.ARMOR.get(), player)) {
                    ItemStack stack = VPUtil.getVestigeStack(Armor.class,player);
                    event.setAmount(Math.max(0, event.getAmount() - (ConfigHandler.COMMON.armorAbsorbBase.get() + stack.getOrCreateTag().getFloat("VPArmor"))));
                    stack.getOrCreateTag().putFloat("VPArmor", stack.getOrCreateTag().getFloat("VPArmor") + (float)(event.getAmount()*ConfigHandler.COMMON.armorAbsorbPercent.get()));
                }
                VPUtil.printDamage(player,event);
            }
            if (event.getSource().getEntity() instanceof Player player) {
                if (VPUtil.hasStellarVestige(ModItems.BOOK.get(), player)) {
                    if (player.getPersistentData().getInt("VPBookDamage") < 10 && event.getAmount() <= 5) {
                        player.getPersistentData().putFloat("VPBookDamage", player.getPersistentData().getInt("VPBookDamage") + event.getAmount());
                        player.getPersistentData().putInt("VPBookHits", player.getPersistentData().getInt("VPBookHits") + 1);
                    } else {
                        VPUtil.dealDamage(entity,player,player.damageSources().magic(),event.getAmount() + player.getPersistentData().getInt("VPBookDamage"),3,true);
                        player.getPersistentData().putInt("VPBookDamage", 0);
                        player.getPersistentData().putInt("VPBookHits", 0);
                        VPUtil.play(player,SoundRegistry.MAGIC_EFFECT1.get());
                    }
                }
                VPUtil.printDamage(player,event);
            }
            if (event.getAmount() <= 0)
                return;
            float overShield = VPUtil.getOverShield(entity);
            if (overShield > 0) {
                if (event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) || event.getSource().is(DamageTypes.MAGIC))
                    event.setAmount(event.getAmount() * 0.1f);
                float shieldDamage = event.getAmount();
                if(entity.getPersistentData().getLong("VPWhirlParagon") > System.currentTimeMillis())
                    shieldDamage *= 8;
                entity.getPersistentData().putFloat("VPOverShield", Math.max(0, entity.getPersistentData().getFloat("VPOverShield") - shieldDamage));
                if (event.getSource().getEntity() instanceof Player player) {
                    player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                        cap.setChallenge(19, cap.getChallenge(19) + (int) Math.min(overShield, event.getAmount()), player);
                    });
                }
                event.setAmount(0);
                if (tag.getFloat("VPOverShield") <= 0) {
                    VPUtil.play(entity, SoundRegistry.OVERSHIELD_BREAK.get());
                    if(entity.getPersistentData().getInt("VPBossType") == 4 && VPUtil.isNightmareBoss(entity) && attacker != null)
                        attacker.hurt(entity.damageSources().freeze(),attacker.getMaxHealth()*20);
                }
            }
            float shield = entity.getPersistentData().getFloat("VPShield");
            float damage = event.getAmount();
            if (shield > damage) {
                if (event.getSource().getEntity() instanceof Player player) {
                    player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                        cap.setChallenge(19, cap.getChallenge(19) + (int) Math.min(shield, event.getAmount()), player);
                    });
                }
                entity.getPersistentData().putFloat("VPShield", shield - damage);
                event.setAmount(0);
            } else {
                if (event.getSource().getEntity() instanceof Player player) {
                    player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                        cap.setChallenge(19, cap.getChallenge(19) + (int) Math.min(shield, event.getAmount()), player);
                    });
                }
                event.setAmount(damage - shield);
                if (shield > 0)
                    VPUtil.play(entity, SoundRegistry.SHIELD_BREAK.get());
                if (entity.getPersistentData().getLong("VPFlowerStellar") > 0 && event.getSource().getEntity() instanceof LivingEntity livingEntity)
                    livingEntity.hurt(entity.damageSources().magic(), entity.getPersistentData().getFloat("VPShieldInit"));
                entity.getPersistentData().putFloat("VPShield", 0);
                entity.getPersistentData().putFloat("VPShieldInit", 0);
                if(entity.getPersistentData().getInt("VPBossType") == 4 && VPUtil.isNightmareBoss(entity) && attacker != null)
                    attacker.hurt(entity.damageSources().freeze(),attacker.getMaxHealth()*20);
            }
            entity.getPersistentData().putBoolean("VPCrownHit",false);
        } else {
            event.getEntity().getPersistentData().putBoolean("VPCrownDR", true);
        }
        if(event.getAmount() > 0 && event.getEntity() instanceof Player player && event.getSource().getEntity() != null && event.getSource().getEntity() instanceof LivingEntity attacker && VPUtil.hasVestige(ModItems.RUNE.get(), player) && VPUtil.isBoss(attacker) && (VPUtil.getShield(player) <= 0 || VPUtil.getOverShield(player) <= 0)){
            if(VPUtil.getShield(player) <= 0)
                VPUtil.addShield(player,event.getAmount()*0.5f,false);
            if(VPUtil.getOverShield(player) <= 0)
                VPUtil.addOverShield(player,event.getAmount()*0.5f,true);
        }
        LivingEntity entity = event.getEntity();
        if(entity instanceof Player player)
            VPUtil.sync(player);
        else VPUtil.syncEntity(event.getEntity());
    }
    @SubscribeEvent
    public void totemUsing(LivingUseTotemEvent event){
        LivingEntity entity = event.getEntity();
        if(entity.getPersistentData().getLong("VPDeath") > 0)
            event.setCanceled(true);
    }
    @SubscribeEvent
    public void attackEvent(LivingAttackEvent event){
        LivingEntity victim = event.getEntity();
        Random random = new Random();
        if(VPUtil.isNpc(victim.getType())) {
            event.setCanceled(true);
            return;
        }
        if(event.getSource().getEntity() instanceof Player player){
            if(VPUtil.isProtectedFromHit(player,victim)) {
                event.setCanceled(true);
                return;
            }
        }
        if(victim instanceof Player player){
            if(VPUtil.hasVestige(ModItems.EARS.get(), player)) {
                float armor = (float) player.getArmorValue();
                float exp = (float) player.experienceLevel;
                float specialAddition = (float)player.getAttribute(Attributes.MOVEMENT_SPEED).getValue()-1;
                if(player.getPersistentData().getBoolean("VPEarsSpecial") && specialAddition > 0.05f)
                    armor = armor+(specialAddition/0.05f);
                float chance = (Math.min(69, armor) / 100);
                float secondChance = (Math.min(90, exp / 10) / 100);
                if (random.nextDouble() < VPUtil.getChance(chance,player)) {
                    if(player.getPersistentData().getLong("VPSoundCd") < System.currentTimeMillis())
                        VPUtil.play(player,SoundEvents.ENDER_DRAGON_FLAP);
                    player.getPersistentData().putLong("VPSoundCd",System.currentTimeMillis()+1000);
                    event.setCanceled(true);
                    return;
                } else if (player.getPersistentData().getBoolean("VPEarsUlt") && random.nextDouble() < VPUtil.getChance(secondChance,player)) {
                    if(player.getPersistentData().getLong("VPSoundCd") < System.currentTimeMillis())
                        VPUtil.play(player,SoundEvents.ENDER_DRAGON_FLAP);
                    player.getPersistentData().putLong("VPSoundCd",System.currentTimeMillis()+1000);
                    event.setCanceled(true);
                    return;
                }
            }
            if(VPUtil.hasVestige(ModItems.WHIRLPOOL.get(), player)){
                int block = player.getPersistentData().getInt("VPWhirlpool");
                if(block > 0){
                    player.getPersistentData().putInt("VPWhirlpool",block-1);
                    VPUtil.play(player,SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_INSIDE);
                    event.setCanceled(true);
                    return;
                }
            }
            if (VPUtil.hasVestige(ModItems.CHAOS.get(), player)) {
                double passiveChance = ConfigHandler.COMMON.chaosChance.get();
                boolean hasStellar = VPUtil.hasStellarVestige(ModItems.CHAOS.get(), player);
                if(hasStellar)
                    passiveChance += player.getAttribute(Attributes.LUCK).getValue() / 100;
                float amount = event.getAmount();
                DamageSource damageSource = event.getSource();
                if (random.nextDouble() < VPUtil.getChance(Math.min(0.99,passiveChance),player)) {
                    if(random.nextDouble() < 0.5){
                        if (random.nextDouble() < 0.5) {
                            if(player.getPersistentData().getFloat("VPHealDebt") == 0)
                                amount = (event.getAmount() + random.nextInt(ConfigHandler.COMMON.chaosDamageCap.get()));
                        } else amount = (event.getAmount() - random.nextInt(ConfigHandler.COMMON.chaosDamageCap.get()));
                        event.setCanceled(true);
                        player.hurt(damageSource,amount);
                        return;
                    } else {
                        player.invulnerableTime = 0;
                        damageSource = VPUtil.randomizeDamageType(player);
                        player.hurt(damageSource, event.getAmount());
                        event.setCanceled(true);
                        if (random.nextDouble() < VPUtil.getChance(0.3,player) && hasStellar) {
                            for (LivingEntity livingEntity : VPUtil.getEntities(player, 30, false)) {
                                livingEntity.hurt(damageSource, amount);
                                livingEntity.getPersistentData().putFloat("VPHealDebt",event.getAmount()*0.1f);
                            }
                        }
                        return;
                    }
                }
                double chance = ConfigHandler.COMMON.chaosChance.get()*10 + random.nextInt(90);
                if (random.nextDouble() < VPUtil.getChance(chance / 100,player) && event.getSource().getEntity() != null && event.getSource().getEntity() instanceof LivingEntity livingEntity && livingEntity != player) {
                    if (player.getPersistentData().getInt("VPChaos") > 0) {
                        player.getPersistentData().putInt("VPChaos", player.getPersistentData().getInt("VPChaos"));
                        livingEntity.hurt(damageSource, amount);
                        livingEntity.getPersistentData().putFloat("VPHealDebt",event.getAmount()*0.1f);
                        event.setCanceled(true);
                        return;
                    }
                }
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.LOW,receiveCanceled = true)
    public void deathEventLowest(LivingDeathEvent event){
        Random random = new Random();
        if(event.getEntity().getPersistentData().getLong("VPQueenDeath") <= System.currentTimeMillis()){
            event.getEntity().getPersistentData().putLong("VPQueenDeath",-1);
        }
        if(!event.isCanceled()) {
            LivingEntity entity = event.getEntity();
            entity.getPersistentData().putInt("VPMirnoeReshenie",0);
            if(VPUtil.isNightmareBoss(entity) && (entity.getHealth() > 0 || VPUtil.getOverShield(entity) > 0) && entity.getPersistentData().getInt("VPSoulRotting") < 100){
                event.setCanceled(true);
                entity.setHealth(entity.getMaxHealth());
            }
            if(entity.getPersistentData().getBoolean("VPSummoned")){
                entity.discard();
                return;
            }
            if(entity.getPersistentData().getBoolean("VPWaved")){
                for(LivingEntity livingEntity: VPUtil.getEntitiesAround(entity,40,40,40,false)) {
                    if (livingEntity instanceof SillySeashell sillySeashell) {
                        sillySeashell.getPersistentData().putInt("VPWaveKilled",sillySeashell.getPersistentData().getInt("VPWaveKilled")+1);
                        for(LivingEntity living: VPUtil.getEntitiesAround(sillySeashell,40,40,40,false)) {
                            if(living.getPersistentData().getBoolean("VPWaved"))
                                living.discard();
                        }
                    }
                }
                entity.discard();
                return;
            }
            if (event.getSource().getEntity() instanceof Player player) {
                if(VPUtil.isProtectedFromHit(player,entity))
                    event.setCanceled(true);
                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                    CompoundTag tag = entity.getPersistentData();
                    if (player.getCommandSenderWorld().dimension() == Level.NETHER && player.getHealth() <= 1.5) {
                        if(entity.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) && player.getAttribute(Attributes.ATTACK_DAMAGE).getValue() < entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue())
                            challange.setChallenge(5, challange.getChallenge(5) + 1, player);
                        else if(entity.getAttributes().hasAttribute(Attributes.MAX_HEALTH) && player.getAttribute(Attributes.MAX_HEALTH).getValue() < entity.getAttribute(Attributes.MAX_HEALTH).getValue())
                            challange.setChallenge(5, challange.getChallenge(5) + 1, player);
                    }
                    if (entity.getType().getCategory() == MobCategory.MONSTER && !VPUtil.isBoss(entity))
                        challange.addMonsterKill(entity.getType().toString(), player);
                    if (entity.getType().getCategory() == MobCategory.CREATURE)
                        challange.addMobTame(entity.getType().toString(), player);
                    if (VPUtil.isBoss(entity))
                        challange.addBossKill(entity.getType().toString(), player);
                    if(!player.onGround() && !entity.onGround() && !entity.isInWater())
                        challange.addCreatureKilledAir(event.getEntity().getType().toString(), player);
                    if(event.getSource().is(DamageTypeTags.IS_EXPLOSION))
                        challange.setChallenge(4,player);
                    if(entity instanceof EnderMan && player.getMainHandItem().getItem() instanceof TieredItem tieredItem)
                        challange.addTool(tieredItem.toString(),player);
                    if((entity instanceof Player || entity instanceof Warden) && VPUtil.getCurseAmount(player) > 10)
                        challange.setChallenge(12,10,player);
                    /*if(VPUtil.isBoss(entity))
                        challange.addDamageDo(event.getSource(),player);*/
                    if(entity.getType().getDescriptionId().equals(challange.getRandomEntity())) {
                        challange.setChallenge(14, player);
                        challange.setChaosTime(System.currentTimeMillis(),player);
                        challange.setRandomEntity(VPUtil.getRandomEntity(),player);
                    }
                });
                int count = 0;
                if(VPUtil.hasVestige(ModItems.PRISM.get(), player) && entity.getPersistentData().getLong("VPPrismBuff") > 0){
                    ItemStack stack2 = VPUtil.getVestigeStack(Prism.class,player);
                    if(stack2 != null && stack2.getItem() instanceof Prism prism) {
                        double chance = stack2.getOrCreateTag().getInt("VPPrismKill")*ConfigHandler.COMMON.prismChance.get();
                        chance /= 100; 
                        VPUtil.dropEntityLoot(entity, player,true);
                        count++;
                        while (random.nextDouble() < VPUtil.getChance(chance,player)) {
                            VPUtil.dropEntityLoot(entity, player,true);
                            chance /= 10;
                            count++;
                        }
                        prism.fuckNbt();
                        stack2.getOrCreateTag().putInt("VPPrismKill", stack2.getOrCreateTag().getInt("VPPrismKill") + 1);
                    }
                }
                if(VPUtil.isNightmareBoss(entity)){
                    for(LivingEntity livingEntity: VPUtil.getEntitiesAround(entity,20,20,20,false)){
                        if(livingEntity instanceof Player killer){
                            int min = ConfigHandler.COMMON.nightmareLootMin.get();
                            int max = ConfigHandler.COMMON.nightmareLoot.get();
                            int amount = random.nextInt(Math.max(1,max-min))+min;
                            for(int i = 0;i < amount;i++) {
                                VPUtil.dropEntityLoot(entity, killer,false);
                            }
                            min = ConfigHandler.COMMON.nightmareFragsMin.get();
                            max = ConfigHandler.COMMON.nightmareFrags.get();
                            amount = random.nextInt(Math.max(1,max-min))+min;
                            VPUtil.giveStack(new ItemStack(ModItems.STELLAR.get(),amount),killer);
                            if(random.nextDouble() < VPUtil.getChance(ConfigHandler.COMMON.nightmareRefresherChance.get(),player))
                                VPUtil.giveStack(new ItemStack(ModItems.REFRESHER.get()),killer);
                            if(random.nextDouble() < VPUtil.getChance(ConfigHandler.COMMON.nightmareBoxChance.get(),player)) {
                                min = ConfigHandler.COMMON.nightmareBoxesMin.get();
                                max = ConfigHandler.COMMON.nightmareBoxes.get();
                                amount = random.nextInt(Math.max(1,max-min))+min;
                                if(random.nextDouble() < 0.5){
                                    if(random.nextDouble() < 0.5)
                                        VPUtil.giveStack(new ItemStack(ModItems.BOX_EGGS.get(),amount),killer);
                                    else VPUtil.giveStack(new ItemStack(ModItems.BOX.get(),amount),killer);
                                } else VPUtil.giveStack(new ItemStack(ModItems.BOX_SAPLINGS.get(),amount),killer);
                            }
                            VPUtil.giveStack(new ItemStack(ModItems.NIGHTMARESHARD.get()),player);
                        }
                    }
                }
                if(VPUtil.isEasterEvent()) {
                    double chanceEgg = 0.0001;
                    double add = 1+ConfigHandler.COMMON.easterChance.get()/100d;
                    if (VPUtil.isBoss(entity))
                        chanceEgg *= 100;
                    if(count > 0)
                        chanceEgg *= count*10;
                    chanceEgg *= add;
                    if (random.nextDouble() < VPUtil.getChance(chanceEgg,player) && player.getCommandSenderWorld() instanceof ServerLevel serverLevel) {
                        BlockPos pos = entity.getOnPos();
                        EasterEggEntity easterEggEntity = new EasterEggEntity(serverLevel);
                        easterEggEntity.setPos(pos.getX(),pos.getY(),pos.getZ());
                        serverLevel.addFreshEntity(easterEggEntity);
                        VPUtil.play(player,SoundEvents.EGG_THROW);
                    }
                }
                ItemStack stack = VPUtil.getVestigeStack(Midas.class,player);
                if(stack != null && stack.getItem() instanceof Midas midas) {
                    int kill = 1;
                    if (player.getPersistentData().getFloat("VPMidasTouch") > 0) {
                        player.getPersistentData().putFloat("VPMidasTouch", player.getPersistentData().getFloat("VPMidasTouch") - 1);
                        if (VPUtil.isBoss(entity))
                            kill *= 10;
                        else kill *= 2;
                    }
                    midas.fuckNbt();
                    stack.getOrCreateTag().putInt("VPKills", stack.getOrCreateTag().getInt("VPKills") + kill);
                }
                if(VPUtil.isBoss(entity) && ConfigHandler.COMMON.hardcore.get() && !VPUtil.isNightmareBoss(entity)) {
                    VPUtil.giveStack(new ItemStack(ModItems.SHARD.get()),player);
                }
                if(VPUtil.hasVestige(ModItems.DEVOURER.get(), player) && VPUtil.isBoss(entity)){
                    ItemStack stack3 = VPUtil.getVestigeStack(Devourer.class,player);
                    if(stack3 != null && stack3.getItem() instanceof Devourer devourer) {
                        devourer.fuckNbt();
                        stack3.getOrCreateTag().putInt("VPDevoured", stack3.getOrCreateTag().getInt("VPDevoured") + 1);
                    }
                }
                if(VPUtil.hasVestige(ModItems.CROWN.get(), player) && entity.getPersistentData().getBoolean("VPCrownHitDeath")) {
                    VPUtil.addShield(player, (entity.getMaxHealth() / 100 * ConfigHandler.COMMON.crownShield.get()), true);
                    VPUtil.addOverShield(player, (entity.getMaxHealth() / 100 * ConfigHandler.COMMON.crownShield.get()), true);
                }
                if (VPUtil.hasVestige(ModItems.WHIRLPOOL.get(), player) && event.getSource().is(DamageTypeTags.IS_DROWNING)){
                    VPUtil.addShield(player,20,true);
                    VPUtil.addOverShield(player,20,true);
                }

            }
            if (event.getEntity() instanceof Player player){
                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                    challange.addDamageDie(event.getSource().getMsgId(),player);
                    challange.failChallenge(13,player);
                    challange.failChallenge(18,player);
                });
                if(VPUtil.hasVestige(ModItems.BOOK.get(), player)){
                    ItemStack stack = VPUtil.getVestigeStack(Book.class,player);
                    if(stack.getItem() instanceof Book book) {
                        if (book.isUltimateActive(stack)) {
                            if (event.getSource().getEntity() instanceof LivingEntity dealer) {
                                VPUtil.enchantCurseAll(dealer);
                            }
                        } else if (book.ultimateCharges(stack) != book.currentChargeUltimate(stack)) {
                            VPUtil.enchantCurseAll(player);
                        }
                    }
                }
                if(VPUtil.hasVestige(ModItems.ARMOR.get(), player)){
                    ItemStack stack = VPUtil.getVestigeStack(Armor.class,player);
                    stack.getOrCreateTag().putFloat("VPArmor",0);
                }
                if(VPUtil.hasVestige(ModItems.KILLER.get(), player) && player.getPersistentData().getLong("VPQueenDeath") >= 0 && player.getPersistentData().getLong("VPDeath") == 0){
                    int percent = 800;
                    if(player.getPersistentData().getLong("VPQueenDeath") > 0){
                        percent = 8000;
                        player.setHealth(player.getMaxHealth()/2);
                        event.setCanceled(true);
                        player.getPersistentData().putLong("VPQueenDeath",-1);
                        ItemStack stack = VPUtil.getVestigeStack(Killer.class,player);
                        if(player instanceof ServerPlayer serverPlayer)
                            PacketHandler.sendToClient(new ItemAnimationPacket(stack),serverPlayer);
                        VPUtil.play(player, SoundEvents.TOTEM_USE);
                        VPUtil.spawnParticles(player, ParticleTypes.GLOW, player.getX(), player.getY(), player.getZ(), 8, 0, 0, 0);
                    }
                    boolean stellar = VPUtil.hasStellarVestige(ModItems.KILLER.get(), player);
                    for(LivingEntity livingEntity: VPUtil.getEntitiesAround(player,20,20,20,false)){
                        VPUtil.dealDamage(livingEntity,player, player.damageSources().explosion(livingEntity,player),percent,3);
                        if(stellar){
                            livingEntity.getPersistentData().putLong("VPAntiShield",3*60*1000+System.currentTimeMillis());
                            livingEntity.getPersistentData().putLong("VPAntiTP",3*60*1000+System.currentTimeMillis());
                        }
                    }
                }
            }
            Player playerNear = entity.getCommandSenderWorld().getNearestPlayer(entity,20);
            if(playerNear != null && VPUtil.hasVestige(ModItems.CATALYST.get(), playerNear)) {
                List<MobEffectInstance> effectList = new ArrayList<>(VPUtil.getEffectsHas(entity,false));
                for (LivingEntity iterator : VPUtil.getEntitiesAround(entity, 10, 10, 10, false)) {
                    for(MobEffectInstance instance: effectList){
                        iterator.addEffect(instance);
                        VPUtil.spawnParticles(playerNear, ParticleTypes.BUBBLE_POP,iterator.getX(),iterator.getY(),iterator.getZ(),8,0,-0.5,0);
                    }
                }
            }
            for(LivingEntity livingEntity: VPUtil.getEntitiesAround(entity,20,20,20,true)) {
                if (livingEntity instanceof Player bard && VPUtil.hasVestige(ModItems.LYRA.get(), bard)) {
                    ItemStack stack = VPUtil.getVestigeStack(Lyra.class, bard);
                    if (stack != null && stack.getItem() instanceof Vestige vestige) {
                        if (vestige.isUltimateActive(stack) && bard.getPersistentData().getFloat("VPHealDebt") <= 0 && entity.getPersistentData().getLong("VPDeath") == 0) {
                            bard.getPersistentData().putFloat("VPHealDebt", bard.getPersistentData().getFloat("VPHealDebt") + entity.getMaxHealth() * 3);
                            entity.setHealth(entity.getMaxHealth());
                            event.setCanceled(true);
                        }
                    }
                }
            }
            if(event.getSource().getEntity() == null && entity instanceof Warden){
                    for(LivingEntity livingEntity: VPUtil.getEntitiesAround(event.getEntity(),20,20,20,false)){
                        if(livingEntity instanceof Player player){
                            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challenge -> {
                                challenge.addDamageDo(event.getSource(),player);
                            });
                        }
                    }
            }
        } else {
            LivingEntity entity = event.getEntity();
            if(entity.getPersistentData().getInt("VPMirnoeReshenie") > 10) {
                VPUtil.setHealth(entity,0);
                VPUtil.setDead(entity, entity.damageSources().genericKill());
            }
            else if(entity.getPersistentData().getLong("VPDeath") > 0){
                entity.setHealth(0);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST,receiveCanceled = true)
    public void onLootDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if (event.getSource().getEntity() instanceof Player player && !event.isCanceled() && player.getServer() != null){
            List<Item> rareDrops = new ArrayList<>();
            LootTable lootTable = player.getServer().getLootData().getLootTable(entity.getLootTable());
            for(LootPool pool: ((LootTableVzlom)lootTable).getPools()){
                for (LootPoolEntryContainer entry : ((LootPoolMixin) pool).getEntries()) {
                    if (entry instanceof LootItem lootItemEntry) {
                        Item item = ((LootItemMixin) lootItemEntry).getItem();
                        for (LootItemCondition condition : ((LootPoolMixin) pool).getConditions()) {
                            if (condition instanceof LootItemRandomChanceCondition itemCondition) {
                                float chance = ((LootRandomItemMixin) itemCondition).getChance();
                                if(chance <= ConfigHandler.COMMON.rareItemChance.get()+0.001){
                                    rareDrops.add(item);
                                }
                            } else if (condition instanceof LootItemRandomChanceWithLootingCondition lootingCondition) {
                                float chance = ((LootItemEnchantMixin) lootingCondition).getChance();
                                if(chance <= ConfigHandler.COMMON.rareItemChance.get()+0.001){
                                    rareDrops.add(item);
                                }
                            }
                        }
                    }
                }
            }
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                for(ItemEntity itemEntity: event.getDrops()){
                    for(Item item: rareDrops){
                        if(itemEntity.getItem().getItem().getDescriptionId().equals(item.getDescriptionId()) && player.getPersistentData().getLong("VPPrismChallenge") < System.currentTimeMillis()) {
                            challange.setChallenge(13, player);
                            player.getPersistentData().putLong("VPPrismChallenge",System.currentTimeMillis()+1000);
                        }
                    }
                }
            });
        }
    }
    public static boolean hasCurses(int curses,Player player){
        if(getCurses(player) >= curses)
            return true;
        return false;
    }
    public static int getCurses(Player player){
        int cursedEnchantmentCount = 0;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR || slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
                ItemStack itemStack = player.getItemBySlot(slot);
                if (!itemStack.isEmpty()) {
                    int curseCount = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.VANISHING_CURSE, itemStack);
                    if (curseCount > 0) {
                        cursedEnchantmentCount += curseCount;
                    }
                    curseCount = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.BINDING_CURSE, itemStack);
                    if (curseCount > 0) {
                        cursedEnchantmentCount += curseCount;

                    }
                }
            }
        }
        return cursedEnchantmentCount;
    }

    @SubscribeEvent
    public static void eatEvent(LivingEntityUseItemEvent.Finish event){
        if(event.getEntity() instanceof Player player) {
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                if(player.getCommandSenderWorld().isClientSide)
                    return;
                challange.addFood(event.getItem().toString().replaceAll("[0-9]", "").trim(), player);
                if (event.getItem().getItem() instanceof EnchantedGoldenAppleItem) {
                    Random random = new Random();
                    int numba = random.nextInt(100);
                    if (numba < challange.getGoldenChance()) {
                        challange.setChallenge(9, player);
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void useBlock(PlayerInteractEvent.RightClickBlock event){
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();
        if(stack.getItem() instanceof RecordItem recordItem && player.level().getBlockState(event.getHitVec().getBlockPos()).getBlock() == Blocks.JUKEBOX){
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                challange.addMusic(recordItem.getDescriptionId()+".desc",player);
            });
        }
    }
    @SubscribeEvent
    public static void interactEvent(PlayerInteractEvent.EntityInteract event){
        Player player = event.getEntity();
        Entity entity = event.getTarget();
        if(entity instanceof Bucketable && event.getItemStack().getItem() instanceof BucketItem && entity instanceof LivingEntity livingEntity){
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                String fish = entity.getType().getDescriptionId();
                if(entity instanceof Axolotl axolotl)
                    fish = axolotl.getVariant().getName();
                else if(entity instanceof TropicalFish tropicalFish)
                    fish = tropicalFish.getVariant().getSerializedName();
                if(!Bucketable.bucketMobPickup(player, InteractionHand.MAIN_HAND,(LivingEntity & Bucketable) livingEntity).equals(Optional.empty()))
                    challange.addSea(fish,player);
            });
        }
    }

    @SubscribeEvent
    public static void useEvent(PlayerInteractEvent.RightClickItem event){
        Player player = event.getEntity();
        if(player.getCommandSenderWorld().isClientSide)
            return;
        ItemStack stack = event.getItemStack();
        if(stack.getItem() instanceof EnderEyeItem && VPUtil.hasVestige(ModItems.ANOMALY.get(), player)){
            if(player instanceof ServerPlayer serverPlayer){
                ItemStack stackInSlot = VPUtil.getVestigeStack(Anomaly.class,player);
                if(stackInSlot != null && stackInSlot.getItem() instanceof Anomaly anomaly) {
                    anomaly.fuckNbt();
                    double x = stackInSlot.getOrCreateTag().getDouble("VPReturnX");
                    double y = stackInSlot.getOrCreateTag().getDouble("VPReturnY");
                    double z = stackInSlot.getOrCreateTag().getDouble("VPReturnZ");
                    String key = stackInSlot.getOrCreateTag().getString("VPReturnKey");
                    String path = stackInSlot.getOrCreateTag().getString("VPReturnDir");
                    if(x != 0 && y != 0 && z != 0 && !key.isEmpty()) {
                        ServerLevel serverLevel = serverPlayer.getCommandSenderWorld().getServer().getLevel(VPUtil.getWorldKey(key,path));
                        if(serverLevel == null){
                            player.sendSystemMessage(Component.literal("World is null somehow..."));
                            event.setCanceled(true);
                            return;
                        }
                        if(ConfigHandler.COMMON.anomaly.get()){
                            for(LivingEntity entity: VPUtil.getEntitiesAround(player,4,4,4,true)){
                                if(VPUtil.isProtectedFromHit(player,entity))
                                    break;
                                entity.changeDimension(serverLevel);
                                entity.teleportTo(x, y, z);
                            }
                        }
                        else {
                            for (Object entity : VPUtil.getEntitiesAroundOfType(Entity.class, player, 4, 4, 4, true)) {
                                if (entity instanceof ServerPlayer victim) {
                                    if(VPUtil.isProtectedFromHit(player,victim))
                                        break;
                                    victim.teleportTo(serverLevel, x, y, z, 0, 0);
                                }
                                else if (entity instanceof Entity target) {
                                    if(VPUtil.isProtectedFromHit(player,target))
                                        break;
                                    target.changeDimension(serverLevel);
                                    target.teleportTo(x, y, z);
                                }
                            }
                        }
                        serverPlayer.teleportTo(serverLevel, x, y, z, 0, 0);
                    } else {
                        for(LivingEntity entity: VPUtil.getEntitiesAround(player,4,4,4,false)){
                            if(VPUtil.isProtectedFromHit(player,entity))
                                break;
                            entity.teleportTo(serverPlayer.getRespawnPosition().getX(),serverPlayer.getRespawnPosition().getY(),serverPlayer.getRespawnPosition().getZ());
                        }
                        serverPlayer.teleportTo(serverPlayer.getRespawnPosition().getX(),serverPlayer.getRespawnPosition().getY(),serverPlayer.getRespawnPosition().getZ());
                    }
                    event.setCanceled(true);
                }
            }
        }
    }
    @SubscribeEvent
    public static void tameEvent(AnimalTameEvent event){
        Animal animal = event.getAnimal();
        Player player = event.getTamer();
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
            challange.addMobTame(event.getEntity().getType().toString(),player);
            if(animal instanceof Cat cat){
                String barsik = String.valueOf(cat.getVariant());
                barsik = barsik.substring("CatVariant[texture=minecraft:textures/entity/cat/".length());
                barsik = barsik.substring(0,barsik.length()-5);
                challange.addCat(barsik,player);
            }
            if(animal.getType().getDescriptionId().equals(challange.getRandomEntity())){
                challange.setChallenge(14, player);
                challange.setChaosTime(System.currentTimeMillis(),player);
                challange.setRandomEntity(VPUtil.getRandomEntity(),player);
            }
        });
    }
    @SubscribeEvent
    public static void craftEvent(PlayerEvent.ItemCraftedEvent event){
        ItemStack stack = event.getCrafting();
        if(stack.getItem().getDescriptionId().contains("gold")){
            Player player = event.getEntity();
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                String name = stack.getItem().getDescriptionId();
                /*name = name.substring("translation{key=".length());
                name = name.substring(0,name.length()-"', args=[]}".length());*/
                name = name.replaceAll(",","");
                cap.addGold(name,player);
            });
        }
    }
    @SubscribeEvent
    public static void loginIn(PlayerEvent.PlayerLoggedInEvent event){
        Player player = event.getEntity();
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            cap.sync(player);
        });
/*        List<ItemStack> vestiges = VPUtil.getVestigeList(player);
        for(ItemStack stack: vestiges){
            if(stack.getItem() instanceof Vestige vestige)
                vestige.init(stack);
        }*/
        PlayerCapabilityVP.initMaximum(player);
        VPUtil.updateStats(player);
    }

    @SubscribeEvent
    public static void loginOut(PlayerEvent.PlayerLoggedOutEvent event){
        Player player = event.getEntity();
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            cap.sync(player);
        });
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onHeal(LivingHealEvent event){
        LivingEntity entity = event.getEntity();
        CompoundTag tag = entity.getPersistentData();
        if (tag != null) {
            if(tag.getLong("VPDeath") > System.currentTimeMillis()) {
                event.setAmount(0);
                event.setCanceled(true);
                return;
            }
            if(tag.getLong("VPWhirlHeal") > System.currentTimeMillis())
                event.setAmount(Math.max(0,event.getAmount()-50));
            float healingBonus = VPUtil.getHealBonus(entity);
            float resedHeal = 0;
            if(healingBonus < 0)
                resedHeal = (event.getAmount()*(healingBonus/100))*-1;
            event.setAmount(Math.max(0,event.getAmount()+(event.getAmount()*(healingBonus/100))));
            float VPHealDebt = tag.getFloat("VPHealDebt");
            if(VPHealDebt > 0 && event.getAmount() > 0) {
                float lastHeal = event.getAmount();
                event.setAmount(Math.max(0, lastHeal - VPHealDebt));
                if(VPHealDebt-lastHeal > 0) {
                    resedHeal += lastHeal;
                    tag.putFloat("VPHealDebt", VPHealDebt - lastHeal);
                }
                else {
                    resedHeal += event.getAmount();
                    tag.putFloat("VPHealDebt", 0);
                }
            }
            if(entity instanceof Player player) {
                if((VPUtil.hasStellarVestige(ModItems.ARMOR.get(), player) && event.getAmount() > 1)){
                    ItemStack stack = VPUtil.getVestigeStack(Armor.class,player);
                    if(stack.getOrCreateTag().getFloat("VPArmor") > 0)
                        stack.getOrCreateTag().putFloat("VPArmor",0);
                }
                if(VPUtil.hasVestige(ModItems.FLOWER.get(), player) && player.getPersistentData().getLong("VPFlowerSpecial") > 0){
                    for(LivingEntity livingEntity: VPUtil.getCreaturesAround(player,20,20,20)){
                        livingEntity.heal(resedHeal);
                        VPUtil.spawnParticles(player, ParticleTypes.HEART,livingEntity.getX(),livingEntity.getY(),livingEntity.getZ(),4,0,0.5,0);
                    }
                }
                if(VPUtil.hasStellarVestige(ModItems.SOULBLIGHTER.get(), player)){
                    boolean found = false;
                    for (LivingEntity entityTarget: VPUtil.getEntities(player,30,false)) {
                        if (entityTarget.getPersistentData().hasUUID("VPPlayer")){
                            if(entityTarget.getPersistentData().getUUID("VPPlayer").equals(player.getUUID())){
                                found = true;
                                entityTarget.heal(event.getAmount());
                            }
                        }
                    }
                    if(found) {
                        event.setAmount(0);
                        event.setCanceled(true);
                    }
                }
            }
            if(entity.getHealth()+event.getAmount() > entity.getMaxHealth() || entity.getPersistentData().getBoolean("VPMarkUlt")) {
                float overHeal = entity.getHealth() + event.getAmount() - entity.getMaxHealth();
                if(entity.getPersistentData().getBoolean("VPMarkUlt"))
                    overHeal = event.getAmount();
                if(entity instanceof Player player) {
                    if(!VPUtil.hasStellarVestige(ModItems.ARMOR.get(), player) && VPUtil.hasVestige(ModItems.ARMOR.get(), player)){
                        ItemStack stack = VPUtil.getVestigeStack(Armor.class,player);
                        if(stack.getOrCreateTag().getFloat("VPArmor") > 0)
                            stack.getOrCreateTag().putFloat("VPArmor",0);
                    }
                    if(VPUtil.hasStellarVestige(ModItems.TRIGON.get(), player)) {
                        VPUtil.regenOverShield(player, (float)(overHeal*ConfigHandler.COMMON.trigonHeal.get()));
                    }
                }
                if(entity.getPersistentData().getBoolean("VPSweetUlt")){
                    float saturation = entity.getPersistentData().getFloat("VPSaturation");
                    if(saturation + overHeal < ConfigHandler.COMMON.donutMaxSaturation.get())
                        entity.getPersistentData().putFloat("VPSaturation",overHeal+saturation);
                    else entity.getPersistentData().putFloat("VPSaturation",ConfigHandler.COMMON.donutMaxSaturation.get());
                }
                if(entity.getPersistentData().getBoolean("MaskStellar") && entity.getPersistentData().getLong("VPMaskStellarCD")+1000 <= System.currentTimeMillis()){
                    VPUtil.equipmentDurability(5,entity);
                    entity.getPersistentData().putLong("VPMaskStellarCD",System.currentTimeMillis());
                }
            }
            if(entity.getPersistentData().getBoolean("VPMarkUlt")){
                entity.getPersistentData().putFloat("VPHealReduced",event.getAmount()+entity.getPersistentData().getFloat("VPHealReduced"));
                event.setAmount(0);
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void teleportEvent(EntityTeleportEvent event){
        if(event.getEntity() instanceof LivingEntity entity){
            if(entity.getPersistentData().getLong("VPAntiTP") > 0 || VPUtil.isEvent(entity))
                event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void entityChangeDimension(EntityTravelToDimensionEvent event){
        if(event.getEntity() instanceof LivingEntity entity){
            if(entity.getPersistentData().getLong("VPAntiTP") > 0 || VPUtil.isEvent(entity))
                event.setCanceled(true);
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event){
        Player player = event.getEntity();
        PlayerCapabilityVP.initMaximum(player);
        if(player.getPersistentData().getLong("VPAntiTP") > 0 || VPUtil.isEvent(player))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void struck(EntityStruckByLightningEvent event){
        if(event.getEntity() instanceof Creeper creeper && !creeper.getCommandSenderWorld().isClientSide){
            for(LivingEntity entity: VPUtil.getEntitiesAround(creeper,4,4,4,false)){
                if(entity instanceof Player player && player.getPersistentData().getLong("VPBallChallCd") <= System.currentTimeMillis()) {
                    player.getPersistentData().putLong("VPBallChallCd",System.currentTimeMillis()+1000);
                    player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                        cap.setChallenge(18,player);
                    });
                    break;
                }
            }
        }
    }
    @SubscribeEvent
    public static void tick(LivingEvent.LivingTickEvent event){
        LivingEntity entity = event.getEntity();
        if(Float.isNaN(entity.getHealth())) {
            VPUtil.deadInside(entity);
            return;
        }
        CompoundTag tag = entity.getPersistentData();
        Random random = new Random();
        if(!entity.isAlive())
            return;
        if(entity.tickCount % 20 == 0 && (VPUtil.getShield(entity) > 0 || VPUtil.getOverShield(entity) > 0))
            VPUtil.syncEntity(entity);
        if(VPUtil.isBoss(entity) && ConfigHandler.COMMON.hardcore.get() && entity.getAttributes() != null && (!entity.getAttributes().hasModifier(Attributes.MAX_HEALTH, UUID.fromString("ee3a5be4-dfe5-4756-b32b-3e3206655f47")))){
            VPUtil.spawnBoss(entity);
        }
        if(entity instanceof TropicalFish fish){
            long eat = fish.getPersistentData().getLong("VPEating");
            if(eat > 0){
                if(eat+ConfigHandler.COMMON.eatingMinutes.get()*60*1000 < System.currentTimeMillis() && !fish.getPersistentData().getBoolean("VPEat")) {
                    fish.getPersistentData().putBoolean("VPEat", true);
                    VPUtil.spawnSphere(fish,ParticleTypes.HEART,20,2,0.1f);
                }
                if(entity.tickCount % 20 == 0){
                    VPUtil.spawnSphere(fish,ParticleTypes.BUBBLE,10,2,0.1f);
                    for(LivingEntity livingEntity: VPUtil.getEntitiesAround(entity,30,30,30,false)){
                        if(livingEntity instanceof Monster monster) {
                            monster.setTarget(fish);
                        }
                    }
                }
            }
        }
        /*if(entity.tickCount < 10 && VPUtil.isBoss(entity))
            entity.heal(9999);*/
        if(entity.tickCount % 20 == 0) {
            if(VPUtil.isBoss(entity))
                entity.heal((float) (entity.getMaxHealth() * ConfigHandler.COMMON.healPercent.get()));
            if(VPUtil.isNpc(entity.getType())){
                entity.getPersistentData().putLong("VPAntiTp",System.currentTimeMillis()+99999);
            }
            if(Float.isNaN(entity.getHealth())) {
                entity.setHealth(1);
                entity.getPersistentData().putFloat("VPTry",entity.getPersistentData().getFloat("VPTry")+1);
            }
            if(Float.isNaN(entity.getMaxHealth()) || entity.getPersistentData().getFloat("VPTry") > 5) {
                entity.getPersistentData().putFloat("VPTry",0);
                VPUtil.deadInside(entity);
            }
            if(entity.getPersistentData().getLong("VPLyra1") != 0 && entity.getPersistentData().getLong("VPLyra1") < System.currentTimeMillis()){
                entity.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(entity, Attributes.MAX_HEALTH, UUID.fromString("9548da03-e5f8-4cfd-be48-c4ae9b25d86d"),1.6f, AttributeModifier.Operation.MULTIPLY_TOTAL,"vp.lyra.1"));
                entity.getPersistentData().putLong("VPLyra1",0);
            }
            if(VPUtil.hasLyra(entity,2))
                entity.heal(10);
            if(entity.getPersistentData().getLong("VPLyra5") != 0 && entity.getPersistentData().getLong("VPLyra5") < System.currentTimeMillis()){
                entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity, Attributes.ARMOR, UUID.fromString("403a8f4a-e7a4-4ffa-91b0-c122efa89443"),100, AttributeModifier.Operation.ADDITION,"vp.lyra.5"));
                entity.getPersistentData().putLong("VPLyra5",0);
            }
            if(entity.getPersistentData().getLong("VPWhirlVoid") != 0 && entity.getPersistentData().getLong("VPWhirlVoid") < System.currentTimeMillis()){
                entity.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(entity, Attributes.MAX_HEALTH,UUID.fromString("951043f3-5872-4dd5-a99e-7358b6c619d6"),0.2f, AttributeModifier.Operation.MULTIPLY_TOTAL,"vp:whirlHealth"));
                entity.getPersistentData().putLong("VPWhirlVoid",0);
            }
            if(entity.getPersistentData().getBoolean("VPWaved")){
                for(LivingEntity livingEntity: VPUtil.getEntitiesAround(entity,20,20,20,false)){
                    if(livingEntity instanceof SillySeashell sillySeashell){
                        if(entity.getPersistentData().getInt("VPAbuse") >= 10){
                            VPUtil.teleportRandomly(livingEntity,15);
                            continue;
                        }
                        if(entity instanceof Monster monster) {
                            if(monster.isWithinMeleeAttackRange(sillySeashell))
                                monster.getPersistentData().putInt("VPAbuse", 0);
                            else monster.getPersistentData().putInt("VPAbuse", monster.getPersistentData().getInt("VPAbuse")+1);
                        }
                    }
                }
            }
        }
        if(entity.getPersistentData().getLong("VPBubble") > System.currentTimeMillis()){
            VPUtil.spawnSphere(entity,ParticleTypes.BUBBLE,60,3,1);
            if(entity.tickCount % 20 == 0 && entity.getKillCredit() != null && entity.getKillCredit() instanceof Player player){
                entity.hurt(player.damageSources().drown(),10);
                VPUtil.play(player, SoundRegistry.BUBBLE1.get());
            }
        }
        if(VPUtil.isNightmareBoss(entity)){
            VPUtil.nightmareTickEvent(entity);
        }
        if (entity.getPersistentData().getInt("VPGravity") > 30)
            entity.getPersistentData().putInt("VPGravity", 30);
        if(entity.getPersistentData().getInt("VPSoulRottingStellar") >= 50)
            VPUtil.clearEffects(entity,true);
        if (entity.tickCount % 20 == 0) {
            if(tag.getFloat("VPHealResMask") != 0) {
                tag.putFloat("VPHealResMask", 0);
            }
            if(tag.getBoolean("MaskStellar")){
                tag.putBoolean("MaskStellar", false);
            }
        }
        if(entity.getPersistentData().getLong("VPAntiTP") != 0 && entity.getPersistentData().getLong("VPAntiTP") <= System.currentTimeMillis()) {
            entity.getPersistentData().putLong("VPAntiTP", 0);
        }
        if(entity.getPersistentData().getLong("VPAntiShield") != 0 && entity.getPersistentData().getLong("VPAntiShield") <= System.currentTimeMillis()) {
            entity.getPersistentData().putLong("VPAntiShield", 0);
        }
        if(entity.getPersistentData().getLong("VPDonutSpecial") != 0 && entity.getPersistentData().getLong("VPDonutSpecial") <= System.currentTimeMillis())
            entity.getPersistentData().putLong("VPDonutSpecial",0);
        if(entity.getPersistentData().getLong("VPFlowerStellar") != 0 && entity.getPersistentData().getLong("VPFlowerStellar") <= System.currentTimeMillis())
            entity.getPersistentData().putLong("VPFlowerStellar",0);
        if(entity.getPersistentData().getLong("VPAstral") != 0 && entity.getPersistentData().getLong("VPAstral") <= System.currentTimeMillis())
            entity.getPersistentData().putLong("VPAstral",0);
        if(entity.getPersistentData().getLong("VPPrismBuff") != 0 && entity.getPersistentData().getLong("VPPrismBuff") <= System.currentTimeMillis()) {
            entity.getPersistentData().putLong("VPPrismBuff", 0);
            entity.getPersistentData().putInt("VPPrismDamage",0);
        }
        if(entity.getPersistentData().getLong("VPEnchant") != 0 && entity.getPersistentData().getLong("VPEnchant") <= System.currentTimeMillis()) {
            entity.getPersistentData().putLong("VPEnchant", 0);
            VPUtil.negativnoDisenchant(entity);
        }
        if(entity.tickCount % 2 == 0){
            if(entity instanceof ServerPlayer player && !player.getCommandSenderWorld().isClientSide) {
                CompoundTag sendNudes = new CompoundTag();
                for (String key : player.getPersistentData().getAllKeys()) {
                    if (key.startsWith("VP") && player.getPersistentData().get(key) != null && !key.startsWith("VPMaxChallenge")) {
                        sendNudes.put(key, player.getPersistentData().get(key));
                    }
                }
                PacketHandler.sendToClient(new SendPlayerNbtToClient(player.getUUID(), sendNudes), player);
            } else if(VPUtil.isBoss(entity)) {
                CompoundTag sendNudes = new CompoundTag();
                for (String key : entity.getPersistentData().getAllKeys()) {
                    if (key.startsWith("VP") && entity.getPersistentData().get(key) != null) {
                        sendNudes.put(key, entity.getPersistentData().get(key));
                    }
                }
                if(!entity.getCommandSenderWorld().isClientSide)
                    PacketHandler.sendToClients(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SendEntityNbtToClient(sendNudes,entity.getId()));
            }
        }
        if(entity.tickCount % 20 == 0 && entity instanceof ServerPlayer playerServer){
            playerServer.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                cap.sync(playerServer);
            });
            if(VPUtil.isEvent(playerServer)){
                PacketHandler.sendToClient(new PlayerFlyPacket(2), playerServer);
            }
            if(VPUtil.hasStellarVestige(ModItems.CHAOS.get(), playerServer)){
                for(LivingEntity livingEntity: VPUtil.getEntities(playerServer,20,false)){
                    float VPHealDebt = livingEntity.getPersistentData().getFloat("VPHealDebt");
                    if(VPHealDebt > 0)
                        livingEntity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(livingEntity,Attributes.MAX_HEALTH,UUID.fromString("95124945-2b8e-438e-b070-a48e32605d88"),Math.max(-playerServer.getMaxHealth()+1,-VPHealDebt/10), AttributeModifier.Operation.ADDITION,"vp.chaos.maxhp"));
                }
            }
            if(playerServer.isInWaterRainOrBubble() && VPUtil.hasVestige(ModItems.WHIRLPOOL.get(), playerServer)){
                playerServer.heal(playerServer.getMaxHealth()*0.05f);
            }
            if(playerServer.getPersistentData().getLong("VPIgnisTime") != 0 && playerServer.getPersistentData().getLong("VPIgnisTime") < System.currentTimeMillis()){
                playerServer.getPersistentData().putLong("VPIgnisTime",0);
                playerServer.getPersistentData().putFloat("VPIgnis",0);
                playerServer.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(playerServer,Attributes.MAX_HEALTH,UUID.fromString("4ed92da8-dd60-41a2-9540-cc8816af92e2"),1, AttributeModifier.Operation.ADDITION,"vp:ignis_hp"));
                playerServer.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(playerServer,Attributes.ARMOR,UUID.fromString("39f598b3-b75c-422f-93fc-8e65dade8730"),1, AttributeModifier.Operation.ADDITION,"vp:ignis_armor"));
            }
            VPUtil.setLuck(playerServer);
            VPUtil.sync(playerServer);
        }
        if(entity.tickCount % 40 == 0)
            Objects.requireNonNull(entity.getAttribute(Attributes.MAX_HEALTH)).removeModifier(UUID.fromString("95124945-2b8e-438e-b070-a48e32605d88"));
        if(tag.getLong("VPDeath") != 0 && tag.getLong("VPDeath")+ 40*1000 < System.currentTimeMillis())
            tag.putLong("VPDeath",0);
        if(event.getEntity() instanceof Player player){
            if(entity.tickCount % 4000 == 0 && random.nextDouble() < VPUtil.getChance(0.2+ConfigHandler.COMMON.easterChance.get()/100d,player)){
                if(VPUtil.isEasterEvent()){
                    VPUtil.spawnEgg(player);
                }
            }
            List<ItemStack> slotResultList = VPUtil.getVestigeList(player);
            CompoundTag playerTag = player.getPersistentData();
            if(playerTag == null)
                playerTag = new CompoundTag();
            if(player.tickCount % 1000 == 0){
                player.getPersistentData().putInt("VPDevourerShow",0);
            }
            if(playerTag.getBoolean("VPButton1") || playerTag.getBoolean("VPButton3")){
                List<ItemStack> stackList = VPUtil.getFirstVestige(player);
                ItemStack stackInSlot = null;
                if(!stackList.isEmpty())
                    stackInSlot = stackList.get(0);
                if(stackInSlot != null && stackInSlot.getItem() instanceof Vestige vestige) {
                    if (player.getPersistentData().getBoolean("VPButton1")) {
                        vestige.setSpecialActive(vestige.specialMaxTime(stackInSlot), player, stackInSlot);
                    }
                    else {
                        vestige.setUltimateActive(vestige.ultimateMaxTime(stackInSlot),player, stackInSlot);
                    }
                }
                player.getPersistentData().putBoolean("VPButton1",false);
                player.getPersistentData().putBoolean("VPButton3",false);
            } else if(playerTag.getBoolean("VPButton2") || playerTag.getBoolean("VPButton4")) {
                ItemStack stack;
                if(slotResultList.size() > 1)
                    stack = slotResultList.get(1);
                else if (!slotResultList.isEmpty())
                    stack = slotResultList.get(0);
                else stack = null;
                if(stack != null && stack.getItem() instanceof Vestige vestige) {
                    if (player.getPersistentData().getBoolean("VPButton2")) {
                        vestige.setSpecialActive(vestige.specialMaxTime(stack), player, stack);
                    }
                    else {
                        vestige.setUltimateActive(vestige.ultimateMaxTime(stack), player, stack);
                    }
                }
                player.getPersistentData().putBoolean("VPButton2",false);
                player.getPersistentData().putBoolean("VPButton4",false);
            }
            if(player.tickCount % 20 == 0){
                for(ItemStack stack: player.getInventory().items){
                    if(stack.getItem() instanceof Accessory accessory){
                        if(accessory.getType(stack) == 0 && !player.getCommandSenderWorld().isClientSide)
                            accessory.init(stack,player);
                    }
                }
                /*List<ItemStack> accessories = VPUtil.getAccessoryList(player);
                if(!accessories.isEmpty()){
                    float health = 0;
                    float attack = 0;
                    float damage = 0;
                    float heal = 0;
                    float shields = 0;
                    for(ItemStack stack: accessories){
                        if(stack.getItem() instanceof Accessory accessory){
                            if(accessory.getType(stack) == 1)
                                health += accessory.getStatAmount(stack);
                            if(accessory.getType(stack) == 2)
                                attack += accessory.getStatAmount(stack);
                            if(accessory.getType(stack) == 3)
                                damage += accessory.getStatAmount(stack);
                            if(accessory.getType(stack) == 4)
                                heal += accessory.getStatAmount(stack);
                            if(accessory.getType(stack) == 5)
                                shields += accessory.getStatAmount(stack);
                        }
                    }
                    player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("d05228bf-b23d-4091-8e9c-4954688989fd"), health, AttributeModifier.Operation.ADDITION, "vp_accessory:health"));
                    player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ATTACK_DAMAGE, UUID.fromString("91595e31-3c5a-4f7d-8097-60a96e37a51c"), attack, AttributeModifier.Operation.ADDITION, "vp_accessory:attack"));
                    player.getPersistentData().putFloat("VPAcsDamage",damage);
                    player.getPersistentData().putFloat("VPAcsHeal",heal);
                    player.getPersistentData().putFloat("VPAcsShields",shields);
                } else {
                    player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("d05228bf-b23d-4091-8e9c-4954688989fd"), 0, AttributeModifier.Operation.ADDITION, "vp_accessory:health"));
                    player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ATTACK_DAMAGE, UUID.fromString("91595e31-3c5a-4f7d-8097-60a96e37a51c"), 0, AttributeModifier.Operation.ADDITION, "vp_accessory:attack"));
                }*/
            }
            if(!slotResultList.isEmpty()){
                if(slotResultList.get(0).getItem() instanceof Vestige vestige) {
                    if(vestige.getVestigeNumber(slotResultList.get(0)) != player.getPersistentData().getInt("VPCurioSucks1"))
                        vestige.curioSucks(player, slotResultList.get(0));
                    player.getPersistentData().putInt("VPCurioSucks1", vestige.getVestigeNumber(slotResultList.get(0)));
                } else {
                    player.getPersistentData().putInt("VPCurioSucks1", 0);
                    VPUtil.vestigeNullify(player);
                }
                if(slotResultList.size() > 1 && slotResultList.get(1).getItem() instanceof Vestige vestige) {
                    if (vestige.getVestigeNumber(slotResultList.get(1)) != player.getPersistentData().getInt("VPCurioSucks2"))
                        vestige.curioSucks(player, slotResultList.get(1));
                    player.getPersistentData().putInt("VPCurioSucks2", vestige.getVestigeNumber(slotResultList.get(1)));
                } else if (player.getPersistentData().getInt("VPCurioSucks2") > 0) {
                    player.getPersistentData().putInt("VPCurioSucks2", 0);
                    VPUtil.vestigeNullify(player);
                }
            } else if (player.getPersistentData().getInt("VPCurioSucks1") != 0 || player.getPersistentData().getInt("VPCurioSucks2") != 0) {
                player.getPersistentData().putInt("VPCurioSucks1", 0);
                player.getPersistentData().putInt("VPCurioSucks2", 0);
                VPUtil.vestigeNullify(player);
            }
            /*ItemStack stack = player.inventoryMenu.getCarried();
            System.out.println(stack);
            if(stack.getItem() instanceof Vestige vestige){
                System.out.println("yes yes skibidy");
                vestige.curioSucks(player,stack);
            }*/
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                if(player.tickCount % 4000 == 0 && VPUtil.getCurrentBiome(player) != null){
                    Level level = player.getCommandSenderWorld();
                    String biome = VPUtil.getCurrentBiome(player).getPath();
                    if(biome.contains("ocean") && !biome.contains("deep")){
                        double chance = 0.1;
                        if(biome.contains("warm"))
                            chance *= 3;
                        if(random.nextDouble() < VPUtil.getChance(chance,player)){
                            HungryOyster oyster = new HungryOyster(ModEntities.OYSTER.get(),level);
                            oyster.setPos(player.getX(),player.getY(),player.getZ());
                            VPUtil.teleportRandomly(oyster,30,true);
                            level.addFreshEntity(oyster);
                            if(random.nextDouble() < VPUtil.getChance(0.1,player)){
                                oyster.getPersistentData().putBoolean("VPCool",true);
                                VPUtil.syncEntity(oyster);
                            }
                            VPUtil.play(player,SoundRegistry.BUBBLEPOP.get(),oyster.getX(),oyster.getY(),oyster.getZ());
                            for(int i = 0; i < 3; i++) {
                                TropicalFish fish = new TropicalFish(EntityType.TROPICAL_FISH, level);
                                fish.setPos(player.getX(),player.getY(),player.getZ());
                                VPUtil.teleportRandomly(fish,30,true);
                                fish.getPersistentData().putLong("VPEating",System.currentTimeMillis());
                                level.addFreshEntity(fish);
                            }
                        }
                    }
                    else if(biome.contains("ocean") && biome.contains("deep")){
                        double chance = 0.1;
                        if(biome.contains("cold"))
                            chance *= 3;
                        if(random.nextDouble() < VPUtil.getChance(chance,player)){
                            SillySeashell shell = new SillySeashell(ModEntities.SEASHELL.get(),level);
                            shell.setPos(player.getX(),player.getY(),player.getZ());
                            level.addFreshEntity(shell);
                            VPUtil.teleportRandomly(shell,30,true);
                            VPUtil.play(player,SoundRegistry.BUBBLEPOP.get(),shell.getX(),shell.getY(),shell.getZ());
                        }
                    }
                }
                cap.addBiome(player);
                cap.addDimension(player,player.getCommandSenderWorld().dimension().location().getPath(),player.getCommandSenderWorld().dimension().location().getNamespace());
                for(int i = 0; i < PlayerCapabilityVP.totalVestiges; i++){
                    if(cap.getChallenge(i+1) >= PlayerCapabilityVP.getMaximum(i+1) && !cap.hasCoolDown(i+1) && PlayerCapabilityVP.getMaximum(i+1) > 0){
                        if(i+1 == 11) {
                            if (player.getPersistentData().getLong("VPGiveVestigeCd") == 0) {
                                player.getPersistentData().putLong("VPGiveVestigeCd", System.currentTimeMillis() + 10000);
                            }
                            if (player.getPersistentData().getLong("VPGiveVestigeCd") < System.currentTimeMillis()) {
                                player.getPersistentData().putLong("VPGiveVestigeCd", 0);
                                cap.giveVestige(player, i + 1);
                            }
                        } else cap.giveVestige(player, i + 1);
                    }
                }
                if(System.currentTimeMillis() - cap.getTimeCd() >= VPUtil.coolDown(player)) {
                    cap.clearCoolDown(player);
                    cap.addTimeCd(System.currentTimeMillis(),player);
                }
                if(!player.getCommandSenderWorld().isClientSide && !cap.getSleep() && player.tickCount > 24000 && player.tickCount < 24100 && !cap.getLore(player,2)) {
                    player.sendSystemMessage(Component.translatable("vp.sleep"));
                    cap.setSleep(true);
                }
                if(player.tickCount % 100 == 0 && !cap.getLore(player,1))
                    cap.addLore(player,1);
                if(cap.getLore(player,4) && player.tickCount % 24000 == 0)
                    cap.addLore(player,6);
                if(cap.getLore(player,7) && player.tickCount % 24000 == 0)
                    cap.addLore(player,8);
                if(cap.getChaosTime() <= 0) {
                    cap.setChaosTime(System.currentTimeMillis(),player);
                    cap.setRandomEntity(VPUtil.getRandomEntity(),player);
                }
                if(cap.getRandomEntity().isEmpty())
                    cap.setRandomEntity(VPUtil.getRandomEntity(),player);
                if(cap.getChaosTime()+VPUtil.getChaosTime() < System.currentTimeMillis()){
                    cap.setChallenge(14,0,player);
                    cap.setChaosTime(System.currentTimeMillis(),player);
                    cap.setRandomEntity(VPUtil.getRandomEntity(),player);
                }
                if(player.isSleepingLongEnough()){
                    cap.addLore(player,2);
                    if(cap.getLore(player,3))
                        cap.addLore(player,5);
                }
                Iterator<MobEffectInstance> iterator = player.getActiveEffects().iterator();
                while (iterator.hasNext()) {
                    MobEffectInstance effectInstance = iterator.next();
                    MobEffect effect = effectInstance.getEffect();
                    cap.addEffect(effect.getDescriptionId(),player);
                }
            });
        }
    }
    @SubscribeEvent
    public static void capabilityAttach(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer)){
            event.addCapability(new ResourceLocation(VestigesOfThePresent.MODID, "properties"), new PlayerCapabilityProviderVP());
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event){
        event.getOriginal().reviveCaps();
        event.getOriginal().getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(oldStore -> {
            event.getEntity().getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(newStore -> {
                newStore.copyNBT(oldStore);
                newStore.sync(event.getEntity());
            });
        });
        PlayerCapabilityVP.initMaximum(event.getEntity());
        event.getOriginal().invalidateCaps();
    }

    @SubscribeEvent
    public static void onPlayerPickUp(PlayerEvent.ItemPickupEvent event){
        Player player = event.getEntity();
        ItemStack stack = event.getStack();
        if(stack.getItem() instanceof Vestige vestige){
            vestige.curioSucks(player,stack);
        }
    }


    @SubscribeEvent
    public void onAnvilUse(AnvilRepairEvent event) {
        Player player = event.getEntity();
        ItemStack right = event.getRight();
        if (right.getItem() instanceof EnchantedBookItem book) {
            if(book.getEnchantments(right).toString().contains("curse"))
                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                    cap.setChallenge(7,player);
                });
        }
    }

    @SubscribeEvent
    public static void onMobSpawn(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof LivingEntity entity && VPUtil.isBoss(entity) && ConfigHandler.COMMON.hardcore.get()) {
            VPUtil.spawnBoss(entity);
        }
        if(event.getEntity() instanceof Player player){
            VPUtil.updateStats(player);
            player.getPersistentData().putLong("VPDeath",0);
        }
    }
    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event){
        Player player = event.getPlayer();
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            if(event.getState().getBlock() instanceof FlowerBlock flowerBlock) {
                cap.addFlower(flowerBlock.getDescriptionId(), player);
            }
            if(event.getState().getBlock() instanceof CoralBlock coralBlock) {
                cap.addSea(coralBlock.getDescriptionId(), player);
            }
            /*if (flower.contains("flower")) {
                cap.addFlower(flower, player);
            } else {
                for (String element : vanillaFlowers) {
                    if (flower.contains(element))

                }
            }*/
        });
    }

    @SubscribeEvent
    public static void fishEvent(ItemFishedEvent event){
        Player player = event.getEntity();
        Random random = new Random();
        if(VPUtil.hasVestige(ModItems.PEARL.get(), player) && player.getPersistentData().getInt("VPLures") > 0){
            float drowning = player.getMaxAirSupply()-player.getAirSupply();
            player.getPersistentData().putInt("VPLures",player.getPersistentData().getInt("VPLures") - 1);
            ItemStack stack = VPUtil.getFishDrop(player);
            if(drowning > 0 && random.nextDouble() < VPUtil.getChance(drowning/100,player)) {
                stack = VPUtil.getFishDrop(player);
                VPUtil.giveStack(event.getDrops().get(random.nextInt(event.getDrops().size()-1)),player);
            }
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                if(cap.getPearls() > 0){
                    int luck = (int) (player.getMainHandItem().getEnchantmentLevel(Enchantments.FISHING_LUCK)/3f+player.getAttributeValue(Attributes.LUCK)/10);
                    if(random.nextDouble() < VPUtil.getChance(Math.min(0.0005d,luck/100d),player)){
                        ItemStack itemStack;
                        if(random.nextDouble() < VPUtil.getChance(Math.min(0.1d,luck/100d),player))
                            itemStack = new ItemStack(ModItems.BOX.get());
                        else if(random.nextDouble() < VPUtil.getChance(Math.min(0.3d,luck/100d),player))
                            itemStack = new ItemStack(ModItems.BOX_EGGS.get());
                        else itemStack = new ItemStack(ModItems.BOX_SAPLINGS.get());
                        VPUtil.giveStack(itemStack,player);
                        VPUtil.play(player,SoundRegistry.SUCCESS.get());
                    }
                }
            });
            VPUtil.giveStack(stack,player);
        }
    }

    @SubscribeEvent
    public static void onPlaced(BlockEvent.EntityPlaceEvent event){
        if(event.getEntity() instanceof Player player) {
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                if (event.getState().getBlock() instanceof FlowerBlock && ConfigHandler.COMMON.failFlowers.get()) {
                    cap.failChallenge(16,player);
                }
            });
        }
    }

    /*@SubscribeEvent
    public static void onPlayerClonedClient(ClientPlayerNetworkEvent.Clone event){
        event.getOldPlayer().reviveCaps();
        event.getOldPlayer().getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(oldStore -> {
            event.getNewPlayer().getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(newStore -> {
                newStore.copyNBT(oldStore);
            });
        });
        event.getOldPlayer().invalidateCaps();
    }*/
    @SubscribeEvent
    public static void enchantEvent(EnchantmentLevelSetEvent event){
    }
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        VPCommands.register(event.getDispatcher());
    }

    /*@SubscribeEvent
    public static void sleep(PlayerSleepInBedEvent event){

        Player player = event.getEntity();
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap ->{
            cap.addLore(player,2);
            if(cap.getLore(player,3))
                cap.addLore(player,5);
        });
        //PacketHandler.sendToServer(new ClientToServerPacket());
    }*/
}
