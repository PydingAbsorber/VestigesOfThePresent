package com.pyding.vp.client;

import com.pyding.vp.VestigesOfPresent;
import com.pyding.vp.client.render.BlackHoleRenderer;
import com.pyding.vp.client.render.VestigeHunterKillerRenderer;
import com.pyding.vp.entity.ModEntities;
import com.pyding.vp.entity.models.Hunter;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.*;
import com.pyding.vp.util.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = VestigesOfPresent.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(KeyBinding.FIRST_KEY_ULT.isDown()) {
                PacketHandler.sendToServer(new ButtonPressPacket(3));
            } else if (KeyBinding.FIRST_KEY.isDown()) {
                PacketHandler.sendToServer(new ButtonPressPacket(1));
            }
            if(KeyBinding.SECOND_KEY_ULT.isDown()) {
                PacketHandler.sendToServer(new ButtonPressPacket(4));
            } else if (KeyBinding.SECOND_KEY.isDown()) {
                PacketHandler.sendToServer(new ButtonPressPacket(2));
            }
        }
    }


    @Mod.EventBusSubscriber(modid = VestigesOfPresent.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.FIRST_KEY);
            event.register(KeyBinding.FIRST_KEY_ULT);
            event.register(KeyBinding.SECOND_KEY);
            event.register(KeyBinding.SECOND_KEY_ULT);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("vpshield", ShieldOverlay.HUD_SHIELD);
        }

        @SubscribeEvent
        public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
            event.registerLayerDefinition(Hunter.LAYER_LOCATION, Hunter::createBodyLayer);
        }

        @SubscribeEvent
        public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.BLACK_HOLE.get(), BlackHoleRenderer::new);
            event.registerEntityRenderer(ModEntities.KILLER.get(), VestigeHunterKillerRenderer::new);
        }

    }
}
