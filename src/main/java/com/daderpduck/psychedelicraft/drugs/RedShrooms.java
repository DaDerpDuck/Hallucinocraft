package com.daderpduck.psychedelicraft.drugs;

import com.daderpduck.psychedelicraft.client.rendering.DrugEffects;

public class RedShrooms extends Drug {
    public RedShrooms(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void renderTick(DrugInstance drugInstance, float partialTicks) {
        DrugEffects.BIG_WAVES.addValue(drugInstance.getCurrentEffect()*0.3F);
        DrugEffects.SMALL_WAVES.addValue(drugInstance.getCurrentEffect()*0.4F);
    }
}
