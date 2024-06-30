package com.pyding.vp.entity.models;// Made with Blockbench 4.10.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class CloudEntityModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "cloudentity"), "main");
	private final ModelPart body;

	public CloudEntityModel(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(5.0F, -1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(6, 16).addBox(-4.0F, 0.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(17, 11).addBox(-3.0F, 1.0F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(23, 15).addBox(2.0F, 1.0F, -5.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(-7.0F, 0.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(6, 18).addBox(3.0F, 0.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(25, 31).addBox(4.0F, 1.0F, -4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(24, 25).addBox(-1.0F, -5.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(35, 35).addBox(-5.0F, 0.0F, -5.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(9, 24).addBox(1.0F, -4.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 35).addBox(-7.0F, -2.0F, -4.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(9, 28).addBox(-6.0F, -1.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(18, 31).addBox(4.0F, 0.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 19).addBox(-4.0F, -4.0F, -4.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-5.0F, -2.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -3.0F, -4.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 22).addBox(4.0F, -3.0F, -5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(18, 38).addBox(-4.0F, -3.0F, -5.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 19).addBox(-6.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 21).addBox(3.0F, -4.0F, -5.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 5).addBox(-2.0F, -4.0F, -5.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(26, 28).addBox(-6.0F, -2.0F, -5.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(6, 0).addBox(-7.0F, -1.0F, 0.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(39, 9).addBox(2.0F, 1.0F, -6.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 24).addBox(-3.0F, -5.0F, 0.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 37).addBox(1.0F, -5.0F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(32, 21).addBox(3.0F, -5.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(33, 6).addBox(-6.0F, -4.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(23, 11).addBox(2.0F, -4.0F, -5.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(24, 6).addBox(-2.0F, 1.0F, -6.0F, 3.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(10, 31).addBox(4.0F, -4.0F, -4.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(9, 22).addBox(5.0F, -3.0F, -3.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(12, 17).addBox(-4.0F, 0.0F, -6.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(23, 11).addBox(4.0F, -1.0F, -6.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(6, 5).addBox(-5.0F, -1.0F, -6.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 24).addBox(5.0F, 0.0F, -5.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(21, 37).addBox(4.0F, 1.0F, -1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-2.0F, -5.0F, -1.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(18, 33).addBox(2.0F, -5.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(35, 0).addBox(-5.0F, -4.0F, 0.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(40, 21).addBox(-5.0F, -2.0F, -6.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 40).addBox(2.0F, -3.0F, -6.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 34).addBox(-3.0F, -3.0F, -6.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(18, 31).addBox(-5.0F, 1.0F, -2.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(32, 35).addBox(3.0F, -4.0F, 2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 6).addBox(-6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(1.0F, 1.0F, -7.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(2.0F, 0.0F, -7.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 21).addBox(4.0F, -4.0F, 2.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(40, 36).addBox(-1.0F, 0.0F, -7.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 11).addBox(-7.0F, -2.0F, 1.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(-5.0F, 0.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 29).addBox(5.0F, 0.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(5, 38).addBox(-3.0F, -4.0F, 2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(10, 38).addBox(2.0F, -4.0F, 3.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(37, 39).addBox(-5.0F, -3.0F, 3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(3.0F, -1.0F, -7.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 41).addBox(3.0F, -3.0F, 4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 21).addBox(5.0F, -2.0F, -3.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(12, 11).addBox(-6.0F, 0.0F, -4.0F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(13, 38).addBox(-2.0F, -4.0F, 3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(1.0F, -4.0F, 2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(4, 22).addBox(-5.0F, -3.0F, 5.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(39, 5).addBox(-4.0F, -2.0F, -7.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 41).addBox(2.0F, -3.0F, 5.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(42, 0).addBox(1.0F, -3.0F, -7.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 22).addBox(-6.0F, -3.0F, 4.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(5, 42).addBox(-2.0F, -3.0F, -7.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(6, 11).addBox(4.0F, -2.0F, 5.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 31).addBox(-5.0F, 0.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(-1.0F, -4.0F, -7.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(34, 29).addBox(4.0F, 0.0F, 2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(40, 18).addBox(-5.0F, 1.0F, 4.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(3.0F, 1.0F, -4.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(34, 24).addBox(-1.0F, -4.0F, 3.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(32, 12).addBox(-4.0F, -3.0F, 6.0F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(11, 42).addBox(1.0F, 0.0F, -8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 27).addBox(-5.0F, -2.0F, 6.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 39).addBox(3.0F, -2.0F, -8.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(40, 29).addBox(1.0F, -2.0F, -8.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 5).addBox(-1.0F, -2.0F, -8.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 0).addBox(0.0F, -3.0F, -8.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(15, 42).addBox(3.0F, 0.0F, 6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.0F, 1.0F, -3.0F, 7.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(10, 32).addBox(3.0F, -2.0F, 6.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 14).addBox(-3.0F, -2.0F, 7.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}