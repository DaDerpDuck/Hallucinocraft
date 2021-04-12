package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.events.hooks.PlayerTurnEvent;
import net.minecraft.client.MouseHelper;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHelper.class)
public class MixinMouseHelper {
    @Shadow private double accumulatedDX;
    @Shadow private double accumulatedDY;
    @Shadow private double lastMouseEventTime;

    @Inject(at = @At("HEAD"), method = "turnPlayer()V")
    private void onTurn(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new PlayerTurnEvent(accumulatedDX, accumulatedDY, lastMouseEventTime));
    }
}
