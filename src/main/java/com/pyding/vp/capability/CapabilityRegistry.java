package com.pyding.vp.capability;

import com.pyding.vp.VestigesOfPresent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CapabilityRegistry {
    public static final Capability<IVPCapability> DATA = CapabilityManager.get(new CapabilityToken<>() {
    });

    @SubscribeEvent
    public static void onCapabilityRegistry(RegisterCapabilitiesEvent event) {
        event.register(IVPCapability.VPCapability.class);
    }

    @SubscribeEvent
    public static void onCapabilityAttach(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player)
            event.addCapability(new ResourceLocation(VestigesOfPresent.MODID, "data"), new IVPCapability.VPCapabilityProvider());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath())
            return;

        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        oldPlayer.reviveCaps();

        newPlayer.getCapability(DATA).orElse(null).deserializeNBT(oldPlayer.getCapability(DATA).orElse(null).serializeNBT());

        oldPlayer.invalidateCaps();
    }
}
