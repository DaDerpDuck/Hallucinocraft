#version 150

uniform sampler2D DiffuseSampler;

uniform float Radius;
uniform vec2 BlurDir;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main() {
    vec4 sum = vec4(0.0);

    for (float r = -Radius; r <= Radius; r += 1.0) {
        sum += texture2D(DiffuseSampler, texCoord + oneTexel*r*BlurDir);
    }

    fragColor = vec4(sum.rgb/(Radius*2.0 + 1.0), 1.0);
}
