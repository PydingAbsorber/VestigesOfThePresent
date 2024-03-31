package com.pyding.vp.entity.models;// Made with Blockbench 4.9.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class Hunter<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "hunter"), "main");
	private final ModelPart hunter;
	private final ModelPart leftArm;
	private final ModelPart rightArm;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;

	public Hunter(ModelPart root) {
		this.hunter = root.getChild("hunter");
		leftArm = hunter.getChild("arm_left");
		rightArm = hunter.getChild("arm_right");
		leftLeg = hunter.getChild("leg_left");
		rightLeg = hunter.getChild("leg_right");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition hunter = partdefinition.addOrReplaceChild("hunter", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition leg_left = hunter.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(8, 4).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -5.0F, 0.0F));

		PartDefinition chest = hunter.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 4).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

		PartDefinition arm_right = hunter.addOrReplaceChild("arm_right", CubeListBuilder.create(), PartPose.offset(-2.0F, -14.0F, 0.0F));

		PartDefinition cube_r1 = arm_right.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(14, 10).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition arm_left = hunter.addOrReplaceChild("arm_left", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -14.0F, 0.0F));

		PartDefinition leg_right = hunter.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(6, 12).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -5.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		hunter.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return hunter;
	}
}