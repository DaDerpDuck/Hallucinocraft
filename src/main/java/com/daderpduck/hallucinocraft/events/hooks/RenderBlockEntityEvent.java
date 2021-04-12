package com.daderpduck.hallucinocraft.events.hooks;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

@OnlyIn(Dist.CLIENT)
public class RenderBlockEntityEvent extends Event {
    public final RenderBlockEntityEvent.Phase phase;
    public final MatrixStack matrixStack;

    public enum Phase {
        START, END
    }

    public RenderBlockEntityEvent(RenderBlockEntityEvent.Phase phase, MatrixStack matrixStackIn) {
        this.phase = phase;
        this.matrixStack = matrixStackIn;
    }
}
