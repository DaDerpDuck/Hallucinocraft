package com.daderpduck.hallucinocraft.mixin.client;

import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(PostChain.class)
public interface AccessorShaderGroup {
    @Accessor
    List<PostPass> getPasses();
}
