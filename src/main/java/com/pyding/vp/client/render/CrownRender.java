package com.pyding.vp.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.item.models.CrownModel;
import com.pyding.vp.item.models.HornsModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CrownRender implements ICurioRenderer {
    @OnlyIn(Dist.CLIENT)
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStack.pushPose();
        LivingEntity livingEntity = Minecraft.getInstance().player;
        if(livingEntity == null)
            return;
        if (livingEntity.isCrouching()) {
            matrixStack.translate(0.0F, 0.25F, 0.0F);
        }
        matrixStack.mulPose(Axis.YP.rotationDegrees(netHeadYaw));
        if(livingEntity.isVisuallySwimming()){
            matrixStack.mulPose(Axis.XP.rotationDegrees(-45));
        }
        else matrixStack.mulPose(Axis.XP.rotationDegrees(headPitch));
        matrixStack.translate(0.0F, 0.0F, 0.0F);
        CrownModel<LivingEntity> model = new CrownModel<>(CrownModel.createBodyLayer().bakeRoot());
        model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTicks);
        model.renderToBuffer(matrixStack, renderTypeBuffer.getBuffer(RenderType.entityTranslucent(new ResourceLocation(VestigesOfThePresent.MODID, "textures/item/models/crown_texture.png"))),
                light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }
}
