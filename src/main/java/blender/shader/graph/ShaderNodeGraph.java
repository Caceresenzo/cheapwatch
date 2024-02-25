package blender.shader.graph;

import blender.shader.node.ShaderNode;

import java.util.List;
import java.util.Optional;

public interface ShaderNodeGraph {

    List<ShaderNode> getNodes();

    Optional<ShaderNode> getFinalNode();

}