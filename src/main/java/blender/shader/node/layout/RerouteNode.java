package blender.shader.node.layout;

import blender.shader.ShaderSocket;
import blender.shader.code.ShaderVariable;
import blender.shader.node.ShaderNode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ToString(callSuper = true)
public class RerouteNode extends ShaderNode {

    private final List<ShaderSocket<?>> inputs = new ArrayList<>();
    private final List<ShaderSocket<?>> outputs = new ArrayList<>();

    @Override
    public List<ShaderSocket<?>> getInputs() {
        return Collections.unmodifiableList(inputs);
    }

    @Override
    public List<ShaderSocket<?>> getOutputs() {
        return Collections.unmodifiableList(outputs);
    }

    @Override
    public void generateCode(StringBuilder builder, List<ShaderVariable> inputs, List<ShaderVariable> outputs) {
        builder.append("reroute");
    }

    public RerouteNode addInput(ShaderSocket<?> socket) {
        this.inputs.add(socket);
        return this;
    }

    public RerouteNode addOutput(ShaderSocket<?> socket) {
        this.outputs.add(socket);
        return this;
    }

}