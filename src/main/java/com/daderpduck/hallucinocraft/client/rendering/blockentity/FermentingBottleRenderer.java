package com.daderpduck.hallucinocraft.client.rendering.blockentity;

import com.daderpduck.hallucinocraft.blocks.entities.FermentingBottleBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FermentingBottleRenderer implements BlockEntityRenderer<FermentingBottleBlockEntity> {
    private final ItemRenderer itemRenderer;

    public FermentingBottleRenderer(BlockEntityRendererProvider.Context pContext) {
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(FermentingBottleBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        pBlockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(iFluidHandler -> {
            FluidStack fluidStack = iFluidHandler.getFluidInTank(0);
            if (!fluidStack.isEmpty()) {
                FluidAttributes fluidAttributes = fluidStack.getFluid().getAttributes();
                TextureAtlasSprite texture = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidAttributes.getStillTexture());

                VertexConsumer vertexConsumer = pBufferSource.getBuffer(Sheets.translucentCullBlockSheet());

                PoseStack.Pose lastPose = pPoseStack.last();
                Matrix4f matrix4f = lastPose.pose();
                Matrix3f normal = lastPose.normal();

                float scale = fluidStack.getAmount()/(float)iFluidHandler.getTankCapacity(0);

                float minU = texture.getU0();
                float maxU = texture.getU1();
                float minV = texture.getV0();
                float maxV = texture.getV1();
                int fluidColor = fluidAttributes.getColor();

                float unit1 = 1F/16F;
                float unit3 = 3F/16F;

                // North
                vertexConsumer.vertex(matrix4f, 1F - unit1, (1F - unit3)*scale, 0F + unit1).color(fluidColor).uv(minU, maxV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 1F - unit1, 0F + unit1, 0F + unit1).color(fluidColor).uv(minU, minV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 0F + unit1, 0F + unit1, 0F + unit1).color(fluidColor).uv(maxU, minV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 0F + unit1, (1F - unit3)*scale, 0F + unit1).color(fluidColor).uv(maxU, maxV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                // South
                vertexConsumer.vertex(matrix4f, 1F - unit1, 0F + unit1, 1F - unit1).color(fluidColor).uv(minU, maxV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 1F - unit1, (1F - unit3)*scale, 1F - unit1).color(fluidColor).uv(minU, minV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 0F + unit1, (1F - unit3)*scale, 1F - unit1).color(fluidColor).uv(maxU, minV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 0F + unit1, 0F + unit1, 1F - unit1).color(fluidColor).uv(maxU, maxV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                // East
                vertexConsumer.vertex(matrix4f, 1F - unit1, (1F - unit3)*scale, 1F - unit1).color(fluidColor).uv(minU, maxV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 1F - unit1, 0F + unit1, 1F - unit1).color(fluidColor).uv(minU, minV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 1F - unit1, 0F + unit1, 0F + unit1).color(fluidColor).uv(maxU, minV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 1F - unit1, (1F - unit3)*scale, 0F + unit1).color(fluidColor).uv(maxU, maxV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                // West
                vertexConsumer.vertex(matrix4f, 0F + unit1, 0F + unit1, 1F - unit1).color(fluidColor).uv(minU, maxV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 0F + unit1, (1F - unit3)*scale, 1F - unit1).color(fluidColor).uv(minU, minV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 0F + unit1, (1F - unit3)*scale, 0F + unit1).color(fluidColor).uv(maxU, minV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 0F + unit1, 0F + unit1, 0F + unit1).color(fluidColor).uv(maxU, maxV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                // Up
                vertexConsumer.vertex(matrix4f, 1F - unit1, (1F - unit3)*scale, 1F - unit1).color(fluidColor).uv(minU, maxV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 1F - unit1, (1F - unit3)*scale, 0F + unit1).color(fluidColor).uv(minU, minV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 0F + unit1, (1F - unit3)*scale, 0F + unit1).color(fluidColor).uv(maxU, minV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 0F + unit1, (1F - unit3)*scale, 1F - unit1).color(fluidColor).uv(maxU, maxV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                // Down
                vertexConsumer.vertex(matrix4f, 1F - unit1, 0F + unit1, 0F + unit1).color(fluidColor).uv(minU, maxV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 1F - unit1, 0F + unit1, 1F - unit1).color(fluidColor).uv(minU, minV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 0F + unit1, 0F + unit1, 1F - unit1).color(fluidColor).uv(maxU, minV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
                vertexConsumer.vertex(matrix4f, 0F + unit1, 0F + unit1, 0F + unit1).color(fluidColor).uv(maxU, maxV).overlayCoords(pPackedOverlay).uv2(pPackedLight).normal(normal, 1F, 1F, 1F).endVertex();
            }
        });
        pBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iItemHandler -> {
            ItemStack itemStack1 = iItemHandler.getStackInSlot(1);
            ItemStack itemStack2 = iItemHandler.getStackInSlot(2);
            ItemStack itemStack3 = iItemHandler.getStackInSlot(3);
            ItemStack itemStack4 = iItemHandler.getStackInSlot(4);

            pPoseStack.pushPose();
            pPoseStack.translate(0.5, 0.5, 0.5);
            pPoseStack.scale(0.5F, 0.5F, 0.5F);

            if (!itemStack1.isEmpty()) {
                pPoseStack.pushPose();
                pPoseStack.translate(-0.2F, -0.8F, -0.3F);
                pPoseStack.mulPose(Quaternion.fromXYZ(-1.53589F, 0.10472F, 0.349066F));
                itemRenderer.renderStatic(itemStack1, ItemTransforms.TransformType.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, 0);
                pPoseStack.popPose();
            }
            if (!itemStack2.isEmpty()) {
                pPoseStack.pushPose();
                pPoseStack.translate(0.2F, -0.78F, 0.3F);
                pPoseStack.mulPose(Quaternion.fromXYZ(1.53589F, 0.122173F, -0.174533F));
                itemRenderer.renderStatic(itemStack2, ItemTransforms.TransformType.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, 0);
                pPoseStack.popPose();
            }
            if (!itemStack3.isEmpty()) {
                pPoseStack.pushPose();
                pPoseStack.translate(-0.4F, -0.74F, 0.1F);
                pPoseStack.mulPose(Quaternion.fromXYZ(1.74533F, 0.261799F, 3.28122F));
                itemRenderer.renderStatic(itemStack3, ItemTransforms.TransformType.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, 0);
                pPoseStack.popPose();
            }
            if (!itemStack4.isEmpty()) {
                pPoseStack.pushPose();
                pPoseStack.translate(0.4F, -0.74F, -0.2F);
                pPoseStack.mulPose(Quaternion.fromXYZ(-1.74533F, 0.226893F, 3.03687F));
                itemRenderer.renderStatic(itemStack4, ItemTransforms.TransformType.FIXED, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, 0);
                pPoseStack.popPose();
            }

            pPoseStack.popPose();
        });
    }
}
