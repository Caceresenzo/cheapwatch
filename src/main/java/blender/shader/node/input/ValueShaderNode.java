package blender.shader.node.input;

import java.util.Collections;
import java.util.List;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.code.ast.Identifier;
import blender.shader.code.ast.Litteral;
import blender.shader.code.ast.VariableDeclaration;
import blender.shader.node.ShaderNode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class ValueShaderNode extends ShaderNode {

	public static final List<ShaderSocket> OUTPUTS = List.of(
		new ShaderSocket("Value", ShaderDataType.VALUE, 0)
	);

	private final float value;

	public ValueShaderNode() {
		this(0.0f);
	}

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

		final var block = new VariableDeclaration(
			x.type().getCodeType(),
			new Identifier(x.name()),
			new Litteral(x.type().render(value))
		);

		writer.append(block);
	}

}