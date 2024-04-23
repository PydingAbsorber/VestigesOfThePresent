package com.pyding.vp.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pyding.vp.VestigesOfPresent;
import com.pyding.vp.entity.BlackHole;
import com.pyding.vp.entity.VortexEntity;
import com.pyding.vp.entity.models.BlackHoleModel;
import com.pyding.vp.entity.models.VortexModel;
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

public class VortexRenderer extends EntityRenderer<VortexEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(VestigesOfPresent.MODID,"textures/item/models/vortex.png");
    public VortexRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(VortexEntity p_114482_) {
        return TEXTURE;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(VortexEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        float gravity = 10;
        float scale = 1+gravity/5;
        for(String element: entity.getPersistentData().getString("VPVortexList").split(","))
            scale++;
        if(entity.tickCount < 20)
            scale /= 20-entity.tickCount;
        else if(entity.tickCount+20 > 20 * (gravity+2))
            scale /= (entity.tickCount+20)-(20 * (gravity+2));
        poseStack.scale(scale,scale,scale);
        //poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        float angle = (System.currentTimeMillis() % 36000) / 16.0f;
        poseStack.mulPose(Axis.YP.rotationDegrees(-angle));
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        Player player = Minecraft.getInstance().player;
        VortexModel<LivingEntity> model = new VortexModel<>(VortexModel.createBodyLayer().bakeRoot());
        model.setupAnim(player, 50, 50, partialTicks, 50, 50);
        model.prepareMobModel(player, 50, 50, partialTicks);
        model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE)),
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }


}
