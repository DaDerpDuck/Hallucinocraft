package com.daderpduck.hallucinocraft.mixin.client;

import net.minecraft.client.renderer.texture.AtlasTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AtlasTexture.SheetData.class)
public interface AccessorSheetData {
    @Accessor
    int getWidth();

    @Accessor
    int getHeight();
}
