#version 150

#moj_import <hallucinocraft.glsl>


in vec3 Position;
in vec4 Color;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform float GameTime;

uniform float SmallWaves;
uniform float BigWaves;
uniform float WiggleWaves;
uniform float DistantWorldDeformation;

out vec4 vertexColor;
out vec2 texCoord0;

void main() {
    vec4 pos = ModelViewMat * vec4(Position, 1.0);
    toWavePos(pos, GameTime * 2400.0 * 4.0, SmallWaves, BigWaves, WiggleWaves, DistantWorldDeformation);
    gl_Position = ProjMat * pos;

    vertexColor = Color;
    texCoord0 = UV0;
}
