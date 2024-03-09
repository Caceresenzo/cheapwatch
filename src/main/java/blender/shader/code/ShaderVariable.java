package blender.shader.code;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;

public record ShaderVariable<T>(
	String name,
	ShaderDataType<T> type,
	ShaderSocket<T> socket,
	boolean linked
) {

	public ShaderVariable(String name, ShaderSocket<T> socket, boolean linked) {
		this(name, socket.type(), socket, linked);
	}

	public ShaderVariable(String name, ShaderDataType<T> type) {
		this(name, type, null, true);
	}

}