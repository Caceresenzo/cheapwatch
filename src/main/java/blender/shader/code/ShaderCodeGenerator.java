package blender.shader.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import blender.shader.ShaderDataType;
import blender.shader.graph.ShaderNodeGraph;
import blender.shader.node.ShaderNode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShaderCodeGenerator {

    private final ShaderNodeGraph nodeGraph;
    private final ShaderVariableAllocator variableAllocator;
    private final List<ShaderNode> visitedNodes = new ArrayList<>(); // nodes does not support hashCode

    public String generate() {
        final var last = nodeGraph.getFinalNode().orElseThrow();

        final var lines = new ArrayList<String>();
        followInReverse(last, lines);

        while (lines.remove("")) {
            ;
        }

        return String.join("\n", lines);
    }

    public void followInReverse(ShaderNode node, List<String> lines) {
        for (final var link : node.getReverseLinks()) {
            final var fromNode = link.fromNode();
            followInReverse(fromNode, lines);
            lines.add(generate(fromNode));
        }

        lines.add(generate(node));
    }

    public String generate(ShaderNode node) {
        if (visitedNodes.contains(node)) {
            return "";
        }

        final var inputsArray = new ShaderVariable[node.getInputs().size()];
        for (final var link : node.getReverseLinks()) {
            final var variable = variableAllocator.getOrAllocateSocket(link.fromNode(), link.fromPort(), true);
            final var index = link.toPort().index();

            if (inputsArray[index] != null) {
                throw new IllegalStateException("already allocated socket?");
            }

            inputsArray[index] = variable;
        }

        for (int index = 0; index < inputsArray.length; index++) {
            if (inputsArray[index] != null) {
                continue;
            }

            final var port = node.getInputs().get(index);

            var defaultValue = node.getInputOverrides().get(port);
            var comment = "";
            if (defaultValue == null) {
                defaultValue = port.defaultValue();
                comment = "/*default*/";
            }

            final var variable = new ShaderVariable(
                    ((ShaderDataType) port.type()).render(defaultValue) + comment,
                    port,
                    false
            );

            inputsArray[index] = variable;
        }

        final var inputs = Arrays.asList(inputsArray);

        final var outputArray = new ShaderVariable[node.getOutputs().size()];
        for (final var link : node.getLinks()) {
            final var variable = variableAllocator.getOrAllocateSocket(link.fromNode(), link.fromPort(), true);
            final var index = link.fromPort().index();

            if (outputArray[index] != null) {
                continue;
            }

            outputArray[index] = variable;
        }

        for (int index = 0; index < outputArray.length; index++) {
            if (outputArray[index] != null) {
                continue;
            }

            final var port = node.getOutputs().get(index);
            final var variable = variableAllocator.getOrAllocateSocket(node, port, false);

            outputArray[index] = variable;
        }

        final var outputs = Arrays.asList(outputArray);

        final var builder = new StringBuilder();
        final var writer = new ShaderCodeWriter(builder);

        final var variables = new ShaderVariables(inputs, outputs, variableAllocator);

        node.generateCode(writer, variables);

        visitedNodes.add(node);
        return builder.toString();
    }

}