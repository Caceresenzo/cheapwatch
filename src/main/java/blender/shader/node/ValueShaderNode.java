package blender.shader.node;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.ShaderVariable;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class ValueShaderNode extends ShaderNode {

    private final float value;

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("Value", ShaderDataType.VALUE, 0)
    );

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