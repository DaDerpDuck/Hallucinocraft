package com.daderpduck.psychedelicraft.events.hooks;

import com.daderpduck.psychedelicraft.mixin.client.MixinRenderHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fires when lighting is toggled.
 * Hooks into {@link RenderHelper#turnBackOn()} and {@link RenderHelper#turnOff()} by {@link MixinRenderHelper}.
 */
@OnlyIn(Dist.CLIENT)
public class EnableLightEvent extends Event {
    public final boolean enabled;

    public EnableLightEvent(boolean enabled) {
        this.enabled = enabled;
    }
}
