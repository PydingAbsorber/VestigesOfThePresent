package com.pyding.vp;

import com.mojang.logging.LogUtils;
import com.pyding.vp.client.ClientProxy;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.common.CommonProxy;
import com.pyding.vp.effects.VPEffects;
import com.pyding.vp.entity.ModEntities;
import com.pyding.vp.event.EventHandler;
import com.pyding.vp.item.ModCreativeModTab;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.accessories.BeltOfBrokenMemories;
import com.pyding.vp.item.accessories.EarringOfDeadHopes;
import com.pyding.vp.item.accessories.NecklaceOfTorturedDreams;
import com.pyding.vp.item.accessories.RingOfFallenStar;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.util.ClientConfig;
import com.pyding.vp.util.ConfigHandler;
import com.pyding.vp.util.VPUtil;
import com.pyding.vp.util.VPUtilParticles;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(VestigesOfThePresent.MODID)
public class VestigesOfThePresent
{
    public static final String MODID = "vp";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String VERSION = "1.20.1:1.6.0";

    public static EventHandler eventHandler;

    public static final CommonProxy PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public VestigesOfThePresent()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        ModItems.register(modEventBus);
        ModEntities.register(modEventBus);
        SoundRegistry.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);
        eventHandler = new EventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        FMLJavaModLoadingContext.get().getModEventBus().register(PROXY);
        MinecraftForge.EVENT_BUS.register(PROXY);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.COMMON_SPEC);
        ModCreativeModTab.register(modEventBus);
        modEventBus.addListener(this::addCreative);
        VPUtil.vzlomatJopu(Float.MAX_VALUE);
        VPEffects.EFFECTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void commonSetup(final FMLCommonSetupEvent event) //pre init
    {
        PacketHandler.register();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) //init
        {
            PROXY.initEntityRendering();
            ItemProperties.register(ModItems.BELT_OF_BROKEN_MEMORIES.get(), new ResourceLocation("vp:belt"), (stack, level, entity, type) -> {
                return BeltOfBrokenMemories.getType(stack);
            });
            ItemProperties.register(ModItems.EARRING_OF_DEAD_HOPES.get(), new ResourceLocation("vp:earring"), (stack, level, entity, type) -> {
                return EarringOfDeadHopes.getType(stack);
            });
            ItemProperties.register(ModItems.RING_OF_FALLEN_STAR.get(), new ResourceLocation("vp:ring"), (stack, level, entity, type) -> {
                return RingOfFallenStar.getType(stack);
            });
            ItemProperties.register(ModItems.NECKLACE_OF_TORTURED_DREAMS.get(), new ResourceLocation("vp:necklace"), (stack, level, entity, type) -> {
                return NecklaceOfTorturedDreams.getType(stack);
            });
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

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        PROXY.loadComplete(event);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event){
        if(event.getTab() == ModCreativeModTab.VP_TAB.get()){
            event.accept(ModItems.ANEMOCULUS);
            event.accept(ModItems.CROWN);
            event.accept(ModItems.ATLAS);
            event.accept(ModItems.KILLER);
            event.accept(ModItems.MASK);
            event.accept(ModItems.DONUT);
            event.accept(ModItems.MARK);
            event.accept(ModItems.EARS);
            event.accept(ModItems.MIDAS);
            event.accept(ModItems.ANOMALY);
            event.accept(ModItems.ARMOR);
            event.accept(ModItems.BOOK);
            event.accept(ModItems.PRISM);
            event.accept(ModItems.CHAOS);
            event.accept(ModItems.DEVOURER);
            event.accept(ModItems.FLOWER);
            event.accept(ModItems.CATALYST);
            event.accept(ModItems.BALL);
            event.accept(ModItems.TRIGON);
            event.accept(ModItems.SOULBLIGHTER);
            event.accept(ModItems.RUNE);
            event.accept(ModItems.LYRA);
            event.accept(ModItems.PEARL);
            event.accept(ModItems.WHIRLPOOL);
            event.accept(ModItems.ARCHLINX);
            event.accept(ModItems.TREASURE);
            event.accept(ModItems.STELLAR);
            event.accept(ModItems.REFRESHER);
            event.accept(ModItems.RING_OF_FALLEN_STAR);
            event.accept(ModItems.EARRING_OF_DEAD_HOPES);
            event.accept(ModItems.BELT_OF_BROKEN_MEMORIES);
            event.accept(ModItems.NECKLACE_OF_TORTURED_DREAMS);
            event.accept(ModItems.BOX);
            event.accept(ModItems.BOX_SAPLINGS);
            event.accept(ModItems.MYSTERY_CHEST);
            event.accept(ModItems.MINERAL_CLUSTER);
            event.accept(ModItems.SHARD);
            event.accept(ModItems.NIGHTMARESHARD);
            event.accept(ModItems.VORTEX);
            if(VPUtil.isEasterEvent()) {
                event.accept(ModItems.EASTER_EGG);
                event.accept(ModItems.BOX_EGGS);
            }
            event.accept(ModItems.HEARTY_PEARL);
            event.accept(ModItems.SEASHELL);
            event.accept(ModItems.CORRUPT_FRAGMENT);
            event.accept(ModItems.CORRUPT_ITEM);
            event.accept(ModItems.CHAOS_ORB);
            event.accept(ModItems.CELESTIAL_MIRROR);
            event.accept(ModItems.GUIDE_BOOK);
            event.accept(ModItems.NIGHTMARE_BOOK);
            event.accept(ModItems.EVENT_HORIZON);
        }
    }
}
