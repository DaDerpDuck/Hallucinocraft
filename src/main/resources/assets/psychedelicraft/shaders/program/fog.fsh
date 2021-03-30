#version 110

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;

uniform vec2 ScreenSize;
uniform float _FOV;

varying vec2 texCoord;

void main() {
    gl_FragColor = 1.0-(1.0-texture2D(DiffuseDepthSampler, texCoord))*500.0;
}