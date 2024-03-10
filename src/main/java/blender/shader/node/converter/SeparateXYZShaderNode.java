package blender.shader.node.converter;

import java.util.List;

import org.joml.Vector3f;

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
public class SeparateXYZShaderNode extends ShaderNode {

	public static final List<ShaderSocket> INPUTS = List.of(
		new ShaderSocket("Vector", ShaderDataType.VECTOR, new Vector3f(), 0)
	);

	public static final List<ShaderSocket> OUTPUTS = List.of(
		new ShaderSocket("X", ShaderDataType.VALUE, 0.0f, 0),
		new ShaderSocket("Y", ShaderDataType.VALUE, 0.0f, 1),
		new ShaderSocket("Z", ShaderDataType.VALUE, 0.0f, 2)
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
		final var vector = variables.getInput(0);
		final var x = variables.getOutput(0);
		final var y = variables.getOutput(1);
		final var z = variables.getOutput(2);

		writer.append(toAstNode(vector, x, "x"));
		writer.append(toAstNode(vector, y, "y"));
		writer.append(toAstNode(vector, z, "z"));
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