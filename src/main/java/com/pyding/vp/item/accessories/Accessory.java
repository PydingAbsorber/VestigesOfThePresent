package com.pyding.vp.item.accessories;

import com.pyding.vp.item.artifacts.Vestige;
import com.pyding.vp.util.VPUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class Accessory extends Item implements ICurioItem {
    public Accessory() {
        super(new Item.Properties().stacksTo(1));
    }

    public Accessory(Properties properties) {
        super(properties);
    }

    public int getType(ItemStack stack){
        return stack.getOrCreateTag().getInt("VPType");
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
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
    }
}