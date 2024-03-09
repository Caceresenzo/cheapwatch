package blender.shader.node.converter;

import java.util.List;

import org.joml.Vector3f;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariable;
import blender.shader.code.ShaderVariables;
import blender.shader.node.ShaderNode;
import lombok.ToString;

@ToString(callSuper = true)
public class SeparateXYZShaderNode extends ShaderNode {

	public static final List<ShaderSocket<?>> INPUTS = List.of(
		new ShaderSocket<>("Vector", ShaderDataType.VECTOR, new Vector3f(), 0)
	);

	public static final List<ShaderSocket<?>> OUTPUTS = List.of(
		new ShaderSocket<>("X", ShaderDataType.VALUE, 0.0f, 0),
		new ShaderSocket<>("Y", ShaderDataType.VALUE, 0.0f, 1),
		new ShaderSocket<>("Z", ShaderDataType.VALUE, 0.0f, 2)
	);

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
		final var vector = variables.getInput(0);
		final var x = variables.getOutput(0);
		final var y = variables.getOutput(1);
		final var z = variables.getOutput(2);

		generateCode(writer, vector, x, "x");
		generateCode(writer, vector, y, "y");
		generateCode(writer, vector, z, "z");
	}

	public void generateCode(ShaderCodeWriter writer, ShaderVariable input, ShaderVariable output, String outputComponent) {
		if (!output.linked()) {
			return;
		}

		writer
			.declareAndAssign(output)
			.value(input)
			.field(outputComponent)
			.endLine();
	}

}