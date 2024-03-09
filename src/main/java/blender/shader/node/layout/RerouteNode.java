package blender.shader.node.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import blender.shader.ShaderSocket;
import blender.shader.code.ShaderCodeWriter;
import blender.shader.code.ShaderVariables;
import blender.shader.node.ShaderNode;
import lombok.ToString;

@ToString(callSuper = true)
public class RerouteNode extends ShaderNode {

	private final List<ShaderSocket> inputs = new ArrayList<>();
	private final List<ShaderSocket> outputs = new ArrayList<>();

	@Override
	public List<ShaderSocket> getInputs() {
		return Collections.unmodifiableList(inputs);
	}

	@Override
	public List<ShaderSocket> getOutputs() {
		return Collections.unmodifiableList(outputs);
	}

	@Override
	public void generateCode(ShaderCodeWriter writer, ShaderVariables variables) {
		writer.comment("reroute");
	}

	public RerouteNode addInput(ShaderSocket socket) {
		this.inputs.add(socket);
		return this;
	}

	public RerouteNode addOutput(ShaderSocket socket) {
		this.outputs.add(socket);
		return this;
	}

}