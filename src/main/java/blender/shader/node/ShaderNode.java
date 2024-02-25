package blender.shader.node;

import blender.shader.ShaderLink;
import blender.shader.ShaderPort;
import blender.shader.ShaderVariable;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.EqualsExclude;

import java.util.*;

@Data
@Accessors(chain = true)
public abstract class ShaderNode {

    private String name;
    private final Map<ShaderPort, Object> inputOverrides = new HashMap<>();
    private final List<ShaderLink> links = new ArrayList<>();
    private final List<ShaderLink> reverseLinks = new ArrayList<>();

    public abstract List<ShaderPort> getInputs();

    public abstract List<ShaderPort> getOutputs();

    public abstract void generateCode(StringBuilder builder, List<ShaderVariable> inputs, List<ShaderVariable> outputs);

    public ShaderNode addInputOverrides(int portIndex, JsonNode valueNode) {
        final var port = getInputs().get(portIndex);
        final var defaultValue = port.type().parseDefaultValue(valueNode);

        inputOverrides.put(port, defaultValue);

        return this;
    }

    public ShaderNode addLink(String fromPortName, ShaderNode toNode, String toPortName) {
        return addLink(
            getOutput(fromPortName).orElseThrow(),
            toNode,
            toNode.getInput(toPortName).orElseThrow()
        );
    }

    public ShaderNode addLink(int fromPortIndex, ShaderNode toNode, int toPortIndex) {
        return addLink(
            this.getOutputs().get(fromPortIndex),
            toNode,
            toNode.getInputs().get(toPortIndex)
        );
    }

    public ShaderNode addLink(ShaderPort fromPort, ShaderNode toNode, ShaderPort toPort) {
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

    public Optional<ShaderPort> getInput(String name) {
        return findShaderPort(getInputs(), name);
    }

    public Optional<ShaderPort> getOutput(String name) {
        return findShaderPort(getOutputs(), name);
    }

    private Optional<ShaderPort> findShaderPort(List<ShaderPort> ports, String name) {
        return ports.stream()
                .filter((port) -> port.name().equalsIgnoreCase(name))
                .findFirst();
    }

}