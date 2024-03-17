package blender.shader.node.input;

import java.util.Collections;
import java.util.List;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.code.ast.Identifier;
import blender.shader.code.ast.IndexAccess;
import blender.shader.code.ast.MemberAccess;
import blender.shader.code.ast.VariableDeclaration;
import blender.shader.node.ShaderNode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class VertexColorShaderNode extends ShaderNode {

	public static final List<ShaderSocket> OUTPUTS = List.of(
		new ShaderSocket("Color", ShaderDataType.RGBA, 0),
		new ShaderSocket("Alpha", ShaderDataType.VALUE, 1)
	);

	private final String layerName;

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
		final var temporary = variables.getTemporary("color", ShaderDataType.RGBA);

		final var color = variables.getOutput(0);
		final var alpha = variables.getOutput(1);

		writer.append(new VariableDeclaration(
			temporary.type().getCodeType(),
			temporary.name(),
			new IndexAccess(
				new Identifier("in_ColorAttributes"),
				new Identifier("\"%s\"".formatted(layerName)) // TODO Convert to index
			)
		));

		writer.append(new VariableDeclaration(
			color.type().getCodeType(),
			color.name(),
			new MemberAccess(
				new Identifier(temporary.name()),
				"rgb"
			)
		));

		writer.append(new VariableDeclaration(
			alpha.type().getCodeType(),
			alpha.name(),
			new MemberAccess(
				new Identifier(temporary.name()),
				"a"
			)
		));
	}

}