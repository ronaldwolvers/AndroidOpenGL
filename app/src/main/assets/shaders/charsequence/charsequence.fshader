precision mediump float;

uniform sampler2D u_Texture;

varying vec4 v_Color;
varying vec2 v_TexCoordinate;

void main()
{
    vec4 textureColor = (texture2D(u_Texture, v_TexCoordinate));
    vec4 interim = v_Color;
    if (interim.a > 0.0) {
        interim.rgb = textureColor.rgb;
    }
    gl_FragColor = interim;
}
