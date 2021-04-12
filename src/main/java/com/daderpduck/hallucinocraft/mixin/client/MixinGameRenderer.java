package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.events.hooks.BobHurtEvent;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    /*@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;renderLevel(FJLcom/mojang/blaze3d/matrix/MatrixStack;)V"), method = "render")
    private void preWorldRender(float partialTicks, long finishNanoTime, boolean renderWorld, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderWorldEvent(RenderWorldEvent.Phase.START, partialTicks));
    }
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;renderLevel(FJLcom/mojang/blaze3d/matrix/MatrixStack;)V", shift = At.Shift.AFTER), method = "render")
    private void postWorldRender(float partialTicks, long finishNanoTime, boolean renderWorld, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderWorldEvent(RenderWorldEvent.Phase.END, partialTicks));
    }*/

    @Inject(at = @At("HEAD"), method = "bobHurt(Lcom/mojang/blaze3d/matrix/MatrixStack;F)V")
    private void onCamera(MatrixStack matrixStack, float partialTicks, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new BobHurtEvent(matrixStack, partialTicks));
    }
}
