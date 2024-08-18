package com.pyding.vp.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.entity.HunterKiller;
import com.pyding.vp.entity.models.Hunter;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class VestigeHunterKillerRenderer extends MobRenderer<HunterKiller, Hunter<HunterKiller>> {
    public VestigeHunterKillerRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new Hunter<>(p_174304_.bakeLayer(Hunter.LAYER_LOCATION)),2);
    }

    @Override
    public ResourceLocation getTextureLocation(HunterKiller p_114482_) {
        return new ResourceLocation(VestigesOfThePresent.MODID,"textures/models/hunter.png");
    }

    @Override
    public void render(HunterKiller entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.scale(4,4,4);
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }
}
