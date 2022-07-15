package com.daderpduck.hallucinocraft.client.audio;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.client.ClientUtil;
import com.daderpduck.hallucinocraft.HallucinocraftConfig;
import com.daderpduck.hallucinocraft.drugs.Drug;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.EXTEfx;

import java.util.Random;

import static org.lwjgl.openal.AL10.AL_TRUE;
import static org.lwjgl.openal.EXTEfx.*;

public class SoundProcessor {
    private static boolean isSetup = false;
    private static int maxAuxSends;
    private static final Random RANDOM = new Random();

    private static int directFilter;

    private static int auxFXSlot0;
    private static int sendFilter0;
    private static int echoEffect0;

    private static int auxFXSlot1;
    private static int sendFilter1;
    private static int equalizerEffect1;

    private static int auxFXSlot2;
    private static int sendFilter2;
    private static int distortionEffect2;


    public static void init() {
        // https://github.com/rtpHarry/Sokoban/blob/master/libraries/OpenAL%201.1%20SDK/docs/Effects%20Extension%20Guide.pdf

        if (!HallucinocraftConfig.CLIENT.useSoundProcessor.get()) return;
        Hallucinocraft.LOGGER.info("Initializing sound processor");

        long currentContext = ALC10.alcGetCurrentContext();
        long currentDevice = ALC10.alcGetContextsDevice(currentContext);
        if (!ALC10.alcIsExtensionPresent(currentDevice, "ALC_EXT_EFX")) {
            Hallucinocraft.LOGGER.error("EFX Extension not present on current device");
            return;
        }

        maxAuxSends = ALC10.alcGetInteger(currentDevice, ALC_MAX_AUXILIARY_SENDS);
        Hallucinocraft.LOGGER.debug("Max auxiliary sends: {}", maxAuxSends);

        directFilter = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(directFilter, AL_FILTER_TYPE, AL_FILTER_LOWPASS);
        Hallucinocraft.LOGGER.debug("Filter {} created (direct filter)", directFilter);
        ClientUtil.checkAlErrors("Creating direct filter");

        auxFXSlot0 = EXTEfx.alGenAuxiliaryEffectSlots();
        EXTEfx.alAuxiliaryEffectSloti(auxFXSlot0, AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL_TRUE);
        Hallucinocraft.LOGGER.debug("Aux slot {} created", auxFXSlot0);
        ClientUtil.checkAlErrors("Creating auxiliary effect slot 0");
        sendFilter0 = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(sendFilter0, AL_FILTER_TYPE, AL_FILTER_LOWPASS);
        Hallucinocraft.LOGGER.debug("Filter {} created (send filter 0)", sendFilter0);
        ClientUtil.checkAlErrors("Creating send filter 0");
        echoEffect0 = EXTEfx.alGenEffects();
        EXTEfx.alEffecti(echoEffect0, AL_EFFECT_TYPE, AL_EFFECT_ECHO);
        Hallucinocraft.LOGGER.debug("Echo effect {} created", echoEffect0);
        ClientUtil.checkAlErrors("Creating echo effect");

        auxFXSlot1 = EXTEfx.alGenAuxiliaryEffectSlots();
        EXTEfx.alAuxiliaryEffectSloti(auxFXSlot1, AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL_TRUE);
        Hallucinocraft.LOGGER.debug("Aux slot {} created", auxFXSlot1);
        ClientUtil.checkAlErrors("Creating auxiliary effect slot 1");
        sendFilter1 = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(sendFilter1, AL_FILTER_TYPE, AL_FILTER_LOWPASS);
        Hallucinocraft.LOGGER.debug("Filter {} created (send filter 1)", sendFilter1);
        ClientUtil.checkAlErrors("Creating send filter 1");
        equalizerEffect1 = EXTEfx.alGenEffects();
        EXTEfx.alEffecti(equalizerEffect1, AL_EFFECT_TYPE, AL_EFFECT_EQUALIZER);
        Hallucinocraft.LOGGER.debug("Equalizer effect {} created", equalizerEffect1);
        ClientUtil.checkAlErrors("Creating equalizer effect");
        setEqualizerParameters(equalizerEffect1, auxFXSlot1, new EqualizerParams(
                0.4F,
                0.8F,
                1F,
                1F,
                1F,
                1.05F
        ));


        auxFXSlot2 = EXTEfx.alGenAuxiliaryEffectSlots();
        EXTEfx.alAuxiliaryEffectSloti(auxFXSlot2, AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL_TRUE);
        Hallucinocraft.LOGGER.debug("Aux slot {} created", auxFXSlot2);
        ClientUtil.checkAlErrors("Creating auxiliary effect slot 2");
        sendFilter2 = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(sendFilter2, AL_FILTER_TYPE, AL_FILTER_LOWPASS);
        Hallucinocraft.LOGGER.debug("Filter {} created (send filter 2)", sendFilter2);
        ClientUtil.checkAlErrors("Creating send filter 2");
        distortionEffect2 = EXTEfx.alGenEffects();
        EXTEfx.alEffecti(distortionEffect2, AL_EFFECT_TYPE, AL_EFFECT_DISTORTION);
        Hallucinocraft.LOGGER.debug("Distortion effect {} created", distortionEffect2);
        ClientUtil.checkAlErrors("Creating distortion effect");

        isSetup = true;
    }

