package com.pyding.vp.item;

import com.pyding.vp.client.VortexScreen;
import com.pyding.vp.entity.VortexEntity;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Vortex extends Item {
    public Vortex(Properties p_41383_) {
        super(p_41383_);
    }

    public Vortex() {
        super(new Properties().stacksTo(64));
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        if(player.getY() <= -69) {
            item.shrink(1);
            VortexEntity vortexEntity = new VortexEntity(player.getCommandSenderWorld(),player);
            vortexEntity.setPos(player.getX(),player.getY()-1,player.getZ());
            player.getCommandSenderWorld().addFreshEntity(vortexEntity);
            return false;
        }
        return super.onDroppedByPlayer(item, player);
    }

    public static int hold = 0;
    public static List<ItemStack> items = new ArrayList<>();

    public static List<ItemStack> getItems(){
        if(items.isEmpty())
            init();
        return items;
    }

    public static void init(){
        items.clear();
        for(Item item: VPUtil.items) {
            for (String element : ConfigHandler.repairObjects.get().toString().split(",")) {
                if (item.getDescriptionId().contains(element) && !isBlackListed(element)) {
                    items.add(new ItemStack(item));
                    break;
                }
            }
        }
    }

    public static boolean isBlackListed(String element){
        for(String name : ConfigHandler.repairBlackList.get().toString().split(",")){
            if (name.equals(element)) {
                return true;
            }
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        components.add(Component.translatable("vp.vortex").withStyle(ChatFormatting.GRAY));
        components.add(Component.translatable("vp.vortex.shift").withStyle(ChatFormatting.GRAY));
        if (Screen.hasShiftDown()) {
            hold += 2;
            components.add(Component.literal(hold+"/"+100).withStyle(ChatFormatting.GRAY));
            if(hold >= 100) {
                init();
                Minecraft.getInstance().execute(() -> Minecraft.getInstance().setScreen(new VortexScreen()));
                hold = 0;
            }
        }
        if(hold >0)
            hold--;
        super.appendHoverText(stack, context, components, flag);
    }
}
