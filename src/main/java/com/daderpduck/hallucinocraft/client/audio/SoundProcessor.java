package com.daderpduck.hallucinocraft.client.audio;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.client.ClientUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.EXTEfx;

import static org.lwjgl.openal.AL10.AL_TRUE;
import static org.lwjgl.openal.EXTEfx.*;

public class SoundProcessor {
    private static int auxFXSlot;
    private static int dryFilter;

    public static void init() {
        long currentContext = ALC10.alcGetCurrentContext();
        long currentDevice = ALC10.alcGetContextsDevice(currentContext);
        if (!ALC10.alcIsExtensionPresent(currentDevice, "ALC_EXT_EFX")) {
            Hallucinocraft.LOGGER.error("EFX Extension not present on current device");
            return;
        }

        auxFXSlot = EXTEfx.alGenAuxiliaryEffectSlots();
        EXTEfx.alAuxiliaryEffectSloti(auxFXSlot, AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL_TRUE);
        ClientUtil.checkAlErrors("Creating auxiliary effect slots");

        dryFilter = EXTEfx.alGenFilters();
        EXTEfx.alFilteri(dryFilter, AL_FILTER_TYPE, AL_FILTER_LOWPASS);
        ClientUtil.checkAlErrors("Creating filters");
    }

    public static void cleanup() {
        EXTEfx.alDeleteAuxiliaryEffectSlots(auxFXSlot);
        EXTEfx.alDeleteFilters(dryFilter);
    }

    public static void processSound(int source) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            setParameters(source, 1F, 1F);
            return;
        }

        setParameters(source, (float)Math.pow(0.1F, 0.1D), 0.1F);
    }

    private static void setParameters(int source, float gain, float cutoff) {
        EXTEfx.alFilterf(dryFilter, AL_LOWPASS_GAIN, gain);
        EXTEfx.alFilterf(dryFilter, AL_LOWPASS_GAINHF, cutoff);
        AL11.alSourcei(source, AL_DIRECT_FILTER, dryFilter);
        ClientUtil.checkAlErrors("Setting parameters");
    }
}
