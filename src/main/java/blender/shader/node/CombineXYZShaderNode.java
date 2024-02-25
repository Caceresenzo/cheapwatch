package blender.shader.node;

import blender.shader.ShaderDataType;
import blender.shader.ShaderVariable;
import blender.shader.ShaderPort;
import lombok.ToString;
import org.joml.Vector3f;

import java.util.List;

@ToString(callSuper = true)
public class CombineXYZShaderNode extends ShaderNode {

    public static final List<ShaderPort> INPUTS = List.of(
            new ShaderPort("X", ShaderDataType.VALUE, 0.0f, 0),
            new ShaderPort("Y", ShaderDataType.VALUE, 0.0f, 1),
            new ShaderPort("Z", ShaderDataType.VALUE, 0.0f, 2)
    );

    public static final List<ShaderPort> OUTPUTS = List.of(
            new ShaderPort("Vector", ShaderDataType.VECTOR, new Vector3f(), 0)
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