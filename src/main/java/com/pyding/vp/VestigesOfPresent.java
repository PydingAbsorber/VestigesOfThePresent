package com.pyding.vp;

import com.mojang.logging.LogUtils;
import com.pyding.vp.client.ClientProxy;
import com.pyding.vp.client.sounds.SoundRegistry;
import com.pyding.vp.common.CommonProxy;
import com.pyding.vp.entity.ModEntities;
import com.pyding.vp.event.EventHandler;
import com.pyding.vp.item.ModCreativeModTab;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.util.ConfigHandler;
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
@Mod(VestigesOfPresent.MODID)
public class VestigesOfPresent
{
    public static final String MODID = "vp";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static EventHandler eventHandler;

    public static final CommonProxy PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public VestigesOfPresent()
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
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC); //bye  bye
        ModCreativeModTab.register(modEventBus);
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
        }
    }

    private void postInit(InterModEnqueueEvent event) {
        LOGGER.info("Sending messages to Curios API...");
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        PROXY.loadComplete(event);
    }
}
