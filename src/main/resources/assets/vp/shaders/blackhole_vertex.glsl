#version 120
attribute vec3 Position;
attribute vec2 UV;

uniform mat4 ProjectionMatrix;
uniform mat4 ViewMatrix;
uniform mat4 ModelMatrix;

varying vec2 vUV;

void main() {
    vUV = UV;
    gl_Position = ProjectionMatrix * ViewMatrix * ModelMatrix * vec4(Position, 1.0);
}
