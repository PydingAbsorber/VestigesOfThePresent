package com.pyding.vp.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.network.packets.SendEntityNbtToClient;
import com.pyding.vp.network.packets.SendPlayerNbtToClient;
import com.pyding.vp.network.packets.SoundPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSources;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class VPUtil {
    public static long coolDown(){
        return ConfigHandler.COMMON.cooldown.get()*60*60*1000;
    }
    public static String getRainbowString(String text) {
        StringBuilder coloredText = new StringBuilder();

        Random random = new Random();
        ChatFormatting[] colors = ChatFormatting.values();

        for (char letter : text.toCharArray()) {
            ChatFormatting color = colors[random.nextInt(colors.length - 1) + 1];
            coloredText.append(color).append(letter);
        }

        return coloredText.toString();
    }

    public static String getDarkString(String text) {
        StringBuilder coloredText = new StringBuilder();
        ChatFormatting[] colors = ChatFormatting.values();

        for (char letter : text.toCharArray()) {
            ChatFormatting color;
            if(Math.random() > 0.5){
                color = ChatFormatting.BLACK;
            } else {
                if(Math.random() > 0.5){
                    color = ChatFormatting.DARK_GRAY;
                }
                else color = ChatFormatting.GRAY;
            }
            coloredText.append(color).append(letter);
        }

        return coloredText.toString();
    }

    public static String generateRandomString(int length) {
        String characters = "≣≤≥≦≧≨≩≪≫≬≭≮≯≰≱≲≳≴≵≶≷≸≹≺≻≼≽≾≿⊀⊁⊂⊃⊄⊅⊆⊇⊈⊉⊊⊋⊌⊍⊎⊏⊐⊑⊒⊓⊔⊕⊖⊗⊘⊙⊚⊛⊜⊝⊞⊟⊠⊡⊢⊣⊤⊥⊦⊧⊨⊩⊪⊫⊬⊭⊮⊯⊰⊱⊲⊳⊴⊵⊶⊷⊸⊹⊺⊻⊼⊽⊾⊿⋀⋁⋂⋃⋄⋅⋆⋇⋈⋉⋊⋋⋌⋍⋎⋏⋐⋑⋒⋓⋔⋕⋖⋗⋘⋙⋚⋛⋜⋝⋞⋟▲△▴▵▶▷▸▹►▻▼▽▾▿◀◁◂◃◄◅◆◇◈◉◊○◌◍◎●◐◑◒◓◔◕◖◗◘◙◚◛◜◝◞◟◠◡◢◣◤◥◦◧◨◩◪◫◬◭◮◯☀☁☂☃☄★☆☇☈☉☊☋☌☍☎☏☐☑☒☓☖☗☚☛☜☝☞☟☠☡☢☣☤☥☦☧☨☩☪☫☬☭☮☯✁✂✃✄✆✇✈✉✌✍✎✏✐✑✒✓✔✕✖✗✘✙✚✛✜✝✞✟✠✡✢✣✤✥✦✧✩✪✫✬✭✮✯✰✱✲✳✴✵✶✷✸✹✺✻✼✽✾✿❀❁❂❃❄❅❆❇❈❉❊❋❍❏❐❑❒❖❡❢❣❤❥❦❧❘❙❚❛❜❝❞➱➲➳➴➵➶➷➸➘➙➚➛➜➝➞➟➠➡➢➣➤➥➦➧➨➩➪➫➬➭➮➯➉➔➹➺➻➼➽➾➿ൠൡ•‣‑‒–—―‖‗‘’‚‛“”„‟†‡‰′″‴‵‶‷‸‹›※‼‽‾‿⁀⁁⁂⁃⁄⁅⁆⁐⁑₰₱₲₳₴₵⃒⃓⃘⃙⃚⃑⃔⃕⃖⃗⃛⃜⃝⃞⃟⃠⃡⃢⃣℀℁ℂ℃℄℅℆ℇ℈℉ℊℋℌℍℎℏℐℑℒℓ℔ℕ№℗℘ℙℚℛℜℝ℞℟℠℡™℣ℤ℥Ω℧ℨ℩KÅℬℭ℮ℯℰℱℲℳℴ⅓⅔⅕⅖⅗⅘⅙⅚⅛⅜⅝⅞⅟ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩⅪⅫⅬℵℶℷℸℹ℻ⅅⅆⅇⅈⅉ⅍ⅎⅭⅮⅯⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅺⅻⅼⅽⅾⅿↀↁↂↄ←↑→↓↔↕↖↗↘↙↚↛↜↝↞↟↠↡↢↣↤↥↦↧↨↩↪↫↬↭↮↯↰↱↲↳↴↵↶↷↸↹↺↻↼↽↾↿⇀⇁⇂⇃⇄⇅⇆⇇⇈⇉⇊⇋⇌⇍⇎⇏⇐⇑⇒⇓⇔⇕⇖⇗⇘⇙⇚⇛⇜⇝⇞⇟⇠⇡⇢⇣⇤⇥⇦⇧⇨⇩⇪∀∁∂∃∄∅∆∇∈∉∊∋∌∍∎∏∐∑−∓∔∕∖∗∘∙√∛∜∝∞∟∠∡∢∣∤∥∦∧∨∩∪∫∬∭∮∯∰∱∲∳∴∵∶∷∸∹∺∻∼∽∾∿≀≁≂≃≄≅≆≇≈≉≊≋≌≍≎≏≐≑≒≓≔≕≖≗≘≙≚≛≜≝≞≟≠≡≢";

        Random random = new Random();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            result.append(characters.charAt(randomIndex));
        }

        return result.toString();
    }

    public static float missingHealth(LivingEntity entity){ //0-100
        return (1 - (entity.getHealth() / entity.getMaxHealth())) * 100;
    }

    public static List<LivingEntity> getEntities(Player player,double radius){
        return player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+radius,player.getY()+radius,player.getZ()+radius,player.getX()-radius,player.getY()-radius,player.getZ()-radius));
    }
    public static List<LivingEntity> getEntities(Player player,double radius,boolean self){
        if(self)
            return player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+radius,player.getY()+radius,player.getZ()+radius,player.getX()-radius,player.getY()-radius,player.getZ()-radius));
        else {
            List<LivingEntity> list = new ArrayList<>();
            for(LivingEntity entity: player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+radius,player.getY()+radius,player.getZ()+radius,player.getX()-radius,player.getY()-radius,player.getZ()-radius))){
                if(entity != player)
                    list.add(entity);
            }
            return list;
        }
    }

    public static float getAttack(Player player, boolean hasDurability){
        float attack = (float) player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        if(!hasDurability && player.getMainHandItem().getItem() instanceof TieredItem tieredItem){
            attack -= tieredItem.getTier().getAttackDamageBonus();
        }
        return attack;
    }

    public static List<EntityType<?>> entities = new ArrayList<>();

    public static List<EntityType<?>> getEntitiesList(){
        return entities;
    }

    public static List<EntityType<?>> getEntitiesListOfType(MobCategory category){
        return entities.stream().filter(entityType -> entityType.getCategory() == category).collect(Collectors.toList());
    }

    public void damageAoe(Player player,double radius,DamageSource source,float amount,float percentage,boolean self){
        for(LivingEntity entity: getEntities(player,radius)){
            if(!self && entity == player)
                continue;
            entity.hurt(source,amount*(percentage/100));
        }
    }

    public static void initEntities(){
        entities.addAll(ForgeRegistries.ENTITY_TYPES.getValues());
    }

    public static List<Item> items = new ArrayList<>();
    private static Set<ResourceKey<Biome>> biomeNames = new HashSet<>();
    public static void initBiomes(Level level){
        //biomeNames.addAll(ForgeRegistries.BIOMES.getKeys());
        biomeNames = level.registryAccess().registryOrThrow(Registries.BIOME).registryKeySet();
    }
    public static List<ResourceLocation> getBiomes(){
        List<ResourceLocation> list = new ArrayList<>();
        for(ResourceKey<Biome> key: biomeNames){
            list.add(key.location());
        }
        return list;
    }

    public static List getBiomesLeft(String list){
        List<String> biomeList = new ArrayList<>(Arrays.asList(list.split(",")));
        List<String> allList = new ArrayList<>();
        for(ResourceLocation location: getBiomes()){
            allList.add(location.getPath());
        }
        allList.removeAll(biomeList);
        return allList;
    }
    private static final Pattern PATTERN = Pattern.compile("minecraft:(\\w+)");

