package com.daderpduck.hallucinocraft.client.audio;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.daderpduck.hallucinocraft.events.hooks.SoundEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.openal.AL10;

import java.util.Random;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Hallucinocraft.MOD_ID)
public class SoundEventHandler {
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onPlay(SoundEvent.Play event) {
        SoundProcessor.processSound(event.source, event.position.x, event.position.y, event.position.z);
    }

    @SubscribeEvent
    public static void onPitch(SoundEvent.SetPitch event) {
        Minecraft mc = Minecraft.getInstance();
        Vec3 pos = event.position;
        if (mc.player == null || mc.level == null || (pos.x == 0 && pos.y == 0 && pos.z == 0)) return;

        float pitch = Drug.getDrugEffects().PITCH_RANDOM_SCALE.getClamped()*0.9F;
        if (pitch != 0F) {
            event.setCanceled(true);
            AL10.alSourcef(event.source, AL10.AL_PITCH, event.pitch*(1F + Mth.randomBetween(RANDOM, -pitch, pitch)));
        }
    }
}
