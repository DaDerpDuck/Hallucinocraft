package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.events.hooks.AtlasReloadEvent;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AtlasTexture.class)
public class MixinAtlasTexture {
    @Inject(at = @At("HEAD"), method = "reload(Lnet/minecraft/client/renderer/texture/AtlasTexture$SheetData;)V")
    private void onReload(AtlasTexture.SheetData sheetData, CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new AtlasReloadEvent((AtlasTexture)(Object)this, sheetData));
    }
}
