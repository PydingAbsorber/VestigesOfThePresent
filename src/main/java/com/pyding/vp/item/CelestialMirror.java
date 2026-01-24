package com.pyding.vp.item;

import com.pyding.vp.item.vestiges.SoulBlighter;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.UUID;

public class CelestialMirror extends Item{
    public CelestialMirror() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(player.getCommandSenderWorld().isClientSide() || hand == InteractionHand.OFF_HAND)
            return super.use(level, player, hand);
        if(!player.isCreative()) {
            if (!VPUtil.hasUUID(player.getMainHandItem(),"VPMirror")) {
                player.getMainHandItem().split(1);
                ConfigHandler.dupersList.set(ConfigHandler.dupersList.get() + "Name:" + player.getName().getString() + " UUID:" + player.getUUID() + ",");
                if(player.getServer() != null)
                    player.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Player " + player.getName().getString() + " tried to dupe Celestial Mirror by clearing its NBT or got it illegal!"),false);
                return super.use(level, player, hand);
            }
            if(!ConfigHandler.mirrorUUIDList.get().toString().isEmpty()) for (String element : ConfigHandler.mirrorUUIDList.get().toString().split(",")) {
                if (VPUtil.getNbtU(player.getMainHandItem(),"VPMirror").compareTo(UUID.fromString(element)) == 0) {
                    player.getMainHandItem().split(1);
                    ConfigHandler.dupersList.set(ConfigHandler.dupersList.get() + "Name:" + player.getName().getString() + " UUID:" + player.getUUID() + ",");
                    if(player.getServer() != null) {
                        VPUtil.deadInside(player);
                        player.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Player " + player.getName().getString() + " duped Celestial Mirror!"), false);
                    }
                    return super.use(level, player, hand);
                }
            }
        }
        if(!VPUtil.getNbtB(player.getOffhandItem(),"VPMirrored") && !dupyPoPopochkam(player.getOffhandItem(),player)){
            ItemStack itemStack = player.getOffhandItem();
            VPUtil.setNbt(itemStack,"VPMirrored",true);
            ItemStack clone = itemStack.copy();
            VPUtil.giveStack(clone,player);
            ConfigHandler.mirrorUUIDList.set(ConfigHandler.mirrorUUIDList.get()+VPUtil.getNbtU(player.getMainHandItem(),"VPMirror")+",");
            player.getMainHandItem().split(1);
            player.getPersistentData().putBoolean("VPBlockHand",true);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level p_41405_, Entity entity, int p_41407_, boolean p_41408_) {
        if(entity instanceof Player player) {
            if (!VPUtil.hasUUID(stack,"VPMirror") && player.isCreative()){
                UUID uuid = UUID.randomUUID();
                VPUtil.setNbt(stack,"VPMirror",uuid);
            }
            else if (!player.isCreative()){
                if (!VPUtil.hasUUID(stack,"VPMirror")) {
                    stack.split(1);
                    ConfigHandler.dupersList.set(ConfigHandler.dupersList.get() + "Name:" + player.getName().getString() + " UUID:" + player.getUUID() + ",");
                    if(player.getServer() != null)
                        player.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Player " + player.getName().getString() + " tried to dupe Celestial Mirror by clearing its NBT or got it illegal!"),false);
                }
                else if(!ConfigHandler.mirrorUUIDList.get().toString().isEmpty()) for(String element: ConfigHandler.mirrorUUIDList.get().toString().split(",")){
                    if(VPUtil.getNbtU(stack,"VPMirror").compareTo(UUID.fromString(element)) == 0){
                        stack.split(1);
                        ConfigHandler.dupersList.set(ConfigHandler.dupersList.get()+"Name:"+player.getName().getString()+" UUID:"+player.getUUID()+",");
                        if(player.getServer() != null) {
                            VPUtil.deadInside(player);
                            player.getServer().getPlayerList().broadcastSystemMessage(Component.literal("Player " + player.getName() + " duped Celestial Mirror!"), false);
                        }
                    }
                }
            }
        }
        super.inventoryTick(stack, p_41405_, entity, p_41407_, p_41408_);
    }

    public static boolean dupyPoPopochkam(ItemStack stack, Player player){
        if (stack.getItem() instanceof CelestialMirror || (stack.getItem() instanceof SoulBlighter && VPUtil.getTag(stack).contains("entityData"))){
            player.sendSystemMessage(Component.literal("Oh you thought you smart?"));
            return true;
        }
        for(String neDupy: ConfigHandler.cloneWhiteList.get().toString().split(",")){
            if(stack.getDescriptionId().contains(neDupy)) {
                return false;
            }
        }
        for(String dupy: ConfigHandler.cloneBlackList.get().toString().split(",")){
            if(stack.getDescriptionId().contains(dupy)) {
                player.sendSystemMessage(Component.literal("This item is in black list because it may contain dupes. You can change it in config."));
                return true;
            }
        }
        /*if(hasContainersOrInventory(stack.getClass())) {
            player.sendSystemMessage(Component.literal("This item cannot be cloned because it has containers or inventory. If it's not please write an issue on GitHub."));
            return true;
        }*/
        if(!stack.getItem().canFitInsideContainerItems() || ( VPUtil.getTag(stack).contains("Items"))) {
            player.sendSystemMessage(Component.literal("Nah bro, this item is definitely has smth dirty. I wont even allow you to configure it."));
            return true;
        }
        return false;
    }

    public static boolean hasContainersOrInventory(Class clazz){
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName().toLowerCase();
            if (fieldName.contains("store") || (fieldName.contains("container") && !fieldName.contains("canfitinsidecontaineritems")) || (fieldName.contains("inventory") && !fieldName.equals("inventorytick"))) {
                return true;
            }
        }
        for (Method method : clazz.getDeclaredMethods()) {
            String methodName = method.getName().toLowerCase();
            if ((methodName.contains("container") && !methodName.contains("canfitinsidecontaineritems")) || (methodName.contains("inventory") && !methodName.equals("inventorytick") && !methodName.contains("inventoryslot"))) {
                return true;
            }

            for (Parameter parameter : method.getParameters()) {
                String paramName = parameter.getName().toLowerCase();
                if (paramName.contains("container") || (paramName.contains("inventory") && !paramName.contains("inventoryslot"))) {
                    return true;
                }
            }
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            components.add(Component.translatable("vp.celestial_mirror.desc1").withStyle(ChatFormatting.GRAY));
            components.add(Component.translatable("vp.celestial_mirror.desc2").withStyle(ChatFormatting.GRAY));
        }
        else {
            components.add(Component.translatable("vp.press").append(Component.literal("SHIFT").withStyle(ChatFormatting.DARK_AQUA).append(Component.translatable("vp.shift"))));
        }
        super.appendHoverText(stack, context, components, flag);
    }
}
