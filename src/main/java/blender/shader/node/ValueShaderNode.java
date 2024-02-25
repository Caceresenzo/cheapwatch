package blender.shader.node;

import blender.shader.ShaderDataType;
import blender.shader.ShaderPort;
import blender.shader.ShaderVariable;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class ValueShaderNode extends ShaderNode {

    private final float value;

    public static final List<ShaderPort> OUTPUTS = List.of(
            new ShaderPort("Value", ShaderDataType.VALUE, 0.0f, 0)
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