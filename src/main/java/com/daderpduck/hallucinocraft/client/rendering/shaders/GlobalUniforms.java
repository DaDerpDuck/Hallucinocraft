package com.daderpduck.hallucinocraft.client.rendering.shaders;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class GlobalUniforms {
    public static float timePassed = 0;
    public static float timePassedSin = 0;

    static class EventHandler {
        @SubscribeEvent
        public static void onRenderStart(TickEvent.RenderTickEvent event) {
            if (event.phase == TickEvent.Phase.START) {
                timePassed = (event.renderTickTime + Minecraft.getInstance().gui.getGuiTicks())*0.05F;
                timePassedSin = Mth.sin(timePassed);
            }
        }
    }
}
