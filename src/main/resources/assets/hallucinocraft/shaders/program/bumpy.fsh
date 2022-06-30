#version 150

uniform sampler2D DiffuseSampler;

uniform float Intensity;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main(){
    vec4 c = texture2D(DiffuseSampler, texCoord);
    vec4 nc = normalize(c);
    float du = dot(nc, normalize(texture2D(DiffuseSampler, texCoord + vec2(        0.0, -oneTexel.y))));
    float dd = dot(nc, normalize(texture2D(DiffuseSampler, texCoord + vec2(        0.0,  oneTexel.y))));
    float dl = dot(nc, normalize(texture2D(DiffuseSampler, texCoord + vec2(-oneTexel.x,         0.0))));
    float dr = dot(nc, normalize(texture2D(DiffuseSampler, texCoord + vec2( oneTexel.x,         0.0))));

    float f = 1.0;
    f += (du * Intensity) - (dd * Intensity);
    f += (dr * Intensity) - (dl * Intensity);

    vec4 color = c * clamp(f, 0.5, 2.0);
    fragColor = vec4(color.rgb, 1.0);
}
