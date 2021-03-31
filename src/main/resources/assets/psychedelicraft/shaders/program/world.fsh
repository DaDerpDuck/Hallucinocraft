#version 120

#define MAX_LIGHTS 3
#define AMBIENT_LIGHT 0.4

uniform sampler2D texture;
uniform sampler2D lightMap;

uniform vec4 entityColor;
uniform int lightEnabled;
uniform int lightmapEnabled;
uniform float timePassed;

varying vec4 texCoord;
varying vec4 lmCoord;
varying vec3 normalVector;

void main() {
    /*vec4 modCoord = texCoord;
    float lower = texCoord.s - mod(texCoord.s, 0.015625);
    modCoord.s = fract(timePassed*0.1 + texCoord.s/0.015625)*0.015625 + lower;
    gl_FragColor = texture2D(texture, modCoord.st) * gl_Color;*/

    gl_FragColor = texture2D(texture, texCoord.st) * gl_Color;

    gl_FragColor.rgb = mix(gl_FragColor.rgb, entityColor.rgb, entityColor.a);

    if (lightmapEnabled == 1) {
        gl_FragColor *= texture2D(lightMap, lmCoord.st);
    }

    if (lightEnabled == 1) {
        vec3 finalLightColor = vec3(AMBIENT_LIGHT);

        for (int i = 0; i < MAX_LIGHTS; i++)
        {
            float glLightColor = AMBIENT_LIGHT;
            vec3 lightVec = normalize(gl_LightSource[i].position.xyz);
            vec4 lightDiff = gl_FrontLightProduct[i].diffuse * max(dot(normalVector, lightVec), 0.0);

            finalLightColor += lightDiff.rgb;
        }

        gl_FragColor.rgb = gl_FragColor.rgb * clamp(finalLightColor, 0.0, 1.0);
    }

    gl_FragColor = clamp(gl_FragColor, 0.0, 1.0);
    gl_FragColor.rgb = mix(gl_FragColor.rgb, gl_Fog.color.rgb, clamp((gl_FogFragCoord - gl_Fog.start)*gl_Fog.scale, 0.0, 1.0));
}