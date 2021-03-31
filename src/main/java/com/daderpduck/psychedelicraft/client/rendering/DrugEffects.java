package com.daderpduck.psychedelicraft.client.rendering;

import net.minecraft.util.math.MathHelper;

public enum DrugEffects {
    CAMERA_TREMBLE,
    CAMERA_INERTIA,
    HAND_TREMBLE,
    MOVEMENT_SPEED,
    DIG_SPEED,
    BIG_WAVES(true),
    SMALL_WAVES(true);


    private float value;
    private final boolean normalized;

    DrugEffects() {
        this(false);
    }

    DrugEffects(boolean normalized) {
        this.normalized = normalized;
    }

    public void addValue(float value) {
        this.value += value;
        if (this.normalized) this.value = MathHelper.clamp(this.value, 0, 1);
    }

    public float getValue() {
        return value;
    }

    void resetValue() {
        this.value = 0;
    }
}
