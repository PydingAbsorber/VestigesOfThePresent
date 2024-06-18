package com.pyding.vp.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.pyding.vp.item.ModItems;
import com.pyding.vp.util.VPUtil;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow @Final private RenderBuffers renderBuffers;

    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;

    @Inject(method = "renderEntity",at = @At("HEAD"), require = 1, cancellable = true)
    protected void renderEntityMixin(Entity entity, double p_109519_, double p_109520_, double p_109521_, float p_109522_, PoseStack p_109523_, MultiBufferSource multibuffersource, CallbackInfo ci){
        if(VPUtil.isNightmareBoss(entity)) {
            OutlineBufferSource outlinebuffersource = this.renderBuffers.outlineBufferSource();
            int r = 0;
            int g = 0;
            int b = 0;
            int type = entity.getPersistentData().getInt("VPBossType");
            switch (type) {
                case 1: // Красный
                    r = 255;
                    g = 0;
                    b = 0;
                    break;
                case 2: // Зеленый
                    r = 0;
                    g = 255;
                    b = 0;
                    break;
                case 3: // Коричневый
                    r = 139;
                    g = 69;
                    b = 19;
                    break;
                case 4: // Голубой
                    r = 0;
                    g = 191;
                    b = 255;
                    break;
                case 5: // Циан
                    r = 0;
                    g = 255;
                    b = 255;
                    break;
                case 6: // Бардовый
                    r = 128;
                    g = 0;
                    b = 128;
                    break;
                default:
                    r = 255;
                    g = 255;
                    b = 255; // Белый
                    break;
            }
            outlinebuffersource.setColor(r,g,b, 255);
            multibuffersource = outlinebuffersource;
            double d0 = Mth.lerp((double)p_109522_, entity.xOld, entity.getX());
            double d1 = Mth.lerp((double)p_109522_, entity.yOld, entity.getY());
            double d2 = Mth.lerp((double)p_109522_, entity.zOld, entity.getZ());
            float f = Mth.lerp(p_109522_, entity.yRotO, entity.getYRot());
            this.entityRenderDispatcher.render(entity, d0 - p_109519_, d1 - p_109520_, d2 - p_109521_, f, p_109522_, p_109523_, multibuffersource, this.entityRenderDispatcher.getPackedLightCoords(entity, p_109522_));
            ci.cancel();
        }
    }
}
