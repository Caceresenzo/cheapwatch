package blender.shader.node.converter;

import java.util.List;

import org.joml.Vector3f;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.code.ast.AstNode;
import blender.shader.code.ast.BinaryOperation;
import blender.shader.code.ast.FunctionCall;
import blender.shader.code.ast.Identifier;
import blender.shader.code.ast.VariableDeclaration;
import blender.shader.node.ShaderNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class VectorMathShaderNode extends ShaderNode {

	public static final List<ShaderSocket> INPUTS = List.of(
		new ShaderSocket("Vector", ShaderDataType.VECTOR, new Vector3f(), 0),
		new ShaderSocket("Vector", "Vector_001", ShaderDataType.VECTOR, new Vector3f(), 1),
		new ShaderSocket("Vector", "Vector_002", ShaderDataType.VECTOR, new Vector3f(), 2),
		new ShaderSocket("Scale", ShaderDataType.VALUE, 1.0f, 3)
	);

	public static final List<ShaderSocket> OUTPUTS = List.of(
		new ShaderSocket("Vector", ShaderDataType.VECTOR, new Vector3f(), 0),
		new ShaderSocket("Value", ShaderDataType.VALUE, 0.0f, 1)
	);

	private final Operation operation;

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
		var index = 0;
		if (!operation.isReturnsVector()) {
			index = 1;
		}

		final var result = variables.getOutput(index);

		final var node = operation.toAstNode(variables);

		final var block = new VariableDeclaration(
			result.type().getCodeType(),
			result.name(),
			node
		);

		writer.append(block);
	}

	@RequiredArgsConstructor
	@Getter
	public enum Operation {

		ADD(true) {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new BinaryOperation(
					new Identifier(variables.getInput(0).name()),
					"+",
					new Identifier(variables.getInput(1).name())
				);
			}

		},
		SUBTRACT(true) {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new BinaryOperation(
					new Identifier(variables.getInput(0).name()),
					"-",
					new Identifier(variables.getInput(1).name())
				);
			}

		},
		MULTIPLY(true) {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new BinaryOperation(
					new Identifier(variables.getInput(0).name()),
					"*",
					new Identifier(variables.getInput(1).name())
				);
			}

		},
		DIVIDE(true) {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new BinaryOperation(
					new Identifier(variables.getInput(0).name()),
					"/",
					new Identifier(variables.getInput(1).name())
				);
			}

		},
		//
		CROSS_PRODUCT(true) {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new FunctionCall(
					"cross",
					new Identifier(variables.getInput(0).name()),
					new Identifier(variables.getInput(1).name())
				);
			}

		},
		//        PROJECT,
		//        REFLECT,
		DOT_PRODUCT(false) {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new FunctionCall(
					"dot",
					new Identifier(variables.getInput(0).name()),
					new Identifier(variables.getInput(1).name())
				);
			}

		},
		//
		//        DISTANCE,
		LENGTH(false) {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new FunctionCall(
					"length",
					new Identifier(variables.getInput(0).name())
				);
			}

		},
		//        SCALE,
		NORMALIZE(true) {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new FunctionCall(
					"normalize",
					new Identifier(variables.getInput(0).name())
				);
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
		SINE(true) {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new FunctionCall(
					"sin",
					new Identifier(variables.getInput(0).name())
				);
			}

		},
		COSINE(true) {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new FunctionCall(
					"cos",
					new Identifier(variables.getInput(0).name())
				);
			}

		},
		//        TANGENT,
		//        REFRACT,
		//        FACEFORWARD,
		MULTIPLY_ADD(true) {

			@Override
			public AstNode toAstNode(ShaderVariables variables) {
				return new BinaryOperation(
					new Identifier(variables.getInput(0).name()),
					"*",
					new Identifier(variables.getInput(1).name()),
					"+",
					new Identifier(variables.getInput(2).name())
				);
			}

		};

		private final boolean returnsVector;

		public abstract AstNode toAstNode(ShaderVariables variables);

	}

}