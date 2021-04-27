package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.events.hooks.EntityColorEvent;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks into when an entity is colored from being hurt or swelling (creeper). Replaces overlay texture.
 * TODO: TNT minecart
 */
@Mixin(LivingRenderer.class)
public abstract class MixinLivingRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
    @Shadow protected abstract float getWhiteOverlayProgress(T p_225625_1_, float p_225625_2_);

    protected MixinLivingRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingRenderer;getOverlayCoords(Lnet/minecraft/entity/LivingEntity;F)I", ordinal = 0), method = "render")
    private void entityColor(T entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, CallbackInfo ci) {
        float swellingColor = getWhiteOverlayProgress(entity, partialTicks);

        if (entity.hurtTime > 0 || entity.deathTime > 0) {
            MinecraftForge.EVENT_BUS.post(new EntityColorEvent(1F, 0F, 0F, 0.3F));
        }

        if (swellingColor > 0) {
            MinecraftForge.EVENT_BUS.post(new EntityColorEvent(swellingColor, swellingColor, swellingColor, 0.5F));
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/matrix/MatrixStack;popPose()V"), method = "render")
    private void entityColorEnd(T entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new EntityColorEvent(0F, 0F, 0F, 0F));
    }
}
