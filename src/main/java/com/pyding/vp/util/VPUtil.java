package com.pyding.vp.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import com.pyding.vp.capability.PlayerCapabilityVP;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.entity.*;
import com.pyding.vp.item.*;
import com.pyding.vp.item.accessories.Accessory;
import com.pyding.vp.item.vestiges.Archlinx;
import com.pyding.vp.item.vestiges.Armor;
import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.item.vestiges.Whirlpool;
import com.pyding.vp.mixin.*;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.*;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CoralBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class VPUtil {
    public static long coolDown(Player player){
        if(ConfigHandler.COMMON.leaderboard.get())
            return 12*60*60*1000;
        double reduce = 1;
        if(getSet(player) == 10)
            reduce = 0.9;
        return (long) (ConfigHandler.COMMON.cooldown.get()*60*60*1000*reduce);
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
        Random random = new Random();
        for (char letter : text.toCharArray()) {
            ChatFormatting color;
            if(random.nextDouble() > 0.5){
                color = ChatFormatting.BLACK;
            } else {
                if(random.nextDouble() > 0.5){
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

    public static List<LivingEntity> getEntities(Player player,double radius,boolean self){
        List<LivingEntity> list = new ArrayList<>(player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+radius,player.getY()+radius,player.getZ()+radius,player.getX()-radius,player.getY()-radius,player.getZ()-radius)));
        if(!self)
            list.remove(player);
        return list;
    }

    public static float getAttack(Player player, boolean hasDurability){
        float attack = (float) player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        if(!hasDurability && player.getMainHandItem().getItem() instanceof TieredItem tieredItem){
            attack -= tieredItem.getTier().getAttackDamageBonus();
        }
        float curseMultiplier = getCurseMultiplier(player,4);
        if(curseMultiplier > 0)
            attack *= curseMultiplier;
        return attack;
    }
    public static List<EntityType<?>> entities = new ArrayList<>();

    public static List<EntityType<?>> getEntitiesList(){
        return entities;
    }

    public static List<EntityType<?>> getEntitiesListOfType(MobCategory category){
        return entities.stream().filter(entityType -> entityType.getCategory() == category).collect(Collectors.toList());
    }

    public static void initEntities(){
        HashSet<EntityType<?>> set = new HashSet<>(ForgeRegistries.ENTITY_TYPES.getValues());
        entities.addAll(set);
    }

    public static HashSet<Item> items = new HashSet<>();
    public static HashSet<ResourceKey<Biome>> biomeNames = new HashSet<>();

    public static void initBiomes(Player player,Level level){
        if(player instanceof ServerPlayer serverPlayer) {
            Set<ResourceKey<Biome>> biomes = level.registryAccess().registryOrThrow(Registries.BIOME).registryKeySet();
            biomeNames.addAll(biomes);
            //PacketHandler.sendToClient(new PlayerFlyPacket(1), serverPlayer);
        }
    }
    public static List<ResourceLocation> getBiomes(){
        List<ResourceLocation> list = new ArrayList<>();
        for(ResourceKey<Biome> key: biomeNames){
            list.add(key.location());
        }
        return list;
    }

    public static HashSet<String> getBiomesLeft(String list, Player player){
        HashSet<String> biomeList = new HashSet<>(Arrays.asList(filterString(list).split(",")));
        HashSet<String> allList = new HashSet<>();
        for(ResourceLocation location: getBiomes()){
            allList.add(location.getPath().trim());
        }
        allList.removeAll(biomeList);
        player.getPersistentData().putString("VPBiomesClient", filterAndTranslate(allList.toString(),ChatFormatting.GRAY).getString());
        return allList;
    }
    public static HashSet<String> getBiomesClient(Player player){
        return new HashSet<>(Arrays.asList(player.getPersistentData().getString("VPBiomesClient").split(",")));
    }
    private static final Pattern PATTERN = Pattern.compile("minecraft:(\\w+)");

//Reference{ResourceKey[minecraft:worldgen/biome / minecraft:birch_forest]=net.minecraft.world.level.biome.Biome@e4348c0}
    public static void initItems(){
        for(Item item: ForgeRegistries.ITEMS){
            items.add(item);
        }
    }

    public static HashSet<Item> getItems(){
        return items;
    }

    public static HashSet<String> foodItems = new HashSet<>();
    public static HashSet<String> toolItems = new HashSet<>();
    public static HashSet<String> getEdibleItems(){
        if(foodItems.isEmpty()) {
            for (Item item : items) {
                if (item.isEdible())
                    foodItems.add(item.getDescriptionId());
            }
        }
        return foodItems;
    }

    public static HashSet<String> getFoodLeft(String list){
        HashSet<String> foodList = new HashSet<>(Arrays.asList(filterString(list).split(",")));
        HashSet<String> allList = new HashSet<>(getEdibleItems());
        allList.removeAll(foodList);
        return allList;
    }

    public static HashSet<String> getTools(){
        if(toolItems.isEmpty()) {
            for (Item item : items) {
                if (item instanceof TieredItem)
                    toolItems.add(item.getDescriptionId());
            }
        }
        return toolItems;
    }

    public static HashSet<String> templates = new HashSet<>();

    public static HashSet<String> getTemplates(){
        if(templates.isEmpty()) {
            for (Item item : items) {
                if (item instanceof SmithingTemplateItem templateItem)
                    templates.add(((SmitingMixing) templateItem).upgradeDescription().getString());
            }
        }
        return templates;
    }

    public static HashSet<String> getTemplatesLeft(String list){
        HashSet<String> have = new HashSet<>(Arrays.asList(filterString(list).split(",")));
        HashSet<String> allList = new HashSet<>(getTemplates());
        allList.removeAll(have);
        return allList;
    }

    public static HashSet<String> musicDisks = new HashSet<>();

    public static HashSet<String> getMusicDisks(){
        if(musicDisks.isEmpty()) {
            for (Item item : items) {
                if (item instanceof RecordItem recordItem)
                    musicDisks.add(item.getDescriptionId()+".desc");
            }
        }
        return musicDisks;
    }

    public static HashSet<String> getMusicDisksLeft(String list){
        HashSet<String> have = new HashSet<>(Arrays.asList(filterString(list).split(",")));
        HashSet<String> allList = new HashSet<>(getMusicDisks());
        allList.removeAll(have);
        return allList;
    }


    public static HashSet<String> seaList = new HashSet<>();

    public static HashMap<MobBucketItem,HashSet<String>> bucketMap = new HashMap<>();

    public static HashSet<MobBucketItem> buckets = new HashSet<>();

    public static HashSet<MobBucketItem> getBuckets(){
        if(buckets.isEmpty()) {
            for (Item item : items) {
                if (item instanceof MobBucketItem bucketItem)
                    buckets.add(bucketItem);
            }
        }
        return buckets;
    }

    public static void initBuckets(){
        for(MobBucketItem bucketItem: getBuckets()){
            EntityType<?> type = ((BucketMixin)bucketItem).getFishSup().get();
            if(type.getDescriptionId().contains("entity.minecraft.tropical_fish")) {
                HashSet<String> tropicalFish = new HashSet<>();
                for (TropicalFish.Variant variant : TropicalFish.COMMON_VARIANTS)
                    tropicalFish.add(variant.pattern().getSerializedName());
                bucketMap.put(bucketItem, tropicalFish);
            }
            else if(type.getDescriptionId().contains("entity.minecraft.axolotl")) {
                HashSet<String> axolotl = new HashSet<>();
                for (Axolotl.Variant variant: Axolotl.Variant.values())
                    axolotl.add(variant.getName());
                bucketMap.put(bucketItem, axolotl);
            } else {
                seaList.add(type.getDescriptionId());
            }
        }
    }

    public static HashSet<String> fishTypesFromBucket(MobBucketItem bucketItem){
        if(bucketMap.isEmpty())
            initBuckets();
        if(bucketMap.containsKey(bucketItem))
            return bucketMap.get(bucketItem);
        else return new HashSet<>();
    }

    public static HashSet<String> getSeaList(){
        if(seaList.isEmpty()) {
            for (Block block : blocks) {
                if (block instanceof CoralBlock)
                    seaList.add(block.getDescriptionId());
            }
        }
        if(bucketMap.isEmpty())
            initBuckets();
        return seaList;
    }

    public static int getSeaSize(){
        int size = 0;
        size += getSeaList().size();
        for(MobBucketItem bucketItem: getBuckets()){
            for(String fish: fishTypesFromBucket(bucketItem)){
                size++;
            }
        }
        return size;
    }

    public static HashSet<String> getSeaLeft(String list){
        HashSet<String> have = new HashSet<>(Arrays.asList(filterString(list).split(",")));
        HashSet<String> allList = new HashSet<>(getSeaList());
        allList.removeAll(have);
        return allList;
    }

    public static List<EntityType<?>> monsterList = new ArrayList<>();
    public static List<EntityType<?>> bossList = new ArrayList<>();

    public static void initMonstersAndBosses(Player player){
        if(!monsterList.isEmpty() || !bossList.isEmpty())
            return;
        List<LivingEntity> list = new ArrayList<>();
        for(EntityType<?> type: getEntitiesListOfType(MobCategory.MONSTER)){
            Entity entity = type.create(player.getCommandSenderWorld());
            if(entity instanceof HunterKiller)
                continue;
            if (entity instanceof LivingEntity livingEntity) {
                float health = livingEntity.getMaxHealth();
                if ((health > 190 || isCustomBoss(type)) && !isBlacklistBoss(type,player)){
                    bossList.add(type);
                    zaebali.put(type,health);
                } else monsterList.add(type);
                list.add(livingEntity);
            }
        }
        initRareDrops(list,player.getServer());
    }

    public static boolean isNpc(EntityType<?> type){
        if(type.toString().contains("easy_npc"))
            return true;
        return false;
    }

    public static boolean isBlacklistBoss(EntityType<?> type,Player player){
        if(LeaderboardUtil.isLeaderboardsActive(player)){
            int count = 0;
            for (String types : ConfigHandler.COMMON.blacklistBosses.get().toString().split(",")) {
                count++;
                if(count > 3)
                    break;
                if (type.toString().contains(types))
                    return true;
            }
        } else {
            for (String types : ConfigHandler.COMMON.blacklistBosses.get().toString().split(","))
                if (type.toString().contains(types))
                    return true;
        }
        return false;
    }

    public static boolean isCustomBoss(EntityType<?> type){
         for(String types: ConfigHandler.COMMON.bosses.get().toString().split(","))
             if(type.toString().contains(types))
                 return true;
        return false;
    }
    public static List<String> getMonsterLeft(String list, Player player){
        List<String> mobsList = new ArrayList<>(Arrays.asList(filterString(list).split(",")));
        List<String> allList = new ArrayList<>();
        for(EntityType<?> type: monsterList){
            allList.add(type.toString().trim());
        }
        allList.removeAll(mobsList);
        player.getPersistentData().putString("VPMonsterClient", filterAndTranslate(allList.toString(),ChatFormatting.GRAY).getString());
        return allList;
    }
    public static List<String> getMonsterClient(Player player){
        return new ArrayList<>(Arrays.asList(player.getPersistentData().getString("VPMonsterClient").split(",")));
    }

    public static List<String> getBossesLeft(String list, Player player){
        List<String> mobsList = new ArrayList<>(Arrays.asList(filterString(list).split(",")));
        List<String> allList = new ArrayList<>();
        for(EntityType<?> type: bossList){
            allList.add(type.toString());
        }
        allList.removeAll(mobsList);
        player.getPersistentData().putString("VPBossClient", filterAndTranslate(allList.toString(),ChatFormatting.GRAY).getString());
        return allList;
    }

    public static List<String> getBossClient(Player player){
        return new ArrayList<>(Arrays.asList(player.getPersistentData().getString("VPBossClient").split(",")));
    }

    public static List<String> getMobsLeft(String list, Player player){
        List<String> mobsList = new ArrayList<>(Arrays.asList(filterString(list).split(",")));
        List<String> allList = new ArrayList<>();
        for(EntityType<?> type: getEntitiesListOfType(MobCategory.CREATURE)){
            allList.add(type.toString());
        }
        allList.removeAll(mobsList);
        player.getPersistentData().putString("VPMobsClient", filterAndTranslate(allList.toString(),ChatFormatting.GRAY).getString());
        return allList;
    }

    public static List<String> getMobsClient(Player player){
        return new ArrayList<>(Arrays.asList(player.getPersistentData().getString("VPMobsClient").split(",")));
    }

    public static HashSet<Block> blocks = new HashSet<>();
    public static HashSet<String> flowers = new HashSet<>();

    public static void initBlocks(){
        for(Block block: ForgeRegistries.BLOCKS){
            blocks.add(block);
        }
    }

    public static void initFlowers(){
        for(Block block: blocks){
            if(block instanceof FlowerBlock)
                flowers.add(block.getDescriptionId());
        }
    }

    public static HashSet<String> getFlowers(){
        return flowers;
    }

    public static List<String> getFlowersLeft(String list){
        List<String> flowerList = new ArrayList<>(Arrays.asList(filterString(list).split(",")));
        List<String> allList = new ArrayList<>();
        for(String name: getFlowers()){
            allList.add(name);
        }
        allList.removeAll(flowerList);
        return allList;
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

    public static float damagePercentBonus(Player player,int type){
        float percentBonus = 1;
        Random random = new Random();
        if(type == 1)
            percentBonus += 0;
        else if(type == 2) {
            if(VPUtil.getSet(player) == 2)
                percentBonus += 400;
            if(player.getPersistentData().getLong("VPAcsSpecial") >= System.currentTimeMillis() && VPUtil.getSet(player) == 5 && random.nextDouble() < getChance(0.4,player))
                percentBonus += 600;
            percentBonus += player.getPersistentData().getFloat("VPTrigonBonus");
        }
        else if(type == 3) {
            if(VPUtil.getSet(player) == 4)
                percentBonus += 200;
            if(player.getPersistentData().getLong("VPAcsSpecial") >= System.currentTimeMillis() && VPUtil.getSet(player) == 5 && random.nextDouble() < getChance(0.4,player))
                percentBonus += 600;
            percentBonus += player.getPersistentData().getInt("VPGravity") * 20;
        }
        if(hasLyra(player,4))
            percentBonus += 50;
        return percentBonus;
    }

    public static void dealDamage(LivingEntity entity,Player player, DamageSource source, float percent, int type){
        if(isFriendlyFireBetween(entity,player) || isProtectedFromHit(player,entity))
            return;
        entity.invulnerableTime = 0;
        ItemStack stack = player.getMainHandItem();
        boolean hasDurability = stack.isDamageableItem() && stack.getDamageValue()+1 < stack.getMaxDamage();
        if(hasDurability) {
                    stack.hurtAndBreak(1, player, consumer -> {
                consumer.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        DamageSource damageSource = new DamageSource(source.typeHolder(),player);
        entity.hurt(damageSource,getAttack(player,hasDurability)*((percent+damagePercentBonus(player,type))/100));
    }

    public static void dealDamage(LivingEntity entity,Player player, DamageSource source, float damage, int type, boolean invulPierce){
        if(isFriendlyFireBetween(entity,player) || isProtectedFromHit(player,entity))
            return;
        if(invulPierce)
            entity.invulnerableTime = 0;
        ItemStack stack = player.getMainHandItem();
        boolean hasDurability = stack.isDamageableItem() && stack.getDamageValue()+1 < stack.getMaxDamage();
        if(hasDurability) {
            stack.hurtAndBreak(1, entity, consumer -> {
                consumer.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        float curseMultiplier = getCurseMultiplier(player,4);
        if(curseMultiplier > 0)
            damage *= curseMultiplier;
        entity.hurt(source,damage*(1 + damagePercentBonus(player,type)/100));
    }

    public static void spawnLightning(ServerLevel world, double x, double y, double z) {
        LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(world);
        if (lightningBolt != null) {
            lightningBolt.moveTo(x, y, z);
            world.addFreshEntity(lightningBolt);
        }
    }

    public static boolean isEvent(Entity entity){
        if(entity instanceof Player player && player.isCreative())
            return false;
        else return ConfigHandler.COMMON.eventMode.get();
    }

    public static void setHealth(LivingEntity entity,float amount){
        ((EntityVzlom)entity).getEntityData().set(((LivingEntityVzlom)entity).getDataHealth(),amount);
    }

    public static void despawn(LivingEntity livingEntity){
        livingEntity.invalidateCaps();
        VPUtil.spawnSphere(livingEntity,ParticleTypes.ASH,50,2,0.01f);
        VPUtil.spawnSphere(livingEntity,ParticleTypes.WHITE_ASH,50,2,0.01f);
        VPUtil.play(livingEntity,SoundRegistry.DESPAWN.get());
        if(livingEntity instanceof Player){
            setDead(livingEntity,livingEntity.damageSources().dryOut());
        } else {
            setHealth(livingEntity,0);
            livingEntity.getBrain().clearMemories();
            ((EntityVzlom) livingEntity).setPersistentData(null);
            ((EntityVzlom) livingEntity).getLevelCallback().onRemove(Entity.RemovalReason.DISCARDED);
        }
    }

    public static void setDead(LivingEntity corpse, DamageSource source){
        if (!corpse.isRemoved() && !((LivingEntityVzlom)corpse).isDead()) {
            Entity entity = source.getEntity();
            if (corpse.isSleeping()) {
                corpse.stopSleeping();
            }
            if (!corpse.level().isClientSide && corpse instanceof Player player) {
                player.sendSystemMessage(Component.literal("§cSome fool tried to cancel legal death from Paragon Damage, you died forcefully!"));
            }
            setHealth(corpse,0);
            ((LivingEntityVzlom)corpse).setDead(true);
            corpse.getCombatTracker().recheckStatus();
            Level level = corpse.level();
            if (level instanceof ServerLevel) {
                ServerLevel serverlevel = (ServerLevel)level;
                if (entity == null || entity.killedEntity(serverlevel, corpse)) {
                    corpse.gameEvent(GameEvent.ENTITY_DIE);
                    ((LivingEntityVzlom)corpse).invokeDropAllDeathLoot(source);
                }

                corpse.level().broadcastEntityEvent(corpse, (byte)3);
            }
            corpse.setPose(Pose.DYING);
        }
    }

    public static void syncEntity(Entity entity){
        CompoundTag sendNudes = new CompoundTag();
        for (String key : entity.getPersistentData().getAllKeys()) {
            if (key.startsWith("VP") && entity.getPersistentData().get(key) != null) {
                sendNudes.put(key, entity.getPersistentData().get(key));
            }
        }
        if(!entity.getCommandSenderWorld().isClientSide)
            PacketHandler.sendToClients(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SendEntityNbtToClient(sendNudes,entity.getId()));
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
                for(TagKey<DamageType> type : damageTypes(false)){
                    if(event.getSource().is(type))
                        player.sendSystemMessage(Component.translatable(type.location().toLanguageKey()));
                }
                float shield = getShield(event.getEntity());
                float overshield = getOverShield(event.getEntity());
                if(shield > 0)
                    player.sendSystemMessage(Component.literal("§cHas Shields: " + shield));
                if(overshield > 0)
                    player.sendSystemMessage(Component.literal("§dHas Over Shields: " + overshield));
                if(event.getEntity().getPersistentData().getInt("VPPrismDamage") > 0)
                    player.sendSystemMessage(Component.literal("§5Has Prism with weak point: " + playerDamageSources(player,player).get(event.getEntity().getPersistentData().getInt("VPPrismDamage")-1)));
                player.sendSystemMessage(Component.literal("\n"));
            }
        });
    }

    public static EntityType getRandomEntity(){
        Random random = new Random();
        List<EntityType> allEntities = new ArrayList<>();
        allEntities.addAll(getEntitiesListOfType(MobCategory.CREATURE));
        allEntities.addAll(getEntitiesListOfType(MobCategory.MONSTER));
        return allEntities.get(random.nextInt(allEntities.size()));
    }

    public static EntityType getRandomMonster(){
        Random random = new Random();
        List<EntityType> allEntities = new ArrayList<>();
        allEntities.addAll(getEntitiesListOfType(MobCategory.MONSTER));
        return allEntities.get(random.nextInt(allEntities.size()));
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

    public static List<ItemStack> getCurioList(Player player){
        List<SlotResult> result = new ArrayList<>();
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            result.addAll(handler.findCurios(itemStack -> itemStack.getItem() instanceof ICurioItem));
        });
        List<ItemStack> stacks = new ArrayList<>();
        for(SlotResult hitResult: result){
            stacks.add(hitResult.stack());
        }
        return stacks;
    }

    public static List<ItemStack> getAccessoryList(Player player){
        List<SlotResult> result = new ArrayList<>();
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            result.addAll(handler.findCurios(itemStack -> itemStack.getItem() instanceof Accessory));
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
            if(stack.stack().getItem() instanceof Vestige vestige && vestige.isStellar(stack.stack()))
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

    public static float equipmentDurability(float percentage,LivingEntity entity,Player dealer,boolean stellar){
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
        return totalDamage;
    }

    public static double commonPower = 3;

    public static void liftEntity(LivingEntity entity,double power) {
        Random random = new Random();
        Vec3 motion = new Vec3(0, power, 0);
        if(entity instanceof ServerPlayer player){
            PacketHandler.sendToClient(new PlayerFlyPacket(1),player);
        } else entity.lerpMotion(motion.x, motion.y, motion.z);
        entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 7 * 20));
        if(random.nextDouble() < 0.5)
            play(entity,SoundRegistry.WIND1.get());
        else play(entity,SoundRegistry.WIND2.get());
    }

    public static void suckEntity(Entity entity, Player player,int scale, boolean items) {
        if(entity instanceof ItemEntity){
            if(!items)
                return;
            Vec3 playerPos = player.position();
            Vec3 entityPos = entity.position();
            Vec3 direction = playerPos.subtract(entityPos).normalize();
            Vec3 motion = direction.scale(scale);
            entity.lerpMotion(motion.x, motion.y, motion.z);
        }
        else  {
            if (entity == player)
                return;
            if(isProtectedFromHit(player,entity))
                return;
            Vec3 playerPos = player.position();
            Vec3 entityPos = entity.position();
            Vec3 direction = playerPos.subtract(entityPos).normalize();
            Vec3 motion = direction.scale(scale);
            entity.lerpMotion(motion.x, motion.y, motion.z);
            if (entity instanceof ServerPlayer player2) {
                PacketHandler.sendToClient(new PlayerFlyPacket(1), player2);
            }
            if(entity instanceof LivingEntity livingEntity)
                livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 3 * 20));
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

    public static List<LivingEntity> getEntitiesAround(Entity player,double x, double y, double z,boolean self){
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

    public static void adaptiveDamageHurt(LivingEntity entity, Player player,float percent){
        boolean adopted = entity.getPersistentData().getBoolean("VPCrownDR");
        int damage = entity.getPersistentData().getInt("VPCrownDamage");
        if(damage >= playerDamageSources(player,player).size())
            damage = 0;
        if(adopted){
            damage++;
            if(damage >= playerDamageSources(player,player).size())
                damage = 0;
            entity.getPersistentData().putInt("VPCrownDamage", damage);
        }
        DamageSource source = new DamageSource(playerDamageSources(player,entity).get(damage).typeHolder(),player);
        dealDamage(entity,player,source,percent,2);
        //this fucking stinky dinky spaggety code wrote by an absolute disgusting imbecile(me lol) didn't work even after 20 tries to fix it
    }

    public static DamageSource randomizeDamageType(Player player){
        List<DamageSource> sources = playerDamageSources(player,player);
        int numba = new Random().nextInt(sources.size());
        return sources.get(numba);
    }
    
    public static float getShieldBonus(LivingEntity entity){
        float curseMultiplier = 0;
        if(entity instanceof Player player) {
            curseMultiplier = getCurseMultiplier(player,2);
        }
        float shieldBonus = (entity.getPersistentData().getFloat("VPShieldBonusDonut")
                +entity.getPersistentData().getFloat("VPShieldBonusFlower")
                +entity.getPersistentData().getFloat("VPAcsShields")
                +entity.getPersistentData().getFloat("VPRuneBonus")
                -entity.getPersistentData().getFloat("VPIgnis")
                -curseMultiplier);
        if(hasLyra(entity,6))
            shieldBonus += 70;
        return shieldBonus;
    }

    public static void addShield(LivingEntity entity,float amount,boolean add){
        if(entity.getPersistentData().getInt("VPSoulRotting") >= 10)
            return;
        CompoundTag tag = entity.getPersistentData();
        float shieldBonus = getShieldBonus(entity);
        float shield;
        if(!add) {
            if(amount*(1 + shieldBonus/100) > tag.getFloat("VPShield"))
                shield = amount*(1 + shieldBonus/100);
            else return;
        }
        else shield = tag.getFloat("VPShield") + amount*(1 + shieldBonus/100);
        if(entity instanceof Player player){
            float curseShield = getCurseMultiplier(player,5);
            if(curseShield != 0){
                for(LivingEntity livingEntity: getEntities(player,30,false)){
                    if(!isFriendlyFireBetween(player,livingEntity) && !isProtectedFromHit(player,livingEntity)){
                        float entityShield = getShield(livingEntity);
                        float entityOverShield = getOverShield(livingEntity);
                        if(entityShield > shield*curseShield){
                            livingEntity.getPersistentData().putFloat("VPShield", entityShield-shield*curseShield);
                        } else livingEntity.getPersistentData().putFloat("VPShield", 0);
                        if(entityOverShield > shield*curseShield){
                            livingEntity.getPersistentData().putFloat("VPOverShield", entityOverShield-shield*curseShield);
                        } else livingEntity.getPersistentData().putFloat("VPOverShield", 0);
                        VPUtil.syncEntity(livingEntity);
                    }
                }
                sync(player);
                return;
            }
        }
        Random random = new Random();
        if(tag.getLong("VPAntiShield") < System.currentTimeMillis()) {
            if(entity instanceof Player player && hasVestige(ModItems.SOULBLIGHTER.get(), player)){
                if(random.nextDouble() < getChance(0.2,player))
                    addOverShield(player,shield*0.05f,false);
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
        return entity.getPersistentData().getFloat("VPShield");
    }

    public static float getOverShield(LivingEntity entity){
        return entity.getPersistentData().getFloat("VPOverShield");
    }

    public static void deadInside(LivingEntity entity,Player player){
        Random random = new Random();
        if(entity instanceof Player || isBoss(entity) || isEmpoweredMob(entity)) {
            if (random.nextDouble() < 0.5)
                play(entity, SoundRegistry.DEATH1.get());
            else play(entity, SoundRegistry.DEATH2.get());
        }
        entity.invulnerableTime = 0;
        entity.hurt(player.damageSources().playerAttack(player),0);
        entity.setLastHurtByPlayer(player);
        antiResurrect(entity,10000);
        entity.getPersistentData().putLong("VPMirnoeReshenie", System.currentTimeMillis()+10000);
        antiTp(entity,10000);
        setHealth(entity,0);
        entity.die(player.damageSources().genericKill());
    }

    public static void deadInside(LivingEntity entity){
        if(entity.isRemoved())
            return;
        Random random = new Random();
        if(entity instanceof Player || isBoss(entity) || isEmpoweredMob(entity)) {
            if (random.nextDouble() < 0.5)
                play(entity, SoundRegistry.DEATH1.get());
            else play(entity, SoundRegistry.DEATH2.get());
        }
        entity.invulnerableTime = 0;
        entity.hurt(entity.damageSources().genericKill(),0);
        antiResurrect(entity,10000);
        entity.getPersistentData().putLong("VPMirnoeReshenie", System.currentTimeMillis()+10000);
        antiTp(entity,10000);
        entity.setHealth(0);
        entity.die(entity.damageSources().genericKill());
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

    public static List<LivingEntity> rayClass(Class clas,Player player, float range, int maxDist, boolean stopWhenFound) {
        Vector3 target = Vector3.fromEntityCenter(player);
        List entities = new ArrayList<>();

        for (int distance = 1; distance < maxDist; ++distance) {
            target = target.add(new Vector3(player.getLookAngle()).multiply(distance)).add(0.0, 0.5, 0.0);
            List list = player.getCommandSenderWorld().getEntitiesOfClass(clas, new AABB(target.x - range, target.y - range, target.z - range, target.x + range, target.y + range, target.z + range));
            list.removeIf(entity -> entity == player || !player.hasLineOfSight((Entity) entity));
            for(Object entity: list){
                if(!entities.contains(entity))
                    entities.add(entity);
            }

            if (stopWhenFound && entities.size() > 0) {
                break;
            }
        }

        return entities;
    }

    public static void fall(LivingEntity entity,double power) {
        Vec3 motion = new Vec3(0, power, 0);
        entity.lerpMotion(motion.x, motion.y, motion.z);
        if(entity instanceof ServerPlayer player){
            PacketHandler.sendToClient(new PlayerFlyPacket(-1),player);
        }
    }

    public static float getHealBonus(LivingEntity entity){
        CompoundTag tag = entity.getPersistentData();
        float poison = 0;
        if(entity instanceof Player player) {
            if(isPoisonedByNightmare(player))
                poison = 150;
            poison += getCurseMultiplier(player,2);
        }
        float healBonus = Math.max(-300,tag.getFloat("VPHealResMask")
                +tag.getFloat("VPHealResFlower")
                +tag.getFloat("VPHealBonusDonut")
                +tag.getFloat("VPHealBonusDonutPassive")
                +tag.getFloat("VPAcsHeal")
                -entity.getPersistentData().getFloat("VPIgnis")
                -poison);
        if(entity.getPersistentData().getLong("VPLyra3") > System.currentTimeMillis())
            healBonus += 90;
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
        for(EntityType<?> type: bossList){
            if(entity.getType() == type)
                return true;
        }
        return isCustomBoss(entity.getType());
    }

    public static boolean isNightmareBoss(LivingEntity entity){
        return entity.getPersistentData().getBoolean("VPNightmareBoss");
    }

    public static boolean isNightmareBoss(Entity entity){
        if(entity instanceof LivingEntity livingEntity)
            return isBoss(livingEntity) && entity.getPersistentData().getBoolean("VPNightmareBoss");
        else return false;
    }

    public static void dropEntityLoot(LivingEntity entity, Player player, boolean drop) {
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
            List<ItemStack> list = lootTable.getRandomItems(params);
            for(ItemStack stack: list){
                ItemEntity itemEntity = new ItemEntity(serverLevel,entity.getX(),entity.getY(),entity.getZ(),stack);
                if(drop)
                    serverLevel.addFreshEntity(itemEntity);
                else giveStack(stack,player);
            }
        }
    }

    public static void initWorlds(){

    }

    public static ResourceKey<Level> getWorldKey(String path,String directory){
        ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(directory,path));
        return key;
    }

    public static HashMap<String,List<Item>> rareDrops = new HashMap<>();

    public static HashMap<String,List<Item>> getRareDrops(){
        return rareDrops;
    }

    public static List<Item> getRareDrops(LivingEntity livingEntity){
        return rareDrops.get(livingEntity.getType().getDescriptionId());
    }

    public static HashSet<Item> hashRares = new HashSet<>();

    public static void initRareDrops(List<LivingEntity> list, MinecraftServer server){
        for(LivingEntity livingEntity: list){
            LootTable lootTable = server.getLootData().getLootTable(livingEntity.getLootTable());
            List<Item> rareList = new ArrayList<>();
            for(LootPool pool: ((LootTableVzlom)lootTable).getPools()){
                for (LootPoolEntryContainer entry : ((LootPoolMixin) pool).getEntries()) {
                    if (entry instanceof LootItem lootItemEntry) {
                        Item item = ((LootItemMixin) lootItemEntry).getItem();
                        for (LootItemCondition condition : ((LootPoolMixin) pool).getConditions()) {
                            if (condition instanceof LootItemRandomChanceCondition itemCondition) {
                                float chance = ((LootRandomItemMixin) itemCondition).getChance();
                                if(chance <= ConfigHandler.COMMON.rareItemChance.get()+0.001){ //some wierd shit is going on, so I need a little boost
                                    rareList.add(item);
                                }
                            } else if (condition instanceof LootItemRandomChanceWithLootingCondition lootingCondition) {
                                float chance = ((LootItemEnchantMixin) lootingCondition).getChance();
                                if(chance <= ConfigHandler.COMMON.rareItemChance.get()+0.001){
                                    rareList.add(item);
                                }
                            }
                        }
                    }
                }
            }
            rareDrops.put(livingEntity.getType().getDescriptionId(),rareList);
            hashRares.addAll(rareList);
        }
        hashRares.add(ModItems.CORRUPT_FRAGMENT.get());
        hashRares.add(ModItems.CORRUPT_ITEM.get());
        hashRares.add(ModItems.CHAOS_ORB.get());
    }

    public static MobEffect getRandomEffect(boolean isBenefit){
        List<String> blacklist = new ArrayList<>(Arrays.asList(ConfigHandler.COMMON.debuffBlacklist.get().toString().split(",")));
        MobEffect effect;
        Random random = new Random();
        List<MobEffect> effects = new ArrayList<>();
        for(MobEffect element: ForgeRegistries.MOB_EFFECTS){
            if(element.isBeneficial() == isBenefit && !blacklist.contains(element.getDescriptionId())) {
                effects.add(element);
            }
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
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
            if(!enchantments.isEmpty()){
                for(Enchantment enchantment: enchantments.keySet()){
                    if(stack.getEnchantmentLevel(enchantment) < 0 || enchantment.isCurse())
                        continue;
                    int lvl = enchantments.get(enchantment);
                    enchantments.remove(enchantment);
                    enchantments.put(enchantment,lvl*-1);
                }
                stack.getOrCreateTag().putBoolean("VPEnchant",true);
            }
        }
        entity.getPersistentData().putLong("VPEnchant",System.currentTimeMillis()+15000);
    }

    public static void negativnoDisenchant(LivingEntity entity){
        for(ItemStack stack: getAllEquipment(entity)){
            if(!stack.getOrCreateTag().getBoolean("VPEnchant"))
                continue;
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
            if(!enchantments.isEmpty()){
                for(Enchantment enchantment: enchantments.keySet()){
                    if(stack.getEnchantmentLevel(enchantment) < 0 || enchantment.isCurse())
                        continue;
                    int lvl = enchantments.get(enchantment);
                    enchantments.remove(enchantment);
                    enchantments.put(enchantment,lvl*-1);
                }
                stack.getOrCreateTag().putBoolean("VPEnchant",false);
            }
        }
    }

    public static void enchantCurseAll(LivingEntity entity){
        for(ItemStack stack: getAllEquipment(entity)){
            stack.getEnchantmentTags().clear();
            List<Enchantment> list = new ArrayList<>(ForgeRegistries.ENCHANTMENTS.getValues());
            for(Enchantment enchantment: list){
                if(enchantment.isCurse())
                    stack.enchant(enchantment,1);
            }
            /*stack.enchant(Enchantments.BINDING_CURSE,1);
            stack.enchant(Enchantments.VANISHING_CURSE,1);*/
        }
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
        if(event.getSource().getMsgId().equals(weak.getMsgId())){
            event.setAmount(event.getAmount()*2);
        }
        else {
            event.setAmount(0);
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
        return !source.is(DamageTypes.FELL_OUT_OF_WORLD) && !source.is(DamageTypes.DROWN) && !source.is(DamageTypes.LAVA) && !source.is(DamageTypes.LIGHTNING_BOLT) && !source.is(DamageTypes.FREEZE) && !source.is(DamageTypes.IN_FIRE) && !source.is(DamageTypes.MAGIC) && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS) && !source.is(DamageTypeTags.BYPASSES_EFFECTS);
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

    public static boolean isProtectedFromHit(Player attacker, Entity target) {
        if (attacker == null || target == null) {
            return false;
        }
        if(attacker == target)
            return false;
        if(target instanceof Player player && player.isCreative()) {
            return true;
        }
        if(isNpc(target.getType()))
            return true;
        if(attacker.getPersistentData().hasUUID("VPSlave") && attacker.getPersistentData().getUUID("VPSlave").compareTo(target.getUUID()) == 0)
            return true;
        if(target.getPersistentData().hasUUID("VPPlayer") && target.getPersistentData().getUUID("VPPlayer").compareTo(attacker.getUUID()) == 0)
            return true;
        final boolean[] friend = {false};
        attacker.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            for(String name: cap.getFriends().split(",")){
                if(!name.isEmpty() && (target.getDisplayName().equals(name)
                        || target.getType().getDescriptionId().equals(name)
                        || target.getType().getDescriptionId().contains(name)
                        || (target.hasCustomName() && target.getCustomName().getString().contains(name)))
                        || (target instanceof Player targetPlayer && targetPlayer.getName().getString().equals(name)))
                    friend[0] = true;
            }
        });
        return friend[0];
    }

    public static void spawnParticles(Entity player, ParticleOptions particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            if(random.nextDouble() < 0.5)
                x += random.nextDouble();
            else x -= random.nextDouble();
            if(random.nextDouble() < 0.5)
                y += random.nextDouble();
            else y -= random.nextDouble();
            if(random.nextDouble() < 0.5)
                z += random.nextDouble();
            else z -= random.nextDouble();
            for (LivingEntity entity: getEntitiesAround(player,20,20,20,true)){
                if(entity instanceof ServerPlayer serverPlayer){
                    PacketHandler.sendToClient(new ParticlePacket(VPUtilParticles.getParticleId(particle),x,y,z,deltaX,deltaY,deltaZ),serverPlayer);
                }
            }
        }
    }

    public static void spawnParticle(Entity entity, ParticleOptions particle, double x, double y, double z, double deltaX, double deltaY, double deltaZ) {
        for (LivingEntity livingEntity: getEntitiesAround(entity,30,30,30,true)){
            if(livingEntity instanceof ServerPlayer serverPlayer){
                PacketHandler.sendToClient(new ParticlePacket(VPUtilParticles.getParticleId(particle),x,y,z,deltaX,deltaY,deltaZ),serverPlayer);
            }
        }
    }

    public static void spawnParticles(Player player, ParticleOptions particle,double radius, int count, double deltaX, double deltaY, double deltaZ, double speed, boolean force) {
        double startX = player.getX() - radius;
        double startY = player.getY() - radius;
        double startZ = player.getZ() - radius;
        double endX = player.getX() + radius;
        double endY = player.getY() + radius;
        double endZ = player.getZ() + radius;
        Random random = new Random();
        if(count == 1)
            count = random.nextInt(10)+20;
        for (int i = 0; i < count; i++) {
            double x = startX + (endX - startX) * random.nextDouble();
            double y = startY + (endY - startY) * random.nextDouble();
            double z = startZ + (endZ - startZ) * random.nextDouble();
            for (LivingEntity entity: getEntitiesAround(player,20,20,20,true)){
                if(entity instanceof ServerPlayer serverPlayer){
                    PacketHandler.sendToClient(new ParticlePacket(VPUtilParticles.getParticleId(particle),x,y,z,deltaX,deltaY,deltaZ),serverPlayer);
                }
            }
        }
    }

    public static void spawnParticles(Player player, ParticleOptions particle,double radius, int count, double deltaX, double deltaY, double deltaZ, double color1, double color2,double color3) {
        double startX = player.getX() - radius;
        double startY = player.getY() - radius;
        double startZ = player.getZ() - radius;
        double endX = player.getX() + radius;
        double endY = player.getY() + radius;
        double endZ = player.getZ() + radius;
        Random random = new Random();
        if(count == 1)
            count = random.nextInt(10)+20;
        for (int i = 0; i < count; i++) {
            double x = startX + (endX - startX) * random.nextDouble();
            double y = startY + (endY - startY) * random.nextDouble();
            double z = startZ + (endZ - startZ) * random.nextDouble();
            for (LivingEntity entity: getEntitiesAround(player,20,20,20,true)){
                if(entity instanceof ServerPlayer serverPlayer){
                    PacketHandler.sendToClient(new ParticlePacket(VPUtilParticles.getParticleId(particle),x,y,z,deltaX,deltaY,deltaZ,color1,color2,color3),serverPlayer);
                }
            }
        }
    }

    public static List<LivingEntity> getCreaturesAndPlayersAround(Player player, double x, double y, double z){
        List<LivingEntity> list = new ArrayList<>();
        for(LivingEntity entity: player.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z))){
            if(entity instanceof Animal || entity instanceof Player)
                list.add(entity);
        }
        return list;
    }
    public static void rayParticles(Player player, ParticleOptions particle,double distance,double radius, int count, double deltaX, double deltaY, double deltaZ, double speed, boolean force) {
        Random random = new Random();
        BlockPos pos = rayCords(player,player.getCommandSenderWorld(),distance);
        double startX = pos.getX() - radius;
        double startY = pos.getY() - radius;
        double startZ = pos.getZ() - radius;
        double endX = pos.getX() + radius;
        double endY = pos.getY() + radius;
        double endZ = pos.getZ() + radius;

        for (int i = 0; i < count; i++) {
            double x = startX + (endX - startX) * random.nextDouble();
            double y = startY + (endY - startY) * random.nextDouble();
            double z = startZ + (endZ - startZ) * random.nextDouble();
            for (LivingEntity entity: getEntitiesAround(player,20,20,20,true)){
                if(entity instanceof ServerPlayer serverPlayer){
                    PacketHandler.sendToClient(new ParticlePacket(VPUtilParticles.getParticleId(particle),x,y,z,deltaX,deltaY,deltaZ),serverPlayer);
                }
            }
        }
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

    public static void play(LivingEntity main, SoundEvent sound,double x,double y,double z, float volume){
        for(LivingEntity entity: getEntitiesAround(main,20,20,20,true)) {
            if (entity instanceof ServerPlayer player) {
                PacketHandler.sendToClient(new SoundPacket(sound.getLocation(), volume, 1,x,y,z),player);
            }
        }
    }

    public static void play(LivingEntity main, SoundEvent sound){
        for(LivingEntity entity: getEntitiesAround(main,20,20,20,true)) {
            if (entity instanceof ServerPlayer player) {
                PacketHandler.sendToClient(new SoundPacket(sound.getLocation(), 1, 1,0,0,0),player);
            }
        }
    }

    public static void addOverShield(LivingEntity entity,float amount, boolean applyBonus){
        if(entity.getPersistentData().getInt("VPSoulRotting") >= 10)
            return;
        CompoundTag tag = entity.getPersistentData();
        if(tag.getFloat("VPOverShield") <= 0)
            play(entity,SoundRegistry.OVERSHIELD.get());
        float shieldBonus = getShieldBonus(entity);
        if(!applyBonus)
            shieldBonus = 0;
        float shield = (tag.getFloat("VPOverShield") + amount*(1 + shieldBonus/100));
        if(tag.getLong("VPAntiShield") < System.currentTimeMillis()) {
            if(entity instanceof Player player){
                float curseShield = getCurseMultiplier(player,5);
                if(curseShield != 0){
                    for(LivingEntity livingEntity: getEntities(player,30,false)){
                        if(!isFriendlyFireBetween(player,livingEntity) && !isProtectedFromHit(player,livingEntity)){
                            float entityShield = getShield(livingEntity);
                            float entityOverShield = getOverShield(livingEntity);
                            if(entityShield > shield*curseShield){
                                tag.putFloat("VPShield", entityShield-shield*curseShield);
                            } else tag.putFloat("VPShield", 0);
                            if(entityOverShield > shield*curseShield){
                                tag.putFloat("VPOverShield", entityOverShield-shield*curseShield);
                            } else tag.putFloat("VPOverShield", 0);
                        }
                    }
                    sync(player);
                    return;
                }
            }
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
        if(tag.getFloat("VPOverShieldMax") < shield || tag.getFloat("VPOverShieldMax") <= 0) {
            tag.putFloat("VPOverShieldMax", shield);
        }
    }

    public static void regenOverShield(LivingEntity entity,float amount){
        if(entity.getPersistentData().getInt("VPSoulRotting") >= 10 || (entity instanceof Player player && hasCurse(player,5)))
            return;
        CompoundTag tag = entity.getPersistentData();
        float shieldBonus = (entity.getPersistentData().getFloat("VPShieldBonusDonut")
                +entity.getPersistentData().getFloat("VPShieldBonusFlower"));
        if(tag.getFloat("VPOverShield") <= 0)
            return;
        float shield = (tag.getFloat("VPOverShield") + amount*(1 + shieldBonus/100));
        if(tag.getLong("VPAntiShield") < System.currentTimeMillis()) {
            if (tag.getFloat("VPOverShieldMax") <= 0){
                tag.putFloat("VPOverShield", shield);
                tag.putFloat("VPOverShieldMax", shield);
            }
            else tag.putFloat("VPOverShield", Math.min(tag.getFloat("VPOverShieldMax"), shield));
        }
        if(entity instanceof ServerPlayer player) {
            PacketHandler.sendToClient(new SendPlayerNbtToClient(player.getUUID(), player.getPersistentData()),player);
        } else {
            if(!entity.getCommandSenderWorld().isClientSide)
                PacketHandler.sendToClients(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SendEntityNbtToClient(entity.getPersistentData(),entity.getId()));
        }
    }

    public static void stealShields(LivingEntity entity,LivingEntity stealer,float percent, boolean applyBonus){
        if(stealer.getPersistentData().getInt("VPSoulRotting") >= 10 || (entity instanceof Player player && hasCurse(player,5)))
            return;
        CompoundTag tag = entity.getPersistentData();
        float shieldBonus = getShieldBonus(stealer);
        if(!applyBonus)
            shieldBonus = 0;
        float stolenShield = (tag.getFloat("VPShield")*(percent/100))*(1 + shieldBonus/100);
        float shield = (stealer.getPersistentData().getFloat("VPShield") + stolenShield);
        float stolenOverShield = (tag.getFloat("VPOverShield")*(percent/100))*(1 + shieldBonus/100);
        float overShield = (stealer.getPersistentData().getFloat("VPOverShield") + stolenOverShield);
        if(stealer.getPersistentData().getFloat("VPOverShield") <= 0 && overShield > 0)
            play(entity,SoundRegistry.OVERSHIELD.get());
        if(stealer.getPersistentData().getFloat("VPShield") <= 0 && shield > 0)
            play(entity,SoundRegistry.SHIELD.get());
        if(stealer.getPersistentData().getLong("VPAntiShield") < System.currentTimeMillis()) {
            if(stealer instanceof Player player && hasStellarVestige(ModItems.SOULBLIGHTER.get(), player)){
                boolean found = false;
                for (LivingEntity entityTarget: VPUtil.getEntities(player,30,false)) {
                    if (entityTarget.getPersistentData().hasUUID("VPPlayer")){
                        if(entityTarget.getPersistentData().getUUID("VPPlayer").equals(player.getUUID())){
                            found = true;
                            tag.putFloat("VPShield", Math.max(0,tag.getFloat("VPShield")-stolenShield));
                            entityTarget.getPersistentData().putFloat("VPShield", getShield(entityTarget)+stolenShield);
                            tag.putFloat("VPOverShield", Math.max(0,tag.getFloat("VPOverShield")-stolenOverShield));
                            entityTarget.getPersistentData().putFloat("VPOverShield", getOverShield(entityTarget)+stolenOverShield);
                        }
                    }
                }
                if(!found) {
                    tag.putFloat("VPShield", Math.max(0,tag.getFloat("VPShield")-stolenShield));
                    stealer.getPersistentData().putFloat("VPShield", shield);
                    tag.putFloat("VPOverShield", Math.max(0,tag.getFloat("VPOverShield")-stolenOverShield));
                    stealer.getPersistentData().putFloat("VPOverShield", overShield);
                }
            } else {
                tag.putFloat("VPShield", Math.max(0,tag.getFloat("VPShield")-stolenShield));
                stealer.getPersistentData().putFloat("VPShield", shield);
                tag.putFloat("VPOverShield", Math.max(0,tag.getFloat("VPOverShield")-stolenOverShield));
                stealer.getPersistentData().putFloat("VPOverShield", overShield);
            }
        }
        if(entity instanceof ServerPlayer player) {
            PacketHandler.sendToClient(new SendPlayerNbtToClient(player.getUUID(), player.getPersistentData()),player);
        } else {
            if(!entity.getCommandSenderWorld().isClientSide)
                PacketHandler.sendToClients(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SendEntityNbtToClient(entity.getPersistentData(),entity.getId()));
        }
        if(stealer.getPersistentData().getFloat("VPOverShieldMax") < shield) {
            stealer.getPersistentData().putFloat("VPOverShieldMax", shield);
        }
    }

    public static ResourceLocation getCurrentBiome(Player player) {
        if (player.getCommandSenderWorld() instanceof ServerLevel serverLevel) {
            BlockPos pos = player.blockPosition();
            return serverLevel.getBiomeManager().getBiome(pos).unwrapKey().get().location();
        }
        return null;
    }

    public static HashSet<MobEffect> effects = new HashSet<>();

    public static void initEffects(){
        for(MobEffect effect: ForgeRegistries.MOB_EFFECTS){
            effects.add(effect);
        }
    }

    public static HashSet<MobEffect> getEffects(){
        return effects;
    }

    public static List<String> getEffectsLeft(String list){
        List<String> effectList = new ArrayList<>(Arrays.asList(filterString(list).split(",")));
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

    public static List<String> getDamageKindsLeft(String list){
        List<String> damageList = new ArrayList<>(Arrays.asList(filterString(list).split(",")));
        List<String> allList = new ArrayList<>(getDamageKinds());
        allList.removeAll(damageList);
        return allList;
    }

    public static List<String> getRareItemsLeft(String list,Player player){
        List<String> rareList = new ArrayList<>(Arrays.asList(filterString(list).split(",")));
        List<String> allList = new ArrayList<>();
        for(Item item: hashRares){
            allList.add(item.getDescriptionId());
        }
        allList.removeAll(rareList);
        player.getPersistentData().putString("VPRaresClient", filterAndTranslate(allList.toString(),ChatFormatting.GRAY).getString());
        return allList;
    }

    public static List<String> getRaresClient(Player player){
        return new ArrayList<>(Arrays.asList(player.getPersistentData().getString("VPRaresClient").split(",")));
    }

    public static double calculateCatchChance(float playerHealth, float entityMaxHealth, float entityHealth){
        double probability;
        float base = (0.6f+(1-(entityHealth/entityMaxHealth)));
        if(playerHealth >= entityMaxHealth)
            probability = base;
        else probability = (base * (float) Math.pow(0.95f, Math.abs(playerHealth - entityMaxHealth) / 10));
        return probability*ConfigHandler.COMMON.soulBlighterChance.get();
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

        for (int i = 0; i < 100+radius; i++) {
            double targetX = originalX + (random.nextDouble() - 0.5) * 2.0 * radius;
            double targetY = Math.min(Math.max(originalY + (random.nextInt(2 * radius) - radius), 0), world.getMaxBuildHeight() - 1);
            double targetZ = originalZ + (random.nextDouble() - 0.5) * 2.0 * radius;

            if (isSafeLocation(world, targetX, targetY, targetZ)) {
                entity.teleportTo(targetX, targetY, targetZ);
                break;
            }
        }
    }

    public static boolean teleportRandomly(Entity entity, int radius,boolean water) {
        Random random = new Random();
        Level world = entity.getCommandSenderWorld();
        double originalX = entity.getX();
        double originalY = entity.getY();
        double originalZ = entity.getZ();

        for (int i = 0; i < 100+radius; i++) {
            double targetX = originalX + (random.nextDouble() - 0.5) * 2.0 * radius;
            double targetY = Math.min(Math.max(originalY + (random.nextInt(2 * radius) - radius), 0), world.getMaxBuildHeight() - 1);
            double targetZ = originalZ + (random.nextDouble() - 0.5) * 2.0 * radius;
            BlockPos blockPos = new BlockPos((int) targetX, (int) targetY, (int) targetZ);
            if (world.isWaterAt(blockPos.above(3)) && world.isWaterAt(blockPos) && world.isWaterAt(blockPos.east())
                    && world.isWaterAt(blockPos.west()) && world.isWaterAt(blockPos.north()) && world.isWaterAt(blockPos.south())) {
                entity.teleportTo(targetX, targetY, targetZ);
                return true;
            }
        }
        return false;
    }

    public static BlockPos randomPos(Entity entity, int radius) {
        Random random = new Random();
        Level world = entity.getCommandSenderWorld();
        double originalX = entity.getX();
        double originalY = entity.getY();
        double originalZ = entity.getZ();

        for (int i = 0; i < 100+radius; i++) {
            double targetX = originalX + (random.nextDouble() - 0.5) * 2.0 * radius;
            double targetY = Math.min(Math.max(originalY + (random.nextInt(2 * radius) - radius), 0), world.getMaxBuildHeight() - 1);
            double targetZ = originalZ + (random.nextDouble() - 0.5) * 2.0 * radius;

            if (isSafeLocation(world, targetX, targetY, targetZ)) {
                return new BlockPos( (int) targetX, (int) targetY, (int) targetZ);
            }
        }
        return null;
    }

    private static boolean isSafeLocation(Level world, double x, double y, double z) {
        BlockPos blockPos = new BlockPos((int) x,(int) y,(int) z);
        return world.isEmptyBlock(blockPos)
                && world.isEmptyBlock(blockPos.above())
                && !world.isEmptyBlock(blockPos.below());
    }

    public static void dealParagonDamage(LivingEntity entity,Player player,float damage, int type, boolean hurt){
        if(isFriendlyFireBetween(entity,player) || isProtectedFromHit(player,entity) || entity.isDeadOrDying())
            return;
        entity.setLastHurtByPlayer(player);
        float health = damage*(1+damagePercentBonus(player,type)/100);
        float overShields = getOverShield(entity);
        if(entity instanceof Player playerTarget && hasStellarVestige(ModItems.ARMOR.get(),player)){
            ItemStack stack = getVestigeStack(Armor.class,player);
            if(stack.getItem() instanceof Armor armor && armor.isUltimateActive(stack))
                dealParagonDamage(player,entity,damage*3,3,true);
        }
        if(entity.getPersistentData().getLong("VPBubble") > System.currentTimeMillis()) {
            VPUtil.play(player, SoundRegistry.BUBBLE1.get());
            entity.getPersistentData().putLong("VPBubble", 0);
            VPUtil.dealDamage(entity, player, player.damageSources().drown(), 1000, 3);
            entity.getPersistentData().putLong("VPWet", System.currentTimeMillis() + 20000);
            overShields /= 2;
            entity.getPersistentData().putFloat("VPOverShield",overShields);
            long time = 10000;
            if(hasStellarVestige(ModItems.WHIRLPOOL.get(),player)){
                if(getVestigeStack(Whirlpool.class,player).getItem() instanceof Vestige vestige && vestige.ultimateTimeBonus > 0)
                    time *= (long) vestige.ultimateTimeBonus;
            }
            entity.getPersistentData().putLong("VPWhirlParagon",System.currentTimeMillis()+time);
        }
        if(entity.getPersistentData().getInt("VPBossType") == 7){
            for(LivingEntity livingEntity: getEntitiesAround(entity,20,20,20,false)){
                if(livingEntity.getPersistentData().getBoolean("VPSummoned"))
                    return;
            }
        }
        if(overShields > 0) {
            if(entity.getPersistentData().getLong("VPWhirlParagon") > System.currentTimeMillis()) {
                health *= 8;
            }
            if(isNightmareBoss(entity))
                health *= 0.1f;
            if (overShields > health) {
                entity.getPersistentData().putFloat("VPOverShield", overShields - health);
                return;
            } else {
                entity.getPersistentData().putFloat("VPOverShield", 0);
                health -= overShields;
            }
        }
        float curseMultiplier = getCurseMultiplier(player,4);
        if(curseMultiplier > 0)
            health *= curseMultiplier;
        if(entity.getHealth()-health > 0) {
            setHealth(entity,entity.getHealth() - health);
            if(hurt)
                entity.hurt(player.damageSources().generic(),0);
            if(entity instanceof Player deb && hasVestige(ModItems.RUNE.get(), deb) && (VPUtil.getShield(deb) <= 0 || VPUtil.getOverShield(deb) <= 0)){
                if(VPUtil.getShield(deb) <= 0)
                    VPUtil.addShield(deb,health*0.5f,false);
                if(VPUtil.getOverShield(deb) <= 0)
                    VPUtil.addOverShield(deb,health*0.5f,true);
            }
        } else {
            deadInside(entity,player);
        }
    }

    public static void dealParagonDamage(LivingEntity entity,LivingEntity player,float damage, int type, boolean hurt){
        if(entity.isDeadOrDying())
            return;
        float health = damage;
        float overShields = getOverShield(entity);
        if(overShields > 0) {
            if (overShields > health) {
                entity.getPersistentData().putFloat("VPOverShield", overShields - health);
                return;
            } else {
                entity.getPersistentData().putFloat("VPOverShield", 0);
                health -= overShields;
            }
        }
        if(entity.getHealth()-health > 0) {
            if(hurt)
                entity.hurt(player.damageSources().generic(),0);
            entity.setHealth(entity.getHealth() - health);
            if(entity instanceof Player deb && hasVestige(ModItems.RUNE.get(), deb) && (VPUtil.getShield(deb) <= 0 || VPUtil.getOverShield(deb) <= 0)){
                if(VPUtil.getShield(deb) <= 0)
                    VPUtil.addShield(deb,health*0.5f,false);
                if(VPUtil.getOverShield(deb) <= 0)
                    VPUtil.addOverShield(deb,health*0.5f,true);
            }
        } else {
            deadInside(entity);
        }
    }

    public static List<TagKey<DamageType>> damageTypes(boolean warden){
        List<TagKey<DamageType>> damageTypes = new ArrayList<>();
        damageTypes.add(DamageTypeTags.DAMAGES_HELMET);
        damageTypes.add(DamageTypeTags.BYPASSES_ARMOR);
        damageTypes.add(DamageTypeTags.BYPASSES_SHIELD);
        damageTypes.add(DamageTypeTags.BYPASSES_INVULNERABILITY);
        damageTypes.add(DamageTypeTags.BYPASSES_EFFECTS);
        damageTypes.add(DamageTypeTags.BYPASSES_RESISTANCE);
        damageTypes.add(DamageTypeTags.BYPASSES_ENCHANTMENTS);
        if(!warden)
            damageTypes.add(DamageTypeTags.IS_FIRE);
        damageTypes.add(DamageTypeTags.IS_PROJECTILE);
        damageTypes.add(DamageTypeTags.WITCH_RESISTANT_TO);
        damageTypes.add(DamageTypeTags.IS_EXPLOSION);
        damageTypes.add(DamageTypeTags.IS_FALL);
        damageTypes.add(DamageTypeTags.IS_DROWNING);
        damageTypes.add(DamageTypeTags.IS_FREEZING);
        damageTypes.add(DamageTypeTags.IS_LIGHTNING);
        if(!warden)
            damageTypes.add(DamageTypeTags.NO_ANGER);
        damageTypes.add(DamageTypeTags.NO_IMPACT);
        damageTypes.add(DamageTypeTags.ALWAYS_MOST_SIGNIFICANT_FALL);
        damageTypes.add(DamageTypeTags.WITHER_IMMUNE_TO);
        if(!warden)
            damageTypes.add(DamageTypeTags.IGNITES_ARMOR_STANDS);
        if(!warden)
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

    public static void vestigeNullify(Player player){
        Archlinx.removeModifiers(player);
        player.getPersistentData().putFloat("VPShield", 0);
        player.getPersistentData().putFloat("VPOverShield", 0);
        player.getPersistentData().putInt("VPDevourerHits",0);
        player.getPersistentData().putFloat("VPHealResFlower", 0);
        player.getPersistentData().putFloat("VPShieldBonusFlower", 0);
        Multimap<Attribute, AttributeModifier> mark = HashMultimap.create();
        mark.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("4fc18d7a-9353-45b1-ad77-29117c1d9e6f"), "vp:attack_speed_modifier_mark", 2, AttributeModifier.Operation.MULTIPLY_BASE));
        mark.put(Attributes.ATTACK_SPEED, new AttributeModifier(UUID.fromString("78cf254b-36df-41d6-be91-ad06220d9dd8"), "vp:speed_modifier_mark", 2, AttributeModifier.Operation.MULTIPLY_BASE));
        mark.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("54b7f5ed-0851-4745-b98c-e1f08a1a2f67"), "vp:speed_modifier_mark", 2, AttributeModifier.Operation.MULTIPLY_BASE));
        player.getAttributes().removeAttributeModifiers(mark);
        Multimap<Attribute, AttributeModifier> mask = HashMultimap.create();
        mask.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("ec62548c-5b26-401e-83fd-693e4aafa532"), "vp:attack_speed_modifier", 0, AttributeModifier.Operation.MULTIPLY_TOTAL));
        mask.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("f4ece564-d2c0-40d2-a96a-dc68b493137c"), "vp:speed_modifier", 0, AttributeModifier.Operation.MULTIPLY_BASE));
        player.getAttributes().removeAttributeModifiers(mask);
        Multimap<Attribute, AttributeModifier> midas = HashMultimap.create();
        midas.put(Attributes.LUCK, new AttributeModifier(UUID.fromString("f55f3429-0399-4d9e-9f84-0d7156cc0593"), "vp:luck", 0, AttributeModifier.Operation.ADDITION));
        player.getPersistentData().putInt("VPPrism", 0);
        player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("55ebb7f1-2368-4b6f-a123-f3b1a9fa30ea"),0, AttributeModifier.Operation.ADDITION,"vp:soulblighter_hp_boost"));
        player.getPersistentData().putBoolean("VPSweetUlt",false);
        player.getPersistentData().putFloat("VPSaturation",0);
        player.getPersistentData().putFloat("VPHealBonusDonut", 0);
        player.getPersistentData().putFloat("VPShieldBonusDonut", 0);
        player.getPersistentData().putFloat("VPHealBonusDonutPassive",0);
        player.getPersistentData().putFloat("VPTrigonBonus", 0);
        player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("8dac9436-c37f-4b74-bf64-8666258605b9"), 1, AttributeModifier.Operation.MULTIPLY_TOTAL, "vp:trigon_hp_boost"));
        if(player.isAlive() && player.getHealth() > player.getMaxHealth())
            player.setHealth(player.getMaxHealth());
        sync(player);
        if (player.isCreative()) {
            return;
        }
        player.getAbilities().mayfly = false;
        player.getAbilities().flying = false;
        player.onUpdateAbilities();
        if (player instanceof ServerPlayer serverPlayer)
            PacketHandler.sendToClient(new PlayerFlyPacket(2), serverPlayer);
    }

    public static void sync(Player player){
        if(player instanceof ServerPlayer serverPlayer) {
            CompoundTag sendNudes = new CompoundTag();
            for (String key : player.getPersistentData().getAllKeys()) {
                if (key.startsWith("VP") && player.getPersistentData().get(key) != null) {
                    sendNudes.put(key, player.getPersistentData().get(key));
                }
            }
            PacketHandler.sendToClient(new SendPlayerNbtToClient(player.getUUID(), sendNudes), serverPlayer);
        }
    }

    public static int getSet(Player player){
        int set = 0;
        List<Integer> types = new ArrayList<>();
        for(ItemStack stack: getAccessoryList(player)){
            if(stack.getItem() instanceof Accessory accessory){
                types.add(accessory.getType(stack));
            }
        }
        if(types.isEmpty())
            return set;

        List<Integer> counts = new ArrayList<>(Collections.nCopies(6, 0));

        for (int type : types) {
            counts.set(type, counts.get(type) + 1);
        }

        int uniqueCounts = 0;
        for (int count : counts) {
            if (count > 0) {
                uniqueCounts++;
            }
        }

        if (counts.get(1) >= 2 && counts.get(4) >= 2) {
            set = 1;
        } else if (counts.get(2) >= 2 && counts.get(3) >= 2) {
            set = 2;
        } else if (counts.get(5) >= 2 && counts.get(4) >= 2) {
            set = 3;
        } else if (counts.get(3) >= 4) {
            set = 4;
        } else if (counts.get(2) >= 4) {
            set = 5;
        } else if (counts.get(5) >= 4) {
            set = 6;
        } else if (counts.get(1) >= 4) {
            set = 7;
        } else if (counts.get(4) >= 4) {
            set = 8;
        } else if (uniqueCounts == 4) {
            set = 9;
        } else if (types.size() == 4) {
            set = 10;
        }
        return set;
    }

    public static List<String> vortexItems(){
        List<String> list = new ArrayList<>();
        String stringList = ConfigHandler.COMMON.repairObjects.get().toString();
        for(Item item: items) {
            for (String element : stringList.split(",")) {
                if (item.getDescriptionId().contains(element) && !list.contains(item.getDescriptionId())) {
                    boolean add = true;
                    for(String name : ConfigHandler.COMMON.repairBlackList.get().toString().split(",")){
                        if(name.equals(element)){
                            add = false;
                        }
                    }
                    if(add)
                        list.add(item.getDescriptionId());
                }
            }
        }
        return list;
    }

    public static int getCurseAmount(ItemStack stack) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        int totalCurses = 0;

        for (Enchantment enchantment: enchantments.keySet()) {
            if (enchantment.isCurse() && enchantments.get(enchantment) > 0) {
                totalCurses+=1;
            }
        }
        return totalCurses;
    }

    public static int getCurseAmount(Player player) {
        int count = 0;
        for (ItemStack theStack : getFullEquipment(player)) {
            if (theStack != null) {
                count += getCurseAmount(theStack);
            }
        }

        return count;
    }

    public static List<ItemStack> getFullEquipment(Player player) {
        List<ItemStack> equipmentStacks = Lists.newArrayList();

        equipmentStacks.add(player.getMainHandItem());
        equipmentStacks.add(player.getOffhandItem());
        equipmentStacks.addAll(player.getInventory().armor);
        equipmentStacks.addAll(getCurioList(player));

        return equipmentStacks;
    }

    public static boolean isEasterEvent() {
        if(ConfigHandler.COMMON.easter.get())
            return true;
        LocalDate today = LocalDate.now();
        int year = today.getYear();

        int a = year % 19;
        int b = year / 100;
        int c = year % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int i1 = h + l - 7 * m + 114;
        int month = i1 / 31;
        int day = (i1 % 31) + 1;

        LocalDate easterDate = LocalDate.of(year, month, day);

        return today.equals(easterDate);
    }

    public static boolean spawnEgg(Player player){
        BlockPos pos = player.getOnPos();
        EasterEggEntity easterEggEntity = new EasterEggEntity(player.getCommandSenderWorld());
        easterEggEntity.setPos(pos.getX(),pos.getY(),pos.getZ());
        VPUtil.teleportRandomly(easterEggEntity,60);
        int x = player.getPersistentData().getInt("VPEggX");
        int y = player.getPersistentData().getInt("VPEggY");
        int z = player.getPersistentData().getInt("VPEggZ");
        double distance = pos.distToCenterSqr(new Vec3(x,y,z));
        if(isSafeLocation(player.getCommandSenderWorld(),easterEggEntity.getX(),easterEggEntity.getY(),easterEggEntity.getZ()) && distance > 20000) {
            player.getCommandSenderWorld().addFreshEntity(easterEggEntity);
            player.getPersistentData().putInt("VPEggX", (int) player.getX());
            player.getPersistentData().putInt("VPEggY", (int) player.getY());
            player.getPersistentData().putInt("VPEggZ", (int) player.getZ());
            play(player, SoundEvents.EGG_THROW);
            return true;
        }
        return false;
    }

    public static void giveStack(ItemStack stack, Player player){
        if(player.getInventory().getFreeSlot() != -1)
            player.addItem(stack);
        else player.drop(stack,false);
    }

    public static void resync(PlayerCapabilityVP cap, Player player){
        if(!player.getCommandSenderWorld().isClientSide) {
            VPUtil.getBiomesLeft(cap.getBiomesFound(), player);
            VPUtil.getMonsterLeft(cap.getMonstersKilled(),player);
            VPUtil.getBossesLeft(cap.getBosses(),player);
            VPUtil.getMobsLeft(cap.getMobsTamed(),player);
            VPUtil.getRareItemsLeft(cap.getrareItems(),player);
        }
    }

    public static boolean notContains(String list, String word){
        boolean notContains = true;
        if(list.isEmpty())
            return true;
        for (String element : list.split(",")) {
            if (element.equals(word)) {
                notContains = false;
                break;
            }
        }
        return notContains;
    }

    public static float dreadAbsorbtion(float number,float capAbsorb) {
        float maxCap = (float) (ConfigHandler.COMMON.nightmareDamageCap.get() + 0);
        float baseAbsorb = 0.05f;
        float cap = Math.max(1,(maxCap-capAbsorb));
        float damage;
        if (number <= cap) {
            damage = number * baseAbsorb;
        } else {
            damage = baseAbsorb*100 + 10f * (float) Math.log10(number);
        }
        return damage*maxCap/(maxCap+cap*baseAbsorb*100);
    }

    public static boolean hasLyra(LivingEntity entity, int number){
        return entity.getPersistentData().getLong("VPLyra"+number) > System.currentTimeMillis();
    }

    public static List<Item> fishList = new ArrayList<>();
    private static final Map<ResourceLocation, List<Item>> biomeFishMap = new HashMap<>();

    public static void initFishItems(){
        for(Item item: items){
            for(String name: ConfigHandler.COMMON.fishObjects.get().toString().split(",")) {
                if (item.getDescriptionId().contains(name) && !isBlackListed(item)){
                    fishList.add(item);
                }
            }
        }
    }

    public static boolean isBlackListed(Item item){
        for(String name: ConfigHandler.COMMON.fishingBlacklist.get().toString().split(",")) {
            if (item.getDescriptionId().contains(name)){
                return true;
            }
        }
        return false;
    }

    public static void initFishDrops(List<ResourceLocation> biomes) {
        Random random = new Random();
        for (ResourceLocation biome : biomes) {
            biomeFishMap.put(biome, new ArrayList<>());
        }

        int index = 0;
        for (Item item : fishList) {
            ResourceLocation biome = biomes.get(index % biomes.size());
            biomeFishMap.get(biome).add(item);
            index++;
        }

        for (ResourceLocation biome : biomes) {
            List<Item> items = biomeFishMap.get(biome);
            while (items.size() < 8) {
                items.add(fishList.get(random.nextInt(fishList.size())));
            }
        }
    }

    public static ItemStack getFishDrop(Player player){
        ItemStack stack = ItemStack.EMPTY;
        Random random = new Random();
        if(player.getCommandSenderWorld() instanceof ServerLevel serverLevel){
            if(fishList.isEmpty())
                initFishItems();
            if(biomeFishMap.isEmpty())
                initFishDrops(getBiomes());
            List<Item> items = biomeFishMap.getOrDefault(getCurrentBiome(player), new ArrayList<>());
            if (!items.isEmpty()) {
                Item randomItem = items.get(random.nextInt(items.size()-1));
                while(ConfigHandler.COMMON.fishingBlacklist.get().toString().contains(randomItem.getDescriptionId())){
                    randomItem = items.get(random.nextInt(items.size()-1));
                }
                if((stack.getItem() instanceof PinkyPearl && random.nextDouble() > getChance(ConfigHandler.COMMON.rareFishingDropChance.get(),player)/10)){
                    randomItem = items.get(random.nextInt(items.size()-1));
                }
                else if((isRare(stack) && random.nextDouble() > getChance(ConfigHandler.COMMON.rareFishingDropChance.get(),player)))
                    randomItem = items.get(random.nextInt(items.size()-1));
                stack = new ItemStack(randomItem);
            }
        }
        return stack;
    }

    public static boolean isRare(ItemStack stack){
        for(String name: ConfigHandler.COMMON.rareItems.get().toString().split(",")) {
            if (stack.getDescriptionId().contains(name))
                return true;
        }
        return false;
    }

    public static void initFishMap(){
        if(fishList.isEmpty())
            initFishItems();
        if(biomeFishMap.isEmpty())
            initFishDrops(getBiomes());
    }

    public static void printFishDrop(Player player){
        if(fishList.isEmpty())
            initFishItems();
        if(biomeFishMap.isEmpty())
            initFishDrops(getBiomes());
        List<Item> items = biomeFishMap.getOrDefault(getCurrentBiome(player), new ArrayList<>());
        Item item;
        Random random = new Random();
        ChatFormatting style;
        player.sendSystemMessage(Component.literal(""));
        player.sendSystemMessage(Component.literal(""));
        for(int i = 0; i < items.size();i++) {
            int numba = random.nextInt(3);
            item = items.get(i);
            if(isRare(new ItemStack(item))) {
                style = ChatFormatting.RED;
                player.sendSystemMessage(Component.literal((i + 1) + ") ").append(Component.translatable(item.getDescriptionId())).withStyle(style).withStyle(ChatFormatting.BOLD));
            }
            else {
                if (numba == 1)
                    style = ChatFormatting.BLUE;
                else if (numba == 2)
                    style = ChatFormatting.AQUA;
                else
                    style = ChatFormatting.DARK_AQUA;
                player.sendSystemMessage(Component.literal((i + 1) + ") ").append(Component.translatable(item.getDescriptionId())).withStyle(style));
            }
        }
    }

    public static int getWaterDepth(Player player) {
        Level level = player.getCommandSenderWorld();
        BlockPos playerPos = player.blockPosition();

        if(!player.isInWaterRainOrBubble())
            return 0;

        int depth = 0;
        BlockPos checkPos = playerPos;

        while (level.getBlockState(checkPos).getBlock() == Blocks.WATER) {
            depth++;
            checkPos = checkPos.above();
        }

        return depth;
    }

    public static String filterString(String string){
        if(string.isEmpty())
            return string;
        StringBuilder builder = new StringBuilder(string);
        if(string.startsWith("["))
            builder.deleteCharAt(0);
        if(string.endsWith("]"))
            builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public static Component translateString(String string, ChatFormatting color) {
        List<String> list = new ArrayList<>(List.of(string.split(",")));
        StringBuilder translatedText = new StringBuilder();
        for (String name : list) {
            Component translatedComponent = Component.translatable(name.trim());
            String plain = translatedComponent.getString().replaceAll("§.", "");
            translatedText.append(plain).append(", ");
        }
        if (translatedText.length() > 2) {
            translatedText.setLength(translatedText.length() - 2);
        }
        return Component.literal(translatedText.toString()).withStyle(color);
    }


    public static Component filterAndTranslate(String string, ChatFormatting color){
        return translateString(filterString(string),color);
    }

    public static Component filterAndTranslate(String string){
        return translateString(filterString(string),ChatFormatting.GRAY);
    }

    public static List<String> filterList(List<String> list){
        List<String> filtered = new ArrayList<>();
        for(String element: list){
            filtered.add(element.trim());
        }
        return filtered;
    }

    public static Map<EntityType<?>,Float> zaebali = new HashMap<>();

    public static float getBaseHealth(EntityType<?> type){
        if(zaebali.isEmpty() || !zaebali.containsKey(type)){
            return 0;
        }
        return zaebali.get(type);
    }

    public static void spawnBoss(LivingEntity entity){
        entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity,Attributes.MAX_HEALTH,UUID.fromString("ee3a5be4-dfe5-4756-b32b-3e3206655f47"),ConfigHandler.COMMON.bossHP.get(), AttributeModifier.Operation.MULTIPLY_TOTAL,"vp:boss_health"));
        entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity,Attributes.ATTACK_DAMAGE,UUID.fromString("c87d7c0e-8804-4ada-aa26-8109a1af8b31"),ConfigHandler.COMMON.bossAttack.get(), AttributeModifier.Operation.MULTIPLY_TOTAL,"vp:boss_damage"));
        entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity,Attributes.ARMOR,UUID.fromString("5cb61d4f-d008-40d9-8353-d2d2c302503a"),ConfigHandler.COMMON.armorHardcore.get(), AttributeModifier.Operation.ADDITION,"vp:boss_armor"));
        entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity,Attributes.ARMOR_TOUGHNESS,UUID.fromString("fe739733-3069-41af-93af-321759771f52"),ConfigHandler.COMMON.armorHardcore.get(), AttributeModifier.Operation.ADDITION,"vp:boss_armor_toughness"));
        VPUtil.addShield(entity, (float) (entity.getMaxHealth()*ConfigHandler.COMMON.shieldHardcore.get()),false);
        VPUtil.setHealth(entity,entity.getMaxHealth()); //test
        entity.getPersistentData().putFloat("VPOverShield", (float) (entity.getMaxHealth()*ConfigHandler.COMMON.overShieldHardcore.get()));
        if(VPUtil.isNightmareBoss(entity)){
            entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity, Attributes.MAX_HEALTH, UUID.fromString("534c53b9-3c22-4c34-bdcd-f255a9694b34"),10, AttributeModifier.Operation.MULTIPLY_TOTAL,"vp:nightmare.hp"));
            entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity, Attributes.ATTACK_DAMAGE, UUID.fromString("1d665861-143f-4906-9ab0-e511ad377783"),10, AttributeModifier.Operation.MULTIPLY_TOTAL,"vp:nightmare.attack"));
            entity.setHealth(entity.getMaxHealth());
            VPUtil.addShield(entity, (float) (entity.getMaxHealth()*ConfigHandler.COMMON.shieldHardcore.get()),false);
            entity.getPersistentData().putFloat("VPOverShield", (float) (entity.getMaxHealth()*ConfigHandler.COMMON.overShieldHardcore.get()));
            AABB boundingBox = entity.getBoundingBox();
            AABB scaledBoundingBox = boundingBox.inflate(boundingBox.getXsize(), boundingBox.getYsize(), boundingBox.getZsize());
            entity.setBoundingBox(scaledBoundingBox);
        }
        entity.refreshDimensions(); //test
    }

    public static void vzlomatJopu(float value){
        final Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(new ResourceLocation("generic.max_health"));
        if (attribute instanceof RangedAttribute ranged) {
            final VzlomJopiMixin vzlom = (VzlomJopiMixin) (Object) attribute;
            if (ranged.getMaxValue() != value)
                vzlom.vzlomatJopu(value);
        }
    }

    public static boolean isPoisonedByNightmare(Player player){
        if(player.hasEffect(MobEffects.POISON)){
            for(LivingEntity entity: getEntitiesAround(player,30,30,30,false)){
                if(isNightmareBoss(entity))
                    return true;
            }
        }
        return false;
    }

    public static void updateStats(Player player){
        List<ItemStack> list = VPUtil.getVestigeList(player);
        for(ItemStack itemStack: list){
            if(itemStack.getItem() instanceof Vestige vestige){
                vestige.init(itemStack);
                vestige.applyBonus(itemStack,player);
            }
        }
    }

    public static void nightmareDamageEvent(LivingEntity boss, Player player, LivingDamageEvent event){
        Random random = new Random();
        float attack = (float) boss.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        int type = boss.getPersistentData().getInt("VPBossType");
        if(boss.getHealth() <= boss.getMaxHealth()*0.5f){
            dealParagonDamage(player,boss,event.getAmount()*0.1f,0,true);
        }
        if(type == 1){
            player.getPersistentData().putFloat("VPHealDebt", player.getPersistentData().getFloat("VPHealDebt") + player.getMaxHealth() * 2);
            if ((player.getHealth() < player.getMaxHealth() * 0.3 || player.getMaxHealth() <= 5) && random.nextDouble() < getChance(0.2,player))
                deadInside(player);
            float stack = player.getPersistentData().getFloat("VPIgnis");
            player.getPersistentData().putFloat("VPIgnis",Math.min(99,stack+5));
            player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player,Attributes.MAX_HEALTH,UUID.fromString("4ed92da8-dd60-41a2-9540-cc8816af92e2"),1, AttributeModifier.Operation.ADDITION,"vp:ignis_hp"));
            player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player,Attributes.ARMOR,UUID.fromString("39f598b3-b75c-422f-93fc-8e65dade8730"),1, AttributeModifier.Operation.ADDITION,"vp:ignis_armor"));
            player.getAttributes().addTransientAttributeModifiers(createAttributeMap(player,Attributes.MAX_HEALTH,UUID.fromString("4ed92da8-dd60-41a2-9540-cc8816af92e2"),-player.getMaxHealth()*(stack/100f), AttributeModifier.Operation.ADDITION,"vp:ignis_hp"));
            player.getAttributes().addTransientAttributeModifiers(createAttributeMap(player,Attributes.ARMOR,UUID.fromString("39f598b3-b75c-422f-93fc-8e65dade8730"),-player.getArmorValue()*(stack/100f), AttributeModifier.Operation.ADDITION,"vp:ignis_armor"));
            player.getPersistentData().putLong("VPIgnisTime",System.currentTimeMillis()+60000);
            player.setHealth(player.getMaxHealth());
        }
        if(type == 2){
            player.addEffect(new MobEffectInstance(MobEffects.POISON,255,255));
            player.addEffect(new MobEffectInstance(getRandomEffect(false),255,255));
            updateStats(player);
        }
        if(type == 3){
            List<MobEffectInstance> list = getEffectsHas(player,true);
            if(!list.isEmpty()) {
                MobEffectInstance effectInstance = list.get(random.nextInt(list.size()-1));
                boss.addEffect(effectInstance);
                player.removeEffect(effectInstance.getEffect());
            }
        }
        if(type == 4){
            if(random.nextDouble() < getChance(0.3,player)){
                player.invulnerableTime = 0;
                float damage = attack;
                int shield = 1;
                if(player.isInWaterRainOrBubble()) {
                    damage *= 30;
                    shield = 5;
                }
                player.hurt(boss.damageSources().freeze(),damage);
                boss.getPersistentData().putInt("VPFreezeShield",shield);
            }
        }
        if(type == 5){
            if((!player.onGround() && !player.isInWater()) || (!boss.onGround() && !boss.isInWater()))
                dealParagonDamage(player,boss,attack*0.05f,0,true);
        }
        if(type == 6 && (getShield(player) > 0 || getOverShield(player) > 0))
            event.setAmount(event.getAmount()*8);
        double brightness = (player.getCommandSenderWorld().getBrightness(LightLayer.BLOCK,boss.blockPosition())+player.getCommandSenderWorld().getBrightness(LightLayer.SKY,boss.blockPosition()))/2D;
        if(type == 7){
            boolean somebodyHelp = false;
            for(LivingEntity livingEntity: getEntities(player,20,false)){
                if(livingEntity instanceof Player)
                    somebodyHelp = true;
            }
            if(!somebodyHelp)
                event.setAmount(event.getAmount()*10);
            if(brightness <=7) {
                player.invulnerableTime = 0;
                float damage = attack * 2;
                if (!somebodyHelp) {
                    damage *= 10;
                }
                player.hurt(boss.damageSources().fellOutOfWorld(), damage);
                dealParagonDamage(player,boss,50,0,true);
            }
        }
    }

    public static void boostEntity(LivingEntity livingEntity,float amount, float shields, float overShields){
        livingEntity.getAttributes().addTransientAttributeModifiers(createAttributeMap(livingEntity, Attributes.MAX_HEALTH, UUID.randomUUID(), amount, AttributeModifier.Operation.MULTIPLY_TOTAL, "vp:boss7:1"));
        livingEntity.getAttributes().addTransientAttributeModifiers(createAttributeMap(livingEntity, Attributes.ARMOR, UUID.randomUUID(), amount, AttributeModifier.Operation.MULTIPLY_TOTAL, "vp:boss7:5"));
        livingEntity.getAttributes().addTransientAttributeModifiers(createAttributeMap(livingEntity, Attributes.ARMOR_TOUGHNESS, UUID.randomUUID(), amount, AttributeModifier.Operation.MULTIPLY_TOTAL, "vp:boss7:6"));
        livingEntity.getAttributes().addTransientAttributeModifiers(createAttributeMap(livingEntity, Attributes.ATTACK_DAMAGE, UUID.randomUUID(), amount, AttributeModifier.Operation.MULTIPLY_TOTAL, "vp:boss7:4"));
        livingEntity.getAttributes().addTransientAttributeModifiers(createAttributeMap(livingEntity, Attributes.MOVEMENT_SPEED, UUID.randomUUID(), scaleDown(amount,2), AttributeModifier.Operation.MULTIPLY_TOTAL, "vp:boss7:7"));
        livingEntity.setHealth(livingEntity.getMaxHealth());
        if(shields > 0) {
            livingEntity.getPersistentData().putFloat("VPShieldInit",shields);
            addShield(livingEntity, shields, true);
        }
        if(overShields > 0) {
            livingEntity.getPersistentData().putFloat("VPOverShield", overShields);
            livingEntity.getPersistentData().putFloat("VPOverShieldMax", overShields);
        }
        livingEntity.getPersistentData().putBoolean("VPEmpowered",true);
        syncEntity(livingEntity);
    }

    public static void nightmareTickEvent(LivingEntity entity){
        Random random = new Random();
        float slow = 1;
        boolean rage = entity.getHealth() < entity.getMaxHealth() * 0.5;
        if(rage)
            slow = 1;
        if(entity.getHealth() < entity.getMaxHealth() * 0.15)
            slow = 0.2f;
        if(rage && entity.getPersistentData().getLong("VPTick") < System.currentTimeMillis()){
            entity.getPersistentData().putLong("VPTick",System.currentTimeMillis()+1000);
            entity.tick();
        }
/*        if(entity.tickCount > (10*60*20*slow))
            entity.discard();*/
        if(entity instanceof Monster monster){
            if(monster.getTarget() == null || !(monster.getTarget() instanceof Player))
                ((MobEntityVzlom)monster).setTarget(monster.getCommandSenderWorld().getNearestPlayer(monster,45));
            if(monster.getTarget() != null) {
                LivingEntity target = monster.getTarget();
                if(entity.getPersistentData().getLong("VPGhost") > System.currentTimeMillis()){
                    entity.noPhysics = true;
                    suckToPos(entity, new BlockPos((int) target.getX(), (int) target.getY(), (int) target.getZ()), 10);
                } else {
                    entity.noPhysics = false;
                }
                if(entity.tickCount % (20*slow) == 0) {
                    target = monster.getTarget();
                    if (!monster.isWithinMeleeAttackRange(target))
                        entity.getPersistentData().putInt("VPReach", entity.getPersistentData().getInt("VPReach") + 1);
                    else {
                        entity.getPersistentData().putInt("VPReach", 0);
                        if (entity.getPersistentData().getLong("VPGhost") > 0) {
                            monster.doHurtTarget(target);
                            entity.noPhysics = false;
                            //((Monster) entity).setNoAi(false);
                            entity.getPersistentData().putLong("VPGhost", 0);
                        }
                    }
                }
            }
        }
        if(entity.getPersistentData().getInt("VPReach") >= 13){
            entity.getPersistentData().putInt("VPReach",0);
            entity.getPersistentData().putLong("VPGhost",System.currentTimeMillis()+13000);
        }
        if(entity.tickCount % (2*20*slow) == 0) {
            entity.getPersistentData().putFloat("VPDreadAbsorb", Math.min((float)(ConfigHandler.COMMON.nightmareDamageCap.get()+0),entity.getPersistentData().getFloat("VPDreadAbsorb")+(float)(ConfigHandler.COMMON.nightmareDamageCap.get()*0.01)));
        }
        if (entity.getHealth() < entity.getMaxHealth() * 0.5) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 255, 255));
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 255, 255));
        }
        if(entity.tickCount % (5*20*slow) == 0)
            clearEffects(entity,false);
        if(!entity.isCurrentlyGlowing())
            entity.setGlowingTag(true);
        float attack = (float)(100*ConfigHandler.COMMON.hardcoreDamage.get());
        if(entity.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE))
            attack = (float) entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        int type = entity.getPersistentData().getInt("VPBossType");
        if(entity.tickCount % 2 == 0) {
            nightmareAura(type, entity);
        }
        if(type == 1 && entity.tickCount % (20*slow) == 0){
            spawnCircleParticles(entity,60,ParticleTypes.FLAME,5,0.5);
            spawnCircleParticles(entity,30,ParticleTypes.FLAME,3,1);
            spawnCircleParticles(entity,10,ParticleTypes.FLAME,2,1.5);
        }
        if(type == 2){
            if(entity.tickCount % (40*slow) == 0) {
                int count = 0;
                for (LivingEntity livingEntity : getEntitiesAround(entity, 20, 20, 20, false)) {
                    if (livingEntity instanceof Player)
                        count++;
                }
                entity.getPersistentData().putFloat("VPAcsHeal", count * 30);
            }
            if(entity.tickCount % (5*20*slow) == 0) {
                CloudEntity cloudEntity = new CloudEntity(entity.getCommandSenderWorld(), entity);
                cloudEntity.teleportTo(entity.getX(),entity.getY(),entity.getZ());
                teleportRandomly(cloudEntity,20);
                entity.getCommandSenderWorld().addFreshEntity(cloudEntity);
            }
        }
        if(type == 3){
            if(entity.tickCount % (200*slow) == 0) {
                regenOverShield(entity, getOverShield(entity) * 0.1f);
                if(random.nextDouble() < 0.2){
                    spawnCircleParticles(entity,30,ParticleTypes.POOF,30,0.5);
                    for(LivingEntity livingEntity: getEntitiesAround(entity,20,20,20,false)){
                        livingEntity.invulnerableTime = 0;
                        livingEntity.hurt(entity.damageSources().inWall(),attack*40);
                    }
                    play(entity, SoundEvents.STONE_BREAK);
                    play(entity, SoundEvents.STONE_PLACE);
                    play(entity, SoundEvents.STONE_BREAK);
                    play(entity, SoundEvents.STONE_BREAK);
                    play(entity, SoundEvents.STONE_BREAK);
                    play(entity, SoundEvents.STONE_BREAK);
                }
            }
            for(LivingEntity livingEntity: getEntitiesAround(entity,20,20,20,false)){
                if(livingEntity instanceof Player player && player.getPersistentData().getBoolean("VPAttacked")) {
                    player.getPersistentData().putBoolean("VPAttacked", false);
                    player.invulnerableTime = 0;
                    player.hurt(randomizeDamageType(player),random.nextInt());
                    entity.getPersistentData().putLong("VPDef",System.currentTimeMillis()+10000);
                }
            }
            if((entity.tickCount % (200*slow) == 0 && entity.getPersistentData().getLong("VPDef") == 0))
                entity.getPersistentData().putLong("VPDef",System.currentTimeMillis());
        }
        if(type == 4){
            if(entity.getPersistentData().getInt("VPFreezeShield") > 0)
                spawnSphere(entity,ParticleTypes.SNOWFLAKE,20,3,0);
            if(entity.tickCount % (50*slow) == 0 && random.nextDouble() < 0.3){
                entity.getPersistentData().putLong("VPBlizzard",System.currentTimeMillis()+10000);
            }
        }
        if(type == 5){
            double x = 5;
            double y = 5;
            double z = 5;
            for(Entity element: entity.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, new AABB(entity.getX()+x,entity.getY()+y,entity.getZ()+z,entity.getX()-x,entity.getY()-y,entity.getZ()-z))){
                if(element instanceof Projectile projectile && !(element instanceof BlackHole) && !(element instanceof VortexEntity) && !(element instanceof CloudEntity)){
                    projectile.setDeltaMovement(projectile.getDeltaMovement().reverse());
                    projectile.setOwner(entity);
                }
            }
        }
        if(type == 6){
            if(entity.tickCount % (3*20*slow) == 0)
                regenOverShield(entity,getOverShield(entity)*0.05f);
            for(LivingEntity livingEntity: getEntitiesAround(entity,5,5,5,false)) {
                if (livingEntity instanceof Player player && !player.isCreative()) {
                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN,255,255));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,255,255));
                    player.getPersistentData().putLong("VPForbidden",System.currentTimeMillis()+1000);
                }
            }
        }
        if(type == 7){
            int amount = 0;
            for(LivingEntity livingEntity: getEntitiesAround(entity,40,40,40,false)){
                if(livingEntity.getPersistentData().getBoolean("VPSummoned"))
                    amount++;
            }
            if(entity instanceof Monster boss && boss.getTarget() != null && boss.getTarget().getPersistentData().getBoolean("VPSummoned")) {
                boss.setTarget(null);
                Player player = boss.getCommandSenderWorld().getNearestPlayer(boss,40);
                if(player != null){
                    boss.setTarget(player);
                }
            }
            float shield = 3000;
            float overShield = 1000;
            if(entity.tickCount % (30*20*slow) == 0 && amount < 20) {
                Level level = entity.getCommandSenderWorld();
                for (int i = 0; i < random.nextInt(15)+5; i++) {
                    Entity monster = getRandomMonster().create(level);
                    if (monster instanceof LivingEntity livingEntity) {
                        int count = 0;
                        while((isBoss(livingEntity) || livingEntity instanceof HunterKiller) && count < 100){
                            count++;
                            monster = getRandomMonster().create(level);
                            if(monster instanceof LivingEntity livingEntity1)
                                livingEntity = livingEntity1;
                        }
                        if(count >= 100)
                            continue;
                        livingEntity.teleportTo(entity.getX(), entity.getY(), entity.getZ());
                        teleportRandomly(livingEntity, 10);
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100000, 255));
                        boostEntity(livingEntity,10,shield,overShield);
                        livingEntity.getPersistentData().putBoolean("VPSummoned", true);
                        livingEntity.setHealth(livingEntity.getMaxHealth());
                        level.addFreshEntity(livingEntity);
                    }
                }
            }
            if(entity.tickCount % (10*20*slow) == 0) {
                for(LivingEntity livingEntity: getEntitiesAround(entity,20,20,20,false)){
                    if(livingEntity.getPersistentData().getBoolean("VPSummoned")){
                        livingEntity.heal(livingEntity.getMaxHealth()*0.1f);
                        livingEntity.getPersistentData().putFloat("VPShieldInit", getShield(livingEntity)+shield);
                        addShield(livingEntity,shield,false);
                        regenOverShield(livingEntity,overShield);
                        if(livingEntity instanceof Monster monster && entity instanceof Monster boss && boss.getTarget() != null)
                            monster.setTarget(boss.getTarget());
                    }
                }
            }
        }
        BlockPos pos = randomPos(entity,20);
        boolean lift = false;
        BlockPos position = randomPos(entity,15);
        ServerLevel serverLevel = null;
        List<LivingEntity> lightList = new ArrayList<>();
        for(LivingEntity livingEntity: getEntitiesAround(entity,30,30,30,false)){
            if(livingEntity instanceof Player player && !player.isCreative()) {
                double x = player.getX();
                double y = player.getY();
                double z = player.getZ();
                if(type == 1 && entity.tickCount % (4*20*slow) == 0){
                    player.setSecondsOnFire(entity.getRemainingFireTicks()+10000);
                    player.hurt(entity.damageSources().lava(),attack*0.6f);
                }
                if(type == 2 && (entity.tickCount % (10*20*slow)) == 0){
                    spawnParticle(player,ParticleTypes.CLOUD,x,y,z,0,0,0);
                }
                if(type == 4){
                    player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN,255,255));
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,255,255));
                    player.setIsInPowderSnow(true);
                    player.setTicksFrozen(player.getTicksRequiredToFreeze());
                    if(entity.tickCount % (40*slow) == 0 && random.nextInt(3) == 2)
                        play(player,SoundEvents.SNOW_GOLEM_HURT);
                    if(entity.getPersistentData().getLong("VPBlizzard") > System.currentTimeMillis() && entity.tickCount % 20 == 0){
                        player.invulnerableTime = 0;
                        player.hurt(entity.damageSources().freeze(),attack*10);
                        player.getPersistentData().putLong("VPForbidden",System.currentTimeMillis()+1000);
                    }
                }
                if(entity.tickCount % (2*20*slow) == 0 && type == 5){
                    if(random.nextDouble() < 0.5) {
                        if(!player.isCreative() && player.getAbilities().flying) {
                            clearEffects(player,true);
                            if(random.nextDouble() < 0.5)
                                play(player,SoundRegistry.WIND1.get());
                            else play(player,SoundRegistry.WIND2.get());
                            player.getAbilities().mayfly = false;
                            player.getAbilities().flying = false;
                            player.onUpdateAbilities();
                            if (player instanceof ServerPlayer serverPlayer)
                                PacketHandler.sendToClient(new PlayerFlyPacket(2), serverPlayer);
                            player.invulnerableTime = 0;
                            player.hurt(entity.damageSources().fall(), attack * 8);
                        }
                    } else {
                        if(pos != null) {
                            clearEffects(player,true);
                            play(player,SoundRegistry.WIND3.get());
                            suckToPos(player, pos, 10);
                        }
                    }
                    if(random.nextDouble() < 0.3){
                        if(random.nextDouble() < 0.5)
                            play(player,SoundRegistry.WIND1.get());
                        else play(player,SoundRegistry.WIND2.get());
                        liftEntity(player,8);
                        lift = true;
                    }
                }
                if(entity.tickCount % (1*20*slow) == 0 && type == 6){
                    if(position != null && livingEntity.distanceToSqr(position.getCenter()) <= 5){
                        lightList.add(livingEntity);
                    }
                    entity.getPersistentData().putLong("VPBossL",System.currentTimeMillis()+random.nextInt(10)*1000);
                    if(player.getCommandSenderWorld() instanceof ServerLevel serverLevel1) {
                        serverLevel = serverLevel1;
                        if(random.nextDouble() < 0.2){
                            lightList.add(livingEntity);
                            spawnLightning(serverLevel, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                        }
                    }
                }
                if(entity.tickCount % (10*20*slow) == 0 && random.nextDouble() < 0.5 && type == 6){
                    entity.teleportTo(player.getX(),player.getY(),player.getZ());
                }
                if(entity.getPersistentData().getLong("VPBlizzard") > System.currentTimeMillis() && entity.tickCount % 20 == 0){
                    spawnSphere(player,ParticleTypes.SNOWFLAKE,20,4,2);
                    spawnSphere(player,ParticleTypes.SNOWFLAKE,20,2,1);
                    play(entity,SoundEvents.POWDER_SNOW_HIT);
                }
            }
            if(entity.tickCount % (2*slow) == 0 && entity.getPersistentData().getLong("VPBossL") > System.currentTimeMillis() && serverLevel != null){
                if(position != null)
                    spawnLightning(serverLevel, position.getX(), position.getY(), position.getZ());
                if(!lightList.isEmpty()){
                    for(LivingEntity living: lightList){
                        if(living == entity)
                            continue;
                        living.invulnerableTime = 0;
                        living.hurt(entity.damageSources().lightningBolt(),attack*0.5f);
                        stealShields(living,entity,15,true);
                    }
                    entity.getPersistentData().putFloat("VPAcsHeal",entity.getPersistentData().getFloat("VPAcsHeal")+lightList.size());
                }
            }
            if(lift)
                liftEntity(entity,8);
        }
    }

    public static void spawnCircleParticles(Entity entity, int particleCount, SimpleParticleType type, double radius,double height) {
        Vec3 entityPosition = entity.position();
        Random random = new Random();
        for (int i = 0; i < particleCount; i++) {
            double angle = 2 * Math.PI * random.nextDouble();
            double xOffset = Math.cos(angle) * radius;
            double zOffset = Math.sin(angle) * radius;

            double x = entityPosition.x + xOffset;
            double y = entityPosition.y + height;
            double z = entityPosition.z + zOffset;
            double numba = random.nextDouble();
            double velocityX = xOffset * 0.1;
            double velocityY = numba * 0.1;
            double velocityZ = zOffset * 0.1;

            spawnParticle(entity,type, x, y, z, velocityX, velocityY, velocityZ);
        }
    }

    public static void spawnAura(Entity entity, int particleCount, SimpleParticleType type, double radius) {
        Vec3 entityPosition = entity.position();
        Random random = new Random();
        double speed = 1;
        if (type == ParticleTypes.ELECTRIC_SPARK || type == ParticleTypes.MYCELIUM) {
            speed = 4;
            particleCount *= 2;
        }
        radius += entity.getBbWidth();
        for (int i = 0; i < particleCount; i++) {
            double angle = 2 * Math.PI * random.nextDouble();
            double xOffset = Math.cos(angle) * radius;
            double zOffset = Math.sin(angle) * radius;
            double x = entityPosition.x + xOffset;
            double y = entityPosition.y + 0.5;
            double z = entityPosition.z + zOffset;
            if (type == ParticleTypes.DRIPPING_OBSIDIAN_TEAR)
                y += 6;
            double numba = random.nextDouble()*speed;
            double velocityX = xOffset * 0.01;
            double velocityY = numba * 0.1;
            double velocityZ = zOffset * 0.01;

            spawnParticle(entity,type, x, y, z, velocityX*numba, velocityY+1, velocityZ*numba);
        }
    }

    public static void spawnSphere(Entity entity,ParticleOptions type, int count, float radius, float speed) {
        Vec3 entityPos = entity.position();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            double theta = 2 * Math.PI * random.nextDouble();
            double phi = Math.acos(2 * random.nextDouble() - 1);

            double xOffset = radius * Math.sin(phi) * Math.cos(theta);
            double yOffset = radius * Math.sin(phi) * Math.sin(theta);
            double zOffset = radius * Math.cos(phi);

            double xSpeed = xOffset * speed;
            double ySpeed = yOffset * speed;
            double zSpeed = zOffset * speed;
            spawnParticle(entity,type,entityPos.x + xOffset,
                    entityPos.y + yOffset + entity.getBbHeight() / 2,
                    entityPos.z + zOffset,
                    xSpeed,
                    ySpeed,
                    zSpeed);
        }
    }

    public static void nightmareAura(int type, LivingEntity entity){
        SimpleParticleType particleType = null;
        Random random = new Random();
        double radius = 2;
        switch (type){
            case 1: {
                particleType = ParticleTypes.FLAME;
                break;
            }
            case 2:{
                particleType = ParticleTypes.GLOW_SQUID_INK;
                break;
            }
            case 3:{
                particleType = ParticleTypes.ENCHANT;
                break;
            }
            case 4:{
                particleType = ParticleTypes.SNOWFLAKE;
                break;
            }
            case 5:{
                particleType = ParticleTypes.CLOUD;
                break;
            }
            case 6:{
                particleType = ParticleTypes.ELECTRIC_SPARK;
                break;
            }
            case 7:{
                if(random.nextDouble() < 0.5)
                    particleType = ParticleTypes.SOUL_FIRE_FLAME;
                else particleType = ParticleTypes.SOUL;
                break;
            }
        }
        spawnAura(entity,30,particleType,radius);
    }

    public static void setLuck(Player player){
        player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
            int luck = cap.getPearls();
            if(hasVestige(ModItems.CHAOS.get(),player))
                luck += 7;
            player.getPersistentData().putInt("VPPearlsLuck",luck);
        });
    }

    public static int getLuck(Player player){
        return player.getPersistentData().getInt("VPPearlsLuck");
    }

    public static double getChance(double chance,Player player){
        double multiplier = 1.2d;
        double newChance = chance + Math.log1p(getLuck(player)*multiplier / 100.0) * (1 - chance);
        if(chance >= 100)
            newChance = chance;
        return Math.min(newChance, chance * 3);
    }

    public static void spawnGuardianParticle(LivingEntity livingEntity,LivingEntity target){
        double d5 = 0.5;
        double d0 = target.getX() - livingEntity.getX();
        double d1 = target.getY(0.5D) - livingEntity.getEyeY();
        double d2 = target.getZ() - livingEntity.getZ();
        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
        d0 /= d3;
        d1 /= d3;
        d2 /= d3;
        double d4 = livingEntity.getRandom().nextDouble();

        while(d4 < d3) {
            d4 += 1.8D - d5 + livingEntity.getRandom().nextDouble() * (1.7D - d5);
            livingEntity.level().addParticle(ParticleTypes.BUBBLE, livingEntity.getX() + d0 * d4, livingEntity.getEyeY() + d1 * d4, livingEntity.getZ() + d2 * d4, 0.0D, 0.0D, 0.0D);
        }
    }

    public static boolean isHalloween() {
        LocalDate today = LocalDate.now();
        return today.getMonthValue() == 10 && today.getDayOfMonth() == 31;
    }

    public static boolean isEmpoweredMob(LivingEntity livingEntity){
        return (livingEntity.getPersistentData().getBoolean("VPEmpowered"));
    }

    public static boolean curseVestige(ItemStack stack, int curse,Player player){
        if(stack.getOrCreateTag().getInt("VPCursed") <= 0){
            if(Math.random() < 0.05) {
                curse = 6;
                Vestige.increaseStars(stack,player);
            }
            stack.getOrCreateTag().putInt("VPCursed",curse);
            return true;
        }
        return false;
    }

    public static int getVestigeCurse(ItemStack stack){
        return stack.getOrCreateTag().getInt("VPCursed");
    }

    public static boolean isEnchantable(ItemStack stack){
        if(stack.getItem() instanceof ArmorItem || stack.getItem() instanceof TieredItem || stack.getItem() instanceof BowItem){
            return true;
        }
        return false;
    }

    public static void dropStack(ItemStack stack,LivingEntity livingEntity){
        ItemEntity itemEntity = new ItemEntity(livingEntity.getCommandSenderWorld(), livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), stack);
        livingEntity.getCommandSenderWorld().addFreshEntity(itemEntity);
        SoundEvent sound = null;
        SimpleParticleType particle = null;
        if(stack.getItem() instanceof CorruptFragment) {
            sound = SoundRegistry.DROP_CORRUPT1.get();
            particle = ParticleTypes.CRIMSON_SPORE;
        }
        else if(stack.getItem() instanceof CorruptItem) {
            sound = SoundRegistry.DROP_CORRUPT2.get();
            particle = ParticleTypes.FLAME;
        }
        else if(stack.getItem() instanceof ChaosOrb) {
            sound = SoundRegistry.DROP_CHAOS.get();
            particle = ParticleTypes.WAX_ON;
        }
        else if(stack.getItem() instanceof CelestialMirror) {
            sound = SoundRegistry.DROP_MIRROR.get();
            particle = ParticleTypes.GLOW_SQUID_INK;
        }
        if(sound != null){
            VPUtil.play(livingEntity, sound);
            VPUtil.spawnSphere(livingEntity,particle,40,2,0.1f);
        }
    }

    public static boolean hasCurse(Player player, int curse){
        List<ItemStack> list = getVestigeList(player);
        for(ItemStack stack: list){
            if(getVestigeCurse(stack) == curse)
                return true;
        }
        return false;
    }

    public static float getCurseMultiplier(Player player, int curse){
        List<ItemStack> list = getVestigeList(player);
        for(ItemStack stack: list){
            if(getVestigeCurse(stack) == curse && stack.getItem() instanceof Vestige vestige) {
                if(curse > 0) {
                    float multiplier = 0;
                    if(curse == 1) {
                        multiplier = -0.9f; //maxhp*
                        if(vestige.isStellar(stack))
                            multiplier = -0.75f;
                        if(vestige.isDoubleStellar(stack))
                            multiplier = -0.45f;
                        if(vestige.isTripleStellar(stack))
                            multiplier = -0.35f;
                    }
                    else if (curse == 2){
                        multiplier = 80; //bonus-
                        if(vestige.isStellar(stack))
                            multiplier = 65;
                        if(vestige.isDoubleStellar(stack))
                            multiplier = 50;
                        if(vestige.isTripleStellar(stack))
                            multiplier = 40;
                    }
                    else if(curse == 3){
                        multiplier = 0.03f; //shields-shields*s
                        if(vestige.isStellar(stack))
                            multiplier = 0.02f;
                        if(vestige.isDoubleStellar(stack))
                            multiplier = 0.01f;
                        if(vestige.isTripleStellar(stack))
                            multiplier = 0.005f;
                    }
                    else if(curse == 4){
                        multiplier = 1.4f; //damage*
                        if(vestige.isStellar(stack))
                            multiplier = 1.7f;
                        if(vestige.isDoubleStellar(stack))
                            multiplier = 2.1f;
                        if(vestige.isTripleStellar(stack))
                            multiplier = 2.6f;
                    }
                    else if(curse == 5){
                        multiplier = 1.5f;  //enemy shields - your add shield*
                        if(vestige.isStellar(stack))
                            multiplier = 2.25f;
                        if(vestige.isDoubleStellar(stack))
                            multiplier = 3.5f;
                        if(vestige.isTripleStellar(stack))
                            multiplier = 4.5f;
                    }
                    else if(curse == 6) {
                        multiplier = 0.6f;
                        if (vestige.isDoubleStellar(stack))
                            multiplier = 0.5f;
                        if (vestige.isTripleStellar(stack))
                            multiplier = 0.4f;
                    }
                    return multiplier;
                }
            }
        }
        return 0;
    }

    public static int scaleDown(int number, int cap) {
        if (number <= cap) {
            return number;
        }
        double scaled = cap + Math.log((number - cap) + 1);
        return (int) Math.round(scaled);
    }

    public static float scaleDown(float number, float cap) {
        if (number <= cap) {
            return number;
        }
        return (float) (cap + Math.log((number - cap) + 1));
    }

    public static double scaleDown(double number, double cap) {
        if (number <= cap) {
            return number;
        }
        System.out.println(number);
        return cap + Math.log((number - cap) + 1);
    }


    public static void bindEntity(LivingEntity entity, long time){
        antiTp(entity,time);
        if(entity instanceof Player player){
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                cap.setBindTime(System.currentTimeMillis() + time);
                cap.setBindX(entity.getX());
                cap.setBindY(entity.getY());
                cap.setBindZ(entity.getZ());
            });
        } else {
            entity.getPersistentData().putDouble("VPDevourerX", entity.getX());
            entity.getPersistentData().putDouble("VPDevourerY", entity.getY());
            entity.getPersistentData().putDouble("VPDevourerZ", entity.getZ());
        }
    }

    public static void antiTp(LivingEntity entity, long time){
        entity.getPersistentData().putLong("VPAntiTP", System.currentTimeMillis() + time);
        syncEntity(entity);
        if(entity instanceof Player player){
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                cap.setAntiTp(System.currentTimeMillis() + time);
            });
        }
    }

    public static void antiResurrect(LivingEntity entity, long time){
        entity.getPersistentData().putLong("VPDeath", System.currentTimeMillis()+time);
        syncEntity(entity);
        if(entity instanceof Player player){
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                cap.setDeathTime(System.currentTimeMillis() + time);
            });
        }
    }

    public static boolean canResurrect(Entity entity){
        if(entity.getPersistentData().getLong("VPDeath") > System.currentTimeMillis()) {
            return false;
        }
        AtomicBoolean resurrect = new AtomicBoolean(true);
        if(entity instanceof Player player){
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                if(cap.getDeathTime() > System.currentTimeMillis())
                    resurrect.set(false);
            });
        }
        return resurrect.get();
    }

    public static boolean canTeleport(Entity entity){
        if(entity.getPersistentData().getLong("VPAntiTP") > System.currentTimeMillis() || VPUtil.isEvent(entity)) {
            return false;
        }
        AtomicBoolean teleport = new AtomicBoolean(true);
        if(entity instanceof Player player){
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                if(cap.getAntiTp() > System.currentTimeMillis()) {
                    teleport.set(false);
                }
            });
        }
        return teleport.get();
    }

    public static void bindTick(LivingEntity livingEntity){
        if(livingEntity instanceof Player player){
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                if(cap.getBindTime() > System.currentTimeMillis()) {
                    BlockPos pos = new BlockPos((int) cap.getBindX(),(int)cap.getBindY(),(int)cap.getBindZ());
                    VPUtil.suckToPos(player,pos,1);
                    if(player instanceof ServerPlayer serverPlayer)
                        PacketHandler.sendToClient(new PlayerFlyPacket(301),serverPlayer);
                }
            });
        } else if(livingEntity.getPersistentData().getDouble("VPDevourerX") != 0){
            if(livingEntity.getPersistentData().getLong("VPAntiTP") < System.currentTimeMillis()){
                livingEntity.getPersistentData().putDouble("VPDevourerX", 0);
                livingEntity.getPersistentData().putDouble("VPDevourerY", 0);
                livingEntity.getPersistentData().putDouble("VPDevourerZ", 0);
                return;
            }
            BlockPos pos = new BlockPos((int) livingEntity.getPersistentData().getDouble("VPDevourerX"),(int)livingEntity.getPersistentData().getDouble("VPDevourerY"),(int)livingEntity.getPersistentData().getDouble("VPDevourerZ"));
            VPUtil.suckToPos(livingEntity,pos,1);
        }
    }

    public static String addMysteryLoot(String base, String id, String rarity) {
        int index = rarityToIndex(rarity);
        String[] segments = base.split(">");
        if (segments.length != 4) {
            throw new IllegalArgumentException("Invalid base string format");
        }
        String[] parts = segments[index].split("<", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid segment format at index " + index);
        }
        String chance = parts[0];
        String dropList = parts[1].trim();
        String[] drops = dropList.split(",");
        for (String drop : drops) {
            if (drop.trim().equals(id)) {
                return base;
            }
        }
        if (dropList.isEmpty()) {
            dropList = id;
        } else {
            dropList = dropList + "," + id;
        }
        segments[index] = chance + "<" + dropList;
        StringBuilder sb = new StringBuilder();
        for (String seg : segments) {
            sb.append(seg).append(">");
        }
        return sb.toString();
    }

    public static String removeMysteryLoot(String base, String id, String rarity) {
        int index = rarityToIndex(rarity);
        String[] segments = base.split(">");
        if (segments.length != 4) {
            throw new IllegalArgumentException("Invalid base string format ");
        }
        String[] parts = segments[index].split("<", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid segment format at index " + index);
        }
        String chance = parts[0];
        String dropList = parts[1].trim();
        String[] drops = dropList.split(",");
        StringBuilder newDropList = new StringBuilder();
        for (String drop : drops) {
            String trimmed = drop.trim();
            if (!trimmed.equals(id)) {
                if (newDropList.length() > 0) {
                    newDropList.append(",");
                }
                newDropList.append(trimmed);
            }
        }
        segments[index] = chance + "<" + newDropList.toString();
        StringBuilder sb = new StringBuilder();
        for (String seg : segments) {
            sb.append(seg).append(">");
        }
        return sb.toString();
    }

    private static int rarityToIndex(String rarity) {
        switch (rarity.toLowerCase()) {
            case "common": return 0;
            case "rare": return 1;
            case "mythic": return 2;
            case "legendary": return 3;
            default: throw new IllegalArgumentException("Unknown rarity: " + rarity);
        }
    }

    public static boolean strictOptimization(){
        if(ConfigHandler.COMMON_SPEC.isLoaded())
            return ConfigHandler.COMMON.strictOptimization.get();
        return false;
    }

    public static HashMap<UUID,String> osMap = new HashMap<>();

    public static String getOs(Player player){
        return osMap.get(player.getUUID());
    }
}
