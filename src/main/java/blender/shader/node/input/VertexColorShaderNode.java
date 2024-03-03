package blender.shader.node.input;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderVariable;
import blender.shader.node.ShaderNode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class VertexColorShaderNode extends ShaderNode {

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("Color", ShaderDataType.RGBA, 0),
            new ShaderSocket<>("Alpha", ShaderDataType.VALUE, 1)
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
        final var color = outputs.get(0);
        final var alpha = outputs.get(1);

        builder
                .append(color.type().getCodeType())
                .append(" ")
                .append(color.name())
                .append(" = ")
                .append("in_COLOR")
                .append(";");

        builder
                .append(alpha.type().getCodeType())
                .append(" ")
                .append(alpha.name())
                .append(" = ")
                .append("in_ALPHA")
                .append(";");
    }

}