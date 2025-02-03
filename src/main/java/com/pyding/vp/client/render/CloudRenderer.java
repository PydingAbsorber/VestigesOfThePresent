package com.pyding.vp.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.entity.CloudEntity;
import com.pyding.vp.entity.models.CloudEntityModel;
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

public class CloudRenderer extends EntityRenderer<CloudEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(VestigesOfThePresent.MODID,"textures/item/models/green_cloud.png");
    public CloudRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(CloudEntity p_114482_) {
        return TEXTURE;
    }

    private static final int[] sequence = {8, 6, 4, 6, 8, 10, 12, 14, 12, 10};
    private static final int CYCLE_DURATION = 20 * 4;
    private static final int CHANGE_DURATION = sequence.length;

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(CloudEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        int tickCount = entity.tickCount % CYCLE_DURATION;
        float scale = 10;

        if (tickCount < CHANGE_DURATION) {
            scale = sequence[tickCount];
        }
        poseStack.scale(10,scale,scale);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
        poseStack.translate(0, -(19) / 10.0f, 0);
        Player player = Minecraft.getInstance().player;
        CloudEntityModel<LivingEntity> model = new CloudEntityModel<>(CloudEntityModel.createBodyLayer().bakeRoot());
        model.setupAnim(player, 50, 50, partialTicks, 50, 50);
        model.prepareMobModel(player, 50, 50, partialTicks);
        model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE)),
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }
}
