#version 330 core

out vec4 out_Color;

//in VS_OUT
//{
//    vec4 color;
//} fs_in;

void main(void) {
    vec4 color = vec4(sin(gl_FragCoord.x),cos(gl_FragCoord.y),sin(gl_FragCoord.x), 1.0);
    out_Color = color;
}
