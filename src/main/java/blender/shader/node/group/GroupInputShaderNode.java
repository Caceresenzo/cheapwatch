package blender.shader.node.group;

import java.util.Collections;
import java.util.List;

import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
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
	public List<ShaderSocket<?>> getInputs() {
		return Collections.emptyList();
	}

	@Override
	public List<ShaderSocket<?>> getOutputs() {
		return group.getInputs();
	}

	@Override
	public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
		writer.comment("inputs:").endLine();

		final var size = variables.getOutputsCount();
		for (int index = 0; index < size; index++) {
			final var socket = getOutputs().get(index);
			final var variable = variables.getOutput(index);

			writer.comment("%s = %s".formatted(socket.name(), variable.name())).endLine();
		}
	}

}