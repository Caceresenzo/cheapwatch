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
public class BsdfDiffuseShaderNode extends ShaderNode {

	public static final List<ShaderSocket> INPUTS = List.of(
		new ShaderSocket("Color", ShaderDataType.RGBA, new Vector4f(0.8f, 0.8f, 0.8f, 1.0f), 0),
		new ShaderSocket("Roughness", ShaderDataType.VALUE, 0.5f, 1),
		new ShaderSocket("Normal", ShaderDataType.VECTOR, 2),
		new ShaderSocket("Weight", ShaderDataType.VALUE, 3)
	);

	public static final List<ShaderSocket> OUTPUTS = List.of(
		new ShaderSocket("BSDF", ShaderDataType.SHADER, 0)
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