package com.daderpduck.hallucinocraft.drugs;

import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class DrugEffects {
    private final List<DrugEffect> EFFECTS = new ArrayList<>();

    // Visual Effects
    public final FloatDrugEffect CAMERA_TREMBLE = registerClient();
    public final FloatDrugEffect CAMERA_INERTIA = registerClient();
    public final FloatDrugEffect HAND_TREMBLE = registerClient();
    public final FloatDrugEffect BIG_WAVES = registerClient();
    public final FloatDrugEffect SMALL_WAVES = registerClient();
    public final FloatDrugEffect WIGGLE_WAVES = registerClient();
    public final FloatDrugEffect WORLD_DEFORMATION = registerClient();
    public final FloatDrugEffect KALEIDOSCOPE_INTENSITY = registerClient();
    public final FloatDrugEffect SATURATION = registerClient();
    public final FloatDrugEffect HUE_AMPLITUDE = registerClient();
    public final FloatDrugEffect BUMPY = registerClient();
    public final FloatDrugEffect BRIGHTNESS = registerClient();
    public final FloatDrugEffect BLOOM_RADIUS = registerClient();
    public final FloatDrugEffect BLOOM_THRESHOLD = registerClient();
    public final FloatDrugEffect RECURSION = registerClient();
    public final FloatDrugEffect WATER_DISTORT = registerClient();
    public final FloatDrugEffect GLITCH = registerClient();
    public final FloatDrugEffect FOG_DENSITY = registerClient();
    public final FloatDrugEffect FOG_DARKEN = registerClient();
    public final FloatDrugEffect BLUR = registerClient();

    // Input Effects
    public final FloatDrugEffect MOUSE_SENSITIVITY_SCALE = registerClient(1F);

    // Audio Effects
    public final FloatDrugEffect MUFFLE = registerClient();
    public final FloatDrugEffect ECHO = registerClient();
    public final FloatDrugEffect REVERB = registerClient();
    public final FloatDrugEffect PITCH_RANDOM_SCALE = registerClient();

    // Attribute Effects
    public final FloatDrugEffect MOVEMENT_SPEED = register();
    public final FloatDrugEffect DIG_SPEED = register();
    public final FloatDrugEffect DROWN_RATE = register();
    public final FloatDrugEffect REGENERATION_RATE = register();
    public final FloatDrugEffect HUNGER_RATE = register();


    private FloatDrugEffect register(boolean isClientOnly, float defaultValue) {
        FloatDrugEffect floatDrugEffect = new FloatDrugEffect(isClientOnly, defaultValue);
        EFFECTS.add(floatDrugEffect);
        return floatDrugEffect;
    }

    private FloatDrugEffect register() {
        return register(false, 0);
    }

    private FloatDrugEffect register(float defaultValue) {
        return register(false, defaultValue);
    }

    private FloatDrugEffect registerClient() {
        return register(true, 0);
    }

    private FloatDrugEffect registerClient(float defaultValue) {
        return register(true, defaultValue);
    }

    public void reset(boolean clientOnly) {
        for (DrugEffect effect : EFFECTS) {
            if (effect.isClientOnly() == clientOnly) effect.resetValue();
        }
    }

    private interface DrugEffect {
        boolean isClientOnly();
        void resetValue();
    }

    public static class FloatDrugEffect implements DrugEffect {
        private final boolean isClientOnly;
        private final float defaultValue;
        private float value;

        public FloatDrugEffect(boolean isClientOnly, float defaultValue) {
            this.isClientOnly = isClientOnly;
            this.defaultValue = defaultValue;
            this.value = defaultValue;
        }

        public void addValue(float valueIn) {
            value += valueIn;
        }

        public float getValue() {
            return value;
        }

        public float getClamped() {
            return Mth.clamp(value, 0, 1);
        }

        public float getClamped(float min, float max) {
            return Mth.clamp(value, min, max);
        }

        @Override
        public boolean isClientOnly() {
            return isClientOnly;
        }

        @Override
        public void resetValue() {
            value = defaultValue;
        }
    }
}