//Reference{ResourceKey[minecraft:worldgen/biome / minecraft:birch_forest]=net.minecraft.world.level.biome.Biome@e4348c0}
    public static void initItems(){
        for(Item item: ForgeRegistries.ITEMS){
            items.add(item);
        }
    }

    public static List<Item> getItems(){
        return items;
    }

    public static List<Item> foodItems = new ArrayList();
    public static List<Item> toolItems = new ArrayList();
    public static List<Item> getEdibleItems(){
        if(foodItems.size() < 1) {
            for (Item item : items) {
                if (item.isEdible())
                    foodItems.add(item);
            }
        }
        return foodItems;
    }

    public static List getFoodLeft(String list){
        List<String> foodList = new ArrayList<>(Arrays.asList(list.split(",")));
        List<String> allList = new ArrayList<>();
        for(Item type: foodItems){
            allList.add(type.toString());
        }
        allList.removeAll(foodList);
        return allList;
    }

    public static List<Item> getTools(){
        if(toolItems.size() < 1) {
            for (Item item : items) {
                if (item instanceof TieredItem)
                    toolItems.add(item);
            }
        }
        return toolItems;
    }

    public static List getToolLeft(String list){
        List<String> itemList = new ArrayList<>(Arrays.asList(list.split(",")));
        List<String> allList = new ArrayList<>();
        for(Item type: toolItems){
            allList.add(type.toString());
        }
        allList.removeAll(itemList);
        return allList;
    }
    public static String getRandomMob(){
        Random random = new Random();
        return entities.get(random.nextInt(entities.size())).toString();
    }
    public static List<EntityType<?>> monsterList = new ArrayList<>();
    public static List<EntityType<?>> bossList = new ArrayList<>();

    public static int getBossSize(){
        if(bossList.isEmpty())
            return 3;
        else return bossList.size();
    }

    public static void initMonstersAndBosses(Level level){
        if(!monsterList.isEmpty() || !bossList.isEmpty())
            return;
        for(EntityType<?> type: getEntitiesListOfType(MobCategory.MONSTER)){
            Entity entity = type.create(level);
            if (entity instanceof LivingEntity livingEntity) {
                double health = livingEntity.getMaxHealth();
                if (health > 190) {
                    bossList.add(type);
                } else monsterList.add(type);
            }
        }
    }
    public static List getMonsterLeft(String list){
        List<String> mobsList = new ArrayList<>(Arrays.asList(list.split(",")));
        List<String> allList = new ArrayList<>();
        for(EntityType<?> type: monsterList){
            allList.add(type.toString());
        }
        allList.removeAll(mobsList);
        List<String> filteredList = new ArrayList<>();
        for (String name: allList){
            if(name.contains("entity.minecraft."))
                name = name.substring("entity.minecraft.".length());
            filteredList.add(name);
        }
        return filteredList;
    }

    public static List getBossesLeft(String list){
        List<String> mobsList = new ArrayList<>(Arrays.asList(list.split(",")));
        List<String> allList = new ArrayList<>();
        for(EntityType<?> type: bossList){
            allList.add(type.toString());
        }
        allList.removeAll(mobsList);
        List<String> filteredList = new ArrayList<>();
        for (String name: allList){
            if(name.contains("entity.minecraft."))
                name = name.substring("entity.minecraft.".length());
            filteredList.add(name);
        }
        return filteredList;
    }

    public static List getMobsLeft(String list){
        List<String> mobsList = new ArrayList<>(Arrays.asList(list.split(",")));
        List<String> allList = new ArrayList<>();
        for(EntityType<?> type: getEntitiesListOfType(MobCategory.CREATURE)){
            allList.add(type.toString());
        }
        allList.removeAll(mobsList);
        List<String> filteredList = new ArrayList<>();
        for (String name: allList){
            if(name.contains("entity.minecraft."))
                name = name.substring("entity.minecraft.".length());
            filteredList.add(name);
        }
        return filteredList;
    }

    public static List<Block> blocks = new ArrayList<>();
    public static List<String> flowers = new ArrayList<>();

    public static void initBlocks(){
        for(Block block: ForgeRegistries.BLOCKS){
            blocks.add(block);
        }
    }

    public static void initFlowers(){
        for(Block block: blocks){
            if(block instanceof FlowerBlock)
                flowers.add(block.getDescriptionId());
            /*if(name.contains("flower")){
                flowers.add(block.getDescriptionId());
            } else {
                for(String element: vanillaFlowers){
                    if(name.contains(element))
                        flowers.add(block.getDescriptionId());
                }
            }*/
        }
    }

    public static List<String> getFlowers(){
        return flowers;
    }

    public static List<String> getFlowersLeft(String list){
        List<String> flowerList = new ArrayList<>(Arrays.asList(list.split(",")));
        List<String> allList = new ArrayList<>();
        for(String name: getFlowers()){
            allList.add(name);
        }
        allList.removeAll(flowerList);
        List<String> filteredList = new ArrayList<>();
        for (String name: allList){
            if (name.contains("block.minecraft."))
                name = name.substring("block.minecraft.".length());
            filteredList.add(name);
        }
        return filteredList;
    }

    public static String formatMilliseconds(long milliseconds) {
        if (milliseconds < 0) {
            return "Invalid input";
        }

        long seconds = milliseconds / 1000;
        long days = seconds / (24 * 3600);
        seconds = seconds % (24 * 3600);
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        StringBuilder formattedTime = new StringBuilder();

        if (days > 0) {
            formattedTime.append(days).append("d ");
        }
        if (hours > 0) {
            formattedTime.append(hours).append("h ");
        }
        if (minutes > 0) {
            formattedTime.append(minutes).append("m ");
        }
        if (seconds > 0 || formattedTime.length() == 0) {
            formattedTime.append(seconds).append("s");
        }

        return formattedTime.toString().trim();
    }

    public static void dealDamage(LivingEntity entity,Player player, DamageSource source, float percent, int type){
        if(isFriendlyFireBetween(entity,player))
            return;
        entity.invulnerableTime = 0;
        percent /= 100;
        ItemStack stack = player.getMainHandItem();
        float percentBonus = 1;
        if(type == 1)
            percentBonus += 0;
        else if(type == 2)
            percentBonus += player.getPersistentData().getFloat("VPTrigonBonus");
        else if(type == 3)
            percentBonus += player.getPersistentData().getInt("VPGravity")*2;
        boolean hasDurability = stack.isDamageableItem() && stack.getDamageValue()+1 < stack.getMaxDamage();
        if(hasDurability) {
            stack.hurtAndBreak(1, entity, consumer -> {
                consumer.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        DamageSource damageSource = new DamageSource(source.typeHolder(),player);
        if(source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            percentBonus /= 20;
        entity.hurt(damageSource,getAttack(player,hasDurability)*(percent+percentBonus));
    }

    public static void dealDamage(LivingEntity entity,Player player, DamageSource source, float damage, int type, boolean invulPierce){
        if(isFriendlyFireBetween(entity,player))
            return;
        if(invulPierce)
            entity.invulnerableTime = 0;
        ItemStack stack = player.getMainHandItem();
        float percentBonus = 1;
        if(type == 1)
            type += 0;
        else if(type == 2)
            percentBonus += player.getPersistentData().getFloat("VPTrigonBonus");
        else if(type == 3)
            percentBonus += player.getPersistentData().getInt("VPGravity")*20;
        boolean hasDurability = stack.isDamageableItem() && stack.getDamageValue()+1 < stack.getMaxDamage();
        if(hasDurability) {
            stack.hurtAndBreak(1, entity, consumer -> {
                consumer.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        entity.hurt(source,damage*(percentBonus));
    }

    public static void spawnLightning(ServerLevel world, double x, double y, double z) {
        LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(world);
        if (lightningBolt != null) {
            lightningBolt.moveTo(x, y, z);
            world.addFreshEntity(lightningBolt);
        }
    }

    public static final List<String> vanillaFlowers = Arrays.asList(
            "poppy", "dandelion", "lily_of_the_valley", "blue_orchid", "allium",
            "azure_bluet", "red_tulip", "orange_tulip", "white_tulip", "pink_tulip",
            "oxeye_daisy", "cornflower", "wither_rose", "sunflower", "lilac", "rose_bush", "peony"
    );

    public static final String damageSubtypes(){
        String damage = "";
        damage += "bypassArmor,";
        damage += "damageHelmet,";
        damage += "bypassEnchantments,";
        damage += "explosion,";
        damage += "bypassInvul,";
        damage += "bypassMagic,";
        damage += "fall,";
        damage += "magic,";
        damage += "noAggro,";
        damage += "projectile,";
        return damage;
    }

    public static void printDamage(Player player, LivingDamageEvent event){
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            if (cap.getDebug() && !player.getCommandSenderWorld().isClientSide) {
                player.sendSystemMessage(event.getEntity().getType().getDescription());
                player.sendSystemMessage(Component.literal("Damage source:§5 " + event.getSource().getMsgId() + "§r, amount after absorption:§5" + event.getAmount()));
                for(TagKey<DamageType> type : damageTypes()){
                    if(event.getSource().is(type))
                        player.sendSystemMessage(Component.translatable(type.location().toLanguageKey()));
                }
                float shield = getShield(event.getEntity());
                float overshield = getOverShield(event.getEntity());
                if(shield > 0)
                player.sendSystemMessage(Component.literal("§cHas Shields: " + shield));
                if(overshield > 0)
                player.sendSystemMessage(Component.literal("§dHas Over Shields: " + overshield));
                player.sendSystemMessage(Component.literal("\n"));
            }
        });
    }

    public static List<String> getDamageDoLeft(String list){
        List<String> damageList = new ArrayList<>(Arrays.asList(list.split(",")));
        List<String> allList = new ArrayList<>();
        for(TagKey<DamageType> key: damageTypes()){
            allList.add(key.location().getPath());
        }
        allList.removeAll(damageList);
        return allList;
    }

    public static EntityType getRandomEntity(){
        Random random = new Random();
        List<EntityType> allEntities = new ArrayList<>();
        allEntities.addAll(getEntitiesListOfType(MobCategory.CREATURE));
        allEntities.addAll(getEntitiesListOfType(MobCategory.MONSTER));
        return allEntities.get(random.nextInt(allEntities.size()));
    }

    public static void randomTeleportChorus(LivingEntity entity){
        double d0 = entity.getX();
        double d1 = entity.getY();
        double d2 = entity.getZ();

        for(int i = 0; i < 16; ++i) {
            double d3 = entity.getX() + (entity.getRandom().nextDouble() - 0.5D) * 16.0D;
            double d4 = Mth.clamp(entity.getY() + (double)(entity.getRandom().nextInt(16) - 8), 2, 2);
            double d5 = entity.getZ() + (entity.getRandom().nextDouble() - 0.5D) * 16.0D;
            if (entity.isPassenger()) {
                entity.stopRiding();
            }
            if (entity.randomTeleport(entity.getX(),entity.getY(),entity.getZ(), true)) {
                break;
            }
        }
    }

    public static int getChaosTime(){
        return ConfigHandler.COMMON.chaostime.get()*60*1000;
    }

    public static boolean hasVestige(Item item,Player player){
        if(!(item instanceof Vestige))
            return false;
        List<SlotResult> result = new ArrayList<>();
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            result.addAll(handler.findCurios(item));
        });
        return result.size() > 0;
    }


    public static ItemStack getVestigeStack(Vestige vestige,Player player){
        List<SlotResult> result = new ArrayList<>();
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            result.addAll(handler.findCurios(itemStack -> itemStack.getItem() == vestige));
        });
        return result.get(0).stack();
    }
    public static ItemStack getVestigeStack(Class vestige,Player player){
        List<SlotResult> result = new ArrayList<>();
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            result.addAll(handler.findCurios(itemStack -> itemStack.getItem().getClass() == vestige));
        });
        if(result.size() > 0)
            return result.get(0).stack();
        return null;
    }
    public static List<ItemStack> getFirstVestige(Player player){
        List<SlotResult> result = new ArrayList<>();
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            handler.findFirstCurio(itemStack -> itemStack.getItem() instanceof Vestige).ifPresent(result::add);
        });
        List<ItemStack> stacks = new ArrayList<>();
        for(SlotResult hitResult: result){
            stacks.add(hitResult.stack());
        }
        return stacks;
    }
    public static List<ItemStack> getVestigeList(Player player){
        List<SlotResult> result = new ArrayList<>();
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            result.addAll(handler.findCurios(itemStack -> itemStack.getItem() instanceof Vestige));
        });
        List<ItemStack> stacks = new ArrayList<>();
        for(SlotResult hitResult: result){
            stacks.add(hitResult.stack());
        }
        return stacks;
    }

    public static boolean hasStellarVestige(Item item,Player player){
        if(!(item instanceof Vestige))
            return false;
        List<SlotResult> result = new ArrayList<>();
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            result.addAll(handler.findCurios(item));
        });
        for(SlotResult stack: result){
            if(stack.stack().getItem() instanceof Vestige vestige && vestige.isStellar)
                return true;
        }
        return false;
    }
    public static void equipmentDurability(float percentage,LivingEntity entity) {
        percentage = percentage / 100;
        for (ItemStack stack2 : entity.getArmorSlots()) {
            int damage = (int) (stack2.getMaxDamage() * percentage);
            stack2.hurtAndBreak(damage, entity, consumer -> {
                consumer.broadcastBreakEvent(EquipmentSlot.CHEST);
            });
        }

        ItemStack stack = entity.getMainHandItem();
        if (stack.isDamageableItem()) {
            int damage = (int) (stack.getMaxDamage() * percentage);
            stack.hurtAndBreak(damage, entity, consumer -> {
                consumer.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        ItemStack stackLeft = entity.getOffhandItem();
        if (stackLeft.isDamageableItem()) {
            int damage = (int) (stackLeft.getMaxDamage() * percentage);
            stackLeft.hurtAndBreak(damage, entity, consumer -> {
                consumer.broadcastBreakEvent(EquipmentSlot.OFFHAND);
            });
        }
    }

    public static void equipmentDurability(float percentage,LivingEntity entity,Player dealer,boolean stellar){
        percentage = percentage / 100;
        int totalDamage = 0;
        for (ItemStack stack2 : entity.getArmorSlots()) {
            int damage = (int) (stack2.getMaxDamage() * percentage);
            stack2.hurtAndBreak(damage,entity,consumer -> {
                consumer.broadcastBreakEvent(EquipmentSlot.CHEST);
            });
            totalDamage += damage;
        }

        ItemStack stack = entity.getMainHandItem();
        if (stack.isDamageableItem()) {
            int damage = (int) (stack.getMaxDamage() * percentage);
            ItemStack finalStack = stack;
            stack.hurtAndBreak(damage,entity, consumer -> {
                consumer.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
            totalDamage += damage;
        }

        ItemStack stackLeft = entity.getOffhandItem();
        if (stackLeft.isDamageableItem()) {
            int damage = (int) (stackLeft.getMaxDamage() * percentage);
            stackLeft.hurtAndBreak(damage,entity,consumer -> {
                consumer.broadcastBreakEvent(EquipmentSlot.OFFHAND);
            });
            totalDamage += damage;
        }

        if(stellar){
            totalDamage /= 10;
            for (ItemStack stack2 : dealer.getInventory().armor) {
                int damage = stack2.getDamageValue();
                if(damage < totalDamage) {
                    stack2.setDamageValue(0);
                    totalDamage -= damage;
                } else {
                    stack2.setDamageValue(damage-totalDamage);
                    totalDamage = 0;
                }
            }
            ItemStack stack2;
            stack2 = dealer.getMainHandItem();
            if(stack2.isDamageableItem()){
                int damage = stack2.getDamageValue();
                if(damage < totalDamage) {
                    stack2.setDamageValue(0);
                    totalDamage -= damage;
                } else {
                    stack2.setDamageValue(damage-totalDamage);
                    totalDamage = 0;
                }
            }

            stack = dealer.getOffhandItem();
            if(stack.isDamageableItem()){
                int damage = stack2.getDamageValue();
                if(damage < totalDamage) {
                    stack2.setDamageValue(0);
                    totalDamage -= damage;
                } else {
                    stack2.setDamageValue(damage-totalDamage);
                    totalDamage = 0;
                }
            }
            if(totalDamage > 0)
                dealer.heal(totalDamage);
        }
    }

    public static double commonPower = 3;

    public static void liftEntity(LivingEntity entity,double power) {
        Vec3 motion = new Vec3(0, power, 0);
        entity.lerpMotion(motion.x, motion.y, motion.z);
        if(entity instanceof ServerPlayer player){
            PacketHandler.sendToClient(new PlayerFlyPacket(1),player);
        }
        entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 7 * 20));
        if(Math.random() < 0.5)
            play(entity,SoundRegistry.WIND1.get());
        else play(entity,SoundRegistry.WIND2.get());
    }

    public static void suckEntity(Entity entity1, Player player,int scale, boolean items) {
        if(entity1 instanceof ItemEntity entity){
            if(!items)
                return;
            Vec3 playerPos = player.position();
            Vec3 entityPos = entity.position();
            Vec3 direction = playerPos.subtract(entityPos).normalize();
            Vec3 motion = direction.scale(scale);
            entity.lerpMotion(motion.x, motion.y, motion.z);
        }
        if(entity1 instanceof LivingEntity entity) {
            if (entity == player)
                return;
            Vec3 playerPos = player.position();
            Vec3 entityPos = entity.position();
            Vec3 direction = playerPos.subtract(entityPos).normalize();
            Vec3 motion = direction.scale(scale);
            entity.lerpMotion(motion.x, motion.y, motion.z);
            if (entity instanceof ServerPlayer player2) {
                PacketHandler.sendToClient(new PlayerFlyPacket(1), player2);
            }
            entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 3 * 20));
        }
    }

    public static void suckEntity(Entity entity1, BlockPos pos,int scale, boolean items) {
        if(entity1 instanceof ItemEntity entity){
            if(!items)
                return;
            Vec3 playerPos = Vec3.atCenterOf(pos);
            Vec3 entityPos = entity.position();
            Vec3 direction = playerPos.subtract(entityPos).normalize();
            Vec3 motion = direction.scale(scale);
            entity.lerpMotion(motion.x, motion.y, motion.z);
        }
        if(entity1 instanceof LivingEntity entity) {
            Vec3 playerPos = Vec3.atCenterOf(pos);
            Vec3 entityPos = entity.position();
            Vec3 direction = playerPos.subtract(entityPos).normalize();
            Vec3 motion = direction.scale(scale);
            entity.lerpMotion(motion.x, motion.y, motion.z);
            if (entity instanceof ServerPlayer player2) {
                PacketHandler.sendToClient(new PlayerFlyPacket(1), player2);
            }
            entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 3 * 20));
        }
    }
    public static List<Attribute> attributeList(){
        List<Attribute> list = new ArrayList<>();
        list.add(Attributes.ATTACK_DAMAGE);
        list.add(Attributes.MOVEMENT_SPEED);
        list.add(Attributes.ATTACK_SPEED);
        list.add(Attributes.ARMOR);
        list.add(Attributes.ARMOR_TOUGHNESS);
        list.add(Attributes.ATTACK_KNOCKBACK);
        list.add(Attributes.FLYING_SPEED);
        list.add(Attributes.JUMP_STRENGTH);
        list.add(Attributes.LUCK);
        list.add(Attributes.MAX_HEALTH);
        list.add(Attributes.KNOCKBACK_RESISTANCE);
        return list;
    }

    public static int compareStats(LivingEntity owner, LivingEntity victim, boolean self){
        int selfStats = 0;
        int enemyStats = 0;
        AttributeMap map = owner.getAttributes();
        AttributeMap map2 = victim.getAttributes();
        List<Attribute> list = attributeList();
        for(int i = 0; i < list.size(); i++){
            if(map.hasAttribute(list.get(i)) && map2.hasAttribute(list.get(i))) {
                if (map.getValue(list.get(i)) > map2.getValue(list.get(i)))
                    selfStats++;
                else enemyStats++;
            } else if(map.hasAttribute(list.get(i)) && !map2.hasAttribute(list.get(i)))
                selfStats++;
            else enemyStats++;
        }
        if(self)
            return selfStats;
        else return enemyStats;
    }

    public static List<LivingEntity> getEntitiesAround(Player player,double x, double y, double z){
        return player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z));
    }
    public static List getEntitiesAroundOfType(Class entityClass,Player player,double x, double y, double z,boolean self){
        if(self)
            return player.getCommandSenderWorld().getEntitiesOfClass(entityClass, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z));
        else {
            List list = new ArrayList<>();
            for(Object entity: player.getCommandSenderWorld().getEntitiesOfClass(entityClass, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z))){
                if(entity != player)
                    list.add(entity);
            }
            return list;
        }
    }
    public static List<LivingEntity> getEntitiesAround(Player player,double x, double y, double z,boolean self){
        if(self)
            return player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z));
        else {
            List<LivingEntity> list = new ArrayList<>();
            for(LivingEntity entity: player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z))){
                if(entity != player)
                    list.add(entity);
            }
            return list;
        }
    }

    public static List<LivingEntity> getEntitiesAround(LivingEntity player,double x, double y, double z,boolean self){
        if(self)
            return player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z));
        else {
            List<LivingEntity> list = new ArrayList<>();
            for(LivingEntity entity: player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z))){
                if(entity != player)
                    list.add(entity);
            }
            return list;
        }
    }

    public static void adaptiveDamageHurt(LivingEntity entity, Player player, boolean adopted,float percent){
        int damage = entity.getPersistentData().getInt("VPCrownDamage");
        int cycle = entity.getPersistentData().getInt("VPCrownCycle");
        DamageSource source = new DamageSource(playerDamageSources(player,entity).get(damage).typeHolder(),player);
        while(entity.fireImmune() && source.is(DamageTypeTags.IS_FIRE) || entity.ignoreExplosion() && source.is(DamageTypeTags.IS_EXPLOSION)){
            damage++;
            source = playerDamageSources(player,entity).get(damage);
        }
        dealDamage(entity,player,source,percent,2);
        if(adopted  && cycle > 0) {
            entity.getPersistentData().putInt("VPCrownCycle",cycle-1);
            if(damage == playerDamageSources(player,player).size())
                damage = 0;
            entity.getPersistentData().putInt("VPCrownDamage", damage+1);
            entity.getPersistentData().putBoolean("VPCrownDR",false);
        }
    }

    public static DamageSource randomizeDamageType(Player player){
        List<DamageSource> sources = playerDamageSources(player,player);
        int numba = new Random().nextInt(sources.size());
        return sources.get(numba);
    }

    public static void addShield(LivingEntity entity,float amount,boolean add){
        if(entity.getPersistentData().getInt("VPSoulRotting") >= 10)
            return;
        CompoundTag tag = entity.getPersistentData();
        float shieldBonus = (entity.getPersistentData().getFloat("VPShieldBonusDonut")
        +entity.getPersistentData().getFloat("VPShieldBonusFlower"));
        float shield;
        if(!add) {
            if(amount*(1 + shieldBonus/100) > tag.getFloat("VPShield"))
                shield = amount;
            else return;
        }
        else shield = tag.getFloat("VPShield") + amount;
        shield = shield*(1 + shieldBonus/100);
        if(!tag.getBoolean("VPAntiShield")) {
            if(entity instanceof Player player && hasVestige(ModItems.SOULBLIGHTER.get(), player)){
                if(Math.random() < 0.2)
                    addOverShield(player,shield*0.05f);
                if(hasStellarVestige(ModItems.SOULBLIGHTER.get(), player)) {
                    boolean found = false;
                    for (LivingEntity entityTarget : VPUtil.getEntities(player, 30, false)) {
                        if (entityTarget.getPersistentData().hasUUID("VPPlayer")) {
                            if (entityTarget.getPersistentData().getUUID("VPPlayer").equals(player.getUUID())) {
                                found = true;
                                entityTarget.getPersistentData().putFloat("VPShield", entityTarget.getPersistentData().getFloat("VPShield") + amount * (1 + shieldBonus / 100));
                            }
                        }
                    }
                    if (!found)
                        tag.putFloat("VPShield", shield);
                }
            }
            else tag.putFloat("VPShield", shield);
        }
        if(entity instanceof ServerPlayer player) {
            PacketHandler.sendToClient(new SendPlayerNbtToClient(player.getUUID(), player.getPersistentData()),player);
        } else {
            if(!entity.getCommandSenderWorld().isClientSide)
                PacketHandler.sendToClients(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SendEntityNbtToClient(entity.getPersistentData(),entity.getId()));
        }
        if(tag.getFloat("VPShieldInit") == 0) {
            tag.putFloat("VPShieldInit", shield);
            play(entity,SoundRegistry.SHIELD.get());
        }
    }

    public static float getShield(LivingEntity entity){
        CompoundTag tag = entity.getPersistentData();
        return tag.getFloat("VPShield");
    }

    public static float getOverShield(LivingEntity entity){
        CompoundTag tag = entity.getPersistentData();
        return tag.getFloat("VPOverShield");
    }

    public static void deadInside(LivingEntity entity,Player player){
        if(Math.random() < 0.5)
            play(entity,SoundRegistry.DEATH1.get());
        else play(entity,SoundRegistry.DEATH2.get());
        entity.getPersistentData().putLong("VPDeath",System.currentTimeMillis()+40000);
        entity.setHealth(0);
        entity.die(player.damageSources().playerAttack(player));
    }
    public static void deadInside(LivingEntity entity){
        entity.getPersistentData().putLong("VPDeath",System.currentTimeMillis()+40000);
        entity.setHealth(0);
        entity.die(entity.damageSources().generic());
    }

    public static List<LivingEntity> ray(Player player, float range, int maxDist, boolean stopWhenFound) {
        Vector3 target = Vector3.fromEntityCenter(player);
        List<LivingEntity> entities = new ArrayList<>();

        for (int distance = 1; distance < maxDist; ++distance) {
            target = target.add(new Vector3(player.getLookAngle()).multiply(distance)).add(0.0, 0.5, 0.0);
            List<LivingEntity> list = player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(target.x - range, target.y - range, target.z - range, target.x + range, target.y + range, target.z + range));
            list.removeIf(entity -> entity == player || !player.hasLineOfSight(entity));
            for(LivingEntity entity: list){
                if(!entities.contains(entity))
                    entities.add(entity);
            }

            if (stopWhenFound && entities.size() > 0) {
                break;
            }
        }

        return entities;
    }

    public static BlockPos rayPose(Player player, int maxDist) {
        Vector3 target = Vector3.fromEntityCenter(player);
        for (int distance = 1; distance < maxDist; ++distance) {
            target = target.add(new Vector3(player.getLookAngle()).multiply(distance)).add(0.0, 0.5, 0.0);
        }
        return new BlockPos((int) target.x,(int)target.y,(int)target.z);
    }

    public static void pullEntityTowardsCoordinates(Entity entity, BlockPos pos, double strength) {
        double targetX = pos.getX();
        double targetY = pos.getY();
        double targetZ = pos.getZ();
        double dX = targetX - entity.getX();
        double dY = targetY - entity.getY();
        double dZ = targetZ - entity.getZ();

        double distance = Math.sqrt(dX * dX + dY * dY + dZ * dZ);

        if (distance != 0.0D) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(dX / distance * strength, dY / distance * strength, dZ / distance * strength));
        }
    }

    @Nullable
    public static <T extends LivingEntity> T getClosestEntity(List<? extends T> entities, Predicate<LivingEntity> predicate, double x, double y, double z) {
        double d0 = -1.0D;
        T t = null;

        for (T t1 : entities) {
            if (predicate.test(t1)) {
                double d1 = t1.distanceToSqr(x, y, z);
                if (d0 == -1.0D || d1 < d0) {
                    d0 = d1;
                    t = t1;
                }
            }
        }

        return t;
    }

    public static void fall(LivingEntity entity,double power) {
        Vec3 motion = new Vec3(0, power, 0);
        entity.lerpMotion(motion.x, motion.y, motion.z);
        if(entity instanceof ServerPlayer player){
            PacketHandler.sendToClient(new PlayerFlyPacket(1),player);
        }
    }

    public static float getHealBonus(LivingEntity entity){
        CompoundTag tag = entity.getPersistentData();
        float healBonus = Math.max(-99,tag.getFloat("VPHealResMask")+tag.getFloat("VPHealResFlower")+tag.getFloat("VPHealBonusDonut")+tag.getFloat("VPHealBonusDonutPassive"));
        return healBonus;
    }

    public static double calculatePercentageDifference(double number1, double number2) {
        if (number1 == number2) {
            return 0;
        }

        double average = (number1 + number2) / 2.0;
        double difference = Math.abs(number1 - number2);
        return (difference / average) * 100.0;
    }

    public static boolean isBoss(LivingEntity entity){
        if(entity.getMaxHealth() >= 190 || entity instanceof WitherBoss || entity instanceof EnderDragon || entity instanceof Warden)
            return true;
        return false;
    }

    public static void dropEntityLoot(LivingEntity entity, Player player) {
        if (entity instanceof Player) {
            return;
        }
        if(player.getCommandSenderWorld() instanceof ServerLevel serverLevel) {
            ResourceLocation lootTableLocation = entity.getLootTable();
            LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(lootTableLocation);
            Map<LootContextParam<?>, Object> lootparams = new HashMap<>();
            lootparams.put(LootContextParams.ORIGIN, entity.position());
            lootparams.put(LootContextParams.THIS_ENTITY, entity);
            lootparams.put(LootContextParams.DAMAGE_SOURCE, player.damageSources().playerAttack(player));
            LootParams params = new LootParams(serverLevel,lootparams,null,1);
            //LootContext.Builder builder = new LootContext.Builder();
            List<ItemStack> list = lootTable.getRandomItems(params);
            for(ItemStack stack: list){
                ItemEntity itemEntity = new ItemEntity(serverLevel,entity.getX(),entity.getY(),entity.getZ(),stack);
                serverLevel.addFreshEntity(itemEntity);
            }
        }
    }

    @SafeVarargs
    public static <T> T getRandomElement(List<T> list, T... excluding) {
        List<T> filtered = new ArrayList<>(list);
        Arrays.stream(excluding).forEach(filtered::remove);
        Random random = new Random();
        if (filtered.size() <= 0)
            throw new IllegalArgumentException("List has no valid elements to choose");
        else if (filtered.size() == 1)
            return filtered.get(0);
        else
            return filtered.get(random.nextInt(filtered.size()));
    }
    public static List<ResourceKey<Level>> worlds = new ArrayList<>();
    public static void initWorlds(){

    }

    public static ResourceKey<Level> getWorldKey(String name){
        ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(name));
        return key;
    }

    public static MobEffect getRandomEffect(boolean isBenefit){
        MobEffect effect;
        Random random = new Random();
        List<MobEffect> effects = new ArrayList<>();
        for(MobEffect element: ForgeRegistries.MOB_EFFECTS){
            if(element.isBeneficial() == isBenefit)
                effects.add(element);
        }
        int numba = random.nextInt(effects.size());
        effect = effects.get(numba);
        return effect;
    }
    public static List<MobEffectInstance> getEffectsHas(LivingEntity entity, boolean isBenefit){
        List<MobEffectInstance> effects = new ArrayList<>();
        Iterator<MobEffectInstance> iterator = entity.getActiveEffects().iterator();
        while (iterator.hasNext()) {
            MobEffectInstance effectInstance = iterator.next();
            MobEffect effect = effectInstance.getEffect();
            if (effect.isBeneficial() == isBenefit) {
                effects.add(effectInstance);
            }
        }
        return effects;
    }
    public static void clearEffects(LivingEntity entity, boolean isBeneficial){
        List<MobEffectInstance> list = new ArrayList<>(entity.getActiveEffects());
        for(MobEffectInstance instance: list){
            MobEffect effect = instance.getEffect();
            if(effect.isBeneficial() == isBeneficial)
                entity.removeEffect(effect);
        }
    }
    public static List<ItemStack> getAllEquipment(LivingEntity entity){
        List<ItemStack> list = new ArrayList<>();
        for(ItemStack stack: entity.getArmorSlots())
            list.add(stack);
        list.add(entity.getMainHandItem());
        list.add(entity.getOffhandItem());
        return list;
    }
    public static void repairAll(Player player, int totalDamage){
        for (ItemStack stack : getAllEquipment(player)) {
            int damage = stack.getDamageValue();
            if(damage < totalDamage) {
                stack.setDamageValue(0);
                totalDamage -= damage;
            } else {
                stack.setDamageValue(damage-totalDamage);
                totalDamage = 0;
            }
        }
        if(totalDamage > 0)
            player.giveExperiencePoints(totalDamage);
    }

    public static void negativnoEnchant(LivingEntity entity){
        for(ItemStack stack: getAllEquipment(entity)){
            List<Integer> lvl = new ArrayList<>();
            List<Enchantment> enchantments = new ArrayList<>();
            if(stack.isEnchanted()){
                for(Enchantment enchantment: stack.getAllEnchantments().keySet()){
                    if(stack.getEnchantmentLevel(enchantment) < 0)
                        continue;
                    lvl.add(stack.getEnchantmentLevel(enchantment));
                    enchantments.add(enchantment);
                }
                stack.getEnchantmentTags().clear();
                for(int i = 0;i < lvl.size();i++){
                    stack.enchant(enchantments.get(i),lvl.get(i)*-1);
                }
                stack.getOrCreateTag().putBoolean("VPEnchant",true);
            }
        }
        entity.getPersistentData().putLong("VPEnchant",System.currentTimeMillis()+15000);
    }

    public static void negativnoDisenchant(LivingEntity entity){
        for(ItemStack stack: getAllEquipment(entity)){
            List<Integer> lvl = new ArrayList<>();
            List<Enchantment> enchantments = new ArrayList<>();
            if(stack.isEnchanted()){
                for(Enchantment enchantment: stack.getAllEnchantments().keySet()){
                    if(stack.getEnchantmentLevel(enchantment) > 0)
                        continue;
                    lvl.add(stack.getEnchantmentLevel(enchantment));
                    enchantments.add(enchantment);
                }
                stack.getEnchantmentTags().clear();
                for(int i = 0;i < lvl.size();i++){
                    stack.enchant(enchantments.get(i),lvl.get(i)*-1);
                }
                stack.getOrCreateTag().putBoolean("VPEnchant",false);
            }
        }
    }

    public static void enchantCurseAll(LivingEntity entity){
        for(ItemStack stack: getAllEquipment(entity)){
            stack.getEnchantmentTags().clear();
            stack.enchant(Enchantments.BINDING_CURSE,1);
            stack.enchant(Enchantments.VANISHING_CURSE,1);
        }
    }

    public static LivingEntity getRandomEntityNear(Player player,LivingEntity entityMain,boolean playerCount){
        for (LivingEntity entity: getEntities(player,10,false)) {
            if(entity == entityMain)
                continue;
            if(entity instanceof Player) {
                if(playerCount) {
                    return entity;
                }
            } else return entity;
        }
        return null;
    }

    public static String generateRandomDamageType(){
        Random random = new Random();
        List<String> list = new ArrayList<>(Arrays.asList(damageSubtypes().split(",")));
        return list.get(random.nextInt(list.size()));
    }
    public static void damageAdoption(LivingEntity entity, LivingDamageEvent event){
        int damage = entity.getPersistentData().getInt("VPPrismDamage");
        if(damage == 0)
            return;
        List<DamageSource> sources = playerDamageSources(entity,entity);
        DamageSource weak = sources.get(damage-1);
        System.out.println("prism");
        System.out.println(weak);
        System.out.println(event.getSource());
        if(event.getSource() == weak){
            event.setAmount(event.getAmount()*2);
        }
        else {
            event.setAmount(0);
            event.setCanceled(true);
        }
    }

    public static void suckToPos(Entity entity, BlockPos targetPos, double maxPullStrength) {
        Vec3 targetVec = Vec3.atCenterOf(targetPos);
        Vec3 entityVec = entity.position();

        Vec3 direction = targetVec.subtract(entityVec);
        double distance = direction.length();

        Vec3 normalizedDirection = direction.normalize();
        double pullStrength = Math.min(maxPullStrength, distance / 10.0);

        Vec3 pullVelocity = normalizedDirection.scale(pullStrength);

        entity.setDeltaMovement(pullVelocity.x, pullVelocity.y, pullVelocity.z);
    }

    public static Multimap<Attribute, AttributeModifier> createAttributeMap(LivingEntity entity, Attribute attribute, UUID uuid, float amount, AttributeModifier.Operation operation, String name) {
        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();
        attributesDefault.put(attribute, new AttributeModifier(uuid, name, amount, operation));
        return attributesDefault;
    }

    public static List<LivingEntity> getMonstersAround(Player player,double x, double y, double z){
        List<LivingEntity> list = new ArrayList<>();
        for(LivingEntity entity: player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z))){
            if(!(entity instanceof Animal) && entity != player)
                list.add(entity);
        }
        return list;
    }

    public static List<LivingEntity> getCreaturesAround(Player player,double x, double y, double z){
        List<LivingEntity> list = new ArrayList<>();
        for(LivingEntity entity: player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z))){
            if(entity instanceof Animal)
                list.add(entity);
        }
        return list;
    }

    public static boolean isDamagePhysical(DamageSource source){
        return !source.is(DamageTypes.IN_FIRE) && !source.is(DamageTypes.MAGIC) && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS) && !source.is(DamageTypeTags.BYPASSES_EFFECTS);
    }

    public static boolean isFriendlyFireBetween(Entity attacker, Entity target) {
        if (attacker == null || target == null)
            return false;
        var team = attacker.getTeam();
        if (team != null) {
            return team.isAlliedTo(target.getTeam()) && !team.isAllowFriendlyFire();
        }
        return attacker.isAlliedTo(target);
    }
    public static void spawnParticles(Player player, ParticleOptions particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ) {
        if(player == null)
            return;
        Random random = new Random();
        for(int i = 0; i < count; i++) {
            double numba = random.nextInt(2);
            if(Math.random() > 0.5) {
                x += numba;
                y += numba;
                z += numba;
            }
            else {
                x -= numba;
                y -= numba;
                z -= numba;
            }
            player.getCommandSenderWorld().addParticle(particle, x, y, z, deltaX, deltaY, deltaZ);
        }
    }
    public static void spawnParticles(Player player, ParticleOptions particle,double radius, int count, double deltaX, double deltaY, double deltaZ, double speed, boolean force) {
        if(player == null)
            return;
        double startX = player.getX() - radius;
        double startY = player.getY() - radius;
        double startZ = player.getZ() - radius;
        double endX = player.getX() + radius;
        double endY = player.getY() + radius;
        double endZ = player.getZ() + radius;
        Random random = new Random();
        for (double x = startX; x <= endX; x += 1.0) {
            for (double y = startY; y <= endY; y += 1.0) {
                for (double z = startZ; z <= endZ; z += 1.0) {
                    double numba = random.nextInt(2);
                    if(Math.random() > 0.5) {
                        x += numba;
                        y += numba;
                        z += numba;
                    }
                    else {
                        x -= numba;
                        y -= numba;
                        z -= numba;
                    }
                    player.getCommandSenderWorld().addParticle(particle, x, y, z, deltaX, deltaY, deltaZ);
                }
            }
        }
    }
    public static void rayParticles(Player player, ParticleOptions particle,double distance,double radius, int count, double deltaX, double deltaY, double deltaZ, double speed, boolean force) {
        if(player == null)
            return;
        Random random = new Random();
        BlockPos pos = rayCords(player,player.getCommandSenderWorld(),distance);
        double startX = pos.getX() - radius;
        double startY = pos.getY() - radius;
        double startZ = pos.getZ() - radius;
        double endX = pos.getX() + radius;
        double endY = pos.getY() + radius;
        double endZ = pos.getZ() + radius;

        for (double x = startX; x <= endX; x += 1.0) {
            for (double y = startY; y <= endY; y += 1.0) {
                for (double z = startZ; z <= endZ; z += 1.0) {
                    double numba = random.nextInt(2);
                    if(Math.random() > 0.5) {
                        x += numba;
                        y += numba;
                        z += numba;
                    }
                    else {
                        x -= numba;
                        y -= numba;
                        z -= numba;
                    }
                    player.getCommandSenderWorld().addParticle(particle, x, y, z, deltaX, deltaY, deltaZ);
                }
            }
        }
    }

    public static boolean isEntityLookingAt(LivingEntity looker, Entity seen, double degree) {
        degree *= 1 + (looker.distanceTo(seen) * 0.1);
        Vec3 vec3 = looker.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(seen.getX() - looker.getX(), seen.getBoundingBox().minY + (double) seen.getEyeHeight() - (looker.getY() + (double) looker.getEyeHeight()), seen.getZ() - looker.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > 1.0D - degree / d0 && looker.hasLineOfSight(seen);
    }

    public static BlockPos rayCords(LivingEntity entity, Level level, double distance){
        Vec3 eyePosition = entity.getEyePosition(1.0F);
        Vec3 viewDirection = entity.getViewVector(1.0F);
        Vec3 targetPosition = eyePosition.add(viewDirection.x * distance, viewDirection.y * distance, viewDirection.z * distance);
        BlockHitResult hitResult = level.clip(new ClipContext(eyePosition, targetPosition, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
        BlockPos finalBlockPos;
        Vec3i what = new Vec3i((int) (viewDirection.x * distance), (int) (viewDirection.y * distance), (int) (viewDirection.z * distance));
        if (hitResult.getType() == BlockHitResult.Type.BLOCK) {
            finalBlockPos = hitResult.getBlockPos();
        } else {
            Vec3 end = eyePosition.add(entity.getLookAngle().scale(distance));
            finalBlockPos = new BlockPos((int) end.x,(int)end.y,(int)end.z);
            //finalBlockPos = new BlockPos((int) (viewDirection.x * distance), (int) (viewDirection.y * distance), (int) (viewDirection.z * distance));
        }

        return finalBlockPos;
    }

    public static void moveSpiral(Entity entity, BlockPos targetPos, float deltaTime) {
        Vec3 targetCenter = Vec3.atCenterOf(targetPos);
        Vec3 currentPosition = entity.position();
        Vec3 directionToCenter = targetCenter.subtract(currentPosition).normalize();
        double distanceToCenter = currentPosition.distanceTo(targetCenter);

        // theta - угол для создания спирального эффекта, зависит от времени или расстояния
        double theta = 4.0 * Math.PI * (1.0 - distanceToCenter / 100.0); // Примерное значение, требует настройки
        Vec3 spiralVector = new Vec3(Math.cos(theta), 0, Math.sin(theta)).scale(0.5); // Масштабирование для контроля скорости

        Vec3 combinedDirection = directionToCenter.add(spiralVector).normalize();
        double speedMultiplier = 1.0 + (1.0 - distanceToCenter / 100.0) * 5.0; // Ускорение ближе к центру

        Vec3 newVelocity = combinedDirection.scale(speedMultiplier);
        Vec3 currentVelocity = entity.getDeltaMovement();
        Vec3 smoothedVelocity = currentVelocity.add(newVelocity.subtract(currentVelocity).scale(deltaTime));
        entity.setDeltaMovement(smoothedVelocity);
    }

    public static void play(LivingEntity entity, SoundEvent sound){
        if(entity.getCommandSenderWorld().isClientSide)
            entity.playSound(sound,1,1);
        else {
            if(entity instanceof Player player) {
                PacketHandler.sendToClients(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new SoundPacket(sound.getLocation(), 1, 1));
            }
        }
    }

    public static void addOverShield(LivingEntity entity,float amount){
        if(entity.getPersistentData().getInt("VPSoulRotting") >= 10)
            return;
        CompoundTag tag = entity.getPersistentData();
        if(tag.getFloat("VPOverShield") <= 0)
            play(entity,SoundRegistry.OVERSHIELD.get());
        float shieldBonus = (entity.getPersistentData().getFloat("VPShieldBonusDonut")
                +entity.getPersistentData().getFloat("VPShieldBonusFlower"));
        float shield = (tag.getFloat("VPOverShield") + amount)*(1 + shieldBonus/100);
        if(!tag.getBoolean("VPAntiShield")) {
            if(entity instanceof Player player && hasStellarVestige(ModItems.SOULBLIGHTER.get(), player)){
                boolean found = false;
                for (LivingEntity entityTarget: VPUtil.getEntities(player,30,false)) {
                    if (entityTarget.getPersistentData().hasUUID("VPPlayer")){
                        if(entityTarget.getPersistentData().getUUID("VPPlayer").equals(player.getUUID())){
                            found = true;
                            entityTarget.getPersistentData().putFloat("VPOverShield", entityTarget.getPersistentData().getFloat("VPOverShield")+amount*(1 + shieldBonus/100));
                        }
                    }
                }
                if(!found)
                    tag.putFloat("VPOverShield", shield);
            } else tag.putFloat("VPOverShield", shield);
        }
        if(entity instanceof ServerPlayer player) {
            PacketHandler.sendToClient(new SendPlayerNbtToClient(player.getUUID(), player.getPersistentData()),player);
        } else {
            if(!entity.getCommandSenderWorld().isClientSide)
                PacketHandler.sendToClients(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SendEntityNbtToClient(entity.getPersistentData(),entity.getId()));
        }
        if(tag.getFloat("VPOverShieldMax") < shield) {
            tag.putFloat("VPOverShieldMax", shield);
        }
    }

    public static void regenOverShield(LivingEntity entity,float amount){
        if(entity.getPersistentData().getInt("VPSoulRotting") >= 10)
            return;
        CompoundTag tag = entity.getPersistentData();
        float shieldBonus = (entity.getPersistentData().getFloat("VPShieldBonusDonut")
                +entity.getPersistentData().getFloat("VPShieldBonusFlower"));
        if(tag.getFloat("VPOverShield") <= 0)
            return;
        float shield = (tag.getFloat("VPOverShield") + amount*(1 + shieldBonus/100));
        if(!tag.getBoolean("VPAntiShield")) {
            if(tag.getFloat("VPOverShieldMax") >= shield)
                tag.putFloat("VPOverShield", shield);
            else tag.putFloat("VPOverShield", tag.getFloat("VPOverShieldMax"));
        }
        if(entity instanceof ServerPlayer player) {
            PacketHandler.sendToClient(new SendPlayerNbtToClient(player.getUUID(), player.getPersistentData()),player);
        } else {
            if(!entity.getCommandSenderWorld().isClientSide)
                PacketHandler.sendToClients(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SendEntityNbtToClient(entity.getPersistentData(),entity.getId()));
        }
    }

    public static ResourceLocation getCurrentBiome(Player player) {
        if (player.getCommandSenderWorld() instanceof ServerLevel serverLevel) {
            BlockPos pos = player.blockPosition();
            return serverLevel.getBiomeManager().getBiome(pos).unwrapKey().get().location();
        }
        return null;
    }

    public static Optional<Vector3> findValidPosition(ServerPlayer player, Level world, int x, int y, int z) {
        int checkAxis = y - 10;

        for (int counter = 0; counter <= checkAxis; counter++) {
            BlockPos below = new BlockPos(x, y - counter - 1, z);
            BlockPos feet = new BlockPos(x, y - counter, z);
            BlockPos head = new BlockPos(x, y - counter + 1, z);

            if (world.getMinBuildHeight() >= below.getY())
                return Optional.empty();

            if (!world.isEmptyBlock(below) && world.getBlockState(below).canOcclude() && world.isEmptyBlock(feet) && world.isEmptyBlock(head))
                return Optional.of(new Vector3(feet.getX() + 0.5, feet.getY(), feet.getZ() + 0.5));
        }

        return Optional.empty();
    }

    public static List<MobEffect> effects = new ArrayList<>();

    public static void initEffects(){
        for(MobEffect effect: ForgeRegistries.MOB_EFFECTS){
            effects.add(effect);
        }
    }

    public static List<MobEffect> getEffects(){
        return effects;
    }

    public static List getEffectsLeft(String list){
        List<String> effectList = new ArrayList<>(Arrays.asList(list.split(",")));
        List<String> allList = new ArrayList<>();
        for(MobEffect effect: effects){
            allList.add(effect.getDescriptionId());
        }
        allList.removeAll(effectList);
        return allList;
    }

    public static List<String> getDamageKinds(){
        List<String> damageSourceNames  = new ArrayList<>();
        damageSourceNames.add("inFire");
        damageSourceNames.add("lightningBolt");
        damageSourceNames.add("onFire");
        damageSourceNames.add("lava");
        damageSourceNames.add("hotFloor");
        damageSourceNames.add("inWall");
        //damageSourceNames.add("cramming"); ?
        damageSourceNames.add("drown");
        damageSourceNames.add("starve");
        damageSourceNames.add("cactus");
        damageSourceNames.add("fall");
        damageSourceNames.add("flyIntoWall");
        damageSourceNames.add("outOfWorld");
        damageSourceNames.add("generic");
        damageSourceNames.add("magic");
        damageSourceNames.add("wither");
        damageSourceNames.add("anvil");
        damageSourceNames.add("fallingBlock");
        damageSourceNames.add("dragonBreath");
        //damageSourceNames.add("dryout"); !!!
        damageSourceNames.add("sweetBerryBush");
        damageSourceNames.add("freeze");
        damageSourceNames.add("fallingStalactite");
        damageSourceNames.add("stalagmite");
        damageSourceNames.add("sting");
        damageSourceNames.add("mob");
        damageSourceNames.add("player");
        damageSourceNames.add("arrow");
        damageSourceNames.add("trident");
        damageSourceNames.add("fireworks");
        damageSourceNames.add("witherSkull");
        //damageSourceNames.add("thrown"); !
        damageSourceNames.add("indirectMagic");
        damageSourceNames.add("thorns");
        damageSourceNames.add("explosion");
        damageSourceNames.add("sonic_boom");
        return damageSourceNames;
    }

    public static List getDamageKindsLeft(String list){
        List<String> damageList = new ArrayList<>(Arrays.asList(list.split(",")));
        List<String> allList = new ArrayList<>(getDamageKinds());
        allList.removeAll(damageList);
        return allList;
    }

    public static double calculateCatchChance(float playerHealth, float entityMaxHealth, float entityHealth){
        double probability;
        float base = (0.6f+(1-(entityHealth/entityMaxHealth)));
        if(playerHealth >= entityMaxHealth)
            probability = base;
        else probability = (base * (float) Math.pow(0.95f, Math.abs(playerHealth - entityMaxHealth) / 10));
        return probability;
    }

    public static EntityType<?> entityTypeFromNbt(CompoundTag nbtTagCompound) {
        ResourceLocation typeId = new ResourceLocation(nbtTagCompound.getString("id"));
        return ForgeRegistries.ENTITY_TYPES.getValue(typeId);
    }

    public static void teleportRandomly(Entity entity, int radius) {
        Random random = new Random();
        Level world = entity.getCommandSenderWorld();
        double originalX = entity.getX();
        double originalY = entity.getY();
        double originalZ = entity.getZ();

        for (int i = 0; i < 100; i++) {
            double targetX = originalX + (random.nextDouble() - 0.5) * 2.0 * radius;
            double targetY = Math.min(Math.max(originalY + (random.nextInt(2 * radius) - radius), 0), world.getMaxBuildHeight() - 1);
            double targetZ = originalZ + (random.nextDouble() - 0.5) * 2.0 * radius;

            if (isSafeLocation(world, targetX, targetY, targetZ)) {
                entity.teleportTo(targetX, targetY, targetZ);
                break;
            }
        }
    }

    private static boolean isSafeLocation(Level world, double x, double y, double z) {
        BlockPos blockPos = new BlockPos((int) x,(int) y,(int) z);
        return world.isEmptyBlock(blockPos)
                && world.isEmptyBlock(blockPos.above())
                && !world.isEmptyBlock(blockPos.below());
    }

    public static void dealParagonDamage(LivingEntity entity,Player player,float damage, int type, boolean hurt){
        if(isFriendlyFireBetween(entity,player))
            return;
        ItemStack stack = player.getMainHandItem();
        float percentBonus = 1;
        if(type == 1)
            percentBonus += 0;
        else if(type == 2)
            percentBonus += player.getPersistentData().getFloat("VPTrigonBonus");
        else if(type == 3)
            percentBonus += player.getPersistentData().getInt("VPGravity")*20;
        float health = damage*(1+percentBonus/100);
        if(entity.getHealth()-health > 0) {
            if(hurt)
                entity.hurt(player.damageSources().generic(),0);
            entity.setHealth(entity.getHealth() - health);
        } else {
            deadInside(entity,player);
        }
    }

    public static List<TagKey<DamageType>> damageTypes(){
        List<TagKey<DamageType>> damageTypes = new ArrayList<>();
        damageTypes.add(DamageTypeTags.DAMAGES_HELMET);
        damageTypes.add(DamageTypeTags.BYPASSES_ARMOR);
        damageTypes.add(DamageTypeTags.BYPASSES_SHIELD);
        damageTypes.add(DamageTypeTags.BYPASSES_INVULNERABILITY);
        damageTypes.add(DamageTypeTags.BYPASSES_EFFECTS);
        damageTypes.add(DamageTypeTags.BYPASSES_RESISTANCE);
        damageTypes.add(DamageTypeTags.BYPASSES_ENCHANTMENTS);
        damageTypes.add(DamageTypeTags.IS_FIRE);
        damageTypes.add(DamageTypeTags.IS_PROJECTILE);
        damageTypes.add(DamageTypeTags.WITCH_RESISTANT_TO);
        damageTypes.add(DamageTypeTags.IS_EXPLOSION);
        damageTypes.add(DamageTypeTags.IS_FALL);
        damageTypes.add(DamageTypeTags.IS_DROWNING);
        damageTypes.add(DamageTypeTags.IS_FREEZING);
        damageTypes.add(DamageTypeTags.IS_LIGHTNING);
        damageTypes.add(DamageTypeTags.NO_ANGER);
        damageTypes.add(DamageTypeTags.NO_IMPACT);
        damageTypes.add(DamageTypeTags.ALWAYS_MOST_SIGNIFICANT_FALL);
        damageTypes.add(DamageTypeTags.WITHER_IMMUNE_TO);
        damageTypes.add(DamageTypeTags.IGNITES_ARMOR_STANDS);
        damageTypes.add(DamageTypeTags.BURNS_ARMOR_STANDS);
        damageTypes.add(DamageTypeTags.AVOIDS_GUARDIAN_THORNS);
        damageTypes.add(DamageTypeTags.ALWAYS_TRIGGERS_SILVERFISH);
        damageTypes.add(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS);
        return damageTypes;
    }

    public static List<DamageSource> playerDamageSources(LivingEntity player, LivingEntity entity){
        List<DamageSource> damageSources = new ArrayList<>();
        damageSources.add(player.damageSources().lightningBolt());
        damageSources.add(player.damageSources().inWall());
        damageSources.add(player.damageSources().cramming());
        damageSources.add(player.damageSources().drown());
        damageSources.add(player.damageSources().starve());
        damageSources.add(player.damageSources().cactus());
        damageSources.add(player.damageSources().fall());
        damageSources.add(player.damageSources().flyIntoWall());
        damageSources.add(player.damageSources().fellOutOfWorld());
        damageSources.add(player.damageSources().generic());
        damageSources.add(player.damageSources().magic());
        damageSources.add(player.damageSources().wither());
        damageSources.add(player.damageSources().dragonBreath());
        //damageSources.add(player.damageSources().dryOut());
        damageSources.add(player.damageSources().sweetBerryBush());
        damageSources.add(player.damageSources().freeze());
        damageSources.add(player.damageSources().stalagmite());
        damageSources.add(player.damageSources().fallingBlock(player));
        damageSources.add(player.damageSources().anvil(player));
        damageSources.add(player.damageSources().fallingStalactite(player));
        damageSources.add(player.damageSources().sting(player));
        damageSources.add(player.damageSources().mobAttack(player));
        damageSources.add(player.damageSources().trident(player,entity));
        damageSources.add(player.damageSources().mobProjectile(player,entity));
        damageSources.add(player.damageSources().thorns(player));
        damageSources.add(player.damageSources().explosion(player,entity));
        damageSources.add(player.damageSources().sonicBoom(player));
        damageSources.add(player.damageSources().inFire());
        damageSources.add(player.damageSources().onFire());
        damageSources.add(player.damageSources().lava());
        damageSources.add(player.damageSources().hotFloor());
        return damageSources;
    }
}
