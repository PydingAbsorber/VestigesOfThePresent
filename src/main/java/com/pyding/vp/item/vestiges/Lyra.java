package com.pyding.vp.item.vestiges;

import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Lyra extends Vestige{
    public Lyra(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage, ItemStack stack) {
        super.dataInit(22, ChatFormatting.DARK_GREEN, 3, 40, 1, 120, 30, 10, false, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        if (player.getPersistentData().getLong("VPOrchestra") > System.currentTimeMillis())
            return;
        Random random = new Random();
        int effect = random.nextInt(8)+1;
        List<Integer> effects = new ArrayList<>();
        for(int i = 1;i < 9;i++) {
            if (VPUtil.hasLyra(player, i))
                effects.add(i);
        }
        if(!effects.isEmpty()){
            do {
                effect = random.nextInt(8) + 1;
            } while (effects.contains(effect));
        }
        if(player.getPersistentData().getLong("VPMusic") < System.currentTimeMillis()){
            switch (effect) {
                case 1:
                    VPUtil.play(player, SoundRegistry.LYRE1.get());
                    player.getPersistentData().putLong("VPMusic", System.currentTimeMillis() + 8 * 1000);
                    break;
                case 2:
                    VPUtil.play(player, SoundRegistry.LYRE2.get());
                    player.getPersistentData().putLong("VPMusic", System.currentTimeMillis() + 5 * 1000);
                    break;
                case 3:
                    VPUtil.play(player, SoundRegistry.LYRE3.get());
                    player.getPersistentData().putLong("VPMusic", System.currentTimeMillis() + 4 * 1000);
                    break;
                case 4:
                    VPUtil.play(player, SoundRegistry.LYRE4.get());
                    player.getPersistentData().putLong("VPMusic", System.currentTimeMillis() + 6 * 1000);
                    break;
                case 5:
                    VPUtil.play(player, SoundRegistry.LYRE5.get());
                    player.getPersistentData().putLong("VPMusic", System.currentTimeMillis() + 8 * 1000);
                    break;
                case 6:
                    VPUtil.play(player, SoundRegistry.LYRE6.get());
                    player.getPersistentData().putLong("VPMusic", System.currentTimeMillis() + 4 * 1000);
                    break;
                case 7:
                    VPUtil.play(player, SoundRegistry.LYRE7.get());
                    player.getPersistentData().putLong("VPMusic", System.currentTimeMillis() + 4 * 1000);
                    break;
                case 8:
                    VPUtil.play(player, SoundRegistry.LYRE8.get());
                    player.getPersistentData().putLong("VPMusic", System.currentTimeMillis() + 2 * 1000);
                    break;
            }
        }
        for(LivingEntity entity: VPUtil.getCreaturesAndPlayersAround(player,15,15,15)){
            if(entity instanceof Player && !VPUtil.isFriendlyFireBetween(player,entity) && !VPUtil.isProtectedFromHit(player,entity))
                continue;
            VPUtil.spawnAura(entity,20, ParticleTypes.NOTE,1);
            VPUtil.spawnCircleParticles(entity,20,ParticleTypes.NOTE,5,1);
            VPUtil.spawnSphere(entity,ParticleTypes.NOTE,40,3,0);
            entity.getPersistentData().putLong("VPLyra"+effect,System.currentTimeMillis()+seconds);
            if(effect == 1)
                entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity, Attributes.MAX_HEALTH, UUID.fromString("9548da03-e5f8-4cfd-be48-c4ae9b25d86d"),VPUtil.scalePower(1.6f,22,player), AttributeModifier.Operation.MULTIPLY_TOTAL,"vp.lyra.1"));
            if(effect == 5)
                entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity, Attributes.ARMOR, UUID.fromString("403a8f4a-e7a4-4ffa-91b0-c122efa89443"),VPUtil.scalePower(100,22,player), AttributeModifier.Operation.ADDITION,"vp.lyra.5"));
        }
        boolean notHas = true;
        for (int i = 1; i < 9; i++) {
            if (!VPUtil.hasLyra(player, i)) {
                notHas = true;
                break;
            } else notHas = false;
        }
        if (!notHas) {
            int song = random.nextInt(5) + 1;
            switch (song) {
                case 1:
                    VPUtil.play(player, SoundRegistry.LYRESONG1.get());
                    break;
                case 2:
                    VPUtil.play(player, SoundRegistry.LYRESONG2.get());
                    break;
                case 3:
                    VPUtil.play(player, SoundRegistry.LYRESONG3.get());
                    break;
                case 4:
                    VPUtil.play(player, SoundRegistry.LYRESONG4.get());
                    break;
                case 5:
                    VPUtil.play(player, SoundRegistry.LYRESONG5.get());
                    break;
            }
            for (LivingEntity entity : VPUtil.getCreaturesAndPlayersAround(player, 15, 15, 15)) {
                if(entity instanceof Player && !VPUtil.isFriendlyFireBetween(player,entity) && !VPUtil.isProtectedFromHit(player,entity))
                    continue;
                entity.getPersistentData().putLong("VPOrchestra",System.currentTimeMillis()+seconds*2);
                addRadiance(60,stack,player);
                for(int i = 1; i < 9; i++){
                    entity.getPersistentData().putLong("VPLyra"+i,System.currentTimeMillis()+seconds*2);
                }
            }
        }
        super.doSpecial(seconds, player, level, stack);
    }

    @Override
    public void doUltimate(long seconds, Player player, Level level, ItemStack stack) {
        VPUtil.play(player, SoundRegistry.LYREULT.get());
        VPUtil.spawnAura(player,20, ParticleTypes.NOTE,1);
        VPUtil.spawnCircleParticles(player,20,ParticleTypes.NOTE,5,1);
        VPUtil.spawnSphere(player,ParticleTypes.NOTE,40,3,0);
        super.doUltimate(seconds, player, level, stack);
    }
}
