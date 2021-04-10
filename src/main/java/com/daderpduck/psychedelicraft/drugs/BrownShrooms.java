package com.daderpduck.psychedelicraft.drugs;

import com.daderpduck.psychedelicraft.client.rendering.DrugEffects;

public class BrownShrooms extends Drug {
    public BrownShrooms(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void renderTick(float effect, float partialTicks) {
        DrugEffects.BIG_WAVES.addValue(effect*0.6F);
        DrugEffects.SMALL_WAVES.addValue(effect*0.8F);
        DrugEffects.WIGGLE_WAVES.addValue(effect*0.8F);
        DrugEffects.WORLD_DEFORMATION.addValue(effect*3.0F);
        DrugEffects.KALEIDOSCOPE_INTENSITY.addValue(effect);
        DrugEffects.SATURATION.addValue(effect);
    }
}
