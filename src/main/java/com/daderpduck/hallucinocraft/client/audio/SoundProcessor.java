package com.daderpduck.hallucinocraft.client.audio;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.client.ClientUtil;
import com.daderpduck.hallucinocraft.drugs.Drug;
import net.minecraft.client.Minecraft;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.EXTEfx;

import static org.lwjgl.openal.AL10.AL_TRUE;
import static org.lwjgl.openal.EXTEfx.*;

public class SoundProcessor {
    private static int directFilter;
    private static int auxFXSlot0;
    private static int sendFilter0;
    private static int echoEffect;
    private static int auxFXSlot1;
    private static int sendFilter1;
    private static int reverbEffect;


    public static void init() {
        // https://github.com/rtpHarry/Sokoban/blob/master/libraries/OpenAL%201.1%20SDK/docs/Effects%20Extension%20Guide.pdf
        long currentContext = ALC10.alcGetCurrentContext();
        long currentDevice = ALC10.alcGetContextsDevice(currentContext);
        if (!ALC10.alcIsExtensionPresent(currentDevice, "ALC_EXT_EFX")) {
            Hallucinocraft.LOGGER.error("EFX Extension not present on current device");
            return;
        }

        directFilter = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(directFilter, AL_FILTER_TYPE, AL_FILTER_LOWPASS);
        ClientUtil.checkAlErrors("Creating direct filter");

        auxFXSlot0 = EXTEfx.alGenAuxiliaryEffectSlots();
        EXTEfx.alAuxiliaryEffectSloti(auxFXSlot0, AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL_TRUE);
        ClientUtil.checkAlErrors("Creating auxiliary effect slot 0");

        sendFilter0 = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(sendFilter0, AL_FILTER_TYPE, AL_FILTER_LOWPASS);
        ClientUtil.checkAlErrors("Creating send filter 0");

        echoEffect = EXTEfx.alGenEffects();
        EXTEfx.alEffecti(echoEffect, AL_EFFECT_TYPE, AL_EFFECT_ECHO);
        ClientUtil.checkAlErrors("Creating echo effect");

        auxFXSlot1 = EXTEfx.alGenAuxiliaryEffectSlots();
        EXTEfx.alAuxiliaryEffectSloti(auxFXSlot1, AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL_TRUE);
        ClientUtil.checkAlErrors("Creating auxiliary effect slot 1");

        sendFilter1 = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(sendFilter1, AL_FILTER_TYPE, AL_FILTER_LOWPASS);
        ClientUtil.checkAlErrors("Creating send filter 1");

        reverbEffect = EXTEfx.alGenEffects();
        EXTEfx.alEffecti(reverbEffect, AL_EFFECT_TYPE, AL_EFFECT_EAXREVERB);
        ClientUtil.checkAlErrors("Creating reverb effect");
        // idk what i'm doing
        // TODO: fix my incompetence
        setReverbParameters(new ReverbParams(
                0.5F,
                1F,
                0.1F,
                0.89F,
                10F,
                1.2F,
                0F,
                0.2F,
                1F,
                0.02F,
                0.25F,
                1F,
                0.11F,
                0.9F));
    }

    public static void cleanup() {
        EXTEfx.alDeleteFilters(directFilter);
        EXTEfx.alDeleteAuxiliaryEffectSlots(auxFXSlot0);
        EXTEfx.alDeleteFilters(sendFilter0);
        EXTEfx.alDeleteEffects(echoEffect);
        EXTEfx.alDeleteAuxiliaryEffectSlots(auxFXSlot1);
        EXTEfx.alDeleteFilters(sendFilter1);
        EXTEfx.alDeleteEffects(reverbEffect);
    }

