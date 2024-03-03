package blender.shader.library;

import blender.shader.node.group.GroupShaderNode;
import blender.shader.node.group.ShaderNodeGroup;

import java.util.*;

public class ShaderLibrary {

    private final Map<String, ShaderNodeGroup> groups = new HashMap<>();

    public ShaderLibrary add(ShaderNodeGroup group) {
        Objects.requireNonNull(group, "group.name == null");

        groups.put(group.getName(), group);

        return this;
    }

    public ShaderNodeGroup get(String name) {
        return groups.get(name);
    }

    public Set<String> names() {
        return Collections.unmodifiableSet(groups.keySet());
    }

    public void doLinkage() {
        for (final var group : groups.values()) {
            for (final var node : group.getNodes()) {
                if (node instanceof GroupShaderNode groupShaderNode) {
                    groupShaderNode.link(this);
                }
            }
        }
    }

}