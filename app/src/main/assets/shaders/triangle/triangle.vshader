uniform mat4 uMVPMatrix;
uniform vec4 u_Color;

varying vec4 v_Color;

attribute vec4 vPosition;

void main() {
    v_Color = u_Color;

    gl_Position = uMVPMatrix * vPosition;
}