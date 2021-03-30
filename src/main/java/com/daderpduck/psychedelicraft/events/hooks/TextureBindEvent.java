package com.daderpduck.psychedelicraft.events.hooks;

import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

public class TextureBindEvent extends Event {
    public final Texture texture;
    public final ResourceLocation resourceLocation;

    public TextureBindEvent(Texture texture, ResourceLocation resourceLocation) {
        this.texture = texture;
        this.resourceLocation = resourceLocation;
    }
}
