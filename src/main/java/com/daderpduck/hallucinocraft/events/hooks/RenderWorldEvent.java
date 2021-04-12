package com.daderpduck.hallucinocraft.events.hooks;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

@OnlyIn(Dist.CLIENT)
public class RenderWorldEvent extends Event {
    public final Phase phase;
    public final float partialTicks;

    public enum Phase {
        START, END
    }

    public RenderWorldEvent(Phase phase, float partialTicks) {
        this.phase = phase;
        this.partialTicks = partialTicks;
    }
}
