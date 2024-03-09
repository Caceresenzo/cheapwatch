package blender.shader.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import blender.shader.node.ShaderNode;
import lombok.Getter;

@Getter
public class SimpleShaderNodeGraph implements ShaderNodeGraph {

	private final List<ShaderNode> nodes = new ArrayList<>();

	public SimpleShaderNodeGraph addNode(ShaderNode node) {
		this.nodes.add(node);
		return this;
	}

	@Override
	public Optional<ShaderNode> getFinalNode() {
		if (nodes.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(nodes.getLast());
	}

}