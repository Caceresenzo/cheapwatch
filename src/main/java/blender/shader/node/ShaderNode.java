package blender.shader.node;

import blender.shader.ShaderLink;
import blender.shader.ShaderSocket;
import blender.shader.ShaderVariable;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;

@Data
@Accessors(chain = true)
public abstract class ShaderNode {

    private String name;
    private final Map<ShaderSocket<?>, Object> inputOverrides = new HashMap<>();
    private final List<ShaderLink> links = new ArrayList<>();
    private final List<ShaderLink> reverseLinks = new ArrayList<>();

    public abstract List<ShaderSocket<?>> getInputs();

    public abstract List<ShaderSocket<?>> getOutputs();

    public abstract void generateCode(StringBuilder builder, List<ShaderVariable> inputs, List<ShaderVariable> outputs);

    public ShaderNode addInputOverrides(ShaderSocket<?> socket, JsonNode valueNode) {
        final var defaultValue = socket.type().parse(valueNode);

        inputOverrides.put(socket, defaultValue);

        return this;
    }

    public ShaderNode addLink(int fromPortIndex, ShaderNode toNode, int toPortIndex) {
        return addLink(
                this.getOutputs().get(fromPortIndex),
                toNode,
                toNode.getInputs().get(toPortIndex)
        );
    }

    public ShaderNode addLink(ShaderSocket<?> fromPort, ShaderNode toNode, ShaderSocket<?> toPort) {
        final var link = new ShaderLink(
                this,
                fromPort,
                toNode,
                toPort
        );

        this.links.add(link);
        toNode.reverseLinks.add(link);

        return this;
    }

    public Optional<ShaderSocket<?>> getInput(int index, String name) {
        return findShaderPort(getInputs(), index, name);
    }

    public Optional<ShaderSocket<?>> getOutput(int index, String name) {
        return findShaderPort(getOutputs(), index, name);
    }

    private Optional<ShaderSocket<?>> findShaderPort(List<ShaderSocket<?>> ports, int index, String name) {
        final var found = ports.stream()
                .filter((port) -> port.name().equalsIgnoreCase(name))
                .toList();

        if (found.size() == 1) {
            return Optional.of(found.getFirst());
        }

        return Optional.of(ports.get(index));
    }

}