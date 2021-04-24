package com.daderpduck.hallucinocraft.drugs;

public class RedShrooms extends Drug {
    public RedShrooms(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void renderTick(float effect) {
        DrugEffects.BIG_WAVES.addValue(effect*0.3F);
        DrugEffects.SMALL_WAVES.addValue(effect*0.4F);
        DrugEffects.WIGGLE_WAVES.addValue(effect*0.4F);
        DrugEffects.WORLD_DEFORMATION.addValue(effect*0.7F);
        DrugEffects.SATURATION.addValue(effect*2.0F);
        DrugEffects.HUE_AMPLITUDE.addValue(effect*0.4F);
        DrugEffects.CAMERA_TREMBLE.addValue(effect*0.5F);
        if (effect > 0.7) {
            DrugEffects.KALEIDOSCOPE_INTENSITY.addValue((effect - 0.7F)*15.0F);
        }
        super.renderTick(effect);
    }
}
