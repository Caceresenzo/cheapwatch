package blender.shader.node.input;

import java.util.Collections;
import java.util.List;

import org.joml.Vector3fc;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.node.ShaderNode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class VectorShaderNode extends ShaderNode {

	public static final List<ShaderSocket> OUTPUTS = List.of(
		new ShaderSocket("Vector", ShaderDataType.VECTOR, 0)
	);

	private final Vector3fc value;

	@Override
	public List<ShaderSocket> getInputs() {
		return Collections.emptyList();
	}

	@Override
	public List<ShaderSocket> getOutputs() {
		return OUTPUTS;
	}

	@Override
	public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
		final var x = variables.getOutput(0);

		writer
			.declareAndAssign(x)
			.value(x.type(), value)
			.endLine();
	}

}