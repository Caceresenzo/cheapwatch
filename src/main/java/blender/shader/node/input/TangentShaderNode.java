package blender.shader.node.input;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderVariable;
import blender.shader.node.ShaderNode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class TangentShaderNode extends ShaderNode {

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("Tangent", ShaderDataType.VECTOR, 0)
    );

    @Override
    public List<ShaderSocket<?>> getInputs() {
        return Collections.emptyList();
    }

    @Override
    public List<ShaderSocket<?>> getOutputs() {
        return OUTPUTS;
    }

    @Override
    public void generateCode(StringBuilder builder, List<ShaderVariable> inputs, List<ShaderVariable> outputs) {
        throw new UnsupportedOperationException();
    }

}