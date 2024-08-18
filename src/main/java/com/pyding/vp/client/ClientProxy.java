package com.pyding.vp.client;

import com.pyding.vp.client.render.EarsRender;
import com.pyding.vp.client.render.HornsRender;
import com.pyding.vp.common.CommonProxy;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.item.Seashell;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import java.util.Map;

public class ClientProxy extends CommonProxy {
    public ClientProxy() {
        super();
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::onClientSetup);
    }
    @OnlyIn(Dist.CLIENT)
    public void onClientSetup(FMLClientSetupEvent event) {
        CuriosRendererRegistry.register(ModItems.EARS.get(), EarsRender::new);
        CuriosRendererRegistry.register(ModItems.MASK.get(), HornsRender::new);
        ModItems.SEASHELL_HOLDER.registerChick();
    }

    @OnlyIn(Dist.CLIENT)
    private <T extends LivingEntity, M extends HumanoidModel<T>, R extends LivingEntityRenderer<T, M>> void addEntityLayer(EntityRenderersEvent.AddLayers evt, EntityType<? extends T> entityType) {
        /*R renderer = evt.getRenderer(entityType);

        if (renderer != null) {
            renderer.addLayer(new Layer<>(renderer, evt.getEntityModels()));
        }*/
    }
}
