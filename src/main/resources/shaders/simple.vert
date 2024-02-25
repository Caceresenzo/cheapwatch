#version 410 core

layout (location = 0) in vec3 in_Positions;
layout (location = 1) in vec2 in_UV;

out vec2 pass_UV;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void
main()
{
    gl_Position = projection * view * model * vec4(in_Positions, 1.0);
    
    pass_UV = in_UV;
}