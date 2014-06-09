#version 330 core

// "offset" is an input vertex attribute
layout (location = 0) in float time;

out vec4 frag_Color;

void main(void) {
    vec4 offset = vec4(sin(time),cos(time),0.0,1.0);
    const vec4 vertices[3] = vec4[3](vec4( 0.25, -0.25, 0.5, 1.0), vec4(-0.25, -0.25, 0.5, 1.0),
    vec4( 0.25, 0.25, 0.5, 1.0));
    // Add "offset" to our hard-coded vertex position
    gl_Position = vertices[gl_VertexID] + offset; 
    frag_Color = vec4(sin(time),cos(time),sin(time),1.0);
}