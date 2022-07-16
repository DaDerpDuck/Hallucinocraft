package com.daderpduck.hallucinocraft.sounds;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;

public class DrugLoopSoundInstance extends AbstractTickableSoundInstance {
    public DrugLoopSoundInstance(SoundEvent soundEvent) {
        super(soundEvent, SoundSource.AMBIENT);
        this.looping = true;
        this.delay = 0;
        this.volume = 1.0F;
        this.relative = true;
    }

    public void setVolume(float volume) {
        this.volume = Mth.clamp(volume, 0F, 1F);
    }

    @Override
    public void tick() {
        if (this.volume == 0) this.stop();
    }
}
