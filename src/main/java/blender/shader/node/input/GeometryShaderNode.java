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
public class GeometryShaderNode extends ShaderNode {

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("Position", ShaderDataType.VECTOR, 0),
            new ShaderSocket<>("Normal", ShaderDataType.VECTOR, 1),
            new ShaderSocket<>("Tangent", ShaderDataType.VECTOR, 2),
            new ShaderSocket<>("True Normal", ShaderDataType.VECTOR, 3),
            new ShaderSocket<>("Incoming", ShaderDataType.VECTOR, 4),
            new ShaderSocket<>("Parametric", ShaderDataType.VECTOR, 5),
            new ShaderSocket<>("Backfacing", ShaderDataType.VALUE, 6),
            new ShaderSocket<>("Pointiness", ShaderDataType.VALUE, 7),
            new ShaderSocket<>("Random Per Island", ShaderDataType.VALUE, 8)
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
        throw new UnsupportedOperationException();
    }

}