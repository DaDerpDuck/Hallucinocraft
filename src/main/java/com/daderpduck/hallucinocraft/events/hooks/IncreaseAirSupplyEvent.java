package com.daderpduck.hallucinocraft.events.hooks;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class IncreaseAirSupplyEvent extends Event {
    public final LivingEntity livingEntity;

    public IncreaseAirSupplyEvent(LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }
}
