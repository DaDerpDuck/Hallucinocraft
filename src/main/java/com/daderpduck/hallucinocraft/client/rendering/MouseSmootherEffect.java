package com.daderpduck.hallucinocraft.client.rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.MouseSmoother;
import net.minecraft.client.util.NativeUtil;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MouseSmootherEffect {
    public static final MouseSmootherEffect INSTANCE = new MouseSmootherEffect();
    private final MouseSmoother smoothTurnX = new MouseSmoother();
    private final MouseSmoother smoothTurnY = new MouseSmoother();
    private double lastMouseEventTime = Double.MIN_VALUE;
    private double amplifier = 0;
    private double smoothedX = 0;
    private double smoothedY = 0;

    private MouseSmootherEffect() {
    }

    public void setAmplifier(double amplifier) {
        this.amplifier = amplifier;
    }

    public double getAmplifier() {
        return amplifier;
    }

    public void tick(double deltaX, double deltaY) {
        Minecraft minecraft = Minecraft.getInstance();
        double time = NativeUtil.getTime();
        double deltaTime = time - lastMouseEventTime;
        lastMouseEventTime = time;

        if (minecraft.mouseHandler.isMouseGrabbed() && minecraft.isWindowActive()) {
            double d0 = minecraft.options.sensitivity * 0.6D + 0.2D;
            double d1 = d0 * d0 * d0 * 8.0D;

            double amplifier = getAmplifier();
            double t = amplifier * amplifier * amplifier;
            double lerped = MathHelper.lerp(t, 1.0D, deltaTime*d1);

            smoothedX = smoothTurnX.getNewDeltaValue(deltaX*d1, lerped);
            smoothedY = smoothTurnY.getNewDeltaValue(deltaY*d1, lerped);
        }
    }

    public double getX() {
        return smoothedX;
    }

    public double getY() {
        return smoothedY;
    }
}
