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

public class EasterEggModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "egg"), "main");
	private final ModelPart egg;

	public EasterEggModel(ModelPart root) {
		this.egg = root.getChild("egg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition egg = partdefinition.addOrReplaceChild("egg", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -4.0F, 4.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(20, 9).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 0).addBox(-2.0F, -5.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(-3.0F, -4.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 3).addBox(2.0F, -4.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(6, 25).addBox(-1.0F, -4.0F, 2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 25).addBox(-3.0F, 3.0F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(10, 25).addBox(2.0F, 3.0F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 6).addBox(-2.0F, 3.0F, -3.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 21).addBox(-2.0F, 3.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(24, 18).addBox(-2.0F, 3.0F, 2.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(-1.0F, -4.0F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		egg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}