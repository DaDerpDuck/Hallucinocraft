package com.daderpduck.hallucinocraft.drugs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class Morphine extends Drug {
    public Morphine(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void startUse(PlayerEntity player) {
        Effects.HEAL.applyInstantenousEffect(player, player, player, 2, 1D);
    }

    @Override
    public void effectTick(PlayerEntity player, DrugEffects drugEffects, float effect) {
        player.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 35, 0, true, true));
        if (effect == 1F) drugEffects.DROWN_RATE.addValue(3F);
    }
}
