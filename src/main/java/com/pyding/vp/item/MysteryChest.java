package com.pyding.vp.item;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.client.MysteryChestScreen;
import com.pyding.vp.client.MysteryDropScreen;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MysteryChest extends Item {
    public MysteryChest(Properties p_41383_) {
        super(p_41383_);
    }

    public MysteryChest() {
        super(new Properties().stacksTo(64));
    }

    public static List<ItemStack> commonItems = new ArrayList<>();
    public static List<ItemStack> rareItems = new ArrayList<>();
    public static List<ItemStack> mythicItems = new ArrayList<>();
    public static List<ItemStack> legendaryItems = new ArrayList<>();
    public static double commonChance = 0;
    public static double rareChance = 0;
    public static double mythicChance = 0;
    public static double legendaryChance = 0;
    public static HashMap<Integer,List<String>> cacheLists = new HashMap<>();

    public static void init(){
        String list = ConfigHandler.COMMON.lootDrops.get().toString();
        String[] segments = list.split(">");
        commonChance = Double.parseDouble(segments[0].split("<")[0]);
        rareChance = Double.parseDouble(segments[1].split("<")[0]);
        mythicChance = Double.parseDouble(segments[2].split("<")[0]);
        legendaryChance = Double.parseDouble(segments[3].split("<")[0]);
        commonItems.clear();
        rareItems.clear();
        mythicItems.clear();
        legendaryItems.clear();
        for(Item item: VPUtil.getItems()){
            boolean stop = false;
            boolean strictOptimization = VPUtil.strictOptimization();
            for(String id: segments[0].split("<")[1].split(",")){
                List<String> cringe = processString(id);
                if(item.getDescriptionId().equals(cringe.get(0))) {
                    commonItems.add(new ItemStack(item, Math.max(1,Integer.parseInt(cringe.get(1)))));
                    stop = true;
                    break;
                }
            }
            if(stop && strictOptimization)
                continue;
            for(String id: segments[1].split("<")[1].split(",")){
                List<String> cringe = processString(id);
                if(item.getDescriptionId().equals(cringe.get(0))) {
                    rareItems.add(new ItemStack(item, Math.max(1,Integer.parseInt(cringe.get(1)))));
                    stop = true;
                    break;
                }
            }
            if(stop && strictOptimization)
                continue;
            for(String id: segments[2].split("<")[1].split(",")){
                List<String> cringe = processString(id);
                if(item.getDescriptionId().equals(cringe.get(0))) {
                    mythicItems.add(new ItemStack(item, Math.max(1,Integer.parseInt(cringe.get(1)))));
                    stop = true;
                    break;
                }
            }
            if(stop && strictOptimization)
                continue;
            for(String id: segments[3].split("<")[1].split(",")){
                List<String> cringe = processString(id);
                if(item.getDescriptionId().equals(cringe.get(0))) {
                    legendaryItems.add(new ItemStack(item, Math.max(1,Integer.parseInt(cringe.get(1)))));
                    break;
                }
            }
        }
    }

    public static List<String> processString(String input) {
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\d+)$");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            list.add(input.substring(0, matcher.start(1)));
            list.add(matcher.group(1));
        } else {
            list.add(input);
            list.add("1");
        }
        return list;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        if(p_41434_ != InteractionHand.MAIN_HAND)
            return super.use(level, player, p_41434_);
        if(level.isClientSide)
            Minecraft.getInstance().setScreen(new MysteryChestScreen());
        return super.use(level, player, p_41434_);
    }

    public static Map<ItemStack,String> getRandomDrop(){
        for(ItemStack itemStack: MysteryChest.commonItems){
            if(itemStack.getCount() == 0 || itemStack.is(Items.AIR)) {
                init();
                break;
            }
        }
        for(ItemStack itemStack: MysteryChest.rareItems){
            if(itemStack.getCount() == 0 || itemStack.is(Items.AIR)) {
                init();
                break;
            }
        }
        for(ItemStack itemStack: MysteryChest.mythicItems){
            if(itemStack.getCount() == 0 || itemStack.is(Items.AIR)) {
                init();
                break;
            }
        }
        for(ItemStack itemStack: MysteryChest.legendaryItems){
            if(itemStack.getCount() == 0 || itemStack.is(Items.AIR)) {
                init();
                break;
            }
        }
        Map<ItemStack,String> map = new HashMap<>();
        Random random = new Random();
        double randomNumber = random.nextDouble();
        if(randomNumber <= legendaryChance)
            map.put(legendaryItems.get(random.nextInt(legendaryItems.size())),"legendary");
        else if(randomNumber <= mythicChance)
            map.put(mythicItems.get(random.nextInt(mythicItems.size())),"mythic");
        else if(randomNumber <= rareChance)
            map.put(rareItems.get(random.nextInt(rareItems.size())),"rare");
        else if(randomNumber <= commonChance)
            map.put(commonItems.get(random.nextInt(commonItems.size())),"common");
        else map.put(new ItemStack(Items.AIR),"common");
        return map;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(!stack.hasTag())
            stack.getOrCreateTag().putInt("VPOpen",0);
        if (Screen.hasShiftDown()){
            for(ItemStack itemStack: MysteryChest.commonItems){
                if(itemStack.getCount() == 0 || itemStack.is(Items.AIR)) {
                    init();
                    break;
                }
            }
            for(ItemStack itemStack: MysteryChest.rareItems){
                if(itemStack.getCount() == 0 || itemStack.is(Items.AIR)) {
                    init();
                    break;
                }
            }
            for(ItemStack itemStack: MysteryChest.mythicItems){
                if(itemStack.getCount() == 0 || itemStack.is(Items.AIR)) {
                    init();
                    break;
                }
            }
            for(ItemStack itemStack: MysteryChest.legendaryItems){
                if(itemStack.getCount() == 0 || itemStack.is(Items.AIR)) {
                    init();
                    break;
                }
            }
            Minecraft.getInstance().setScreen(new MysteryDropScreen());
        } else if (Screen.hasControlDown()) {
            components.add(Component.translatable("vp.mystery.desc3",ConfigHandler.COMMON.mysteryChestAdvancementChance.get()*100+"%",ConfigHandler.COMMON.mysteryChestChallengeChance.get()*100+"%").withStyle(ChatFormatting.GRAY));
        } else {
            components.add(Component.translatable("vp.mystery.desc").withStyle(ChatFormatting.GRAY));
            components.add(Component.translatable("vp.mystery.desc2").withStyle(ChatFormatting.GRAY));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void registerChick() {
        ItemProperties.register(this, new ResourceLocation(VestigesOfThePresent.MODID, "open"),
                (stack, world, entity, number) -> stack.getOrCreateTag().getInt("VPOpen"));
    }
}
