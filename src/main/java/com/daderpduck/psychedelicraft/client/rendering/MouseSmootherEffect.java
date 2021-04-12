package com.daderpduck.psychedelicraft.client.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.MouseSmoother;
import net.minecraft.client.util.NativeUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MouseSmootherEffect {
    private static final float EPSILON = 1E-6F;
    private final MouseSmoother smoothTurnX = new MouseSmoother();
    private final MouseSmoother smoothTurnY = new MouseSmoother();
    private double lastMouseEventTime = Double.MIN_VALUE;

    public void tick(float amplifier, double deltaX, double deltaY) {
        Minecraft minecraft = Minecraft.getInstance();
        double time = NativeUtil.getTime();
        double deltaTime = time - lastMouseEventTime;
        lastMouseEventTime = time;

        if (minecraft.mouseHandler.isMouseGrabbed() && minecraft.isWindowActive()) {
            double d0 = minecraft.options.sensitivity * 0.6D + 0.2D;
            double d1 = d0 * d0 * d0 * 8.0D;
            double smoothedX = smoothTurnX.getNewDeltaValue(deltaX*d1, deltaTime*d1);
            double smoothedY = smoothTurnY.getNewDeltaValue(deltaY*d1, deltaTime*d1);

            if (minecraft.player != null && amplifier > EPSILON) {
                minecraft.player.turn(amplifier*smoothedX, amplifier*smoothedY*(minecraft.options.invertYMouse ? -1D : 1D));
            }
        }
    }
}
