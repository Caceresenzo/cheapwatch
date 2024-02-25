package blender.shader.node;

import blender.shader.ShaderDataType;
import blender.shader.ShaderPort;
import blender.shader.ShaderVariable;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Collections;
import java.util.List;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class VectorShaderNode extends ShaderNode {

    private final Vector3fc value;

    public static final List<ShaderPort> OUTPUTS = List.of(
            new ShaderPort("Vector", ShaderDataType.VECTOR, new Vector3f(0.0f), 0)
    );

    @Override
    public List<ShaderPort> getInputs() {
        return Collections.emptyList();
    }

    @Override
    public List<ShaderPort> getOutputs() {
        return OUTPUTS;
    }

    @Override
    public void generateCode(StringBuilder builder, List<ShaderVariable> inputs, List<ShaderVariable> outputs) {
        final var x = outputs.get(0);

        builder
                .append(x.type().getCodeType())
                .append(" ")
                .append(x.name())
                .append(" = ")
                .append(x.port().type().renderDefaultValue(value))
                .append(";");
    }

}