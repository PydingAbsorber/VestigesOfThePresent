package com.pyding.vp.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.entity.EasterEggEntity;
import com.pyding.vp.entity.models.EasterEggModel;
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

public class EasterEggRenderer extends EntityRenderer<EasterEggEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(VestigesOfThePresent.MODID,"textures/item/models/easter_egg_entity.png");
    public EasterEggRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(EasterEggEntity p_114482_) {
        return TEXTURE;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(EasterEggEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        float scale = 2;
        poseStack.scale(scale,scale,scale);
        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        poseStack.translate(0.0, -1.5, 0.0);
        Player player = Minecraft.getInstance().player;
        EasterEggModel<LivingEntity> model = new EasterEggModel<>(EasterEggModel.createBodyLayer().bakeRoot());
        model.setupAnim(player, 50, 50, partialTicks, 50, 50);
        model.prepareMobModel(player, 50, 50, partialTicks);
        model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE)),
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }


}
