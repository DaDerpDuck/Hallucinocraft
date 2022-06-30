#version 150

void toWavePos(inout vec4 pos, float time, float smallWave, float bigWave, float wiggleWave, float distantDeformation) {
    float dist = length(pos.xyz);

    if (smallWave > 0.0) {
        pos.y += sin((pos.x + time * 0.2) * 0.785398163) * sin((pos.z + time * 0.2) * 0.785398163) * smallWave * 1.5;
        pos.y -= sin((time * 0.2) * 0.785398163) * sin((time * 0.2) * 0.785398163) * smallWave * 1.5;

        pos.y += sin((pos.x + time * 0.125) * 0.392699082) * sin((pos.z) * 0.392699082) * smallWave * 3.0;

        pos.x = mix(pos.x, pos.x * (1.0 + dist * 0.05), smallWave);
        pos.y = mix(pos.y, pos.y * (1.0 + dist * 0.05), smallWave);
    }

    if (wiggleWave > 0.0) {
        pos.x += sin((pos.y + time*0.125)*0.125 * 3.14159 * 2.0) * sin((pos.z + time*0.2) * 0.125 * 3.14159 * 2.0) * wiggleWave;
    }

    if (distantDeformation > 0.0 && dist > 5.0) {
        pos.y += (sin(dist*0.125*3.14159*2.0) + 1.0)*distantDeformation * (dist - 5.0)*0.125;
    }

    if (bigWave > 0.0) {
        float dist2D = length(pos.xz);
        float f = 20.0 * bigWave + (dist2D*bigWave - 20.0) * bigWave * 0.3;
        f *= min(dist2D * 0.05, 1.0);

        float inf1 = sin(time * 0.0086465563) * f;
        float inf2 = cos(time * 0.0086465563) * f;
        float inf3 = sin(time * 0.0091033941) * f;
        float inf4 = cos(time * 0.0091033941) * f;
        float inf5 = sin(time * 0.0064566190) * f;
        float inf6 = cos(time * 0.0064566190) * f;

        float pMul = 1.3;

        pos.x += sin(pos.z * 0.1 * sin(time * 0.001849328) + time * 0.014123412) * 0.5 * inf1 * pMul;
        pos.y += cos(pos.z * 0.1 * sin(time * 0.001234728) + time * 0.017481893) * 0.4 * inf1 * pMul;

        pos.x += sin(pos.y * 0.1 * sin(time * 0.001523784) + time * 0.021823911) * 0.2 * inf2 * pMul;
        pos.y += sin(pos.x * 0.1 * sin(time * 0.001472387) + time * 0.023193141) * 0.08 * inf2 * pMul;

        pos.x += sin(pos.z * 0.15 * sin(time * 0.001284923) + time * 0.019404289) * 0.25 * inf3 * pMul;
        pos.y += cos(pos.z * 0.15 * sin(time * 0.001482938) + time * 0.018491238) * 0.15 * inf3 * pMul;

        pos.x += sin(pos.y * 0.05 * sin(time * 0.001283942) + time * 0.012942342) * 0.4 * inf4 * pMul;
        pos.y += sin(pos.x * 0.05 * sin(time * 0.001829482) + time * 0.012981328) * 0.35 * inf4 * pMul;

        pos.z += sin(pos.y * 0.13 * sin(time * 0.02834472) + time * 0.023482934) * 0.1 * inf5 * pMul;
        pos.z += sin(pos.x * 0.124 * sin(time * 0.00184298) + time * 0.018394082) * 0.05 * inf6 * pMul;
    }
}
