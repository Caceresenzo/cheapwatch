package blender.shader.group;

import blender.shader.ShaderPort;
import blender.shader.ShaderVariable;
import blender.shader.node.ShaderNode;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ShaderGroupInputNode extends ShaderNode {

    private final ShaderNodeGroup group;

    ShaderGroupInputNode(ShaderNodeGroup group) {
        this.group = group;

        setName("Group Input");
    }

    @Override
    public List<ShaderPort> getInputs() {
        return Collections.emptyList();
    }

    @Override
    public List<ShaderPort> getOutputs() {
        return group.getInputs();
    }

    @Override
    public void generateCode(StringBuilder builder, List<ShaderVariable> inputs, List<ShaderVariable> outputs) {
        builder.append("// inputs:\n");

        final var size = outputs.size();
        for (int index = 0; index < size; index++) {
            final var port = getOutputs().get(index);
            final var variable = outputs.get(index);

            builder.append("// ")
                    .append(port.name())
                    .append(" = ")
                    .append(variable.name())
                    .append("\n");
        }
    }

}