package com.pyding.vp;

import com.pyding.vp.capability.VestigeCapProvider;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.effects.VPEffects;
import com.pyding.vp.entity.ModEntities;
import com.pyding.vp.item.ModCreativeModTab;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.util.ClientConfig;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import com.pyding.vp.util.VPUtilParticles;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(VestigesOfThePresent.MODID)
public class VestigesOfThePresent {
    public static final String MODID = "vp";
    public static final String VERSION = "1.21.1:1.6.2";
    public static final Logger LOGGER = LogUtils.getLogger();

    public VestigesOfThePresent(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, ConfigHandler.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        VestigeCapProvider.ATTACHMENT_TYPES.register(modEventBus);
        ModItems.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModCreativeModTab.register(modEventBus);
        VPEffects.register(modEventBus);
        SoundRegistry.register(modEventBus);
        modEventBus.addListener(this::postInit);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Porting is hard asf");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTab() == ModCreativeModTab.VP_TAB.get()){
            event.accept(ModItems.ANEMOCULUS.get());
            event.accept(ModItems.CROWN.get());
            event.accept(ModItems.ATLAS.get());
            event.accept(ModItems.KILLER.get());
            event.accept(ModItems.MASK.get());
            event.accept(ModItems.DONUT.get());
            event.accept(ModItems.MARK.get());
            event.accept(ModItems.EARS.get());
            event.accept(ModItems.MIDAS.get());
            event.accept(ModItems.ANOMALY.get());
            event.accept(ModItems.ARMOR.get());
            event.accept(ModItems.BOOK.get());
            event.accept(ModItems.PRISM.get());
            event.accept(ModItems.CHAOS.get());
            event.accept(ModItems.DEVOURER.get());
            event.accept(ModItems.FLOWER.get());
            event.accept(ModItems.CATALYST.get());
            event.accept(ModItems.BALL.get());
            event.accept(ModItems.TRIGON.get());
            event.accept(ModItems.SOULBLIGHTER.get());
            event.accept(ModItems.RUNE.get());
            event.accept(ModItems.LYRA.get());
            event.accept(ModItems.PEARL.get());
            event.accept(ModItems.WHIRLPOOL.get());
            event.accept(ModItems.ARCHLINX.get());
            event.accept(ModItems.TREASURE.get());
            event.accept(ModItems.STELLAR.get());
            event.accept(ModItems.REFRESHER.get());
            event.accept(ModItems.RING_OF_FALLEN_STAR.get());
            event.accept(ModItems.EARRING_OF_DEAD_HOPES.get());
            event.accept(ModItems.BELT_OF_BROKEN_MEMORIES.get());
            event.accept(ModItems.NECKLACE_OF_TORTURED_DREAMS.get());
            event.accept(ModItems.BOX.get());
            event.accept(ModItems.BOX_SAPLINGS.get());
            event.accept(ModItems.MYSTERY_CHEST.get());
            event.accept(ModItems.MINERAL_CLUSTER.get());
            event.accept(ModItems.SHARD.get());
            event.accept(ModItems.NIGHTMARESHARD.get());
            event.accept(ModItems.VORTEX.get());
            if(VPUtil.isEasterEvent()) {
                event.accept(ModItems.EASTER_EGG.get());
                event.accept(ModItems.BOX_EGGS.get());
            }
            event.accept(ModItems.HEARTY_PEARL.get());
            event.accept(ModItems.SEASHELL.get());
            event.accept(ModItems.CORRUPT_FRAGMENT.get());
            event.accept(ModItems.CORRUPT_ITEM.get());
            event.accept(ModItems.CHAOS_ORB.get());
            event.accept(ModItems.CELESTIAL_MIRROR.get());
            event.accept(ModItems.GUIDE_BOOK.get());
            event.accept(ModItems.NIGHTMARE_BOOK.get());
            event.accept(ModItems.EVENT_HORIZON.get());
        }
    }

    private void postInit(InterModEnqueueEvent event) {
        LOGGER.info("Sending messages to Curios API...");
        VPUtil.initEntities();
        VPUtil.initItems();
        VPUtil.initBlocks();
        VPUtil.initFlowers();
        VPUtil.initWorlds();
        VPUtil.initEffects();
        VPUtilParticles.fillParticleMaps();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
