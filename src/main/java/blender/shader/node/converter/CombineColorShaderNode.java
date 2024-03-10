package blender.shader.node.converter;

import java.util.List;

import org.joml.Vector4f;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.code.ast.FunctionCall;
import blender.shader.code.ast.Identifier;
import blender.shader.code.ast.Litteral;
import blender.shader.code.ast.VariableDeclaration;
import blender.shader.node.ShaderNode;
import lombok.ToString;

@ToString(callSuper = true)
public class CombineColorShaderNode extends ShaderNode {

	public static final List<ShaderSocket> INPUTS = List.of(
		new ShaderSocket("Red", ShaderDataType.VALUE, 0.0f, 0),
		new ShaderSocket("Green", ShaderDataType.VALUE, 0.0f, 1),
		new ShaderSocket("Blue", ShaderDataType.VALUE, 0.0f, 2)
	);

	public static final List<ShaderSocket> OUTPUTS = List.of(
		new ShaderSocket("Color", ShaderDataType.RGBA, new Vector4f(0.8f, 0.8f, 0.8f, 1.0f), 0)
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
		final var red = variables.getInput(0);
		final var green = variables.getInput(1);
		final var blue = variables.getInput(2);
		final var result = variables.getOutput(0);

		final var block = new VariableDeclaration(
			result.type().getCodeType(),
			result.name(),
			new FunctionCall(
				"vec4",
				List.of(
					new Identifier(red.name()),
					new Identifier(green.name()),
					new Identifier(blue.name()),
					new Litteral("1.0")
				)
			)
		);

		writer.append(block);
	}

}