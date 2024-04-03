package com.pyding.vp.item.accessories;

import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Accessory extends Item implements ICurioItem {
    public Accessory() {
        super(new Item.Properties().stacksTo(1));
    }

    public Accessory(Properties properties) {
        super(properties);
    }

    public void init(ItemStack stack){
        Random random = new Random();
        int type = random.nextInt(5)+1;
        stack.getOrCreateTag().putInt("VPLvl",0);
        stack.getOrCreateTag().putInt("VPType",type);
        switch (type) {
            case 1 -> stack.getOrCreateTag().putFloat("VPStat", (float) (Math.random()*3 + 1));
            case 2 -> stack.getOrCreateTag().putFloat("VPStat", (float) (Math.random()*6 + 2));
            case 3 -> stack.getOrCreateTag().putFloat("VPStat", (float) (Math.random()*15 + 5));
            case 4 -> stack.getOrCreateTag().putFloat("VPStat", (float) (Math.random()*9 + 3));
            case 5 -> stack.getOrCreateTag().putFloat("VPStat", (float) (Math.random()*12 + 4));
            default -> {
            }
        }
        /*switch (type) {
            case 1 -> stack.getOrCreateTag().putFloat("VPStat", random.nextInt(4) + 1);
            case 2 -> stack.getOrCreateTag().putFloat("VPStat", random.nextInt(7) + 2);
            case 3 -> stack.getOrCreateTag().putFloat("VPStat", random.nextInt(16) + 5);
            case 4 -> stack.getOrCreateTag().putFloat("VPStat", (float) (random.nextInt(4) + 1) / 100);
            case 5 -> stack.getOrCreateTag().putFloat("VPStat", (float) (random.nextInt(4) + 1) / 100);
            default -> {
            }
        }*/
    }

    public boolean lvlUp(ItemStack stack){
        double baseChance = 0.9;
        int lvl = getLvl(stack);
        if(lvl > 0) {
            for (int i = 0; i < lvl; i++)
                baseChance *= 0.8;
        }
        if(Math.random() < baseChance){
            stack.getOrCreateTag().putInt("VPLvl",lvl+1);
            switch (getType(stack)) {
                case 1 -> stack.getOrCreateTag().putFloat("VPStat", getStatAmount(stack)+(float) (Math.random()*3 + 1));
                case 2 -> stack.getOrCreateTag().putFloat("VPStat", getStatAmount(stack)+(float) (Math.random()*6 + 2));
                case 3 -> stack.getOrCreateTag().putFloat("VPStat", getStatAmount(stack)+(float) (Math.random()*15 + 5));
                case 4 -> stack.getOrCreateTag().putFloat("VPStat", getStatAmount(stack)+(float) (Math.random()*9 + 3));
                case 5 -> stack.getOrCreateTag().putFloat("VPStat", getStatAmount(stack)+(float) (Math.random()*12 + 4));
                default -> {
                }
            }
            return true;
        }
        return false;
    }
    public int getLvl(ItemStack stack){
        return stack.getOrCreateTag().getInt("VPLvl");
    }

    public static int getType(ItemStack stack){
        return stack.getOrCreateTag().getInt("VPType");
    }

    public float getStatAmount(ItemStack stack){
        return stack.getOrCreateTag().getFloat("VPStat");
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        List<ItemStack> list = VPUtil.getVestigeList(player);
        for(ItemStack itemStack: list){
            if(itemStack.getItem() instanceof Vestige vestige){
                vestige.curioSucks(player,itemStack);
            }
        }
        refreshStats(player);
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        List<ItemStack> list = VPUtil.getVestigeList(player);
        for(ItemStack itemStack: list){
            if(itemStack.getItem() instanceof Vestige vestige){
                vestige.curioSucks(player,itemStack);
            }
        }
        refreshStats(player);
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        Player player = Minecraft.getInstance().player;
        int set = VPUtil.getSet(player);
        if(Screen.hasShiftDown()){
            components.add(Component.translatable("vp.acs.types"));
        } else if (Screen.hasControlDown()){
            components.add(Component.translatable("vp.acs.sets").withStyle(ChatFormatting.DARK_PURPLE));
            for(int i = 1; i < 11; i++){
                if(set > 0 && set == i)
                    components.add(Component.translatable("vp.acs.set."+i).withStyle(ChatFormatting.GREEN));
                else components.add(Component.translatable("vp.acs.set."+i).withStyle(ChatFormatting.GRAY));
            }
        } else {
            components.add(Component.translatable("vp.acs.lvl").append(Component.literal(" "+getLvl(stack)).withStyle(ChatFormatting.LIGHT_PURPLE)).withStyle(ChatFormatting.DARK_PURPLE));
            if(getType(stack) > 0) {
                if(getType(stack) < 3)
                    components.add(Component.translatable("vp.acs.stats").withStyle(ChatFormatting.DARK_PURPLE).append(Component.literal(" +" + String.format("%.1f", getStatAmount(stack))).append(Component.translatable("vp.acs.stats." + getType(stack))).withStyle(ChatFormatting.LIGHT_PURPLE)));
                else components.add(Component.translatable("vp.acs.stats").withStyle(ChatFormatting.DARK_PURPLE).append(Component.literal(" +"+String.format("%.1f", getStatAmount(stack)) + "%").append(Component.translatable("vp.acs.stats."+getType(stack))).withStyle(ChatFormatting.LIGHT_PURPLE)));
            }components.add(Component.translatable("vp.acs.shift"));
            components.add(Component.translatable("vp.acs.ctrl"));
        }
    }

    public void refreshStats(Player player){
        List<ItemStack> accessories = VPUtil.getAccessoryList(player);
        if(!accessories.isEmpty()){
            float health = 0;
            float attack = 0;
            float damage = 0;
            float heal = 0;
            float shields = 0;
            for(ItemStack stackAcs: accessories){
                if(stackAcs.getItem() instanceof Accessory accessory){
                    if(getType(stackAcs) == 1)
                        health += accessory.getStatAmount(stackAcs);
                    if(getType(stackAcs) == 2)
                        attack += accessory.getStatAmount(stackAcs);
                    if(getType(stackAcs) == 3)
                        damage += accessory.getStatAmount(stackAcs);
                    if(getType(stackAcs) == 4)
                        heal += accessory.getStatAmount(stackAcs);
                    if(getType(stackAcs) == 5)
                        shields += accessory.getStatAmount(stackAcs);
                }
            }
            player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("d05228bf-b23d-4091-8e9c-4954688989fd"), 0, AttributeModifier.Operation.ADDITION, "vp_accessory:health"));
            player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ATTACK_DAMAGE, UUID.fromString("91595e31-3c5a-4f7d-8097-60a96e37a51c"), 0, AttributeModifier.Operation.ADDITION, "vp_accessory:attack"));
            player.setHealth(player.getMaxHealth());
            player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("d05228bf-b23d-4091-8e9c-4954688989fd"), health, AttributeModifier.Operation.ADDITION, "vp_accessory:health"));
            player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ATTACK_DAMAGE, UUID.fromString("91595e31-3c5a-4f7d-8097-60a96e37a51c"), attack, AttributeModifier.Operation.ADDITION, "vp_accessory:attack"));
            player.getPersistentData().putFloat("VPAcsDamage",damage);
            player.getPersistentData().putFloat("VPAcsHeal",heal);
            player.getPersistentData().putFloat("VPAcsShields",shields);
        } else {
            AttributeModifier modifier = player.getAttribute(Attributes.MAX_HEALTH).getModifier(UUID.fromString("d05228bf-b23d-4091-8e9c-4954688989fd"));
            if (modifier != null) {
                player.getAttribute(Attributes.MAX_HEALTH).removeModifier(modifier);
            }
            modifier = player.getAttribute(Attributes.ATTACK_DAMAGE).getModifier(UUID.fromString("91595e31-3c5a-4f7d-8097-60a96e37a51c"));
            if (modifier != null) {
                player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(modifier);
            }
            player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.MAX_HEALTH, UUID.fromString("d05228bf-b23d-4091-8e9c-4954688989fd"), 0, AttributeModifier.Operation.ADDITION, "vp_accessory:health"));
            player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ATTACK_DAMAGE, UUID.fromString("91595e31-3c5a-4f7d-8097-60a96e37a51c"), 0, AttributeModifier.Operation.ADDITION, "vp_accessory:attack"));
        }
    }
}