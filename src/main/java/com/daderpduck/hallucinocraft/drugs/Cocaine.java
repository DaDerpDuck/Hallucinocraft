package com.daderpduck.hallucinocraft.drugs;

import net.minecraft.entity.player.PlayerEntity;

public class Cocaine extends Drug {
    public Cocaine(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void renderTick(float effect) {
        DrugEffects.SATURATION.addValue(effect*-0.5F);
        DrugEffects.CAMERA_TREMBLE.addValue(effect*5F);
        DrugEffects.BUMPY.addValue(effect*320.0F);
        super.renderTick(effect);
    }

    @Override
    public void effectTick(PlayerEntity player, float effect) {
        DrugEffects.MOVEMENT_SPEED.addValue(effect*1.1F);
        DrugEffects.DIG_SPEED.addValue(effect*1.1F);
        super.effectTick(player, effect);
    }
}
