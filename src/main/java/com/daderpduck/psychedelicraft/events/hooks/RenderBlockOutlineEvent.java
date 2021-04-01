package com.daderpduck.psychedelicraft.events.hooks;

import net.minecraftforge.eventbus.api.Event;

public class RenderBlockOutlineEvent extends Event {
    public final Phase phase;

    public enum Phase {
        START, END
    }

    public RenderBlockOutlineEvent(Phase phase) {
        this.phase = phase;
    }
}
