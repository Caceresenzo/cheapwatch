package blender.shader;

import blender.shader.node.ShaderNode;

import java.util.UUID;

public record ShaderLink(
        UUID id,
        ShaderNode fromNode,
        ShaderPort fromPort,
        ShaderNode toNode,
        ShaderPort toPort
) {

    public ShaderLink(
            ShaderNode fromNode,
            ShaderPort fromPort,
            ShaderNode toNode,
            ShaderPort toPort
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