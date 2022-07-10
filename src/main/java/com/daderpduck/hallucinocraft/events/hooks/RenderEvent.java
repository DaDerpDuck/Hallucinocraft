package com.daderpduck.hallucinocraft.events.hooks;

import net.minecraftforge.eventbus.api.Event;

public class RenderEvent extends Event {
    public static class LevelStartRender extends RenderEvent {
    }

    public static class LevelShaderEvent extends RenderEvent {
    }

    public static class BufferUploadShaderEvent extends RenderEvent {
    }
}
