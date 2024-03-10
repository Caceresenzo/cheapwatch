package blender.shader.node.converter;

import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4f;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariable;
import blender.shader.code.ShaderVariables;
import blender.shader.code.ast.BinaryOperation;
import blender.shader.code.ast.Expression;
import blender.shader.code.ast.FunctionCall;
import blender.shader.code.ast.Identifier;
import blender.shader.code.ast.Litteral;
import blender.shader.code.ast.MemberAccess;
import blender.shader.code.ast.Paranthesis;
import blender.shader.code.ast.VariableDeclaration;
import blender.shader.node.ShaderNode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import opengl.util.Vectors;

/**
 * @author https://github.com/blender/blender/blob/703353b5dafc344ac4080d280312ef3aa496b6de/source/blender/nodes/shader/nodes/node_shader_mix.cc#L341
 * @author https://github.com/blender/blender/blob/703353b5dafc344ac4080d280312ef3aa496b6de/source/blender/blenkernel/intern/material.cc#L1692
 */
@ToString(callSuper = true)
@RequiredArgsConstructor
public class MixShaderNode extends ShaderNode {

	public static final List<ShaderSocket> INPUTS = List.of(
		new ShaderSocket("Factor", "Factor_Float", ShaderDataType.VALUE, 0.5f, 0),
		new ShaderSocket("Factor", "Factor_Vector", ShaderDataType.VECTOR, new Vector3f(0.5f), 1),
		new ShaderSocket("A", "A_Float", ShaderDataType.VALUE, 0.0f, 2),
		new ShaderSocket("B", "B_Float", ShaderDataType.VALUE, 0.0f, 3),
		new ShaderSocket("A", "A_Vector", ShaderDataType.VECTOR, new Vector3f(0.0f), 4),
		new ShaderSocket("B", "B_Vector", ShaderDataType.VECTOR, new Vector3f(0.0f), 5),
		new ShaderSocket("A", "A_Color", ShaderDataType.RGBA, new Vector4f(0.5f, 0.5f, 0.5f, 1.0f), 6),
		new ShaderSocket("B", "B_Color", ShaderDataType.RGBA, new Vector4f(0.5f, 0.5f, 0.5f, 1.0f), 7),
		new ShaderSocket("A", "A_Rotation", ShaderDataType.ROTATION, 6),
		new ShaderSocket("B", "B_Rotation", ShaderDataType.ROTATION, 7)
	);

	public static final List<ShaderSocket> OUTPUTS = List.of(
		new ShaderSocket("Result", "Result_Float", ShaderDataType.VALUE, null, 0),
		new ShaderSocket("Result", "Result_Vector", ShaderDataType.VECTOR, null, 1),
		new ShaderSocket("Result", "Result_Color", ShaderDataType.RGBA, null, 2),
		new ShaderSocket("Result", "Result_Rotation", ShaderDataType.ROTATION, null, 3)
	);

	private final boolean clampFactor;
	private final boolean clampResult;
	private final FactorMode factorMode;
	private final BlendType blendType;
	private final ShaderDataType dataType;

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
		ShaderVariable factor;
		final ShaderVariable value1;
		final ShaderVariable value2;
		final ShaderVariable result;

		if (ShaderDataType.VALUE.equals(dataType)) {
			factor = variables.getInput(0);
			value1 = variables.getInput(2);
			value2 = variables.getInput(3);
			result = variables.getOutput(0);
		} else if (ShaderDataType.VECTOR.equals(dataType)) {
			if (FactorMode.UNIFORM.equals(factorMode)) {
				factor = variables.getInput(0);
			} else {
				factor = variables.getInput(1);
			}

			value1 = variables.getInput(4);
			value2 = variables.getInput(5);
			result = variables.getOutput(1);
		} else if (ShaderDataType.RGBA.equals(dataType)) {
			factor = variables.getInput(0);
			value1 = variables.getInput(6);
			value2 = variables.getInput(7);
			result = variables.getOutput(2);
		} else {
			throw new UnsupportedOperationException("dataType=%s".formatted(dataType));
		}

		if (clampFactor) {
			final var temporary = variables.getTemporary("factor", factor.type());

			writer.append(new VariableDeclaration(
				temporary.type().getCodeType(),
				temporary.name(),
				new FunctionCall(
					"clamp",
					new Identifier(temporary.name()),
					new Litteral("0.0"),
					new Litteral("1.0")
				)
			));

			factor = temporary;
		}

