package blender.shader.node.group;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.code.ast.CommentBlock;
import blender.shader.library.ShaderLibrary;
import blender.shader.node.ShaderNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString(callSuper = true)
@RequiredArgsConstructor
public class GroupShaderNode extends ShaderNode {

	private final String treeName;
	private @ToString.Exclude @Getter ShaderNodeGroup target;

	@Override
	public List<ShaderSocket> getInputs() {
		if (target != null) {
			return target.getInputs();
		}

		return Collections.emptyList();
	}

	@Override
	public List<ShaderSocket> getOutputs() {
		if (target != null) {
			return target.getOutputs();
		}

		return Collections.emptyList();
	}

	public Map.Entry<ShaderNode, ShaderSocket> getAtOutput(int index) {
		final var socket = getOutput(index);

		for (final var link : target.getOutputNode().getReverseLinks()) {
			if (link.toSocket().equals(socket)) {
				return link.from();
			}
		}

		throw new IllegalArgumentException("not found");
	}

	@Override
	public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
		writer.append(new CommentBlock(
			List.of(),
			"Tree Name: %s".formatted(treeName),
			null
		));

		final var childGenerator = writer.getCodeGenerator().createChild(target, variables);
		childGenerator.generate();
	}

	public GroupShaderNode link(ShaderLibrary library) {
		target = library.get(treeName);

		if (target == null) {
			throw new IllegalStateException("linkage failed, no target group found with name: %s".formatted(treeName));
		}

		return this;
	}

}