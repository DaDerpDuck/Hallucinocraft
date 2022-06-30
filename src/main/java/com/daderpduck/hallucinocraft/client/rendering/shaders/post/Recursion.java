package com.daderpduck.hallucinocraft.client.rendering.shaders.post;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.client.rendering.shaders.GlobalUniforms;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;

public class Recursion extends PostShader {
    public Recursion() throws IOException, JsonSyntaxException {
        super(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/recursion.json"));
    }

    @Override
    public boolean shouldRender() {
        return getDrugEffects().RECURSION.getValue() > EPSILON;
    }

    @Override
    public void render(float partialTicks) {
        setUniform("Extend", getDrugEffects().RECURSION.getValue());
        setUniform("TimePassed", GlobalUniforms.timePassed);
        process(partialTicks);
    }
}
