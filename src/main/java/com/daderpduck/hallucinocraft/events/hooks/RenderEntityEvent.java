package com.daderpduck.hallucinocraft.events.hooks;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

/**
 * Hooks into {@link WorldRenderer#renderLevel}
 * <br>
 * Fired on entity rendering
 */
@OnlyIn(Dist.CLIENT)
public class RenderEntityEvent extends Event {
    public final RenderEntityEvent.Phase phase;
    public final MatrixStack matrixStack;

    public enum Phase {
        START, END
    }

    public RenderEntityEvent(RenderEntityEvent.Phase phase, MatrixStack matrixStackIn) {
        this.phase = phase;
        this.matrixStack = matrixStackIn;
    }
}
