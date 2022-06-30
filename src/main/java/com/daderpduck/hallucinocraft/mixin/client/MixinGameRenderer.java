package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.events.hooks.BobHurtEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO: Inject into shader getters, conditionally return our shaders or Minecraft's shaders depending on if rendering level
@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(at = @At("HEAD"), method = "bobHurt(Lcom/mojang/blaze3d/vertex/PoseStack;F)V")
    private void onCamera(PoseStack pMatrixStack, float pPartialTicks, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new BobHurtEvent(pMatrixStack, pPartialTicks));
    }
}
