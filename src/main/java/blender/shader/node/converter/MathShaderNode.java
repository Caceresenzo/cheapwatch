package blender.shader.node.converter;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderVariable;
import blender.shader.node.ShaderNode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

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
        ARCSINE {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateBiFunctionCallCode(builder, inputs, "asin");
            }

        },
        ARCCOSINE {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateBiFunctionCallCode(builder, inputs, "acos");
            }

        },
        //		ARCTANGENT,
        POWER {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateBiFunctionCallCode(builder, inputs, "pow");
            }

        },
        //		LOGARITHM,
        MINIMUM {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateBiFunctionCallCode(builder, inputs, "min");
            }

        },
        MAXIMUM {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateBiFunctionCallCode(builder, inputs, "max");
            }

        },
        //		ROUND,
        LESS_THAN {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateComparisonCode(builder, inputs, "<");
            }

        },
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

        },

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
        MULTIPLY_ADD {
            @Override
            public void generateCode(StringBuilder builder, List<ShaderVariable> inputs) {
                generateTrinaryCode(builder, inputs, "*", "+");
            }

        };
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

        private static void generateTrinaryCode(StringBuilder builder, List<ShaderVariable> inputs, String operator1, String operator2) {
            final var a = inputs.get(0);
            final var b = inputs.get(1);
            final var c = inputs.get(2);

            builder
                    .append(a.name())
                    .append(" ")
                    .append(operator1)
                    .append(" ")
                    .append(b.name())
                    .append(" ")
                    .append(operator2)
                    .append(" ")
                    .append(c.name());
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