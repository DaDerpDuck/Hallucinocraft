package com.daderpduck.hallucinocraft.drugs;

import net.minecraft.entity.player.PlayerEntity;

public class Cannabis extends Drug {
    public Cannabis(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void renderTick(float effect) {
        DrugEffects.SATURATION.addValue(effect);
        super.renderTick(effect);
    }

    @Override
    public void effectTick(PlayerEntity player, float effect) {
        DrugEffects.MOVEMENT_SPEED.addValue(effect*-0.2F);
        DrugEffects.DIG_SPEED.addValue(effect*-0.2F);
        super.effectTick(player, effect);
    }
}
