package com.pyding.vp.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.entity.SillySeashell;
import com.pyding.vp.entity.models.PearlModel;
import com.pyding.vp.entity.models.SeashellModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SeashellRenderer extends EntityRenderer<SillySeashell> {
    public static final ResourceLocation PEARL = new ResourceLocation(VestigesOfThePresent.MODID,"textures/models/common_pearl.png");
    public static final ResourceLocation MAIN = new ResourceLocation(VestigesOfThePresent.MODID,"textures/models/silly_seashell.png");
    public SeashellRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(SillySeashell p_114482_) {
        return MAIN;
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(SillySeashell entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        float scale = 3;
        poseStack.scale(scale,scale,scale);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(180));
        poseStack.translate(0, -(15) / 10.0f, 0);
        Player player = Minecraft.getInstance().player;
        SeashellModel<LivingEntity> model = new SeashellModel<>(SeashellModel.createBodyLayer().bakeRoot());
        model.setupAnim(player, 50, 50, partialTicks, 50, 50);
        model.prepareMobModel(player, 50, 50, partialTicks);
        model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(MAIN)),
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        for(int i = 0; i < entity.getPersistentData().getInt("VPPeals");i++) {
            PearlModel<LivingEntity> pearl = new PearlModel<>(PearlModel.createBodyLayer().bakeRoot());
            pearl.setupAnim(player, 50, 50, partialTicks, 50, 50);
            pearl.prepareMobModel(player, 50, 50, partialTicks);
            pearl.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(PEARL)),
                    LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.translate(0,2+i,0);
        }
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }
}
