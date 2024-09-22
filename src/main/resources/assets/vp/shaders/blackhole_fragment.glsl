#version 120
uniform float iTime;
uniform vec2 iResolution;

varying vec2 vUV;

void main() {
    vec2 uv = vUV * iResolution.xy;

    // Применение шейдера чёрной дыры с Shadertoy
    vec3 ray = vec3(uv, 1.0);
    vec3 pos = vec3(0.0, 0.0, -5.0);

    float brightness = sin(iTime) * 0.5 + 0.5; // пример анимации
    vec3 color = vec3(1.0, brightness, 0.0);

    gl_FragColor = vec4(color, 1.0);
}
