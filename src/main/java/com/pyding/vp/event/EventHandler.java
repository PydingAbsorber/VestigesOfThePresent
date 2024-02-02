package com.pyding.vp.event;

import com.pyding.vp.VestigesOfPresent;
import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.capability.PlayerCapabilityVP;
import com.pyding.vp.commands.VPCommands;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.artifacts.Anemoculus;
import com.pyding.vp.item.artifacts.Crown;
import com.pyding.vp.item.artifacts.Midas;
import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ClientToServerPacket;
import com.pyding.vp.network.packets.SendPlayerNbtToClient;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
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
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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

import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

@Mod.EventBusSubscriber(modid = VestigesOfPresent.MODID)
public class EventHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void damageEventLowest(LivingDamageEvent event){
        LivingEntity entity = event.getEntity();
        CompoundTag tag = entity.getPersistentData();

        if(event.getAmount() <= 5)
            tag.putBoolean("VPCrownDR",true);
        if(entity.getPersistentData().getBoolean("VPKillerQueen")){
            entity.getPersistentData().putBoolean("VPKillerQueen",false);
            event.setAmount(event.getAmount()*1.7f);
        }
        if(event.getSource() == null)
            return;
        if(event.getSource().getEntity() instanceof Player player){
            if(event.getSource().isFall())
                player.getPersistentData().putInt("VPGravity",player.getPersistentData().getInt("VPGravity")+1);
            if(VPUtil.hasVestige(ModItems.ANEMOCULUS.get(),player) && !entity.isOnGround()){
                event.setAmount(event.getAmount()*7);
                VPUtil.equipmentDurability(10,entity,player,VPUtil.hasStellarVestige(ModItems.ANEMOCULUS.get(),player));
            }
            if(VPUtil.hasVestige(ModItems.MASK.get(), player))
                entity.getPersistentData().putFloat("HealDebt", (float) (entity.getPersistentData().getFloat("HealDebt")+entity.getPersistentData().getFloat("HealDebt")*0.01));
            if(entity.getHealth() <= entity.getMaxHealth()*0.5 && player.getPersistentData().getInt("VPMadness") > 0 && VPUtil.hasVestige(ModItems.MARK.get(), player)){
                event.setAmount(event.getAmount()*(2*player.getPersistentData().getInt("VPMadness")));
                event.getSource().bypassMagic();
                if(Math.random() < 0.5)
                    player.getPersistentData().putInt("VPMadness",player.getPersistentData().getInt("VPMadness")-1);
            }
            if(VPUtil.hasStellarVestige(ModItems.MIDAS.get(), player)){
                event.setAmount(event.getAmount()*(1+(float)(player.getAttribute(Attributes.LUCK).getValue()/100)));
            }

        }
        if(entity instanceof Player player){
            LivingEntity attacker = event.getEntity();
            double playerDamage = player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
            double entityDamage = attacker.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
            if(playerDamage > entityDamage)
                event.setAmount(event.getAmount()-(float)(playerDamage-entityDamage));
            else event.setAmount(event.getAmount()-(float)(entityDamage-playerDamage));
            if(player.getPersistentData().getBoolean("VPMarkUlt")){
                player.getPersistentData().putFloat("VPDamageReduced",event.getAmount()+player.getPersistentData().getFloat("VPDamageReduced"));
                event.setAmount(0);
            }
            if(VPUtil.hasVestige(ModItems.EARS.get(), player)){
                float chance = (Math.min(69,(float)player.getArmorValue())/100);
                float secondChance = (Math.min(90,(float)player.experienceLevel/10)/100);
                if(Math.random() < chance){
                    event.setAmount(0);
                    event.setCanceled(true);
                } else if (player.getPersistentData().getBoolean("VPEarsUlt") && Math.random() < secondChance) {
                    event.setAmount(0);
                    event.setCanceled(true);
                } else if(VPUtil.hasStellarVestige(ModItems.EARS.get(), player)) {
                    event.setAmount(event.getAmount()-(event.getAmount()*(float)(player.getArmorValue()/100)));
                }
            }
        }

