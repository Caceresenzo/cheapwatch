package blender.shader.group;

import blender.shader.ShaderVariable;
import blender.shader.ShaderSocket;
import blender.shader.node.ShaderNode;
import blender.shader.graph.ShaderNodeGraph;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
public class ShaderNodeGroup extends ShaderNode implements ShaderNodeGraph {

    private final List<ShaderSocket<?>> inputs = new ArrayList<>();
    private final List<ShaderSocket<?>> outputs = new ArrayList<>();
    private final List<ShaderNode> nodes = new ArrayList<>();

    private final ShaderGroupOutputNode outputNode;
    private final ShaderGroupInputNode inputNode;

    public ShaderNodeGroup() {
        addNode(outputNode = new ShaderGroupOutputNode(this));
        addNode(inputNode = new ShaderGroupInputNode(this));
    }

    @Override
    public void generateCode(StringBuilder builder, List<ShaderVariable> inputs, List<ShaderVariable> outputs) {}

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