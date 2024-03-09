package blender.shader.node.converter;

import java.util.List;

import org.joml.Vector3f;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
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

		writer.declareAndAssign(result);

		operation.generateCode(writer, variables);

		writer.endLine();
	}

	@RequiredArgsConstructor
	@Getter
	public enum Operation {

		ADD(true) {

			@Override
			public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
				writer.useBinaryOperator("+", variables.getInput(0), variables.getInput(1));
			}

		},
		SUBTRACT(true) {

			@Override
			public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
				writer.useBinaryOperator("-", variables.getInput(0), variables.getInput(1));
			}

		},
		MULTIPLY(true) {

			@Override
			public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
				writer.useBinaryOperator("*", variables.getInput(0), variables.getInput(1));
			}

		},
		DIVIDE(true) {

			@Override
			public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
				writer.useBinaryOperator("/", variables.getInput(0), variables.getInput(1));
			}

		},
		//
		CROSS_PRODUCT(true) {

			@Override
			public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
				writer.useBiFunctionCall("cross", variables.getInput(0), variables.getInput(1));
			}

		},
		//        PROJECT,
		//        REFLECT,
		DOT_PRODUCT(false) {

			@Override
			public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
				writer.useBiFunctionCall("dot", variables.getInput(0), variables.getInput(1));
			}

		},
		//
		//        DISTANCE,
		LENGTH(false) {

			@Override
			public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
				writer.useFunctionCall("length", variables.getInput(0));
			}

		},
		//        SCALE,
		NORMALIZE(true) {

			@Override
			public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
				writer.useFunctionCall("normalize", variables.getInput(0));
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
			public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
				writer.useFunctionCall("sin", variables.getInput(0));
			}

		},
		COSINE(true) {

			@Override
			public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
				writer.useFunctionCall("cos", variables.getInput(0));
			}

		},
		//        TANGENT,
		//        REFRACT,
		//        FACEFORWARD,
		MULTIPLY_ADD(true) {

			@Override
			public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
				writer.useTrinaryOperator("*", "+", variables.getInput(0), variables.getInput(1), variables.getInput(2));
			}

		},
		_END(false) {

			@Override
			public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
				throw new UnsupportedOperationException();
			}

		};

		private final boolean returnsVector;

		public abstract void generateCode(ShaderCodeWriter writer, ShaderVariables variables);

	}

}