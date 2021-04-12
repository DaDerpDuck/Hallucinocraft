package com.daderpduck.hallucinocraft.events.hooks;

import com.daderpduck.hallucinocraft.mixin.client.AccessorSheetData;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraftforge.eventbus.api.Event;

public class AtlasReloadEvent extends Event {
    public final AtlasTexture atlasTexture;
    public final AtlasTexture.SheetData sheetData;
    public final int width;
    public final int height;

    public AtlasReloadEvent(AtlasTexture atlasTexture, AtlasTexture.SheetData sheetData) {
        this.atlasTexture = atlasTexture;
        this.sheetData = sheetData;
        this.width = ((AccessorSheetData)sheetData).getWidth();
        this.height = ((AccessorSheetData)sheetData).getHeight();
    }
}
