package blender.shader.code;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;

public record ShaderVariable(
	String name,
	ShaderDataType type,
	ShaderSocket socket,
	boolean linked
) {

	public ShaderVariable(String name, ShaderSocket socket, boolean linked) {
		this(name, socket.type(), socket, linked);
	}

	public ShaderVariable(String name, ShaderDataType type) {
		this(name, type, null, true);
	}

}