package blender.shader.node;

import blender.shader.ShaderDataType;
import blender.shader.ShaderPort;
import blender.shader.ShaderVariable;
import lombok.ToString;
import org.joml.Vector3f;

import java.util.List;

@ToString(callSuper = true)
public class SeparateXYZShaderNode extends ShaderNode {

    public static final List<ShaderPort> INPUTS = List.of(
            new ShaderPort("Vector", ShaderDataType.VECTOR, new Vector3f(), 0)
    );

    public static final List<ShaderPort> OUTPUTS = List.of(
            new ShaderPort("X", ShaderDataType.VALUE, 0.0f, 0),
            new ShaderPort("Y", ShaderDataType.VALUE, 0.0f, 1),
            new ShaderPort("Z", ShaderDataType.VALUE, 0.0f, 2)
    );

    @Override
    public List<ShaderPort> getInputs() {
        return INPUTS;
    }

    @Override
    public List<ShaderPort> getOutputs() {
        return OUTPUTS;
    }

    @Override
    public void generateCode(StringBuilder builder, List<ShaderVariable> inputs, List<ShaderVariable> outputs) {
        final var vector = inputs.get(0);
        final var x = outputs.get(0);
        final var y = outputs.get(1);
        final var z = outputs.get(2);

        generateCode(builder, vector, x, "x");
        builder.append("\n");

        generateCode(builder, vector, y, "y");
        builder.append("\n");

        generateCode(builder, vector, z, "z");
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