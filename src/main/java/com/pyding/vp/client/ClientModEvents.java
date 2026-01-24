package com.pyding.vp.client;

import com.pyding.vp.client.render.CrownRender;
import com.pyding.vp.client.render.EarsRender;
import com.pyding.vp.client.render.HornsRender;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.MysteryChest;
import com.pyding.vp.item.Seashell;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@EventBusSubscriber(modid = "your_mod_id")
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            CuriosRendererRegistry.register(ModItems.EARS.get(), EarsRender::new);
            CuriosRendererRegistry.register(ModItems.MASK.get(), HornsRender::new);
            CuriosRendererRegistry.register(ModItems.CROWN.get(), CrownRender::new);
            if (ModItems.SEASHELL.get() instanceof Seashell seashell) {
                seashell.registerChick();
            }
            if (ModItems.MYSTERY_CHEST.get() instanceof MysteryChest mystery) {
                mystery.registerChick();
            }
        });
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.AddLayers event) {
    }

    private static <T extends LivingEntity, M extends HumanoidModel<T>, R extends LivingEntityRenderer<T, M>>
    void addEntityLayer(EntityRenderersEvent.AddLayers evt, EntityType<? extends T> entityType) {
    }
}
