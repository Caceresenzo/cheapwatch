package blender.shader.graph;

import java.util.List;
import java.util.Optional;

import blender.shader.node.ShaderNode;

public interface ShaderNodeGraph {

    List<ShaderNode> getNodes();

    default ShaderNode getNode(String name) {
        for (final var node : getNodes()) {
            if (name.equals(node.getName())) {
                return node;
            }
        }

        throw new IllegalArgumentException("node not found: %s".formatted(name));
    }

    Optional<ShaderNode> getFinalNode();

}