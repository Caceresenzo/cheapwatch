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
public class SeparateColorShaderNode extends ShaderNode {

    public static final List<ShaderSocket<?>> INPUTS = List.of(
            new ShaderSocket<>("Color", ShaderDataType.RGBA, new Vector4f(0.8f, 0.8f, 0.8f, 1.0f), 0)
    );

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("Red", ShaderDataType.VALUE, 0),
            new ShaderSocket<>("Green", ShaderDataType.VALUE, 1),
            new ShaderSocket<>("Blue", ShaderDataType.VALUE, 2)
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
        final var color = inputs.get(0);
        final var red = outputs.get(0);
        final var green = outputs.get(1);
        final var blue = outputs.get(2);

        generateCode(builder, color, red, "red");
        builder.append("\n");

        generateCode(builder, color, green, "green");
        builder.append("\n");

        generateCode(builder, color, blue, "blue");
    }

    public void generateCode(StringBuilder builder, ShaderVariable input, ShaderVariable output, String outputComponent) {
        if (output.unused()) {
            builder
                    .append("// ");
        }

        builder
                .append(output.type().getCodeType())
                .append(" ")
                .append(output.name())
                .append(" = ")
                .append(input.name())
                .append(".")
                .append(outputComponent)
                .append(";");

        if (output.unused()) {
            builder
                    .append(" /*unused*/");
        }
    }

}