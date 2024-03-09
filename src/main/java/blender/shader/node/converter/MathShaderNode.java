package blender.shader.node.converter;

import java.util.List;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.node.ShaderNode;
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
    public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
        final var result = variables.getOutput(0);

        writer
                .declareAndAssign(result);

        if (clamp) {
            writer.prepareCall("clamp");
        }

        operation.generateCode(writer, variables);

        if (clamp) {
            writer.endCall("0.0", "1.0");
        }

        writer.endLine();
    }

    public enum Operation {
        ADD {
            @Override
            public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
                writer.useBinaryOperator("+", variables.getInput(0), variables.getInput(1));
            }

        },
        SUBTRACT {
            @Override
            public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
                writer.useBinaryOperator("-", variables.getInput(0), variables.getInput(1));
            }

        },
        MULTIPLY {
            @Override
            public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
                writer.useBinaryOperator("*", variables.getInput(0), variables.getInput(1));
            }

        },
        DIVIDE {
            @Override
            public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
                writer.useBinaryOperator("/", variables.getInput(0), variables.getInput(1));
            }

        },

        //		SINE,
        //		COSINE,
        //		TANGENT,
        ARCSINE {
            @Override
            public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
                writer.useFunctionCall("asin", variables.getInput(0));
            }

        },
        ARCCOSINE {
            @Override
            public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
                writer.useFunctionCall("acos", variables.getInput(0));
            }

        },
        //		ARCTANGENT,
        POWER {
            @Override
            public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
                writer.useBiFunctionCall("pow", variables.getInput(0), variables.getInput(1));
            }

        },
        //		LOGARITHM,
        MINIMUM {
            @Override
            public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
                writer.useBiFunctionCall("min", variables.getInput(0), variables.getInput(1));
            }

        },
        MAXIMUM {
            @Override
            public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
                writer.useBiFunctionCall("max", variables.getInput(0), variables.getInput(1));
            }

        },
        //		ROUND,
        LESS_THAN {
            @Override
            public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
                writer.useComparison("<", variables.getInput(0), variables.getInput(1));
            }

        },
        GREATER_THAN {
            @Override
            public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
                writer.useComparison(">", variables.getInput(0), variables.getInput(1));
            }

        },
        //		MODULO,
        ABSOLUTE {
            @Override
            public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
                writer.useFunctionCall("abs", variables.getInput(0));
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
            public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
                writer.useTrinaryOperator("*", "+", variables.getInput(0), variables.getInput(1), variables.getInput(2));
            }

        };
        //		PINGPONG,
        //		SMOOTH_MIN,
        //		SMOOTH_MAX,
        //		FLOORED_MODULO,

        public abstract void generateCode(ShaderCodeWriter writer, ShaderVariables variables);

    }

}