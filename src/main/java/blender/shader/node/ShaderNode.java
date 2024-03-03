package blender.shader.node;

import blender.shader.ShaderLink;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderVariable;
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

    public ShaderNode addInputOverrides(ShaderSocket<?> socket, Object defaultValue) {
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

    public ShaderNode addLink(ShaderSocket<?> fromSocket, ShaderNode toNode, ShaderSocket<?> toSocket) {
        final var link = new ShaderLink(
                this,
                fromSocket,
                toNode,
                toSocket
        );

        this.links.add(link);
        toNode.reverseLinks.add(link);

        return this;
    }

    public ShaderSocket<?> getInput(int index) {
        return getInputs().get(index);
    }

    public ShaderSocket<?> getOutput(int index) {
        return getOutputs().get(index);
    }

}