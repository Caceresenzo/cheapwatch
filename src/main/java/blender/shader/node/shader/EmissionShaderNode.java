package blender.shader.node.shader;

import java.util.List;

import org.joml.Vector4f;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.node.ShaderNode;
import lombok.ToString;

@ToString(callSuper = true)
public class EmissionShaderNode extends ShaderNode {

	public static final List<ShaderSocket<?>> INPUTS = List.of(
		new ShaderSocket<>("Color", ShaderDataType.RGBA, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), 0),
		new ShaderSocket<>("Strength", ShaderDataType.VALUE, 1.0f, 1),
		new ShaderSocket<>("Weight", ShaderDataType.VALUE, 2)
	);

	public static final List<ShaderSocket<?>> OUTPUTS = List.of(
		new ShaderSocket<>("Emission", ShaderDataType.SHADER, 0)
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
		throw new UnsupportedOperationException();
	}

}