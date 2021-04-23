package com.daderpduck.hallucinocraft.drugs;

import net.minecraft.util.math.MathHelper;

public enum DrugEffects {
    CAMERA_TREMBLE(true),
    CAMERA_INERTIA(true),
    HAND_TREMBLE(true),
    MOVEMENT_SPEED(false),
    DIG_SPEED(false),
    BIG_WAVES(true),
    SMALL_WAVES(true),
    WIGGLE_WAVES(true),
    WORLD_DEFORMATION(true),
    KALEIDOSCOPE_INTENSITY(true),
    SATURATION(true),
    HUE_AMPLITUDE(true),
    BUMPY(true),
    BRIGHTNESS(true),
    BLOOM_RADIUS(true),
    BLOOM_THRESHOLD(true),
    RECURSION(true);


    private float value;
    private final boolean clientOnly;

    DrugEffects(boolean clientOnly) {
        this.clientOnly = clientOnly;
    }

    public boolean isClientOnly() {
        return clientOnly;
    }

    public void addValue(float valueIn) {
        value += valueIn;
    }

    public float getValue() {
        return value;
    }

    public float getClamped() {
        return MathHelper.clamp(value, 0, 1);
    }

    public void resetValue() {
        value = 0;
    }
}
