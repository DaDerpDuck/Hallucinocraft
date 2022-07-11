package com.daderpduck.hallucinocraft.drugs;

import net.minecraft.world.entity.player.Player;

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
        drugEffects.MUFFLE.addValue(effect*0.4F);
        drugEffects.REVERB.addValue(effect*0.6F);
    }

    @Override
    public void effectTick(Player player, DrugEffects drugEffects, float effect) {
        drugEffects.MOVEMENT_SPEED.addValue(effect*-0.2F);
        drugEffects.DIG_SPEED.addValue(effect*-0.2F);
        drugEffects.HUNGER_RATE.addValue(effect*1.3F);
        if (effect > 0.5F) drugEffects.REGENERATION_RATE.addValue((effect - 0.5F)*0.1F);
    }
}
