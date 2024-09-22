package com.pyding.vp.client.render.blackhole;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.entity.BlackHole;
import com.pyding.vp.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

public class BlackHoleRenderer extends EntityRenderer<BlackHole> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(VestigesOfThePresent.MODID,"textures/item/models/blackholepallet.png");
    //private final BlackHoleModel<BlackHole> model;
    private final ItemRenderer itemRenderer;
    public BlackHoleRenderer(EntityRendererProvider.Context context) {
        super(context);
        itemRenderer = context.getItemRenderer();
        //model = new BlackHoleModel<>(context.bakeLayer(BlackHoleModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(BlackHole p_114482_) {
        return TEXTURE;
    }
    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(BlackHole entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        float gravity = entity.getPersistentData().getFloat("VPGravity");
        poseStack.translate(0.0F,2.5F,0.0F);
        float scale = 6+gravity/5;
        if(entity.tickCount < 20)
            scale /= 20-entity.tickCount;
        else if(entity.tickCount+20 > 20 * (gravity+2))
            scale /= (entity.tickCount+20)-(20 * (gravity+2));
        poseStack.scale(scale,scale,scale);
        float angle = (System.currentTimeMillis() % 36000) / 2.0f;
        poseStack.mulPose(Axis.YP.rotationDegrees(-angle));
        ItemStack stack = new ItemStack(ModItems.BLACKHOLE_ITEM.get());
        poseStack.scale(0.5F, 0.5F, 0.5F);
        this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), entity.getId());

        ShaderBlackHole shader = null;
        try {
            shader = new ShaderBlackHole();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        shader.bind();
        shader.setUniform("iTime", (System.currentTimeMillis() % 10000L) / 1000.0f);
        shader.setUniform("iResolution", Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight());
        shader.unbind();

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }


}
