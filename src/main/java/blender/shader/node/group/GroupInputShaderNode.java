package blender.shader.node.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.code.ast.AstStatement;
import blender.shader.code.ast.CommentBlock;
import blender.shader.code.ast.FunctionCall;
import blender.shader.code.ast.Identifier;
import blender.shader.code.ast.VariableDeclaration;
import blender.shader.node.ShaderNode;
import lombok.ToString;

@ToString(callSuper = true)
public class GroupInputShaderNode extends ShaderNode {

	@ToString.Exclude
	private final ShaderNodeGroup group;

	GroupInputShaderNode(ShaderNodeGroup group) {
		this.group = group;

		setName("Group Input");
	}

	@Override
	public List<ShaderSocket> getInputs() {
		return Collections.emptyList();
	}

	@Override
	public List<ShaderSocket> getOutputs() {
		return group.getInputs();
	}

	@Override
	public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
		final var size = variables.getOutputsCount();
		final var statements = new ArrayList<AstStatement>(size);

		for (int index = 0; index < size; index++) {
			final var socket = getOutputs().get(index);
			final var variable = variables.getOutput(index);

			statements.add(
				new VariableDeclaration(
					variable.type().getCodeType(),
					new Identifier(variable.name()),
					new FunctionCall(
						"getSocket",
						List.of(
							new Identifier(socket.name())
						)
					)
				)
			);
		}

		final var block = new CommentBlock(
			statements,
			"inputs",
			"end of inputs"
		);

		writer.append(block);
	}

}