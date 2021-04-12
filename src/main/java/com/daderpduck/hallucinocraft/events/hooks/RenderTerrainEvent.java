package com.daderpduck.hallucinocraft.events.hooks;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

/**
 * Hooks into {@link WorldRenderer#renderChunkLayer}
 * <br>
 * Fired when blocks are rendered
 */
@OnlyIn(Dist.CLIENT)
public class RenderTerrainEvent extends Event {
    public final Phase phase;
    public final RenderType blockLayer;
    public final MatrixStack matrixStack;

    public enum Phase {
        START, END
    }

    public RenderTerrainEvent(Phase phase, RenderType blockLayerIn, MatrixStack matrixStackIn) {
        this.phase = phase;
        this.blockLayer = blockLayerIn;
        this.matrixStack = matrixStackIn;
    }
}
