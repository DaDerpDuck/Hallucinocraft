#version 150

// https://www.shadertoy.com/view/Md2GDw

uniform sampler2D DiffuseSampler;
uniform sampler2D NoiseSampler;

in vec2 texCoord;

uniform vec2 ScreenSize;
uniform float TimePassed;
uniform float Artifacts;

out vec4 fragColor;

void main() {
    vec3 outColor = vec3(0.0);
    vec2 block = floor(gl_FragCoord.xy / vec2(16));
    vec2 uv_noise = block / vec2(64);
    uv_noise += floor(vec2(TimePassed) * vec2(1234.0, 3543.0)) / vec2(64);

    float block_thresh = pow(fract(TimePassed * 1236.0453), 2.0) * 0.2 * Artifacts;
    float line_thresh = pow(fract(TimePassed * 2236.0453), 3.0) * 0.7 * Artifacts;

    vec2 uv_r = texCoord, uv_g = texCoord, uv_b = texCoord;

    // glitch some blocks and lines
    if (texture(DiffuseSampler, uv_noise).r < block_thresh ||
    texture(DiffuseSampler, vec2(uv_noise.y, 0.0)).g < line_thresh) {

        vec2 dist = (fract(uv_noise) - 0.5) * 0.3;
        uv_r += dist * 0.1;
        uv_g += dist * 0.2;
        uv_b += dist * 0.125;
    }

    outColor.r = texture(DiffuseSampler, uv_r).r;
    outColor.g = texture(DiffuseSampler, uv_g).g;
    outColor.b = texture(DiffuseSampler, uv_b).b;

    // loose luma for some blocks
    if (texture(NoiseSampler, uv_noise).g < block_thresh)
        outColor.rgb = outColor.ggg;

    // discolor block lines
    if (texture(NoiseSampler, vec2(uv_noise.y, 0.0)).b * 3.5 < line_thresh)
    outColor.rgb = vec3(0.0, dot(outColor.rgb, vec3(1.0)), 0.0);

    // interleave lines in some blocks
    if (texture(NoiseSampler, uv_noise).g * 1.5 < block_thresh || texture(NoiseSampler, vec2(uv_noise.y, 0.0)).g * 2.5 < line_thresh) {
        float line = fract(gl_FragCoord.y / 3.0);
        vec3 mask = vec3(3.0, 0.0, 0.0);
        if (line > 0.333)
        mask = vec3(0.0, 3.0, 0.0);
        if (line > 0.666)
        mask = vec3(0.0, 0.0, 3.0);

        outColor.xyz *= mask;
    }

    fragColor = vec4(outColor.xyz, 1.0);
}
