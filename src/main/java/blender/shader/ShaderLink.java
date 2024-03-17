package blender.shader;

import java.util.Map;
import java.util.UUID;

import blender.shader.node.ShaderNode;

public record ShaderLink(
	UUID id,
	ShaderNode fromNode,
	ShaderSocket fromSocket,
	ShaderNode toNode,
	ShaderSocket toSocket
) {

	public ShaderLink(
		ShaderNode fromNode,
		ShaderSocket fromSocket,
		ShaderNode toNode,
		ShaderSocket toSocket
	) {
		this(
			UUID.randomUUID(),
			fromNode,
			fromSocket,
			toNode,
			toSocket
		);
	}

	public int fromIndex() {
		return fromSocket.index();
	}

	public int toIndex() {
		return toSocket.index();
	}

	public Map.Entry<ShaderNode, ShaderSocket> from() {
		return Map.entry(fromNode, fromSocket);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return "ShaderLink[%s]".formatted(id);
	}

}