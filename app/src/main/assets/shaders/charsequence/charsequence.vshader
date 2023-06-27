uniform mat4 u_MVPMatrix;
uniform vec4 u_Color;

attribute vec2 a_TexCoordinate; // Per-vertex texture coordinate information we will pass in.
attribute vec4 a_Position;      // Per-vertex position information we will pass in.

varying vec4 v_Color;
varying vec2 v_TexCoordinate;

void main()
{
    v_Color = u_Color;
    v_TexCoordinate = a_TexCoordinate;

    gl_Position = u_MVPMatrix * a_Position;
}
