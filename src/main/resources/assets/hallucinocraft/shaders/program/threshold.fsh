#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;

uniform float Threshold;

out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);

    float brightness = dot(color.rgb, vec3(0.2126, 0.7152, 0.0722));
    float contribution = max(0, brightness - Threshold);
    contribution /= max(brightness, 0.0001);
    fragColor = vec4(color.rgb*contribution, 1.0);
}
