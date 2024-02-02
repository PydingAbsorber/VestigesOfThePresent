package com.pyding.vp.util;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.pyding.vp.capability.PlayerCapabilityVP;
import com.pyding.vp.item.artifacts.Midas;
import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.network.packets.SendPlayerCapaToClient;
import com.pyding.vp.network.packets.SendPlayerNbtToClient;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.InterModComms;
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

    public static float missingHealth(Player player){
        return (1 - (player.getHealth() / player.getMaxHealth())) * 100;
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

    public static float getAttack(Player player){
        //float attack = (float) player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        /*if(player.getMainHandItem().getItem() instanceof TieredItem tieredItem){
            attack += tieredItem.getTier().getAttackDamageBonus();
        }*/
        return (float) player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
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
        percent /= 100;
        entity.hurt(source,getAttack(player)*percent);
        ItemStack stack = player.getMainHandItem();
        stack.hurtAndBreak(1,entity,consumer -> {
            consumer.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
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

    public static int compareStats(LivingEntity owner, LivingEntity victim, boolean self){
        int stats = 0;
        AttributeMap map = owner.getAttributes();
        AttributeMap map2 = victim.getAttributes();
        if(map.getValue(Attributes.ATTACK_DAMAGE) > map2.getValue(Attributes.ATTACK_DAMAGE))
            stats++;
        if(map.getValue(Attributes.MOVEMENT_SPEED) > map2.getValue(Attributes.MOVEMENT_SPEED))
            stats++;
        if(map.getValue(Attributes.ATTACK_SPEED) > map2.getValue(Attributes.ATTACK_SPEED))
            stats++;
        if(map.getValue(Attributes.ARMOR) > map2.getValue(Attributes.ARMOR))
            stats++;
        if(map.getValue(Attributes.ARMOR_TOUGHNESS) > map2.getValue(Attributes.ARMOR_TOUGHNESS))
            stats++;
        if(map.getValue(Attributes.ATTACK_KNOCKBACK) > map2.getValue(Attributes.ATTACK_KNOCKBACK))
            stats++;
        if(map.getValue(Attributes.FLYING_SPEED) > map2.getValue(Attributes.FLYING_SPEED))
            stats++;
        if(map.getValue(Attributes.JUMP_STRENGTH) > map2.getValue(Attributes.JUMP_STRENGTH))
            stats++;
        if(map.getValue(Attributes.LUCK) > map2.getValue(Attributes.LUCK))
            stats++;
        if(map.getValue(Attributes.MAX_HEALTH) > map2.getValue(Attributes.MAX_HEALTH))
            stats++;
        if(map.getValue(Attributes.KNOCKBACK_RESISTANCE) > map2.getValue(Attributes.KNOCKBACK_RESISTANCE))
            stats++;
        if(self)
            return stats;
        else return 11-stats;
    }

    public static List<LivingEntity> getEntitiesAround(Player player,double x, double y, double z){
        return player.level.getEntitiesOfClass(LivingEntity.class, new AABB(player.getX()+x,player.getY()+y,player.getZ()+z,player.getX()-x,player.getY()-y,player.getZ()-z));
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

    public static DamageSource randomizeDamageType(DamageSource source){
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
            case 11:{
                source = source.setIsFire();
                break;
            }
            default:
                break;
        }
        return source;
    }

    public static void addShield(LivingEntity entity,float amount,boolean add){
        CompoundTag tag = entity.getPersistentData();
        float shieldBonus = (entity.getPersistentData().getFloat("VPShieldBonusDonut"));
        float shield;
        if(!add)
            shield = amount;
        else shield = tag.getFloat("VPShield") + amount;
        shield = shield*(1 + shieldBonus/100);
        if(!tag.getBoolean("VPAntiShield"))
            tag.putFloat("VPShield",shield);
        if(entity instanceof ServerPlayer player) {
            PacketHandler.sendToClient(new SendPlayerNbtToClient(player.getUUID(), player.getPersistentData()),player);
        }
    }

    public static float getShield(LivingEntity entity){
        CompoundTag tag = entity.getPersistentData();
        return tag.getFloat("VPShield");
    }

    public static void deadInside(LivingEntity entity,Player player){
        entity.setHealth(0);
        entity.die(DamageSource.playerAttack(player));
        Multimap<Attribute, AttributeModifier> attributesDefault = HashMultimap.create();
        attributesDefault.put(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("b2f5e343-120a-49f1-91b3-2909de24374e"), "vp:dead_inside", 0, AttributeModifier.Operation.MULTIPLY_TOTAL));
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
        List<LivingEntity> entities = new ArrayList<>();

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
        float healBonus = Math.max(-99,tag.getFloat("HealResMask")+tag.getFloat("VPHealResFlower")+tag.getFloat("VPHealBonusDonut")+tag.getFloat("VPHealBonusDonutPassive"));
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
        if (entity instanceof Player || player.level.isClientSide) {
            return;
        }

        ResourceLocation lootTableLocation = entity.getLootTable();
        ServerLevel world = (ServerLevel) player.level;
        LootTables lootTableManager = world.getServer().getLootTables();
        LootTable lootTable = lootTableManager.get(lootTableLocation);

        LootContext lootContext = (new LootContext.Builder(world))
                .withRandom(world.random)
                .withOptionalParameter(LootContextParams.THIS_ENTITY, entity)
                //.withOptionalParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                .create(LootContextParamSets.ENTITY);

        lootTable.getRandomItems(lootContext).forEach(stack -> {
            entity.spawnAtLocation(stack);
        });
    }


}
