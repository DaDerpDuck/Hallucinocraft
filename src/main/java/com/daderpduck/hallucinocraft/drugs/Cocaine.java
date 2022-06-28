package com.daderpduck.hallucinocraft.drugs;

import net.minecraft.world.entity.player.Player;

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
    public void effectTick(Player player, DrugEffects drugEffects, float effect) {
        drugEffects.MOVEMENT_SPEED.addValue(effect*1.1F);
        if (effect == 1.0) drugEffects.DROWN_RATE.addValue(1F);
    }

    @Override
    public void abuseTick(Player player, DrugEffects drugEffects, int abuse) {
        if (abuse > 8000) {
            drugEffects.MOVEMENT_SPEED.addValue(-0.001F*(float)(abuse - 8000));
        }
        if (abuse > 12000) {
            drugEffects.DROWN_RATE.addValue(1F);
        }
    }
}
