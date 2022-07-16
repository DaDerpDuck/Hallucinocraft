package com.daderpduck.hallucinocraft.sounds;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.drugs.Drug;
import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Hallucinocraft.MOD_ID)
public class SoundEventHandler {
    private static final Random RANDOM = new Random();
    private static final List<Channel> channels = new LinkedList<>();
    private static DrugLoopSoundInstance soulWrencherLoop;
    private static float soulWrencherLoopTicks = 0F;

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        SoundManager soundManager = mc.getSoundManager();
        float effect = Drug.getDrugEffects().SOUL_WRENCHER_AMBIENCE.getClamped();

        if (effect > 0F) {
            if (soulWrencherLoop == null) {
                soulWrencherLoop = new DrugLoopSoundInstance(ModSounds.AMBIENT_SOUL_WRENCHER_LOOP.get());
                soundManager.play(soulWrencherLoop);
            }
            soulWrencherLoop.setVolume(effect);

            soulWrencherLoopTicks += RANDOM.nextFloat()*effect;
            if (soulWrencherLoopTicks > 100F) {
                soulWrencherLoopTicks -= 100F;

                soundManager.play(SimpleSoundInstance.forAmbientAddition(ModSounds.AMBIENT_SOUL_WRENCHER_ADDITIONS.get()));
            }
        } else if (soulWrencherLoop != null) {
            soulWrencherLoop.setVolume(0F);
            soulWrencherLoopTicks = 0F;
            soulWrencherLoop = null;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (Minecraft.getInstance().player == null) return;
        if (event.phase == TickEvent.Phase.START) {
            channels.removeIf(Channel::stopped);
            Drug.getDrugEffects().ARTIFACTS.setValue(channels.isEmpty() ? 0F : (1F + Drug.getDrugEffects().GLITCH.getClamped()));
        }
    }

    @SubscribeEvent
    public static void onPlaySound(PlaySoundSourceEvent event) {
        if (event.getSound().getLocation().equals(ModSounds.AMBIENT_SOUL_WRENCHER_ADDITIONS.get().getLocation())) {
            if (event.getChannel().playing())
                channels.add(event.getChannel());
        }
    }
}
