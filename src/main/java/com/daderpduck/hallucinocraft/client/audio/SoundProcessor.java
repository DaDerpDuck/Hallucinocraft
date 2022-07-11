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
    private static int auxFXSlot;
    private static int echoEffect;
    private static int sendFilter;

    public static void init() {
        long currentContext = ALC10.alcGetCurrentContext();
        long currentDevice = ALC10.alcGetContextsDevice(currentContext);
        if (!ALC10.alcIsExtensionPresent(currentDevice, "ALC_EXT_EFX")) {
            Hallucinocraft.LOGGER.error("EFX Extension not present on current device");
            return;
        }

        directFilter = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(directFilter, AL_FILTER_TYPE, AL_FILTER_LOWPASS);
        ClientUtil.checkAlErrors("Creating dry filter");

        auxFXSlot = EXTEfx.alGenAuxiliaryEffectSlots();
        EXTEfx.alAuxiliaryEffectSloti(auxFXSlot, AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL_TRUE);
        ClientUtil.checkAlErrors("Creating auxiliary effect slots");

        echoEffect = EXTEfx.alGenEffects();
        EXTEfx.alEffecti(echoEffect, AL_EFFECT_TYPE, AL_EFFECT_ECHO);
        ClientUtil.checkAlErrors("Creating echo effect");

        sendFilter = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(sendFilter, AL_FILTER_TYPE, AL_FILTER_LOWPASS);
        ClientUtil.checkAlErrors("Creating wet filter");
    }

    public static void cleanup() {
        EXTEfx.alDeleteFilters(directFilter);
        EXTEfx.alDeleteAuxiliaryEffectSlots(auxFXSlot);
        EXTEfx.alDeleteEffects(echoEffect);
        EXTEfx.alDeleteFilters(sendFilter);
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
        float directGain = (float)Math.pow(directCutoff, 0.1D);
        float sendCutoff = echoFactor*directCutoff*directCutoff;
        float sendGain = (float)Math.pow(sendCutoff, 0.1D);

        setFilterParameters(source, directGain, directCutoff, sendGain, sendCutoff);
    }

    private static void setDefaultParameters(int source) {
        setFilterParameters(source, 1F, 1F, 0F, 1F);
    }

    private static void setFilterParameters(int source, float directGain, float directCutoff, float sendGain, float sendCutoff) {
        EXTEfx.alFilterf(directFilter, AL_LOWPASS_GAIN, directGain);
        EXTEfx.alFilterf(directFilter, AL_LOWPASS_GAINHF, directCutoff);
        AL11.alSourcei(source, AL_DIRECT_FILTER, directFilter);
        ClientUtil.checkAlErrors("Setting dry filter parameters");

        EXTEfx.alFilterf(sendFilter, AL_LOWPASS_GAIN, sendGain);
        EXTEfx.alFilterf(sendFilter, AL_LOWPASS_GAINHF, sendCutoff);
        AL11.alSource3i(source, AL_AUXILIARY_SEND_FILTER, auxFXSlot, 1, sendFilter);
        ClientUtil.checkAlErrors("Setting wet filter parameters");
    }

    private static void setEchoParameters(float delay, float damping) {
        EXTEfx.alEffectf(echoEffect, AL_ECHO_DELAY, delay);
        ClientUtil.checkAlErrors("Setting echo parameter delay: " + delay);
        EXTEfx.alEffectf(echoEffect, AL_ECHO_LRDELAY, delay);
        ClientUtil.checkAlErrors("Setting echo parameter LR delay: " + delay);
        EXTEfx.alEffectf(echoEffect, AL_ECHO_DAMPING, damping);
        ClientUtil.checkAlErrors("Setting echo parameter damping: " + damping);

        EXTEfx.alAuxiliaryEffectSloti(auxFXSlot, AL_EFFECTSLOT_EFFECT, echoEffect);
    }
}
