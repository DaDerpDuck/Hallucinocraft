#version 150

#moj_import <fog.glsl>

#moj_import <hallucinocraft.glsl>

in vec3 Position;
in vec4 Color;
in ivec2 UV2;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform mat3 IViewRotMat;
uniform vec4 ColorModulator;
uniform int FogShape;
uniform float GameTime;

uniform float SmallWaves;
uniform float BigWaves;
uniform float WiggleWaves;
uniform float DistantWorldDeformation;

out float vertexDistance;
flat out vec4 vertexColor;

void main() {
    vec4 pos = ModelViewMat * vec4(Position, 1.0);
    toWavePos(pos, GameTime * 2400.0 * 4.0, SmallWaves, BigWaves, WiggleWaves, DistantWorldDeformation);
    gl_Position = ProjMat * pos;

    vertexDistance = fog_distance(ModelViewMat, IViewRotMat * Position, FogShape);
    vertexColor = Color * ColorModulator * texelFetch(Sampler2, UV2 / 16, 0);
}
