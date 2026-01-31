package com.pyding.vp.client;

import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.client.render.*;
import com.pyding.vp.client.render.blackhole.BlackHoleRenderer;
import com.pyding.vp.entity.ModEntities;
import com.pyding.vp.entity.models.Hunter;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.accessories.BeltOfBrokenMemories;
import com.pyding.vp.item.accessories.EarringOfDeadHopes;
import com.pyding.vp.item.accessories.NecklaceOfTorturedDreams;
import com.pyding.vp.item.accessories.RingOfFallenStar;
import com.pyding.vp.network.PacketHandler;
import com.pyding.vp.network.packets.ButtonPressPacket;
import com.pyding.vp.util.KeyBinding;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.TropicalFish;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import static com.pyding.vp.VestigesOfThePresent.MODID;
import static com.pyding.vp.client.ShieldOverlay.HUD_SHIELD;

public class ClientEvents {
    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
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

        @SubscribeEvent
        public static void renderEvent(RenderLivingEvent.Pre<LivingEntity, ?> event) {
            LivingEntity entity = event.getEntity();
            if(VPUtil.isNightmareBoss(entity))
                event.getPoseStack().scale(3.0F, 3.0F, 3.0F);
            if(entity instanceof TropicalFish && entity.getPersistentData().getLong("VPEating") > 0) {
                float scale = Math.min(10,(System.currentTimeMillis()-entity.getPersistentData().getLong("VPEating"))/10000f);
                event.getPoseStack().scale(scale,scale,scale);
            }
        }
    }


    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.FIRST_KEY);
            event.register(KeyBinding.FIRST_KEY_ULT);
            event.register(KeyBinding.SECOND_KEY);
            event.register(KeyBinding.SECOND_KEY_ULT);
        }

        @SubscribeEvent
        public static void registerLayers(RegisterGuiLayersEvent event) {
            event.registerAbove(
                    VanillaGuiLayers.PLAYER_HEALTH,
                    ResourceLocation.fromNamespaceAndPath(MODID, "shield_hud"),
                    HUD_SHIELD
            );
        }

        @SubscribeEvent
        public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
            event.registerLayerDefinition(Hunter.LAYER_LOCATION, Hunter::createBodyLayer);
        }

        @SubscribeEvent
        public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.BLACK_HOLE.get(), BlackHoleRenderer::new);
            event.registerEntityRenderer(ModEntities.VORTEX.get(), VortexRenderer::new);
            event.registerEntityRenderer(ModEntities.KILLER.get(), VestigeHunterKillerRenderer::new);
            event.registerEntityRenderer(ModEntities.EASTER_EGG_ENTITY.get(), EasterEggRenderer::new);
            event.registerEntityRenderer(ModEntities.CLOUD_ENTITY.get(), CloudRenderer::new);
            event.registerEntityRenderer(ModEntities.OYSTER.get(), OysterRenderer::new);
            event.registerEntityRenderer(ModEntities.SEASHELL.get(), SeashellRenderer::new);
            event.registerEntityRenderer(ModEntities.SHELLHEAL.get(), ShellHealRenderer::new);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                ItemProperties.register(ModItems.BELT_OF_BROKEN_MEMORIES.get(),
                        ResourceLocation.fromNamespaceAndPath(MODID, "belt"),
                        (stack, level, entity, seed) -> (float) BeltOfBrokenMemories.getType(stack));

                ItemProperties.register(ModItems.EARRING_OF_DEAD_HOPES.get(),
                        ResourceLocation.fromNamespaceAndPath(MODID, "earring"),
                        (stack, level, entity, seed) -> (float) EarringOfDeadHopes.getType(stack));

                ItemProperties.register(ModItems.RING_OF_FALLEN_STAR.get(),
                        ResourceLocation.fromNamespaceAndPath(MODID, "ring"),
                        (stack, level, entity, seed) -> (float) RingOfFallenStar.getType(stack));

                ItemProperties.register(ModItems.NECKLACE_OF_TORTURED_DREAMS.get(),
                        ResourceLocation.fromNamespaceAndPath(MODID, "necklace"),
                        (stack, level, entity, seed) -> (float) NecklaceOfTorturedDreams.getType(stack));
            });
        }
    }
}
