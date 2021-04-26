package com.daderpduck.hallucinocraft.drugs;

import net.minecraft.entity.player.PlayerEntity;

public class Cannabis extends Drug {
    public Cannabis(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void renderTick(DrugEffects drugEffects, float effect) {
        drugEffects.SATURATION.addValue(effect*2F);
        drugEffects.BLOOM_RADIUS.addValue(effect*15.0F);
        drugEffects.BLOOM_THRESHOLD.addValue(effect);
        drugEffects.CAMERA_INERTIA.addValue(effect);
    }

    @Override
    public void effectTick(PlayerEntity player, DrugEffects drugEffects, float effect) {
        drugEffects.MOVEMENT_SPEED.addValue(effect*-0.2F);
        drugEffects.DIG_SPEED.addValue(effect*-0.2F);
        drugEffects.HUNGER_RATE.addValue(effect);
    }
}
