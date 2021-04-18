package com.daderpduck.hallucinocraft.events.hooks;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.eventbus.api.Event;

public class RenderParticlesEvent extends Event {
    public final RenderParticlesEvent.Phase phase;
    public final MatrixStack matrixStack;

    public enum Phase {
        START, END
    }

    public RenderParticlesEvent(RenderParticlesEvent.Phase phase, MatrixStack matrixStackIn) {
        this.phase = phase;
        this.matrixStack = matrixStackIn;
    }
}
