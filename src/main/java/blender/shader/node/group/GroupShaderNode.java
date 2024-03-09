package blender.shader.node.group;

import java.util.Collections;
import java.util.List;

import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
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

	@Override
	public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
		writer.comment("treeName: %s".formatted(treeName));
	}

	public void link(ShaderLibrary library) {
		target = library.get(treeName);
		if (target == null) {
			throw new IllegalStateException("linkage failed, no target group found with name: %s".formatted(treeName));
		}
	}

}