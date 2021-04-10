#version 120

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;

uniform vec2 ScreenSize;
uniform float Stretch;
uniform float Amplitude;
uniform float TimePassed;

varying vec2 texCoord;

const float near = 0.1;
const float far = 1000.0;

// https://gist.github.com/mairod/a75e7b44f68110e1576d77419d608786#gistcomment-3195243
vec3 hueShift(vec3 color, float hue) {
    const vec3 k = vec3(0.57735, 0.57735, 0.57735);
    float cosAngle = cos(hue);
    return vec3(color * cosAngle + cross(k, color) * sin(hue) + k * dot(k, color) * (1.0 - cosAngle));
}

float linearize(float depth) {
    float z = depth*2.0 - 1.0;
    return (near*far)/(far + near - z*(far - near));
}

void main() {
    float depth = Stretch*linearize(texture2D(DiffuseDepthSampler, texCoord).x);
    float distance = length(vec3(1.0, (2.0*texCoord - 1.0)*vec2(ScreenSize.x/ScreenSize.y, 1.0)*depth));
    gl_FragColor = vec4(hueShift(texture2D(DiffuseSampler, texCoord).rgb, Amplitude*sin(TimePassed + distance)), 1.0);
}