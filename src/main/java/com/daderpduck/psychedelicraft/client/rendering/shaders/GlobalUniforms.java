package com.daderpduck.psychedelicraft.client.rendering.shaders;

import com.daderpduck.psychedelicraft.Psychedelicraft;
import com.daderpduck.psychedelicraft.events.hooks.SetCameraEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Psychedelicraft.MOD_ID)
public class GlobalUniforms {
    public static final Matrix4f modelView = new Matrix4f();
    public static final Matrix4f modelViewInverse = new Matrix4f();
    public static float timePassed = 0;

    static {
        modelView.setIdentity();
        modelViewInverse.setIdentity();
    }

    @SubscribeEvent
    public static void onRenderStart(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            timePassed = event.renderTickTime + Minecraft.getInstance().gui.getGuiTicks();
        }
    }

    @SubscribeEvent
    public static void onCameraPostSetup(SetCameraEvent event) {
        Matrix4f matrix4f = event.matrixStack.last().pose().copy();
        modelView.set(matrix4f);
        matrix4f.invert();
        modelViewInverse.set(matrix4f);
    }
}
