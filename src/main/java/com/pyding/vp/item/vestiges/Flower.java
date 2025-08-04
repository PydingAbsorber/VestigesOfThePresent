package com.pyding.vp.item.vestiges;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

public class Flower extends Vestige{
    public Flower(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(16, ChatFormatting.DARK_GREEN, 2, 10, 1, 30, 5, 30, hasDamage, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        float healRes = 0;
        for(LivingEntity entity: VPUtil.getCreaturesAndPlayersAround(player,30,30,30)){
            healRes += VPUtil.missingHealth(entity)/10;
            if(isStellar(stack))
                entity.getPersistentData().putLong("VPFlowerStellar",System.currentTimeMillis()+1000);
        }
        player.getPersistentData().putFloat("VPHealResFlower",-healRes);
        player.getPersistentData().putFloat("VPShieldBonusFlower",healRes*10);
        if(isStellar(stack))
            player.getPersistentData().putLong("VPFlowerStellar",System.currentTimeMillis()+1000);
        super.curioTick(slotContext, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.HEAL2.get());
        player.getPersistentData().putLong("VPFlowerSpecial",System.currentTimeMillis()+seconds);
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.MAGIC2.get());
        float damage = 0;
        for(ItemStack stack2: VPUtil.getAllEquipment(player)){
            if(stack2.isDamaged())
                damage += stack2.getDamageValue();
        }
        damage *= ConfigHandler.COMMON.flowerShield.get();
        for(LivingEntity entity: VPUtil.getCreaturesAndPlayersAround(player,30,30,30)){
            if(entity instanceof Player && !VPUtil.isFriendlyFireBetween(player,entity) && !VPUtil.isProtectedFromHit(player,entity) && entity != player)
                continue;
            VPUtil.addShield(entity, damage, false);
            VPUtil.spawnParticles(player, ParticleTypes.FALLING_HONEY, entity.getX(), entity.getY() + 2, entity.getZ(), 8, 0, 0.5, 0);
        }
        super.doUltimate(seconds, player, level, stack);
    }
}
