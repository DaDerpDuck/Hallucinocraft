package com.daderpduck.psychedelicraft.mixin.client;

import com.daderpduck.psychedelicraft.client.rendering.shaders.RenderUtil;
import com.daderpduck.psychedelicraft.client.rendering.shaders.ShaderRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Flushes render buffer after model is rendered. Needed for lighting and hurt color to work correctly.
 */
@Mixin(ModelRenderer.class)
public class MixinModelRenderer {
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/matrix/MatrixStack;popPose()V", shift = At.Shift.AFTER), method = "render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lcom/mojang/blaze3d/vertex/IVertexBuilder;IIFFFF)V")
    private void onRender(MatrixStack p_228309_1_, IVertexBuilder p_228309_2_, int p_228309_3_, int p_228309_4_, float p_228309_5_, float p_228309_6_, float p_228309_7_, float p_228309_8_, CallbackInfo ci) {
        if (ShaderRenderer.useShader) RenderUtil.flushRenderBuffer();
    }
}