    public static void processSound(int source, double x, double y, double z) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || (x == 0 && y == 0 && z == 0)) {
            setDefaultParameters(source);
            return;
        }

        // idk much about audio mixing pls help lmao
        float echoFactor = Drug.getDrugEffects().ECHO.getClamped();
        float delay = echoFactor*0.1F;
        float damping = (1F - echoFactor)*0.4F + 0.5F;
        setEchoParameters(delay, damping);

        float directCutoff = 1F - Drug.getDrugEffects().MUFFLE.getClamped(0F, 0.9F);
        float directGain = (float)Math.pow(directCutoff, 0.1);

        float sendCutoff0 = echoFactor*directCutoff*directCutoff;
        float sendGain0 = (float)Math.pow(sendCutoff0, 0.1);

        float sendCutoff1 = Drug.getDrugEffects().REVERB.getClamped()*directCutoff;
        float sendGain1 = (float)Math.sqrt(sendCutoff1);

        setFilterParameters(source, directGain, directCutoff, sendGain0, sendCutoff0, sendGain1, sendCutoff1);
    }

    private static void setDefaultParameters(int source) {
        setFilterParameters(source, 1F, 1F, 0F, 1F, 0F, 1F);
    }

    private static void setFilterParameters(int source, float directGain, float directCutoff, float sendGain0, float sendCutoff0, float sendGain1, float sendCutoff1) {
        EXTEfx.alFilterf(directFilter, AL_LOWPASS_GAIN, directGain);
        EXTEfx.alFilterf(directFilter, AL_LOWPASS_GAINHF, directCutoff);
        AL11.alSourcei(source, AL_DIRECT_FILTER, directFilter);
        ClientUtil.checkAlErrors("Setting direct filter parameters");

        EXTEfx.alFilterf(sendFilter0, AL_LOWPASS_GAIN, sendGain0);
        EXTEfx.alFilterf(sendFilter0, AL_LOWPASS_GAINHF, sendCutoff0);
        AL11.alSource3i(source, AL_AUXILIARY_SEND_FILTER, auxFXSlot0, 1, sendFilter0);
        ClientUtil.checkAlErrors("Setting send filter 0 parameters");

        EXTEfx.alFilterf(sendFilter1, AL_LOWPASS_GAIN, sendGain1);
        EXTEfx.alFilterf(sendFilter1, AL_LOWPASS_GAINHF, sendCutoff1);
        AL11.alSource3i(source, AL_AUXILIARY_SEND_FILTER, auxFXSlot1, 1, sendFilter1);
        ClientUtil.checkAlErrors("Setting send filter 1 parameters");
    }

    private static void setEchoParameters(float delay, float damping) {
        EXTEfx.alEffectf(echoEffect, AL_ECHO_DELAY, delay);
        ClientUtil.checkAlErrors("Setting echo parameter delay to " + delay);
        EXTEfx.alEffectf(echoEffect, AL_ECHO_LRDELAY, delay);
        ClientUtil.checkAlErrors("Setting echo parameter LR delay to " + delay);
        EXTEfx.alEffectf(echoEffect, AL_ECHO_DAMPING, damping);
        ClientUtil.checkAlErrors("Setting echo parameter damping to " + damping);

        EXTEfx.alAuxiliaryEffectSloti(auxFXSlot0, AL_EFFECTSLOT_EFFECT, echoEffect);
    }

    private static void setReverbParameters(ReverbParams params) {
        EXTEfx.alEffectf(reverbEffect, AL_EAXREVERB_DENSITY, params.density());
        ClientUtil.checkAlErrors("Setting reverb parameter density to " + params.density());
        EXTEfx.alEffectf(reverbEffect, AL_EAXREVERB_DIFFUSION, params.diffusion());
        ClientUtil.checkAlErrors("Setting reverb parameter diffusion to " + params.diffusion());
        EXTEfx.alEffectf(reverbEffect, AL_EAXREVERB_GAIN, params.gain());
        ClientUtil.checkAlErrors("Setting reverb parameter gain to " + params.gain());
        EXTEfx.alEffectf(reverbEffect, AL_EAXREVERB_GAINHF, params.gainHF());
        ClientUtil.checkAlErrors("Setting reverb parameter gainHF to " + params.gainHF());
        EXTEfx.alEffectf(reverbEffect, AL_EAXREVERB_DECAY_TIME, params.decayTime());
        ClientUtil.checkAlErrors("Setting reverb parameter decayTime to " + params.decayTime());
        EXTEfx.alEffectf(reverbEffect, AL_EAXREVERB_DECAY_HFRATIO, params.decayHFRatio());
        ClientUtil.checkAlErrors("Setting reverb parameter decayHFRatio to " + params.decayHFRatio());
        EXTEfx.alEffectf(reverbEffect, AL_EAXREVERB_REFLECTIONS_GAIN, params.reflectionsGain());
        ClientUtil.checkAlErrors("Setting reverb parameter reflectionsGain to " + params.reflectionsGain());
        EXTEfx.alEffectf(reverbEffect, AL_EAXREVERB_REFLECTIONS_DELAY, params.reflectionsDelay());
        ClientUtil.checkAlErrors("Setting reverb parameter reflectionsDelay to " + params.reflectionsDelay());
        EXTEfx.alEffectf(reverbEffect, AL_EAXREVERB_LATE_REVERB_GAIN, params.lateReverbGain());
        ClientUtil.checkAlErrors("Setting reverb parameter lateReverbGain to " + params.lateReverbGain());
        EXTEfx.alEffectf(reverbEffect, AL_EAXREVERB_LATE_REVERB_DELAY, params.lateReverbDelay());
        ClientUtil.checkAlErrors("Setting reverb parameter lateReverbDelay to " + params.lateReverbDelay());
        EXTEfx.alEffectf(reverbEffect, AL_EAXREVERB_ECHO_TIME, params.echoTime());
        ClientUtil.checkAlErrors("Setting reverb parameter echoTime to " + params.echoTime());
        EXTEfx.alEffectf(reverbEffect, AL_EAXREVERB_ECHO_DEPTH, params.echoDepth());
        ClientUtil.checkAlErrors("Setting reverb parameter echoDepth to " + params.echoDepth());
        EXTEfx.alEffectf(reverbEffect, AL_EAXREVERB_ROOM_ROLLOFF_FACTOR, params.roomRolloffFactor());
        ClientUtil.checkAlErrors("Setting reverb parameter roomRolloffFactor to " + params.roomRolloffFactor());
        EXTEfx.alEffectf(reverbEffect, AL_EAXREVERB_AIR_ABSORPTION_GAINHF, params.airAbsorptionGainHF());
        ClientUtil.checkAlErrors("Setting reverb parameter airAbsorptionGainHF to " + params.airAbsorptionGainHF());

        EXTEfx.alAuxiliaryEffectSloti(auxFXSlot1, AL_EFFECTSLOT_EFFECT, reverbEffect);
    }
}
