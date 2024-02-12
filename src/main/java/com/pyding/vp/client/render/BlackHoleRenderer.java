package com.pyding.vp.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.pyding.vp.VestigesOfPresent;
import com.pyding.vp.entity.BlackHole;
import com.pyding.vp.entity.models.BlackHoleModel;
import com.pyding.vp.item.models.ears;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class BlackHoleRenderer extends EntityRenderer<BlackHole> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(VestigesOfPresent.MODID,"textures/item/models/blackholepallet.png");
    //private final BlackHoleModel<BlackHole> model;
    public BlackHoleRenderer(EntityRendererProvider.Context context) {
        super(context);
        //model = new BlackHoleModel<>(context.bakeLayer(BlackHoleModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(BlackHole p_114482_) {
        return TEXTURE;
    }


    @Override
    public void render(BlackHole entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        float gravity = entity.getPersistentData().getFloat("VPGravity");
        float scale = 1+gravity/5;
        poseStack.scale(scale,scale,scale);
        //poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        float angle = (System.currentTimeMillis() % 36000) / 25.0f;
        poseStack.mulPose(Vector3f.YP.rotationDegrees(angle));
        Player player = Minecraft.getInstance().player;
        //model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity))), packedLight, 0, 1.0F, 1.0F, 1.0F, 1.0F);
        BlackHoleModel<LivingEntity> model = new BlackHoleModel<>(BlackHoleModel.createBodyLayer().bakeRoot());
        model.setupAnim(player, 50, 50, partialTicks, 50, 50);
        model.prepareMobModel(player, 50, 50, partialTicks);
        model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(new ResourceLocation(VestigesOfPresent.MODID, "textures/item/models/blackholepallet.png"))),
                10, OverlayTexture.WHITE_OVERLAY_V, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

}
