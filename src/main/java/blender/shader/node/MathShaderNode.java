package blender.shader.node;

import java.util.List;

import blender.shader.ShaderDataType;
import blender.shader.ShaderPort;
import blender.shader.ShaderVariable;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class MathShaderNode extends ShaderNode {

    public static final List<ShaderPort> INPUTS = List.of(
            new ShaderPort("A", ShaderDataType.VALUE, 0.5f, 0),
            new ShaderPort("B", ShaderDataType.VALUE, 0.5f, 1),
            new ShaderPort("C", ShaderDataType.VALUE, 0.5f, 2)
    );

    public static final List<ShaderPort> OUTPUTS = List.of(
            new ShaderPort("Value", ShaderDataType.VALUE, 0.0f, 0)
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
        final var result = outputs.get(0);

        builder
                .append(result.type().getCodeType())
                .append(" ")
                .append(result.name())
                .append(" = ");

        operation.generateCode(builder, inputs);

        builder
                .append(";");
    }

    public enum Operation {
        ADD {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateBinaryCode(builder, inputs, "+");
            }

        },
        SUBTRACT {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateBinaryCode(builder, inputs, "-");
            }

        },
        MULTIPLY {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateBinaryCode(builder, inputs, "*");
            }

        },
        DIVIDE {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateBinaryCode(builder, inputs, "/");
            }

        },

        //		SINE,
        //		COSINE,
        //		TANGENT,
        //		ARCSINE,
        //		ARCCOSINE,
        //		ARCTANGENT,
        //		POWER,
        //		LOGARITHM,
        //		MINIMUM,
        //		MAXIMUM,
        //		ROUND,
        //		LESS_THAN,
        GREATER_THAN {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateComparisonCode(builder, inputs, ">");
            }

        },
        //		MODULO,
        ABSOLUTE {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateFunctionCallCode(builder, inputs, "abs");
            }

        };

        //		ARCTAN2,
        //		FLOOR,
        //		CEIL,
        //		FRACTION,
        //		SQRT,
        //		INV_SQRT,
        //		SIGN,
        //		EXPONENT,
        //		RADIANS,
        //		DEGREES,
        //		SINH,
        //		COSH,
        //		TANH,
        //		TRUNC,
        //		SNAP,
        //		WRAP,
        //		COMPARE,
        //		MULTIPLY_ADD,
        //		PINGPONG,
        //		SMOOTH_MIN,
        //		SMOOTH_MAX,
        //		FLOORED_MODULO,

        public abstract void generateCode(StringBuilder builder, List<ShaderVariable> inputs);

        private static void generateFunctionCallCode(StringBuilder builder, List<ShaderVariable> inputs, String functionName) {
            final var a = inputs.get(0);

            builder
                    .append(functionName)
                    .append("(")
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

        private static void generateComparisonCode(StringBuilder builder, List<ShaderVariable> inputs, String operator) {
            final var a = inputs.get(0);
            final var b = inputs.get(1);

            builder
                    .append(a.name())
                    .append(" ")
                    .append(operator)
                    .append(" ")
                    .append(b.name())
                    .append(" ? 1.0f : 0.0f");
        }

    }

}