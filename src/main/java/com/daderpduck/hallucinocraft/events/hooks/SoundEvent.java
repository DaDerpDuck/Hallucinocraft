package com.daderpduck.hallucinocraft.events.hooks;

import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;
import java.util.Objects;

public class SoundEvent extends Event {
    public final Vec3 position;

    private SoundEvent(@Nullable Vec3 position) {
        this.position = Objects.requireNonNullElse(position, Vec3.ZERO);
    }

    public static class Play extends SoundEvent {
        public final int source;
        public Play(@Nullable Vec3 position, int source) {
            super(position);
            this.source = source;
        }
    }

    @Cancelable
    public static class SetPitch extends SoundEvent {
        public float pitch;

        public SetPitch(@Nullable Vec3 position, float pitch) {
            super(position);
            this.pitch = pitch;
        }
    }
}
