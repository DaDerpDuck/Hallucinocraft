#version 150

#moj_import <hallucinocraft.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform float GameTime;

uniform float SmallWaves;
uniform float BigWaves;
uniform float WiggleWaves;
uniform float DistantWorldDeformation;

out vec4 vertexColor;
out vec2 texCoord0;
out vec4 normal;

void main() {
    vec4 pos = ModelViewMat * vec4(Position, 1.0);
    toWavePos(pos, GameTime * 2400.0 * 4.0, SmallWaves, BigWaves, WiggleWaves, DistantWorldDeformation);
    gl_Position = ProjMat * pos;

    vertexColor = Color * texelFetch(Sampler2, UV2 / 16, 0);
    texCoord0 = UV0;
    normal = ProjMat * ModelViewMat * vec4(Normal, 0.0);
}
