package com.pyding.vp.entity.models;// Made with Blockbench 4.9.4
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

public class VortexModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "vortex"), "main");
	private final ModelPart vortex;

	public VortexModel(ModelPart root) {
		this.vortex = root.getChild("vortex");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition vortex = partdefinition.addOrReplaceChild("vortex", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -9.0F, -5.0F, 14.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-7.0F, -10.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(30, 23).addBox(6.0F, -10.0F, -5.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(12, 36).addBox(-6.0F, -9.0F, -6.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 34).addBox(-6.0F, -9.0F, 5.0F, 12.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(37, 37).addBox(-5.0F, -10.0F, 6.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(37, 35).addBox(-5.0F, -10.0F, -7.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(3, 3).addBox(-6.0F, -10.0F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(3, 1).addBox(-6.0F, -10.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 2).addBox(5.0F, -10.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(5.0F, -10.0F, -6.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 11).addBox(-5.0F, -8.0F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 23).addBox(-6.0F, -8.0F, -4.0F, 12.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(30, 11).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(38, 0).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		vortex.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}