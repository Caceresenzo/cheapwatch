package blender.shader.node;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.ShaderVariable;
import lombok.ToString;
import org.joml.Vector4f;

import java.util.List;

@ToString(callSuper = true)
public class NormalMapShaderNode extends ShaderNode {

    public static final List<ShaderSocket<?>> INPUTS = List.of(
            new ShaderSocket<>("Strength", ShaderDataType.VALUE, 1.0f, 0),
            new ShaderSocket<>("Color", ShaderDataType.RGBA, new Vector4f(0.5f, 0.5f, 1.0f, 1.0f), 1)
    );

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("Normal", ShaderDataType.SHADER, 0)
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
        final var x = inputs.get(0);
        final var y = inputs.get(1);
        final var z = inputs.get(2);
        final var result = outputs.get(0);

        throw new UnsupportedOperationException();
    }

}