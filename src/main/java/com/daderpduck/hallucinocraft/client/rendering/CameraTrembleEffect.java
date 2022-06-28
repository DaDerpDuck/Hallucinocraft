package com.daderpduck.hallucinocraft.client.rendering;

import com.daderpduck.hallucinocraft.client.rendering.shaders.GlobalUniforms;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class CameraTrembleEffect {
    private final Random random = new Random();
    private double amplitude = 0;

    public void setAmplitude(double amplitudeIn) {
        amplitude = amplitudeIn;
    }

    public void tick(PoseStack cameraMatrixStack) {
        /*double shiftX = random.nextDouble() - 0.5D;
        double shiftY = MathHelper.sin(GlobalUniforms.timePassed * 7.0F);

        cameraMatrixStack.translate(shiftX*amplitude*0.05D, shiftY*shiftY*amplitude*0.1D, 0);*/

        double shiftY = Mth.sin(GlobalUniforms.timePassed * 7.0F);
        cameraMatrixStack.translate(0, shiftY*shiftY*amplitude*0.1D, 0);
    }
}
