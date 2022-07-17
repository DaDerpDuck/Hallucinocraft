package com.daderpduck.hallucinocraft.drugs;

import net.minecraft.world.entity.player.Player;

/*
Cannabis generally produces a happy and relaxed state, hallucinations, and increased appetite (https://www.healthline.com/health/what-does-it-feel-like-to-be-high)
Music also sounds better when on a high (https://www.quora.com/Why-does-music-sound-better-when-youre-stoned)
To replicate visual effects, saturation and bloom is added for a happy state, camera inertia and movement slowdown for a relaxed state, hunger increase for increased appetite
To replicate audio effects, treble is accentuated for higher clarity
 */
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
        drugEffects.TREBLE.addValue(effect*0.6F);
        drugEffects.REVERB.addValue(effect*0.1F);
    }

    @Override
    public void effectTick(Player player, DrugEffects drugEffects, float effect) {
        drugEffects.MOVEMENT_SPEED.addValue(effect*-0.2F);
        drugEffects.DIG_SPEED.addValue(effect*-0.2F);
        drugEffects.HUNGER_RATE.addValue(effect*1.3F);
        if (effect > 0.5F) drugEffects.REGENERATION_RATE.addValue((effect - 0.5F)*0.1F);
    }
}
