package blender.shader;

public record ShaderSocket<T>(
	String name,
	String identifier,
	ShaderDataType<T> type,
	T defaultValue,
	int index
) {

	public ShaderSocket(String name, ShaderDataType<T> type, int index) {
		this(name, name, type, type.getDefaultValue(), index);
	}

	public ShaderSocket(String name, ShaderDataType<T> type, T defaultValue, int index) {
		this(name, name, type, defaultValue, index);
	}

	public ShaderSocket(String name, String identifier, ShaderDataType<T> type, int index) {
		this(name, identifier, type, type.getDefaultValue(), index);
	}

}