package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.client.rendering.MouseSmootherEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MixinMouseHandler {
    @Shadow private double accumulatedDX;
    @Shadow private double accumulatedDY;

    @Inject(at = @At("HEAD"), method = "turnPlayer()V")
    private void onTurn(CallbackInfo ci) {
        if (MouseSmootherEffect.INSTANCE.getAmplifier() > 0) {
            MouseSmootherEffect.INSTANCE.tick(accumulatedDX, accumulatedDY);
        } else {
            MouseSmootherEffect.INSTANCE.reset();
        }
    }

    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHandler;accumulatedDX:D", ordinal = 1), method = "turnPlayer()V")
    private double getAccumulatedDX(MouseHandler mouseHelper) {
        if (MouseSmootherEffect.INSTANCE.getAmplifier() > 0) {
            double d4 = Minecraft.getInstance().options.sensitivity * 0.6D + 0.2D;
            double d5 = d4 * d4 * d4 * 8.0D;
            return MouseSmootherEffect.INSTANCE.getX()/d5;
        } else {
            return accumulatedDX;
        }
    }

    @Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHandler;accumulatedDY:D", ordinal = 1), method = "turnPlayer()V")
    private double getAccumulatedDY(MouseHandler mouseHelper) {
        if (MouseSmootherEffect.INSTANCE.getAmplifier() > 0) {
            double d4 = Minecraft.getInstance().options.sensitivity * 0.6D + 0.2D;
            double d5 = d4 * d4 * d4 * 8.0D;
            return MouseSmootherEffect.INSTANCE.getY()/d5;
        } else {
            return accumulatedDY;
        }
    }
}
