package com.pyding.vp.item.artifacts;

import com.pyding.vp.capability.PlayerCapabilityProviderVP;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Prism extends Vestige{
    public Prism(){
        super();
    }

    @Override
    public void dataInit(int vestigeNumber, ChatFormatting color, int specialCharges, int specialCd, int ultimateCharges, int ultimateCd, int specialMaxTime, int ultimateMaxTime, boolean hasDamage) {
        super.dataInit(13, ChatFormatting.DARK_BLUE, 4, 10, 1, 300, 1, 60, hasDamage);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(player.isCreative()) {
            player.getCapability(PlayerCapabilityProviderVP.playerCap).ifPresent(cap -> {
                cap.clearCoolDown(player);
                cap.clearAllProgress(player);
            });
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
