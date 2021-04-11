package com.daderpduck.psychedelicraft.events.hooks;

import net.minecraftforge.eventbus.api.Event;

public class PlayerTurnEvent extends Event {
    public final double accumulatedDX;
    public final double accumulatedDY;
    public final double lastMouseEventTime;

    public PlayerTurnEvent(double accumulatedDX, double accumulatedDY, double lastMouseEventTime) {
        this.accumulatedDX = accumulatedDX;
        this.accumulatedDY = accumulatedDY;
        this.lastMouseEventTime = lastMouseEventTime;
    }
}
