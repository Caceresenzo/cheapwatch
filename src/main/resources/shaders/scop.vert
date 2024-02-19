#version 330 core

layout (location = 0) in vec3 in_Positions;

out vec3 pass_Positions;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void
main()
{
    gl_Position = projection * view * model * vec4(in_Positions, 1.0);
    
    pass_Positions = in_Positions;
}