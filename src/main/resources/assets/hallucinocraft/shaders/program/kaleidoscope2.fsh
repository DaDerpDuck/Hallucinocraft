#version 150

const int ITERATIONS = 3;

uniform sampler2D DiffuseSampler;

in vec2 texCoord;

uniform float Scale;
uniform float Shift;
uniform float TimePassed;
uniform float TimePassedSin;
uniform float Intensity;
uniform float Extend;

out vec4 fragColor;

mat2 rotate(float angle) {
    float x = cos(angle);
    float y = sin(angle);
    return mat2(x, -y, y, x);
}

void main() {
    // https://www.shadertoy.com/view/tltSWs
    vec2 st = texCoord - 0.5;

    st *= TimePassedSin*0.5 + 1.5;

    for (int i = 0; i < ITERATIONS; i++) {
        st *= Scale;
        st *= rotate(TimePassed);
        st = abs(st);
        st -= Shift;
    }

    // vignette masking (https://www.shadertoy.com/view/lsKSWR)
    vec2 uv = texCoord*(1.0 - texCoord);
    float alpha = pow(uv.x*uv.y * Intensity, Extend);
    vec3 col = mix(texture(DiffuseSampler, fract(st)).rgb, texture(DiffuseSampler, texCoord).rgb, min(alpha, 1.0));

    fragColor = vec4(col, 1.0);
}