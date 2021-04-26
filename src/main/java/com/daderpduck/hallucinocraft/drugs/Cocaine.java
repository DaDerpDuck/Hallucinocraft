package com.daderpduck.hallucinocraft.drugs;

import net.minecraft.entity.player.PlayerEntity;

public class Cocaine extends Drug {
    public Cocaine(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void renderTick(DrugEffects drugEffects, float effect) {
        drugEffects.SATURATION.addValue(effect*-0.5F);
        drugEffects.CAMERA_TREMBLE.addValue(effect*5F);
        drugEffects.BUMPY.addValue(effect*320.0F);
    }

    @Override
    public void effectTick(PlayerEntity player, DrugEffects drugEffects, float effect) {
        drugEffects.MOVEMENT_SPEED.addValue(effect*1.1F);
    }
}
