package blender.shader;

public record ShaderSocket(
	String name,
	String identifier,
	ShaderDataType type,
	Object defaultValue,
	int index
) {

	public ShaderSocket {
		if (defaultValue != null && !type.getTypeClass().isAssignableFrom(defaultValue.getClass())) {
			throw new IllegalArgumentException("default value `%s` must be of type %s".formatted(defaultValue, type.getTypeClass().getSimpleName()));
		}
	}

	public ShaderSocket(String name, ShaderDataType type, int index) {
		this(name, name, type, type.getDefaultValue(), index);
	}

	public ShaderSocket(String name, ShaderDataType type, Object defaultValue, int index) {
		this(name, name, type, defaultValue, index);
	}

	public ShaderSocket(String name, String identifier, ShaderDataType type, int index) {
		this(name, identifier, type, type.getDefaultValue(), index);
	}

}