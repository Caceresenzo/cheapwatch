package blender.shader.node;

import blender.shader.ShaderDataType;
import blender.shader.ShaderPort;
import blender.shader.ShaderVariable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.joml.Vector3f;

import java.util.List;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class MathVectorShaderNode extends ShaderNode {

    public static final List<ShaderPort> INPUTS = List.of(
            new ShaderPort("A", ShaderDataType.VECTOR, new Vector3f(), 0),
            new ShaderPort("B", ShaderDataType.VECTOR, new Vector3f(), 1),
            new ShaderPort("C", ShaderDataType.VECTOR, new Vector3f(), 2),
            new ShaderPort("Scale", ShaderDataType.VALUE, 1.0f, 3)
    );

    public static final List<ShaderPort> OUTPUTS = List.of(
            new ShaderPort("Vector", ShaderDataType.VECTOR, new Vector3f(), 0),
            new ShaderPort("Value", ShaderDataType.VALUE, 0.0f, 1)
    );

    private final Operation operation;

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
        var index = 0;
        if (!operation.isReturnsVector()) {
            index = 1;
        }

        final var result = outputs.get(index);

        builder
                .append(result.type().getCodeType())
                .append(" ")
                .append(result.name())
                .append(" = ");

        operation.generateCode(builder, inputs);

        builder
                .append(";");
    }

    @RequiredArgsConstructor
    @Getter
    public enum Operation {
        ADD(true) {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateBinaryCode(builder, inputs, "+");
            }

        },
        SUBTRACT(true) {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateBinaryCode(builder, inputs, "-");
            }

        },
        MULTIPLY(true) {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateBinaryCode(builder, inputs, "*");
            }

        },
        DIVIDE(true) {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateBinaryCode(builder, inputs, "/");
            }

        },
        //
//        CROSS_PRODUCT,
//        PROJECT,
//        REFLECT,
        DOT_PRODUCT(false) {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateFunctionCallCode(builder, inputs, "dot");
            }

        },
        //
//        DISTANCE,
//        LENGTH,
//        SCALE,
        NORMALIZE(true) {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateFunctionCallCode(builder, inputs, "normalize");
            }

        },
        //
//        SNAP,
//        FLOOR,
//        CEIL,
//        MODULO,
//        FRACTION,
//        ABSOLUTE,
//        MINIMUM,
//        MAXIMUM,
//        WRAP,
//        SINE,
//        COSINE,
//        TANGENT,
//        REFRACT,
//        FACEFORWARD,
//        MULTIPLY_ADD,
        _END(false) {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                throw new UnsupportedOperationException();
            }

        };

        private final boolean returnsVector;

        public abstract void generateCode(StringBuilder builder, List<ShaderVariable> inputs);

        private static void generateFunctionCallCode(StringBuilder builder, List<ShaderVariable> inputs, String functionName) {
            final var a = inputs.get(0);

            builder
                    .append("abs(")
                    .append(a.name())
                    .append(")");
        }

        private static void generateBinaryCode(StringBuilder builder, List<ShaderVariable> inputs, String operator) {
            final var a = inputs.get(0);
            final var b = inputs.get(1);

            builder
                    .append(a.name())
                    .append(" ")
                    .append(operator)
                    .append(" ")
                    .append(b.name());
        }

    }

}