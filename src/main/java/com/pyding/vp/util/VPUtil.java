package com.pyding.vp.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.SlotTypeMessage;

import javax.annotation.Nullable;
import java.util.*;
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

    public static float getAttack(Player player){
        return (float) player.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
    }

    public static List<EntityType<?>> entities = new ArrayList<>();

    public static List<EntityType<?>> getEntitiesList(){
        return entities;
    }

    public static List<EntityType<?>> getEntitiesListOfType(MobCategory category){
        return entities.stream().filter(entityType -> entityType.getCategory() == category).collect(Collectors.toList());
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

    public static List getMonsterLeft(String list){
        List<String> mobsList = new ArrayList<>(Arrays.asList(list.split(",")));
        List<String> allList = new ArrayList<>();
        for(EntityType<?> type: getEntitiesListOfType(MobCategory.MONSTER)){
            allList.add(type.toString());
        }
        allList.removeAll(mobsList);
        List<String> filteredList = new ArrayList<>();
        for (String name: allList){
            filteredList.add(name.substring("entity.minecraft.".length()));
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
            filteredList.add(name.substring("entity.minecraft.".length()));
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
            String name = block.getDescriptionId();
            if(name.contains("flower")){
                flowers.add(block.getDescriptionId());
            } else {
                for(String element: vanillaFlowers){
                    if(name.contains(element))
                        flowers.add(block.getDescriptionId());
                }
            }
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
            filteredList.add(name.substring("block.minecraft.".length()));
        }
        return filteredList;
    }

    private static final List<String> vanillaFlowers = Arrays.asList(
            "poppy", "dandelion", "lily_of_the_valley", "blue_orchid", "allium",
            "azure_bluet", "red_tulip", "orange_tulip", "white_tulip", "pink_tulip",
            "oxeye_daisy", "cornflower", "wither_rose", "sunflower", "lilac", "rose_bush", "peony"
    );
}
