package com.daderpduck.hallucinocraft.client.rendering.shaders;

import com.daderpduck.hallucinocraft.Hallucinocraft;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
import com.mojang.blaze3d.shaders.*;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.FileUtil;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.IntSupplier;

@OnlyIn(Dist.CLIENT)
public class WorldShader implements Shader, AutoCloseable {
    private static final Logger LOGGER = Hallucinocraft.LOGGER;
    private static final WorldShaderDefault DUMMY_UNIFORM = new WorldShaderDefault();
    protected static WorldShader lastAppliedEffect;
    protected static int lastProgramId = -1;
    protected final Map<String, IntSupplier> samplerMap = Maps.newHashMap();
    protected final List<String> samplerNames = Lists.newArrayList();
    protected final List<Integer> samplerLocations = Lists.newArrayList();
    protected final List<WorldShaderUniform> uniforms = Lists.newArrayList();
    protected final List<Integer> uniformLocations = Lists.newArrayList();
    protected final Map<String, WorldShaderUniform> uniformMap = Maps.newHashMap();
    protected final int programId;
    protected final String name;
    protected boolean dirty;
    protected final BlendMode blend;
    protected final List<Integer> attributes;
    protected final List<String> attributeNames;
    protected final Program vertexProgram;
    protected final Program fragmentProgram;
    protected boolean samplerDirty;

    public WorldShader(ResourceManager resourceManager, ResourceLocation location) throws IOException {
        ResourceLocation resourcelocation = new ResourceLocation(location.getNamespace(), "shaders/program/" + location.getPath() + ".json");
        this.name = location.getPath();
        Resource iresource = null;

        try {
            iresource = resourceManager.getResource(resourcelocation);
            JsonObject jsonobject = GsonHelper.parse(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8));
            String s = GsonHelper.getAsString(jsonobject, "vertex");
            String s2 = GsonHelper.getAsString(jsonobject, "fragment");
            JsonArray jsonarray = GsonHelper.getAsJsonArray(jsonobject, "samplers", null);
            if (jsonarray != null) {
                int i = 0;

                for(JsonElement jsonelement : jsonarray) {
                    try {
                        this.parseSamplerNode(jsonelement);
                    } catch (Exception exception2) {
                        ChainedJsonException jsonexception1 = ChainedJsonException.forException(exception2);
                        jsonexception1.prependJsonKey("samplers[" + i + "]");
                        throw jsonexception1;
                    }

                    ++i;
                }
            }

            JsonArray jsonarray1 = GsonHelper.getAsJsonArray(jsonobject, "attributes", null);
            if (jsonarray1 != null) {
                int j = 0;
                this.attributes = Lists.newArrayListWithCapacity(jsonarray1.size());
                this.attributeNames = Lists.newArrayListWithCapacity(jsonarray1.size());

                for(JsonElement jsonelement1 : jsonarray1) {
                    try {
                        this.attributeNames.add(GsonHelper.convertToString(jsonelement1, "attribute"));
                    } catch (Exception exception1) {
                        ChainedJsonException jsonexception2 = ChainedJsonException.forException(exception1);
                        jsonexception2.prependJsonKey("attributes[" + j + "]");
                        throw jsonexception2;
                    }

                    ++j;
                }
            } else {
                this.attributes = null;
                this.attributeNames = null;
            }

            JsonArray jsonarray2 = GsonHelper.getAsJsonArray(jsonobject, "uniforms", null);
            if (jsonarray2 != null) {
                int k = 0;

                for(JsonElement jsonelement2 : jsonarray2) {
                    try {
                        this.parseUniformNode(jsonelement2);
                    } catch (Exception exception) {
                        ChainedJsonException jsonexception3 = ChainedJsonException.forException(exception);
                        jsonexception3.prependJsonKey("uniforms[" + k + "]");
                        throw jsonexception3;
                    }

                    ++k;
                }
            }

            this.blend = ShaderInstance.parseBlendNode(GsonHelper.getAsJsonObject(jsonobject, "blend", null));
            this.vertexProgram = getOrCreate(resourceManager, Program.Type.VERTEX, s);
            this.fragmentProgram = getOrCreate(resourceManager, Program.Type.FRAGMENT, s2);
            this.programId = ProgramManager.createProgram();

            if (this.attributeNames != null) {
                for(String s3 : this.attributeNames) {
                    int l = Uniform.glGetAttribLocation(this.programId, s3);
                    this.attributes.add(l);
                }
            }

            ProgramManager.linkShader(this);
            this.updateLocations();
        } catch (Exception exception3) {
            String s1;
            if (iresource != null) {
                s1 = " (" + iresource.getSourceName() + ")";
            } else {
                s1 = "";
            }

            ChainedJsonException jsonexception = ChainedJsonException.forException(exception3);
            jsonexception.setFilenameAndFlush(resourcelocation.getPath() + s1);
            throw jsonexception;
        } finally {
            IOUtils.closeQuietly(iresource);
        }

