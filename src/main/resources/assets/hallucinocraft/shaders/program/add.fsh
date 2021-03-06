#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D OtherSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    fragColor = vec4(texture(DiffuseSampler, texCoord).rgb + texture(OtherSampler, texCoord).rgb, 1.0);
}
