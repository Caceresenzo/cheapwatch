package blender.shader.node.converter;

import java.util.List;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.code.ast.AstNode;
import blender.shader.code.ast.BinaryOperation;
import blender.shader.code.ast.FunctionCall;
import blender.shader.code.ast.Identifier;
import blender.shader.code.ast.Litteral;
import blender.shader.code.ast.Ternary;
import blender.shader.code.ast.VariableDeclaration;
import blender.shader.node.ShaderNode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class MathShaderNode extends ShaderNode {

	public static final List<ShaderSocket> INPUTS = List.of(
		new ShaderSocket("A", ShaderDataType.VALUE, 0.5f, 0),
		new ShaderSocket("B", ShaderDataType.VALUE, 0.5f, 1),
		new ShaderSocket("C", ShaderDataType.VALUE, 0.5f, 2)
	);

	public static final List<ShaderSocket> OUTPUTS = List.of(
		new ShaderSocket("Value", ShaderDataType.VALUE, 0.0f, 0)
	);

	private final Operation operation;
	private final boolean clamp;

	@Override
	public List<ShaderSocket> getInputs() {
		return INPUTS;
	}

	@Override
	public List<ShaderSocket> getOutputs() {
		return OUTPUTS;
	}

	@Override
	public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
		final var result = variables.getOutput(0);

		var node = operation.toAstNode(variables);
		if (clamp) {
			node = new FunctionCall(
				"clamp",
				List.of(
					node,
					new Litteral("0.0"),
					new Litteral("1.0")
				)
			);
		}

		final var block = new VariableDeclaration(
			result.type().getCodeType(),
			new Identifier(result.name()),
			node
		);

		writer.append(block);
	}

	public enum Operation {

		ADD {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new BinaryOperation(
					"+",
					new Identifier(variables.getInput(0).name()),
					new Identifier(variables.getInput(1).name())
				);
			}

		},
		SUBTRACT {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new BinaryOperation(
					"-",
					new Identifier(variables.getInput(0).name()),
					new Identifier(variables.getInput(1).name())
				);
			}

		},
		MULTIPLY {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new BinaryOperation(
					"*",
					new Identifier(variables.getInput(0).name()),
					new Identifier(variables.getInput(1).name())
				);
			}

		},
		DIVIDE {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new BinaryOperation(
					"/",
					new Identifier(variables.getInput(0).name()),
					new Identifier(variables.getInput(1).name())
				);
			}

		},

		//		SINE,
		//		COSINE,
		//		TANGENT,
		ARCSINE {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new FunctionCall(
					"asin",
					List.of(
						new Identifier(variables.getInput(0).name())
					)
				);
			}

		},
		ARCCOSINE {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new FunctionCall(
					"acos",
					List.of(
						new Identifier(variables.getInput(0).name())
					)
				);
			}

		},
		//		ARCTANGENT,
		POWER {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new FunctionCall(
					"pow",
					List.of(
						new Identifier(variables.getInput(0).name()),
						new Identifier(variables.getInput(1).name())
					)
				);
			}

		},
		//		LOGARITHM,
		MINIMUM {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new FunctionCall(
					"min",
					List.of(
						new Identifier(variables.getInput(0).name()),
						new Identifier(variables.getInput(1).name())
					)
				);
			}

		},
		MAXIMUM {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new FunctionCall(
					"max",
					List.of(
						new Identifier(variables.getInput(0).name()),
						new Identifier(variables.getInput(1).name())
					)
				);
			}

		},
		//		ROUND,
		LESS_THAN {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new Ternary(
					new BinaryOperation(
						"<",
						new Identifier(variables.getInput(0).name()),
						new Identifier(variables.getInput(1).name())
					),
					new Litteral("1.0"),
					new Litteral("0.0")
				);
			}

		},
		GREATER_THAN {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new Ternary(
					new BinaryOperation(
						">",
						new Identifier(variables.getInput(0).name()),
						new Identifier(variables.getInput(1).name())
					),
					new Litteral("1.0"),
					new Litteral("0.0")
				);
			}

		},
		//		MODULO,
		ABSOLUTE {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new FunctionCall(
					"abs",
					List.of(new Identifier(variables.getInput(0).name()))
				);
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
			public AstNode toAstNode(ShaderVariables variables) {
				return new BinaryOperation(
					"+",
					new BinaryOperation(
						"*",
						new Identifier(variables.getInput(0).name()),
						new Identifier(variables.getInput(1).name())
					),
					new Identifier(variables.getInput(2).name())
				);
			}

		};
		//		PINGPONG,
		//		SMOOTH_MIN,
		//		SMOOTH_MAX,
		//		FLOORED_MODULO,

		public abstract AstNode toAstNode(ShaderVariables variables);

	}

}