package com.daderpduck.hallucinocraft.drugs;

public class SoulWrencher extends Drug {
    public SoulWrencher(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void renderTick(DrugEffects drugEffects, float effect) {
        drugEffects.GLITCH.addValue(effect);
        drugEffects.PITCH_RANDOM_SCALE.addValue(effect);
        drugEffects.DISTORTION.addValue(effect);
        drugEffects.SOUL_WRENCHER_AMBIENCE.addValue(effect);
    }
}
