package com.daderpduck.hallucinocraft.client.audio;

public record EqualizerParams(
        float lowGain,
        float mid1Gain,
        float mid1Width,
        float mid2Gain,
        float mid2Width,
        float highGain) {
}
