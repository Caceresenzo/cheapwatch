package blender.shader.node.input;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderVariable;
import blender.shader.node.ShaderNode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.joml.Vector3fc;

import java.util.Collections;
import java.util.List;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class VectorShaderNode extends ShaderNode {

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("Vector", ShaderDataType.VECTOR, 0)
    );

    private final Vector3fc value;

    @Override
    public List<ShaderSocket<?>> getInputs() {
        return Collections.emptyList();
    }

    @Override
    public List<ShaderSocket<?>> getOutputs() {
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
                .append(x.port().type().render(value))
                .append(";");
    }

}