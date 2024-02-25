#version 410 core

in vec2 pass_UV;

out vec4 out_FragColor;

uniform sampler2D textureSampler;

void main()
{
	out_FragColor = texture(textureSampler, pass_UV);

	// float greyIntensity = (gl_PrimitiveID % 12) / 20.0;
	// vec4 grey = vec4(greyIntensity, greyIntensity, greyIntensity, 1.0);
	
	// out_FragColor = grey;
}