        float shield = entity.getPersistentData().getFloat("VPShield");
        float damage = event.getAmount();
        if(shield > damage){
            entity.getPersistentData().putFloat("VPShield",shield-damage);
            event.setAmount(0);
        } else {
            event.setAmount(damage-shield);
            entity.getPersistentData().putFloat("VPShield",0);
        }
    }
    @SubscribeEvent
    public void totemUsing(LivingUseTotemEvent event){
        LivingEntity entity = event.getEntity();
        if(entity.getPersistentData().getLong("VPDeath") > 0)
            event.setCanceled(true);
    }
    @SubscribeEvent(priority = EventPriority.LOWEST,receiveCanceled = true)
    public void deathEventLowest(LivingDeathEvent event){
        if(event.getEntity().getPersistentData().getLong("VPQueenDeath") >= System.currentTimeMillis()){
            event.getEntity().getPersistentData().putLong("VPQueenDeath",1);
        }
        if(!event.isCanceled()) {
            if (event.getSource().getEntity() instanceof Player player) {
                LivingEntity entity = event.getEntity();
                if (player.getCommandSenderWorld().dimension() == Level.NETHER && player.getHealth() <= 1) {
                    player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                        challange.setChallenge(5, challange.getChallenge(5) + 1, player);
                    });
                }
                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                    CompoundTag tag = entity.getPersistentData();
                    if (entity.getType().getCategory() == MobCategory.MONSTER)
                        challange.addMonsterKill(entity.getType().toString(), player);
                    if (entity.getType().getCategory() == MobCategory.CREATURE)
                        challange.addMobTame(entity.getType().toString(), player);
                    if(!player.isOnGround() && !entity.isOnGround())
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
                        VPUtil.dealDamage(livingEntity,player, DamageSource.playerAttack(player).setExplosion(),300);
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
                if(VPUtil.hasVestige(ModItems.PRISM.get(), player)){

                }



            }
            if (event.getEntity() instanceof Player player){
                player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                    String damage = event.getSource().toString().substring("DamageSource (".length());
                    damage = damage.substring(0,damage.length()-1);
                    challange.addDamageDie(damage,player);
                    challange.failChallenge(13,player);
                });

                if(VPUtil.hasVestige(ModItems.KILLER.get(), player) && player.getPersistentData().getLong("VPQueenDeath") != 1){
                    int percent = 800;
                    if(player.getPersistentData().getLong("VPQueenDeath") > 0){
                        percent = 8000;
                    }
                    for(LivingEntity entity: VPUtil.getEntitiesAround(player,40,40,40)){
                        VPUtil.dealDamage(entity,player, DamageSource.playerAttack(player).setExplosion(),percent);
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

    @SubscribeEvent(priority = EventPriority.LOWEST,receiveCanceled = true)
    public void onPlayerDeath(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player player){
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(challange -> {
                if(event.isCanceled()) {
                    System.out.println("canceled");
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
            event.setAmount(event.getAmount()+(event.getAmount()*(healingBonus/100)));
            float healDebt = tag.getFloat("HealDebt");
            if(healDebt > 0) {
                float lastHeal = event.getAmount();
                event.setAmount(Math.max(0, lastHeal - healDebt));
                if(healDebt-lastHeal > 0)
                    tag.putFloat("HealDebt", healDebt - lastHeal);
                else tag.putFloat("HealDebt", 0);
            }
            if(entity.getHealth()+event.getAmount() > entity.getMaxHealth() || entity.getPersistentData().getBoolean("VPMarkUlt")) {
                float overHeal = entity.getHealth() + event.getAmount() - entity.getMaxHealth();
                if(entity.getPersistentData().getBoolean("VPMarkUlt"))
                    overHeal = event.getAmount();
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
    public static void tick(LivingEvent.LivingTickEvent event){
        LivingEntity entity = event.getEntity();
        CompoundTag tag = entity.getPersistentData();
        if (entity.tickCount % 300 == 0) {
            if(tag.getFloat("HealResMask") != 0) {
                tag.putFloat("HealResMask", 0);
            }
            if(tag.getBoolean("MaskStellar")){
                tag.putBoolean("MaskStellar", false);
            }
        }
        if (entity.tickCount % 2000 == 0) {
            tag.putString("VPCrownDamage","");
        }
        if(entity instanceof ServerPlayer player){  //entity.tickCount % 20 == 0 &&
            PacketHandler.sendToClient(new SendPlayerNbtToClient(player.getUUID(),player.getPersistentData()),player);
        }
        if(tag.getLong("VPDeath") != 0 && tag.getLong("VPDeath")+ 40*1000 < System.currentTimeMillis())
            tag.putLong("VPDeath",0);
        if(event.getEntity() instanceof Player player){
            CompoundTag playerTag = player.getPersistentData();
            if(playerTag == null)
                playerTag = new CompoundTag();
            ICuriosHelper api = CuriosApi.getCuriosHelper();
            if(playerTag.getBoolean("VPButton1")){
                playerTag.putBoolean("VPButton1",false);
                api.findFirstCurio(player, (stackInSlot) -> {
                    if(stackInSlot.getItem() instanceof Vestige vestige) {
                        if (!player.isShiftKeyDown())
                            vestige.setSpecialActive(vestige.getSpecialMaxTime(),player);
                        else vestige.setUltimateActive(vestige.getUltimateMaxTime(),player);
                        return true;
                    }
                    return false;
                });
            } else if(playerTag.getBoolean("VPButton2")) {
                playerTag.putBoolean("VPButton2", false);
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
                Vestige vestige = (Vestige) slotResult.stack().getItem();
                if (!player.isShiftKeyDown())
                    vestige.setSpecialActive(vestige.getSpecialMaxTime(),player);
                else vestige.setUltimateActive(vestige.getUltimateMaxTime(),player);
            }
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                cap.addBiome(player.level.getBiome(player.blockPosition()).toString(), player);
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
                    if(!cap.getLore(player,3))
                        cap.addLore(player,5);
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
        *//*player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap ->{
            cap.addLore(player,2);
        });*//*

        if(player.level.isClientSide)
            System.out.println("client!!!! :(((((");
        else System.out.println("YESS!!!!! :)))))");

        PacketHandler.sendToServer(new ClientToServerPacket());
    }*/
}
