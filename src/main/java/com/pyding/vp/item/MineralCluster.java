package com.pyding.vp.item;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MineralCluster extends Item {
    public MineralCluster(Properties p_41383_) {
        super(p_41383_);
    }

    public MineralCluster() {
        super(new Item.Properties().stacksTo(64));
    }

    public static List<Item> drops = new ArrayList<>();

    public static List<Item> getDrops(){
        if(drops.isEmpty()){
            for(Item item: VPUtil.getItems()){
                for(String name: ConfigHandler.COMMON.mineralCluster.get().toString().split(",")){
                    for(String blackList: ConfigHandler.COMMON.mineralClusterBlacklist.get().toString().split(",")) {
                        if (item.getDescriptionId().contains(name) && !item.getDescriptionId().contains(blackList))
                            drops.add(item);
                    }
                }
                for(String name: VPUtil.getOres()){
                    if(item.getDescriptionId().equals(name))
                        drops.add(item);
                }
            }
        }
        return drops;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        VPUtil.play(player, SoundRegistry.OPEN.get());
        if(!level.isClientSide) {
            ItemStack stack = player.getItemInHand(p_41434_);
            stack.split(1);
            Random random = new Random();
            VPUtil.giveStack(new ItemStack(getDrops().get(random.nextInt(getDrops().size()))), player);
        }
        return super.use(level, player, p_41434_);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        Player player = Minecraft.getInstance().player;
        components.add(Component.translatable("vp.cluster.desc1").withStyle(ChatFormatting.BLUE));
        components.add(Component.translatable("vp.cluster.desc2").withStyle(ChatFormatting.DARK_GREEN));
    }
}
