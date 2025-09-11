package com.pyding.vp.item.vestiges;

import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class Treasure extends Vestige{
    public Treasure(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(26, ChatFormatting.GOLD, 2, 60, 1, 50, 30, 10, hasDamage, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {

        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {

        super.doUltimate(seconds, player, level, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);
        if(slotContext.entity() instanceof Player player){
            if(player.getMainHandItem().getItem() instanceof PickaxeItem || player.getOffhandItem().getItem() instanceof PickaxeItem){
                float armor = VPUtil.scalePower(getOres(player)/20,26,player);
                float tough = VPUtil.scalePower(getMinerals(player)/32,26,player);
                player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ARMOR, UUID.fromString("e6fdfccb-e294-481c-bd65-f464a9982e3f"),armor, AttributeModifier.Operation.ADDITION,"vp.treasure.armor"));
                player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ARMOR_TOUGHNESS, UUID.fromString("c692ceea-e05b-441f-8c98-0ff7842fa89e"),tough, AttributeModifier.Operation.ADDITION,"vp.treasure.armor"));
            } else if(player.getAttributes().hasModifier(Attributes.ARMOR,UUID.fromString("e6fdfccb-e294-481c-bd65-f464a9982e3f"))){
                player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ARMOR, UUID.fromString("e6fdfccb-e294-481c-bd65-f464a9982e3f"),0, AttributeModifier.Operation.ADDITION,"vp.treasure.armor"));
                player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ARMOR_TOUGHNESS, UUID.fromString("c692ceea-e05b-441f-8c98-0ff7842fa89e"),0, AttributeModifier.Operation.ADDITION,"vp.treasure.armor"));
            }
        }
    }

    @Override
    public void curioSucks(Player player, ItemStack stack) {
        super.curioSucks(player, stack);
        if(player.getAttributes().hasModifier(Attributes.ARMOR,UUID.fromString("e6fdfccb-e294-481c-bd65-f464a9982e3f"))){
            player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ARMOR, UUID.fromString("e6fdfccb-e294-481c-bd65-f464a9982e3f"),0, AttributeModifier.Operation.ADDITION,"vp.treasure.armor"));
            player.getAttributes().removeAttributeModifiers(VPUtil.createAttributeMap(player, Attributes.ARMOR_TOUGHNESS, UUID.fromString("c692ceea-e05b-441f-8c98-0ff7842fa89e"),0, AttributeModifier.Operation.ADDITION,"vp.treasure.armor"));
        }
    }

    public static float getOres(Player player){
        float ores = 0;
        for(ItemStack stack: player.getInventory().items){
            if(VPUtil.getOres().contains(stack.getDescriptionId()))
                ores += stack.getCount();
        }
        return ores;
    }

    public static float getMinerals(Player player){
        float ores = 0;
        for(ItemStack stack: player.getInventory().items){
            for(String name: ConfigHandler.COMMON.mineralCluster.get().toString().split(",")){
                if(stack.getDescriptionId().equals(name))
                    ores += stack.getCount();
            }
        }
        return ores;
    }
}
