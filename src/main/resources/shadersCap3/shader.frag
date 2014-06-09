#version 330 core

out vec4 out_Color;

in VS_OUT
{
    vec4 color;
} fs_in;

void main(void) {
    out_Color = fs_in.color;
}
