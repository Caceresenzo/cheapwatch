package blender.shader.node.input;

import java.util.Collections;
import java.util.List;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.node.ShaderNode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class VertexColorShaderNode extends ShaderNode {

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("Color", ShaderDataType.RGBA, 0),
            new ShaderSocket<>("Alpha", ShaderDataType.VALUE, 1)
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
    public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
        throw new UnsupportedOperationException();
    }

}