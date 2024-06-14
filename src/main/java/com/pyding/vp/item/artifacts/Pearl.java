package com.pyding.vp.item.artifacts;

import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Pearl extends Vestige{
    public Pearl(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(23, ChatFormatting.DARK_BLUE, 4, 40, 1, 60, 30, 30, false, stack);
    }

    boolean later = false;

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        if(player.fishing != null){
            FishingHook hook = player.fishing;
            if(hook.isOpenWaterFishing()){
                try {
                    Field timeUntilLured = FishingHook.class.getDeclaredField("timeUntilLured");
                    timeUntilLured.setAccessible(true);
                    timeUntilLured.setInt(hook, 100);
                    timeUntilLured = FishingHook.class.getDeclaredField("nibble");
                    timeUntilLured.setAccessible(true);
                    timeUntilLured.setInt(hook, 100);
                    player.setAirSupply(Math.max(0,player.getAirSupply()-(int)(player.getMaxAirSupply()*0.3)));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else later = true;
        } else later = true;
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void whileSpecial(Player player, ItemStack stack) {
        if(later){
            if(player.fishing != null){
                FishingHook hook = player.fishing;
                if(hook.isOpenWaterFishing()){
                    try {
                        Field timeUntilLured = FishingHook.class.getDeclaredField("timeUntilLured");
                        timeUntilLured.setAccessible(true);
                        timeUntilLured.setInt(hook, 100);
                        timeUntilLured = FishingHook.class.getDeclaredField("nibble");
                        timeUntilLured.setAccessible(true);
                        timeUntilLured.setInt(hook, 100);
                        player.setAirSupply(Math.max(0,player.getAirSupply()-(int)(player.getMaxAirSupply()*0.3)));
                        later = false;
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        super.whileSpecial(player, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        player.getPersistentData().putInt("VPLures",10);
        player.getPersistentData().putFloat("VPDepth", VPUtil.getWaterDepth(player));
        super.doUltimate(seconds, player, level, stack);
    }

    @Override
    public void ultimateRecharges(Player player, ItemStack stack) {
        player.getPersistentData().putInt("VPLures",0);
        super.ultimateRecharges(player, stack);
    }

    @Override
    public void ultimateEnds(Player player, ItemStack stack) {
        player.getPersistentData().putFloat("VPDepth",0);
        super.ultimateEnds(player, stack);
    }
}
