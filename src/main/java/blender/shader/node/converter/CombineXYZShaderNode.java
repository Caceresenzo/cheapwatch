package blender.shader.node.converter;

import blender.shader.ShaderDataType;
import blender.shader.code.ShaderVariable;
import blender.shader.ShaderSocket;
import blender.shader.node.ShaderNode;
import lombok.ToString;
import org.joml.Vector3f;

import java.util.List;

@ToString(callSuper = true)
public class CombineXYZShaderNode extends ShaderNode {

    public static final List<ShaderSocket<?>> INPUTS = List.of(
            new ShaderSocket<>("X", ShaderDataType.VALUE, 0.0f, 0),
            new ShaderSocket<>("Y", ShaderDataType.VALUE, 0.0f, 1),
            new ShaderSocket<>("Z", ShaderDataType.VALUE, 0.0f, 2)
    );

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("Vector", ShaderDataType.VECTOR, new Vector3f(), 0)
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

        builder
                .append(result.type().getCodeType())
                .append(" ")
                .append(result.name())
                .append(" = vec3(")
                .append(x.name())
                .append(", ")
                .append(y.name())
                .append(", ")
                .append(z.name())
                .append(");");
    }

}