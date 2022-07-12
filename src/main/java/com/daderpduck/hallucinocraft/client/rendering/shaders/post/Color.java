package com.daderpduck.hallucinocraft.client.rendering.shaders.post;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.io.IOException;

public class Color extends PostShader {
    public Color() throws IOException, JsonSyntaxException {
        super(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/color.json"));
    }

    @Override
    public boolean shouldRender() {
        return getDrugEffects().SATURATION.getValue() != 0 || getDrugEffects().BRIGHTNESS.getValue() != 0;
    }

    @Override
    public void render(float partialTicks) {
        setUniform("Saturation", Mth.clamp(getDrugEffects().SATURATION.getValue() + 1F, 0F, 3F));
        setUniform("Brightness", getDrugEffects().BRIGHTNESS.getValue());
        process(partialTicks);
    }
}
