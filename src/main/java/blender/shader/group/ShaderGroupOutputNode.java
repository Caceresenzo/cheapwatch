package blender.shader.group;

import blender.shader.ShaderPort;
import blender.shader.ShaderVariable;
import blender.shader.node.ShaderNode;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ShaderGroupOutputNode extends ShaderNode {

    private final ShaderNodeGroup group;

    ShaderGroupOutputNode(ShaderNodeGroup group) {
        this.group = group;

        setName("Group Output");
    }

    @Override
    public List<ShaderPort> getInputs() {
        return group.getOutputs();
    }

    @Override
    public List<ShaderPort> getOutputs() {
        return Collections.emptyList();
    }

    @Override
    public void generateCode(StringBuilder builder, List<ShaderVariable> inputs, List<ShaderVariable> outputs) {
        builder.append("// outputs:\n");

        final var size = inputs.size();
        for (int index = 0; index < size; index++) {
            final var port = getInputs().get(index);
            final var variable = inputs.get(index);

            builder.append("// ")
                    .append(port.name())
                    .append(" = ")
                    .append(variable.name())
                    .append("\n");
        }
    }

}