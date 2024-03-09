package blender.shader.node.converter;

import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4f;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariable;
import blender.shader.code.ShaderVariables;
import blender.shader.node.ShaderNode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class MixShaderNode extends ShaderNode {

	public static final List<ShaderSocket<?>> INPUTS = List.of(
		new ShaderSocket<>("Factor", "Factor_Float", ShaderDataType.VALUE, 0.5f, 0),
		new ShaderSocket<>("Factor", "Factor_Vector", ShaderDataType.VECTOR, new Vector3f(0.5f), 1),
		new ShaderSocket<>("A", "A_Float", ShaderDataType.VALUE, 0.0f, 2),
		new ShaderSocket<>("B", "B_Float", ShaderDataType.VALUE, 0.0f, 3),
		new ShaderSocket<>("A", "A_Vector", ShaderDataType.VECTOR, new Vector3f(0.0f), 4),
		new ShaderSocket<>("B", "B_Vector", ShaderDataType.VECTOR, new Vector3f(0.0f), 5),
		new ShaderSocket<>("A", "A_Color", ShaderDataType.RGBA, new Vector4f(0.5f, 0.5f, 0.5f, 1.0f), 6),
		new ShaderSocket<>("B", "B_Color", ShaderDataType.RGBA, new Vector4f(0.5f, 0.5f, 0.5f, 1.0f), 7),
		new ShaderSocket<>("A", "A_Rotation", ShaderDataType.ROTATION, 6),
		new ShaderSocket<>("B", "B_Rotation", ShaderDataType.ROTATION, 7)
	);

	public static final List<ShaderSocket<?>> OUTPUTS = List.of(
		new ShaderSocket<>("Result", "Result_Float", ShaderDataType.VALUE, null, 0),
		new ShaderSocket<>("Result", "Result_Vector", ShaderDataType.VECTOR, null, 1),
		new ShaderSocket<>("Result", "Result_Color", ShaderDataType.RGBA, null, 2),
		new ShaderSocket<>("Result", "Result_Rotation", ShaderDataType.ROTATION, null, 3)
	);

	private final boolean clampFactor;
	private final boolean clampResult;
	private final FactorMode factorMode;
	private final BlendType blendType;
	private final ShaderDataType<?> dataType;

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
		ShaderVariable<?> factor;
		final ShaderVariable<?> value1;
		final ShaderVariable<?> value2;

		if (ShaderDataType.VALUE.equals(dataType)) {
			factor = variables.getInput(0);
			value1 = variables.getInput(2);
			value2 = variables.getInput(3);
		} else if (ShaderDataType.VECTOR.equals(dataType)) {
			if (FactorMode.UNIFORM.equals(factorMode)) {
				factor = variables.getInput(0);
			} else {
				factor = variables.getInput(1);
			}

			value1 = variables.getInput(4);
			value2 = variables.getInput(5);
		} else if (ShaderDataType.RGBA.equals(dataType)) {
			factor = variables.getInput(0);
			value1 = variables.getInput(6);
			value2 = variables.getInput(7);
		} else {
			throw new UnsupportedOperationException("dataType=%s".formatted(dataType));
		}

		//        if (clampFactor) {
		//            final var temporary = variables.getTemporary("factor", factor.type());
		//
		//
		//            factor = temporary;
		//        }
		//
		//        final var floatResult = outputs.get(0);
		//        if (floatResult.linked()) {
		//            throw new UnsupportedOperationException("floatResult");
		//        }
		//
		//        final var vectorResult = outputs.get(1);
		//        if (vectorResult.linked()) {
		//            throw new UnsupportedOperationException("vectorResult");
		//        }
		//
		//        final var colorResult = outputs.get(2);
		//        if (colorResult.linked()) {
		//            throw new UnsupportedOperationException("colorResult");
		//        }
		//
		//        final var rotationResult = outputs.get(3);
		//        if (rotationResult.linked()) {
		//            throw new UnsupportedOperationException("rotationResult");
		//        }
		//
		//        System.out.println(outputs);
		throw new UnsupportedOperationException("no result");
	}

	public enum FactorMode {
		UNIFORM,
		NON_UNIFORM;
	}

	public enum BlendType {
		MIX /* BLEND */,
		ADD,
		MULTIPLY,
		// SUB,
		// SCREEN,
		// DIV,
		// DIFF,
		DARKEN,
		// LIGHT,
		// OVERLAY,
		// DODGE,
		// BURN,
		// HUE,
		// SAT,
		// VAL,
		// COLOR,
		SOFT_LIGHT,
		// LINEAR,
		// EXCLUSION;
	}

}