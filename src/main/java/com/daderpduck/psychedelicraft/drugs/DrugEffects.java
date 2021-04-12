package com.daderpduck.psychedelicraft.drugs;

import net.minecraft.util.math.MathHelper;

public enum DrugEffects {
    CAMERA_TREMBLE,
    CAMERA_INERTIA,
    HAND_TREMBLE,
    MOVEMENT_SPEED,
    DIG_SPEED,
    BIG_WAVES,
    SMALL_WAVES,
    WIGGLE_WAVES,
    WORLD_DEFORMATION,
    KALEIDOSCOPE_INTENSITY,
    HUE,
    SATURATION,
    BRIGHTNESS,
    HUE_AMPLITUDE;


    private float value;

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
