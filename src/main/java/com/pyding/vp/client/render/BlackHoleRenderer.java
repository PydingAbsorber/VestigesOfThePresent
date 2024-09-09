package com.pyding.vp.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.pyding.vp.VestigesOfThePresent;
import com.pyding.vp.entity.BlackHole;
import com.pyding.vp.entity.models.BlackHoleModel;
import com.pyding.vp.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderItemInFrameEvent;

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
        //poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        float angle = (System.currentTimeMillis() % 36000) / 2.0f;
        poseStack.mulPose(Axis.YP.rotationDegrees(-angle));
        Player player = Minecraft.getInstance().player;
        ItemStack stack = new ItemStack(ModItems.BLACKHOLE_ITEM.get());
        ItemEntity itemEntity = new ItemEntity(entity.getCommandSenderWorld(),0,0,0,stack);
        poseStack.scale(0.5F, 0.5F, 0.5F);
        this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), entity.getId());
        /*BlackHoleModel<LivingEntity> model = new BlackHoleModel<>(BlackHoleModel.createBodyLayer().bakeRoot());
        model.setupAnim(player, 50, 50, partialTicks, 50, 50);
        model.prepareMobModel(player, 50, 50, partialTicks);
        model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityTranslucent(TEXTURE)),
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);*/
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }


}
