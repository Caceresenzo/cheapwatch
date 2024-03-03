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
public class CombineColorShaderNode extends ShaderNode {

    public static final List<ShaderSocket<?>> INPUTS = List.of(
            new ShaderSocket<>("Red", ShaderDataType.VALUE, 0.0f, 0),
            new ShaderSocket<>("Green", ShaderDataType.VALUE, 0.0f, 1),
            new ShaderSocket<>("Blue", ShaderDataType.VALUE, 0.0f, 2)
    );

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("Color", ShaderDataType.RGBA, new Vector4f(0.8f, 0.8f, 0.8f, 1.0f), 0)
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
        final var red = inputs.get(0);
        final var green = inputs.get(1);
        final var blue = inputs.get(2);
        final var result = outputs.get(0);

        builder
                .append(result.type().getCodeType())
                .append(" ")
                .append(result.name())
                .append(" = vec4(")
                .append(red.name())
                .append(", ")
                .append(green.name())
                .append(", ")
                .append(blue.name())
                .append(", ")
                .append(1.0f)
                .append(");");
    }

}