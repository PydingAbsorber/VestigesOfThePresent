package com.pyding.vp.compat;

import com.pyding.ng.util.ZoneUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;

public class NoGriefCompat {
    public static boolean ngLoaded() {
        return ModList.get().isLoaded("ng");
    }

    public static boolean canHurt(LivingEntity entity, Player player){
        BlockPos pos1 = entity.getOnPos();
        BlockPos pos2 = player.getOnPos();
        Level level = player.level();
        if(ZoneUtil.isInStrictZone(pos1,level) || ZoneUtil.isInStrictZone(pos2,level))
            return false;
        if(ZoneUtil.isInZone(pos1,level) || ZoneUtil.isInZone(pos2,level)){
            return ZoneUtil.canInteract(player, pos2,level);
        }
        return true;
    }
}
