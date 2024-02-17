package com.pyding.vp.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.network.packets.SendEntityNbtToClient;
import com.pyding.vp.network.packets.SendPlayerNbtToClient;
import com.pyding.vp.network.packets.SoundPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class VPUtil {
    public static void registerCurioType(final String identifier, final int slots, final boolean isHidden, @Nullable final ResourceLocation icon) {
        final SlotTypeMessage.Builder message = new SlotTypeMessage.Builder(identifier);

        message.size(slots);

        if (isHidden) {
            message.hide();
        }

        if (icon != null) {
            message.icon(icon);
        }

        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> message.build());

    }

    public static long coolDown(){
        return 8*60*60*1000;
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
        return player.level.getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+radius,player.getY()+radius,player.getZ()+radius,player.getX()-radius,player.getY()-radius,player.getZ()-radius));
    }
    public static List<LivingEntity> getEntities(Player player,double radius,boolean self){
        if(self)
            return player.level.getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+radius,player.getY()+radius,player.getZ()+radius,player.getX()-radius,player.getY()-radius,player.getZ()-radius));
        else {
            List<LivingEntity> list = new ArrayList<>();
            for(LivingEntity entity: player.level.getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+radius,player.getY()+radius,player.getZ()+radius,player.getX()-radius,player.getY()-radius,player.getZ()-radius))){
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
        for(EntityType type: ForgeRegistries.ENTITY_TYPES.getValues()){
            entities.add(type);
        }
    }

    public static List biomes = new ArrayList<>();
    public static List<Item> items = new ArrayList<>();
    public static void initBiomes(){
        for (Biome biome : ForgeRegistries.BIOMES.getValues()){
            biomes.add(biome.toString());
        }
    }
    public static List getBiomes(){
        return biomes;
    }
    private static final Pattern PATTERN = Pattern.compile("minecraft:(\\w+)");
    private static Set<String> biomeNames = new HashSet<>();

    public static void addBiome(String name) {
        Matcher matcher = PATTERN.matcher(name);
        while (matcher.find()) {
            String biomeName = matcher.group(1);
            if (!biomeNames.contains(biomeName) && !biomeName.contains("worldgen")) {
                biomeNames.add(biomeName); //.substring("minecraft:".length())
            }
        }
    }
    public static List getBiomesFound(String list){
        List<String> biomesList = new ArrayList<>(Arrays.asList(list.split(",")));
        for (String name: biomesList){
            addBiome(name);
        }
        return Arrays.asList(biomeNames.toArray());
    }
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
    public static List getMonsterLeft(String list){
        List<String> mobsList = new ArrayList<>(Arrays.asList(list.split(",")));
        List<String> allList = new ArrayList<>();
        for(EntityType<?> type: getEntitiesListOfType(MobCategory.MONSTER)){
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

    public static void dealDamage(LivingEntity entity,Player player, DamageSource source, float percent){
        if(isFriendlyFireBetween(entity,player))
            return;
        entity.invulnerableTime = 0;
        percent /= 100;
        ItemStack stack = player.getMainHandItem();
        boolean hasDurability = stack.isDamageableItem() && stack.getDamageValue()+1 < stack.getMaxDamage();
        if(hasDurability) {
            stack.hurtAndBreak(1, entity, consumer -> {
                consumer.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        entity.hurt(source,getAttack(player,hasDurability)*percent);
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

    public static List<String> getDamageDoLeft(String list){
        List<String> damageList = new ArrayList<>(Arrays.asList(list.split(",")));
        List<String> allList = new ArrayList<>();
        for(String name: damageSubtypes().split(",")){
            allList.add(name);
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

    public static int getChaosTime(){
        return 15*60*1000;
    }

    public static boolean hasVestige(Item item,Player player){
        if(!(item instanceof Vestige))
            return false;
        ICuriosHelper api = CuriosApi.getCuriosHelper();
        List result = api.findCurios(player, (stackInSlot) -> {
            if(stackInSlot.getItem() == item) {
                return true;
            }
            return false;
        });
        return result.size() > 0;
    }

    public static boolean hasStellarVestige(Item item,Player player){
        if(!(item instanceof Vestige))
            return false;
        ICuriosHelper api = CuriosApi.getCuriosHelper();
        List result = api.findCurios(player, (stackInSlot) -> {
            if(stackInSlot.getItem() instanceof Vestige vestige && vestige == item && vestige.isStellar(stackInSlot)) {
                return true;
            }
            return false;
        });
        return result.size() > 0;
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
        return player.level.getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z));
    }
    public static List getEntitiesAroundOfType(Class entityClass,Player player,double x, double y, double z,boolean self){
        if(self)
            return player.level.getEntitiesOfClass(entityClass, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z));
        else {
            List list = new ArrayList<>();
            for(Object entity: player.level.getEntitiesOfClass(entityClass, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z))){
                if(entity != player)
                    list.add(entity);
            }
            return list;
        }
    }
    public static List<LivingEntity> getEntitiesAround(Player player,double x, double y, double z,boolean self){
        if(self)
            return player.level.getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z));
        else {
            List<LivingEntity> list = new ArrayList<>();
            for(LivingEntity entity: player.level.getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z))){
                if(entity != player)
                    list.add(entity);
            }
            return list;
        }
    }

    public static List<LivingEntity> getEntitiesAround(LivingEntity player,double x, double y, double z,boolean self){
        if(self)
            return player.level.getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z));
        else {
            List<LivingEntity> list = new ArrayList<>();
            for(LivingEntity entity: player.level.getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z))){
                if(entity != player)
                    list.add(entity);
            }
            return list;
        }
    }

    public static void adaptiveDamageHurt(LivingEntity entity, Player player, boolean adopted,float percent){
        String damage = entity.getPersistentData().getString("VPCrownDamage");
        DamageSource source = DamageSource.playerAttack(player);
        String adept = "";
        switch (damage){
            case "bypassArmor":{
                source.damageHelmet();
                adept = "damageHelmet";
                break;
            }
            case "damageHelmet":{
                source.bypassEnchantments();
                adept = "bypassEnchantments";
                break;
            }
            case "bypassEnchantments": {
                source.setExplosion();
                adept = "explosion";
                break;
            }
            case "explosion": {
                source.bypassInvul();
                adept = "bypassInvul";
                break;
            }
            case "bypassInvul": {
                source.bypassMagic();
                adept = "bypassMagic";
                break;
            }
            case "bypassMagic": {
                source.setIsFall();
                adept = "fall";
                break;
            }
            case "fall": {
                source.setMagic();
                adept = "magic";
                break;
            }
            case "magic": {
                source.setNoAggro();
                adept = "noAggro";
                break;
            }
            case "noAggro": {
                source.setProjectile();
                adept = "projectile";
                break;
            }
            case "projectile": {
                source.setIsFire();
                adept = "fire";
                break;
            }
            case "fire": {
                source.bypassArmor();
                adept = "bypassArmor";
                break;
            }
            default:
                break;
        }
        dealDamage(entity,player,source,percent);
        if(adopted) {
            entity.getPersistentData().putString("VPCrownDamage", adept);
            entity.getPersistentData().putBoolean("VPCrownDR",false);
        }
    }

    public static DamageSource randomizeDamageType(){
        DamageSource source = new DamageSource("ChaosCore");
        Random random = new Random();
        int numba = random.nextInt(11);
        switch (numba){
            case 1:{
                source.damageHelmet();
                break;
            }
            case 2:{
                source.bypassEnchantments();
                break;
            }
            case 3: {
                source = source.setExplosion();
                break;
            }
            case 4: {
                source = source.bypassInvul();
                break;
            }
            case 5: {
                source = source.bypassMagic();
                break;
            }
            case 6: {
                source = source.setIsFall();
                break;
            }
            case 7: {
                source = source.setMagic();
                break;
            }
            case 8: {
                source = source.setNoAggro();
                break;
            }
            case 9: {
                source = source.setProjectile();
                break;
            }
            case 10: {
                source = source.bypassArmor();
                break;
            }
            default: {
                source = source.setIsFire();
                break;
            }
        }
        return source;
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
        if(!tag.getBoolean("VPAntiShield"))
            tag.putFloat("VPShield",shield);
        if(entity instanceof ServerPlayer player) {
            PacketHandler.sendToClient(new SendPlayerNbtToClient(player.getUUID(), player.getPersistentData()),player);
        } else {
            if(!entity.level.isClientSide)
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
        entity.die(DamageSource.playerAttack(player));
    }
    public static void deadInside(LivingEntity entity){
        entity.getPersistentData().putLong("VPDeath",System.currentTimeMillis()+40000);
        entity.setHealth(0);
        entity.die(DamageSource.GENERIC);
    }

    public static List<LivingEntity> ray(Player player, float range, int maxDist, boolean stopWhenFound) {
        Vector3 target = Vector3.fromEntityCenter(player);
        List<LivingEntity> entities = new ArrayList<>();

        for (int distance = 1; distance < maxDist; ++distance) {
            target = target.add(new Vector3(player.getLookAngle()).multiply(distance)).add(0.0, 0.5, 0.0);
            List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class, new AABB(target.x - range, target.y - range, target.z - range, target.x + range, target.y + range, target.z + range));
            list.removeIf(entity -> entity == player || !player.hasLineOfSight(entity));
            entities.addAll(list);

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
        return new BlockPos(target.x,target.y,target.z);
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
        if(entity.getMaxHealth() >= 200 || entity instanceof WitherBoss || entity instanceof EnderDragon || entity instanceof Warden)
            return true;
        return false;
    }

    public static void dropEntityLoot(LivingEntity entity, Player player) {
        if (entity instanceof Player) {
            return;
        }
        if(player.level instanceof ServerLevel serverLevel) {
            ResourceLocation lootTableLocation = entity.getLootTable();
            LootTable lootTable = serverLevel.getServer().getLootTables().get(lootTableLocation);
            LootContext.Builder builder = new LootContext.Builder(serverLevel)
                    .withParameter(LootContextParams.ORIGIN, entity.position())
                    .withParameter(LootContextParams.THIS_ENTITY, entity)
                    .withParameter(LootContextParams.DAMAGE_SOURCE, DamageSource.playerAttack(player))
                    .withRandom(serverLevel.random);

            List<ItemStack> list = lootTable.getRandomItems(builder.create(LootContextParamSets.ENTITY));
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
        ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(name));
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
    public static void clearEffects(LivingEntity entity, boolean isBeneficial){
        Iterator<MobEffectInstance> iterator = entity.getActiveEffects().iterator();
        while (iterator.hasNext()) {
            MobEffectInstance effectInstance = iterator.next();
            MobEffect effect = effectInstance.getEffect();
            if (effect.isBeneficial() == isBeneficial) {
                iterator.remove();
            }
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

    public static LivingEntity getRandomEntityNear(Player player,boolean playerCount){
        for (LivingEntity entity: getEntities(player,10,false)) {
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
        String damage = entity.getPersistentData().getString("VPPrismDamage");

        if(!damage.isEmpty()) {
            if(damage.equals("bypassArmor") && event.getSource().isBypassArmor()){
                event.setAmount(event.getAmount()*2);
            }
            else if(damage.equals("damageHelmet") && event.getSource().isDamageHelmet()){
                event.setAmount(event.getAmount()*2);
            }
            else if(damage.equals("bypassEnchantments") && event.getSource().isBypassEnchantments()){
                event.setAmount(event.getAmount()*2);
            }
            else if(damage.equals("explosion") && event.getSource().isExplosion()){
                event.setAmount(event.getAmount()*2);
            }
            else if(damage.equals("bypassInvul") && event.getSource().isBypassInvul()){
                event.setAmount(event.getAmount()*2);
            }
            else if(damage.equals("bypassMagic") && event.getSource().isBypassMagic()){
                event.setAmount(event.getAmount()*2);
            }
            else if(damage.equals("fall") && event.getSource().isFall()){
                event.setAmount(event.getAmount()*2);
            }
            else if(damage.equals("magic") && event.getSource().isMagic()){
                event.setAmount(event.getAmount()*2);
            }
            else if(damage.equals("noAggro") && event.getSource().isNoAggro()){
                event.setAmount(event.getAmount()*2);
            }
            else if(damage.equals("projectile") && event.getSource().isProjectile()){
                event.setAmount(event.getAmount()*2);
            }
            else {
                event.setAmount(0);
                event.setCanceled(true);
            }
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
        for(LivingEntity entity: player.level.getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z))){
            if(!(entity instanceof Animal) && entity != player)
                list.add(entity);
        }
        return list;
    }

    public static List<LivingEntity> getCreaturesAround(Player player,double x, double y, double z){
        List<LivingEntity> list = new ArrayList<>();
        for(LivingEntity entity: player.level.getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z))){
            if(entity instanceof Animal)
                list.add(entity);
        }
        return list;
    }

    public static boolean isDamagePhysical(DamageSource source){
        return !source.isFire() && !source.isMagic() && !source.isBypassInvul() && !source.isBypassEnchantments() && !source.isBypassMagic();
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

    public static void spawnParticles(ServerPlayer player, ParticleOptions particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed, boolean force) {
        if(player.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(player, particle, force, x, y, z, count, deltaX, deltaY, deltaZ, speed);
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
        if (hitResult.getType() == BlockHitResult.Type.BLOCK) {
            finalBlockPos = hitResult.getBlockPos();
        } else {
            finalBlockPos = new BlockPos(targetPosition);
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
        if(entity.level.isClientSide)
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
        if(!tag.getBoolean("VPAntiShield"))
            tag.putFloat("VPOverShield",shield);
        if(entity instanceof ServerPlayer player) {
            PacketHandler.sendToClient(new SendPlayerNbtToClient(player.getUUID(), player.getPersistentData()),player);
        } else {
            if(!entity.level.isClientSide)
                PacketHandler.sendToClients(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SendEntityNbtToClient(entity.getPersistentData(),entity.getId()));
        }
        if(tag.getFloat("VPOverShieldMax") < shield) {
            tag.putFloat("VPOverShieldMax", shield);
        }
    }


}
