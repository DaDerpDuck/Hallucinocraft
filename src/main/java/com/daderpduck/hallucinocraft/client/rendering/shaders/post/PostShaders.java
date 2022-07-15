package com.daderpduck.hallucinocraft.client.rendering.shaders.post;

import com.daderpduck.hallucinocraft.client.rendering.shaders.LevelShaders;
import com.daderpduck.hallucinocraft.client.rendering.shaders.ShaderEventHandler;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostShaders {
    private static boolean usePostShaders = false;
    private static boolean isSetup = false;
    private static final List<PostShader> postShaders = new ArrayList<>();
    private static final Object2ObjectOpenHashMap<String, PostShaderSupplier> supplierMap = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<String, PostShader> activeShaders = new Object2ObjectOpenHashMap<>();

    public static void setup() throws IOException {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(LevelShaders::setup);
            return;
        }

        register(Depth::new);
        register(Color::new);
        register(Bumpy::new);
        register(Recursion::new);
        register(Kaleidoscope::new);
        register(WaterDistort::new);
        register(Bloom::new);
        register(Glitch::new);
        register(Blur::new);

        MinecraftForge.EVENT_BUS.register(ShaderEventHandler.Post.class);

        isSetup = true;
    }

    public static void cleanup() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(LevelShaders::setup);
            return;
        }

        MinecraftForge.EVENT_BUS.unregister(ShaderEventHandler.Post.class);

        postShaders.clear();
        supplierMap.clear();
        activeShaders.forEach((name, shader) -> shader.close());
        activeShaders.clear();

        isSetup = false;
    }

    public static boolean isSetup() {
        return isSetup;
    }

    private static void register(PostShaderSupplier supplier) throws IOException {
        PostShader shader = supplier.get();
        shader.close();
        postShaders.add(shader);
        supplierMap.put(shader.toString(), supplier);
    }

    public static void processPostShaders(float partialTicks) {
        if (!usePostShaders) return;
        Minecraft mc = Minecraft.getInstance();
        RenderTarget framebuffer = mc.getMainRenderTarget();

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.resetTextureMatrix();

        for (PostShader postShader : postShaders) {
            if (postShader.shouldRender()) {
                PostShader shader = getShader(postShader.toString());
                shader.render(partialTicks);
            } else {
                dealloc(postShader.toString());
            }
        }

        RenderSystem.enableTexture();

        framebuffer.bindWrite(false);
    }

    public static void toggleShaders(boolean enableShaders) {
        usePostShaders = enableShaders;
    }

    private static PostShader getShader(String name) {
        if (activeShaders.containsKey(name)) {
            return activeShaders.get(name);
        } else {
            try {
                PostShader shader = supplierMap.get(name).get();
                activeShaders.put(name, shader);
                return shader;
            } catch (IOException e) {
                throw new RuntimeException("Couldn't create post shader", e);
            }
        }
    }

    private static void dealloc(String name) {
        if (activeShaders.containsKey(name)) activeShaders.remove(name).close();
    }

    @FunctionalInterface
    private interface PostShaderSupplier {
        PostShader get() throws IOException, JsonSyntaxException;
    }
}
