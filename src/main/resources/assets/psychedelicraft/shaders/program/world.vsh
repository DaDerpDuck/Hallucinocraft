#version 120

varying vec4 texCoord;
varying vec4 lmCoord;
varying vec3 normalVector;

uniform mat4 modelViewMat;
uniform mat4 modelViewInverseMat;

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

    position = ModelViewInverseMat * position;

    /*float distance2D = position.x * position.x + position.z * position.z;
    position.y += min(distance2D/20, 2)*sin(distance2D/20);*/

    position = ModelViewMat * position;

    gl_Position = gl_ProjectionMatrix * position;
    gl_FrontColor = gl_Color;
    gl_FogFragCoord = length(position.xyz);


}
