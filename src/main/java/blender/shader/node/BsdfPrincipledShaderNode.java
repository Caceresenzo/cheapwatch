package blender.shader.node;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.ShaderVariable;
import lombok.ToString;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

@ToString(callSuper = true)
public class BsdfPrincipledShaderNode extends ShaderNode {

    public static final List<ShaderSocket<?>> INPUTS = List.of(
            new ShaderSocket<>("Base Color", ShaderDataType.RGBA, new Vector4f(0.8f, 0.8f, 0.8f, 1.0f), 0),
            new ShaderSocket<>("Metallic", ShaderDataType.VALUE, 0.0f, 1),
            new ShaderSocket<>("Roughness", ShaderDataType.VALUE, 0.5f, 2),
            new ShaderSocket<>("IOR", ShaderDataType.VALUE, 1.5f, 3),
            new ShaderSocket<>("Alpha", ShaderDataType.VALUE, 1.0f, 4),
            new ShaderSocket<>("Normal", ShaderDataType.VECTOR, 5),
            new ShaderSocket<>("Weight", ShaderDataType.VALUE, 6),

            new ShaderSocket<>("Subsurface Weight", ShaderDataType.VALUE, 0.0f, 7),
            new ShaderSocket<>("Subsurface Radius", ShaderDataType.VECTOR, new Vector3f(1.0f, 0.2f, 0.1f), 8),
            new ShaderSocket<>("Subsurface Scale", ShaderDataType.VALUE, 0.05f, 9),
            new ShaderSocket<>("Subsurface IOR", ShaderDataType.VALUE, 1.4f, 10),
            new ShaderSocket<>("Subsurface Anisotropy", ShaderDataType.VALUE, 0.0f, 11),

            new ShaderSocket<>("Specular IOR Level", ShaderDataType.VALUE, 0.5f, 12),
            new ShaderSocket<>("Specular Tint", ShaderDataType.RGBA, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), 13),

            new ShaderSocket<>("Anisotropic", ShaderDataType.VALUE, 0.0f, 14),
            new ShaderSocket<>("Anisotropic Rotation", ShaderDataType.VALUE, 0.0f, 15),

            new ShaderSocket<>("Tangent", ShaderDataType.VECTOR, 16),
            new ShaderSocket<>("Transmission Weight", ShaderDataType.VALUE, 0.0f, 17),

            new ShaderSocket<>("Coat Weight", ShaderDataType.VALUE, 0.0f, 18),
            new ShaderSocket<>("Coat Roughness", ShaderDataType.VALUE, 0.03f, 19),
            new ShaderSocket<>("Coat IOR", ShaderDataType.VALUE, 1.5f, 20),
            new ShaderSocket<>("Coat Tint", ShaderDataType.RGBA, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), 21),
            new ShaderSocket<>("Coat Normal", ShaderDataType.VECTOR, 22),

            new ShaderSocket<>("Sheen Weight", ShaderDataType.VALUE, 0.0f, 23),
            new ShaderSocket<>("Sheen Roughness", ShaderDataType.VALUE, 0.5f, 24),
            new ShaderSocket<>("Sheen Tint", ShaderDataType.RGBA, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), 25),

            new ShaderSocket<>("Emission Color", ShaderDataType.RGBA, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), 26),
            new ShaderSocket<>("Emission Strength", ShaderDataType.VALUE, 0.0f, 27)
    );

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("Shader", ShaderDataType.VALUE, null, 0)
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

        throw new UnsupportedOperationException();
    }

}