package blender.shader;

public record ShaderPort(
	String name,
	ShaderDataType type,
	Object defaultValue,
	int index
) {}