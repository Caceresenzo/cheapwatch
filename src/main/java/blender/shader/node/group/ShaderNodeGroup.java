package blender.shader.node.group;

import blender.shader.code.ShaderVariable;
import blender.shader.ShaderSocket;
import blender.shader.node.ShaderNode;
import blender.shader.graph.ShaderNodeGraph;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ToString(callSuper = true)
@Getter
public class ShaderNodeGroup implements ShaderNodeGraph {

    private @Setter String name;

    private final List<ShaderSocket<?>> inputs = new ArrayList<>();
    private final List<ShaderSocket<?>> outputs = new ArrayList<>();
    private final List<ShaderNode> nodes = new ArrayList<>();

    private final GroupOutputShaderNode outputNode;
    private final GroupInputShaderNode inputNode;

    public ShaderNodeGroup() {
        addNode(outputNode = new GroupOutputShaderNode(this));
        addNode(inputNode = new GroupInputShaderNode(this));
    }

    public ShaderNodeGroup addInput(ShaderSocket<?> input) {
        this.inputs.add(input);
        return this;
    }

    public ShaderNodeGroup addOutput(ShaderSocket<?> output) {
        this.outputs.add(output);
        return this;
    }

    public ShaderNodeGroup addNode(ShaderNode node) {
        this.nodes.add(node);
        return this;
    }

    public List<ShaderSocket<?>> getInputs() {
        return Collections.unmodifiableList(inputs);
    }

    public List<ShaderSocket<?>> getOutputs() {
        return Collections.unmodifiableList(outputs);
    }

    public List<ShaderNode> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    @Override
    public Optional<ShaderNode> getFinalNode() {
        return Optional.of(outputNode);
    }

}