package blender.shader.node.group;

import java.util.Collections;
import java.util.List;

import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
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
	public List<ShaderSocket<?>> getInputs() {
		return group.getOutputs();
	}

	@Override
	public List<ShaderSocket<?>> getOutputs() {
		return Collections.emptyList();
	}

	@Override
	public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
		writer.comment("outputs:").endLine();

		final var size = variables.getInputsCount();
		for (int index = 0; index < size; index++) {
			final var socket = getInputs().get(index);
			final var variable = variables.getInput(index);

			writer.comment("%s = %s".formatted(socket.name(), variable.name())).endLine();
		}
	}

}