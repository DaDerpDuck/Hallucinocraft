package com.daderpduck.hallucinocraft.events.hooks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.eventbus.api.Event;

public class BobHurtEvent extends Event {
    public final PoseStack matrixStack;
    public final float partialTicks;

    public BobHurtEvent(PoseStack matrixStack, float partialTicks) {
        this.matrixStack = matrixStack;
        this.partialTicks = partialTicks;
    }
}