		if (!ShaderDataType.RGBA.equals(dataType)) {
			writer.append(new VariableDeclaration(
				result.type().getCodeType(),
				result.name(),
				new FunctionCall(
					"mix",
					new Identifier(value1.name()),
					new Identifier(value2.name()),
					new Identifier(factor.name())
				)
			));
		} else {
			writer.append(new VariableDeclaration(
				result.type().getCodeType(),
				result.name(),
				new FunctionCall(
					"vec4",
					new Identifier(value1.name())
				)
			));

			blendType.generateCode(
				writer,
				variables,
				new Identifier(value1.name()),
				new Identifier(value2.name()),
				new Identifier(factor.name()),
				new Identifier(result.name())
			);
		}
	}

	public enum FactorMode {
		UNIFORM,
		NON_UNIFORM;
	}

	@RequiredArgsConstructor
	public enum BlendType {

		MIX(true) {

			@Override
			public void generateCode(ShaderCodeWriter writer, Identifier value1, Identifier value2, Identifier factor, Identifier factorInverse, Identifier result, String componentName) {
				writer.append(new Expression(
					new BinaryOperation(
						new MemberAccess(result, componentName),
						"=",
						factorInverse,
						"*",
						new MemberAccess(value1, componentName),
						"+",
						factor,
						"*",
						new MemberAccess(value2, componentName)
					)
				));
			}

		}
		/* BLEND */,
		ADD(false) {

			@Override
			public void generateCode(ShaderCodeWriter writer, Identifier value1, Identifier value2, Identifier factor, Identifier factorInverse, Identifier result, String componentName) {
				writer.append(new Expression(
					new BinaryOperation(
						new MemberAccess(result, componentName),
						"+=",
						factor,
						"*",
						new MemberAccess(value2, componentName)
					)
				));
			}

		},
		MULTIPLY(true) {

			@Override
			public void generateCode(ShaderCodeWriter writer, Identifier value1, Identifier value2, Identifier factor, Identifier factorInverse, Identifier result, String componentName) {
				writer.append(new Expression(
					new BinaryOperation(
						new MemberAccess(result, componentName),
						"*=",
						factorInverse,
						"+",
						factor,
						"*",
						new MemberAccess(value2, componentName)
					)
				));
			}

		},
		// SUB,
		// SCREEN,
		// DIV,
		// DIFF,
		DARKEN(true) {

			@Override
			public void generateCode(ShaderCodeWriter writer, Identifier value1, Identifier value2, Identifier factor, Identifier factorInverse, Identifier result, String componentName) {
				writer.append(new Expression(
					new BinaryOperation(
						new MemberAccess(result, componentName),
						"=",
						new FunctionCall(
							"min",
							new MemberAccess(value1, componentName),
							new MemberAccess(value2, componentName)
						),
						"*",
						factor,
						"+",
						new MemberAccess(value1, componentName),
						"*",
						factorInverse
					)
				));
			}

		},
		// LIGHT,
		// OVERLAY,
		// DODGE,
		// BURN,
		// HUE,
		// SAT,
		// VAL,
		// COLOR,
		SOFT_LIGHT(true) /* SOFT */ {

			@Override
			public void generateCode(ShaderCodeWriter writer, Identifier value1, Identifier value2, Identifier factor, Identifier factorInverse, Identifier result, String componentName) {
				final var screen = new BinaryOperation(
					new Litteral("1.0"),
					"-",
					new Paranthesis(new BinaryOperation(
						new Litteral("1.0"),
						"-",
						new MemberAccess(value2, componentName)
					)),
					"*",
					new Paranthesis(new BinaryOperation(
						new Litteral("1.0"),
						"-",
						new MemberAccess(value1, componentName)
					))
				);

				writer.append(new Expression(
					new BinaryOperation(
						new MemberAccess(result, componentName),
						"=",
						factorInverse,
						"*",
						new MemberAccess(value1, componentName),
						"+",
						factor,
						"*",
						new Paranthesis(new BinaryOperation(
							new Paranthesis(new BinaryOperation(
								new Paranthesis(new BinaryOperation(
									new Litteral("1.0"),
									"-",
									new MemberAccess(value1, componentName)
								)),
								"*",
								new MemberAccess(value2, componentName),
								"*",
								new MemberAccess(value1, componentName)
							)),
							"+",
							new Paranthesis(new BinaryOperation(
								new MemberAccess(value1, componentName),
								"*",
								screen
							))
						))
					)
				));
			}

		};
		// LINEAR,
		// EXCLUSION;

		private final boolean requireFactorInverse;

		public abstract void generateCode(ShaderCodeWriter writer, Identifier value1, Identifier value2, Identifier factor, Identifier factorInverse, Identifier result, String componentName);

		public void generateCode(ShaderCodeWriter writer, ShaderVariables variables, Identifier value1, Identifier value2, Identifier factor, Identifier result) {
			Identifier factorInverse = null;
			if (requireFactorInverse) {
				final var temporary = variables.getTemporary("facm", ShaderDataType.VALUE);

				writer.append(new VariableDeclaration(
					temporary.type().getCodeType(),
					temporary.name(),
					new BinaryOperation(
						new Litteral("1.0"),
						"-",
						factor
					)
				));

				factorInverse = new Identifier(temporary.name());
			}

			for (final var componentName : Vectors.XYZ) {
				generateCode(writer, value1, value2, factor, factorInverse, result, componentName);
			}
		}

	}

}