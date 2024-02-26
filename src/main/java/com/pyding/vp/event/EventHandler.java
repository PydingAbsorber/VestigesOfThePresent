package com.pyding.vp.event;

import com.pyding.vp.VestigesOfPresent;
import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.capability.PlayerCapabilityVP;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.commands.VPCommands;
import com.pyding.vp.entity.BlackHole;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.artifacts.*;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ClientToServerPacket;
import com.pyding.vp.network.packets.SendPlayerNbtToClient;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

import java.util.*;

@Mod.EventBusSubscriber(modid = VestigesOfPresent.MODID)
public class EventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void damageEventLowest(LivingDamageEvent event){
        if(!event.isCanceled()) {
            LivingEntity entity = event.getEntity();
            CompoundTag tag = entity.getPersistentData();
            VPUtil.damageAdoption(entity, event);
            Random random = new Random();
            if (event.getAmount() <= 5)
                tag.putBoolean("VPCrownDR", true);
            if (entity.getPersistentData().getBoolean("VPKillerQueen")) {
                entity.getPersistentData().putBoolean("VPKillerQueen", false);
                event.setAmount(event.getAmount() * 1.7f);
            }
            if (entity.getPersistentData().getBoolean("VPFlowerStellar") && VPUtil.isDamagePhysical(event.getSource())) {
                entity.getPersistentData().putBoolean("VPFlowerStellar", false);
            }
            if (event.getSource() == null)
                return;
            if(entity.getPersistentData().getLong("VPAstral") > 0 || (event.getSource().getEntity() != null && event.getSource().getEntity().getPersistentData().getLong("VPAstral") > 0)){
                if(!event.getSource().isMagic()) {
                    event.setAmount(0);
                } else event.setAmount(event.getAmount()*10);
            }
            if (event.getSource().getEntity() instanceof Player player) {
                if (event.getSource().isFall() && player.getPersistentData().getInt("VPGravity") < 30)
                    player.getPersistentData().putInt("VPGravity", Math.min(30, player.getPersistentData().getInt("VPGravity") + 1));
                if (VPUtil.hasVestige(ModItems.ANEMOCULUS.get(), player) && !entity.isOnGround()) {
                    event.setAmount(event.getAmount() * 7);
                    VPUtil.equipmentDurability(10, entity, player, VPUtil.hasStellarVestige(ModItems.ANEMOCULUS.get(), player));
                }
                if (VPUtil.hasVestige(ModItems.MASK.get(), player))
                    entity.getPersistentData().putFloat("HealDebt", (float) (entity.getPersistentData().getFloat("HealDebt") + entity.getPersistentData().getFloat("HealDebt") * 0.01));
                if (entity.getHealth() <= entity.getMaxHealth() * 0.5 && player.getPersistentData().getInt("VPMadness") > 0 && VPUtil.hasVestige(ModItems.MARK.get(), player)) {
                    event.setAmount(event.getAmount() * (2 * player.getPersistentData().getInt("VPMadness")));
                    event.getSource().bypassMagic();
                    if (Math.random() < 0.5)
                        player.getPersistentData().putInt("VPMadness", player.getPersistentData().getInt("VPMadness") - 1);
                }
                if (VPUtil.hasStellarVestige(ModItems.MIDAS.get(), player)) {
                    float luck = (float) player.getAttribute(Attributes.LUCK).getValue();
                    event.setAmount(event.getAmount() * (1 + luck / 10));
                }
                if (entity.getPersistentData().getLong("VPEnchant") > 0)
                    event.setAmount(event.getAmount() * 1.5f);
                if (VPUtil.hasVestige(ModItems.BALL.get(), player) && event.getAmount() > entity.getHealth()) {
                    int counter = 0;
                    for (LivingEntity livingEntity : VPUtil.getEntitiesAround(entity, 15, 15, 15, false)) {
                        livingEntity.hurt(event.getSource(), event.getAmount());
                        counter++;
                        if (counter >= 3)
                            break;
                    }
                }
            }
            if (entity instanceof Player player) {
                LivingEntity attacker = event.getEntity();
                double damagePlayer = player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
                double entityDamage = attacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
                if (damagePlayer > entityDamage)
                    event.setAmount(event.getAmount() - (float) (damagePlayer - entityDamage));
                else event.setAmount(event.getAmount() - (float) (entityDamage - damagePlayer));
                if (player.getPersistentData().getBoolean("VPMarkUlt")) {
                    player.getPersistentData().putFloat("VPDamageReduced", event.getAmount() + player.getPersistentData().getFloat("VPDamageReduced"));
                    event.setAmount(0);
                }
                if (VPUtil.hasVestige(ModItems.EARS.get(), player)) {
                    float armor = (float) player.getArmorValue();
                    float chance = (Math.min(69, armor) / 100);
                    if (VPUtil.hasStellarVestige(ModItems.EARS.get(), player)) {
                        event.setAmount(event.getAmount() - (event.getAmount() * chance));
                    }
                }
                if (VPUtil.hasVestige(ModItems.ARMOR.get(), player)) {
                    float amount = event.getAmount();
                    event.setAmount(Math.max(0, event.getAmount() - (40 + player.getPersistentData().getFloat("VPArmor"))));
                    player.getPersistentData().putFloat("VPArmor", player.getPersistentData().getFloat("VPArmor") + amount);
                }
                if (VPUtil.hasVestige(ModItems.CHAOS.get(), player)) {
                    double passiveChance = 0.1 + player.getAttribute(Attributes.LUCK).getValue() / 100;
                    if (Math.random() < passiveChance) {
                        if (Math.random() < 0.5) {
                            if (Math.random() < 0.5) {
                                event.setAmount(event.getAmount() + random.nextInt(Integer.MAX_VALUE));
                            } else event.setAmount(event.getAmount() - random.nextInt(Integer.MAX_VALUE));
                        } else {
                            player.invulnerableTime = 0;
                            player.hurt(VPUtil.randomizeDamageType(), event.getAmount());
                            event.setAmount(0);
                        }
                        if (Math.random() < 0.3 && VPUtil.hasStellarVestige(ModItems.CHAOS.get(), player)) {
                            for (LivingEntity livingEntity : VPUtil.getEntities(player, 30, false)) {
                                livingEntity.hurt(event.getSource(), event.getAmount());
                            }
                        }
                    }
                    double chance = 10 + random.nextInt(90);
                    if (Math.random() < chance / 100 && event.getSource().getEntity() != null && event.getSource().getEntity() instanceof LivingEntity livingEntity && livingEntity != player) {
                        if (player.getPersistentData().getInt("VPChaos") > 0) {
                            player.getPersistentData().putInt("VPChaos", player.getPersistentData().getInt("VPChaos"));
                            livingEntity.hurt(event.getSource(), event.getAmount());
                            event.setAmount(0);
                        }
                    }
                }
                if (VPUtil.hasVestige(ModItems.DEVOURER.get(), player) && player.getPersistentData().getInt("VPDevourerHits") > 0) {
                    ICuriosHelper api = CuriosApi.getCuriosHelper();
                    List list3 = api.findCurios(player, (stackInSlot) -> {
                        if (stackInSlot.getItem() instanceof Devourer devourer) {
                            ItemStack stack = stackInSlot;
                            int souls = stack.getOrCreateTag().getInt("VPDevoured");
                            int attributesBetter = 0;
                            if (VPUtil.hasStellarVestige(ModItems.DEVOURER.get(), player))
                                attributesBetter = VPUtil.compareStats(player, entity, true);
                            if (Math.random() < 0.01 * souls)
                                entity.getPersistentData().putInt("VPSoulRotting", entity.getPersistentData().getInt("VPSoulRotting") + 1 + attributesBetter);
                            if (VPUtil.hasStellarVestige(ModItems.DEVOURER.get(), player))
                                entity.getPersistentData().putInt("VPSoulRottingStellar", entity.getPersistentData().getInt("VPSoulRotting"));
                            if (entity.getPersistentData().getInt("VPSoulRotting") >= 100)
                                VPUtil.deadInside(entity, player);
                            player.getPersistentData().putInt("VPDevourerHits", player.getPersistentData().getInt("VPDevourerHits") - 1);
                            return true;
                        }
                        return false;
                    });
                }
                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                    if (event.getSource().isExplosion())
                        cap.failChallenge(18, player);
                });
                VPUtil.printDamage(player,event);
            }
            if (event.getSource().getEntity() instanceof Player player) {
                if (VPUtil.hasStellarVestige(ModItems.BOOK.get(), player)) {
                    if (player.getPersistentData().getInt("VPBookDamage") < 10 && event.getAmount() <= 5) {
                        player.getPersistentData().putFloat("VPBookDamage", player.getPersistentData().getInt("VPBookDamage") + event.getAmount());
                        player.getPersistentData().putInt("VPBookHits", player.getPersistentData().getInt("VPBookHits") + 1);
                    } else {
                        event.setAmount(event.getAmount() + player.getPersistentData().getInt("VPBookDamage"));
                        event.getSource().bypassEnchantments();
                        player.getPersistentData().putInt("VPBookDamage", 0);
                        player.getPersistentData().putInt("VPBookHits", 0);
                    }
                }
                VPUtil.printDamage(player,event);
            }
            if (event.getAmount() <= 0)
                return;
            float overShield = VPUtil.getOverShield(entity);
            if (overShield > 0) {
                if (event.getSource().isMagic() || event.getSource().isBypassInvul())
                    event.setAmount(event.getAmount() * 0.1f);
                entity.getPersistentData().putFloat("VPOverShield", Math.max(0, entity.getPersistentData().getFloat("VPOverShield") - event.getAmount()));
                event.setAmount(0);
                if (tag.getFloat("VPOverShield") <= 0)
                    VPUtil.play(entity, SoundRegistry.OVERSHIELD_BREAK.get());
            }
            float shield = entity.getPersistentData().getFloat("VPShield");
            float damage = event.getAmount();
            if (shield > damage) {
                entity.getPersistentData().putFloat("VPShield", shield - damage);
                if (event.getSource().getEntity() instanceof Player player) {
                    player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                        cap.setChallenge(19, cap.getChallenge(19) + (int) damage, player);
                    });
                }
                event.setAmount(0);
            } else {
                event.setAmount(damage - shield);
                if (event.getSource().getEntity() instanceof Player player) {
                    player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                        cap.setChallenge(19, cap.getChallenge(19) + (int) shield, player);
                    });
                }
                if (shield > 0)
                    VPUtil.play(entity, SoundRegistry.SHIELD_BREAK.get());
                if (entity.getPersistentData().getLong("VPFlowerStellar") > 0 && event.getSource().getEntity() != null)
                    event.getSource().getEntity().hurt(DamageSource.MAGIC, entity.getPersistentData().getFloat("VPShieldInit"));
                entity.getPersistentData().putFloat("VPShield", 0);
                entity.getPersistentData().putFloat("VPShieldInit", 0);

            }
        } else {
        }
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
        if(victim instanceof Player player){
            if(VPUtil.hasVestige(ModItems.EARS.get(), player)) {
                float armor = (float) player.getArmorValue();
                float exp = (float) player.experienceLevel;
                float specialAddition = (float)player.getAttribute(Attributes.MOVEMENT_SPEED).getValue()-1;
                if(player.getPersistentData().getBoolean("VPEarsSpecial") && specialAddition > 0.05f)
                    armor = armor+(specialAddition/0.05f);
                float chance = (Math.min(69, armor) / 100);
                float secondChance = (Math.min(90, exp / 10) / 100);
                if (Math.random() < chance) {
                    VPUtil.play(player,SoundEvents.ENDER_DRAGON_FLAP);
                    event.setCanceled(true);
                } else if (player.getPersistentData().getBoolean("VPEarsUlt") && Math.random() < secondChance) {
                    VPUtil.play(player,SoundEvents.ENDER_DRAGON_FLAP);
                    event.setCanceled(true);
                }
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST,receiveCanceled = true)
    public void deathEventLowest(LivingDeathEvent event){
        if(event.getEntity().getPersistentData().getLong("VPQueenDeath") >= System.currentTimeMillis()){
            event.getEntity().getPersistentData().putLong("VPQueenDeath",1);
        }
        if(!event.isCanceled()) {
            if (event.getSource().getEntity() instanceof Player player) {
                LivingEntity entity = event.getEntity();
                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                    CompoundTag tag = entity.getPersistentData();
                    if (player.getCommandSenderWorld().dimension() == Level.NETHER && player.getHealth() <= 1)
                        challange.setChallenge(5, challange.getChallenge(5) + 1, player);
                    if (entity.getType().getCategory() == MobCategory.MONSTER)
                        challange.addMonsterKill(entity.getType().toString(), player);
                    if (entity.getType().getCategory() == MobCategory.CREATURE)
                        challange.addMobTame(entity.getType().toString(), player);
                    if (VPUtil.isBoss(entity))
                        challange.addBossKill(entity.getType().toString(), player);
                    if(!player.isOnGround() && !entity.isOnGround() && !entity.isInWater())
                        challange.addCreatureKilledAir(event.getEntity().getType().toString(), player);
                    if(event.getSource().isExplosion())
                        challange.setChallenge(4,player);
                    if(entity instanceof EnderMan && player.getMainHandItem().getItem() instanceof TieredItem tieredItem)
                        challange.addTool(tieredItem.toString(),player);
                    if((entity instanceof Player || entity instanceof Warden) && hasCurses(10,player))
                        challange.setChallenge(12,10,player);
                    if(entity instanceof Warden)
                        challange.addDamageDo(event.getSource(),player);
                    if(entity.getType().getDescriptionId().equals(challange.getRandomEntity())) {
                        challange.setChallenge(14, player);
                        challange.setChaosTime(System.currentTimeMillis(),player);
                        challange.setRandomEntity(VPUtil.getRandomEntity(),player);
                    }
                });
                if(!entity.getPersistentData().getString("VPCrownDamage").isEmpty() && VPUtil.hasStellarVestige(ModItems.CROWN.get(), player))
                    VPUtil.addShield(player,(float)(entity.getMaxHealth()*0.1),true);
                if(event.getSource().isExplosion() && Math.random() < 0.5 && VPUtil.hasStellarVestige(ModItems.KILLER.get(), player)){
                    for(LivingEntity livingEntity: VPUtil.getEntitiesAround(entity,8,8,8,false)){
                        VPUtil.dealDamage(livingEntity,player, DamageSource.playerAttack(player).setExplosion(),300,1);
                    }
                }
                ICuriosHelper api = CuriosApi.getCuriosHelper();
                List list = api.findCurios(player, (stackInSlot) -> {
                    if(stackInSlot.getItem() instanceof Midas midas) {
                        int kill = 1;
                        if(player.getPersistentData().getFloat("VPMidasTouch") > 0){
                            player.getPersistentData().putFloat("VPMidasTouch",player.getPersistentData().getFloat("VPMidasTouch")-1);
                            if(VPUtil.isBoss(entity))
                                kill *= 10;
                            else kill *= 2;
                        }
                        midas.fuckNbtCheck1 = true;
                        midas.fuckNbtCheck2 = true;
                        stackInSlot.getOrCreateTag().putInt("VPKills",stackInSlot.getOrCreateTag().getInt("VPKills")+kill);
                        return true;
                    }
                    return false;
                });
                if(VPUtil.hasVestige(ModItems.PRISM.get(), player) && entity.getPersistentData().getLong("VPPrismBuff") > 0){
                    List list2 = api.findCurios(player, (stackInSlot) -> {
                        if(stackInSlot.getItem() instanceof Prism prism) {
                            ItemStack stack = stackInSlot;
                            double chance =  stack.getOrCreateTag().getInt("VPPrismKill");
                            chance /= 100;
                            VPUtil.dropEntityLoot(entity,player);
                            while (Math.random() < chance){
                                VPUtil.dropEntityLoot(entity,player);
                                chance /= 10;
                            }
                            prism.fuckNbt1 = true;
                            prism.fuckNbt2 = true;
                            stack.getOrCreateTag().putInt("VPPrismKill",stack.getOrCreateTag().getInt("VPPrismKill")+1);
                            return true;
                        }
                        return false;
                    });
                }
                if(VPUtil.hasVestige(ModItems.DEVOURER.get(), player) && VPUtil.isBoss(entity)){
                    List list3 = api.findCurios(player, (stackInSlot) -> {
                        if(stackInSlot.getItem() instanceof Devourer devourer) {
                            ItemStack stack = stackInSlot;
                            devourer.fuckNbt1 = true;
                            devourer.fuckNbt2 = true;
                            stack.getOrCreateTag().putInt("VPDevoured",stack.getOrCreateTag().getInt("VPDevoured")+1);
                            return true;
                        }
                        return false;
                    });
                }


            }
            if (event.getEntity() instanceof Player player){
                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                    challange.addDamageDie(event.getSource().getMsgId(),player);
                    challange.failChallenge(13,player);
                    challange.failChallenge(18,player);
                });

                if(VPUtil.hasVestige(ModItems.KILLER.get(), player) && player.getPersistentData().getLong("VPQueenDeath") != 1){
                    int percent = 800;
                    if(player.getPersistentData().getLong("VPQueenDeath") > 0){
                        percent = 8000;
                    }
                    for(LivingEntity entity: VPUtil.getEntitiesAround(player,40,40,40)){
                        VPUtil.dealDamage(entity,player, DamageSource.playerAttack(player).setExplosion(),percent,3);
                    }
                }
                if(VPUtil.hasVestige(ModItems.BOOK.get(), player)){
                    if(player.getPersistentData().getBoolean("VPBook")) {
                        if(event.getSource().getEntity() instanceof LivingEntity dealer)
                        VPUtil.enchantCurseAll(dealer);
                    } else VPUtil.enchantCurseAll(player);
                }
            }
            LivingEntity entity = event.getEntity();
            if(entity.level.getNearestPlayer(entity,10) != null && VPUtil.hasVestige(ModItems.CATALYST.get(), entity.level.getNearestPlayer(entity,10))) {
                List<MobEffectInstance> effectList = new ArrayList<>(VPUtil.getEffectsHas(entity,false));
                for (LivingEntity iterator : VPUtil.getEntitiesAround(entity, 10, 10, 10, false)) {
                    for(MobEffectInstance instance: effectList){
                        iterator.addEffect(instance);
                        if(Minecraft.getInstance().player != null)
                            VPUtil.spawnParticles(Minecraft.getInstance().player, ParticleTypes.BUBBLE_POP,iterator.getX(),iterator.getY(),iterator.getZ(),8,0,-0.5,0);
                    }
                }
            }
        } else {
            LivingEntity entity = event.getEntity();
            if(entity.getPersistentData().getLong("VPDeath") > 0){
                entity.setHealth(0);
                event.setCanceled(false);
            }
        }
    }

    /*@SubscribeEvent(priority = EventPriority.LOWEST,receiveCanceled = true)
    public void onPlayerDeath(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player player){
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                if(event.isCanceled()) {
                    System.out.println("canceled");
                }
            });
        }
    }*/
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
                if(player.level.isClientSide)
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
    public static void useEvent(PlayerInteractEvent.RightClickItem event){
        Player player = event.getEntity();
        if(event.getItemStack().getItem() instanceof EnderEyeItem && VPUtil.hasVestige(ModItems.ANOMALY.get(), player)){
            if(player instanceof ServerPlayer serverPlayer){
                ICuriosHelper api = CuriosApi.getCuriosHelper();
                List list = api.findCurios(player, (stackInSlot) -> {
                    if(stackInSlot.getItem() instanceof Anomaly anomaly) {
                        anomaly.fuckNbt1 = true;
                        anomaly.fuckNbt2 = true;
                        double x = stackInSlot.getOrCreateTag().getDouble("VPReturnX");
                        double y = stackInSlot.getOrCreateTag().getDouble("VPReturnY");
                        double z = stackInSlot.getOrCreateTag().getDouble("VPReturnZ");
                        String key = stackInSlot.getOrCreateTag().getString("VPReturnKey");
                        if(x != 0 && y != 0 && z != 0 && !key.isEmpty()) {
                            ServerLevel serverLevel = serverPlayer.level.getServer().getLevel(VPUtil.getWorldKey(key));
                            for(LivingEntity entity: VPUtil.getEntitiesAround(player,4,4,4,false)){
                                if(entity instanceof ServerPlayer victim)
                                    victim.teleportTo(serverLevel, x, y, z, 0, 0);
                                else {
                                    entity.changeDimension(serverLevel);
                                    entity.teleportTo(x,y,z);
                                }
                            }
                            serverPlayer.teleportTo(serverLevel, x, y, z, 0, 0);
                        } else {
                            for(LivingEntity entity: VPUtil.getEntitiesAround(player,4,4,4,false)){
                                entity.teleportTo(serverPlayer.getRespawnPosition().getX(),serverPlayer.getRespawnPosition().getY(),serverPlayer.getRespawnPosition().getZ());
                            }
                            serverPlayer.teleportTo(serverPlayer.getRespawnPosition().getX(),serverPlayer.getRespawnPosition().getY(),serverPlayer.getRespawnPosition().getZ());
                        }
                        return true;
                    }
                    return false;
                });
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
                String barsik = String.valueOf(cat.getCatVariant());
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
        if(player.level instanceof ServerLevel serverLevel)
            VPUtil.initMonstersAndBosses(serverLevel);
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
            float healingBonus = VPUtil.getHealBonus(entity);
            float resedHeal = 0;
            if(healingBonus < 0)
                resedHeal = (event.getAmount()*(healingBonus/100));
            event.setAmount(event.getAmount()+(event.getAmount()*(healingBonus/100)));
            float healDebt = tag.getFloat("HealDebt");
            if(healDebt > 0) {
                float lastHeal = event.getAmount();
                event.setAmount(Math.max(0, lastHeal - healDebt));
                if(healDebt-lastHeal > 0) {
                    resedHeal += lastHeal;
                    tag.putFloat("HealDebt", healDebt - lastHeal);
                }
                else {
                    resedHeal += event.getAmount();
                    tag.putFloat("HealDebt", 0);
                }
            }
            if(entity instanceof Player player) {
                if(!VPUtil.hasStellarVestige(ModItems.ARMOR.get(), player) || event.getAmount() > 1 && player.getPersistentData().getFloat("VPArmor") > 0)
                    player.getPersistentData().putFloat("VPArmor", 0);
                if(VPUtil.hasVestige(ModItems.FLOWER.get(), player) && player.getPersistentData().getLong("VPDonutSpecial") > 0){
                    for(LivingEntity livingEntity: VPUtil.getCreaturesAround(player,30,30,30)){
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
                    if(!VPUtil.hasStellarVestige(ModItems.ARMOR.get(), player) && player.getPersistentData().getFloat("VPArmor") > 0)
                        player.getPersistentData().putFloat("VPArmor", 0);
                    if(VPUtil.hasStellarVestige(ModItems.TRIGON.get(), player)) {
                        VPUtil.regenOverShield(player, overHeal);
                    }
                }
                if(entity.getPersistentData().getBoolean("VPSweetUlt")){
                    float saturation = entity.getPersistentData().getFloat("VPSaturation");
                    if(saturation + overHeal < 400)
                        entity.getPersistentData().putFloat("VPSaturation",overHeal+saturation);
                    else entity.getPersistentData().putFloat("VPSaturation",400);
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
    @SubscribeEvent
    public static void teleportEvent(EntityTeleportEvent event){
        if(event.getEntity() instanceof LivingEntity entity){
            if(entity.getPersistentData().getLong("VPAntiTP") > 0)
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void entityChangeDimension(EntityTravelToDimensionEvent event){
        if(event.getEntity() instanceof LivingEntity entity){
            if(entity.getPersistentData().getLong("VPAntiTP") > 0)
                event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public static void playerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event){
        Player player = event.getEntity();
        if(player.getPersistentData().getLong("VPAntiTP") > 0)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void struck(EntityStruckByLightningEvent event){
        if(event.getEntity() instanceof Player player){
            for(LivingEntity entity: VPUtil.getEntities(player,1,false)){
                if(entity instanceof Creeper) {
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
        CompoundTag tag = entity.getPersistentData();
        if (entity.getPersistentData().getInt("VPGravity") > 30)
            entity.getPersistentData().putInt("VPGravity", 30);
        if(entity.getPersistentData().getInt("VPSoulRottingStellar") >= 30)
            entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity,Attributes.MAX_HEALTH, UUID.fromString("addff144-f58a-4c10-9c69-726515295786"),entity.getHealth()-entity.getMaxHealth(), AttributeModifier.Operation.ADDITION, "vp:rotting_hp"));
        else entity.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(entity,Attributes.MAX_HEALTH, UUID.fromString("addff144-f58a-4c10-9c69-726515295786"),entity.getHealth()-entity.getMaxHealth(), AttributeModifier.Operation.ADDITION, "vp:rotting_hp"));
        if(entity.getPersistentData().getInt("VPSoulRottingStellar") >= 50)
            VPUtil.clearEffects(entity,true);
        if (entity.tickCount % 300 == 0) {
            if(tag.getFloat("VPHealResMask") != 0) {
                tag.putFloat("VPHealResMask", 0);
            }
            if(tag.getBoolean("MaskStellar")){
                tag.putBoolean("MaskStellar", false);
            }
        }
        if (entity.tickCount % 2000 == 0) {
            tag.putString("VPCrownDamage","");
        }
        if(entity.getPersistentData().getLong("VPAntiTP") != 0 && entity.getPersistentData().getLong("VPAntiTP") <= System.currentTimeMillis())
            entity.getPersistentData().putLong("VPAntiTP",0);
        if(entity.getPersistentData().getLong("VPDonutSpecial") != 0 && entity.getPersistentData().getLong("VPDonutSpecial") <= System.currentTimeMillis())
            entity.getPersistentData().putLong("VPDonutSpecial",0);
        if(entity.getPersistentData().getLong("VPFlowerStellar") != 0 && entity.getPersistentData().getLong("VPFlowerStellar") <= System.currentTimeMillis())
            entity.getPersistentData().putLong("VPFlowerStellar",0);
        if(entity.getPersistentData().getLong("VPAstral") != 0 && entity.getPersistentData().getLong("VPAstral") <= System.currentTimeMillis())
            entity.getPersistentData().putLong("VPAstral",0);
        if(entity.getPersistentData().getLong("VPPrismBuff") != 0 && entity.getPersistentData().getLong("VPPrismBuff") <= System.currentTimeMillis()) {
            entity.getPersistentData().putLong("VPPrismBuff", 0);
            entity.getPersistentData().putString("VPPrismDamage","");
        }
        if(entity.getPersistentData().getLong("VPEnchant") != 0 && entity.getPersistentData().getLong("VPEnchant") <= System.currentTimeMillis()) {
            entity.getPersistentData().putLong("VPEnchant", 0);
            VPUtil.negativnoDisenchant(entity);
        }
        if(entity.tickCount % 5 == 0 && entity instanceof ServerPlayer player){
            CompoundTag sendNudes = new CompoundTag();
            for (String key : player.getPersistentData().getAllKeys()) {
                if (key.startsWith("VP") && player.getPersistentData().get(key) != null) {
                    sendNudes.put(key, player.getPersistentData().get(key));
                }
            }
            PacketHandler.sendToClient(new SendPlayerNbtToClient(player.getUUID(),sendNudes),player);
        }
        if(tag.getLong("VPDeath") != 0 && tag.getLong("VPDeath")+ 40*1000 < System.currentTimeMillis())
            tag.putLong("VPDeath",0);
        if(event.getEntity() instanceof Player player){
            CompoundTag playerTag = player.getPersistentData();
            if(playerTag == null)
                playerTag = new CompoundTag();
            ICuriosHelper api = CuriosApi.getCuriosHelper();
            if(playerTag.getBoolean("VPButton1") || playerTag.getBoolean("VPButton3")){
                api.findFirstCurio(player, (stackInSlot) -> {
                    if(stackInSlot.getItem() instanceof Vestige vestige) {
                        if (player.getPersistentData().getBoolean("VPButton1")) {
                            vestige.setSpecialActive(vestige.getSpecialMaxTime(), player);
                        }
                        else {
                            vestige.setUltimateActive(vestige.getUltimateMaxTime(),player);
                        }
                        return true;
                    }
                    return false;
                });
                player.getPersistentData().putBoolean("VPButton1",false);
                player.getPersistentData().putBoolean("VPButton3",false);
            } else if(playerTag.getBoolean("VPButton2") || playerTag.getBoolean("VPButton4")) {
                List list = api.findCurios(player, (stackInSlot) -> {
                    if(stackInSlot.getItem() instanceof Vestige) {
                        return true;
                    }
                    return false;
                });
                SlotResult slotResult;
                if(list.size() > 1)
                    slotResult = (SlotResult) list.get(1);
                else slotResult =  api.findFirstCurio(player, (stackInSlot) -> stackInSlot.getItem() instanceof Vestige).orElse(null);
                if(slotResult != null) {
                    Vestige vestige = (Vestige) slotResult.stack().getItem();
                    if (player.getPersistentData().getBoolean("VPButton2")) {
                        vestige.setSpecialActive(vestige.getSpecialMaxTime(), player);
                    }
                    else {
                        vestige.setUltimateActive(vestige.getUltimateMaxTime(), player);
                    }
                }
                player.getPersistentData().putBoolean("VPButton2",false);
                player.getPersistentData().putBoolean("VPButton4",false);
            }
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                cap.addBiome(player);
                cap.addDimension(player,player.level.dimension().location().getPath());
                for(int i = 0; i < PlayerCapabilityVP.totalVestiges; i++){
                    if(cap.getChallenge(i+1) >= cap.getMaximum(i+1) && !cap.hasCoolDown(i+1)){
                        cap.giveVestige(player,i+1);
                    }
                }
                if(System.currentTimeMillis() - cap.getTimeCd() >= VPUtil.coolDown()) {
                    cap.clearCoolDown(player);
                    cap.addTimeCd(System.currentTimeMillis(),player);
                }
                if(player.tickCount % 24000 == 0 && !cap.getLore(player,2))
                    player.sendSystemMessage(Component.translatable("vp.sleep"));
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
                if(cap.getRandomEntity().length() == 0)
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
            event.addCapability(new ResourceLocation(VestigesOfPresent.MODID, "properties"), new PlayerCapabilityProviderVP());
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
        event.getOriginal().invalidateCaps();
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
    public static void onBreak(BlockEvent.BreakEvent event){
        Player player = event.getPlayer();
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            String flower = event.getState().getBlock().getDescriptionId();
            if(event.getState().getBlock() instanceof FlowerBlock flowerBlock) {
                cap.addFlower(flower, player);
                //System.out.println(flowerBlock.getStateDefinition().getOwner().isRandomlyTicking(event.getState()));
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
    public static void onPlaced(BlockEvent.EntityPlaceEvent event){
        if(event.getEntity() instanceof Player player) {
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                if (event.getState().getBlock() instanceof FlowerBlock) {
                    cap.failChallenge(16,player);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerClonedClient(ClientPlayerNetworkEvent.Clone event){
        event.getOldPlayer().reviveCaps();
        event.getOldPlayer().getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(oldStore -> {
            event.getNewPlayer().getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(newStore -> {
                newStore.copyNBT(oldStore);
            });
        });
        event.getOldPlayer().invalidateCaps();
    }
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
