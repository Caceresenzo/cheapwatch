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
public class MixShaderNode extends ShaderNode {

    public static final List<ShaderSocket<?>> INPUTS = List.of(
            new ShaderSocket<>("Factor", "Factor_Float", ShaderDataType.VALUE, 0.5f, 0),
            new ShaderSocket<>("Factor", "Factor_Vector", ShaderDataType.VECTOR, new Vector3f(0.5f), 1),
            new ShaderSocket<>("A", "A_Float", ShaderDataType.VALUE, 0.0f, 2),
            new ShaderSocket<>("B", "B_Float", ShaderDataType.VALUE, 0.0f, 3),
            new ShaderSocket<>("A", "A_Vector", ShaderDataType.VECTOR, new Vector3f(0.0f), 4),
            new ShaderSocket<>("B", "B_Vector", ShaderDataType.VECTOR, new Vector3f(0.0f), 5),
            new ShaderSocket<>("A", "A_Color", ShaderDataType.RGBA, new Vector4f(0.5f, 0.5f, 0.5f, 1.0f), 6),
            new ShaderSocket<>("B", "B_Color", ShaderDataType.RGBA, new Vector4f(0.5f, 0.5f, 0.5f, 1.0f), 7),
            new ShaderSocket<>("A", "A_Rotation", ShaderDataType.ROTATION, 6),
            new ShaderSocket<>("B", "B_Rotation", ShaderDataType.ROTATION, 7)
    );

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("Result", "Result_Float", ShaderDataType.VALUE, null, 0),
            new ShaderSocket<>("Result", "Result_Vector", ShaderDataType.VECTOR, null, 1),
            new ShaderSocket<>("Result", "Result_Color", ShaderDataType.RGBA, null, 2)
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