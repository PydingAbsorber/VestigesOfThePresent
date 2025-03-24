package com.pyding.vp.item;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.client.sounds.SoundRegistry;
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

public class MysteryBox extends Item {
    public MysteryBox(Properties p_41383_) {
        super(p_41383_);
    }

    public MysteryBox() {
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
            for(String id: segments[0].split("<")[1].split(",")){
                List<String> cringe = processString(id);
                if(item.getDescriptionId().equals(cringe.get(0)))
                    commonItems.add(new ItemStack(item, Integer.parseInt(cringe.get(1))));
            }
            for(String id: segments[1].split("<")[1].split(",")){
                List<String> cringe = processString(id);
                if(item.getDescriptionId().equals(cringe.get(0)))
                    rareItems.add(new ItemStack(item, Integer.parseInt(cringe.get(1))));
            }
            for(String id: segments[2].split("<")[1].split(",")){
                List<String> cringe = processString(id);
                if(item.getDescriptionId().equals(cringe.get(0)))
                    mythicItems.add(new ItemStack(item, Integer.parseInt(cringe.get(1))));
            }
            for(String id: segments[3].split("<")[1].split(",")){
                List<String> cringe = processString(id);
                if(item.getDescriptionId().equals(cringe.get(0)))
                    legendaryItems.add(new ItemStack(item, Integer.parseInt(cringe.get(1))));
            }
        }
        cacheLists.clear();
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
        VPUtil.play(player, SoundRegistry.OPEN.get());
        if(commonItems.isEmpty() && rareItems.isEmpty() && mythicItems.isEmpty() && legendaryItems.isEmpty())
            init();
        ItemStack stack = player.getItemInHand(p_41434_);
        stack.split(1);
        if(!level.isClientSide()) {
            Random random = new Random();
            ItemStack randomDrop;
            double randomNumber = random.nextDouble();
            if(randomNumber <= legendaryChance)
                randomDrop = legendaryItems.get(random.nextInt(legendaryItems.size()));
            else if(randomNumber <= mythicChance)
                randomDrop = mythicItems.get(random.nextInt(mythicItems.size()));
            else if(randomNumber <= rareChance)
                randomDrop = rareItems.get(random.nextInt(rareItems.size()));
            else if(randomNumber <= commonChance)
                randomDrop = commonItems.get(random.nextInt(commonItems.size()));
            else randomDrop = new ItemStack(Items.AIR);
            player.addItem(randomDrop);
        }
        return super.use(level, player, p_41434_);
    }

    public static HashMap<Integer,List<String>> cacheLists = new HashMap<>();

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (Screen.hasShiftDown()){
            if(cacheLists.isEmpty()){
                List<String> list = new ArrayList<>();
                for(ItemStack stacks: commonItems)
                    list.add(stacks.getDescriptionId());
                cacheLists.put(0,list);
                list = new ArrayList<>();
                for(ItemStack stacks: rareItems)
                    list.add(stacks.getDescriptionId());
                cacheLists.put(1,list);
                list = new ArrayList<>();
                for(ItemStack stacks: mythicItems)
                    list.add(stacks.getDescriptionId());
                cacheLists.put(2,list);
                list = new ArrayList<>();
                for(ItemStack stacks: legendaryItems)
                    list.add(stacks.getDescriptionId());
                cacheLists.put(3,list);
            }
            components.add((VPUtil.filterAndTranslate(cacheLists.get(0).toString(),ChatFormatting.GRAY)));
            components.add((VPUtil.filterAndTranslate(cacheLists.get(1).toString(),ChatFormatting.BLUE)));
            components.add((VPUtil.filterAndTranslate(cacheLists.get(2).toString(),ChatFormatting.LIGHT_PURPLE)));
            components.add((VPUtil.filterAndTranslate(cacheLists.get(3).toString(),ChatFormatting.RED)));
        } else if (Screen.hasControlDown()) {
            components.add(Component.translatable("vp.mystery.desc2").withStyle(ChatFormatting.GRAY));
        } else {
            components.add(Component.translatable("vp.mystery.desc").withStyle(ChatFormatting.GRAY));
            components.add(Component.translatable("vp.mystery.desc2").withStyle(ChatFormatting.GRAY));
        }
    }
}
