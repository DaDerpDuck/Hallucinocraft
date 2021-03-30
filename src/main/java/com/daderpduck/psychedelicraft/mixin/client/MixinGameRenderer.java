package com.daderpduck.psychedelicraft.mixin.client;

import com.daderpduck.psychedelicraft.events.hooks.RenderWorldEvent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// unused
@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;renderLevel(FJLcom/mojang/blaze3d/matrix/MatrixStack;)V"), method = "render")
    private void preWorldRender(float partialTicks, long finishNanoTime, boolean renderWorld, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderWorldEvent(RenderWorldEvent.Phase.START, partialTicks));
    }
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;renderLevel(FJLcom/mojang/blaze3d/matrix/MatrixStack;)V", shift = At.Shift.AFTER), method = "render")
    private void postWorldRender(float partialTicks, long finishNanoTime, boolean renderWorld, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new RenderWorldEvent(RenderWorldEvent.Phase.END, partialTicks));
    }
}
