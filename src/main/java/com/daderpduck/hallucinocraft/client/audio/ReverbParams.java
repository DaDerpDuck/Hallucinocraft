package com.daderpduck.hallucinocraft.client.audio;

public record ReverbParams(
        float density,
        float diffusion,
        float gain,
        float gainHF,
        float decayTime,
        float decayHFRatio,
        float reflectionsGain,
        float reflectionsDelay,
        float lateReverbGain,
        float lateReverbDelay,
        float echoTime,
        float echoDepth,
        float roomRolloffFactor,
        float airAbsorptionGainHF) {
}
