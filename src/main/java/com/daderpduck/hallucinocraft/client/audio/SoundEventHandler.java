package com.daderpduck.hallucinocraft.client.audio;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.events.hooks.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Hallucinocraft.MOD_ID)
public class SoundEventHandler {
    @SubscribeEvent
    public static void onPlay(SoundEvent.Play event) {
        SoundProcessor.processSound(event.source, event.position.x, event.position.y, event.position.z);
    }

    @SubscribeEvent
    public static void onPitch(SoundEvent.SetPitch event) {
        Float pitch = SoundProcessor.modifyPitch(event.position.x, event.position.y, event.position.z, event.pitch);
        if (pitch != null) {
            event.setCanceled(true);
            event.pitch = pitch;
        }
    }
}
