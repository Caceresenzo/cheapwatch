package blender.shader.node.group;

import blender.shader.ShaderSocket;
import blender.shader.code.ShaderVariable;
import blender.shader.node.ShaderNode;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@ToString(callSuper = true)
public class GroupOutputShaderNode extends ShaderNode {

    @ToString.Exclude
    private final ShaderNodeGroup group;

    GroupOutputShaderNode(ShaderNodeGroup group) {
        this.group = group;

        setName("Group Output");
    }

    @Override
    public List<ShaderSocket<?>> getInputs() {
        return group.getOutputs();
    }

    @Override
    public List<ShaderSocket<?>> getOutputs() {
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