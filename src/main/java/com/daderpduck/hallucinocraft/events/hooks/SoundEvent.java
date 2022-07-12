package com.daderpduck.hallucinocraft.events.hooks;

import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;
import java.util.Objects;

public class SoundEvent extends Event {
    public final int source;
    public final Vec3 position;

    private SoundEvent(int source, @Nullable Vec3 position) {
        this.source = source;
        this.position = Objects.requireNonNullElse(position, Vec3.ZERO);
    }

    public static class Play extends SoundEvent {
        public Play(int source, @Nullable Vec3 position) {
            super(source, position);
        }
    }

    @Cancelable
    public static class SetPitch extends SoundEvent {
        public final float pitch;

        public SetPitch(int source, @Nullable Vec3 position, float pitch) {
            super(source, position);
            this.pitch = pitch;
        }
    }
}