        this.markDirty();
    }

    private static Program getOrCreate(final ResourceProvider pResourceProvider, Program.Type pProgramType, String pName) throws IOException {
        Program program1 = pProgramType.getPrograms().get(pName);
        Program program;
        if (program1 == null) {
            ResourceLocation loc = new ResourceLocation(pName);
            String s = "shaders/core/" + loc.getPath() + pProgramType.getExtension();
            ResourceLocation resourcelocation = new ResourceLocation(loc.getNamespace(), s);
            Resource resource = pResourceProvider.getResource(resourcelocation);
            final String s1 = FileUtil.getFullResourcePath(s);

            try {
                program = Program.compileShader(pProgramType, pName, resource.getInputStream(), resource.getSourceName(), new GlslPreprocessor() {
                    private final Set<String> importedPaths = Sets.newHashSet();

                    public String applyImport(boolean p_173374_, @Nonnull String p_173375_) {
                        p_173375_ = FileUtil.normalizeResourcePath((p_173374_ ? s1 : "shaders/include/") + p_173375_);
                        if (!this.importedPaths.add(p_173375_)) {
                            return null;
                        } else {
                            ResourceLocation resourcelocation1 = new ResourceLocation(p_173375_);

                            try {
                                Resource resource1 = pResourceProvider.getResource(resourcelocation1);

                                String s2;
                                try {
                                    s2 = IOUtils.toString(resource1.getInputStream(), StandardCharsets.UTF_8);
                                } catch (Throwable throwable1) {
                                    if (resource1 != null) {
                                        try {
                                            resource1.close();
                                        } catch (Throwable throwable) {
                                            throwable1.addSuppressed(throwable);
                                        }
                                    }

                                    throw throwable1;
                                }

                                if (resource1 != null) {
                                    resource1.close();
                                }

                                return s2;
                            } catch (IOException ioexception) {
                                Hallucinocraft.LOGGER.error("Could not open GLSL import {}: {}", p_173375_, ioexception.getMessage());
                                return "#error " + ioexception.getMessage();
                            }
                        }
                    }
                });
            } finally {
                IOUtils.closeQuietly(resource);
            }
        } else {
            program = program1;
        }

        return program;
    }

    @Override
    public void markDirty() {
        this.dirty = true;
    }

    @Nullable
    public WorldShaderUniform getUniform(String name) {
        RenderSystem.assertOnRenderThread();
        return this.uniformMap.get(name);
    }

    public WorldShaderDefault safeGetUniform(String name) {
        RenderSystem.assertOnGameThread();
        WorldShaderUniform shaderuniform = this.getUniform(name);
        return shaderuniform == null ? DUMMY_UNIFORM : shaderuniform;
    }

    private void updateLocations() {
        RenderSystem.assertOnRenderThread();
        IntList intlist = new IntArrayList();

        for(int i = 0; i < this.samplerNames.size(); ++i) {
            String s = this.samplerNames.get(i);
            int j = Uniform.glGetUniformLocation(this.programId, s);
            if (j == -1) {
                LOGGER.warn("Shader {} could not find sampler named {} in the specified shader program.", this.name, s);
                this.samplerMap.remove(s);
                intlist.add(i);
            } else {
                this.samplerLocations.add(j);
            }
        }

        for(int l = intlist.size() - 1; l >= 0; --l) {
            this.samplerNames.remove(intlist.getInt(l));
        }

        for(WorldShaderUniform shaderuniform : this.uniforms) {
            String s1 = shaderuniform.getName();
            int k = Uniform.glGetUniformLocation(this.programId, s1);
            if (k == -1) {
                LOGGER.warn("Could not find uniform named {} in the specified shader program.", s1);
            } else {
                this.uniformLocations.add(k);
                shaderuniform.setLocation(k);
                this.uniformMap.put(s1, shaderuniform);
            }
        }

    }

    private void parseSamplerNode(JsonElement p_216541_1_) {
        JsonObject jsonobject = GsonHelper.convertToJsonObject(p_216541_1_, "sampler");
        String s = GsonHelper.getAsString(jsonobject, "name");
        if (!GsonHelper.isStringValue(jsonobject, "file")) {
            this.samplerMap.put(s, null);
        }
        this.samplerNames.add(s);
    }

    public void setSampler(@Nonnull String samplerName, @Nonnull IntSupplier intSupplier) {
        samplerMap.remove(samplerName);

        this.samplerMap.put(samplerName, intSupplier);
        this.markDirty();

        samplerDirty = true;
    }

    private void parseUniformNode(JsonElement p_216540_1_) throws ChainedJsonException {
        JsonObject jsonobject = GsonHelper.convertToJsonObject(p_216540_1_, "uniform");
        String uniformName = GsonHelper.getAsString(jsonobject, "name");
        int uniformType = WorldShaderUniform.getTypeFromString(GsonHelper.getAsString(jsonobject, "type"));
        int uniformCount = GsonHelper.getAsInt(jsonobject, "count");
        float[] afloat = new float[Math.max(uniformCount, 16)];
        JsonArray jsonarray = GsonHelper.getAsJsonArray(jsonobject, "values");
        if (jsonarray.size() != uniformCount && jsonarray.size() > 1) {
            throw new ChainedJsonException("Invalid amount of values specified (expected " + uniformCount + ", found " + jsonarray.size() + ")");
        } else {
            int k = 0;

            for(JsonElement jsonelement : jsonarray) {
                try {
                    afloat[k] = GsonHelper.convertToFloat(jsonelement, "value");
                } catch (Exception exception) {
                    ChainedJsonException jsonexception = ChainedJsonException.forException(exception);
                    jsonexception.prependJsonKey("values[" + k + "]");
                    throw jsonexception;
                }

                ++k;
            }

            if (uniformCount > 1 && jsonarray.size() == 1) {
                while(k < uniformCount) {
                    afloat[k] = afloat[0];
                    ++k;
                }
            }

            int l = (uniformCount > 1 && uniformCount <= 4 && uniformType < 8) ? uniformCount - 1 : 0;
            WorldShaderUniform shaderuniform = new WorldShaderUniform(uniformName, uniformType + l, uniformCount, this);
            if (uniformType <= 3) {
                shaderuniform.setSafeInt((int)afloat[0], (int)afloat[1], (int)afloat[2], (int)afloat[3]);
            } else if (uniformType <= 7) {
                shaderuniform.setSafeFloat(afloat[0], afloat[1], afloat[2], afloat[3]);
            } else {
                shaderuniform.setFloat(afloat);
            }

            this.uniforms.add(shaderuniform);
        }
    }

    public void apply() {
        RenderSystem.assertOnRenderThread();

        if (samplerDirty) {
            samplerDirty = false;
            samplerLocations.clear();
            uniformLocations.clear();
            updateLocations();
        }

        dirty = false;
        lastAppliedEffect = this;
        blend.apply();
        if (programId != lastProgramId) {
            ProgramManager.glUseProgram(getId());
            lastProgramId = programId;
        }

        for(int i = 0; i < samplerLocations.size(); ++i) {
            String s = samplerNames.get(i);
            IntSupplier intsupplier = samplerMap.get(s);
            if (intsupplier != null) {
                int j = intsupplier.getAsInt();
                if (j != -1) {
                    Uniform.uploadInteger(samplerLocations.get(i), j);
                }
            }
        }

        for (WorldShaderUniform shaderUniform : uniforms) {
            shaderUniform.upload();
        }
    }

    @Override
    public void close() {
        for(WorldShaderUniform shaderuniform : this.uniforms) {
            shaderuniform.close();
        }

        ProgramManager.releaseProgram(this);
    }

    public void clear() {
        RenderSystem.assertOnRenderThread();
        ProgramManager.glUseProgram(0);
        lastProgramId = -1;
        lastAppliedEffect = null;
    }

    @Override
    @Nonnull
    public Program getVertexProgram() {
        return this.vertexProgram;
    }

    @Override
    @Nonnull
    public Program getFragmentProgram() {
        return this.fragmentProgram;
    }

    @Override
    public void attachToProgram() {

    }

    @Override
    public int getId() {
        return this.programId;
    }
}
