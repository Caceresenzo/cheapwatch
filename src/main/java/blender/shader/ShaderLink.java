package blender.shader;

import java.util.UUID;

import blender.shader.node.ShaderNode;

public record ShaderLink(
	UUID id,
	ShaderNode fromNode,
	ShaderSocket fromPort,
	ShaderNode toNode,
	ShaderSocket toPort
) {

	public ShaderLink(
		ShaderNode fromNode,
		ShaderSocket fromPort,
		ShaderNode toNode,
		ShaderSocket toPort
	) {
		this(
			UUID.randomUUID(),
			fromNode,
			fromPort,
			toNode,
			toPort
		);
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