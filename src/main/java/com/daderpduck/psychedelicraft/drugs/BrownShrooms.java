package com.daderpduck.psychedelicraft.drugs;

public class BrownShrooms extends Drug {
    public BrownShrooms(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void renderTick(float effect) {
        DrugEffects.BIG_WAVES.addValue(effect*0.6F);
        DrugEffects.SMALL_WAVES.addValue(effect*0.8F);
        DrugEffects.WIGGLE_WAVES.addValue(effect*0.8F);
        DrugEffects.WORLD_DEFORMATION.addValue(effect*3.0F);
        DrugEffects.KALEIDOSCOPE_INTENSITY.addValue(effect*0.4F);
        DrugEffects.SATURATION.addValue(effect);
        DrugEffects.HUE_AMPLITUDE.addValue(effect*0.8F);
        DrugEffects.CAMERA_TREMBLE.addValue(effect*0.5F);
        super.renderTick(effect);
    }
}
