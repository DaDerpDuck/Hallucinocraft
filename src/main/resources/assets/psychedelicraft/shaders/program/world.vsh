#version 120

varying vec4 texCoord;
varying vec4 lmCoord;
varying vec3 normalVector;

uniform mat4 modelViewMat;
uniform mat4 modelViewInverseMat;
uniform float smallWaves;
uniform float bigWaves;
uniform float timePassed;

int hash(int state) {
    state ^= 2747636419;
    state *= 2654435769;
    state ^= state >> 16;
    state *= 2654435769;
    state ^= state >> 16;
    state *= 2654435769;

    return state;
}

void main(){
    normalVector = normalize(gl_NormalMatrix * gl_Normal);
    texCoord = gl_TextureMatrix[0] * gl_MultiTexCoord0;
    lmCoord = gl_TextureMatrix[2] * gl_MultiTexCoord2;

    vec4 position = gl_Vertex;

    position = gl_ModelViewMatrix * position;

    position = modelViewInverseMat * position;

    if (smallWaves > 0.0) {
        position.y += sin((position.x + timePassed * 0.2) * 0.785398163) * sin((position.z + timePassed * 0.2) * 0.785398163) * smallWaves * 1.5;
        position.y -= sin((timePassed * 0.2) * 0.785398163) * sin((timePassed * 0.2) * 0.785398163) * smallWaves * 1.5;

        position.y += sin((position.x + timePassed * 0.125) * 0.392699082) * sin((position.z) * 0.392699082) * smallWaves * 3.0;

        position.x = mix(position.x, position.x * (1.0 + gl_FogFragCoord * 0.05), smallWaves);
        position.y = mix(position.y, position.y * (1.0 + gl_FogFragCoord * 0.05), smallWaves);
    }

    if(bigWaves > 0.0) {
        float dist = length(position.xz);
        float f = 20.0 * bigWaves + (dist*bigWaves - 20.0) * bigWaves * 0.3;
        f *= min(dist * 0.05, 1.0);

        float inf1 = sin(timePassed * 0.0086465563) * f;
        float inf2 = cos(timePassed * 0.0086465563) * f;
        float inf3 = sin(timePassed * 0.0091033941) * f;
        float inf4 = cos(timePassed * 0.0091033941) * f;
        float inf5 = sin(timePassed * 0.0064566190) * f;
        float inf6 = cos(timePassed * 0.0064566190) * f;

        float pMul = 1.3;

        position.x += sin(position.z * 0.1 * sin(timePassed * 0.001849328) + timePassed * 0.014123412) * 0.5 * inf1 * pMul;
        position.y += cos(position.z * 0.1 * sin(timePassed * 0.001234728) + timePassed * 0.017481893) * 0.4 * inf1 * pMul;

        position.x += sin(position.y * 0.1 * sin(timePassed * 0.001523784) + timePassed * 0.021823911) * 0.2 * inf2 * pMul;
        position.y += sin(position.x * 0.1 * sin(timePassed * 0.001472387) + timePassed * 0.023193141) * 0.08 * inf2 * pMul;

        position.x += sin(position.z * 0.15 * sin(timePassed * 0.001284923) + timePassed * 0.019404289) * 0.25 * inf3 * pMul;
        position.y += cos(position.z * 0.15 * sin(timePassed * 0.001482938) + timePassed * 0.018491238) * 0.15 * inf3 * pMul;

        position.x += sin(position.y * 0.05 * sin(timePassed * 0.001283942) + timePassed * 0.012942342) * 0.4 * inf4 * pMul;
        position.y += sin(position.x * 0.05 * sin(timePassed * 0.001829482) + timePassed * 0.012981328) * 0.35 * inf4 * pMul;

        position.z += sin(position.y * 0.13 * sin(timePassed * 0.02834472) + timePassed * 0.023482934) * 0.1 * inf5 * pMul;
        position.z += sin(position.x * 0.124 * sin(timePassed * 0.00184298) + timePassed * 0.018394082) * 0.05 * inf6 * pMul;
    }

    position = modelViewMat * position;

    gl_Position = gl_ProjectionMatrix * position;
    gl_FrontColor = gl_Color;
    gl_FogFragCoord = length(position.xyz);
}
