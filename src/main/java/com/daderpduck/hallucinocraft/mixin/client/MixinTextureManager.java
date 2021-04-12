package com.daderpduck.hallucinocraft.mixin.client;

import com.daderpduck.hallucinocraft.events.hooks.TextureBindEvent;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(TextureManager.class)
public class MixinTextureManager {
    @Shadow @Final private Map<ResourceLocation, Texture> byPath;

    @Inject(at = @At("HEAD"), method = "_bind(Lnet/minecraft/util/ResourceLocation;)V")
    private void onBind(ResourceLocation rl, CallbackInfo ci) {
        Texture tex = byPath.get(rl);
        if (tex == null) tex = new SimpleTexture(rl);
        MinecraftForge.EVENT_BUS.post(new TextureBindEvent(tex, rl));
    }
}
