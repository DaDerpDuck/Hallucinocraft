package com.daderpduck.hallucinocraft.drugs;

import net.minecraft.entity.player.PlayerEntity;

public class Cocaine extends Drug {
    public Cocaine(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void renderTick(float effect) {
        DrugEffects.SATURATION.addValue(effect*-0.1F);
        DrugEffects.CAMERA_TREMBLE.addValue(effect);
        super.renderTick(effect);
    }

    @Override
    public void effectTick(PlayerEntity player, float effect) {
        DrugEffects.MOVEMENT_SPEED.addValue(effect*0.3F);
        DrugEffects.DIG_SPEED.addValue(effect*0.3F);
        super.effectTick(player, effect);
    }
}
