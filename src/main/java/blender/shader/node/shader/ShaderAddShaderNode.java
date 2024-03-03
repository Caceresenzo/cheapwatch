package blender.shader.node.shader;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderVariable;
import blender.shader.node.ShaderNode;
import lombok.ToString;

import java.util.List;

@ToString(callSuper = true)
public class ShaderAddShaderNode extends ShaderNode {

    public static final List<ShaderSocket<?>> INPUTS = List.of(
            new ShaderSocket<>("Shader", ShaderDataType.SHADER, 0),
            new ShaderSocket<>("Shader", "Shader_001", ShaderDataType.SHADER, 1)
    );

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("Shader", ShaderDataType.SHADER, 0)
    );

    @Override
    public List<ShaderSocket<?>> getInputs() {
        return INPUTS;
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