#version 330 core

in vec3 pass_Positions;

out vec4 out_FragColor;

void main()
{
	float greyIntensity = (gl_PrimitiveID % 12) / 20.0;
	vec4 grey = vec4(greyIntensity, greyIntensity, greyIntensity, 1.0);
	
	out_FragColor = grey;
}