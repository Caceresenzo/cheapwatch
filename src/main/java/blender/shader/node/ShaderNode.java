package blender.shader.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blender.shader.ShaderLink;
import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public abstract class ShaderNode {

	private String name;
	private final Map<ShaderSocket, Object> inputOverrides = new HashMap<>();
	private final List<ShaderLink> links = new ArrayList<>();
	private final List<ShaderLink> reverseLinks = new ArrayList<>();

	@ToString.Include
	public abstract List<ShaderSocket> getInputs();

	@ToString.Include
	public abstract List<ShaderSocket> getOutputs();

	public abstract void generateCode(ShaderCodeWriter writer, ShaderVariables variables);

	public ShaderNode addInputOverrides(ShaderSocket socket, Object defaultValue) {
		inputOverrides.put(socket, defaultValue);

		return this;
	}

	public ShaderNode addLink(ShaderSocket fromSocket, ShaderNode toNode, ShaderSocket toSocket) {
		final var link = new ShaderLink(
			this,
			fromSocket,
			toNode,
			toSocket
		);

		this.links.add(link);
		toNode.reverseLinks.add(link);

		return this;
	}

	public ShaderSocket getInput(int index) {
		return getInputs().get(index);
	}

	public ShaderSocket getOutput(int index) {
		return getOutputs().get(index);
	}

}