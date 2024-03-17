package blender.shader.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import blender.shader.ShaderLink;
import blender.shader.code.ast.AstStatement;
import blender.shader.graph.ShaderNodeGraph;
import blender.shader.node.ShaderNode;
import blender.shader.node.group.GroupInputShaderNode;
import blender.shader.node.group.GroupShaderNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShaderCodeGenerator {

	private final @Getter ShaderNodeGraph nodeGraph;
	private final @Getter ShaderVariableAllocator variableAllocator;
	private final List<AstStatement> statements;
	private final ShaderVariables parentVariables;
	private final List<ShaderNode> visitedNodes = new ArrayList<>(); // nodes does not support hashCode

	public List<AstStatement> generate() {
		final var last = nodeGraph.getFinalNode().orElseThrow();

		followInReverse(last);

		return statements;
	}

	public void followInReverse(ShaderNode node) {
		for (final var link : node.getReverseLinks()) {
			final var fromNode = link.fromNode();
			followInReverse(fromNode);
			generate(fromNode);
		}

		generate(node);
	}

	public void generate(ShaderNode node) {
		if (visitedNodes.contains(node)) {
			return;
		}

		final var writer = new ShaderCodeWriter(statements, this);
		final var variables = allocateVariables(node);

		node.generateCode(writer, variables);

		visitedNodes.add(node);
	}

	public ShaderVariables allocateVariables(ShaderNode node) {
		final var inputsArray = new ShaderVariable[node.getInputs().size()];
		for (final var link : node.getReverseLinks()) {
			final var variable = getInputVariable(link);

			final var index = link.toIndex();
			if (inputsArray[index] != null) {
				throw new IllegalStateException("already allocated socket?");
			}

			inputsArray[index] = variable;
		}

		for (int index = 0; index < inputsArray.length; index++) {
			if (inputsArray[index] != null) {
				continue;
			}

			final var socket = node.getInputs().get(index);

			var defaultValue = node.getInputOverrides().get(socket);
			var comment = "";
			if (defaultValue == null) {
				defaultValue = socket.defaultValue();
				comment = "/*default*/";
			}

			final var variable = new ShaderVariable(
				socket.type().render(defaultValue) + comment,
				socket,
				false
			);

			inputsArray[index] = variable;
		}

		final var inputs = Arrays.asList(inputsArray);

		final var outputArray = new ShaderVariable[node.getOutputs().size()];
		for (final var link : node.getLinks()) {
			final var variable = variableAllocator.getOrAllocateSocket(link.fromNode(), link.fromSocket(), true);
			final var index = link.fromIndex();

			if (outputArray[index] != null) {
				continue;
			}

			outputArray[index] = variable;
		}

		for (int index = 0; index < outputArray.length; index++) {
			if (outputArray[index] != null) {
				continue;
			}

			final var socket = node.getOutputs().get(index);
			final var variable = variableAllocator.getOrAllocateSocket(node, socket, false);

			outputArray[index] = variable;
		}

		final var outputs = Arrays.asList(outputArray);

		return new ShaderVariables(node, inputs, outputs, variableAllocator);
	}

	public ShaderVariable getInputVariable(ShaderLink link) {
		var fromNode = link.fromNode();

		if (parentVariables != null && fromNode instanceof GroupInputShaderNode) {
			return parentVariables.getInput(link.fromIndex());
		}

		if (fromNode instanceof GroupShaderNode groupOutput) {
			final var nodeAndSocket = groupOutput.getAtOutput(link.fromIndex());

			return variableAllocator.getSocket(nodeAndSocket.getKey(), nodeAndSocket.getValue());
		}

		return variableAllocator.getOrAllocateSocket(fromNode, link.fromSocket(), true);
	}

	public ShaderCodeGenerator createChild(ShaderNodeGraph graph, ShaderVariables variables) {
		return new ShaderCodeGenerator(graph, variableAllocator, statements, variables);
	}

}