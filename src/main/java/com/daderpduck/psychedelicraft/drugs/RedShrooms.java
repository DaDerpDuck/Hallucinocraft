package com.daderpduck.psychedelicraft.drugs;

import com.daderpduck.psychedelicraft.client.rendering.DrugEffects;

public class RedShrooms extends Drug {
    public RedShrooms(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void renderTick(DrugInstance drugInstance, float partialTicks) {
        //TODO: Do kaleidoscope, then waves at higher values?
        float effect = drugInstance.getCurrentEffect();
        DrugEffects.BIG_WAVES.addValue(effect*0.3F);
        DrugEffects.SMALL_WAVES.addValue(effect*0.4F);
        DrugEffects.KALEIDOSCOPE_INTENSITY.addValue(effect*0.15F);
    }
}
