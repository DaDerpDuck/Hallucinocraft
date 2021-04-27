package com.daderpduck.hallucinocraft.events.hooks;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;

@OnlyIn(Dist.CLIENT)
public class RenderEvent extends Event {
    public enum Phase {
        START, END
    }

    public final Phase phase;

    public RenderEvent(Phase phase) {
        this.phase = phase;
    }

    public static class RenderTerrainEvent extends RenderEvent {
        public final RenderType blockLayer;
        public final MatrixStack matrixStack;

        public RenderTerrainEvent(Phase phase, RenderType blockLayerIn, MatrixStack matrixStackIn) {
            super(phase);
            this.blockLayer = blockLayerIn;
            this.matrixStack = matrixStackIn;
        }
    }

    public static class RenderEntityEvent extends RenderEvent {
        public final MatrixStack matrixStackIn;

        public RenderEntityEvent(Phase phase, MatrixStack matrixStackIn) {
            super(phase);
            this.matrixStackIn = matrixStackIn;
        }
    }

    public static class RenderBlockEntityEvent extends RenderEvent {
        public final MatrixStack matrixStackIn;

        public RenderBlockEntityEvent(Phase phase, MatrixStack matrixStackIn) {
            super(phase);
            this.matrixStackIn = matrixStackIn;
        }
    }

    public static class RenderBlockOutlineEvent extends RenderEvent {
        public final IRenderTypeBuffer.Impl buffer;

        public RenderBlockOutlineEvent(Phase phase, IRenderTypeBuffer.Impl buffer) {
            super(phase);
            this.buffer = buffer;
        }
    }

    public static class RenderParticlesEvent extends RenderEvent {
        public final MatrixStack matrixStackIn;

        public RenderParticlesEvent(Phase phase, MatrixStack matrixStackIn) {
            super(phase);
            this.matrixStackIn = matrixStackIn;
        }
    }
}
