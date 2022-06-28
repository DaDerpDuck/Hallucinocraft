package com.daderpduck.hallucinocraft.drugs;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class Morphine extends Drug {
    public Morphine(DrugProperties properties) {
        super(properties);
    }

    @Override
    public void startUse(Player player) {
        MobEffects.HEAL.applyInstantenousEffect(player, player, player, 2, 1D);
    }

    @Override
    public void renderTick(DrugEffects drugEffects, float effect) {
        if (effect > 0.5F) {
            float f = effect - 0.5F;
            drugEffects.WATER_DISTORT.addValue(Math.min(f*f, 0.01F));
            drugEffects.SATURATION.addValue(f);
        }
    }

    @Override
    public void effectTick(Player player, DrugEffects drugEffects, float effect) {
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 35, 0, true, true));
        if (effect == 1F) drugEffects.DROWN_RATE.addValue(3F);
    }
}
