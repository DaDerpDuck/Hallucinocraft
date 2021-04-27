package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.events.hooks.EntityColorEvent;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.TNTMinecartRenderer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TNTMinecartRenderer.class)
public class MixinTNTMinecartRenderer {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockRendererDispatcher;renderSingleBlock(Lnet/minecraft/block/BlockState;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;II)V"), method = "renderWhiteSolidBlock")
    private static void renderTntFlash(BlockState blockState, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLight, boolean doFullBright, CallbackInfo ci) {
        if (doFullBright) MinecraftForge.EVENT_BUS.post(new EntityColorEvent(1F, 1F, 1F, 0.5F));
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/BlockRendererDispatcher;renderSingleBlock(Lnet/minecraft/block/BlockState;Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;II)V", shift = At.Shift.AFTER), method = "renderWhiteSolidBlock")
    private static void renderTntFlashEnd(BlockState blockState, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLight, boolean doFullBright, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new EntityColorEvent(0F, 0F, 0F, 0F));
    }
}
