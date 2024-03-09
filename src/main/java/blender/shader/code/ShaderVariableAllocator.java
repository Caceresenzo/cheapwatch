package blender.shader.code;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.node.ShaderNode;

public class ShaderVariableAllocator {

    private final Map<Map.Entry<ShaderNode, ShaderSocket>, ShaderVariable> allocatedSockets = new HashMap<>();
    private final Set<String> allocatedNames = new HashSet<>();

    public <T> ShaderVariable<T> getOrAllocateSocket(ShaderNode node, ShaderSocket<T> socket, boolean linked) {
        final var key = Map.entry(node, (ShaderSocket) socket);
        var variable = allocatedSockets.get(key);

        if (variable == null) {
            final var baseName = sanitize("%s_%s".formatted(node.getName(), socket.name()));

            final var name = allocateName(baseName);
            variable = new ShaderVariable<>(name, socket, linked);

            allocatedSockets.put(key, variable);
        }

        return variable;
    }

    public <T> ShaderVariable<T> allocateTemporary(String hint, ShaderDataType<T> type) {
        final var baseName = sanitize(hint);
        final var name = allocateName(baseName);

        return new ShaderVariable<>(name, type);
    }

    public String allocateName(String baseName) {
        String name;
        for (int index = 0; ; index++) {
            name = "%s_%s".formatted(baseName, index);
            if (!allocatedNames.contains(name)) {
                break;
            }
        }

        allocatedNames.add(name);

        return name;
    }

    public String sanitize(String input) {
        return input
                .replace(" ", "_")
                .replace(".", "_");
    }

}