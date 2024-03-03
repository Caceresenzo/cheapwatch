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
public class UVMapShaderNode extends ShaderNode {

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("UV", ShaderDataType.VECTOR, 0)
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
        final var uv = outputs.get(0);

        builder
                .append(uv.type().getCodeType())
                .append(" ")
                .append(uv.name())
                .append(" = ")
                .append("in_UV")
                .append(";");
    }

}