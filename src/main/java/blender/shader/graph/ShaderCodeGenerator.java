package blender.shader.graph;

import blender.shader.ShaderPort;
import blender.shader.ShaderVariable;
import blender.shader.node.ShaderNode;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class ShaderCodeGenerator {

    private final ShaderNodeGraph nodeGraph;
    private final Map<Map.Entry<ShaderNode, ShaderPort>, ShaderVariable> allocatedVariables = new HashMap<>();
    private final Set<String> allocatedVariableNames = new HashSet<>();
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
            final var variable = getOrAllocateVariable(link.fromNode(), link.fromPort());
            final var index = link.toPort().index();

            if (inputsArray[index] != null) {
                throw new IllegalStateException("already allocated port?");
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
                    port.type().renderDefaultValue(defaultValue) + comment,
                    port
            );

            inputsArray[index] = variable;
        }

        final var inputs = Arrays.asList(inputsArray);

        final var outputArray = new ShaderVariable[node.getOutputs().size()];
        for (int index = 0; index < outputArray.length; index++) {
            final var port = node.getOutputs().get(index);
            final var variable = getOrAllocateVariable(node, port);

            outputArray[index] = variable;
        }

        final var outputs = Arrays.asList(outputArray);

        final var builder = new StringBuilder();
        node.generateCode(builder, inputs, outputs);

        visitedNodes.add(node);
        return builder.toString();
    }

    public ShaderVariable getOrAllocateVariable(ShaderNode node, ShaderPort port) {
        final var key = Map.entry(node, port);
        var variable = allocatedVariables.get(key);

        if (variable == null) {
            final var baseName = "%s__%s".formatted(node.getName(), port.name())
                    .replace(" ", "_")
                    .replace(".", "_");

            String name;
            for (int index = 0; ; index++) {
                name = "%s__%s".formatted(baseName, index);
                if (!allocatedVariableNames.contains(name)) {
                    break;
                }
            }

            variable = new ShaderVariable(name, port);

            allocatedVariables.put(key, variable);
            allocatedVariableNames.add(name);
        }

        return variable;
    }

}