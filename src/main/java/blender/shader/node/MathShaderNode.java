package blender.shader.node;

import java.util.List;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.ShaderVariable;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class MathShaderNode extends ShaderNode {

    public static final List<ShaderSocket<?>> INPUTS = List.of(
            new ShaderSocket<>("A", ShaderDataType.VALUE, 0.5f, 0),
            new ShaderSocket<>("B", ShaderDataType.VALUE, 0.5f, 1),
            new ShaderSocket<>("C", ShaderDataType.VALUE, 0.5f, 2)
    );

    public static final List<ShaderSocket<?>> OUTPUTS = List.of(
            new ShaderSocket<>("Value", ShaderDataType.VALUE, 0.0f, 0)
    );

    private final Operation operation;
    private final boolean clamp;

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
        final var result = outputs.get(0);

        builder
                .append(result.type().getCodeType())
                .append(" ")
                .append(result.name())
                .append(" = ");

        if (clamp) {
            builder
                    .append("clamp(");
        }

        operation.generateCode(builder, inputs);

        if (clamp) {
            builder
                    .append(", 0.0f, 1.0f)");
        }

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
        POWER {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateBiFunctionCallCode(builder, inputs, "pow");
            }

        },
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

        private static void generateBiFunctionCallCode(StringBuilder builder, List<ShaderVariable> inputs, String functionName) {
            final var a = inputs.get(0);
            final var b = inputs.get(1);

            builder
                    .append(functionName)
                    .append("(")
                    .append(a.name())
                    .append(", ")
                    .append(b.name())
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