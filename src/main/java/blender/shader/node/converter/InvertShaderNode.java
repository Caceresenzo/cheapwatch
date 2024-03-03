package blender.shader.node.converter;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderVariable;
import blender.shader.node.ShaderNode;
import lombok.ToString;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

@ToString(callSuper = true)
public class InvertShaderNode extends ShaderNode {

    public static final List<ShaderSocket<?>> INPUTS = List.of(
            new ShaderSocket<>("Fac", ShaderDataType.VALUE, 1.0f, 0),
            new ShaderSocket<>("Color", ShaderDataType.RGBA, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f), 1)
    );

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("Color", ShaderDataType.RGBA, 0)
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