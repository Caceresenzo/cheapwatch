package blender.shader.node.converter;

import java.util.List;

import org.joml.Vector4f;

import blender.shader.ShaderDataType;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariable;
import blender.shader.code.ShaderVariables;
import blender.shader.code.ast.AstStatement;
import blender.shader.code.ast.CommentBlock;
import blender.shader.code.ast.Identifier;
import blender.shader.code.ast.MemberAccess;
import blender.shader.code.ast.VariableDeclaration;
import blender.shader.node.ShaderNode;
import lombok.ToString;

@ToString(callSuper = true)
public class SeparateColorShaderNode extends ShaderNode {

	public static final List<ShaderSocket> INPUTS = List.of(
		new ShaderSocket("Color", ShaderDataType.RGBA, new Vector4f(0.8f, 0.8f, 0.8f, 1.0f), 0)
	);

	public static final List<ShaderSocket> OUTPUTS = List.of(
		new ShaderSocket("Red", ShaderDataType.VALUE, 0),
		new ShaderSocket("Green", ShaderDataType.VALUE, 1),
		new ShaderSocket("Blue", ShaderDataType.VALUE, 2)
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
		final var color = variables.getInput(0);
		final var red = variables.getOutput(0);
		final var green = variables.getOutput(1);
		final var blue = variables.getOutput(2);

		writer.append(toAstNode(color, red, "x"));
		writer.append(toAstNode(color, green, "y"));
		writer.append(toAstNode(color, blue, "z"));
	}

	public AstStatement toAstNode(ShaderVariable input, ShaderVariable output, String component) {
		AstStatement block = new VariableDeclaration(
			output.type().getCodeType(),
			new Identifier(output.name()),
			new MemberAccess(
				new Identifier(input.name()),
				component
			)
		);

		if (!output.linked()) {
			block = new CommentBlock(
				List.of(block)
			);
		}

		return block;
	}

}