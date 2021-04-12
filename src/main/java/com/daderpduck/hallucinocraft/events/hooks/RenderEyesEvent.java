package com.daderpduck.hallucinocraft.events.hooks;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

@OnlyIn(Dist.CLIENT)
public class RenderEyesEvent extends Event {
    public final boolean enabled;

    public RenderEyesEvent(boolean enabled) {
        this.enabled = enabled;
    }
}
