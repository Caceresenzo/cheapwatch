package blender.shader.node.converter;

import java.util.List;

import org.joml.Vector3f;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.node.ShaderNode;
import lombok.ToString;

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
    public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
        final var x = variables.getInput(0);
        final var y = variables.getInput(1);
        final var z = variables.getInput(2);
        final var result = variables.getOutput(0);

        writer
                .declareAndAssign(result)
                .value("vec3", x.name(), y.name(), z.name())
                .endLine();
    }

}