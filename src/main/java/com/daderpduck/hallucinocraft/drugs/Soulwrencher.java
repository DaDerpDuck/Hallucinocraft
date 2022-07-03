package com.daderpduck.hallucinocraft.drugs;

public class Soulwrencher extends Drug {
    public Soulwrencher(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void renderTick(DrugEffects drugEffects, float effect) {
        drugEffects.GLITCH.addValue(effect);
    }
}
