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

import java.util.Random;

public class Flower extends Vestige{
    public Flower(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(16, ChatFormatting.DARK_GREEN, 2, 10, 1, 35, 666, 30, hasDamage, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if(isSpecialActive(stack)) {
            float healRes = 0;
            for (LivingEntity entity : VPUtil.getCreaturesAndPlayersAround(player, 30, 30, 30)) {
                healRes += VPUtil.missingHealth(entity) / 10;
                if (isStellar(stack) && isUltimateActive(stack))
                    entity.getPersistentData().putLong("VPFlowerStellar", System.currentTimeMillis() + 100);
            }
            player.getPersistentData().putFloat("VPHealResFlower", -healRes);
            player.getPersistentData().putFloat("VPShieldBonusFlower", healRes * 10);
        } else if(player.getPersistentData().getFloat("VPHealResFlower") != 0 || player.getPersistentData().getFloat("VPShieldBonusFlower") != 0){
            player.getPersistentData().putFloat("VPHealResFlower", 0);
            player.getPersistentData().putFloat("VPShieldBonusFlower", 0);
        }
        if(isStellar(stack) && isUltimateActive(stack))
            player.getPersistentData().putLong("VPFlowerStellar",System.currentTimeMillis()+100);
        super.curioTick(slotContext, stack);
    }

    @Override
    public int setSpecialActive(long seconds, Player player, ItemStack stack) {
        if(isSpecialActive(stack) && !player.getCommandSenderWorld().isClientSide) {
            if(currentChargeSpecial(stack) > 0) {
                if(!player.getCommandSenderWorld().isClientSide) {
                    setTime(1,stack);
                    setSpecialActive(true,stack);
                    setCdSpecialActive(cdSpecialActive(stack)+specialCd(stack),stack);     //time until cd recharges in seconds*tps
                    Random random = new Random();
                    if(!(VPUtil.getSet(player) == 3 && random.nextDouble() < VPUtil.getChance(0.3,player)) || !(VPUtil.getSet(player) == 6 && random.nextDouble() < VPUtil.getChance(0.5,player)) || random.nextDouble() < VPUtil.getChance(player.getPersistentData().getFloat("VPDepth")/10,player))
                        setCurrentChargeSpecial(currentChargeSpecial(stack) - 1, stack);
                    if(damageType == null)
                        init(stack);
                    this.doSpecial(seconds, player, player.getCommandSenderWorld(), stack);
                    if(VPUtil.hasCurse(player,3))
                        player.getPersistentData().putLong("VPForbidden",System.currentTimeMillis()+3000);
                } else this.localSpecial(player);
            }
            return 0;
        }
        return super.setSpecialActive(seconds, player, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.HEAL2.get());
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
