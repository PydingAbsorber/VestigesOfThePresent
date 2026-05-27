package com.pyding.vp.item.vestiges;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.item.NightmareShard;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

public class NightmareDevourer extends Vestige{
    public NightmareDevourer(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(666, ChatFormatting.DARK_RED, 4, 99999, 2, 100, 600, 10, hasDamage, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        if(!player.getCommandSenderWorld().isClientSide && player.tickCount % 1200 == 0){
            for(ItemStack item: player.getInventory().items){
                if(item.getItem() instanceof NightmareShard && stack.getCount() >= 16){
                    addRadiance(10,stack,player);
                    setCurrentChargeSpecial(currentChargeSpecial(stack)+1,stack);
                }
            }
        }
        super.curioTick(slotContext, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.DEVOURER_BIND.get());
        int souls = 0;
        for (LivingEntity entity : VPUtil.getEntitiesAround(player,20,20,20)) {
            if(!VPUtil.isProtectedFromHit(player,entity)) {
                int soulDevour = (int) (VPUtil.getMaxSoulIntegrity(entity)*0.05);
                VPUtil.modifySoulIntegrity(entity,player,-soulDevour);
                souls += soulDevour;
                VPUtil.spawnSphere(entity,ParticleTypes.SOUL,32,2,0.1f);
            }
        }
        player.getPersistentData().putInt("VPNDevour",player.getPersistentData().getInt("VPNDevour") + souls/10);
        player.getPersistentData().putLong("VPNDevourTime",System.currentTimeMillis()+specialMaxTime(stack)*1000);
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player,SoundRegistry.SOUL2.get());
        super.doUltimate(seconds, player, level, stack);
    }

    @Override
    public void whileUltimate(Player player, ItemStack stack) {
        int soul = 1;
        if(isStellar(stack))
            soul += (int) (VPUtil.damagePercentBonus(player,3)/4);
        for(LivingEntity entity: VPUtil.ray(player,3,30,false)){
            VPUtil.modifySoulIntegrity(entity,player,-soul);
            VPUtil.spawnCircleParticles(entity,8,ParticleTypes.SOUL,1,0.1f);
        }
        super.whileUltimate(player, stack);
    }

    @Override
    public int setUltimateActive(long seconds, Player player, ItemStack stack) {
        if(isDoubleStellar(stack))
            seconds += 1000L *(VPUtil.getMaxSoulIntegrity(player)/200);
        return super.setUltimateActive(seconds, player, stack);
    }
}
