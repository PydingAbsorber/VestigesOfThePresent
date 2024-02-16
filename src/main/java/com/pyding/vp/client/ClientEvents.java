package com.pyding.vp.client;

import com.pyding.vp.VestigesOfPresent;
import com.pyding.vp.client.render.BlackHoleRenderer;
import com.pyding.vp.entity.ModEntities;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ButtonPressPacket;
import com.pyding.vp.network.packets.ButtonPressPacket2;
import com.pyding.vp.network.packets.ButtonPressPacket3;
import com.pyding.vp.network.packets.ButtonPressPacket4;
import com.pyding.vp.util.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
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
            if(KeyBinding.FIRST_KEY.consumeClick()) {
                PacketHandler.sendToServer(new ButtonPressPacket());
            }
            if(KeyBinding.SECOND_KEY.consumeClick()) {
                PacketHandler.sendToServer(new ButtonPressPacket2());
            }
            if(KeyBinding.FIRST_KEY_ULT.consumeClick()) {
                PacketHandler.sendToServer(new ButtonPressPacket3());
            }
            if(KeyBinding.SECOND_KEY_ULT.consumeClick()) {
                PacketHandler.sendToServer(new ButtonPressPacket4());
            }
        }
    }

    @Mod.EventBusSubscriber(modid = VestigesOfPresent.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.FIRST_KEY);
            event.register(KeyBinding.SECOND_KEY);
            event.register(KeyBinding.FIRST_KEY_ULT);
            event.register(KeyBinding.SECOND_KEY_ULT);
        }

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll("vpshield", ShieldOverlay.HUD_SHIELD);
        }

        @SubscribeEvent
        public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.BLACK_HOLE.get(), BlackHoleRenderer::new);
        }
    }
}
