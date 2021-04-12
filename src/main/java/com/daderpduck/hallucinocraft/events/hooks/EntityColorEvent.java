package com.daderpduck.hallucinocraft.events.hooks;

import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

/**
 * Hooks into {@link LivingRenderer#render}
 */
@OnlyIn(Dist.CLIENT)
public class EntityColorEvent extends Event {
    public final LivingEntity entity;
    public final float r;
    public final float g;
    public final float b;
    public final float a;

    public EntityColorEvent(LivingEntity entity, float r, float g, float b, float a) {
        this.entity = entity;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
}