    public static void cleanup() {
        if (!isSetup) return;
        EXTEfx.alDeleteEffects(new int[]{echoEffect0, equalizerEffect1, distortionEffect2});
        EXTEfx.alDeleteFilters(new int[]{directFilter, sendFilter0, sendFilter1, sendFilter2});
        EXTEfx.alDeleteAuxiliaryEffectSlots(new int[]{auxFXSlot0, auxFXSlot1, auxFXSlot2});
    }

    public static Float modifyPitch(double x, double y, double z, float pitch) {
        if (!HallucinocraftConfig.CLIENT.useSoundProcessor.get()) return null;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || (x == 0 && y == 0 && z == 0)) return null;

        float pitchRange = Drug.getDrugEffects().PITCH_RANDOM_SCALE.getClamped()*0.9F;
        if (pitchRange != 0F) {
            return pitch * (1F + Mth.randomBetween(RANDOM, -pitchRange, pitchRange));
        } else {
            return null;
        }
    }

    public static void processSound(int source, double x, double y, double z) {
        if (!HallucinocraftConfig.CLIENT.useSoundProcessor.get()) return;
        if (!isSetup) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || (x == 0 && y == 0 && z == 0)) {
            setDefaultParameters(source);
            return;
        }

        float echoFactor = Drug.getDrugEffects().ECHO.getClamped();
        float delay = echoFactor*0.1F;
        float damping = (1F - echoFactor)*0.4F + 0.5F;
        setEchoParameters(echoEffect0, auxFXSlot0, delay, damping);

        float directCutoff = 1F - Drug.getDrugEffects().MUFFLE.getClamped(0F, 0.9F);
        float directGain = (float)Math.pow(directCutoff, 0.1);

        float sendCutoff0 = echoFactor*directCutoff*directCutoff;
        float sendGain0 = (float)Math.pow(sendCutoff0, 0.1);

        float sendCutoff1 = Drug.getDrugEffects().TREBLE.getClamped();
        float sendGain1 = (float)Math.sqrt(sendCutoff1);

        float sendGain2 = Drug.getDrugEffects().DISTORTION.getClamped();
        setDistortionParameters(distortionEffect2, auxFXSlot2, sendGain2*0.3F);

        setFilterParameters(source, directGain, directCutoff, sendGain0, sendCutoff0, sendGain1, sendCutoff1, sendGain2, 1F);
    }

    private static void setDefaultParameters(int source) {
        setFilterParameters(source, 1F, 1F, 0F, 1F, 0F, 1F, 0F, 1F);
    }

    private static void setFilterParameters(int source, float directGain, float directCutoff, float sendGain0, float sendCutoff0, float sendGain1, float sendCutoff1, float sendGain2, float sendCutoff2) {
        EXTEfx.alFilterf(directFilter, AL_LOWPASS_GAIN, directGain);
        EXTEfx.alFilterf(directFilter, AL_LOWPASS_GAINHF, directCutoff);
        AL11.alSourcei(source, AL_DIRECT_FILTER, directFilter);
        ClientUtil.checkAlErrors("Setting direct filter parameters");

        if (maxAuxSends >= 1) {
            EXTEfx.alFilterf(sendFilter0, AL_LOWPASS_GAIN, sendGain0);
            EXTEfx.alFilterf(sendFilter0, AL_LOWPASS_GAINHF, sendCutoff0);
            AL11.alSource3i(source, AL_AUXILIARY_SEND_FILTER, auxFXSlot0, 0, sendFilter0);
            ClientUtil.checkAlErrors("Setting send filter 0 parameters");
        }

        if (maxAuxSends >= 2) {
            EXTEfx.alFilterf(sendFilter1, AL_LOWPASS_GAIN, sendGain1);
            EXTEfx.alFilterf(sendFilter1, AL_LOWPASS_GAINHF, sendCutoff1);
            AL11.alSource3i(source, AL_AUXILIARY_SEND_FILTER, auxFXSlot1, 1, sendFilter1);
            ClientUtil.checkAlErrors("Setting send filter 1 parameters");
        }

        if (maxAuxSends >= 3) {
            EXTEfx.alFilterf(sendFilter2, AL_LOWPASS_GAIN, sendGain2);
            EXTEfx.alFilterf(sendFilter2, AL_LOWPASS_GAINHF, sendCutoff2);
            AL11.alSource3i(source, AL_AUXILIARY_SEND_FILTER, auxFXSlot2, 2, sendFilter2);
            ClientUtil.checkAlErrors("Setting send filter 2 parameters");
        }
    }

    private static void setEchoParameters(int effect, int auxFXSlot, float delay, float damping) {
        EXTEfx.alEffectf(effect, AL_ECHO_DELAY, delay);
        ClientUtil.checkAlErrors("Setting echo parameter delay to " + delay);
        EXTEfx.alEffectf(effect, AL_ECHO_LRDELAY, delay);
        ClientUtil.checkAlErrors("Setting echo parameter LR delay to " + delay);
        EXTEfx.alEffectf(effect, AL_ECHO_DAMPING, damping);
        ClientUtil.checkAlErrors("Setting echo parameter damping to " + damping);

        EXTEfx.alAuxiliaryEffectSloti(auxFXSlot, AL_EFFECTSLOT_EFFECT, effect);
    }

    private static void setReverbParameters(int effect, int auxFXSlot, ReverbParams params) {
        EXTEfx.alEffectf(effect, AL_EAXREVERB_DENSITY, params.density());
        ClientUtil.checkAlErrors("Setting reverb parameter density to " + params.density());
        EXTEfx.alEffectf(effect, AL_EAXREVERB_DIFFUSION, params.diffusion());
        ClientUtil.checkAlErrors("Setting reverb parameter diffusion to " + params.diffusion());
        EXTEfx.alEffectf(effect, AL_EAXREVERB_GAIN, params.gain());
        ClientUtil.checkAlErrors("Setting reverb parameter gain to " + params.gain());
        EXTEfx.alEffectf(effect, AL_EAXREVERB_GAINHF, params.gainHF());
        ClientUtil.checkAlErrors("Setting reverb parameter gainHF to " + params.gainHF());
        EXTEfx.alEffectf(effect, AL_EAXREVERB_DECAY_TIME, params.decayTime());
        ClientUtil.checkAlErrors("Setting reverb parameter decayTime to " + params.decayTime());
        EXTEfx.alEffectf(effect, AL_EAXREVERB_DECAY_HFRATIO, params.decayHFRatio());
        ClientUtil.checkAlErrors("Setting reverb parameter decayHFRatio to " + params.decayHFRatio());
        EXTEfx.alEffectf(effect, AL_EAXREVERB_REFLECTIONS_GAIN, params.reflectionsGain());
        ClientUtil.checkAlErrors("Setting reverb parameter reflectionsGain to " + params.reflectionsGain());
        EXTEfx.alEffectf(effect, AL_EAXREVERB_REFLECTIONS_DELAY, params.reflectionsDelay());
        ClientUtil.checkAlErrors("Setting reverb parameter reflectionsDelay to " + params.reflectionsDelay());
        EXTEfx.alEffectf(effect, AL_EAXREVERB_LATE_REVERB_GAIN, params.lateReverbGain());
        ClientUtil.checkAlErrors("Setting reverb parameter lateReverbGain to " + params.lateReverbGain());
        EXTEfx.alEffectf(effect, AL_EAXREVERB_LATE_REVERB_DELAY, params.lateReverbDelay());
        ClientUtil.checkAlErrors("Setting reverb parameter lateReverbDelay to " + params.lateReverbDelay());
        EXTEfx.alEffectf(effect, AL_EAXREVERB_ECHO_TIME, params.echoTime());
        ClientUtil.checkAlErrors("Setting reverb parameter echoTime to " + params.echoTime());
        EXTEfx.alEffectf(effect, AL_EAXREVERB_ECHO_DEPTH, params.echoDepth());
        ClientUtil.checkAlErrors("Setting reverb parameter echoDepth to " + params.echoDepth());
        EXTEfx.alEffectf(effect, AL_EAXREVERB_ROOM_ROLLOFF_FACTOR, params.roomRolloffFactor());
        ClientUtil.checkAlErrors("Setting reverb parameter roomRolloffFactor to " + params.roomRolloffFactor());
        EXTEfx.alEffectf(effect, AL_EAXREVERB_AIR_ABSORPTION_GAINHF, params.airAbsorptionGainHF());
        ClientUtil.checkAlErrors("Setting reverb parameter airAbsorptionGainHF to " + params.airAbsorptionGainHF());

        EXTEfx.alAuxiliaryEffectSloti(auxFXSlot, AL_EFFECTSLOT_EFFECT, effect);
    }

    private static void setEqualizerParameters(int effect, int auxFXSlot, EqualizerParams params) {
        EXTEfx.alEffectf(effect, AL_EQUALIZER_LOW_GAIN, params.lowGain());
        ClientUtil.checkAlErrors("Setting equalizer parameter lowGain to " + params.lowGain());
        EXTEfx.alEffectf(effect, AL_EQUALIZER_MID1_GAIN, params.mid1Gain());
        ClientUtil.checkAlErrors("Setting equalizer parameter mid1Gain to " + params.mid1Gain());
        EXTEfx.alEffectf(effect, AL_EQUALIZER_MID1_WIDTH, params.mid1Width());
        ClientUtil.checkAlErrors("Setting equalizer parameter mid1Width to " + params.mid1Width());
        EXTEfx.alEffectf(effect, AL_EQUALIZER_MID2_GAIN, params.mid2Gain());
        ClientUtil.checkAlErrors("Setting equalizer parameter mid2Gain to " + params.mid2Gain());
        EXTEfx.alEffectf(effect, AL_EQUALIZER_MID2_WIDTH, params.mid2Width());
        ClientUtil.checkAlErrors("Setting equalizer parameter mid2Width to " + params.mid2Width());
        EXTEfx.alEffectf(effect, AL_EQUALIZER_HIGH_GAIN, params.highGain());
        ClientUtil.checkAlErrors("Setting equalizer parameter highGain to " + params.highGain());

        EXTEfx.alAuxiliaryEffectSloti(auxFXSlot, AL_EFFECTSLOT_EFFECT, effect);
    }

    private static void setDistortionParameters(int effect, int auxFXSlot, float edge) {
        EXTEfx.alEffectf(effect, AL_DISTORTION_EDGE, edge);
        ClientUtil.checkAlErrors("Setting distortion parameter edge to " + edge);

        EXTEfx.alAuxiliaryEffectSloti(auxFXSlot, AL_EFFECTSLOT_EFFECT, effect);
    }
}
