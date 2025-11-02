package com.pyding.vp.item.vestiges;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.PlayerFlyPacket;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Random;

public class Anemoculus extends Vestige{
    public Anemoculus(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(1, ChatFormatting.DARK_AQUA, 2, 30, 5, 70, 1, 20, hasDamage, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        int dudes = 0;
        if(!isUltimateActive(stack)) {
            VPUtil.spawnParticles(player, ParticleTypes.CLOUD,8,25,0,0.5,0,3,false);
            for (LivingEntity entity : VPUtil.getEntities(player, 8,true)) {
                if(!VPUtil.isProtectedFromHit(player,entity)) {
                    VPUtil.liftEntity(entity, VPUtil.commonPower);
                    dudes++;
                }
            }
        }
        else {
            Random random = new Random();
            if(random.nextDouble() < 0.5)
                VPUtil.play(player,SoundRegistry.WIND1.get());
            else VPUtil.play(player,SoundRegistry.WIND2.get());
            for(LivingEntity entity: VPUtil.getEntities(player,16,false)){
                if(!VPUtil.isProtectedFromHit(player,entity)) {
                    VPUtil.suckEntity(entity, player, 2, true);
                    dudes++;
                }
            }
            VPUtil.spawnParticles(player, ParticleTypes.CLOUD,8,1,0,0.5,0,3,false);
        }
        if(dudes >= 3)
            addRadiance(25,stack,player);
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.WIND3.get());
        player.getAbilities().mayfly = true;
        player.getAbilities().flying = true;
        player.onUpdateAbilities();
        if(player instanceof ServerPlayer serverPlayer)
            PacketHandler.sendToClient(new PlayerFlyPacket(6),serverPlayer);
        VPUtil.spawnParticles(player, ParticleTypes.CAMPFIRE_COSY_SMOKE,3,1,0,0.1,0,1,false);
        super.doUltimate(seconds, player, level, stack);
    }

    @Override
    public void ultimateEnds(Player player, ItemStack stack) {
        if(player.isCreative())
            return;
        player.getAbilities().mayfly = false;
        player.getAbilities().flying = false;
        player.onUpdateAbilities();
        if(player instanceof ServerPlayer serverPlayer)
            PacketHandler.sendToClient(new PlayerFlyPacket(2),serverPlayer);
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 7 * 20));
        super.ultimateEnds(player, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        super.curioTick(slotContext, stack);
    }
}
