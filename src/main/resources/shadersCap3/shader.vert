#version 330 core

// "offset" is an input vertex attribute
layout (location = 0) in vec4 offset;
layout (location = 1) in vec4 in_Color;

out vec4 frag_Color;

void main(void) {
    const vec4 vertices[3] = vec4[3](vec4( 0.25, -0.25, 0.5, 1.0), vec4(-0.25, -0.25, 0.5, 1.0),
    vec4( 0.25, 0.25, 0.5, 1.0));
    // Add "offset" to our hard-coded vertex position
    gl_Position = vertices[gl_VertexID] + offset; 
    frag_Color = in_Color;
}