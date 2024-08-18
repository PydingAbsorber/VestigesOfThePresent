package com.pyding.vp.entity.models;// Made with Blockbench 4.10.4
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

public class HornsModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "horns"), "main");
	private final ModelPart main2;
	private final ModelPart main3;

	public HornsModel(ModelPart root) {
		this.main2 = root.getChild("main2");
		this.main3 = root.getChild("main3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main2 = partdefinition.addOrReplaceChild("main2", CubeListBuilder.create().texOffs(2, 1).addBox(0.4824F, -11.1837F, -3.8711F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 3).addBox(0.4824F, -13.1837F, -5.8711F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 6).addBox(0.4824F, -16.1837F, -6.8711F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 24.3F, -1.1F, 2.6539F, 0.2582F, 3.1091F));

		PartDefinition main3 = partdefinition.addOrReplaceChild("main3", CubeListBuilder.create().texOffs(2, 1).mirror().addBox(-0.4824F, -11.1837F, -3.8711F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 3).mirror().addBox(-0.4824F, -13.1837F, -5.8711F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 6).mirror().addBox(-0.4824F, -16.1837F, -6.8711F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 24.3F, -1.1F, 2.6539F, -0.2582F, -3.1091F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.main2.y = 0;
		this.main3.y = 0;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		main3.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}