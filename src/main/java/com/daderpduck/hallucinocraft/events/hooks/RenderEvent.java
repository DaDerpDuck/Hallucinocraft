package com.daderpduck.hallucinocraft.events.hooks;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

@OnlyIn(Dist.CLIENT)
public class RenderEvent extends Event {
    public static class LevelStartRender extends RenderEvent {
    }

    public static class LevelShaderEvent extends RenderEvent {
    }

    public static class BufferUploadShaderEvent extends RenderEvent {
    }
}
