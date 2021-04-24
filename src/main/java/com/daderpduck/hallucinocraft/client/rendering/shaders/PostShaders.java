package com.daderpduck.hallucinocraft.client.rendering.shaders;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.daderpduck.hallucinocraft.drugs.DrugEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

@OnlyIn(Dist.CLIENT)
public class PostShaders {
    public static boolean useShaders = false;
    private static final List<ResourceLocation> resourceLocations = new ArrayList<>();
    private static final Map<ResourceLocation, BooleanSupplier> supplierHashMap = new HashMap<>();
    private static final Map<ResourceLocation, BiConsumer<PostShader, Float>> consumerHashMap = new HashMap<>();
    private static final Map<ResourceLocation, PostShader> activeShaders = new HashMap<>();
    private static final float EPSILON = 1E-6F;

    public static void setup() {
        register(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/depth.json"),
            () -> DrugEffects.HUE_AMPLITUDE.getValue() > EPSILON,
            (shader, partialTicks) -> {
                shader.setUniform("Amplitude", DrugEffects.HUE_AMPLITUDE.getClamped());
                shader.setUniform("TimePassed", GlobalUniforms.timePassed);
                shader.process(partialTicks);
            });
        register(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/color.json"),
            () -> DrugEffects.SATURATION.getValue() != 0 || DrugEffects.BRIGHTNESS.getValue() != 0,
            (shader, partialTicks) -> {
                shader.setUniform("Saturation", MathHelper.clamp(DrugEffects.SATURATION.getValue() + 1F, 0F, 3F));
                shader.setUniform("Brightness", DrugEffects.BRIGHTNESS.getValue());
                shader.process(partialTicks);
            });
        register(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/bumpy.json"),
            () -> DrugEffects.BUMPY.getValue() > EPSILON,
            (shader, partialTicks) -> {
                shader.setUniform("Intensity", DrugEffects.BUMPY.getValue());
                shader.process(partialTicks);
            });
        register(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/recursion.json"),
                () -> DrugEffects.RECURSION.getValue() > EPSILON,
                (shader, partialTicks) -> {
                    shader.setUniform("Extend", DrugEffects.RECURSION.getValue());
                    shader.setUniform("TimePassed", GlobalUniforms.timePassed);
                    shader.process(partialTicks);
                });
        register(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/kaleidoscope.json"),
            () -> DrugEffects.KALEIDOSCOPE_INTENSITY.getValue() > EPSILON,
            (shader, partialTicks) -> {
                shader.setUniform("Extend", DrugEffects.KALEIDOSCOPE_INTENSITY.getValue());
                shader.setUniform("TimePassed", GlobalUniforms.timePassed);
                shader.setUniform("TimePassedSin", GlobalUniforms.timePassedSin);
                shader.process(partialTicks);
            });
        register(new ResourceLocation(Hallucinocraft.MOD_ID, "shaders/post/bloom.json"),
            () -> DrugEffects.BLOOM_RADIUS.getValue() > EPSILON,
            (shader, partialTicks) -> {
                shader.setUniform("Radius", DrugEffects.BLOOM_RADIUS.getValue());
                shader.setUniform("Threshold", 1F - DrugEffects.BLOOM_THRESHOLD.getValue());
                shader.process(partialTicks);
            });

        useShaders = true;
    }

    private static void register(ResourceLocation resourceLocation, BooleanSupplier condition, BiConsumer<PostShader, Float> consumer) {
        resourceLocations.add(resourceLocation);
        supplierHashMap.put(resourceLocation, condition);
        consumerHashMap.put(resourceLocation, consumer);
    }

    public static void cleanup() {
        useShaders = false;

        activeShaders.forEach((resourceLocation, shader) -> shader.close());
        activeShaders.clear();
    }

    public static void processShaders(float partialTicks) {
        for (ResourceLocation resourceLocation : resourceLocations) {
            if (supplierHashMap.get(resourceLocation).getAsBoolean()) {
                PostShader shader = getShader(resourceLocation);
                consumerHashMap.get(resourceLocation).accept(shader, partialTicks);
            } else {
                dealloc(resourceLocation);
            }
        }
    }

    public static PostShader getShader(ResourceLocation resourceLocation) {
        if (activeShaders.containsKey(resourceLocation)) {
            return activeShaders.get(resourceLocation);
        } else {
            try {
                PostShader shader = new PostShader(resourceLocation);
                activeShaders.put(resourceLocation, shader);
                return shader;
            } catch (IOException e) {
                throw new RuntimeException("Couldn't create post shader", e);
            }
        }
    }

    public static void dealloc(ResourceLocation resourceLocation) {
        if (activeShaders.containsKey(resourceLocation)) activeShaders.remove(resourceLocation).close();
    }
}
