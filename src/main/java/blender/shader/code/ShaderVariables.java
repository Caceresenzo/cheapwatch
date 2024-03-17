package blender.shader.code;

import java.util.Collections;
import java.util.List;

import blender.shader.ShaderDataType;
import blender.shader.node.ShaderNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShaderVariables {

	private final @Getter ShaderNode node;
	private final List<ShaderVariable> inputs;
	private final List<ShaderVariable> outputs;
	private final ShaderVariableAllocator allocator;

	public ShaderVariable getInput(int index) {
		return inputs.get(index);
	}

	public List<ShaderVariable> getInputs() {
		return Collections.unmodifiableList(inputs);
	}

	public int getInputsCount() {
		return inputs.size();
	}

	public ShaderVariable getOutput(int index) {
		return outputs.get(index);
	}

	public List<ShaderVariable> getOutputs() {
		return Collections.unmodifiableList(outputs);
	}

	public int getOutputsCount() {
		return outputs.size();
	}

	public ShaderVariable getTemporary(String hint, ShaderDataType type) {
		return allocator.allocateTemporary(hint, type);
	}

}