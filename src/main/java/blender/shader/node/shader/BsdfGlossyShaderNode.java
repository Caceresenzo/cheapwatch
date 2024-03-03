package blender.shader.node.shader;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderVariable;
import blender.shader.node.ShaderNode;
import lombok.ToString;
import org.joml.Vector4f;

import java.util.List;

@ToString(callSuper = true)
public class BsdfGlossyShaderNode extends ShaderNode {

    public static final List<ShaderSocket<?>> INPUTS = List.of(
            new ShaderSocket<>("Color", ShaderDataType.RGBA, new Vector4f(0.8f, 0.8f, 0.8f, 1.0f), 0),
            new ShaderSocket<>("Roughness", ShaderDataType.VALUE, 0.5f, 1),
            new ShaderSocket<>("Anisotropy", ShaderDataType.VALUE, 0.0f, 2),
            new ShaderSocket<>("Rotation", ShaderDataType.VALUE, 0.0f, 3),
            new ShaderSocket<>("Normal", ShaderDataType.VECTOR, 4),
            new ShaderSocket<>("Tangent", ShaderDataType.VECTOR, 5),
            new ShaderSocket<>("Weight", ShaderDataType.VECTOR, 6)
    );

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("BSDF", ShaderDataType.SHADER, 0)
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