package blender.shader.node.input;

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
public class FresnelShaderNode extends ShaderNode {

	public static final List<ShaderSocket> INPUTS = List.of(
		new ShaderSocket("IOR", ShaderDataType.VALUE, 1.5f, 0),
		new ShaderSocket("Normal", ShaderDataType.VECTOR, 1)
	);

	public static final List<ShaderSocket> OUTPUTS = List.of(
		new ShaderSocket("Fac", ShaderDataType.VALUE, 0)
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