package com.daderpduck.hallucinocraft.events.hooks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraftforge.eventbus.api.Event;

/**
 * Hooks into {@link LevelRenderer#renderLevel}
 * <br>
 * Fires after camera's coordinates has been set
 */
public class SetCameraEvent extends Event {
    public final PoseStack matrixStack;
    public final float partialTicks;
    public final long finishTimeNano;
    public final boolean drawBlockOutline;
    public final Camera camera;
    public final GameRenderer gameRenderer;
    public final LightTexture lightMap;
    public final Matrix4f projection;

    public SetCameraEvent(PoseStack matrixStackIn, float partialTicks, long finishTimeNano, boolean drawBlockOutline, Camera camera, GameRenderer gameRendererIn, LightTexture lightmapIn, Matrix4f projectionIn) {
        this.matrixStack = matrixStackIn;
        this.partialTicks = partialTicks;
        this.finishTimeNano = finishTimeNano;
        this.drawBlockOutline = drawBlockOutline;
        this.camera = camera;
        this.gameRenderer = gameRendererIn;
        this.lightMap = lightmapIn;
        this.projection = projectionIn;
    }
}
