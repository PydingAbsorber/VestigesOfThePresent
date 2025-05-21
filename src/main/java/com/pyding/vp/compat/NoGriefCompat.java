package com.pyding.vp.compat;

import com.pyding.ng.util.ZoneUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;

public class NoGriefCompat {
    public static boolean ngLoaded() {
        return ModList.get().isLoaded("ng");
    }

    public static boolean canHurt(LivingEntity entity, Player player){
        BlockPos pos1 = entity.getOnPos();
        BlockPos pos2 = player.getOnPos();
        if(ZoneUtil.isInStrictZone(pos1) || ZoneUtil.isInStrictZone(pos2))
            return false;
        if(ZoneUtil.isInZone(pos1) || ZoneUtil.isInZone(pos2)){
            return ZoneUtil.canInteract(player, pos2);
        }
        return true;
    }
}
