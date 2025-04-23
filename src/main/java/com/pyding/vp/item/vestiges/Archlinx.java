package com.pyding.vp.item.vestiges;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.event.EventHandler;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Random;

public class Archlinx extends Vestige{
    public Archlinx(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(25, ChatFormatting.BLUE, 2, 25, 1, 1200, 99999, 99999, true, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        if(new Random().nextDouble() < 0.5)
            VPUtil.play(player,SoundRegistry.ARROW_READY_1.get());
        else VPUtil.play(player,SoundRegistry.ARROW_READY_2.get());
        VPUtil.spawnParticles(player, ParticleTypes.SNOWFLAKE,3,1,0,0.1,0,1,false);
        player.getPersistentData().putInt("VPArchShots",5);
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.MAGIC_ARROW_1.get());
        VPUtil.spawnParticles(player, ParticleTypes.ENCHANTED_HIT,3,1,0,0.1,0,1,false);
        if(isUltimateActive(stack) && !player.getCommandSenderWorld().isClientSide){
            setTime(1,stack);
        }
        super.doUltimate(seconds, player, level, stack);
    }

    @Override
    public void ultimateEnds(Player player, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.MAGIC_ARROW_2.get());
        float damage = player.getPersistentData().getInt("VPArchdamage");
        player.getPersistentData().putFloat("VPArchdamage",0);
        super.ultimateEnds(player, stack);
    }

    @Override
    public void curioSucks(Player player, ItemStack stack) {
        removeModifiers(player);
        super.curioSucks(player, stack);
    }

    public static void removeModifiers(Player player){
        AttributeMap map = player.getAttributes();
        List<Attribute> list = VPUtil.attributeList();
        for(int i = 0; i < list.size(); i++){
            if(map.hasAttribute(list.get(i))) {
                player.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(player,list.get(i), EventHandler.archUUIDs.get(i),0, AttributeModifier.Operation.ADDITION, "vp:arch"+list.get(i).getDescriptionId()));
            }
        }
    }
}
