package blender.shader.node.shader;

import java.util.List;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.node.ShaderNode;
import lombok.ToString;

@ToString(callSuper = true)
public class ShaderMixShaderNode extends ShaderNode {

	public static final List<ShaderSocket> INPUTS = List.of(
		new ShaderSocket("Fac", ShaderDataType.VALUE, 0.5f, 0),
		new ShaderSocket("Shader", ShaderDataType.SHADER, 1),
		new ShaderSocket("Shader", "Shader_001", ShaderDataType.SHADER, 2)
	);

	public static final List<ShaderSocket> OUTPUTS = List.of(
		new ShaderSocket("Shader", ShaderDataType.SHADER, 0)
	);

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
		throw new UnsupportedOperationException();
	}

}