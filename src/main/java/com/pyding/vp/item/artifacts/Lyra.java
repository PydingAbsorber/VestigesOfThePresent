package com.pyding.vp.item.artifacts;

import com.pyding.vp.util.VPUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

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
        super.dataInit(22, ChatFormatting.DARK_GREEN, 3, 40, 1, 60, 30, 10, false, stack);
    }

    @Override
    public void doSpecial(long seconds, Player player, Level level, ItemStack stack) {
        Random random = new Random();
        int effect = random.nextInt(8)+1;
        List<Integer> effects = new ArrayList<>();
        for(int i = 1;i < 8;i++) {
            if (VPUtil.hasLyra(player, i))
                effects.add(i);
        }
        if(!effects.isEmpty()){
            if(effects.size() >= 8)
                return;
            do {
                effect = random.nextInt(8) + 1;
            } while (effects.contains(effect));
        }
        for(LivingEntity entity: VPUtil.getCreaturesAndPlayersAround(player,15,15,15)){
            entity.getPersistentData().putLong("VPLyra"+effect,System.currentTimeMillis()+seconds);
            if(effect == 1)
                entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity, Attributes.MAX_HEALTH, UUID.fromString("9548da03-e5f8-4cfd-be48-c4ae9b25d86d"),1.6f, AttributeModifier.Operation.MULTIPLY_TOTAL,"vp.lyra.1"));
            if(effect == 5)
                entity.getAttributes().addTransientAttributeModifiers(VPUtil.createAttributeMap(entity, Attributes.ARMOR, UUID.fromString("403a8f4a-e7a4-4ffa-91b0-c122efa89443"),100, AttributeModifier.Operation.ADDITION,"vp.lyra.5"));

        }
        super.doSpecial(seconds, player, level, stack);
    }
}
