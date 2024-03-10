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
public class GroupOutputShaderNode extends ShaderNode {

	@ToString.Exclude
	private final ShaderNodeGroup group;

	GroupOutputShaderNode(ShaderNodeGroup group) {
		this.group = group;

		setName("Group Output");
	}

	@Override
	public List<ShaderSocket> getInputs() {
		return group.getOutputs();
	}

	@Override
	public List<ShaderSocket> getOutputs() {
		return Collections.emptyList();
	}

	@Override
	public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
		final var size = variables.getInputsCount();
		final var statements = new ArrayList<AstStatement>(size);

		for (int index = 0; index < size; index++) {
			final var socket = getInputs().get(index);
			final var variable = variables.getInput(index);

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
			"outputs",
			"end of outputs"
		);

		writer.append(block);
	}

}