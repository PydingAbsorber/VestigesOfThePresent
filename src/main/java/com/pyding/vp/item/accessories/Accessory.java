package com.pyding.vp.item.accessories;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.item.vestiges.Vestige;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Accessory extends Item implements ICurioItem {
    public Accessory() {
        super(new Properties().stacksTo(1));
    }

    public Accessory(Properties properties) {
        super(properties);
    }

    public void init(ItemStack stack,Player player){
        Random random = new Random();
        int type = random.nextInt(5)+1;
        VPUtil.setNbt(stack,"VPLvl",0);
        VPUtil.setNbt(stack,"VPType",type);
        switch (type) {
            case 1 -> VPUtil.setNbt(stack,"VPStat", (float) (VPUtil.getChance(random.nextDouble(),player)*3 + 1));
            case 2 -> VPUtil.setNbt(stack,"VPStat", (float) (VPUtil.getChance(random.nextDouble(),player)*6 + 2));
            case 3 -> VPUtil.setNbt(stack,"VPStat", (float) (VPUtil.getChance(random.nextDouble(),player)*15 + 5));
            case 4 -> VPUtil.setNbt(stack,"VPStat", (float) (VPUtil.getChance(random.nextDouble(),player)*9 + 3));
            case 5 -> VPUtil.setNbt(stack,"VPStat", (float) (VPUtil.getChance(random.nextDouble(),player)*12 + 4));
            default -> {
            }
        }
    }

    public boolean lvlUp(ItemStack stack,Player player){
        double baseChance = 0.9;
        int lvl = getLvl(stack);
        if(lvl > 0) {
            for (int i = 0; i < lvl; i++)
                baseChance *= 0.8;
        }
        Random random = new Random();
        if(random.nextDouble() < VPUtil.getChance(baseChance,player)){
            VPUtil.play(player, SoundRegistry.RUNE1.get());
            VPUtil.setNbt(stack,"VPLvl",lvl+1);
            switch (getType(stack)) {
                case 1 -> VPUtil.setNbt(stack,"VPStat", getStatAmount(stack)+(float) (VPUtil.getChance(random.nextDouble(),player)*3 + 1));
                case 2 -> VPUtil.setNbt(stack,"VPStat", getStatAmount(stack)+(float) (VPUtil.getChance(random.nextDouble(),player)*6 + 2));
                case 3 -> VPUtil.setNbt(stack,"VPStat", getStatAmount(stack)+(float) (VPUtil.getChance(random.nextDouble(),player)*15 + 5));
                case 4 -> VPUtil.setNbt(stack,"VPStat", getStatAmount(stack)+(float) (VPUtil.getChance(random.nextDouble(),player)*9 + 3));
                case 5 -> VPUtil.setNbt(stack,"VPStat", getStatAmount(stack)+(float) (VPUtil.getChance(random.nextDouble(),player)*12 + 4));
                default -> {
                }
            }
            return true;
        } else VPUtil.play(player,SoundRegistry.RUNE2.get());
        return false;
    }
    public int getLvl(ItemStack stack){
        return VPUtil.getTag(stack).getInt("VPLvl");
    }

    public static int getType(ItemStack stack){
        return VPUtil.getTag(stack).getInt("VPType");
    }

    public float getStatAmount(ItemStack stack){
        return VPUtil.getTag(stack).getFloat("VPStat");
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        List<ItemStack> list = VPUtil.getVestigeList(player);
        for(ItemStack itemStack: list){
            if(itemStack.getItem() instanceof Vestige vestige){
                vestige.applyBonus(stack,player);
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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
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
        } else if (Screen.hasAltDown()){
            components.add(Component.translatable("vp.acs.upgrade"));
        }
        else {
            components.add(Component.translatable("vp.acs.lvl").append(Component.literal(" "+getLvl(stack)).withStyle(ChatFormatting.LIGHT_PURPLE)).withStyle(ChatFormatting.DARK_PURPLE));
            if(getType(stack) > 0) {
                if(getType(stack) < 3)
                    components.add(Component.translatable("vp.acs.stats").withStyle(ChatFormatting.DARK_PURPLE).append(Component.literal(" +" + String.format("%.1f", getStatAmount(stack))).append(Component.translatable("vp.acs.stats." + getType(stack))).withStyle(ChatFormatting.LIGHT_PURPLE)));
                else components.add(Component.translatable("vp.acs.stats").withStyle(ChatFormatting.DARK_PURPLE).append(Component.literal(" +"+String.format("%.1f", getStatAmount(stack)) + "%").append(Component.translatable("vp.acs.stats."+getType(stack))).withStyle(ChatFormatting.LIGHT_PURPLE)));
            }components.add(Component.translatable("vp.acs.shift"));
            components.add(Component.translatable("vp.acs.ctrl"));
            components.add(Component.translatable("vp.acs.alt"));
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
            player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(Attributes.MAX_HEALTH, 0, AttributeModifier.Operation.ADD_VALUE, "vp_accessory.health"));
            player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(Attributes.ATTACK_DAMAGE,0, AttributeModifier.Operation.ADD_VALUE, "vp_accessory.attack"));
            if(player.isAlive() && player.getHealth() > player.getMaxHealth())
                player.setHealth(player.getMaxHealth());
            player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(Attributes.MAX_HEALTH, health, AttributeModifier.Operation.ADD_VALUE, "vp_accessory.health"));
            player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(Attributes.ATTACK_DAMAGE, attack, AttributeModifier.Operation.ADD_VALUE, "vp_accessory.attack"));
            player.getPersistentData().putFloat("VPAcsDamage",damage);
            player.getPersistentData().putFloat("VPAcsHeal",heal);
            player.getPersistentData().putFloat("VPAcsShields",shields);
        } else {
            VPUtil.removeAttributeModifier(player,Attributes.MAX_HEALTH, "vp_accessory.health");
            VPUtil.removeAttributeModifier(player,Attributes.ATTACK_DAMAGE, "vp_accessory.attack");
        }
    }
}