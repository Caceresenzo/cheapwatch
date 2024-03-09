package blender.shader.node.converter;

import java.util.List;

import org.joml.Vector3f;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.node.ShaderNode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class VectorTransformShaderNode extends ShaderNode {

	public static final List<ShaderSocket> INPUTS = List.of(
		new ShaderSocket("Vector", ShaderDataType.VECTOR, new Vector3f(0.5f, 0.5f, 0.5f), 0)
	);

	public static final List<ShaderSocket> OUTPUTS = List.of(
		new ShaderSocket("Vector", ShaderDataType.VECTOR, 0)
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