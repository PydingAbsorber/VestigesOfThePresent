package com.pyding.vp.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class CapabilityUtils {
    /*public static IVPCapability getVPCapability(Player player) {
        return player.getCapability(CapabilityRegistry.DATA).orElse(null);
    }

    public static CompoundTag getCapData(Player player) {
        return getVPCapability(player).getCapData();
    }

    public static void setResearchData(Player player, CompoundTag data) {
        CapabilityUtils.getRelicsCapability(player).setResearchData(data);

        if (!player.level().isClientSide())
            NetworkHandler.sendToClient(new CapabilitySyncPacket(CapabilityUtils.getRelicsCapability(player).serializeNBT()), (ServerPlayer) player);
    }

    public static boolean isItemResearched(Player player, Item item) {
        return item instanceof RelicItem relic && getResearchData(player).getBoolean(ForgeRegistries.ITEMS.getKey(relic).getPath() + "_researched");
    }

    public static void setItemResearched(Player player, Item item, boolean researched) {
        getResearchData(player).putBoolean(ForgeRegistries.ITEMS.getKey(item).getPath() + "_researched", researched);

        if (!player.level().isClientSide())
            NetworkHandler.sendToClient(new CapabilitySyncPacket(CapabilityUtils.getRelicsCapability(player).serializeNBT()), (ServerPlayer) player);
    }*/
}
